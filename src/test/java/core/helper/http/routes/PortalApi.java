package core.helper.http.routes;

import core.helper.http.Call;
import io.restassured.http.Method;

public interface PortalApi extends Api {

    //Добавление пользователей в группу
    @Route(method = Method.POST, path = "/v1/projects/{project_name}/access_groups/{access_group_name}/group_users", status = 200)
    Call postV1ProjectsProjectNameAccessGroupsAccessGroupNameGroupUsers();

    //Удаление пользователя из группы
    @Route(method = Method.DELETE, path = "/v1/projects/{project_name}/access_groups/{access_group_name}/group_users", status = 204)
    Call deleteV1ProjectsProjectNameAccessGroupsAccessGroupNameGroupUsers();

    //Обновление группы доступа
    @Route(method = Method.PATCH, path = "/v1/projects/{project_name}/access_groups/{name}", status = 404)
    Call patchV1ProjectsProjectNameAccessGroupsName();

    //Удаление группы доступа
    @Route(method = Method.DELETE, path = "/v1/projects/{project_name}/access_groups/{name}", status = 404)
    Call deleteV1ProjectsProjectNameAccessGroupsName();

    //Получение группы доступа
    @Route(method = Method.GET, path = "/v1/projects/{project_name}/access_groups/{name}", status = 404)
    Call getV1ProjectsProjectNameAccessGroupsName();

    //Удаление групп доступа
    @Route(method = Method.DELETE, path = "/v1/projects/{project_name}/access_groups/delete_list", status = 204)
    Call deleteV1ProjectsProjectNameAccessGroupsDeleteList();

    //Создать группу доступа
    @Route(method = Method.POST, path = "/v1/projects/{project_name}/access_groups", status = 404)
    Call postV1ProjectsProjectNameAccessGroups();

    //Cписок групп доступа
    @Route(method = Method.GET, path = "/v1/projects/{project_name}/access_groups", status = 200)
    Call getV1ProjectsProjectNameAccessGroups();
}
