package com.github.vyhovskyi.validator;

import com.github.vyhovskyi.entity.Product;

import java.math.BigDecimal;

public class ProductValidator {

    public static boolean isValid(Product product) {
        if (product == null) {
            return false;
        }

        if (product.getName() == null || product.getName().isBlank()) {
            return false;
        }


        if (product.getDescription() == null || product.getDescription().isBlank()) {
            return false;
        }

        if (product.getManufacturerId() <= 0) {
            return false;
        }

        if (product.getQuantity() < 0) {
            return false;
        }

        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        return true;
    }

    public static boolean isValidForUpdate(Product product) {
        if (product == null) {
            return false;
        }

        if (product.getId() == null) {
            return false;
        }

        if (product.getName() != null && product.getName().isBlank()) {
            return false;
        }

        if (product.getDescription() != null && product.getDescription().isBlank()) {
            return false;
        }

        if (product.getManufacturerId() != 0 && product.getManufacturerId() <= 0) {
            return false;
        }

        if (product.getQuantity() < 0) {
            return false;
        }

        if (product.getPrice() != null && product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        return true;
    }
}
