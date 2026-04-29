package models;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import tests.DataInjector;

@ExtendWith(DataInjector.class)
public interface Environment {
    String getEnv();
    String getProjectName();

    @Tag("dev")
    interface Dev extends Environment {
        default String getEnv() {
            return "DEV";
        }
        default String getProjectName() {
            return "proj-dev";
        }
    }

    @Tag("ift")
    interface Ift extends Environment {
        default String getEnv() {
            return "IFT";
        }
        default String getProjectName() {
            return "proj-ift";
        }
    }

    @Tag("test")
    interface TestEnv extends Environment {
        default String getEnv() {
            return "TEST";
        }
        default String getProjectName() {
            return "proj-test";
        }
    }
}
