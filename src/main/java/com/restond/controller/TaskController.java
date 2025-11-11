package com.restond.controller;

import com.restond.entity.Task;
import com.restond.entity.TaskStatus;
import com.restond.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/TaskManagerSystem")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        Task newTask = taskService.createTask(task);
        return new ResponseEntity<>(newTask, HttpStatus.CREATED);
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @Valid @RequestBody Task task) {
        task.setId(taskId);
        Task updatedTask = taskService.updateTask(task);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> findAllTasks() {
        return ResponseEntity.ok(taskService.findAllTasks());
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<Optional<Task>> findTaskById(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.findTaskById(taskId));
    }

    @GetMapping("/tasks/status/{status}")
    public ResponseEntity<List<Task>> findTasksByStatus(@PathVariable TaskStatus status) {
        return ResponseEntity.ok(taskService.findAllByStatus(status));
    }

    @GetMapping("/tasks/priority/{priority}")
    public ResponseEntity<List<Task>> findTasksByPriority(@PathVariable int priority) {
        return ResponseEntity.ok(taskService.findTasksByPriority(priority));
    }

    @GetMapping("/tasks/due/{dueDateBefore}")
    public ResponseEntity<List<Task>> findTasksByDueDateBefore(@PathVariable LocalDate dueDateBefore) {
        return ResponseEntity.ok(taskService.findTasksByDueDateBefore(dueDateBefore));
    }
}
