package ru.clevertec.sm.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import ru.clevertec.sm.dto.Product;
import ru.clevertec.sm.service.ProductApiService;
import ru.clevertec.sm.statemachine.Events;
import ru.clevertec.sm.statemachine.States;
import ru.clevertec.sm.util.ProductsSMConstants;
import ru.clevertec.sm.util.StateMachineUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
@EnableStateMachine
@RequiredArgsConstructor
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {

    private final ProductApiService productApiService;

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {
        config.withConfiguration()
                .listener(listener());
    }

    private StateMachineListener<States, Events> listener() {
        return new StateMachineListenerAdapter<>() {
            @Override
            public void transition(Transition<States, Events> transition) {
                log.warn("Transition from <{}> to <{}>",
                        ofNullableState(transition.getSource()),
                        ofNullableState(transition.getTarget()));
            }

            @Override
            public void eventNotAccepted(Message<Events> event) {
                log.error("Event not accepted: <{}>", event);
            }

            private Object ofNullableState(State<States, Events> s) {
                return Optional.ofNullable(s)
                        .map(State::getId)
                        .orElse(null);
            }
        };
    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
        states.withStates()
                .initial(States.STARTED)
                .states(new HashSet<>(Arrays.asList(States.values())));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
        transitions
                .withExternal()
                .source(States.STARTED)
                .target(States.CATEGORY_PROCESSING)
                .event(Events.FETCH_CATEGORIES)
                .action(fetchCategories())
                .and()
                .withExternal()
                .source(States.STARTED)
                .target(States.CATEGORY_PROCESSING)
                .event(Events.STARTED_WITH_CATEGORY)
                .action(fetchProductsByCategory())
                .and()
                .withExternal()
                .source(States.CATEGORY_PROCESSING)
                .target(States.MAKING_CSV_FILES)
                .event(Events.PRODUCTS_FETCHED)
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
                .withExternal()
                .source(States.MAKING_ZIP_ARCHIVES)
                .target(States.IDLE)
                .event(Events.FINISH_ZIP_ARCHIVES)
                .and()
                .withInternal()
                .source(States.IDLE)
                .event(Events.SEND_EMAIL)
//                .action(sendEmailToSubscribers())
        ;
    }

    @Bean
    public Action<States, Events> fetchProductsByCategory() {
        return context -> {
            String category = context
                    .getExtendedState()
                    .get(ProductsSMConstants.VARIABLE_CURRENT_CATEGORY, String.class);
            List<Product> products = productApiService.fetchProductsByCategory(category);
            StateMachineUtil.putVariableToSM(
                    context.getStateMachine(),
                    ProductsSMConstants.VARIABLE_PRODUCTS_TO_PROCESS,
                    products
            );
            StateMachineUtil.sendEventToSM(
                    context.getStateMachine(),
                    Events.PRODUCTS_FETCHED
            );
        };
    }

    @Bean
    public Action<States, Events> fetchCategories() {
        return context -> {
            List<String> categories = productApiService.fetchSortedCategories();
            StateMachineUtil.putVariableToSM(
                    context.getStateMachine(),
                    ProductsSMConstants.VARIABLE_CATEGORIES,
                    categories
            );
            StateMachineUtil.sendEventToSM(
                    context.getStateMachine(),
                    Events.FETCH_CATEGORIES
            );
        };
    }
}
