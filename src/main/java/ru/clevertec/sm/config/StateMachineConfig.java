package ru.clevertec.sm.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListener;
import ru.clevertec.sm.statemachine.Event;
import ru.clevertec.sm.statemachine.State;
import ru.clevertec.sm.statemachine.action.FetchCategoriesAction;
import ru.clevertec.sm.statemachine.action.MakeCsvFilesAction;
import ru.clevertec.sm.statemachine.action.MakeZipArchiveAction;
import ru.clevertec.sm.util.ServiceConstants;

import java.util.Optional;

@Slf4j
@Configuration
@EnableStateMachine
@RequiredArgsConstructor
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<State, Event> {

    private final MakeCsvFilesAction makeCsvFilesAction;
    private final MakeZipArchiveAction makeZipArchivesAction;
    private final StateMachineListener<State, Event> listener;
    private final FetchCategoriesAction fetchCategoriesAction;

    @Override
    public void configure(StateMachineConfigurationConfigurer<State, Event> config) throws Exception {
        config.withConfiguration()
                .listener(listener);
    }

    @Override
    public void configure(StateMachineStateConfigurer<State, Event> states) throws Exception {
        states.withStates()
                .initial(State.STARTED)
                .state(State.MAKING_CSV_FILES, makeCsvFilesAction)
                .state(State.CATEGORY_PROCESSING)
                .state(State.MAKING_ZIP_ARCHIVES, makeZipArchivesAction)
                .state(State.IDLE);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<State, Event> transitions) throws Exception {
        transitions
                .withExternal()
                .source(State.STARTED)
                .target(State.CATEGORY_PROCESSING)
                .event(Event.FETCH_CATEGORIES)
                .action(fetchCategoriesAction)
                .and()
                .withExternal()
                .source(State.STARTED)
                .target(State.MAKING_CSV_FILES)
                .event(Event.MAKE_CSV_FILES)
                .and()
                .withExternal()
                .source(State.CATEGORY_PROCESSING)
                .target(State.MAKING_CSV_FILES)
                .event(Event.MAKE_CSV_FILES)
                .and()
                .withExternal()
                .source(State.MAKING_CSV_FILES)
                .target(State.MAKING_ZIP_ARCHIVES)
                .event(Event.MAKE_ZIP_ARCHIVES)
                .and()
                .withExternal()
                .source(State.MAKING_ZIP_ARCHIVES)
                .target(State.IDLE)
                .event(Event.FINISH_ZIP_ARCHIVES)
                .and()
                .withInternal()
                .source(State.MAKING_CSV_FILES)
                .event(Event.MAKE_CSV_FILES)
                .action(makeCsvFilesAction)
                .and()
                .withInternal()
                .source(State.IDLE)
                .event(Event.SEND_EMAIL)
                .guard(shouldSendEmail())
//                .action(sendEmailToSubscribers())
        ;
    }

    @Bean
    Guard<State, Event> shouldSendEmail() {
        return context -> Optional.ofNullable(
                        context.getExtendedState()
                                .get(ServiceConstants.SEND_EMAIL, Boolean.class)
                )
                .orElse(ServiceConstants.SEND_EMAIL_DEFAULT);
    }
}
