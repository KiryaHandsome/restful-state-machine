package ru.clevertec.sm.service;

import ru.clevertec.sm.statemachine.Event;
import ru.clevertec.sm.statemachine.State;

import java.util.Map;
import java.util.Optional;

public interface StateMachineService {

    void launch(boolean sendEmail, Optional<String> category);

    Map<?, ?> getVariables();

    State getCurrentState();

    void sendEvent(Event event);
}
