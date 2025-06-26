//package com.github.vyhovskyi.dao.jdbc;
//
//import com.github.vyhovskyi.entity.Group;
//import org.junit.jupiter.api.*;
//
//import java.nio.file.*;
//import java.sql.*;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class JdbcGroupDaoTest {
//
//    private Connection connection;
//    private JdbcGroupDao dao;
//
//    @BeforeEach
//    void setUp() throws Exception {
//        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
//        String sql = Files.readString(Paths.get("src/test/resources/test_group_schema.sql"));
//        try (Statement stmt = connection.createStatement()) {
//            for (String command : sql.split(";")) {
//                if (!command.trim().isEmpty()) {
//                    stmt.execute(command);
//                }
//            }
//        }
//        dao = new JdbcGroupDao(connection);
//    }
//
//    @AfterEach
//    void tearDown() throws Exception {
//        connection.close();
//    }
//
//    @Test
//    void testCreateAndGetGroupById() {
//        Group group = new Group("Beverages", "Hot and cold drinks");
//        dao.createGroup(group);
//
//        Optional<Group> found = dao.getGroupById("Beverages");
//        assertTrue(found.isPresent());
//        assertEquals("Hot and cold drinks", found.get().getDescription());
//    }
//
//    @Test
//    void testUpdateGroup() {
//        dao.createGroup(new Group("Dairy", "Milk-based products"));
//        dao.updateGroup(new Group("Dairy", "Updated description"), "Dairy");
//
//        Optional<Group> updated = dao.getGroupById("Dairy");
//        assertTrue(updated.isPresent());
//        assertEquals("Updated description", updated.get().getDescription());
//    }
//
//    @Test
//    void testDeleteGroup() {
//        dao.createGroup(new Group("Snacks", "Chips, crackers"));
//        dao.deleteGroup("Snacks");
//
//        Optional<Group> deleted = dao.getGroupById("Snacks");
//        System.out.println("DELETED: " + deleted);
//        assertFalse(deleted.isPresent());
//    }
//
//    @Test
//    void testGetAllGroups() {
//        dao.createGroup(new Group("Vegetables", "Fresh vegetables"));
//        dao.createGroup(new Group("Fruits", "Seasonal fruits"));
//
//        List<Group> all = dao.getAllGroups();
//        assertEquals(2, all.size());
//    }
//
//    @Test
//    void testGetGroupsByFilter() {
//        dao.createGroup(new Group("Grains", "Buckwheat, rice"));
//        dao.createGroup(new Group("Flour", "Wheat, rye"));
//
//        List<Group> filtered = dao.getGroupsByFilter("Grain", null);
//        assertEquals(1, filtered.size());
//        assertEquals("Grains", filtered.get(0).getName());
//    }
//}