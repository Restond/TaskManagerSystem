package com.restond.service;

import com.restond.entity.Task;
import com.restond.entity.TaskStatus;
import com.restond.exception.TaskAlreadyExistsException;
import com.restond.exception.TaskNotFoundException;
import com.restond.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        if (taskRepository.existsByTitle(task.getTitle())) {
            throw new TaskAlreadyExistsException("任务标题已存在" + task.getTitle());
        }

        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.PENDING);
        }

        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        } else {
            throw new TaskNotFoundException("任务不存在" + id);
        }
    }

    public Task updateTask(Task task) {
        if (!taskRepository.existsById(task.getId())) {
            throw new TaskNotFoundException("任务不存在: " + task.getId());
        }
        return taskRepository.save(task);
    }

    public Task updateTaskStatus(Long taskId, TaskStatus status) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setStatus(status);
            return taskRepository.save(task);
        } else {
            throw new TaskNotFoundException("任务不存在: " + taskId);
        }
    }

    public Optional<Task> findTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> findByUserId(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    public List<Task> findByUserIdAndPriority(Long userId, int priority) {
        return taskRepository.findByUserIdAndPriority(userId, priority);
    }

    public List<Task> findByUserIdAndStatus(Long userId, TaskStatus status) {
        return taskRepository.findByUserIdAndStatus(userId, status);
    }

    public List<Task> findTasksByUserIdAndDueDateBefore(Long userId, LocalDate dueDateBefore) {
        return taskRepository.findByUserIdAndDueDateBefore(userId, dueDateBefore);
    }

    public boolean validateTaskOwnership(Long userId, Long taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            return task.getUserId().equals(userId);
        }
        return false;
    }
}
