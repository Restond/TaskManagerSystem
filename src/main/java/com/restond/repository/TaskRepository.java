package com.restond.repository;

import com.restond.entity.Task;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 *
 * findTasksById                --> 根据任务 Id 查询任务
 * findAllByCompleted           --> 根据完成状态查询任务
 * findAllByPriority            --> 根据任务优先级查询任务
 * findTasksByDueDateBefore     --> 根据任务截止时间查询任务
 * existsTaskById               --> 根据任务 Id 判断是否已存在该任务
 *
 */
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Nonnull
    Optional<Task> findById(@Nonnull Long id);

    List<Task> findAllByCompleted(Boolean completed);
    List<Task> findTasksByPriority(int priority);
    List<Task> findTasksByDueDateBefore(LocalDate dueDateBefore);
    boolean existsById(@Nonnull Long id);
    boolean existsByTitle(String title);
}
