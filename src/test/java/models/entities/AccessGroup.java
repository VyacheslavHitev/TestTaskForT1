package models.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import models.Entity;
import org.json.JSONObject;

import java.util.List;

@Builder
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class AccessGroup extends Entity {
    String name;
    String projectName;
    String accountsType;
    String description;
    List<String> users;
    String codePurpose;

    @Override
    public Entity init() {
        return null;
    }

    @Override
    public JSONObject toJson() {
        return null;
    }

    @Override
    protected void create() {

    }

    @Override
    protected void delete() {

    }
}
