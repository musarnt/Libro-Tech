package com.riwi.librotech.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsbnValidator.class) // Apunta a la clase que hará la validación real
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidISBN {

    // Mensaje de error por defecto
    String message() default "Formato de ISBN inválido. Debe comenzar con 'ISBN-' o '978-'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}