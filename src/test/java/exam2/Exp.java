package exam2;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.WebElement;


public class Exp {
	

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
    
    @Test(priority = 1)
  public void TC_01_OpenBrowser() {

    log.info("Launching Chrome browser");

    ChromeOptions options = new ChromeOptions();
   options.addArguments("--headless=new"); // enable headless
    options.addArguments("--window-size=1920,1080"); // must specify for headless
    options.addArguments("--disable-gpu"); // stable on Mac
    options.addArguments("--no-sandbox"); // sometimes needed on Mac
    options.addArguments("--disable-software-rasterizer"); // avoid GPU crash

    driver = new ChromeDriver(options);

    // Avoid start-maximized with headless
    // driver.manage().window().maximize();

    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    driver.get("https://tldr.lumiolabs.ai/?date=2025-11-01&contentType=Show&movieId=5179&tab=top_10");

    wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    act = new Actions(driver);
    soft = new SoftAssert();
    metrics = new MetricsCollectorsSlider();

    log.info("Navigated to TLDR application");

    wait = new WebDriverWait(driver, Duration.ofSeconds(20));

    String xpath = "//div[contains(@class,'swiper-slide-active')]//div[contains(@class,'truncate')]/following::p[1]";

    WebElement descriptionElement = wait.until(
        ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))
    );

    String disText = descriptionElement.getText().trim();



   
        // ================= Description Validation =================
        System.out.println("📝 Checking description...");
     

        if (disText.isBlank()) {
            System.out.println("❌ Description is MISSING for: ");
           
          
        } else {
            System.out.println("✅ Description found: " + disText.substring(0, Math.min(50, disText.length())) + "...");
          
        }

  driver.close();

}
}