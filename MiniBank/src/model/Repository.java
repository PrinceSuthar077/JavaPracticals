package model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import model.annotation.Id;

public class Repository<T> {

    private final HashMap<String, T> data = new HashMap<>();

    public T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null.");
        }

        Class<?> type = entity.getClass();

        while (type != null) {
            for (Field field : type.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    try {
                        field.setAccessible(true);
                        Object value = field.get(entity);

                        if (value == null) {
                            throw new IllegalArgumentException(
                                    "Entity ID cannot be null.");
                        }

                        String id = value.toString();
                        data.put(id, entity);
                        return entity;

                    } catch (IllegalAccessException e) {
                        throw new IllegalArgumentException(
                                "Unable to access entity ID.", e);
                    }
                }
            }

            type = type.getSuperclass();
        }

        throw new IllegalArgumentException(
                "No field marked with @Id found.");
    }

    public T findById(String id) {
        return data.get(id);
    }

    public ArrayList<T> findAll() {
        return new ArrayList<>(data.values());
    }

    public boolean delete(String id) {
        return data.remove(id) != null;
    }

    @SafeVarargs
    public final void saveAll(T... items) {
        for (T item : items) {
            save(item);
        }
    }

    public static <N extends Number> double average(N[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException(
                    "Values cannot be null or empty.");
        }

        double sum = 0;

        for (N value : values) {
            if (value == null) {
                throw new IllegalArgumentException(
                        "Values cannot contain null.");
            }

            sum += value.doubleValue();
        }

        return sum / values.length;
    }
}
