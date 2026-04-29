package models.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.log4j.Log4j2;
import models.Entity;
import org.json.JSONObject;

@ToString(callSuper = true, onlyExplicitlyIncluded = true, includeFieldNames = false)
@EqualsAndHashCode(callSuper = true)
@Log4j2
@Data
@NoArgsConstructor
@SuperBuilder
public class MainProduct extends TestProduct {
    String mainString;
    @Override
    public Entity init() {
        if (mainString == null) {
            mainString = "Я главный продукт";
        }
        return this;
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject().put("platform", platform)
                .put("segment", segment)
                .put("env", env)
                .put("availabilityZone", availabilityZone)
                .put("domain", domain)
                .put("role", role)
                .put("mainString", mainString);
    }

    @Override
    public void create() {
        System.out.println("Создан главный продукт" + toJson().toString());
    }

    @Override
    public void delete() {
        System.out.println("Главный продукт удален" + toJson().toString());
    }
}
