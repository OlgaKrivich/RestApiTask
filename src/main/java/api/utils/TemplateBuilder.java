package api.utils;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.WriteContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TemplateBuilder {
    private final DocumentContext template;

    private final List<Consumer<WriteContext>> modifiers = new ArrayList<>();

    public TemplateBuilder(String template) {
        this.template = JsonPath.parse(template);
    }

    public static TemplateBuilder of(String template) {
        return new TemplateBuilder(template);
    }

    public TemplateBuilder withInsert(String jsonPath, String key, Object value) {
        Consumer<WriteContext> c = template -> template.put(jsonPath, key, value);
        modifiers.add(c);
        return this;
    }

    public TemplateBuilder withDelete(String key) {
        Consumer<WriteContext> c = template -> template.delete(key);
        modifiers.add(c);
        return this;
    }

    public TemplateBuilder withUpdate(String key, String newValue) {
        Consumer<WriteContext> c = template -> template.set(key, newValue);
        modifiers.add(c);
        return this;
    }

    public String build() {
        DocumentContext value = template;
        modifiers.forEach(modifier -> modifier.accept(value));
        modifiers.clear();
        return value.jsonString();
    }
}
