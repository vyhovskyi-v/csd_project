package com.github.vyhovskyi;


import com.github.vyhovskyi.dao.DBConnectionManager;
import com.github.vyhovskyi.dao.GroupDao;
import com.github.vyhovskyi.dao.ProductDao;
import com.github.vyhovskyi.dao.jdbc.JdbcGroupDao;
import com.github.vyhovskyi.dao.jdbc.JdbcProductDao;
import com.github.vyhovskyi.entity.Group;
import com.github.vyhovskyi.entity.Product;
import com.github.vyhovskyi.entity.ProductFilter;
import com.github.vyhovskyi.service.GroupService;
import com.github.vyhovskyi.service.ProductService;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try(Connection connection = DBConnectionManager.getConnection()){
            GroupDao groupDao = new JdbcGroupDao(connection);
            GroupService groupService = new GroupService(groupDao);
            ProductDao productDao = new JdbcProductDao(connection);
            ProductService productService = new ProductService(productDao);

            ProductFilter filter = new  ProductFilter();
            filter.setName("pasta");
            List<Product> productList = productService.getProductsByFilter(filter, 10,0);
            System.out.println(Arrays.toString(productList.toArray()));

       }catch (Exception e){
            e.printStackTrace();
        }
    }
}