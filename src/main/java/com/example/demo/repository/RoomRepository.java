package com.example.demo.repository;

import com.example.demo.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
    List<Room> findByBuilding(String building);
    List<Room> findByCapacityGreaterThanEqual(int capacity);
    List<Room> findByHasProjector(boolean hasProjector);
    List<Room> findByHasAirConditioner(boolean hasAirConditioner);
} 