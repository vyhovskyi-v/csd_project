
CREATE TABLE IF NOT EXISTS `Group`(
    `group_id` int PRIMARY KEY AUTO_INCREMENT,
    `group_name` varchar(50) UNIQUE NOT NULL,
    `group_description` varchar(1000) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

    INSERT INTO `Group` (`group_name`, `group_description`) VALUES
('Electronics', 'Devices, gadgets, and electronic accessories.'),
('Home Appliances', 'Various appliances for home use.'),
('Books', 'Different types of books and literature.'),
('Sports', 'Sporting goods and fitness equipment.');




CREATE TABLE IF NOT EXISTS `Product`(
    `product_id` int PRIMARY KEY AUTO_INCREMENT,
    `group_id` int NOT NULL,
    `product_name` varchar(50) UNIQUE NOT NULL,
    `product_description` varchar(1000) NOT NULL,
    `product_manufacturer` int NOT NULL,
    `product_quantity` int NOT NULL,
    `product_price` decimal(13,4) NOT NULL,
    FOREIGN KEY (`group_id`) REFERENCES `Group` (`group_id`) ON UPDATE CASCADE ON DELETE CASCADE
)ENGINE=InnoDB  DEFAULT CHARSET=utf8;

    INSERT INTO `Product` (`group_id`, `product_name`, `product_description`, `product_manufacturer`, `product_quantity`, `product_price`) VALUES
(1, 'Smartphone X100', 'Latest generation smartphone with high-end features.', 101, 50, 799.9900),
(1, 'Bluetooth Headphones', 'Wireless headphones with noise cancellation.', 102, 120, 149.9900),
(2, 'Microwave Oven', '1000W microwave oven with digital controls.', 103, 30, 199.9900),
(2, 'Vacuum Cleaner', 'Bagless vacuum cleaner with HEPA filter.', 104, 25, 159.9900),
(3, 'Novel: The Lost Island', 'Adventure novel by bestselling author John Smith.', 105, 200, 19.9900),
(4, 'Treadmill Pro', 'High-end treadmill with multiple speed modes.', 106, 10, 999.9900);

CREATE TABLE IF NOT EXISTS `User`(
    `username` varchar(20) PRIMARY KEY NOT NULL,
    `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `user` (`username`, `password`) VALUES
("vlad", "$2a$10$hYdCNkmukMfP0MzVixL1Oe7LuXodZ7XpEziQqVnN.IM2zdOiQ6JD.");

