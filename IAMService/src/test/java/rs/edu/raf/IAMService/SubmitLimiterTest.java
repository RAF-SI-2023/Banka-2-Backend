package rs.edu.raf.IAMService;

import org.junit.jupiter.api.Test;
import rs.edu.raf.IAMService.utils.SubmitLimiter;

import static org.junit.jupiter.api.Assertions.*;

public class SubmitLimiterTest {


    @Test
    void testAllowRequestWithinLimit() {
        SubmitLimiter submitLimiter = new SubmitLimiter();
        String userEmail = "test@example.com";

        for (int i = 0; i < 5; i++) {
            assertTrue(submitLimiter.allowRequest(userEmail));
        }
    }

    @Test
    void testAllowRequestExceedLimit() {
        SubmitLimiter submitLimiter = new SubmitLimiter();
        String userEmail = "test@example.com";

        for (int i = 0; i < 5; i++) {
            assertTrue(submitLimiter.allowRequest(userEmail));
        }

        assertFalse(submitLimiter.allowRequest(userEmail));
    }

    @Test
    void testAllowRequestAfterResetInterval() {
        SubmitLimiter submitLimiter = new SubmitLimiter();
        String userEmail = "test@example.com";

        for (int i = 0; i < 5; i++) {
            assertTrue(submitLimiter.allowRequest(userEmail));
        }

        // Sleep for more than the reset interval
        try {
            Thread.sleep(61 * 1000); // 61 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(submitLimiter.allowRequest(userEmail));
    }

}
