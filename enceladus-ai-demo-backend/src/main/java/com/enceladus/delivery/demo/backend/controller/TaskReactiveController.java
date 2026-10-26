package com.enceladus.delivery.demo.backend.controller;

import com.enceladus.delivery.demo.backend.exceptions.UnknownAccessKeyException;
import com.enceladus.delivery.demo.backend.service.TaskBackendService;
import com.enceladus.delivery.demo.commons.model.Task;
import com.enceladus.delivery.demo.commons.model.core.Constants;
import com.enceladus.delivery.demo.commons.model.core.DashboardRequest;
import com.enceladus.delivery.demo.commons.model.core.DashboardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/${_DASHBOARD_API_VERSION:v1}/reactive")
@Tag(name = "Task Reactive Endpoints", description = "Internal APIs for Task dashboard interactions")
@Slf4j
public class TaskReactiveController {

    @Autowired
    TaskBackendService taskBackendService;

    @Operation(summary = "Create new task async")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Response with newly created task",
                    content = {@Content(mediaType = "application/json")})})
    @PostMapping("/task/create")
    public Mono<DashboardResponse<Task>> createTask(@RequestHeader(name = Constants.API_KEY_HEADER_NAME, required = true) @NonNull String token,
                                                    @RequestBody DashboardRequest<Task> taskToSave) throws UnknownAccessKeyException {
        if (!Constants.DASHBOARD_API_KEY.equals(token)) {
            throw new UnknownAccessKeyException("Invalid header value");
        }

        return taskBackendService.createNewTask(taskToSave.getPayload())
                .map(task -> DashboardResponse.<Task>builder().data(task).status("SUCCESS").message("Created").build())
                .doOnError(e -> log.error("Failed to create tasks in repository {}", e.toString(), e));
    }

    @Operation(summary = "Lists Tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Response with filtered lists task",
                    content = {@Content(mediaType = "application/json")})})
    @PostMapping("/task/lists")
    public Mono<DashboardResponse<List<Task>>> listTasks(@RequestHeader(name = Constants.API_KEY_HEADER_NAME, required = true) @NonNull String token,
                                                         @RequestBody DashboardRequest<Void> voidRequest, @RequestParam(value = "size", required = false, defaultValue = Constants.DEFAULT_PAGE_SIZE) Integer size,
                                                         @RequestParam(value = "page", required = false, defaultValue = "0") Integer page) throws UnknownAccessKeyException {
        if (!Constants.DASHBOARD_API_KEY.equals(token)) {
            throw new UnknownAccessKeyException("Invalid header value");
        }
        return taskBackendService.getPaginatedTaskLists(page, size)
                .subscribeOn(Schedulers.boundedElastic())
                .map(tasks -> DashboardResponse.<List<Task>>builder().data(tasks).status("SUCCESS").message("Tasks retrieved successfully.").build())
                .doOnError(e -> log.error("Failed to retrieve tasks in repository {}", e.toString(), e));
    }
}
