package ru.netology.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardOrderFormTest {

    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void cardOrderFormFilledCorrectly() {

        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Евгений Стариков");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79201234567");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());

    }

    @Test
    public void NameWithHyphen() {

        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Евгений-Стариков");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79201234567");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());

    }

    @Test
    public void NoClickAgreement() {

        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Евгений Стариков");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79201234567");
        driver.findElement(By.cssSelector(".button")).click();
        WebElement agree = driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid"));

        assertTrue(agree.isDisplayed(), "Сообщение об ошибке");

    }

    @Test
    public void PhoneNotFilled() {

        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Евгений Стариков");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());

    }

    @Test
    public void SpaceBarInsteadPhone() {

        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Евгений Стариков");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("   ");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());

    }

    @Test
    public void doubleName() {

        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Евгений Анатольевич Стариков");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79201234567");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());

    }

    @Test
    public void InNameEnglishWord() {

        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Evgeny Starikov");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79201234567");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());

    }

    @Test
    public void NameWithNumbers() {

        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Евгений 789456123");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79201234567");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());

    }

    @Test
    public void emptyForm() {

        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("");
//        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());

    }

    @Test
    public void SpecialSymbolsInPhone() {

        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Евгений Стариков");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+795374&4015");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());

    }

    @Test
    public void incompleteNumber() {

        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Евгений Стариков");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+9537424015");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());

    }

    @Test
    public void IncompleteNumberWithoutPlusSign() {

        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Евгений Стариков");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("79537424015");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());

    }

    @Test
    public void phoneNumberWithTwoPlusSigns() {

        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Евгений Стариков");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("++79537424015");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());

    }

    @Test
    public void numberWithPlusSignInMiddle() {

        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Евгений Стариков");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("7+9537424015");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());

    }

}