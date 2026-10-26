package com.enceladus.delivery.demo.backend.controller;

import com.enceladus.delivery.demo.backend.service.TaskBackendService;
import com.enceladus.delivery.demo.commons.model.core.Constants;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;

@Ignore
@WebMvcTest(value = TaskReactiveController.class, properties = "version=v1")
@AutoConfigureMockMvc
@Slf4j
public class TaskReactiveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskBackendService taskBackendService;

    @MockitoBean
    private BuildProperties buildProperties;

    @BeforeEach
    public void setup() {}

    @Test
    public void testFailGetVersionInfoWhenApiHeaderIsMissing() throws Exception {

        String expectedJson = """
                {"data":"","status":"MISSING_ACCESS_KEY_HEADER","message":"Dashboard Error : Missing x-api-key header"}""";

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/reactive/version"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedJson));
    }

    @Test
    public void testFailGetVersionInfoWhenApiAccessKeyInvalid() throws Exception {

        String expectedJson = """
                {"data":"","status":"INVALID_API_KEY","message":"Dashboard Error : Invalid x-api-key header"}""";

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/reactive/version")
                        .header("x-api-key", "FooBar"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedJson));
    }


    @Test
    public void testGetVersionInfo() throws Exception {

        Mockito.when(buildProperties.getVersion()).thenReturn("0.0.1-SNAPSHOT");
        Mockito.when(buildProperties.getArtifact()).thenReturn("enceladus-ai-demo-backend");
        Mockito.when(buildProperties.getGroup()).thenReturn("com.enceladus.delivery");
        Mockito.when(buildProperties.getName()).thenReturn("enceladus-ai-demo-backend");
        Mockito.when(buildProperties.getTime()).thenReturn(Instant.ofEpochMilli(1749928291697L));

        String expectedJson = """
        {"version":"0.0.1-SNAPSHOT","buildInfo":"{\\"entries\\":\\"null\\"}}""";

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/reactive/version")
                        .header("x-api-key", Constants.DASHBOARD_API_KEY))
                .andExpect(MockMvcResultMatchers.status().isOk());
                //.andExpect(MockMvcResultMatchers.content().string(expectedJson)); //<-- FIXME ?
    }
}
