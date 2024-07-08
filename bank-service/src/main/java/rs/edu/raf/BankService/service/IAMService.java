package rs.edu.raf.BankService.service;

import org.springframework.stereotype.Service;

@Service
public interface IAMService {
    Double getAgentLimitLeft(Long agentId);

    boolean reduceAgentLimit(Long agentId, Double amount);

    Boolean isApprovalNeeded(Long agentId);

    Boolean getUserById(Long id);

}
