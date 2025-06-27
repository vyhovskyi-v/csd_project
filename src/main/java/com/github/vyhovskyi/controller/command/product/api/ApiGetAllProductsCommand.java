package com.github.vyhovskyi.controller.command.product.api;

import com.github.vyhovskyi.controller.command.Command;
import com.github.vyhovskyi.controller.utils.HttpSender;
import com.github.vyhovskyi.entity.Product;
import com.github.vyhovskyi.entity.ProductFilter;
import com.github.vyhovskyi.exception.ServiceException;
import com.github.vyhovskyi.service.ProductService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiGetAllProductsCommand implements Command {
    ProductService productService;

    public ApiGetAllProductsCommand(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void execute(HttpExchange exchange) throws IOException {
        ProductFilter productFilter = getProductFilter(exchange);

        try{
            List<Product> products = productService.getProductsByFilter(productFilter, 0,0);
            HttpSender.sendJson(exchange, 200, products);
            exchange.close();
        }catch(Exception e){
            exchange.sendResponseHeaders(500,0);
            exchange.close();
        }
    }

    private ProductFilter getProductFilter(HttpExchange exchange) throws IOException {
        Map<String, String> params = getQueryParams(exchange);

        ProductFilter filter = new ProductFilter();

        if (params.containsKey("name")) {
            filter.setName(params.get("name"));
        }
        if (params.containsKey("groupName")) {
            filter.setGroupName(params.get("groupName"));
        }
        if (params.containsKey("description")) {
            filter.setDescription(params.get("description"));
        }
        if (params.containsKey("manufacturerId")) {
            filter.setManufacturerId(tryParseInt(params.get("manufacturerId")));
        }
        if (params.containsKey("minQuantity")) {
            filter.setMinQuantity(tryParseInt(params.get("minQuantity")));
        }
        if (params.containsKey("maxQuantity")) {
            filter.setMaxQuantity(tryParseInt(params.get("maxQuantity")));
        }
        if (params.containsKey("minPrice")) {
            filter.setMinPrice(tryParseDecimal(params.get("minPrice")));
        }
        if (params.containsKey("maxPrice")) {
            filter.setMaxPrice(tryParseDecimal(params.get("maxPrice")));
        }

        return filter;
    }


    private static Map<String, String> getQueryParams(HttpExchange exchange) {
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> params = new HashMap<>();

        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] parts = pair.split("=", 2);
                if (parts.length == 2) {
                    String key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
                    String value = URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
                    params.put(key, value);
                }
            }
        }

        System.out.println(params);
        return params;
    }

    private static BigDecimal tryParseDecimal(String value) {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Integer tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
