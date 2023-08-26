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
import ru.clevertec.sm.util.SMConstants;
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
                .state(State.MAKING_CSV_FILES, makeCsvFiles())
                .state(State.CATEGORY_PROCESSING, fetchProductsByCategory())
                .state(State.MAKING_ZIP_ARCHIVES, makeZipArchives())
                .state(State.IDLE);
    }

    private Action<State, Event> makeZipArchives() {
        return null;
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
                .target(State.MAKING_CSV_FILES)
                .event(Event.FETCH_PRODUCTS)
                .and()
                .withExternal()
                .source(State.CATEGORY_PROCESSING)
                .target(State.MAKING_CSV_FILES)
                .event(Event.FETCH_PRODUCTS)
                .and()
                .withExternal()
                .source(State.MAKING_CSV_FILES)
                .target(State.CATEGORY_PROCESSING)
                .event(Event.CATEGORIES_REMAINED)
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
                .source(State.IDLE)
                .event(Event.SEND_EMAIL)
//                .action(sendEmailToSubscribers())
        ;
    }

    @Bean
    public Action<State, Event> makeCsvFiles() {
        return context -> {
            log.info("makeCsvFiles action");
            // TODO make service for this files and send event to make zip archives
            StateMachineUtil.sendEvent(context.getStateMachine(), Event.MAKE_ZIP_ARCHIVES);
        };
    }

    @Bean
    public Action<State, Event> fetchProductsByCategory() {
        return context -> {
            log.info("fetchProductsByCategory action");
            String category = context.getExtendedState()
                    .get(SMConstants.CURRENT_CATEGORY, String.class);
            log.info("Current category: {}", category);
            List<Product> products = productApiService.fetchProductsByCategory(category);
            var stateMachine = context.getStateMachine();
            StateMachineUtil.putVariable(stateMachine, SMConstants.PRODUCTS_TO_PROCESS, products);
            log.info("send event make csv files");
        };
    }

    @Bean
    public Action<State, Event> fetchCategories() {
        return context -> {
            log.info("fetchCategories action");
            List<String> categories = productApiService.fetchSortedCategories();
            var stateMachine = context.getStateMachine();
            StateMachineUtil.putVariable(stateMachine, SMConstants.CATEGORIES, categories);
            StateMachineUtil.putVariable(
                    stateMachine,
                    SMConstants.CURRENT_CATEGORY,
                    categories.stream()
                            .findFirst()
                            .orElseThrow(RuntimeException::new) //TODO change to custom exception
            );
            StateMachineUtil.sendEvent(stateMachine, Event.FETCH_PRODUCTS);
        };
    }
}
