package com.github.vyhovskyi.controller.command.product.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vyhovskyi.controller.command.Command;
import com.github.vyhovskyi.controller.utils.HttpSender;
import com.github.vyhovskyi.entity.Group;
import com.github.vyhovskyi.entity.Product;
import com.github.vyhovskyi.exception.ServiceException;
import com.github.vyhovskyi.service.GroupService;
import com.github.vyhovskyi.service.ProductService;
import com.github.vyhovskyi.validator.ProductValidator;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ApiCreateProductCommand implements Command {
    ProductService productService;
    GroupService groupService;

    public ApiCreateProductCommand(ProductService productService, GroupService groupService) {
        this.productService = productService;
        this.groupService = groupService;
    }

    @Override
    public void execute(HttpExchange exchange) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Product product = mapper.readValue(exchange.getRequestBody(), Product.class);

        List<String> errors = ProductValidator.validateForAdd(product);
        errors = validateProduct(product, errors);
        if (!errors.isEmpty()){
            HttpSender.sendErrors(exchange, 409, errors);
            return;
        }

        try{
            Integer id = productService.createProduct(product);
            Map<String, Object> response = new HashMap<>();
            response.put("id", id);

            HttpSender.sendJson(exchange, 201, response);
        }catch (ServiceException e){
            HttpSender.sendJson(exchange, 500, "Server Error");
        }

    }

    private List<String> validateProduct(Product product, List<String> errors){
        Optional<Product> productOptional = productService.getProductByName(product.getName());
        if (productOptional.isPresent()){
            errors.add("Product with that name already exists");
        }

        Optional<Group> groupOptional = groupService.getGroupById(product.getGroup().getId());
        if (!groupOptional.isPresent()){
            errors.add("Group with that id does not exist");
        }


        return errors;
    }
}
