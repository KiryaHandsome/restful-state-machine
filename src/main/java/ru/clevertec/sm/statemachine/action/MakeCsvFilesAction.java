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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MakeCsvFilesAction implements Action<State, Event> {

    private final CsvService csvService;
    private final ProductApiService productApiService;

    private static final String[] HEADER_ROW = {
            "ID",
            "Title",
            "Description",
            "Price",
            "DiscountPercentage",
            "Rating",
            "Stock",
            "Brand",
            "Category"
    };

    @Override
    public void execute(StateContext<State, Event> context) {
        Iterator<String> categoryIter = context.getExtendedState()
                .get(ServiceConstants.CURRENT_CATEGORY_ITERATOR, Iterator.class);
        Event nextEvent = Event.MAKE_ZIP_ARCHIVES;
        if (categoryIter.hasNext()) {
            String category = categoryIter.next();
            List<Product> products = productApiService.fetchProductsByCategory(category);
            var brandAndProducts = groupProductsByBrand(products);
            for (var entry : brandAndProducts.entrySet()) {
                String folderPath = buildFolderPath(entry.getKey());
                List<String[]> data = mapProductsToCsvData(entry.getValue());
                csvService.writeDataToCsv(folderPath, category, data);
            }
            nextEvent = Event.MAKE_CSV_FILES;
        }
        StateMachineUtil.sendEvent(context.getStateMachine(), nextEvent);
    }

    private Map<String, List<Product>> groupProductsByBrand(List<Product> products) {
        return products
                .stream()
                .collect(Collectors.groupingBy(Product::getBrand));
    }

    private String buildFolderPath(String brand) {
        return ServiceConstants.OUTPUT_PATH + File.separator + brand;
    }

    private List<String[]> mapProductsToCsvData(List<Product> products) {
        List<String[]> data = new ArrayList<>();
        data.add(HEADER_ROW);
        products.stream()
                .map(this::mapProductToCsvData)
                .forEach(data::add);

        return data;
    }

    private String[] mapProductToCsvData(Product product) {
        return new String[]{
                String.valueOf(product.getId()),
                product.getTitle(),
                product.getDescription(),
                String.valueOf(product.getPrice()),
                String.valueOf(product.getDiscountPercentage()),
                String.valueOf(product.getRating()),
                String.valueOf(product.getStock()),
                product.getBrand(),
                product.getCategory()
        };
    }
}
