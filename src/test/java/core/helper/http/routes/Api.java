package core.helper.http.routes;

import core.helper.http.Call;
import io.restassured.http.Method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public interface Api {
    PortalApi portal = Api.create(PortalApi.class, "https://google.com/portal/api");

    @SuppressWarnings("unchecked")
    static <T extends Api> T create(Class<? extends T> interfaceClass, String baseUrl) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, (proxy, method, args) -> {
            if (method.getName().equals("equals")) {
                return proxy == args;
            } else if (method.getName().equals("hashCode")) {
                return System.identityHashCode(proxy);
            } else if (method.getName().equals("toString")) {
                return Arrays.toString(args);
            } else if (method.isAnnotationPresent(Route.class)) {
                Route route = method.getAnnotation(Route.class);
                return new Call(route.method(), route.path(), route.status(), baseUrl);
            } else {
                throw new UnsupportedOperationException("Method " + method.getName() + " is not supported");
            }
        });
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface Route {
        Method method();
        String path();
        int status();
    }
}
