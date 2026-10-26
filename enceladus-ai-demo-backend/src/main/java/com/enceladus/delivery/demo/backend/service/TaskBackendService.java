package com.enceladus.delivery.demo.backend.service;

import com.enceladus.delivery.demo.backend.repository.TaskRepository;
import com.enceladus.delivery.demo.commons.model.Task;
import com.enceladus.delivery.demo.commons.model.core.GreaterThan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@Slf4j
public class TaskBackendService {

    TaskRepository taskRepository;

    public TaskBackendService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Mono<Task> createNewTask(Task task) {
        return taskRepository.save(task)
                .subscribeOn(Schedulers.boundedElastic())
                .doOnError(e -> log.error("Failed to save task repository {}", e.toString(), e));
    }

    public Mono<List<Task>> getPaginatedTaskLists(@GreaterThan(value = 0) int page,
                                                  @GreaterThan(value = 1) int size) {
        int offset = Math.abs(page - 1) * size;
        return taskRepository.findAll(Sort.by(Sort.Order.desc("creationDate")))
                .skip(offset)
                .take(size)
                .collectSortedList();
    }
}

