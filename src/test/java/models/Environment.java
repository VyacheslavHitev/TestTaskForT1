package models;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import tests.DataInjector;

@ExtendWith(DataInjector.class)
public interface Environment {
    String getEnv();

    @Tag("dev")
    interface Dev extends Environment {
        default String getEnv() {
            return "DEV";
        }
    }

    @Tag("ift")
    interface Ift extends Environment {
        default String getEnv() {
            return "IFT";
        }
    }
}
