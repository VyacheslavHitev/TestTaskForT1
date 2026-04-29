package tests;


import io.qameta.allure.Step;
import lombok.SneakyThrows;
import models.Environment;
import models.entities.TestProduct;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.lang.reflect.Field;
import java.util.*;

public class DataInjector implements BeforeTestExecutionCallback, TestInstancePostProcessor, AfterTestExecutionCallback {

    private void configureProduct(Object testInstance, TestProduct product) {
        if (testInstance instanceof Environment.Dev) {
            product.setPlatform("OpenStack");
            product.setSegment("dev");
            product.setRole("admin");
            product.setAvailabilityZone("north");
        }
        if (testInstance instanceof Environment.Ift) {
            product.setPlatform("IftPlatform");
            product.setSegment("ift");
            product.setRole("user");
            product.setAvailabilityZone("east");
        }

        product.setEnv(((Environment) testInstance).getEnv());
    }

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) throws IllegalAccessException {
        Object testInstance = extensionContext.getRequiredTestInstance();
        Optional<Field> productField = getProductField(testInstance);
        if (productField.isPresent()) {
            TestProduct product = getProduct(testInstance, productField.get());
            configureProduct(testInstance, product);
        }
    }

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext extensionContext) throws IllegalAccessException {
        Optional<Field> productField = getProductField(testInstance);
        if (productField.isPresent()) {
            TestProduct product = getProduct(testInstance, productField.get());
            if (product == null) {
                setProduct(testInstance, productField.get(), createProduct(productField.get()));
            }
        }
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        Object testInstance = extensionContext.getRequiredTestInstance();
        Optional<Field> productField = getProductField(testInstance);
        if (productField.isPresent()) {
            resetProduct(testInstance, productField.get());
        }
    }

    private Optional<Field> getProductField(Object testInstance) {
        return getFields(testInstance).stream()
                .filter(e -> TestProduct.class.isAssignableFrom(e.getType()))
                .findFirst();
    }

    private TestProduct getProduct(Object testInstance, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        return (TestProduct) field.get(testInstance);
    }

    @SneakyThrows
    private TestProduct createProduct(Field field) {
        return (TestProduct) field.getType().getDeclaredConstructor().newInstance();
    }

    private void setProduct(Object testInstance, Field field, TestProduct product) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(testInstance, product);
    }

    @Step("Сброс полей продукта и инициализация заново")
    private void resetProduct(Object testInstance, Field field) throws IllegalAccessException {
        TestProduct newProduct = createProduct(field);
        setProduct(testInstance, field, newProduct);
    }

    private List<Field> getFields(Object testInstance) {
        List<Field> fields = new ArrayList<>();
        Class<?> clazz = testInstance.getClass();
        while (clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }
}