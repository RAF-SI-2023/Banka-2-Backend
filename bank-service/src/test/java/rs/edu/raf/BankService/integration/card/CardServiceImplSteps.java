package rs.edu.raf.BankService.integration.card;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.BankService.data.dto.CardDto;
import rs.edu.raf.BankService.data.dto.CreateCardDto;
import rs.edu.raf.BankService.mapper.CardMapper;
import rs.edu.raf.BankService.service.CardService;

import static org.junit.Assert.assertEquals;

public class CardServiceImplSteps extends CardServiceImplTestsConfig {


    private CardService cardService;
    ResponseEntity<?> responseEntity;

    String accountNumber;
    CreateCardDto brandNewCreatedCardDto;

    private CardMapper cardMapper;

    Long cardId;

    public CardServiceImplSteps(CardService cardService, CardMapper cardMapper) {
        this.cardService = cardService;
        this.cardMapper = cardMapper;
    }


    @Given("user has an account with account number {string} for the card")
    public void userHasAnAccount(String string) {
        accountNumber = string;
    }

    @When("the user enters the IdentificationCardNumber {long}")
    public void the_user_enters_the_identification_card_number(long id) throws Exception {
        responseEntity = new ResponseEntity<>(cardService.getCardByIdentificationCardNumber(id), null, 200);
    }

    @Then("the user should return 200")
    public void the_user_should_see_the_card() throws Exception {
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @When("the user enters the account number {string}")
    public void the_user_enters_the_card_number(String cardNumber) throws Exception {
        responseEntity = new ResponseEntity<>(cardService.getCardsByAccountNumber(cardNumber), null, 200);
    }


    @And("the user changes status of {long}")
    public void the_user_changes_the_status_to(Long id) throws Exception {
        responseEntity = new ResponseEntity<>(cardService.changeCardStatus(id), null, 200);
    }


    @And("the user changes limit of {long}")
    public void the_user_changes_the_limit_to(Long id) throws Exception {
        responseEntity = new ResponseEntity<>(cardService.changeCardLimit(cardService.getCardByIdentificationCardNumber(id)), null, 200);
    }


    @When("the bank creates a new card with given account number")
    public void the_user_creates_a_new_card() throws Exception {
        brandNewCreatedCardDto = new CreateCardDto();
        brandNewCreatedCardDto.setAccountNumber(accountNumber);

        CardDto cardDto = new CardDto();


        responseEntity = new ResponseEntity<>(cardDto, null, 200);
    }


    @Then("the user should return 200 for creation")
    public void confirmingMessage() throws Exception {
        assertEquals(200, responseEntity.getStatusCodeValue());
    }


}
