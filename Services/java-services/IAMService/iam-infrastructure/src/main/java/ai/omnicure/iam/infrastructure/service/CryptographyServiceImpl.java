package ai.omnicure.iam.infrastructure.service;

import ai.omnicure.iam.application.service.ICryptographyService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CryptographyServiceImpl implements ICryptographyService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
