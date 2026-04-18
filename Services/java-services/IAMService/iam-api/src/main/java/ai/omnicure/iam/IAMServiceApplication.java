package ai.omnicure.iam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
    scanBasePackages = "ai.omnicure",
    exclude = {
        org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration.class,
        org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration.class
    }
)
public class IAMServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(IAMServiceApplication.class, args);
    }
}
