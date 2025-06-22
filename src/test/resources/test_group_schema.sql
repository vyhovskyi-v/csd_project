
DROP TABLE IF EXISTS `group`;

DROP TABLE IF EXISTS `product`;

CREATE TABLE `group` (
    group_name VARCHAR(50) PRIMARY KEY,
    group_description VARCHAR(1000) NOT NULL
);

CREATE TABLE `product`(
    product_name varchar(50) NOT NULL,
    group_name varchar(50) NOT NULL,
    product_description varchar(1000) NOT NULL,
    product_manufacturer int NOT NULL,
    product_quantity int NOT NULL,
    product_price decimal(13,4) NOT NULL,
    PRIMARY KEY (`product_name`),
    FOREIGN KEY (`group_name`) REFERENCES `Group` (`group_name`) ON UPDATE CASCADE ON DELETE CASCADE
    )

