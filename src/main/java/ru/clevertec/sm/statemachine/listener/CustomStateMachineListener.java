package ru.clevertec.sm.statemachine.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;
import ru.clevertec.sm.statemachine.Event;
import ru.clevertec.sm.statemachine.State;

import java.util.Optional;


@Slf4j
@Component
public class CustomStateMachineListener extends StateMachineListenerAdapter<State, Event> {

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
}
