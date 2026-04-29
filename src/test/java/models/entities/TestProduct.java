package models.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.log4j.Log4j2;
import models.Entity;

@SuperBuilder
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true, includeFieldNames = false)
@Log4j2
@Getter
@Setter
public abstract class TestProduct extends Entity {
    @ToString.Include
    protected String platform;
    protected String segment;
    protected String env;
    protected String availabilityZone, domain, role;

}
