//package com.github.vyhovskyi.dao.jdbc;
//
//import com.github.vyhovskyi.entity.Group;
//import com.github.vyhovskyi.entity.Product;
//import com.github.vyhovskyi.entity.ProductFilter;
//import org.junit.jupiter.api.*;
//
//import java.math.BigDecimal;
//import java.nio.file.*;
//import java.sql.*;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class JdbcProductDaoTest {
//
//    private Connection connection;
//    private JdbcGroupDao groupDao;
//    private JdbcProductDao productDao;
//
//    @BeforeEach
//    void setUp() throws Exception {
//        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
//        String schemaSql  = Files.readString(Paths.get("src/test/resources/test_mysql_compatible_schema.sql"));
//
//        try (Statement stmt = connection.createStatement()) {
//            for (String command : schemaSql.split(";")) {
//                if (!command.trim().isEmpty()) {
//                    stmt.execute(command);
//                }
//            }
//        }
//
//        groupDao = new JdbcGroupDao(connection);
//        productDao = new JdbcProductDao(connection);
//
//        groupDao.createGroup(new Group("TestGroup", "Test group description"));
//    }
//
//    @AfterEach
//    void tearDown() throws Exception {
//        connection.close();
//    }
//
//    @Test
//    void testCreateAndFindProduct() {
//        Product product = new Product("Water", new Group("TestGroup", ""), "Still water", 123, 50, new BigDecimal("9.99"));
//        productDao.create(product);
//
//        Optional<Product> found = productDao.findByName("Water");
//        assertTrue(found.isPresent());
//        assertEquals("Still water", found.get().getDescription());
//    }
//
//    @Test
//    void testUpdateProduct() {
//        productDao.create(new Product("Juice", new Group("TestGroup", ""), "Fruit juice", 101, 20, new BigDecimal("19.99")));
//        Product updated = new Product("Juice", new Group("TestGroup", ""), "Updated juice", 101, 30, new BigDecimal("17.50"));
//        productDao.update(updated, "Juice");
//
//        Optional<Product> found = productDao.findByName("Juice");
//        assertTrue(found.isPresent());
//        assertEquals("Updated juice", found.get().getDescription());
//    }
//
//    @Test
//    void testDeleteProduct() {
//        productDao.create(new Product("Milk", new Group("TestGroup", ""), "Fresh milk", 222, 15, new BigDecimal("14.00")));
//        productDao.delete("Milk");
//
//        Optional<Product> found = productDao.findByName("Milk");
//        assertFalse(found.isPresent());
//    }
//
//    @Test
//    void testIncreaseDecreaseQuantity() {
//        productDao.create(new Product("Bread", new Group("TestGroup", ""), "Wheat bread", 111, 10, new BigDecimal("8.00")));
//        productDao.increaseQuantity("Bread", 5);
//        productDao.decreaseQuantity("Bread", 3);
//
//        Optional<Product> found = productDao.findByName("Bread");
//        assertTrue(found.isPresent());
//        assertEquals(12, found.get().getQuantity());
//    }
//
//    @Test
//    void testGetProductsByFilter() {
//        productDao.create(new Product("Pasta", new Group("TestGroup", ""), "Durum pasta", 303, 100, new BigDecimal("20.00")));
//        productDao.create(new Product("Rice", new Group("TestGroup", ""), "White rice", 303, 80, new BigDecimal("18.00")));
//
//        ProductFilter filter = new ProductFilter();
//        filter.setName("Pasta");
//
//        List<Product> filtered = productDao.getProductsByFilter(filter, 10, 0);
//        assertEquals(1, filtered.size());
//        assertEquals("Pasta", filtered.get(0).getName());
//    }
//}