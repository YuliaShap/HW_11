package com.greencity.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignInPage {
    private final WebDriver driver;

    @FindBy(css = "img[alt='sing in button']")
    private WebElement signInButton;

    @FindBy(css = ".ng-star-inserted > h1")
    private WebElement welcomeText;

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(css = "button.greenStyle")
    private WebElement signInSubmitButton;


    public SignInPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void openSignInForm() {
        signInButton.click();
    }

    public void enterEmail(String email) {
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    public void enterPassword(String password) {
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    public void clearEmailField() {
        WebElement emailField = driver.findElement(By.id("email"));
        emailField.clear();
    }


    public void clearPasswordField() {
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.clear();
    }

    public void submit() {
        signInSubmitButton.click();
    }

    public String getWelcomeText() {
        return welcomeText.getText();
    }

    public WebElement getLoginButton() {
        return signInSubmitButton;
    }

}