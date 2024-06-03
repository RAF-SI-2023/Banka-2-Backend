package rs.edu.raf.BankService.service;


import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.BankProfitDto;

@Service
public interface BankProfitService {


    BankProfitDto getTotalProfit();

}
