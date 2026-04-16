package ai.omnicure.iam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ai.omnicure")
public class IAMServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(IAMServiceApplication.class, args);
    }
}
