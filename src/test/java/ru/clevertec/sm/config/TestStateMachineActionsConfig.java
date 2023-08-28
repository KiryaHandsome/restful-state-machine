package ru.clevertec.sm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.clevertec.sm.statemachine.action.FetchCategoriesAction;
import ru.clevertec.sm.statemachine.action.MakeCsvFilesAction;
import ru.clevertec.sm.statemachine.action.MakeZipArchiveAction;
import ru.clevertec.sm.statemachine.action.SendEmailAction;
import ru.clevertec.sm.util.action.FakeFetchCategoriesAction;
import ru.clevertec.sm.util.action.FakeMakeCsvFilesAction;
import ru.clevertec.sm.util.action.FakeMakeZipArchivesAction;
import ru.clevertec.sm.util.action.FakeSendEmailAction;

@Profile("test")
@Configuration
public class TestStateMachineActionsConfig {

    @Bean
    public MakeCsvFilesAction makeCsvAction() {
        return new FakeMakeCsvFilesAction();
    }

    @Bean
    public FetchCategoriesAction fetchCategoriesAction() {
        return new FakeFetchCategoriesAction();
    }

    @Bean
    public MakeZipArchiveAction makeZipArchive() {
        return new FakeMakeZipArchivesAction();
    }

    @Bean
    public SendEmailAction sendEmail() {
        return new FakeSendEmailAction();
    }
}
