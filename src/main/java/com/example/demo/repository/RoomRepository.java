package com.example.demo.repository;

import com.example.demo.model.Room;
import com.example.demo.model.RoomManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
    List<Room> findByManager(RoomManager manager);
    List<Room> findByCapacityGreaterThanEqual(int capacity);
    List<Room> findByHasProjectorAndHasAirConditioner(boolean hasProjector, boolean hasAirConditioner);
    Room findByRoomNumber(String roomNumber);
} 