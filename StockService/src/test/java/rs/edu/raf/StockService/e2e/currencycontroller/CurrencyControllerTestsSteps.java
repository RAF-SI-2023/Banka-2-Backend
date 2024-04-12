package rs.edu.raf.StockService.e2e.currencycontroller;

public class CurrencyControllerTestsSteps extends CurrencyControllerTestsConfig {
    /*
    mora da se dorade testovi da bi prolazili
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private MvcResult mvcResult;
    @When("user requests all currencies")
    public void userRequestsAllCurrencies() {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/currency/all")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
            ).andExpect(status().isOk());
            mvcResult = resultActions.andReturn();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("the response should contain all currencies")
    public void theResponseShouldContainAllCurrencies() {
        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @When("user requests currency with id {string}")
    public void userRequestsCurrencyWithId(String arg0) {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/currency/id/" + arg0)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
            ).andExpect(status().isOk());
            mvcResult = resultActions.andReturn();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("response contains currency with id {string}")
    public void responseContainsCurrencyWithId(String arg0) {
        try {
            Long id = Long.parseLong(arg0);
            String responseAsString = mvcResult.getResponse().getContentAsString();
            Currency currency = objectMapper.readValue(responseAsString, Currency.class);
            assertEquals(id, currency.getId());
        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            fail(e.getMessage());
        }
    }

    @When("user requests currency with non-existing id {string}")
    public void userRequestsCurrencyWithNonExistingId(String arg0) {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/currency/" + arg0)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
            ).andExpect(status().isNotFound());
            mvcResult = resultActions.andReturn();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @When("user requests currency with existing code {string}")
    public void userRequestsCurrencyWithExistingCode(String arg0) {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/currency/code/" + arg0)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
            ).andExpect(status().isOk());
            mvcResult = resultActions.andReturn();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("response contains only currency with code {string}:")
    public void responseContainsOnlyCurrencyWithCode(String arg0) {
        try {
            String content = mvcResult.getResponse().getContentAsString();
            Currency currency = objectMapper.readValue(content, Currency.class);

            assertEquals(currency.getCurrencyCode(), arg0);
        } catch (UnsupportedEncodingException e) {
            fail(e.getMessage());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @When("user requests currency with non-existing code {string}")
    public void userRequestsCurrencyWithNonExistingCode(String arg0) {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/currency/code/" + arg0)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
            ).andExpect(status().isNotFound());
            mvcResult = resultActions.andReturn();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Then("response has status NotFound by code")
    public void responseHasStatusNotFoundByCode() {
        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    

    @Then("response has inflation currency with currency id {string}")
    public void responseHasInflationCurrencyWithCurrencyId(String arg0) {
        try {
            Long id = Long.parseLong(arg0);
            String responseAsString = mvcResult.getResponse().getContentAsString();
            List<CurrencyInflation> currencyList = objectMapper.readValue(responseAsString, new TypeReference<List<CurrencyInflation>>(){});

            boolean found = false;
            for (CurrencyInflation currency : currencyList) {
                if (currency.getId().equals(id)) {
                    found = true;
                    break;
                }
            }

            assertTrue(found, "Currency with ID " + id + " not found in the response");
        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            fail(e.getMessage());
        }
    }

    @When("user requests inflation currency with existing id {string}")
    public void userRequestsInflationCurrencyWithExistingId(String arg0) {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/currency/inflation/currency-id/" + arg0)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
            ).andExpect(status().isOk());
            mvcResult = resultActions.andReturn();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

     */
}
