package com.riwi.librotech.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsbnValidator implements ConstraintValidator<ValidISBN, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // La validación de @NotNull ya se encarga de los nulos
        if (value == null || value.isBlank()) {
            return true;
        }

        // Regla de negocio LibroTech:
        // Debe comenzar con ISBN- o con 978-
        boolean empiezaCorrecto = value.startsWith("ISBN-") || value.startsWith("978-");

        // Longitud entre 9 y 17 caracteres
        boolean longitudCorrecta = value.length() >= 9 && value.length() <= 17;

        return empiezaCorrecto && longitudCorrecta;
    }
}