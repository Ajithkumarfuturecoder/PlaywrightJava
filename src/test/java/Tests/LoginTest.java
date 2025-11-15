package Tests;

import org.testng.SkipException;
import org.testng.annotations.Test;

import Base.BasePage;
import Pages.LoginPage;

public class LoginTest extends BasePage {

    @Test
    public void loginSuccessTest() {
        // Create page objects
    	
    	
        LoginPage loginPage = new LoginPage(page);

        
        loginPage.login("ajithkumar1598@gmail.com", "Digival@123");
        test.info("All test completed");
    }
    @Test
    public void loginSuccessTest1() {
    	
      test.skip("Skip the test");
     throw new SkipException("Skipped the Test");
    }
//    
//    @Test
//    public void loginSuccessTest2() {
////        // Create page objects
//   	
//    	
//        LoginPage loginPage = new LoginPage(page);
//
//        
//        loginPage.login("ajithkumar1598.", "Digival@123");
//        test.fail("Test is failed");
//    }
}
