package ru.clevertec.sm.util.action;

import org.springframework.statemachine.StateContext;
import ru.clevertec.sm.statemachine.Event;
import ru.clevertec.sm.statemachine.State;
import ru.clevertec.sm.statemachine.action.MakeZipArchivesAction;

public class FakeMakeZipArchivesAction extends MakeZipArchivesAction {

    public FakeMakeZipArchivesAction() {
        super(null);
    }

    @Override
    public void execute(StateContext<State, Event> context) {
        //do nothing
    }
}
