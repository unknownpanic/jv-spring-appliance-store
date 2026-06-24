package com.epam.rd.autocode.assessment.appliancestore.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Pointcut("within(com.epam.rd.autocode.assessment.appliancestore.service.impl..*) "
            + "|| within(com.epam.rd.autocode.assessment.appliancestore.controller..*)")
    public void serviceMethods() {
    }

    @Around("serviceMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        long start = System.currentTimeMillis();

        log.info("Started {}", methodName);

        try {
            return joinPoint.proceed();
        } finally {
            long executionTime = System.currentTimeMillis() - start;
            log.info("Finished {} in {} ms", methodName, executionTime);
        }
    }

    @AfterThrowing(pointcut = "serviceMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        log.error("Exception in {}",
                joinPoint.getSignature().toShortString(),
                ex);
    }
}
