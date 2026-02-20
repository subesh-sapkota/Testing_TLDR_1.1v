package exam2;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.Certificate;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class UpdatedTc {

	
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

 // TC_01 – Launch Browser
 
 
 @Test(priority = 1)
public void TC_01_OpenBrowser() {

 log.info("Launching Chrome browser");

 ChromeOptions options = new ChromeOptions();
//options.addArguments("--headless=new"); // enable headless
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
 
 @Test(priority = 2,enabled=false)
 public void verifyWebsite() throws Exception {

     // ===============================
     // 1️⃣ Verify Status Code = 200
     // ===============================
	 String testUrl="https://tldr.lumiolabs.ai/";
	 String expectedTitle="TLDR - Trending, New & Upcoming Movies & Shows on OTT";
	 
	 long maxLoadTime = 60000; // 5 seconds
	 
     URL url = new URL(testUrl);
     HttpURLConnection connection = (HttpURLConnection) url.openConnection();
     connection.setRequestMethod("GET");
     connection.connect();

     int statusCode = connection.getResponseCode();
     System.out.println("Status Code: " + statusCode);
     Assert.assertEquals(statusCode, 200, "Invalid Status Code!");

     // ===============================
     // 2️⃣ Verify SSL + HTTPS
     // ===============================
     Assert.assertTrue(testUrl.startsWith("https"),
             "HTTPS is NOT enforced!");

     HttpsURLConnection httpsConnection =
             (HttpsURLConnection) new URL(testUrl).openConnection();
     httpsConnection.connect();

     Certificate[] certificates =
             httpsConnection.getServerCertificates();

     Assert.assertTrue(certificates.length > 0,
             "SSL Certificate is not valid!");

     // ===============================
     // 3️⃣ Verify Load Time
     // ===============================
     long startTime = System.currentTimeMillis();
     driver.get(testUrl);
     long endTime = System.currentTimeMillis();

     long loadTime = endTime - startTime;
     System.out.println("Page Load Time: " + loadTime + " ms");

     Assert.assertTrue(loadTime <= maxLoadTime,
             "Page Load Time Exceeded!");

     // ===============================
     // 4️⃣ Verify Title
     // ===============================
     String actualTitle = driver.getTitle();
     Assert.assertEquals(actualTitle, expectedTitle,
             "Homepage Title Mismatch!");

     // ===============================
     // 5️⃣ Verify Stability After Refresh
     // ===============================
     String titleBeforeRefresh = driver.getTitle();

     driver.navigate().refresh();

     String titleAfterRefresh = driver.getTitle();

     Assert.assertEquals(titleAfterRefresh, titleBeforeRefresh,
             "Page unstable after refresh!");

     Assert.assertTrue(driver.findElement(By.tagName("body")).isDisplayed(),
             "Page not loaded properly after refresh!");
 }
 
 
 // Verify carousel-container section. 
 
@Test(priority = 3,enabled=false)
 
 public void verifyCarousel()  {
	 
	 List<WebElement> visibleSlides = driver.findElements(
			    By.xpath("//main[@id='hero-carousel-container']//div[contains(@class,'swiper-slide-visible')]")
			);

			System.out.println("Visible Slides Count: " + visibleSlides.size());
		int ExpectedSliderCount=7;	
		int ActualSilderCount =visibleSlides.size();
			
			 Assert.assertEquals(ExpectedSliderCount, ActualSilderCount, "Carousel count missmached");	
			 
 }
 
@Test(priority = 4,enabled=true)
public void TC_03_OpenCalendarAndGoToNovember() {

    log.info("Opening calendar");
    
    WebElement popUpCloseButton = wait.until(
            ExpectedConditions.presenceOfElementLocated(
                    By.xpath("(//div[contains(@class,'text-white')])/button")
            )
    );

    ((JavascriptExecutor) driver)
            .executeScript("arguments[0].click();", popUpCloseButton);           
    
    WebElement calendarButton = driver.findElement(By.xpath("//div[contains(@class,'border-l border-[#444444] flex items-center justify-center')]"));
	
	JavascriptExecutor js = (JavascriptExecutor)driver;
	js.executeScript("arguments[0].scrollIntoView(true);", calendarButton);
	
	act = new Actions(driver);
	act.moveToElement(calendarButton).perform();

    WebElement prevBtn =driver.findElement(By.xpath("//button[contains(@aria-label,'Go to the Previous Month')]"));
          

    while (true) {
        String month = driver.findElement(By.xpath("//span[contains(@role,'status')]")).getText();
        if (month.equals("November 2025")) {
            log.info("Reached November 2025");
            break;
        }
        prevBtn.click();
    }

    driver.findElement(By.xpath("//button[normalize-space()='1']")).click();
 
}


 



 // 



 


 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
	
}
