package com.enceladus.delivery.demo.commons.model.core;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DashboardResponse<T> implements Serializable {
    private T data;
    private String status;
    private String message;
}
