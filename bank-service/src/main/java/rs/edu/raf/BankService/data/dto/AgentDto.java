package rs.edu.raf.BankService.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AgentDto {
    private Double userLimit;
    private Double leftOfLimit;
    private Boolean orderApprovalRequired;
    private int id;
    private long dateOfBirth;
    private String email;
    private String username;
    private String phone;
    private String address;
    private String role;
    private List<String> permissions;


}