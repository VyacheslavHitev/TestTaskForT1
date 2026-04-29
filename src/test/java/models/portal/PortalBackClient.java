package models.portal;

import core.helper.http.ApiClient;
import models.entities.AccessGroup;

import static core.helper.http.Spec.requestSpecificationMain;
import static core.helper.http.routes.Api.portal;

public class PortalBackClient {

    public static ApiClient<AccessGroup>.Response getV1ProjectsProjectNameAccessGroupsName(String projectName, String groupName) {
        return new ApiClient<AccessGroup>(requestSpecificationMain())
                .model(AccessGroup.class)
                .api(portal.getV1ProjectsProjectNameAccessGroupsName(), projectName, groupName);
    }

    public static ApiClient<Void>.Response postV1ProjectsProjectNameAccessGroups(String projectName, Object body) {
        return new ApiClient<Void>(requestSpecificationMain().body(body))
                .api(portal.postV1ProjectsProjectNameAccessGroups(), projectName);
    }

    public static ApiClient<Void>.Response patchV1ProjectsProjectNameAccessGroupsName(String projectName, Object body, String groupName) {
        return new ApiClient<Void>(requestSpecificationMain().body(body))
                .api(portal.patchV1ProjectsProjectNameAccessGroupsName(), projectName, groupName);
    }

    public static ApiClient<Void>.Response deleteV1ProjectsProjectNameAccessGroupsName(String projectName, String groupName) {
        return new ApiClient<Void>(requestSpecificationMain())
                .api(portal.deleteV1ProjectsProjectNameAccessGroupsName(), projectName, groupName);
    }
}
