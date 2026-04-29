package tests;

import core.helper.StepLoggerExtension;
import io.qameta.allure.Epic;
import models.Environment;
import models.entities.MainProduct;
import models.entities.SecondProduct;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Epic("Создание продуктов")
@Tag("smoke")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepLoggerExtension.class)
public class Test3 implements Environment.Dev {

    protected MainProduct mainProduct;
    protected SecondProduct product;

    protected void createMainProduct() {
        mainProduct = MainProduct.builder()
                .segment(product.getSegment())
                .platform(product.getPlatform())
                .availabilityZone(product.getAvailabilityZone())
                .role(product.getRole())
                .env(product.getEnv())
                .domain(product.getDomain())
                .build();
        try (MainProduct main = (MainProduct) mainProduct.init()) {
            main.create();
        }
    }

    protected void createSecondProduct() {
        try (SecondProduct second = (SecondProduct) product.init()) {
            second.create();
        }
    }

    @Test
    @Order(1)
    @DisplayName("Создание двух продуктов")
    void create() {
        var executor = Executors.newFixedThreadPool(2);
        Future<?> secondFuture = executor.submit(this::createSecondProduct);
        Future<?> mainFuture = executor.submit(this::createMainProduct);
        awaitTerminationAfterShutdown(executor);
        Assertions.assertAll("Создание главного и второго продукта", mainFuture::get, secondFuture::get);
    }

    public static void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(1, TimeUnit.HOURS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
