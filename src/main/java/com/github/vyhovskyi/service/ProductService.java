package com.github.vyhovskyi.service;

import com.github.vyhovskyi.dao.ProductDao;
import com.github.vyhovskyi.entity.Product;
import com.github.vyhovskyi.entity.ProductFilter;
import com.github.vyhovskyi.exception.ServerException;
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

    public Optional<Product> getProductByName(String name) {
        try{
            return productDao.findByName(name);
        }catch(Exception e){
            throw new ServiceException("Failed to get product by name", e);
        }
    }

    public void addProduct(Product product) {
        try{
            productDao.create(product);
        }catch (Exception e){
            throw new ServiceException("Failed to add product", e);
        }
    }

    public void updateProduct(Product product, String oldName) {
        try{
            productDao.update(product, oldName);
        }catch (Exception e){
            throw new ServiceException("Failed to update product", e);
        }
    }

    public void deleteProduct(String name) {
        try{
            productDao.delete(name);
        }catch (Exception e){
            throw new ServiceException("Failed to delete product", e);
        }
    }

    public void increaseStock(String productName, int amount) {
        try{
            productDao.increaseQuantity(productName, amount);
        }catch (Exception e){
            throw new ServiceException("Failed to increase stock", e);
        }
    }

    public void decreaseStock(String productName, int amount) {
        try{
            productDao.decreaseQuantity(productName, amount);
        }catch (Exception e){
            throw new ServiceException("Failed to decrease stock", e);
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
