package com.project.test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.project.test");

        noClasses()
            .that()
            .resideInAnyPackage("com.project.test.service..")
            .or()
            .resideInAnyPackage("com.project.test.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.project.test.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
