package com.taskmanager.task_manager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // ✅ Add this — fixes the red error in TaskService
    Page<Task> findByUser(User user, Pageable pageable);
}