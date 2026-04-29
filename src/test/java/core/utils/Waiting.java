package core.utils;

import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;


/**
 * Класс ожиданий. Загрузка элементов, загрузка страницы, возможность кликнуть и т.п.
 *
 * @author kochetkovma
 */
@Log4j2
public class Waiting {

    private Waiting() {}

    /**
     * Заснуть на таймаут
     *
     * @param millis Время ожидания в мс
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {}
    }

    public static void sleep(Duration duration) {
        sleep(duration.toMillis());
    }

    public static boolean sleep(Supplier<Boolean> b, Duration duration) {
        return sleep(b, duration, Duration.ofMillis(300));
    }

    public static boolean sleep(Supplier<Boolean> b, Duration duration, Duration interval) {
        Instant start = Instant.now();
        while (duration.compareTo(Duration.between(start, Instant.now())) > 0) {
            if (b.get()) return true;
            sleep(interval.toMillis());
        }
        return false;
    }
}
