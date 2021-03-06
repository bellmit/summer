package com.summer.limit.strategy;

import com.google.common.util.concurrent.RateLimiter;
import com.summer.limit.interfaces.RateLimiterInterface;
import com.summer.limit.strategy.factory.RateLimiterStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component(RateLimiterStrategyFactory.RATE_LIMITER_PRE + SingleRateLimiter.RATE_LIMITER_SUF)
@Slf4j
public class SingleRateLimiter implements RateLimiterInterface {

    public static final String RATE_LIMITER_SUF = "Single";


    private ConcurrentHashMap<String, RateLimiter> rateLimiters = new ConcurrentHashMap();

    @Override
    public boolean limit(String key, long limit, long expire) {
        // 每秒5000个许可
        RateLimiter rateLimiter = rateLimiters.computeIfAbsent(key, k -> RateLimiter.create(limit / (double) expire));

        if (!rateLimiter.tryAcquire()) {
            log.info("由于超过单位时间={}-允许的请求次数={}[触发限流]", expire, limit);
            return false;
        }

        return true;
    }
}
