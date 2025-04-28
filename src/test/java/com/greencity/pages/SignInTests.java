package com.greencity.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;

public class SignInTests {

    private static WebDriver driver;
    private SignInPage signInPage;

    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("http://localhost:4205/#/greenCity");
    }

    @BeforeEach
    public void initPage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("http://localhost:4205/#/greenCity");

        WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("img.ubs-header-sing-in-img.ubs-header-sing-in-img-greencity")));
        signInButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".sign-in-form")));

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("email")));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("password")));

            signInPage = new SignInPage(driver);
            signInPage.clearEmailField();
            signInPage.clearPasswordField();
        } catch (StaleElementReferenceException e) {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("email"))).clear();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("password"))).clear();
        }
    }


//     Negative tests

    @ParameterizedTest
    @CsvSource({
            "'invalidemail.com', '123', 'Please check that your e-mail address is indicated correctly', 'Перевірте, чи правильно вказано вашу адресу електронної пошти'"
    })
    public void signInWithInvalidEmail(String email, String password, String expectedErrorEn, String expectedErrorUa) {
        signInPage.enterEmail(email);
        signInPage.enterPassword(password);

        String errorMessage = signInPage.getErrorEmailText();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement errorEmail = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#email-err-msg > app-error")
        ));

        String errorEmailText = errorEmail.getText().trim();

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        assertThat("Submit button should be disabled for invalid email", submitButton.isEnabled(), is(false));
    }


    @ParameterizedTest
    @CsvSource({
            "validemail@example.com, wrongPassword, Bad email or password, Введено невірний email або пароль",
            "nonexistentuser@example.com, SomePassword123, Bad email or password, Введено невірний email або пароль",
            "domik339@gmail.com, WrongPassword999!, Bad email or password, Введено невірний email або пароль"
    })
    public void signInWithInvalidPassword(String email, String password, String expectedErrorEn, String expectedErrorUa) {
        signInPage.enterEmail(email);
        signInPage.enterPassword(password);
        signInPage.submit();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement errorPassword = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-general-error.ng-star-inserted")));

        String errorPasswordText = errorPassword.getText().trim();
        String expectedError = errorPasswordText.contains("Bad") ? expectedErrorEn : expectedErrorUa;

        assertThat(errorPasswordText, is(expectedError));
    }

    @ParameterizedTest
    @CsvSource({
            "'', validPassword123, Email is required, Введіть пошту"
    })
    public void signInWithEmptyEmail(String email, String password, String expectedErrorEn, String expectedErrorUa) {
        signInPage.enterEmail(email);
        signInPage.enterPassword(password);
        signInPage.submit();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement errorEmail = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("#email-err-msg > app-error")));

        String errorEmailText = errorEmail.getText().trim();
        String expectedError = errorEmailText.contains("Bad") ? expectedErrorEn : expectedErrorUa;

        assertThat(errorEmailText, is(expectedError));

    }


    @ParameterizedTest
    @CsvSource({
            "validemail@example.com, '', Password is required, Введіть пароль"
    })
    public void signInWithEmptyPassword(String email, String password, String expectedErrorEn, String expectedErrorUa) {
        signInPage.enterEmail(email);
        signInPage.enterPassword(password);
        signInPage.submit();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement errorPassword = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#pass-err-msg > app-error")));

        String errorPasswordText = errorPassword.getText().trim();
        String expectedError = errorPasswordText.contains("Bad") ? expectedErrorEn : expectedErrorUa;

        assertThat(errorPasswordText, is(expectedError));
    }


    //    Positive test
    @ParameterizedTest
    @CsvSource({
            "domik339@gmail.com, Inbloom339546!"
    })
    public void verifySuccessfulSignIn(String email, String password) {
        signInPage.enterEmail(email);
        signInPage.enterPassword(password);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(signInPage.getLoginButton()));
        signInPage.submit();

        String welcomeMessage = signInPage.getWelcomeText();
        List<String> validMessages = Arrays.asList("Hi Eco Friend ;-)", "З поверненням!");

        assertThat(validMessages, hasItem(welcomeMessage));
    }


    @AfterAll
    public static void tearDown() {
        driver.quit();
    }
}
