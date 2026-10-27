package util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import model.annotation.MaxLength;
import model.annotation.Positive;

public class AnnotationValidator {

    public static String[] validate(Object obj) {
        ArrayList<String> errors = new ArrayList<>();

        if (obj == null) {
            errors.add("Object cannot be null");
            return errors.toArray(new String[0]);
        }

        Class<?> currentClass = obj.getClass();

        while (currentClass != null) {
            Field[] fields = currentClass.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);

                try {
                    Object value = field.get(obj);

                    if (field.isAnnotationPresent(Positive.class)) {
                        Positive positive = field.getAnnotation(Positive.class);

                        if (value instanceof Number && ((Number) value).doubleValue() <= 0) {
                            errors.add(field.getName() + " " + positive.message());
                        }
                    }

                    if (field.isAnnotationPresent(MaxLength.class)) {
                        MaxLength maxLength = field.getAnnotation(MaxLength.class);

                        if (value instanceof String && ((String) value).length() > maxLength.value()) {
                            errors.add(field.getName() + " must have at most " + maxLength.value() + " characters");
                        }
                    }

                } catch (IllegalAccessException e) {
                    errors.add("Unable to access field: " + field.getName());
                }
            }

            currentClass = currentClass.getSuperclass();
        }

        return errors.toArray(new String[0]);
    }
}