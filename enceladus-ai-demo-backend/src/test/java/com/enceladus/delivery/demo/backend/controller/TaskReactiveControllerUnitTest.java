package com.enceladus.delivery.demo.backend.controller;

import com.enceladus.delivery.demo.backend.exceptions.UnknownAccessKeyException;
import com.enceladus.delivery.demo.backend.service.TaskBackendService;
import com.enceladus.delivery.demo.commons.model.Task;
import com.enceladus.delivery.demo.commons.model.core.Constants;
import com.enceladus.delivery.demo.commons.model.core.DashboardRequest;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskReactiveControllerUnitTest {

    @Test
    void rejectsInvalidApiKeyBeforeCallingService() {
        TaskBackendService service = mock(TaskBackendService.class);
        TaskReactiveController controller = new TaskReactiveController(service);

        assertThrows(UnknownAccessKeyException.class,
                () -> controller.listTasks("invalid", new DashboardRequest<>(), 10, 0));
    }

    @Test
    void createsTaskAndMapsSuccessfulResponse() throws Exception {
        TaskBackendService service = mock(TaskBackendService.class);
        TaskReactiveController controller = new TaskReactiveController(service);
        Task task = task();
        when(service.createNewTask(task)).thenReturn(Mono.just(task));

        StepVerifier.create(controller.createTask(Constants.DASHBOARD_API_KEY, new DashboardRequest<>(task)))
                .assertNext(response -> {
                    assertEquals(task, response.getData());
                    assertEquals("SUCCESS", response.getStatus());
                    assertEquals("Created", response.getMessage());
                })
                .verifyComplete();

        verify(service).createNewTask(task);
    }

    @Test
    void listsTasksAndMapsSuccessfulResponse() throws Exception {
        TaskBackendService service = mock(TaskBackendService.class);
        TaskReactiveController controller = new TaskReactiveController(service);
        List<Task> tasks = List.of(task());
        when(service.getPaginatedTaskLists(2, 5)).thenReturn(Mono.just(tasks));

        StepVerifier.create(controller.listTasks(Constants.DASHBOARD_API_KEY,
                        new DashboardRequest<>(), 5, 2))
                .assertNext(response -> {
                    assertEquals(tasks, response.getData());
                    assertEquals("SUCCESS", response.getStatus());
                    assertEquals("Tasks retrieved successfully.", response.getMessage());
                })
                .verifyComplete();

        verify(service).getPaginatedTaskLists(2, 5);
    }

    private Task task() {
        return Task.builder()
                .id(1L)
                .description("Test task")
                .creationDate(Instant.parse("2026-01-01T00:00:00Z"))
                .dueDate(LocalDate.of(2026, 1, 2))
                .build();
    }
}
