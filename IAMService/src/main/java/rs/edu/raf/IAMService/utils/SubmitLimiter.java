package rs.edu.raf.IAMService.utils;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Getter
public class SubmitLimiter {
    private static final int MAX_REQUESTS = 5;
    private static final long RESET_INTERVAL_MINUTES = 1; // Reset interval in minutes
    private final ConcurrentHashMap<String, RateLimitInfo> userRequestInfo = new ConcurrentHashMap<>();

    public boolean allowRequest(String userEmail) {
        RateLimitInfo info = userRequestInfo.computeIfAbsent(userEmail, k -> new RateLimitInfo());
        Long currentTime = System.currentTimeMillis();
        if (currentTime > (info.getLastReset() + TimeUnit.MINUTES.toMillis(RESET_INTERVAL_MINUTES))) {
            info.resetRequestCount();
        }
        int count = info.incrementAndGetRequestCount();
        if (count > MAX_REQUESTS)
            return false;

        return true;
    }

    @Getter
    private static class RateLimitInfo {
        private final AtomicInteger requestCount = new AtomicInteger(0);
        private long lastReset = System.currentTimeMillis();

        public int incrementAndGetRequestCount() {
            return requestCount.incrementAndGet();
        }

        public void resetRequestCount() {
            requestCount.set(0);
            lastReset = System.currentTimeMillis();
        }


    }

}