package com.restond.repository;

import com.restond.entity.Task;
import com.restond.entity.TaskStatus;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Nonnull
    Optional<Task> findById(@Nonnull Long id);

    List<Task> findByStatus(TaskStatus status);
    List<Task> findTasksByPriority(int priority);
    List<Task> findTasksByDueDateBefore(LocalDate dueDateBefore);
    boolean existsById(@Nonnull Long id);
    boolean existsByTitle(String title);
}
