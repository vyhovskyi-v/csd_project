package com.github.vyhovskyi.dao;

import com.github.vyhovskyi.entity.Product;
import com.github.vyhovskyi.entity.ProductFilter;

import java.util.List;
import java.util.Optional;

public interface ProductDao extends AutoCloseable{
     List<Product> findAll();
     Optional<Product> findById(Integer id);
     Optional<Product> findByName(String name);
     Integer create(Product product);
     void update(Product product);
     void delete(Integer id);
     void increaseQuantity(Integer productId, int quantity);
     boolean decreaseQuantity(Integer productId, int quantity);
     List<Product> getProductsByFilter(ProductFilter filter, int limit, int offset);
     void close();
}
