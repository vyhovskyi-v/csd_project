package com.github.vyhovskyi.service;

import com.github.vyhovskyi.dao.ProductDao;
import com.github.vyhovskyi.entity.Product;
import com.github.vyhovskyi.entity.ProductFilter;
import com.github.vyhovskyi.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public class ProductService {
    private final ProductDao productDao;
    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> getProducts() {
        try{
            return productDao.findAll();
        }catch(Exception e){
            throw new ServiceException("Failed to get products", e);
        }
    }

    public Optional<Product> getProductById(Integer id) {
        try{
            return productDao.findById(id);
        }catch(Exception e){
            throw new ServiceException("Failed to get product by name", e);
        }
    }

    public Optional<Product> getProductByName(String name) {
        try{
            return productDao.findByName(name);
        }catch(Exception e){
            throw new ServiceException("Failed to get product by name", e);
        }
    }

    public Integer createProduct(Product product) {
        try{
            return productDao.create(product);
        }catch (Exception e){
            throw new ServiceException("Failed to add product", e);
        }
    }

    public void updateProduct(Product product) {
        try{
            productDao.update(product);
        }catch (Exception e){
            throw new ServiceException("Failed to update product", e);
        }
    }

    public void deleteProduct(Integer id) {
        boolean success;
        try{
            productDao.delete(id);
        }catch (Exception e){
            throw new ServiceException("Failed to delete product", e);
        }
    }

    public void increaseStock(Integer id, int amount) {
        try{
            productDao.increaseQuantity(id, amount);
        }catch (Exception e){
            throw new ServiceException("Failed to increase stock", e);
        }
    }

    public void decreaseStock(Integer id, int amount) {
        boolean success;
        try{
            success = productDao.decreaseQuantity(id, amount);
        }catch (Exception e){
            throw new ServiceException("Failed to decrease stock", e);
        }

        if(!success){
            throw new ServiceException("Not enough product in stock or invalid product ID");
        }
    }

    public List<Product> getProductsByFilter(ProductFilter filter, int limit, int offset){
        try{
            return  productDao.getProductsByFilter(filter, limit, offset);
        }catch (Exception e){
            throw new ServiceException("Failed to get products by filter", e);
        }
    }
}
