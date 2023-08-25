package ru.clevertec.sm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.clevertec.sm.service.StateMachineService;
import ru.clevertec.sm.statemachine.Events;
import ru.clevertec.sm.statemachine.States;
import ru.clevertec.sm.util.ProductsSMConstants;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StateMachineServiceImpl implements StateMachineService {

    private final StateMachine<States, Events> stateMachine;

    /**
     * Starts state machine and send event {@link Events#FETCH_CATEGORIES} to it.
     *
     * @param sendEmail
     * @param category
     */
    @Override
    public void launch(boolean sendEmail, Optional<String> category) {
        stateMachine.startReactively()
                .doOnSuccess(ignored -> shouldSendEmail(sendEmail))
                .subscribe();
        if (category.isPresent()) {
            putVariableToSM(ProductsSMConstants.VARIABLE_CURRENT_CATEGORY, category.get());
            sendEvent(Events.STARTED_WITH_CATEGORY);
        } else {
            stateMachine.sendEvent(Mono.just(
                            MessageBuilder
                                    .withPayload(Events.FETCH_CATEGORIES)
                                    .build()
                    ))
                    .subscribe();
//            sendEvent(Events.FETCH_CATEGORIES);
        }
    }

    @Override
    public Map<?, ?> getVariables() {
        return stateMachine.getExtendedState()
                .getVariables();
    }

    @Override
    public States getCurrentState() {
        return stateMachine.getState().getId();
    }

    private void putVariableToSM(Object key, Object value) {
        stateMachine.getExtendedState()
                .getVariables()
                .put(key, value);
    }

    public void sendEvent(Events event) {
        stateMachine.sendEvent(Mono.just(
                        MessageBuilder
                                .withPayload(event)
                                .build()
                ))
                .subscribe();
    }

    private void shouldSendEmail(boolean sendEmail) {
        stateMachine.getExtendedState()
                .getVariables()
                .put("sendEmail", sendEmail);
    }
}
