package com.enceladus.delivery.demo.commons.commons;

import com.enceladus.delivery.demo.commons.model.DashboardSideNavigationEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VersionTest {
    @Test
    void sampleTest() {
        assertEquals(7, DashboardSideNavigationEnum.values().length);
    }
}
