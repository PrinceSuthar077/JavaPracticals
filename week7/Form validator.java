import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface NotBlank {}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MaxLength {
    int value();
}

class SignupForm {
    @NotBlank
    String name;

    @NotBlank
    @MaxLength(10)
    String username;

    SignupForm(String name, String username) {
        this.name = name;
        this.username = username;
    }
}

public class Main {
    public static void main(String[] args) throws Exception {

        SignupForm form = new SignupForm("", "abcdefghijk");

        List<String> errors = new ArrayList<>();

        for (Field field : SignupForm.class.getDeclaredFields()) {
            field.setAccessible(true);
            String value = (String) field.get(form);

            if (field.isAnnotationPresent(NotBlank.class)) {
                if (value == null || value.trim().isEmpty())
                    errors.add(field.getName() + " cannot be blank");
            }

            if (field.isAnnotationPresent(MaxLength.class)) {
                int max = field.getAnnotation(MaxLength.class).value();

                if (value != null && value.length() > max)
                    errors.add(field.getName() + " is too long");
            }
        }

        for (String error : errors)
            System.out.println(error);
    }
}
