package com.enceladus.delivery.demo.commons.commons;

import com.enceladus.delivery.demo.commons.model.DashboardSideNavigationEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class VersionTest {
    @Test
    public void sampleTest() {
        Assert.assertEquals(6, DashboardSideNavigationEnum.values().length);
    }
}
