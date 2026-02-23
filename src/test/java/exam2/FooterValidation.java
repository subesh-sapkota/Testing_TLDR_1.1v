
package exam2;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

@Listeners(TestListener.class)
public class FooterValidation {

    /* =========================
       LOGGER
       ========================= */
    Logger log = LogManager.getLogger(LumioTest.class);

    /* =========================
       DRIVER OBJECTS
       ========================= */
    public WebDriver driver;
    WebDriverWait wait;
    Actions act;
    SoftAssert soft;
    MetricsCollectorsSlider metrics;
    
    public static String currentMonth;

    // TC_01 – Launch Browser
    
    
    @Test(priority = 1)
  public void TC_01_OpenBrowser() {

    log.info("Launching Chrome browser");

    ChromeOptions options = new ChromeOptions();
     options.addArguments("--headless=new"); // enable headless
    options.addArguments("--window-size=1920,1080"); // must specify for headless
    options.addArguments("--disable-gpu"); // stable on Mac
    options.addArguments("--no-sandbox"); // sometimes needed on Mac
    options.addArguments("--disable-software-rasterizer"); // avoid GPU crash
 // IMPORTANT: Remove automation flags
    options.addArguments("--disable-blink-features=AutomationControlled");

    options.setExperimentalOption("excludeSwitches",
            Arrays.asList("enable-automation"));
    options.setExperimentalOption("useAutomationExtension", false);

    // Real user-agent (match your Chrome version!)
    options.addArguments("--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) " +
            "Chrome/120.0.0.0 Safari/537.36");
    

    driver = new ChromeDriver(options);

    // Avoid start-maximized with headless
    // driver.manage().window().maximize();

    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    driver.get("https://tldr.lumiolabs.ai/");

    wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    act = new Actions(driver);
    soft = new SoftAssert();
    metrics = new MetricsCollectorsSlider();

    log.info("Navigated to TLDR application");
    Assert.assertTrue(driver.getCurrentUrl().contains("tldr.lumiolabs.ai"));
}

    //  TC_02 – Verify Page Title
    
    @Test(priority = 2,enabled=true)
    public void TC_02_VerifyTitle() {

        log.info("Verifying page title");

        String expected =
                "TLDR - Trending, New & Upcoming Movies & Shows on OTT";

        Assert.assertEquals(driver.getTitle(), expected);
    }
    
    
    
    @Test(priority =3)
    public void TC_03_Check_Footer() {
    	
    	WebElement footerText = driver.findElement(By.xpath("//p[normalize-space()='MAKE TLDR BETTER']"));
    	
    	WebElement feedbackForm = driver.findElement(By.xpath("//span[normalize-space()='FEEDBACK FORM']"));
    	
    	WebElement privacyPolicy = driver.findElement(By.xpath("//a[@id='privacy-policy-link']//span[normalize-space()='PRIVACY POLICY']"));
    	
    	System.out.println(footerText.getText());
  
    	
    	
   //  Assert.assertEquals(footerText.getText().trim(),"MAKE TLDR BETTER");
  
    	
    	
    }
    
}
    
    