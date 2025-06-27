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
import java.util.List;
import java.util.Optional;

public class ApiUpdateProductCommand implements Command {
    ProductService productService;
    GroupService groupService;

    public ApiUpdateProductCommand(ProductService productService, GroupService groupService) {
        this.productService = productService;
        this.groupService = groupService;
    }

    @Override
    public void execute(HttpExchange exchange) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Product product = mapper.readValue(exchange.getRequestBody(), Product.class);

        String[] segments = exchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(segments[segments.length - 1]);

        product.setId(id);

        List<String> errors = ProductValidator.validateForUpdate(product);
        errors = validateProduct(product, errors);
        if (!errors.isEmpty()){
            HttpSender.sendErrors(exchange, 409, errors);
            return;
        }

        try{
            productService.updateProduct(product);
            exchange.sendResponseHeaders(204, 0);
            exchange.close();
        }catch (Exception e){
            exchange.sendResponseHeaders(500, 0);
            exchange.close();
        }

    }

    private List<String> validateProduct(Product product, List<String> errors){
        String oldName = productService.getProductById(product.getId()).get().getName();

        if (!oldName.equals(product.getName())){
            Optional<Product> productOptional = productService.getProductByName(product.getName());
            if (productOptional.isPresent()){
                errors.add("Product with that name already exists");
            }
        }

        Optional<Product> productOptional = productService.getProductById(product.getId());
        if (!productOptional.isPresent()){
            errors.add("Product with that id does not exist");
        }

        Optional<Group> groupOptional = groupService.getGroupById(product.getGroup().getId());
        if (!groupOptional.isPresent()){
            errors.add("Group with that id does not exist");
        }


        return errors;
    }
}
