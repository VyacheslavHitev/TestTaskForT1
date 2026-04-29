package core.helper;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.util.AspectUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.message.Message;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.TimeZone;

@Aspect
@Log4j2
@SuppressWarnings("UnusedDeclaration")
public class StepLoggerExtension implements BeforeEachCallback, AfterEachCallback {
    private static final InheritableThreadLocal<StringJoiner> stepLog = new InheritableThreadLocal<>();

    public static void writeStepLog(String text) {
        if (stepLog.get() != null)
            stepLog.get().add(text != null ? text : "null");
    }

    public static void clearStepLog() {
        stepLog.remove();
        stepLog.set(new StringJoiner("\n").setEmptyValue("В stepLog не было записей"));
    }

    public static String getStepLog() {
        var joiner = stepLog.get().setEmptyValue("В stepLog не было записей");
        return joiner.toString();
    }

    @Around("execution(* org.apache.logging.log4j.core.config.LoggerConfig.log(*, *, *, *, *, *, *))")
    public Object interceptLogMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        StackTraceElement location = (StackTraceElement) args[2];
        Level level = (Level) args[4];
        Message data = (Message) args[5];
        String error = args[6] == null ? "" : args[6].toString();

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        String dateFormatted = formatter.format(new Date());

        writeStepLog("%s %s %s:%s - %s %s".formatted(dateFormatted, level.name(), location.getFileName(),
                location.getLineNumber(), data.getFormattedMessage(), error));
        return joinPoint.proceed();
    }

    @Pointcut("@annotation(io.qameta.allure.Step)")
    private void withStepAnnotation() {
    }

    @Pointcut("execution(* *(..))")
    private void anyMethod() {
    }

    @Before("anyMethod() && withStepAnnotation()")
    public void stepStart(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Step step = methodSignature.getMethod().getAnnotation(Step.class);
        String name = AspectUtils.getName(step.value(), joinPoint);
        log.debug(AspectUtils.getName(name, joinPoint));
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        if (Objects.nonNull(getStepLog()))
            Allure.getLifecycle().addAttachment("log-test", "text/html", "log", getStepLog().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        clearStepLog();
    }
}
