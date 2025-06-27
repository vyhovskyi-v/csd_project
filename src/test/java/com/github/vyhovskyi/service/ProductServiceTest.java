package com.github.vyhovskyi.service;

import com.github.vyhovskyi.dao.ProductDao;
import com.github.vyhovskyi.entity.Product;
import com.github.vyhovskyi.entity.ProductFilter;
import com.github.vyhovskyi.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(productDao);
    }

    @Test
    void getProducts_shouldReturnList() {
        List<Product> expected = Arrays.asList(new Product(), new Product());
        when(productDao.findAll()).thenReturn(expected);

        List<Product> actual = productService.getProducts();

        assertEquals(expected, actual);
        verify(productDao).findAll();
    }

    @Test
    void getProductById_shouldReturnProduct() {
        Product product = new Product();
        when(productDao.findById(1)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById(1);

        assertTrue(result.isPresent());
        assertEquals(product, result.get());
    }

    @Test
    void getProductById_shouldThrowServiceExceptionOnError() {
        when(productDao.findById(1)).thenThrow(new RuntimeException("DB error"));

        assertThrows(ServiceException.class, () -> productService.getProductById(1));
    }

    @Test
    void createProduct_shouldReturnId() {
        Product product = new Product();
        when(productDao.create(product)).thenReturn(5);

        Integer id = productService.createProduct(product);

        assertEquals(5, id);
    }

    @Test
    void updateProduct_shouldCallDao() {
        Product product = new Product();
        productService.updateProduct(product);

        verify(productDao).update(product);
    }

    @Test
    void deleteProduct_shouldCallDao() {
        productService.deleteProduct(10);

        verify(productDao).delete(10);
    }

    @Test
    void increaseStock_shouldCallDao() {
        productService.increaseStock(1, 50);

        verify(productDao).increaseQuantity(1, 50);
    }

    @Test
    void decreaseStock_shouldSucceedIfEnoughQuantity() {
        when(productDao.decreaseQuantity(1, 5)).thenReturn(true);

        assertDoesNotThrow(() -> productService.decreaseStock(1, 5));
    }

    @Test
    void decreaseStock_shouldThrowIfNotEnoughStock() {
        when(productDao.decreaseQuantity(1, 100)).thenReturn(false);

        ServiceException exception = assertThrows(ServiceException.class, () -> productService.decreaseStock(1, 100));
        assertEquals("Not enough product in stock or invalid product ID", exception.getMessage());
    }

    @Test
    void getProductsByFilter_shouldReturnFilteredProducts() {
        ProductFilter filter = new ProductFilter();
        List<Product> expected = Arrays.asList(
                new Product(1, "Beer", null, "Nice beer", 1, 50, new BigDecimal("10.00"))
        );

        when(productDao.getProductsByFilter(filter, 10, 0)).thenReturn(expected);

        List<Product> actual = productService.getProductsByFilter(filter, 10, 0);

        assertEquals(expected, actual);
    }
}
