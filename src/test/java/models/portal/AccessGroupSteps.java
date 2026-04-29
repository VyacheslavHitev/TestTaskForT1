package models.portal;

import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import models.entities.AccessGroup;

import static models.portal.PortalBackClient.*;

@Log4j2
public class AccessGroupSteps {

    @Step("Получение группы доступа по названию")
    public static AccessGroup getAccessGroupByName(String projectName, String name) {
        return getV1ProjectsProjectNameAccessGroupsName(projectName, name).assertStatus().asObject();
    }

    @Step("Создание группы доступа")
    public static void createAccessGroup(AccessGroup group) {
        postV1ProjectsProjectNameAccessGroups(group.getProjectName(), group).assertStatus();
    }

    @Step("Редактирование группы доступа")
    public static void editAccessGroup(AccessGroup group) {
        patchV1ProjectsProjectNameAccessGroupsName(group.getProjectName(), group, group.getName()).assertStatus();
    }

    @Step("Удаление группы доступа по названию")
    public static void deleteAccessGroupByName(String projectName, String name) {
        deleteV1ProjectsProjectNameAccessGroupsName(projectName, name).assertStatus();
    }
}
