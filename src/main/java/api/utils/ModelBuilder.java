package api.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModelBuilder<T> {

    private final Supplier<T> instantiator;

    private final List<Consumer<T>> instanceModifiers = new ArrayList<>();

    public ModelBuilder(Supplier<T> instantiator) {
        this.instantiator = instantiator;
    }

    public static <T> ModelBuilder<T> of(Supplier<T> instantiator) {
        return new ModelBuilder<>(instantiator);
    }

    public <U> ModelBuilder<T> with(BiConsumer<T, U> consumer, U value) {
        Consumer<T> c = instance -> consumer.accept(instance, value);
        instanceModifiers.add(c);
        return this;
    }

    public T build() {
        T value = instantiator.get();
        instanceModifiers.forEach(modifier -> modifier.accept(value));
        instanceModifiers.clear();
        return value;
    }
}
