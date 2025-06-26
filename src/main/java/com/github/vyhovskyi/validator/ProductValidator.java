package com.github.vyhovskyi.validator;

import com.github.vyhovskyi.entity.Product;
import com.github.vyhovskyi.service.GroupService;
import com.github.vyhovskyi.service.ProductService;
import com.github.vyhovskyi.service.ServiceFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductValidator {


    public static List<String> validateForAdd(Product product) {
        List<String> errors = new ArrayList<>();


        if (product == null) {
            errors.add("Product cannot be null");
            return errors;
        }

        if (product.getName() == null || product.getName().isBlank()) {
            errors.add("Product name cannot be empty");
        }

        if (product.getDescription() == null || product.getDescription().isBlank()) {
            errors.add("Product description cannot be empty");
        }

        if (product.getManufacturerId() <= 0) {
            errors.add("Manufacturer ID must be greater than 0");
        }

        if (product.getQuantity() < 0) {
            errors.add("Quantity cannot be negative");
        }

        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Price must be greater than 0");
        }

        return errors;
    }

    public static List<String> validateForUpdate(Product product) {
        List<String> errors = new ArrayList<>();

        if (product == null) {
            errors.add("Product cannot be null");
            return errors;
        }

        if (product.getId() == null) {
            errors.add("Product ID is required for update");
        }

        if (product.getName() != null && product.getName().isBlank()) {
            errors.add("Product name cannot be empty if provided");
        }

        if (product.getDescription() != null && product.getDescription().isBlank()) {
            errors.add("Product description cannot be empty if provided");
        }

        if (product.getManufacturerId() != 0 && product.getManufacturerId() <= 0) {
            errors.add("Manufacturer ID must be greater than 0 if provided");
        }

        if (product.getQuantity() < 0) {
            errors.add("Quantity cannot be negative");
        }

        if (product.getPrice() != null && product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Price must be greater than 0 if provided");
        }

        return errors;
    }


}
