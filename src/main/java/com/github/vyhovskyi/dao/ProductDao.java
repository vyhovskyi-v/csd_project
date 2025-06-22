package com.github.vyhovskyi.dao;

import com.github.vyhovskyi.entity.Product;
import com.github.vyhovskyi.entity.ProductFilter;

import java.util.List;
import java.util.Optional;

public interface ProductDao extends AutoCloseable{
     List<Product> findAll();
     Optional<Product> findByName(String productName);
     void create(Product product);
     void update(Product product, String oldProductName);
     void delete(String name);
     void increaseQuantity(String productName, int quantity);
     void decreaseQuantity(String productName, int quantity);
     List<Product> getProductsByFilter(ProductFilter filter, int limit, int offset);
     void close();
}
