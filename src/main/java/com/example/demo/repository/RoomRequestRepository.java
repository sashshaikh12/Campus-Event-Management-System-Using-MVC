package com.example.demo.repository;

import com.example.demo.model.RoomRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRequestRepository extends JpaRepository<RoomRequest, String> {
    List<RoomRequest> findByRequestedBy_Id(String clubHeadId);
    List<RoomRequest> findByStatus(String status);
}
