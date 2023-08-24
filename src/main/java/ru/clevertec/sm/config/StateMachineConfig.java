package ru.clevertec.sm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import ru.clevertec.sm.statemachine.Events;
import ru.clevertec.sm.statemachine.States;

@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {
        config.withConfiguration();
    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
        states.withStates()
                .initial(States.STARTED)
//                .state(States.STARTED, fetchCategories())
                .state(States.CATEGORY_PROCESSING)
                .state(States.MAKING_CSV_FILES)
                .state(States.MAKING_ZIP_ARCHIVES)
                .state(States.SENDING_EMAIL)
//                .state(States.FINISHED, saveState())
                .end(States.FINISHED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
        transitions.withExternal()
                .source(States.STARTED)
                .target(States.CATEGORY_PROCESSING)
                .event(Events.REQUEST_CATEGORIES)
                .and()
                .withExternal()
                .source(States.CATEGORY_PROCESSING)
                .target(States.MAKING_CSV_FILES)
                .event(Events.REQUEST_PRODUCTS)
                .and()
                .withExternal()
                .source(States.MAKING_CSV_FILES)
                .target(States.CATEGORY_PROCESSING)
                .event(Events.CATEGORIES_REMAINED)
                .and()
                .withExternal()
                .source(States.MAKING_CSV_FILES)
                .target(States.MAKING_ZIP_ARCHIVES)
                .event(Events.FINISH_CSV_FILES)
                .and()
                .withInternal()
                .source(States.FINISHED)
                .event(Events.SEND_EMAIL)
//                .action(sendEmailToSubscribers())
        ;
    }
}
