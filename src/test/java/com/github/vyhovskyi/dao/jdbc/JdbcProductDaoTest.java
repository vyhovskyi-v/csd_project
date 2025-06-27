package com.github.vyhovskyi.dao.jdbc;

import com.github.vyhovskyi.entity.Group;
import com.github.vyhovskyi.entity.Product;
import com.github.vyhovskyi.entity.ProductFilter;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class JdbcProductDaoTest {

    private static Connection connection;
    private JdbcProductDao productDao;
    private JdbcGroupDao groupDao;

    @BeforeAll
    static void initDB() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE `group` (group_id INT AUTO_INCREMENT PRIMARY KEY, group_name VARCHAR(50), group_description VARCHAR(1000))");
            stmt.execute("CREATE TABLE `product` (product_id INT AUTO_INCREMENT PRIMARY KEY, product_name VARCHAR(50), group_id INT, product_description VARCHAR(1000), product_manufacturer INT, product_quantity INT, product_price DECIMAL(13,4), FOREIGN KEY (group_id) REFERENCES `group`(group_id))");
        }
    }

    @BeforeEach
    void setUp() {
        productDao = new JdbcProductDao(connection);
        groupDao = new JdbcGroupDao(connection);
        clearTables();
    }

    @AfterAll
    static void tearDown() throws SQLException {
        connection.close();
    }

    private void clearTables() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM product");
            stmt.execute("DELETE FROM `group`");
        } catch (SQLException e) {
            fail("Failed to clear tables: " + e.getMessage());
        }
    }

    @Test
    void testCreateAndFindById() {
        Group group = new Group(null, "Test Group", "Group Desc");
        Integer groupId = groupDao.createGroup(group);
        group.setId(groupId);

        Product product = Product.builder()
                .name("TestProduct")
                .group(group)
                .description("Test Desc")
                .manufacturerId(123)
                .quantity(10)
                .price(new BigDecimal("19.99"))
                .build();

        Integer productId = productDao.create(product);
        assertNotNull(productId);

        Product fetched = productDao.findById(productId).orElseThrow();
        assertEquals("TestProduct", fetched.getName());
        assertEquals(10, fetched.getQuantity());
    }

    @Test
    void testUpdate() {
        Group group = new Group(null, "Group1", "Desc1");
        group.setId(groupDao.createGroup(group));

        Product product = Product.builder()
                .name("Product1")
                .group(group)
                .description("Desc1")
                .manufacturerId(100)
                .quantity(5)
                .price(new BigDecimal("9.99"))
                .build();

        Integer id = productDao.create(product);
        product = productDao.findById(id).orElseThrow();

        product.setName("Updated");
        product.setManufacturerId(99);
        productDao.update(product);

        Product updated = productDao.findById(id).orElseThrow();
        assertEquals("Updated", updated.getName());
        assertEquals(99, updated.getManufacturerId());
    }

    @Test
    void testIncreaseDecreaseQuantity() {
        Group group = new Group(null, "GroupX", "DescX");
        group.setId(groupDao.createGroup(group));

        Product product = Product.builder()
                .name("StockTest")
                .group(group)
                .description("Stock Desc")
                .manufacturerId(55)
                .quantity(5)
                .price(new BigDecimal("15.00"))
                .build();

        Integer id = productDao.create(product);

        productDao.increaseQuantity(id, 10);
        assertEquals(15, productDao.findById(id).orElseThrow().getQuantity());

        assertTrue(productDao.decreaseQuantity(id, 5));
        assertEquals(10, productDao.findById(id).orElseThrow().getQuantity());

        assertFalse(productDao.decreaseQuantity(id, 20)); // більше ніж наявно
    }

    @Test
    void testFindByNameAndFindAll() {
        Group group = new Group(null, "GroupF", "DescF");
        group.setId(groupDao.createGroup(group));

        Product p1 = Product.builder()
                .name("FindMe")
                .group(group)
                .description("Desc")
                .manufacturerId(1)
                .quantity(1)
                .price(BigDecimal.ONE)
                .build();
        productDao.create(p1);

        assertTrue(productDao.findByName("FindMe").isPresent());
        assertEquals(1, productDao.findAll().size());
    }

    @Test
    void testFilter() {
        Group group = new Group(null, "GroupFilter", "DescGroup");
        group.setId(groupDao.createGroup(group));

        productDao.create(Product.builder().name("A").group(group).description("Desc").manufacturerId(1).quantity(10).price(BigDecimal.TEN).build());
        productDao.create(Product.builder().name("B").group(group).description("Other").manufacturerId(2).quantity(5).price(new BigDecimal("20.00")).build());

        ProductFilter filter = ProductFilter.builder().minQuantity(6).build();
        List<Product> filtered = productDao.getProductsByFilter(filter, 10, 0);

        assertEquals(1, filtered.size());
        assertEquals("A", filtered.get(0).getName());
    }
}
