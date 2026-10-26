package com.enceladus.delivery.demo.backend.repository;

import com.enceladus.delivery.demo.commons.model.Task;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends ReactiveCrudRepository<Task, Long>, ReactiveSortingRepository<Task, Long> {
}
