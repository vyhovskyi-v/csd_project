import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vyhovskyi.entity.Product;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        String productJson = """
                {
                  "name": "Buckwheat",
                  "group": {
                    "id": 2
                  },
                  "description": "High quality buckwheat",
                  "manufacturerId": 1,
                  "quantity": 100,
                  "price": 29.99
                }
                
                
                """;

        ObjectMapper mapper = new ObjectMapper();
        Product product = mapper.readValue(productJson, Product.class);
        System.out.println(product);
    }
}
