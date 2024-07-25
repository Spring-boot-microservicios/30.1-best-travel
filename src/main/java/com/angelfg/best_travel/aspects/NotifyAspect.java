package com.angelfg.best_travel.aspects;

import com.angelfg.best_travel.util.annotations.Notify;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

import static com.angelfg.best_travel.util.BestTravelUtil.writeNotification;

@Aspect // AOP => Programacion orientada a aspectos
@Component
public class NotifyAspect {

    private static final String LINE_FORMAT = "At %s new request %s, with size page %s and order %s";
    private static final String PATH = "files/notify.txt";

//    @Before()
//    @After()
//    @Around()
    @After(value = "@annotation(com.angelfg.best_travel.util.annotations.Notify)")
    public void notifyInFile(JoinPoint joinPoint) throws IOException {
//        Arrays.stream(joinPoint.getArgs()).forEach(System.out::println);
        var args = joinPoint.getArgs();
        var size = args[1];
        var order = args[2] == null ? "NONE" : args[2];

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Notify annotation = method.getAnnotation(Notify.class);

        var text = String.format(
            LINE_FORMAT,
            LocalDateTime.now(),
            annotation.value(),
            size.toString(),
            order
        );

        writeNotification(text, PATH);
    }

}
