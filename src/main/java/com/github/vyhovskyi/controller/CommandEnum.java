package com.github.vyhovskyi.controller;

import com.github.vyhovskyi.controller.command.Command;
import com.github.vyhovskyi.controller.command.HomeCommand;
import com.github.vyhovskyi.controller.command.PageNotFoundCommand;
import com.github.vyhovskyi.controller.command.group.api.*;
import com.github.vyhovskyi.controller.command.group.GetAddUpdateGroupCommand;
import com.github.vyhovskyi.controller.command.group.GetAllGroupsPageCommand;
import com.github.vyhovskyi.controller.command.product.*;
import com.github.vyhovskyi.controller.command.login.GetLoginCommand;
import com.github.vyhovskyi.controller.command.product.api.*;
import com.github.vyhovskyi.service.ServiceFactory;

import java.util.regex.Pattern;

public enum CommandEnum {

    HOME{
        {
            this.key = Pattern.compile("GET:");
            this.command = new HomeCommand();
        }
    },

    GET_LOGIN{
        {
            this.key = Pattern.compile("GET:login");
            this.command = new GetLoginCommand();
        }
    },

    PAGE_NOT_FOUND{
        {
            this.key = Pattern.compile("GET:pageNotFound");
            this.command = new PageNotFoundCommand();
        }
    },

    GET_ALL_PRODUCTS{
        {
            this.key = Pattern.compile("GET:products");
            this.command = new GetAllProductsViewCommand();
        }
    },

    GET_ADD_UPDATE_PAGE{
        {
            this.key = Pattern.compile("GET:products/(edit|add)");
            this.command = new GetAddUpdatePageCommand();
        }
    },

    API_PRODUCTS_GET_ALL{
        {
            this.key = Pattern.compile("GET:api/products");
            this.command = new ApiGetAllProductsCommand(ServiceFactory.getProductService());
        }
    },



    API_PRODUCTS_GET_BY_ID{
        {
            this.key = Pattern.compile("GET:api/products/\\d+");
            this.command = new ApiGetProductCommand(ServiceFactory.getProductService());
        }
    },

    API_PRODUCTS_ADD_PRODUCT{
        {
            this.key = Pattern.compile("POST:api/products");
            this.command = new ApiCreateProductCommand(ServiceFactory.getProductService(), ServiceFactory.getGroupDao());
        }
    },

    API_PRODUCTS_UPDATE_PRODUCT{
        {
            this.key = Pattern.compile("PUT:api/products/\\d+");
            this.command = new ApiUpdateProductCommand(ServiceFactory.getProductService(), ServiceFactory.getGroupDao());
        }
    },

    API_PRODUCTS_DELETE_PRODUCT{
        {
            this.key = Pattern.compile("DELETE:api/products/\\d+");
            this.command = new ApiDeleteProductCommand(ServiceFactory.getProductService());
        }
    },

    API_PRODUCTS_DECREASE_QUANTITY{
        {
            this.key = Pattern.compile("POST:api/products/\\d+/decrease");
            this.command = new ApiDecreaseProductQuantityCommand(ServiceFactory.getProductService());
        }
    },

    API_PRODUCTS_INCREASE_QUANTITY{
        {
            this.key = Pattern.compile("POST:api/products/\\d+/increase");
            this.command = new ApiIncreaseQuantityProduct(ServiceFactory.getProductService());
        }
    },

    API_GROUPS_GET_ALL{
        {
            this.key = Pattern.compile("GET:api/groups");
            this.command = new ApiGetAllGroupsCommand(ServiceFactory.getGroupDao());
        }
    },

    GET_ALL_GROUPS{
        {
            this.key = Pattern.compile("GET:groups");
            this.command = new GetAllGroupsPageCommand();
        }
    },

    GET_ADD_UPDATE_GROUP{
        {
            this.key = Pattern.compile("GET:groups/(edit|add)");
            this.command = new GetAddUpdateGroupCommand();
        }
    },

    API_GROUPS_GET_BY_ID{
        {
            this.key = Pattern.compile("GET:api/groups/\\d+");
            this.command = new ApiGetGroupCommand(ServiceFactory.getGroupDao());
        }
    },

    API_GROUPS_ADD_GROUP{
        {
            this.key = Pattern.compile("POST:api/groups");
            this.command = new ApiCreateGroupCommand(ServiceFactory.getGroupDao());
        }
    },

    API_GROUPS_UPDATE_GROUP{
        {
            this.key = Pattern.compile("PUT:api/groups/\\d+");
            this.command = new ApiUpdateGroupCommand(ServiceFactory.getGroupDao());
        }
    },

    API_GROUPS_DELETE_GROUP{
        {
            this.key = Pattern.compile("DELETE:api/groups/\\d+");
            this.command = new ApiDeleteGroupCommand(ServiceFactory.getGroupDao());
        }
    };


    Pattern key;
    Command command;

    public Command getCommand() {return command;}

    public Pattern getKey() {return key;}

    public static Command getCommand(String key){
        for (CommandEnum command : CommandEnum.values()) {
            if (command.getKey().matcher(key).matches()){
                return command.getCommand();
            }
        }
        return PAGE_NOT_FOUND.getCommand();
    }


}
