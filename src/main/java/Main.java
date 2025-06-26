import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vyhovskyi.entity.Product;
import com.github.vyhovskyi.entity.ProductFilter;
import com.github.vyhovskyi.service.ProductService;
import com.github.vyhovskyi.service.ServiceFactory;

import java.util.List;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        ProductService productService = ServiceFactory.getProductService();

        ProductFilter productFilter = new ProductFilter();
        productFilter.setName("x");

        List<Product> products = productService.getProductsByFilter(productFilter, 0,0);

        System.out.println(products);
    }
}
