package ru.clevertec.sm.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.clevertec.sm.util.TimeSpan;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Aspect
@Component
public class TimeProfilingAspect {

    private final static String DELIMITER = " - ";
    private final StringBuilder reportBuilder = new StringBuilder();

    @Around("execution(* ru.clevertec.sm.service.impl.CsvServiceImpl.writeDataToCsv(..))")
    public Object logCsvCreatingExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Map.Entry<TimeSpan, Object> timeAndReturnValue = getExecutionTimeSpan(joinPoint, "Csv file " + args[1]);
        appendTimeSpanInfoToReport(
                "started creating csv file",
                "created file",
                timeAndReturnValue.getValue().toString(),
                timeAndReturnValue.getKey()
        );

        return timeAndReturnValue.getValue();
    }

    @Around("execution(* ru.clevertec.sm.service.impl.ZipServiceImpl.createArchive(..))")
    public Object logZipCreatingExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Map.Entry<TimeSpan, Object> timeSpanAndReturnValue = getExecutionTimeSpan(joinPoint, "Zip archive");
        String zipName = ((Optional<Map.Entry<String, List<String>>>) timeSpanAndReturnValue.getValue())
                .map(Map.Entry::getKey)
                .orElse("undefined");
        appendTimeSpanInfoToReport(
                "started creating zip archive",
                "created archive",
                zipName,
                timeSpanAndReturnValue.getKey()
        );

        return timeSpanAndReturnValue.getValue();
    }

    @After("execution(* ru.clevertec.sm.service.impl.EmailServiceImpl.sendEmailToConsumers(..))")
    public void logSendingEmail() {
        reportBuilder
                .append(LocalDateTime.now())
                .append(DELIMITER)
                .append("email sent to subscribers")
                .append("\n");
        log.info("Email sent");
    }

    public StringBuilder getReportBuilder() {
        return reportBuilder;
    }

    private void appendTimeSpanInfoToReport(String startMessage,
                                            String finishMessage,
                                            String createdFileName,
                                            TimeSpan timeSpan) {
        reportBuilder
                .append(timeSpan.getStart())
                .append(DELIMITER + startMessage)
                .append("\n")
                .append(timeSpan.getFinish())
                .append(DELIMITER + finishMessage)
                .append(createdFileName)
                .append("\n");
    }

    private Map.Entry<TimeSpan, Object> getExecutionTimeSpan(
            ProceedingJoinPoint joinPoint, String objectToCreate
    ) throws Throwable {
        LocalDateTime start = LocalDateTime.now();
        log.info("Start creating {} at {}", objectToCreate, start);
        Object returnValue = joinPoint.proceed();
        LocalDateTime finish = LocalDateTime.now();
        log.info("{} created at {}", returnValue, start);

        return Map.entry(TimeSpan.of(start, finish), returnValue);
    }
}
