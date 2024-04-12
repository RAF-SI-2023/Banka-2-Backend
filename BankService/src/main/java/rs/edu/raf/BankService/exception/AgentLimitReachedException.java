package rs.edu.raf.BankService.exception;

public class AgentLimitReachedException extends RuntimeException {

    public AgentLimitReachedException() {
        super("Agent limit reached. Operation not possible.");
    }
}
