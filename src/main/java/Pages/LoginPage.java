package Pages;

import com.microsoft.playwright.Page;

public class LoginPage {
	
    private Page page;
    
    private final String usernameInput = "//input[contains(@id,\"email\")]";
    private final String passwordInput = "//input[contains(@id,\"password\")]";
    private final String loginButton = "//button[contains(@type,\"submit\")]";

    // Constructor
    public LoginPage(Page page) {
        this.page = page;
    }

    

    public void login(String username, String password) {
        page.fill(usernameInput, username);
        page.fill(passwordInput, password);
        page.click(loginButton);
    }

}
