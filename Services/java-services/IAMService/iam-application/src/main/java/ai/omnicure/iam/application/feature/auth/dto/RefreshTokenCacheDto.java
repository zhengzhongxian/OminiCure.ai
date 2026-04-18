package ai.omnicure.iam.application.feature.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenCacheDto {
    private UUID userId;
    private String deviceId;
    private boolean isRevoked;
    private String tokenId;
    private String parentTokenId;

    public Map<String, String> toHashMap() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId != null ? userId.toString() : "");
        map.put("device_id", deviceId != null ? deviceId : "");
        map.put("is_revoked", String.valueOf(isRevoked));
        map.put("token_id", tokenId != null ? tokenId : "");
        map.put("parent_token_id", parentTokenId != null ? parentTokenId : "");
        return map;
    }

    public static RefreshTokenCacheDto fromHashMap(Map<String, String> map) {
        return RefreshTokenCacheDto.builder()
                .userId(map.get("user_id") != null && !map.get("user_id").isEmpty() ? UUID.fromString(map.get("user_id")) : null)
                .deviceId(map.get("device_id"))
                .isRevoked(Boolean.parseBoolean(map.get("is_revoked")))
                .tokenId(map.get("token_id"))
                .parentTokenId(map.get("parent_token_id"))
                .build();
    }
}
