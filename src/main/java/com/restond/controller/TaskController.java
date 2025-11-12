package com.restond.controller;

import com.restond.entity.Task;
import com.restond.entity.TaskStatus;
import com.restond.entity.User;
import com.restond.service.TaskService;
import com.restond.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/TaskManagerSystem")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @PostMapping("/tasks")
    public ResponseEntity<?> createTask(@Valid @RequestBody Task task) {
        User user = getCurrentUser();
        task.setUserId(user.getId());
        Task newTask = taskService.createTask(task);
        return new ResponseEntity<>(newTask, HttpStatus.CREATED);
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
        User user = getCurrentUser();

        if (!taskService.validateTaskOwnership(user.getId(), taskId)) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "无权操作此任务");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        taskService.deleteTask(taskId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable Long taskId, @Valid @RequestBody Task task) {
        User user = getCurrentUser();

        if (!taskService.validateTaskOwnership(user.getId(), taskId)) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "无权操作此任务");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        task.setId(taskId);
        Task updatedTask = taskService.updateTask(task);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @PatchMapping("/tasks/{taskId}/status/{status}")
    public ResponseEntity<?> updatedTaskStatus(@PathVariable Long taskId, @PathVariable TaskStatus status) {
        User user = getCurrentUser();

        if (!taskService.validateTaskOwnership(user.getId(), taskId)) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "无权操作此任务");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        return ResponseEntity.ok(taskService.updateTaskStatus(taskId, status));
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<?> findTaskById(@PathVariable Long taskId) {
        User user = getCurrentUser();

        if (!taskService.validateTaskOwnership(user.getId(), taskId)) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "无权操作此任务");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        return ResponseEntity.ok(taskService.findTaskById(taskId));
    }

    @GetMapping("/tasks")
    public ResponseEntity<?> findTasksByUserId() {
        User user = getCurrentUser();
        return ResponseEntity.ok(taskService.findByUserId(user.getId()));
    }

    @GetMapping("/tasks/status/{status}")
    public ResponseEntity<?> findTasksByUserIdAndStatus(@PathVariable TaskStatus status) {
        User user = getCurrentUser();
        return ResponseEntity.ok(taskService.findByUserIdAndStatus(user.getId(), status));
    }

    @GetMapping("/tasks/priority/{priority}")
    public ResponseEntity<?> findTasksByUserIdAndPriority(@PathVariable int priority) {
        User user = getCurrentUser();
        return ResponseEntity.ok(taskService.findByUserIdAndPriority(user.getId(), priority));
    }

    @GetMapping("/tasks/due/{dueDateBefore}")
    public ResponseEntity<?> findTasksByDueDateBefore(@PathVariable LocalDate dueDateBefore) {
        User user = getCurrentUser();
        List<Task> tasks = taskService.findTasksByUserIdAndDueDateBefore(user.getId(), dueDateBefore);
        return ResponseEntity.ok(tasks);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> userOptional = userService.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("无法获取用户");
        }

        return userOptional.get();
    }
}
