package com.example.demo.controller;

import com.example.demo.model.Appeal;
import com.example.demo.model.EventRequest;
import com.example.demo.model.HOD;
import com.example.demo.model.command.ApproveAppealCommand;
import com.example.demo.model.command.Command;
import com.example.demo.model.command.RejectAppealCommand;
import com.example.demo.model.service.AppealService;
import com.example.demo.model.service.EventRequestService;
import com.example.demo.repository.AppealRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/hod")
public class HODController {

    @Autowired
    private EventRequestService eventRequestService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AppealService appealService;
    
    @Autowired
    private AppealRepository appealRepository;
    
    // Using a fixed HOD ID for simplicity
    private HOD getCurrentHOD() {
        try {
            // Fixed HOD ID: user-201 (Prof. Williams)
            Optional<HOD> hodOpt = userRepository.findById("user-201")
                    .filter(user -> user instanceof HOD)
                    .map(user -> (HOD) user);
            
            return hodOpt.orElseThrow(() -> new RuntimeException("Fixed HOD with ID user-201 not found"));
        } catch (Exception e) {
            System.err.println("Error getting current HOD: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            HOD hod = getCurrentHOD();
            
            // Get pending event requests for the HOD's department
            List<EventRequest> pendingRequests = eventRequestService.getPendingEventRequestsByDepartment(hod.getDepartment());
            
            model.addAttribute("hod", hod);
            model.addAttribute("pendingRequestsCount", pendingRequests.size());
            model.addAttribute("department", hod.getDepartment());
            
            return "hod/dashboard";
        } catch (Exception e) {
            System.err.println("Error in dashboard: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while loading the dashboard");
            return "error";
        }
    }

    @GetMapping("/event-requests")
    public String eventRequests(Model model, 
                               @RequestParam(required = false) String status) {
        try {
            System.out.println("Starting eventRequests method");
            HOD hod = getCurrentHOD();
            System.out.println("HOD Department: " + hod.getDepartment());
            
            List<EventRequest> requests;
            
            // If status is provided, filter by status, otherwise get all for the department
            if (status != null && !status.isEmpty()) {
                System.out.println("Filtering by status: " + status);
                requests = eventRequestService.getEventRequestsByStatus(status);
                
                // Filter by department (this is a simple in-memory filter since we don't have a combined query method)
                requests = requests.stream()
                    .filter(req -> req.getEvent().getDepartment().equals(hod.getDepartment()))
                    .toList();
            } else {
                System.out.println("Getting all requests for department: " + hod.getDepartment());
                requests = eventRequestService.getEventRequestsByDepartment(hod.getDepartment());
            }
            
            System.out.println("Found " + requests.size() + " event requests");
            
            model.addAttribute("hod", hod);
            model.addAttribute("eventRequests", requests);
            model.addAttribute("selectedStatus", status);
            
            System.out.println("Completed eventRequests method");
            return "hod/event-requests";
        } catch (Exception e) {
            System.err.println("Error in eventRequests: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while loading event requests");
            return "error";
        }
    }

    @PostMapping("/event-requests/{id}/approve")
    public String approveEvent(@PathVariable("id") String id, 
                              @RequestParam(value = "comments", required = false) String comments) {
        try {
            if (comments == null) {
                comments = "Approved";
            }
            
            boolean success = eventRequestService.approveEventRequest(id, comments);
            
            if (success) {
                System.out.println("Successfully approved event request: " + id);
            } else {
                System.err.println("Failed to approve event request: " + id);
            }
            
            return "redirect:/hod/event-requests";
        } catch (Exception e) {
            System.err.println("Error in approveEvent: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/hod/event-requests?error=true";
        }
    }

    @PostMapping("/event-requests/{id}/reject")
    public String rejectEvent(@PathVariable("id") String id, 
                             @RequestParam("reason") String reason) {
        try {
            boolean success = eventRequestService.rejectEventRequest(id, reason);
            
            if (success) {
                System.out.println("Successfully rejected event request: " + id);
            } else {
                System.err.println("Failed to reject event request: " + id);
            }
            
            return "redirect:/hod/event-requests";
        } catch (Exception e) {
            System.err.println("Error in rejectEvent: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/hod/event-requests?error=true";
        }
    }

    @GetMapping("/appeals")
    public String appeals(Model model) {
        try {
            HOD hod = getCurrentHOD();
            
            // Get appeals for this HOD
            List<Appeal> pendingAppeals = appealRepository.findByHodIdAndStatusOrderBySubmissionTimeAsc(hod.getId(), "PENDING");
            List<Appeal> resolvedAppeals = appealRepository.findByHodIdAndIsResolvedOrderBySubmissionTimeDesc(hod.getId(), true);
            
            model.addAttribute("hod", hod);
            model.addAttribute("pendingAppeals", pendingAppeals);
            model.addAttribute("resolvedAppeals", resolvedAppeals);
            model.addAttribute("pendingCount", pendingAppeals.size());
            model.addAttribute("resolvedCount", resolvedAppeals.size());
            
            return "hod/appeals";
        } catch (Exception e) {
            System.err.println("Error in appeals: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while loading appeals");
            return "error";
        }
    }
    
    @PostMapping("/appeals/{id}/approve")
    public String approveAppeal(@PathVariable("id") Long id, 
                               @RequestParam(value = "response", required = false) String response, 
                               RedirectAttributes redirectAttributes) {
        try {
            if (response == null || response.trim().isEmpty()) {
                response = "Your appeal has been approved.";
            }
            
            // Get the appeal by ID
            Optional<Appeal> appealOpt = appealRepository.findById(id);
            if (appealOpt.isPresent()) {
                Appeal appeal = appealOpt.get();
                
                // Use command pattern to approve the appeal
                Command approveCommand = new ApproveAppealCommand(appeal, response);
                approveCommand.execute();
                
                // Save the updated appeal
                appealRepository.save(appeal);
                
                redirectAttributes.addFlashAttribute("successMessage", "Appeal was successfully approved.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Appeal not found with ID: " + id);
            }
            
            return "redirect:/hod/appeals";
        } catch (Exception e) {
            System.err.println("Error in approveAppeal: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while approving the appeal.");
            return "redirect:/hod/appeals";
        }
    }
    
    @PostMapping("/appeals/{id}/reject")
    public String rejectAppeal(@PathVariable("id") Long id, 
                              @RequestParam("reason") String reason, 
                              RedirectAttributes redirectAttributes) {
        try {
            // Get the appeal by ID
            Optional<Appeal> appealOpt = appealRepository.findById(id);
            if (appealOpt.isPresent()) {
                Appeal appeal = appealOpt.get();
                
                // Use command pattern to reject the appeal
                Command rejectCommand = new RejectAppealCommand(appeal, reason);
                rejectCommand.execute();
                
                // Save the updated appeal
                appealRepository.save(appeal);
                
                redirectAttributes.addFlashAttribute("successMessage", "Appeal was successfully rejected.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Appeal not found with ID: " + id);
            }
            
            return "redirect:/hod/appeals";
        } catch (Exception e) {
            System.err.println("Error in rejectAppeal: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while rejecting the appeal.");
            return "redirect:/hod/appeals";
        }
    }
    
    @PostMapping("/appeals/{id}/request-info")
    public String requestMoreInfo(@PathVariable("id") Long id, 
                                 @RequestParam("message") String message, 
                                 RedirectAttributes redirectAttributes) {
        try {
            // Get the appeal by ID
            Optional<Appeal> appealOpt = appealRepository.findById(id);
            if (appealOpt.isPresent()) {
                Appeal appeal = appealOpt.get();
                
                // Update the appeal with the request for more info
                appeal.setHodComments(message);
                appeal.setStatus("INFO_REQUESTED");
                appeal.setResponseDate(LocalDateTime.now());
                
                // Save the updated appeal
                appealRepository.save(appeal);
                
                redirectAttributes.addFlashAttribute("successMessage", 
                    "Request for additional information has been sent to the student.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Appeal not found with ID: " + id);
            }
            
            return "redirect:/hod/appeals";
        } catch (Exception e) {
            System.err.println("Error in requestMoreInfo: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", 
                "An error occurred while requesting additional information.");
            return "redirect:/hod/appeals";
        }
    }
    
    @GetMapping("/profile")
    public String profile(Model model) {
        try {
            HOD hod = getCurrentHOD();
            model.addAttribute("hod", hod);
            return "hod/profile";
        } catch (Exception e) {
            System.err.println("Error in profile: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while loading profile");
            return "error";
        }
    }
}