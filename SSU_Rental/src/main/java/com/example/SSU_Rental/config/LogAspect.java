package com.example.SSU_Rental.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
@Slf4j
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(LogAspect.class);


    @Around("@annotation(LogExecutionTime)")

    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object proceed = joinPoint.proceed();
        stopWatch.stop();

        logger.info(stopWatch.prettyPrint());
        return proceed;
    }

}
