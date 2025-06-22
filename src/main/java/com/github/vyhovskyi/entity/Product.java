package com.github.vyhovskyi.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private String name;
    private Group group;
    private String description;
    private int manufacturerId;
    private int quantity;
    private BigDecimal price;

    public Product() {}

    public Product(String name, Group group, String description, int manufacturerId, int quantity, BigDecimal price) {
        this.name = name;
        this.group = group;
        this.description = description;
        this.manufacturerId = manufacturerId;
        this.quantity = quantity;
        this.price = price;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Group getGroup() {return group;}

    public void setGroup(Group group) {this.group = group;}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(int manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "Product [name=" + name + ", group=" + group + ", description=" + description
                + ", manufacturerId=" + manufacturerId + ", quantity=" + quantity
                + ", price=" + price + "]";
    }
}
