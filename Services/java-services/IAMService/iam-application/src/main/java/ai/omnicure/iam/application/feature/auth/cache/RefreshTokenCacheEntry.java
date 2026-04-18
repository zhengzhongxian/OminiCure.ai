package ai.omnicure.iam.application.feature.auth.cache;

import ai.omnicure.core.domain.cache.CacheEntry;
import ai.omnicure.iam.application.feature.auth.dto.RefreshTokenCacheDto;

public class RefreshTokenCacheEntry extends CacheEntry<RefreshTokenCacheDto> {

    public RefreshTokenCacheEntry(String prefix) {
        super(prefix);
    }
}
