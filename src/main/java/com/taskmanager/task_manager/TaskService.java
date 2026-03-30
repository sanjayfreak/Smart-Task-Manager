package com.taskmanager.task_manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public Task create(TaskDTO dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus("PENDING");   // ✅ always start as PENDING
        task.setUser(user);

        return taskRepository.save(task);
    }

    public Page<Task> getTasksByUsername(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return taskRepository.findByUser(user, pageable);
    }

    // ✅ New: update status
    public Task updateStatus(Long id, String status, String username) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Access denied");
        }

        task.setStatus(status);
        return taskRepository.save(task);
    }

    // ✅ New: delete own task
    public void deleteTask(Long id, String username) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Access denied - not your task");
        }

        taskRepository.deleteById(id);
    }
}