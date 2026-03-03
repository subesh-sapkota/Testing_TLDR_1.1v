
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
import java.util.Set;

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

       
   	 
        WebElement popUpCloseButton = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("(//div[contains(@class,'text-white')])/button")
                )
        );

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", popUpCloseButton);           
        
        
        Assert.assertEquals(driver.getTitle(), expected);
    }
    
    
    
    @Test(priority = 3,enabled= false)
    public void TC_03_Check_Footer() {
    	
    	
    

        SoftAssert soft = new SoftAssert();

        WebElement privacyPolicyElement = driver.findElement(By.xpath("//div[@id='footer']//a[@href='/privacy/']"));
        WebElement EULAlink = driver.findElement(By.xpath("//div[@id='footer']//a[@href='/eula/']"));
        WebElement CHANGELOGSElement = driver.findElement(By.xpath("//div[@id='footer']//a[@href='/changelogs/']"));

        // Validate Text
        soft.assertEquals(privacyPolicyElement.getText().trim(), "PRIVACY POLICY");
        soft.assertEquals(EULAlink.getText().trim(), "END-USER LICENSE AGREEMENT");
        soft.assertEquals(CHANGELOGSElement.getText().trim(), "CHANGELOGS");

        // Store parent window
        String parentWindow = driver.getWindowHandle();

        // -------- PRIVACY --------
        privacyPolicyElement.click();
        switchToNewWindow(parentWindow);
        soft.assertTrue(driver.getCurrentUrl().contains("/privacy/"));
        driver.close();
        driver.switchTo().window(parentWindow);

        // -------- EULA --------
        EULAlink.click();
        switchToNewWindow(parentWindow);
        soft.assertTrue(driver.getCurrentUrl().contains("/eula/"));
        driver.close();
        driver.switchTo().window(parentWindow);

        // -------- CHANGELOGS --------
        CHANGELOGSElement.click();
        switchToNewWindow(parentWindow);
        soft.assertTrue(driver.getCurrentUrl().contains("/changelogs/"));
        driver.close();
        driver.switchTo().window(parentWindow);

        soft.assertAll();
    }
    
    
    
    
    @Test(priority = 4 , enabled=false)
    public void TC_04_Check_FeedbackForm() {

        SoftAssert soft = new SoftAssert();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement feedbackLink = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//span[contains(text(),'FEEDBACK FORM')]")));

        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Scroll to element
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", feedbackLink);

        // Optional small wait for scroll animation
        try { Thread.sleep(500); } catch (InterruptedException e) { }

        // JS Click (bypasses overlay issue)
        js.executeScript("arguments[0].click();", feedbackLink);
        
      WebElement xpathofTextField= driver.findElement(By.xpath("//textarea[@name='feedback']"));
      WebElement xpathNameElement= driver.findElement(By.xpath("//input[@name='name']"));
        WebElement xpathEmailElement= driver.findElement(By.xpath(" //input[@name='email']"));
        
        
        
       WebElement  xpathephoneElement  = driver.findElement(By.xpath("//input[@name='phone']"));
        
        WebElement submitElement = driver.findElement(By.xpath("//button[@type='submit']"));
        
        xpathofTextField.sendKeys("Please ignore it this is automation run      Verifying page title\n"
        		+ "[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 34.65 s -- in TestSuite\n"
        		+ "[INFO] ");
        
        xpathNameElement.sendKeys("Tester");
        
        xpathEmailElement.sendKeys("subesh@circuithouse.tech");
        
        
        
     
        
        
        WebElement uploadElement = driver.findElement(By.xpath("//input[@id='file-upload']"));

        // Provide full file path (IMPORTANT)
        uploadElement.sendKeys(" /Users/subeshsapkota/Downloads/test.webp");
        
        
        submitElement.click();    
        
        

        soft.assertAll();
    }
    
    
    
    
    
    @Test(priority = 5 , enabled=true)
    public void TC_05_Check_Profile_Section() throws InterruptedException {
    	
    	

    	// 2️⃣ Set token in localStorage
        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript(
            "window.localStorage.setItem('accesstoken','eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjc24iOiJjaDAxMDAwMDAwMzk0IiwiZXhwIjoxNzc0MDczODU2LCJ0b2tlblR5cGUiOiJhdXRoVG9rZW4iLCJ1c2VySWQiOiJjNGFjMWQzMy0wZWEyLTQ5YWUtOGU1Mi00MGM4Y2JlY2VkOGUifQ.eQ-JdMysoheIA93i3UJY3Ad-KFXerGW1tXWqIdm2fX4');");
        
        js.executeScript(
            "window.localStorage.setItem('csn','ch01000000394');");

        // 3️⃣ Refresh page to apply authentication
        driver.navigate().refresh();

        // 4️⃣ Use explicit wait (better than Thread.sleep)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
   
        
        
     /*   By signInBtn = By.id("signin-btn");

        WebElement profileSection = wait.until(
            ExpectedConditions.visibilityOfElementLocated(signInBtn));

        System.out.println(driver.findElement(signInBtn).getText());
        
        */
      
        
        //click on profile 
        
        
      //  WebElement xpatProfileElement= driver.findElement(By.xpath("//a[@href='/profile']"));
        
       
        
  
        
        
        
         wait = new WebDriverWait(driver, Duration.ofSeconds(20));

         driver.navigate().to("https://tldr.lumiolabs.ai/profile");

       //   wait = new WebDriverWait(driver, Duration.ofSeconds(20));

       // Click Favourites
         wait.until(ExpectedConditions.elementToBeClickable(
                 By.xpath("(//span[text()='Favourites'])[1]")
         )).click();

        
        
    
        
        
       
  
      // driver.findElement(By.xpath("(//span[text()='Favourites'])[1]")).click();
       
         By editProfileBtn = By.xpath("//a[@href='/profile/edit']");

      // 1️⃣ Wait for correct page
      wait.until(ExpectedConditions.urlContains("/profile"));

      // 2️⃣ Wait for React to finish rendering
      wait.until(driver -> 
          ((JavascriptExecutor) driver)
              .executeScript("return document.readyState")
              .equals("complete")
      );

      // 3️⃣ Wait until element appears in DOM
      wait.until(ExpectedConditions.presenceOfElementLocated(editProfileBtn));

      // 4️⃣ Use fresh reference
      WebElement editBtn = driver.findElement(editProfileBtn);

      // 5️⃣ Click using JS (prevents stale during click)
      ((JavascriptExecutor) driver).executeScript("arguments[0].click();", editBtn);
       
       WebElement xpathName= driver.findElement(By.xpath("//input[@name='name']"));
       
       WebElement xpathUserName= driver.findElement(By.xpath("//input[@name='username']"));
       
       WebElement xpathEmail= driver.findElement(By.xpath("//input[@disabled]"));
       WebElement xpathmobile= driver.findElement(By.xpath("//input[@name='mobile']"));
       
       xpathName.sendKeys("subesh");
       xpathUserName.sendKeys("sudeep");
       xpathmobile.sendKeys("8084637893");
       
     boolean checkemail= xpathEmail.isEnabled();
     
     
     System.out.print("Email check " +checkemail);
     soft.assertFalse(checkemail);
       
       

       // Click Save
       By saveBtn = By.xpath("//button[text()='Save']");

       wait.until(ExpectedConditions.elementToBeClickable(saveBtn)).click();
      
      
      
      
       
  
    	
    }
    
    
    
    @Test(priority = 6 , enabled=true)
    public void TC_06_Close_bowser() {
    	driver.close();
    }
    
    
    public void switchToNewWindow(String parentWindow) {
        Set<String> allWindows = driver.getWindowHandles();
        for (String window : allWindows) {
            if (!window.equals(parentWindow)) {
                driver.switchTo().window(window);
                break;
            }
        }
    }

    
   
    
    
    
}
    
    