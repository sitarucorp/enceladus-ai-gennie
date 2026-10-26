package com.enceladus.delivery.dashboard.service;

import com.enceladus.delivery.dashboard.handler.ReactiveDashboardClientHandler;
import com.enceladus.delivery.demo.commons.model.Task;
import com.enceladus.delivery.demo.commons.model.core.DashboardRequest;
import com.enceladus.delivery.demo.commons.model.core.DashboardResponse;
import com.vaadin.flow.data.provider.Query;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
@Slf4j
public class TaskDashboardService {

    private static final String PATH_TASK_CREATE_POST_API = "/api/v1/reactive/task/create";
    private static final String PATH_TASK_LISTS_API = "/api/v1/reactive/task/lists";

    ReactiveDashboardClientHandler reactiveDashboardClientHandler;

    TaskDashboardService(ReactiveDashboardClientHandler reactiveDashboardClientHandler) {
        this.reactiveDashboardClientHandler = reactiveDashboardClientHandler;
    }

    public Mono<DashboardResponse<Task>> createTask(String description, @Nullable LocalDate dueDate) {
        DashboardRequest<Task> taskRequest = new DashboardRequest<>();
        taskRequest.setPayload(Task.builder().creationDate(Instant.now()).dueDate(dueDate).description(description).build());

        return reactiveDashboardClientHandler.httpPostDashboardRequest(
                        PATH_TASK_CREATE_POST_API,
                        taskRequest, Optional.empty(),
                        new ParameterizedTypeReference<DashboardResponse<Task>>() {
                        }
                ).subscribeOn(Schedulers.boundedElastic()) // FIXME doesn't require though
                .doOnError(e -> log.error("ERROR: ", e));
    }


    public Mono<DashboardResponse<List<Task>>> list(Query<?, ?> query) {

        String page = String.valueOf(query.getPage());
        String size = String.valueOf(query.getPageSize());

        DashboardRequest<?> listRequest = new DashboardRequest<>();

        return reactiveDashboardClientHandler.httpPostDashboardRequest(
                        PATH_TASK_LISTS_API,
                        listRequest,
                        Optional.of(Map.of("page", page, "size", size)),
                        new ParameterizedTypeReference<DashboardResponse<List<Task>>>() {
                        }
                ).subscribeOn(Schedulers.boundedElastic()) // FIXME doesn't require though
                .doOnError(e -> log.error("ERROR: ", e))
                .onErrorResume(e -> Mono.just(new DashboardResponse<List<Task>>(Collections.emptyList(),
                        "ERROR", "On error fallback to empty")));
    }
}
