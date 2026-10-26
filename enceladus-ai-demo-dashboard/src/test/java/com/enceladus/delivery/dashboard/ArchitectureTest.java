package com.enceladus.delivery.dashboard;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@Ignore
class ArchitectureTest {

    static final String BASE_PACKAGE = "com.enceladus.delivery.dashboard";

    private final JavaClasses importedClasses = new ClassFileImporter().importPackages(BASE_PACKAGE);

    @Test
    void domain_model_should_not_depend_on_application_services() {

    }

    @Test
    void domain_model_should_not_depend_on_the_user_interface() {
    }

    @Test
    void repositories_should_only_be_used_by_application_services_and_other_domain_classes() {
    }

    @Test
    void repositories_should_only_be_accessed_by_transactional_classes() {
    }

    @Test
    void application_services_should_not_depend_on_the_user_interface() {
    }
}
