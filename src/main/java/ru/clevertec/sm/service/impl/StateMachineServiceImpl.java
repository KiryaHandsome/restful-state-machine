package ru.clevertec.sm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.clevertec.sm.service.StateMachineService;
import ru.clevertec.sm.statemachine.Event;
import ru.clevertec.sm.statemachine.State;
import ru.clevertec.sm.util.SMConstants;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StateMachineServiceImpl implements StateMachineService {

    private final StateMachine<State, Event> stateMachine;

    /**
     * Starts state machine and send event {@link Event#FETCH_CATEGORIES} to it.
     *
     * @param sendEmail should send email after completion or not
     * @param category if present machine will process only it,
     *                 otherwise machine will fetch categories
     */
    @Override
    public void launch(boolean sendEmail, Optional<String> category) {
        stateMachine.startReactively()
                .doOnSuccess(ignored -> shouldSendEmail(sendEmail))
                .subscribe();
        if (category.isPresent()) {
            putVariableToSM(SMConstants.CURRENT_CATEGORY, category.get());
            sendEvent(Event.FETCH_PRODUCTS);
        } else {
            sendEvent(Event.FETCH_CATEGORIES);
        }
    }

    @Override
    public Map<?, ?> getVariables() {
        return stateMachine.getExtendedState()
                .getVariables();
    }

    @Override
    public State getCurrentState() {
        return stateMachine.getState().getId();
    }

    private void putVariableToSM(Object key, Object value) {
        stateMachine.getExtendedState()
                .getVariables()
                .put(key, value);
    }

    public void sendEvent(Event event) {
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
                .put(SMConstants.SEND_EMAIL, sendEmail);
    }
}
