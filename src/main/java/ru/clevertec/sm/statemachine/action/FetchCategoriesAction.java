package ru.clevertec.sm.statemachine.action;

import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import ru.clevertec.sm.service.ProductApiService;
import ru.clevertec.sm.statemachine.Event;
import ru.clevertec.sm.statemachine.State;
import ru.clevertec.sm.util.SMConstants;
import ru.clevertec.sm.util.StateMachineUtil;

import java.util.List;

@RequiredArgsConstructor
public class FetchCategoriesAction implements Action<State, Event> {

    private final ProductApiService productApiService;

    @Override
    public void execute(StateContext<State, Event> context) {
        List<String> categories = productApiService.fetchSortedCategories();
        var stateMachine = context.getStateMachine();
        StateMachineUtil.putVariable(
                stateMachine,
                SMConstants.CURRENT_CATEGORY_ITERATOR,
                categories.iterator()
        );
        StateMachineUtil.sendEvent(stateMachine, Event.MAKE_CSV_FILES);
    }
}
