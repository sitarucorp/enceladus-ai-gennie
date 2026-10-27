package com.enceladus.delivery.demo.backend.service;

import com.enceladus.delivery.demo.backend.repository.TaskRepository;
import com.enceladus.delivery.demo.commons.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskBackendServiceTest {

    @Test
    void createNewTaskDelegatesToRepository() {
        TaskRepository repository = mock(TaskRepository.class);
        TaskBackendService service = new TaskBackendService(repository);
        Task task = task(1L, "first", 1);
        when(repository.save(task)).thenReturn(Mono.just(task));

        StepVerifier.create(service.createNewTask(task))
                .expectNext(task)
                .verifyComplete();

        verify(repository).save(task);
    }

    @Test
    void returnsRequestedPageSortedByCreationDateDescending() {
        TaskRepository repository = mock(TaskRepository.class);
        TaskBackendService service = new TaskBackendService(repository);
        List<Task> tasks = List.of(task(1L, "oldest", 1), task(2L, "middle", 2), task(3L, "newest", 3));
        when(repository.findAll(any(Sort.class))).thenReturn(Flux.fromIterable(tasks));

        StepVerifier.create(service.getPaginatedTaskLists(2, 1))
                .assertNext(page -> assertEquals(List.of(tasks.get(1)), page))
                .verifyComplete();

        verify(repository).findAll(Sort.by(Sort.Order.desc("creationDate")));
    }

    private Task task(Long id, String description, int day) {
        return Task.builder()
                .id(id)
                .description(description)
                .creationDate(Instant.parse("2026-01-0" + day + "T00:00:00Z"))
                .dueDate(LocalDate.of(2026, 1, day))
                .build();
    }
}
