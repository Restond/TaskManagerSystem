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

    public Optional<Task> findTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> findAllByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    public List<Task> findTasksByPriority(int priority) {
        return taskRepository.findTasksByPriority(priority);
    }

    public List<Task> findTasksByDueDateBefore(LocalDate dueTimeBefore) {
        return taskRepository.findTasksByDueDateBefore(dueTimeBefore);
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }
}
