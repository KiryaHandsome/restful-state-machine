package ru.clevertec.sm.util;

import lombok.experimental.UtilityClass;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import reactor.core.publisher.Mono;
import ru.clevertec.sm.statemachine.Event;
import ru.clevertec.sm.statemachine.State;

@UtilityClass
public class StateMachineUtil {

    public static void sendEventToSM(StateMachine<State, Event> stateMachine, Event event) {
        stateMachine.sendEvent(Mono.just(
                        MessageBuilder
                                .withPayload(event)
                                .build()
                ))
                .subscribe();
    }

    public static void putVariableToSM(StateMachine<State, Event> sm, Object key, Object value) {
        sm.getExtendedState()
                .getVariables()
                .put(key, value);
    }
}
