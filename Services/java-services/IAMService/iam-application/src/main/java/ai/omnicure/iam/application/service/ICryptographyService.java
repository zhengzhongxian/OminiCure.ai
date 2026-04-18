package ai.omnicure.iam.application.service;

public interface ICryptographyService {

    String hashPassword(String rawPassword);

    boolean verifyPassword(String rawPassword, String hashedPassword);
}
