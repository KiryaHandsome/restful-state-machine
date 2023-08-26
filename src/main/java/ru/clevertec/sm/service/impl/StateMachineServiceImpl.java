package ru.clevertec.sm.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.iterators.SingletonIterator;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.clevertec.sm.service.StateMachineService;
import ru.clevertec.sm.statemachine.Event;
import ru.clevertec.sm.statemachine.State;
import ru.clevertec.sm.util.SMConstants;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StateMachineServiceImpl implements StateMachineService {

    private final StateMachine<State, Event> stateMachine;

    /**
     * Starts state machine.
     * If category is present it will send {@link Event#MAKE_CSV_FILES},
     * otherwise - {@link Event#FETCH_CATEGORIES}
     *
     * @param sendEmail should send email after completion or not
     * @param category  if present machine will process only it,
     *                  otherwise machine will fetch categories
     */
    @Override
    public void launch(boolean sendEmail, Optional<String> category) {
        stateMachine.startReactively().subscribe();
        shouldSendEmail(sendEmail);
        Event nextEvent = Event.FETCH_CATEGORIES;
        if (category.isPresent()) {
            Iterator<String> categoryIterator = new SingletonIterator<>(category.get());
            putVariableToSM(SMConstants.CURRENT_CATEGORY_ITERATOR, categoryIterator);
            nextEvent = Event.MAKE_CSV_FILES;
        }
        sendEvent(nextEvent);
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
