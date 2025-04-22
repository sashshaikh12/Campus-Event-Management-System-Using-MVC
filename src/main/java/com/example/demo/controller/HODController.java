package com.example.demo.controller;

import com.example.demo.model.Appeal;
import com.example.demo.model.EventRequest;
import com.example.demo.model.HOD;
import com.example.demo.model.command.ApproveAppealCommand;
import com.example.demo.model.command.ApproveEventCommand;
import com.example.demo.model.command.Command;
import com.example.demo.model.command.RejectAppealCommand;
import com.example.demo.model.command.RejectEventCommand;
import com.example.demo.model.service.HODService;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/hod")
public class HODController {

    @Autowired
    private HODService hodService;
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // For demo purposes, we'll assume a HOD with id "3"
        Optional<HOD> hodOpt = userRepository.findById("3")
                .map(user -> (HOD) user);
        
        if (hodOpt.isPresent()) {
            HOD hod = hodOpt.get();
            List<EventRequest> pendingRequests = hodService.getPendingEventRequests(hod);
            List<Appeal> pendingAppeals = hodService.getPendingAppeals(hod);
            
            model.addAttribute("hod", hod);
            model.addAttribute("pendingRequests", pendingRequests);
            model.addAttribute("pendingAppeals", pendingAppeals);
            
            return "hod/dashboard";
        }
        
        return "redirect:/login";
    }
    
    @GetMapping("/event-requests")
    public String listEventRequests(Model model) {
        Optional<HOD> hodOpt = userRepository.findById("3")
                .map(user -> (HOD) user);
        
        if (hodOpt.isPresent()) {
            HOD hod = hodOpt.get();
            List<EventRequest> pendingRequests = hodService.getPendingEventRequests(hod);
            
            model.addAttribute("requests", pendingRequests);
            return "hod/event-requests";
        }
        
        return "redirect:/login";
    }
    
    @GetMapping("/appeals")
    public String listAppeals(Model model) {
        Optional<HOD> hodOpt = userRepository.findById("3")
                .map(user -> (HOD) user);
        
        if (hodOpt.isPresent()) {
            HOD hod = hodOpt.get();
            List<Appeal> pendingAppeals = hodService.getPendingAppeals(hod);
            
            model.addAttribute("appeals", pendingAppeals);
            return "hod/appeals";
        }
        
        return "redirect:/login";
    }
    
    @GetMapping("/event-requests/{requestId}")
    public String viewEventRequest(@PathVariable String requestId, Model model) {
        Optional<HOD> hodOpt = userRepository.findById("3")
                .map(user -> (HOD) user);
        
        if (hodOpt.isPresent()) {
            HOD hod = hodOpt.get();
            Optional<EventRequest> requestOpt = hod.getPendingRequests().stream()
                    .filter(req -> req.getId().equals(requestId))
                    .findFirst();
            
            if (requestOpt.isPresent()) {
                model.addAttribute("request", requestOpt.get());
                return "hod/event-request-details";
            }
        }
        
        return "redirect:/hod/event-requests";
    }
    
    @PostMapping("/event-requests/{requestId}/approve")
    public String approveEventRequest(@PathVariable String requestId, 
                                     @RequestParam String comments) {
        
        Optional<HOD> hodOpt = userRepository.findById("3")
                .map(user -> (HOD) user);
        
        if (hodOpt.isPresent()) {
            HOD hod = hodOpt.get();
            Optional<EventRequest> requestOpt = hod.getPendingRequests().stream()
                    .filter(req -> req.getId().equals(requestId))
                    .findFirst();
            
            if (requestOpt.isPresent()) {
                EventRequest request = requestOpt.get();
                
                // Using Command pattern
                Command command = new ApproveEventCommand(request, comments);
                hodService.processRequest(command);
                
                return "redirect:/hod/event-requests";
            }
        }
        
        return "redirect:/hod/dashboard";
    }
    
    @PostMapping("/event-requests/{requestId}/reject")
    public String rejectEventRequest(@PathVariable String requestId, 
                                    @RequestParam String comments) {
        
        Optional<HOD> hodOpt = userRepository.findById("3")
                .map(user -> (HOD) user);
        
        if (hodOpt.isPresent()) {
            HOD hod = hodOpt.get();
            Optional<EventRequest> requestOpt = hod.getPendingRequests().stream()
                    .filter(req -> req.getId().equals(requestId))
                    .findFirst();
            
            if (requestOpt.isPresent()) {
                EventRequest request = requestOpt.get();
                
                // Using Command pattern
                Command command = new RejectEventCommand(request, comments);
                hodService.processRequest(command);
                
                return "redirect:/hod/event-requests";
            }
        }
        
        return "redirect:/hod/dashboard";
    }
    
    @GetMapping("/appeals/{appealId}")
    public String viewAppeal(@PathVariable String appealId, Model model) {
        Optional<HOD> hodOpt = userRepository.findById("3")
                .map(user -> (HOD) user);
        
        if (hodOpt.isPresent()) {
            HOD hod = hodOpt.get();
            Optional<Appeal> appealOpt = hod.getPendingAppeals().stream()
                    .filter(appeal -> appeal.getId().equals(appealId))
                    .findFirst();
            
            if (appealOpt.isPresent()) {
                model.addAttribute("appeal", appealOpt.get());
                return "hod/appeal-details";
            }
        }
        
        return "redirect:/hod/appeals";
    }
    
    @PostMapping("/appeals/{appealId}/approve")
    public String approveAppeal(@PathVariable String appealId, 
                               @RequestParam String comments) {
        
        Optional<HOD> hodOpt = userRepository.findById("3")
                .map(user -> (HOD) user);
        
        if (hodOpt.isPresent()) {
            HOD hod = hodOpt.get();
            Optional<Appeal> appealOpt = hod.getPendingAppeals().stream()
                    .filter(appeal -> appeal.getId().equals(appealId))
                    .findFirst();
            
            if (appealOpt.isPresent()) {
                Appeal appeal = appealOpt.get();
                
                // Using Command pattern
                Command command = new ApproveAppealCommand(appeal, comments);
                hodService.processRequest(command);
                
                return "redirect:/hod/appeals";
            }
        }
        
        return "redirect:/hod/dashboard";
    }
    
    @PostMapping("/appeals/{appealId}/reject")
    public String rejectAppeal(@PathVariable String appealId, 
                              @RequestParam String comments) {
        
        Optional<HOD> hodOpt = userRepository.findById("3")
                .map(user -> (HOD) user);
        
        if (hodOpt.isPresent()) {
            HOD hod = hodOpt.get();
            Optional<Appeal> appealOpt = hod.getPendingAppeals().stream()
                    .filter(appeal -> appeal.getId().equals(appealId))
                    .findFirst();
            
            if (appealOpt.isPresent()) {
                Appeal appeal = appealOpt.get();
                
                // Using Command pattern
                Command command = new RejectAppealCommand(appeal, comments);
                hodService.processRequest(command);
                
                return "redirect:/hod/appeals";
            }
        }
        
        return "redirect:/hod/dashboard";
    }
} 