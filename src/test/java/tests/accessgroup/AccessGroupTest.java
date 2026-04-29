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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Epic("Управление")
@Feature("Группы доступа")
@Tag("smoke")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepLoggerExtension.class)
public class AccessGroupTest {

    private static final String GROUP_NAME = "AT-GROUP";
    private static final String PROJECT_NAME = "proj-test";

    private AccessGroup.AccessGroupBuilder buildAccessGroup() {
        return AccessGroup.builder().name(GROUP_NAME)
                .projectName(PROJECT_NAME)
                .accountsType("personal")
                .description("AT")
                .users(List.of("test1", "test2"))
                .codePurpose("compute");
    }

    @BeforeEach
    public void setUp() {
        boolean groupCreated = false;

        try {
            AccessGroup foundGroup = getAccessGroupByName(PROJECT_NAME, GROUP_NAME);

            if (foundGroup != null) {
                groupCreated = true;
            }
        } catch (Exception ignored) {}

        if (!groupCreated) {
            createAccessGroup(buildAccessGroup().build());
        }
    }

    @AfterAll
    public static void tearDown() {
        deleteAccessGroupByName(PROJECT_NAME, GROUP_NAME);
    }

    @Test
    @DisplayName("Создание группы доступа compute")
    void create() {
        AccessGroup foundGroup = getAccessGroupByName(PROJECT_NAME, GROUP_NAME);
        assertNotNull(foundGroup, "Группа должна быть создана");
    }

    @Test
    @DisplayName("Редактирование группы доступа")
    void edit() {
        AccessGroup updatedGroup = buildAccessGroup().description("EDITED").build();
        editAccessGroup(updatedGroup);

        AccessGroup foundGroup = getAccessGroupByName(PROJECT_NAME, GROUP_NAME);
        Assertions.assertEquals(
                updatedGroup.getDescription(),
                foundGroup.getDescription(),
                "Описание группы должно было поменяться");
    }

    @Test
    @DisplayName("Удаление Группы доступа")
    void delete() {
        deleteAccessGroupByName(buildAccessGroup().build().getProjectName(), GROUP_NAME);

        boolean groupFound = false;

        try {
            AccessGroup foundGroup = getAccessGroupByName(PROJECT_NAME, GROUP_NAME);

            if (foundGroup != null) {
                groupFound = true;
            }
        } catch (Exception ignored) {}

        assertFalse(groupFound, "Группа должна удалиться");
    }
}