package ru.clevertec.sm.statemachine.action;

import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import ru.clevertec.sm.service.ZipService;
import ru.clevertec.sm.statemachine.Event;
import ru.clevertec.sm.statemachine.State;
import ru.clevertec.sm.util.StateMachineUtil;

@RequiredArgsConstructor
public class MakeZipArchiveAction implements Action<State, Event> {

    private final ZipService zipService;

    @Override
    public void execute(StateContext<State, Event> context) {
        zipService.createArchives();
        StateMachineUtil.sendEvent(context.getStateMachine(), Event.FINISH_ZIP_ARCHIVES);
    }
}
