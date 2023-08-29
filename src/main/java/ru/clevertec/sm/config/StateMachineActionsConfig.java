package ru.clevertec.sm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.clevertec.sm.service.CsvService;
import ru.clevertec.sm.service.EmailService;
import ru.clevertec.sm.service.ProductApiService;
import ru.clevertec.sm.service.ZipService;
import ru.clevertec.sm.statemachine.action.FetchCategoriesAction;
import ru.clevertec.sm.statemachine.action.MakeCsvFilesAction;
import ru.clevertec.sm.statemachine.action.MakeZipArchivesAction;
import ru.clevertec.sm.statemachine.action.SendEmailAction;

@Profile("!test")
@Configuration
public class StateMachineActionsConfig {

    @Bean
    public MakeCsvFilesAction makeCsvAction(
            CsvService csvService,
            ProductApiService productApiService
    ) {
        return new MakeCsvFilesAction(csvService, productApiService);
    }

    @Bean
    public FetchCategoriesAction fetchCategoriesAction(
            ProductApiService productApiService
    ) {
        return new FetchCategoriesAction(productApiService);
    }

    @Bean
    public MakeZipArchivesAction makeZipArchiveAction(ZipService zipService) {
        return new MakeZipArchivesAction(zipService);
    }

    @Bean
    public SendEmailAction sendEmailAction(
            EmailService emailService,
            EmailSubscribersProperties properties
    ) {
        return new SendEmailAction(emailService, properties);
    }
}
