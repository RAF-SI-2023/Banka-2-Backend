package rs.edu.raf.BankService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.data.dto.MarginCallViolationDto;
import rs.edu.raf.BankService.data.entities.MarginsAccount;
import rs.edu.raf.BankService.data.entities.MarginsTransaction;
import rs.edu.raf.BankService.repository.MarginsAccountRepository;

import java.util.List;

@RequiredArgsConstructor
@Component
public class MarginsAccountMaintainScheduler {

    private final MarginsAccountRepository marginsAccountRepository;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "0 0 0,12 * * *")
    public void updateMarginsCalls() {
        List<MarginsAccount> marginsAccounts = marginsAccountRepository.findAllWithTransactions();
        marginsAccounts.forEach(this::processMarginsAccount);
    }

    @Transactional
    public void processMarginsAccount(MarginsAccount marginsAccount) {
        Double transactionsValue = getMarginsTransactionsValue(marginsAccount.getMarginsTransactions());
        Double maintenanceMargin = marginsAccount.getMaintenanceMargin();

        if (transactionsValue <= maintenanceMargin) {
            marginsAccount.setMarginCall(true);
            sendMarginCallViolationMessage(marginsAccount.getEmail());
        }
        marginsAccount.setBalance(transactionsValue);

        marginsAccountRepository.save(marginsAccount);
    }

    // ovde treba da se proveri trenutna(aktuelna) cena akcije za svaku transakciju i da se sabere
    // ja koristim orderPrice, koji predstavlja koliko je akcija zapravo placena
    // izmeniti kad neko provali kako se dovlaci trenutna cena za akciju
    private Double getMarginsTransactionsValue(List<MarginsTransaction> transactions) {
        return transactions.stream()
                .mapToDouble(MarginsTransaction::getOrderPrice)
                .sum();
    }

    private void sendMarginCallViolationMessage(String email) {
        String message = "You have violated the margin call!";
        rabbitTemplate.convertAndSend(
                "margin-call-violation",
                new MarginCallViolationDto(email, message));
    }
}