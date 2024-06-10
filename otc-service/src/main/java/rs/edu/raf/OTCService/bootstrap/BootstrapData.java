package rs.edu.raf.OTCService.bootstrap;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.edu.raf.OTCService.data.entity.Contract;
import rs.edu.raf.OTCService.data.enums.ContractStatus;
import rs.edu.raf.OTCService.data.enums.ContractType;
import rs.edu.raf.OTCService.repositories.ContractRepository;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    /*

    Sa kojim email adresava seedovati bazu?
    Trenutno koriste iste email kao i u BankServisu

     */
    @Value("${MY_EMAIL_1:lukapavlovic032@gmail.com}")
    private String myEmail1;

    @Value("${MY_EMAIL_2:lpavlovic11521rn@raf.rs}")
    private String myEmail2;

    @Value("${MY_EMAIL_3:lukapa369@gmail.com}")
    private String myEmail3;


    private static final Logger logger = LoggerFactory.getLogger(BootstrapData.class);
    private final ContractRepository contractRepository;

    @Override
    public void run(String... args) {
        try{
            logger.info("OTCService: DEV DATA LOADING IN PROGRESS...");


            loadContracts();

            logger.info("OTCService: DEV DATA LOADING FINISHED...");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadContracts() {

        if(contractRepository.count() == 0) {

            String[] symbols1 = {"AAPL", "GOOGL", "Z", "NEXOY"};
            String[] symbols2 = {"NTFL", "TSLA", "MSFT", "FB"};
            String[] symbols3 = {"K", "TT", "CC", "I"};

            for(int i = 0; i < 4; i++){
                Contract contract1 = new Contract();
                contract1.setBankConfirmation(false);
                contract1.setSellerConfirmation(false);
                //    contract1.setContractNumber("123"); pitanje je da li treba da se postavi
                //     contract1.setDescription("Test contract 1");
                contract1.setTicker(symbols2[i]);
                contract1.setVolume(4);
                contract1.setTotalPrice(200D);
                contract1.setContractStatus(ContractStatus.WAITING);
                //     contract1.setBuyersPIB(); trenutno ne radi nista ali treba koristi pib iz company employee
                //     contract1.setSellersPIB();
                contract1.setBuyersEmail(myEmail1);
                contract1.setSellersEmail(myEmail2);
                contract1.setContractType(ContractType.PRIVATE_CONTRACT);

                contractRepository.save(contract1);

                Contract contract2 = new Contract();
                contract2.setBankConfirmation(false);
                contract2.setSellerConfirmation(false);
                //    contract1.setContractNumber("123"); pitanje je da li treba da se postavi
                //     contract1.setDescription("Test contract 1");
                contract2.setTicker(symbols1[i]);
                contract2.setVolume(6);
                contract2.setTotalPrice(100D);
                contract2.setContractStatus(ContractStatus.REJECTED);
                //     contract1.setBuyersPIB(); trenutno ne radi nista ali treba koristi pib iz company employee
                //     contract1.setSellersPIB();
                contract2.setBuyersEmail(myEmail2);
                contract2.setSellersEmail(myEmail1);
                contract2.setContractType(ContractType.PRIVATE_CONTRACT);

                contractRepository.save(contract2);

                Contract contract3 = new Contract();
                contract3.setBankConfirmation(false);
                contract3.setSellerConfirmation(false);
                //    contract1.setContractNumber("123"); pitanje je da li treba da se postavi
                //     contract1.setDescription("Test contract 1");
                contract3.setTicker(symbols3[i]);
                contract3.setVolume(5);
                contract3.setTotalPrice(150D);
                contract3.setContractStatus(ContractStatus.WAITING);
                //     contract1.setBuyersPIB(); trenutno ne radi nista ali treba koristi pib iz company employee
                //     contract1.setSellersPIB();
                contract3.setBuyersEmail(myEmail1);
                contract3.setSellersEmail(myEmail3);
                contract3.setContractType(ContractType.PRIVATE_CONTRACT);

                contractRepository.save(contract3);
            }
        }

    }

}
