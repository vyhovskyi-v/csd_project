
DROP TABLE IF EXISTS `Product`;
DROP TABLE IF EXISTS `Group`;

CREATE TABLE `Group`(
    `group_name` varchar(50) NOT NULL,
    `group_description` varchar(1000) NOT NULL,
    PRIMARY KEY (`group_name`)
);

CREATE TABLE `Product`(
    `product_name` varchar(50) NOT NULL,
    `group_name` varchar(50) NOT NULL,
    `product_description` varchar(1000) NOT NULL,
    `product_manufacturer` int NOT NULL,
    `product_quantity` int NOT NULL,
    `product_price` decimal(13,4) NOT NULL,
    PRIMARY KEY (`product_name`),
    FOREIGN KEY (`group_name`) REFERENCES `Group` (`group_name`) ON UPDATE CASCADE ON DELETE CASCADE
);
