package ru.clevertec.sm.statemachine.action;

import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import ru.clevertec.sm.dto.Product;
import ru.clevertec.sm.service.CsvService;
import ru.clevertec.sm.service.ProductApiService;
import ru.clevertec.sm.statemachine.Event;
import ru.clevertec.sm.statemachine.State;
import ru.clevertec.sm.util.ServiceConstants;
import ru.clevertec.sm.util.StateMachineUtil;

import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
public class MakeCsvFilesAction implements Action<State, Event> {

    private final CsvService csvService;
    private final ProductApiService productApiService;

    @Override
    public void execute(StateContext<State, Event> context) {
        Iterator<String> categoryIter = context.getExtendedState()
                .get(ServiceConstants.CURRENT_CATEGORY_ITERATOR, Iterator.class);
        Event nextEvent = Event.MAKE_ZIP_ARCHIVES;
        if (categoryIter.hasNext()) {
            String category = categoryIter.next();
            List<Product> products = productApiService.fetchProductsByCategory(category);
            csvService.writeProductsToCSV(products, category);
            nextEvent = Event.MAKE_CSV_FILES;
        }
        StateMachineUtil.sendEvent(context.getStateMachine(), nextEvent);
    }
}
