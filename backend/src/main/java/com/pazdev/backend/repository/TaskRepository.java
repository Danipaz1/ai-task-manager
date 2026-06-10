package com.pazdev.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pazdev.backend.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    List<Task> findByUserId(Long userId);

    List<Task> findByStatus(String status);

    List<Task> findByTitleContaining(String title);

    List<Task> findByTitleContainingAndStatus(String title, String status);

    void deleteById(Long id);

}