package com.example.cepsacbackend.config;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    // Define un punto de corte para todos los controladores en el paquete especificado
    @Pointcut("within(com.example.cepsacbackend.controller..*)")
    public void controllerMethods() {
    }

    @Around("controllerMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("Enter: {}.{}() with argument[s] = {}", 
            joinPoint.getSignature().getDeclaringTypeName(),
            methodName, 
            Arrays.toString(args));

        try {
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            log.info("Exit: {}.{}() execution time = {} ms", 
                joinPoint.getSignature().getDeclaringTypeName(),
                methodName, 
                elapsedTime);
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(args),
                joinPoint.getSignature().getDeclaringTypeName(), methodName);
            throw e;
        }
    }
}
