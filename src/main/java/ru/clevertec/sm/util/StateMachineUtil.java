package ru.clevertec.sm.util;

import lombok.experimental.UtilityClass;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import reactor.core.publisher.Mono;
import ru.clevertec.sm.statemachine.Event;
import ru.clevertec.sm.statemachine.State;

@UtilityClass
public class StateMachineUtil {

    /**
     * Sends event to passed state machine.
     *
     * @param stateMachine machine to pass event
     * @param event        event to pass to machine
     */
    public static void sendEvent(StateMachine<State, Event> stateMachine, Event event) {
        Message<Event> message = MessageBuilder.withPayload(event).build();
        stateMachine.sendEvent(Mono.just(message))
                .subscribe();
    }

    /**
     * Puts variable to passed state machine.
     *
     * @param sm    machine to put variable
     * @param key   key of variable
     * @param value value to put
     */
    public static void putVariable(StateMachine<State, Event> sm, Object key, Object value) {
        sm.getExtendedState()
                .getVariables()
                .put(key, value);
    }
}
