package tests.accessgroup;

import com.mifmif.common.regex.Generex;
import core.helper.StepLoggerExtension;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import models.entities.AccessGroup;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static models.portal.AccessGroupSteps.*;

@Epic("Управление")
@Feature("Группы доступа")
@Tag("smoke")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepLoggerExtension.class)
public class AccessGroupTest {

    private AccessGroup.AccessGroupBuilder buildAccessGroup() {
        return AccessGroup.builder().name("AT-GROUP")
                .projectName("proj-test")
                .accountsType("personal")
                .description("AT")
                .users(List.of("test1", "test2"))
                .codePurpose("compute");
    }

    @Test
    @Order(1)
    @DisplayName("Создание группы доступа compute")
    void create() {
        createAccessGroup(buildAccessGroup().build());
    }

    @Test
    @Order(2)
    @DisplayName("Редактирование группы доступа")
    void edit() {
        var group = buildAccessGroup().description("EDITED").build();
        editAccessGroup(group);
    }

    @Test
    @Order(3)
    @DisplayName("Удаление Группы доступа")
    void delete() {
        deleteAccessGroupByName(buildAccessGroup().build().getProjectName(), "AT-GROUP");
    }
}