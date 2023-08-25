package ru.clevertec.sm.util;

import lombok.experimental.UtilityClass;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import reactor.core.publisher.Mono;
import ru.clevertec.sm.statemachine.Events;
import ru.clevertec.sm.statemachine.States;

@UtilityClass
public class StateMachineUtil {

    public static void sendEventToSM(StateMachine<States, Events> stateMachine, Events event) {
        stateMachine.sendEvent(Mono.just(
                        MessageBuilder
                                .withPayload(event)
                                .build()
                ))
                .subscribe();
    }

    public static void putVariableToSM(StateMachine<States, Events> sm, Object key, Object value) {
        sm.getExtendedState()
                .getVariables()
                .put(key, value);
    }
}
