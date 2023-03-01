package cn.lqs.flink.yarn.admin.core;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.time.Duration;

public final class SystemCache {

  private final static Cache<String, Object> cacheMap = CacheBuilder.newBuilder()
    .expireAfterWrite(Duration.ofHours(24))
    .initialCapacity(1 << 4)
    .maximumSize(1 << 10)
    .build();


}
