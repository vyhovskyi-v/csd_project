package com.github.vyhovskyi.dao.jdbc;

import com.github.vyhovskyi.dao.ProductDao;
import com.github.vyhovskyi.entity.Product;
import com.github.vyhovskyi.entity.ProductFilter;
import com.github.vyhovskyi.exception.ServerException;

import java.sql.*;
import java.util.*;

public class JdbcProductDao implements ProductDao {

    private static final String GET_ALL = "SELECT * FROM `product` JOIN `group` USING(group_id)";
    private static final String GET_BY_ID = "SELECT * FROM  `product` JOIN `group` USING(group_id) WHERE `product_id` = ?";
    private static final String GET_BY_NAME = "SELECT * FROM `product` JOIN `group` USING(group_id) WHERE `product_name` = ?";
    private static final String CREATE = "INSERT INTO `product` (product_name, group_id, product_description, product_manufacturer, product_quantity, product_price) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE `product` SET product_name=?, group_id=?, product_description=?, product_manufacturer=?, product_price=? WHERE `product_id` = ?";
    private static final String DELETE = "DELETE FROM `product` WHERE `product_id` = ?";
    private static final String INCREASE_QUANTITY = "UPDATE  `product` SET product_quantity=product_quantity+? WHERE `product_id` = ?";
    private static final String DECREASE_QUANTITY = "UPDATE  `product` SET product_quantity=product_quantity-? WHERE `product_id` = ? AND product_quantity>=?";

    private final Connection connection;

    public JdbcProductDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        try(Statement query = connection.createStatement(); ResultSet rs = query.executeQuery(GET_ALL)){
            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
        }catch (SQLException e) {
            throw new ServerException(e);
        }
        return products;
    }

    @Override
    public Optional<Product> findById(Integer id) {
        Optional<Product> product = Optional.empty();
        try (PreparedStatement query = connection.prepareStatement(GET_BY_ID)){
            query.setInt(1, id);
            ResultSet rs = query.executeQuery();
            if (rs.next()) {
                product =  Optional.of(extractProductFromResultSet(rs));
            }
        }catch (SQLException e) {
            throw new ServerException(e);
        }
        return product;
    }

    @Override
    public Optional<Product> findByName(String name) {
        Optional<Product> product = Optional.empty();
        try (PreparedStatement query = connection.prepareStatement(GET_BY_NAME)){
            query.setString(1, name);
            ResultSet rs = query.executeQuery();
            if (rs.next()) {
                product =  Optional.of(extractProductFromResultSet(rs));
            }
        }catch (SQLException e) {
            throw new ServerException(e);
        }
        return product;
    }

    @Override
    public Integer create(Product product) {
        try(PreparedStatement query = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)){
            query.setString(1, product.getName());
            query.setInt(2, product.getGroup().getId());
            query.setString(3, product.getDescription());
            query.setInt(4, product.getManufacturerId());
            query.setInt(5, product.getQuantity());
            query.setBigDecimal(6, product.getPrice());
            query.executeUpdate();

            try (ResultSet generatedKeys = query.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Integer id = generatedKeys.getInt(1); // отримання згенерованого id
                    return id;
                } else {
                    throw new SQLException("Creating product failed, no ID obtained.");
                }
            }

        }catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public void update(Product product) {
        try(PreparedStatement query = connection.prepareStatement(UPDATE)){
            query.setString(1, product.getName());
            query.setInt(2, product.getGroup().getId());
            query.setString(3, product.getDescription());
            query.setInt(4, product.getManufacturerId());
            query.setBigDecimal(5, product.getPrice());
            query.setInt(6, product.getId());
            query.executeUpdate();
        }catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        try(PreparedStatement query = connection.prepareStatement(DELETE)){
            query.setInt(1, id);
            query.executeUpdate();
        }catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public void increaseQuantity(Integer id, int quantity) {
        try(PreparedStatement query = connection.prepareStatement(INCREASE_QUANTITY)){
            query.setInt(1, quantity);
            query.setInt(2, id);
            query.executeUpdate();
        }catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public boolean decreaseQuantity(Integer id, int quantity) {
        try(PreparedStatement query = connection.prepareStatement(DECREASE_QUANTITY)){
            query.setInt(1, quantity);
            query.setInt(2, id);
            query.setInt(3, quantity);

            int rowsUpdated = query.executeUpdate();
            return rowsUpdated > 0;
        }catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public List<Product> getProductsByFilter(ProductFilter filter, int limit, int offset) {
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM `product` JOIN `group` USING(group_id) WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (filter.getName() != null && !filter.getName().isEmpty()) {
            queryBuilder.append(" AND LOWER(`product_name`) LIKE ? ");
            params.add("%" + filter.getName().toLowerCase() + "%");
        }

        if (filter.getGroupName() != null && !filter.getGroupName().isEmpty()) {
            queryBuilder.append(" AND LOWER(`group_name`) LIKE ? ");
            params.add("%" + filter.getGroupName().toLowerCase() + "%");
        }

        if (filter.getDescription() != null && !filter.getDescription().isEmpty()) {
            queryBuilder.append(" AND LOWER(`product_description`) LIKE ? ");
            params.add("%" + filter.getDescription().toLowerCase() + "%");
        }

        if (filter.getManufacturerId() != null) {
            queryBuilder.append(" AND `product_manufacturer` = ? ");
            params.add(filter.getManufacturerId());
        }

        if (filter.getMinQuantity() != null) {
            queryBuilder.append(" AND `product_quantity` >= ? ");
            params.add(filter.getMinQuantity());
        }

        if (filter.getMaxQuantity() != null) {
            queryBuilder.append(" AND `product_quantity` <= ? ");
            params.add(filter.getMaxQuantity());
        }

        if(filter.getMinPrice() != null) {
            queryBuilder.append(" AND `product_price` >= ? ");
            params.add(filter.getMinPrice());
        }

        if(filter.getMaxPrice() != null) {
            queryBuilder.append(" AND `product_price` <= ? ");
            params.add(filter.getMaxPrice());
        }

        if (limit > 0) {
            queryBuilder.append(" LIMIT ? ");
            params.add(limit);

            if (offset > 0) {
                queryBuilder.append(" OFFSET ? ");
                params.add(offset);
            }
        }

        List<Product> products = new ArrayList<>();
        try(PreparedStatement query = connection.prepareStatement(queryBuilder.toString())){
            for (int i = 0; i < params.size(); i++) {
                query.setObject(i + 1, params.get(i));
            }
            ResultSet rs = query.executeQuery();
            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
        }catch (SQLException e) {
            throw new ServerException(e);
        }
        return products;

    }

    @Override
    public void close(){
        try{
            connection.close();
        }catch (SQLException e) {
            throw new ServerException("Error closing connection", e);
        }
    }

    protected static Product extractProductFromResultSet(ResultSet rs) throws SQLException {
        return Product.builder()
                .id(rs.getInt("product_id"))
                .group(JdbcGroupDao.extractGroupFromResultSet(rs))
                .name(rs.getString("product_name"))
                .description(rs.getString("product_description"))
                .manufacturerId(rs.getInt("product_manufacturer"))
                .quantity(rs.getInt("product_quantity"))
                .price(rs.getBigDecimal("product_price"))
                .build();
    }
}
