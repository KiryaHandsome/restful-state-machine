package ru.clevertec.sm.util.action;

import org.springframework.statemachine.StateContext;
import ru.clevertec.sm.statemachine.Event;
import ru.clevertec.sm.statemachine.State;
import ru.clevertec.sm.statemachine.action.MakeZipArchiveAction;

public class FakeMakeZipArchivesAction extends MakeZipArchiveAction {

    @Override
    public void execute(StateContext<State, Event> context) {
        //do nothing
    }
}
