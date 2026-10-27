package com.enceladus.delivery.demo.commons.model.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GreaterThanValidatorTest {

    private final GreaterThanValidator validator = new GreaterThanValidator();

    @BeforeEach
    void initializeValidator() {
        GreaterThan constraint = mock(GreaterThan.class);
        when(constraint.value()).thenReturn(10);
        validator.initialize(constraint);
    }

    @Test
    void acceptsValuesAboveThreshold() {
        assertTrue(validator.isValid(11, null));
    }

    @Test
    void rejectsValuesAtOrBelowThreshold() {
        assertFalse(validator.isValid(10, null));
        assertFalse(validator.isValid(9, null));
    }

    @Test
    void treatsNullAsValid() {
        assertTrue(validator.isValid(null, null));
    }
}
