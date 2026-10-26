package com.enceladus.delivery.demo.commons.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.time.LocalDate;

@Table(name = "task")
@Builder
public record Task(@Id Long id, String description, Instant creationDate,
                   LocalDate dueDate) implements Comparable<Task> {
    @Override
    public int compareTo(Task other) {
        return this.creationDate.compareTo(other.creationDate);
    }
}


