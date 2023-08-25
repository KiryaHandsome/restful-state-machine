package ru.clevertec.sm.service;

import ru.clevertec.sm.statemachine.Events;
import ru.clevertec.sm.statemachine.States;

import java.util.Map;
import java.util.Optional;

public interface StateMachineService {

    void launch(boolean sendEmail, Optional<String> category);

    Map<?, ?> getVariables();

    States getCurrentState();

    void sendEvent(Events event);
}
