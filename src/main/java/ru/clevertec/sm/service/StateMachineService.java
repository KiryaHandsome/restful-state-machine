package ru.clevertec.sm.service;

import ru.clevertec.sm.statemachine.State;

import java.util.Optional;

public interface StateMachineService {

    void launch(boolean sendEmail, Optional<String> category);

    Object getVariable(Object key);

    State getCurrentState();

    String buildReport();
}
