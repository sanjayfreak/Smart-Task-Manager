package com.taskmanager.task_manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.taskmanager.task_manager.TaskRepository;
import com.taskmanager.task_manager.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin
public class TaskController {

    @Autowired
    private TaskRepository repo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskService service;

    @Autowired
    private JwtUtil jwtUtil; // ✅ FIX: inject this




    // ✅ SECURE POST
    @PostMapping
    public Task create(
            @RequestHeader("Authorization") String header,
            @RequestBody TaskDTO dto) {

        String token = header.substring(7);
        String username = jwtUtil.extractUsername(token);

        return service.create(dto, username);
    }
    @GetMapping
    public Page<Task> get(
            @RequestHeader("Authorization") String header,
            Pageable pageable) {

        String token = header.substring(7);
        String username = jwtUtil.extractUsername(token);

        return service.getTasksByUsername(username, pageable);
    }
    // ✅ ADD THIS - status update endpoint
    @PatchMapping("/{id}/status")
    public Task updateStatus(
            @RequestHeader("Authorization") String header,
            @PathVariable Long id,
            @RequestBody TaskDTO dto) {

        String username = jwtUtil.extractUsername(header.substring(7));
        return service.updateStatus(id, dto.getStatus(), username);
    }

    @DeleteMapping("/{id}")
    public String delete(
            @RequestHeader("Authorization") String header,
            @PathVariable Long id) {

        String username = jwtUtil.extractUsername(header.substring(7));
        service.deleteTask(id, username);
        return "Deleted successfully";
    }
    }
