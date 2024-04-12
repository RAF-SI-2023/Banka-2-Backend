//package rs.edu.raf.IAMService.integration.users;
//
//// Objasnjenje anotacija:
////  - @Suite: JUnit anotacija za grupisanje testova, u ovom slucaju Cucumber testova
////  - @IncludeEngines: ukljucuje Cucumber engine
////  - @SelectClasspathResource - koristi features definicije iz resources/features/integration/userservice direktorijuma
////  - @ConfigurationParameter - koristimo ovu anotaciju da naznacimo u kom paketu se nalazi Glue kod (obicno je to ovaj
////    isti paket)
//
//import org.junit.platform.suite.api.ConfigurationParameter;
//import org.junit.platform.suite.api.IncludeEngines;
//import org.junit.platform.suite.api.SelectClasspathResource;
//import org.junit.platform.suite.api.Suite;
//
//import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;
//
//@Suite
//@IncludeEngines("cucumber")
//@SelectClasspathResource("features/integration/userservice")
//@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "rs.edu.raf.IAMService.integration.users")
//public class UserServiceIntegrationTests {
//}
