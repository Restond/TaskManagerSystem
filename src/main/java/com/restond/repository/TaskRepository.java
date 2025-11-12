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

    List<Task> findByUserIdAndStatus(Long userId, TaskStatus status);
    List<Task> findByUserIdAndPriority(Long userId, int priority);
    List<Task> findByUserId(Long userId);
    List<Task> findByUserIdAndDueDateBefore(Long userId, LocalDate dueDateBefore);

    boolean existsById(@Nonnull Long id);
    boolean existsByTitle(String title);
}
