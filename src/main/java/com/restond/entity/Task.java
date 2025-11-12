package com.restond.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min=1, max=100)
    private String title;

    @Min(1)
    @Max(5)
    private int priority;

    @Column(nullable = false)
    private Long userId;

    private LocalDate dueDate;

    private String description;
    private TaskStatus status;
}
