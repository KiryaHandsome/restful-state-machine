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
import org.springframework.statemachine.transition.Transition;
import ru.clevertec.sm.dto.Product;
import ru.clevertec.sm.service.ProductApiService;
import ru.clevertec.sm.statemachine.Event;
import ru.clevertec.sm.statemachine.State;
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
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<State, Event> {

    private final ProductApiService productApiService;

    @Override
    public void configure(StateMachineConfigurationConfigurer<State, Event> config) throws Exception {
        config.withConfiguration()
                .listener(listener());
    }

    private StateMachineListener<State, Event> listener() {
        return new StateMachineListenerAdapter<>() {
            @Override
            public void transition(Transition<State, Event> transition) {
                log.warn("Transition from <{}> to <{}>",
                        ofNullableState(transition.getSource()),
                        ofNullableState(transition.getTarget()));
            }

            @Override
            public void eventNotAccepted(Message<Event> event) {
                log.error("Event not accepted: <{}>", event);
            }

            private Object ofNullableState(org.springframework.statemachine.state.State<State, Event> s) {
                return Optional.ofNullable(s)
                        .map(org.springframework.statemachine.state.State::getId)
                        .orElse(null);
            }
        };
    }

    @Override
    public void configure(StateMachineStateConfigurer<State, Event> states) throws Exception {
        states.withStates()
                .initial(State.STARTED)
                .states(new HashSet<>(Arrays.asList(State.values())));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<State, Event> transitions) throws Exception {
        transitions
                .withExternal()
                .source(State.STARTED)
                .target(State.CATEGORY_PROCESSING)
                .event(Event.FETCH_CATEGORIES)
                .action(fetchCategories())
                .and()
                .withExternal()
                .source(State.STARTED)
                .target(State.CATEGORY_PROCESSING)
                .event(Event.FETCH_PRODUCTS)
                .action(fetchProductsByCategory())
                .and()
                .withExternal()
                .source(State.CATEGORY_PROCESSING)
                .target(State.MAKING_CSV_FILES)
                .event(Event.MAKE_CSV_FILES)
                .action(makeCsvFiles())
                .and()
                .withExternal()
                .source(State.MAKING_CSV_FILES)
                .target(State.CATEGORY_PROCESSING)
                .event(Event.CATEGORIES_REMAINED)
                .and()
                .withExternal()
                .source(State.MAKING_CSV_FILES)
                .target(State.MAKING_ZIP_ARCHIVES)
                .event(Event.FINISH_CSV_FILES)
                .and()
                .withExternal()
                .source(State.MAKING_ZIP_ARCHIVES)
                .target(State.IDLE)
                .event(Event.FINISH_ZIP_ARCHIVES)
                .and()
                .withInternal()
                .source(State.IDLE)
                .event(Event.SEND_EMAIL)
//                .action(sendEmailToSubscribers())
        ;
    }

    @Bean
    public Action<State, Event> makeCsvFiles() {
        return context -> {
            // TODO make service for this files and send event
            //check
        };
    }

    @Bean
    public Action<State, Event> fetchProductsByCategory() {
        return context -> {
            String category = context.getExtendedState()
                    .get(ProductsSMConstants.VARIABLE_CURRENT_CATEGORY, String.class);
            List<Product> products = productApiService.fetchProductsByCategory(category);
            StateMachineUtil.putVariableToSM(
                    context.getStateMachine(),
                    ProductsSMConstants.VARIABLE_PRODUCTS_TO_PROCESS,
                    products
            );
            StateMachineUtil.sendEventToSM(
                    context.getStateMachine(),
                    Event.MAKE_CSV_FILES
            );
        };
    }

    @Bean
    public Action<State, Event> fetchCategories() {
        return context -> {
            List<String> categories = productApiService.fetchSortedCategories();
            StateMachineUtil.putVariableToSM(
                    context.getStateMachine(),
                    ProductsSMConstants.VARIABLE_CATEGORIES,
                    categories
            );
            StateMachineUtil.sendEventToSM(
                    context.getStateMachine(),
                    Event.FETCH_PRODUCTS
            );
        };
    }
}
