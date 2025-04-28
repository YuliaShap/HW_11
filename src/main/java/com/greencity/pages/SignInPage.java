package com.greencity.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignInPage {
    private final WebDriver driver;

    @FindBy(xpath = "/html/body/app-root/app-main/div/app-header/header/div[2]/div/div/div/ul/img")
    private WebElement signInButton;

    @FindBy(css = "img.ubs-header-sing-in-img.ubs-header-sing-in-img-greencity")
    private WebElement signInButtonVisible;

    @FindBy(css = ".sign-in-form")
    private WebElement signInForm;

    @FindBy(css = ".ng-star-inserted > h1")
    private WebElement welcomeText;

    @FindBy(css = "app-sign-in > h2")
    WebElement signInDetailsText;

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(css = "button.greenStyle")
    private WebElement signInSubmitButton;

    @FindBy(css = ".alert-general-error")
    private WebElement errorMessage;

    @FindBy(css = ".alert-general-error.ng-star-inserted")
    public WebElement errorPassword;

    @FindBy(xpath = "//*[contains(@class, 'alert-email-validation')]")
    private WebElement errorEmail;

    @FindBy(css = "#pass-err-msg > app-error")
    public WebElement errorEmptyPassword;

    @FindBy(css = "#email-err-msg > app-error")
    private WebElement errorEmptyEmail;

    @FindBy(xpath = "//button[@id='logoutButton']")
    private WebElement logoutButton;

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

    public String getErrorEmailText() {
        return errorEmail.getText();
    }


    public String getWelcomeText() {
        return welcomeText.getText();
    }


    public WebElement getLoginButton() {
        return signInSubmitButton;
    }

    public WebElement getErrorEmptyEmail() {
        return errorEmptyEmail;
    }

    public WebElement getErrorEmptyPassword() {
        return errorEmptyPassword;
    }

}