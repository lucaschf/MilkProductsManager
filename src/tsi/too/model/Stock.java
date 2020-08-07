package tsi.too.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Stock {
    private static Stock instance;
    private ArrayList<Product> items = new ArrayList<>();

    private Stock() {
    }

    public static Stock getInstance() {
        synchronized (Stock.class) {
            if (instance == null) {
                instance = new Stock();
            }

            return instance;
        }
    }

    public int size() {
        return items.size();
    }

    public void insert(Product product) {
        items.add(product);
    }

    public void remove(Product product) {
        items.removeIf(p -> p.getCode() == product.getCode());
    }

    public List<Product> getItems() {
        return Collections.unmodifiableList(items);
    }

    public List<Product> getItems(Predicate<? super Product> predicate) {
        return items.stream().filter(predicate).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return String.format("Stock{items=%s}", items);
    }
}