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
public class LumioTest {

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
    
    
    

    // TC_03 – Calendar Navigation

    @Test(priority = 3,enabled=true)
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
    
    
    
    //  TC_04 – Weekly Content Validation from October to Till of December

    @Test(priority = 4,enabled=true)
    
    public void TC_04_ValidateWeeklyContentTillEnd() throws InterruptedException {
        
    	
    	log.info("Weekly Content Validation from November to Till of Feb");
    	TestListener.getTest().info("Weekly Content Validation from November to Till of Feb");
    	
    	changeWeek("TC_004");
    	
    	//Looping for 2 month
    	
    	for(int i=0;i<5;i++)
		{
			changeMonth("TC_004");	
		}	
    }
    
    


    /* =========================
       FINAL METRICS REPORT
       ========================= */
    @AfterSuite
public void printExecutionSummary() {

    log.info("===== FINAL EXECUTION SUMMARY =====");

    System.out.println("\n========== FINAL EXECUTION SUMMARY ==========");
    
    System.out.println("\n========== Weekly (November to December) Calendar Validation ==========");
    System.out.println("Weeks Tested: " + MetricsCollector.totalWeekValidated);
    System.out.println("Months Tested: " + MetricsCollector.totalMonthValidated);
    System.out.println("Total Movies Verified (TC_004): " + MetricsCollector.totalTitlesVerifiedTC04);
    
    System.out.println("\n========== Provider Validation (Last 3 Weeks) ==========");
    System.out.println("Providers Validated: " + MetricsCollector.providersValidated.size());
    System.out.println("Provider Names: " + MetricsCollector.providersValidated);
    System.out.println("Total Week Checks for Provider Content: " + MetricsCollector.totalWeekCheckforProviderContent);
    System.out.println("Total Movies in Provider Check: " + MetricsCollector.totalMoviesProvider);
    
    
    System.out.println("\n========== Issues Summary ==========");
    System.out.println("Total Issues Found: " + MetricsCollector.issuesFound);

    if (MetricsCollector.issues.length() > 0) {
        System.out.println("\nDetailed Issues:");
        System.out.println(MetricsCollector.issues);
    } else {
        System.out.println("No Issues Found ✅");
    }
    
    printExecutionSummaryinExtentreport();
}

    
    @BeforeSuite
    public void setupMetrics() {
        MetricsCollector.reset();
        log.info("MetricsCollector reset - starting fresh test run");
    }

    /* =========================
       CLEANUP
       ========================= */
    @AfterClass
    public void tearDown() {
        log.info("Closing browser");
        driver.quit();
    }
    
    

public void  printExecutionSummaryinExtentreport() {

    ExtentReports extent = ExtentManager.getExtent();
    ExtentTest summaryTest = extent.createTest("📊 Final Execution Summary");

    summaryTest.info("===== FINAL EXECUTION SUMMARY =====");

    summaryTest.info("📅 Weekly Calendar Validation (Oct → Dec)");
    summaryTest.info("Weeks Tested: " + MetricsCollector.totalWeekValidated);
    summaryTest.info("Months Tested: " + MetricsCollector.totalMonthValidated);
    summaryTest.info("Total Movies Verified (TC_004): " + MetricsCollector.totalTitlesVerifiedTC04);

    summaryTest.info("🎬 Provider Validation (Last 3 Weeks)");
    summaryTest.info("Providers Validated: " + MetricsCollector.providersValidated.size());
    summaryTest.info("Provider Names: " + MetricsCollector.providersValidated);
    summaryTest.info("Total Week Checks: " + MetricsCollector.totalWeekCheckforProviderContent);
    summaryTest.info("Total Movies in Provider Check: " + MetricsCollector.totalMoviesProvider);


    
    summaryTest.info("⚠️ Issues Summary");
    summaryTest.info("Total Issues Found: " + MetricsCollector.issuesFound);
    if ((MetricsCollectorsSlider.issuesFound.size() > 0))
    summaryTest.fail("Total issues found in TC_02_validate_Watch_Tailer_Button_FrontTopTenVsSlider" + MetricsCollectorsSlider.issuesFound.size() + "</pre>");
    
    if (MetricsCollector.issues.length() > 0) {
        summaryTest.fail("❌ Issues Found:");
        
        
     
        summaryTest.fail("<pre>" + MetricsCollector.issues + "</pre>");
    } else {
        summaryTest.pass("✅ No Issues Found");
    }

    extent.flush();
}
    

	
    
    public String takeScreenshot(String testName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File src = ts.getScreenshotAs(OutputType.FILE);

            // Add timestamp for uniqueness
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String destPath = "screenshots/failures/" + testName + "_" + timestamp + ".png";
            File dest = new File(destPath);
            
            // Create directory if it doesn't exist
            dest.getParentFile().mkdirs();

            FileUtils.copyFile(src, dest);
            log.info("Screenshot saved: " + destPath);
            return dest.getAbsolutePath();
        } catch (Exception e) {
            log.error("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
    
  
	
	public void providerChangeValidation() {
		   WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		    List<WebElement> providers =
		            driver.findElements(By.xpath("//div[@class='py-3 lg:py-4 false']"));

		    for (int i = 0; i < providers.size(); i++) {

		        By providerDivLocator =
		                By.xpath("(//div[@class='py-3 lg:py-4 false'])[" + (i + 1) + "]");

		        WebElement providerDiv =
		                wait.until(ExpectedConditions.elementToBeClickable(providerDivLocator));

		        String providerName =
		                providerDiv.findElement(By.tagName("img")).getAttribute("alt");

		        System.out.println("Clicking provider: " + providerName);

		        providerDiv.click();
		        
		        String rawUrl = driver.getCurrentUrl();
		        String decodedUrl = URLDecoder.decode(rawUrl, StandardCharsets.UTF_8);

		        System.out.println("Decoded URL: " + decodedUrl);

		        boolean isProviderPresent =
		                decodedUrl.toLowerCase().contains(providerName.toLowerCase());

		        soft.assertTrue(
		                isProviderPresent,
		                "Provider mismatch. Expected: " + providerName +
		                " | URL: " + decodedUrl
		        );

		        // Wait for page/content refresh after click
		        wait.until(ExpectedConditions.presenceOfElementLocated(
		                By.xpath("//img[@data-card-type='top-ten-card']")));
		        
		        
		        checkImageMoviesCount();
		        
		        
		        
		      
		        
		    }
		    
		    soft.assertAll();
	}
	
	
	
public void changeMonth(String tc) throws InterruptedException {
		WebElement calendarButton = driver.findElement(By.xpath("//div[contains(@class,'border-l border-[#444444] flex items-center justify-center')]"));
		WebElement Next_Month_Button = driver.findElement(By.xpath("//button[contains(@aria-label,'Go to the Next Month')]"));
		act.moveToElement(calendarButton).perform();
		
		Next_Month_Button.click();
		
		// Add a small wait for calendar to update
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		 MetricsCollector.totalMonthValidated++;
		//checkImageMoviesTitle();
		 
		changeWeek(tc);
	}
	

	
	public void changeWeek(String tc) throws InterruptedException {
	
		 log.info("Validating weekly content");
	    int weekno = 1;
	    int maxweek = 5;
	    act = new Actions(driver);
	    String currentMonth = getCurrentMonthName();
	    if (currentMonth.equalsIgnoreCase("November")) {
	        maxweek = 6;
	    }
	
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	
	    for (int weekRow = 1; weekRow <= maxweek; weekRow++) {
	
	        // Open calendar (hover safety)
	        WebElement calendarButton = wait.until(ExpectedConditions
	                .visibilityOfElementLocated(By.xpath(
	                        "//div[contains(@class,'border-l border-[#444444]')]")));
	
	        js.executeScript("arguments[0].scrollIntoView({block:'center'});", calendarButton);
	        new Actions(driver).moveToElement(calendarButton).perform();
	
	        // 🔑 IMPORTANT: click BUTTON inside TD
	        List<WebElement> weekButtons = driver.findElements(
	                By.xpath("//tbody/tr[" + weekRow + "]/td[1]//button"));
	
	        if (weekButtons.isEmpty()) {
	            continue;
	        }
	
	        WebElement weekButton = weekButtons.get(0);
	
	        // Skip disabled days
	        if (!weekButton.isEnabled()) {
	            continue;
	        }
	
	        wait.until(ExpectedConditions.elementToBeClickable(weekButton));
	        js.executeScript("arguments[0].click();", weekButton);
	
	        System.out.println("Month name  "+currentMonth+ "  WeekNumber " + weekno );
	        log.info("Month name  "+currentMonth+ "  WeekNumber " + weekno );
	       
	        //for weekly tested log
	        
	        MetricsCollector.totalWeekValidated++;
	        MetricsCollector.weeksTested.add(currentMonth+weekno);
	        
	        checkImageMoviesTitle(tc);
	        
	        testEachProviderMovies();
	        
	        weekno++;
	    }
	}

	

//new helper function
	
	public void testEachProviderMovies() throws InterruptedException{
		 List<WebElement> initialProviders = driver.findElements(
			        By.xpath("//div[@class='py-3 lg:py-4 false']")
			    );
			    int providerCount = initialProviders.size();
			    
		 for(int j = 0; j < providerCount; j++) {
		        System.out.println("\n========================================");
		        System.out.println("🔄 Testing Provider " + (j + 1) + " of " + providerCount);
		        System.out.println("========================================");
		        
		        Thread.sleep(2000);
		        
		        // ✅ RE-FETCH providers list to avoid stale elements
		        List<WebElement> providers = driver.findElements(
		            By.xpath("//div[@class='py-3 lg:py-4 false']")
		        );
		        
		        // Scroll to the provider element
		        WebElement providerElement = providers.get(j);
		        ((JavascriptExecutor) driver).executeScript(
		            "arguments[0].scrollIntoView({block:'center'});", 
		            providerElement
		        );
		        
		        Thread.sleep(500); // Small wait after scroll
		        
		        // Click the provider (with fallback to JavaScript click)
		        try {
		            providerElement.click();
		            System.out.println("✅ Clicked provider " + (j + 1) + " using standard click");
		        } catch (ElementNotInteractableException e) {
		            System.out.println("⚠️ Standard click failed, using JavaScript click");
		            ((JavascriptExecutor) driver).executeScript(
		                "arguments[0].click();", 
		                providerElement
		            );
		        }
		        
		        Thread.sleep(1500);

		        // 1️⃣ Get front page Top Ten movies
		        System.out.println("📋 Step 1: Collecting movies from front page");
		        List<String> frontMovies = getFrontTopTenMovies();
		        soft.assertFalse(frontMovies.isEmpty(), "❌ No movies found on front page for provider " + (j + 1));
		        
		        metrics.setTotalMoviesOnFrontPage(frontMovies.size());
		        System.out.println("✅ Total movies found on front page: " + frontMovies.size());

		        // 2️⃣ Click first movie
		        System.out.println("📋 Step 2: Opening slider by clicking first movie");
		        
		        List<WebElement> movieElements = driver.findElements(
		            By.xpath("//img[@data-card-type='top-ten-card']")
		        );
		        
		        if (movieElements.isEmpty()) {
		            System.out.println("❌ No movie elements found for provider " + (j + 1));
		            soft.fail("No movies found for provider " + (j + 1));
		            continue; // Skip to next provider
		        }
		        
		        WebElement firstMovie = movieElements.get(0);
		        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstMovie);
		        System.out.println("✅ First movie clicked, slider opened");

		        Thread.sleep(1500);

		        // 3️⃣ Collect movies from slider
		        System.out.println("📋 Step 3: Validating movies in slider");
		        List<String> sliderMovies = getSliderMovies(frontMovies.size(),soft);
		        
		        metrics.setTotalMoviesInSlider(sliderMovies.size());
		        System.out.println("✅ Total movies validated in slider: " + sliderMovies.size());

		        // 4️⃣ Compare front vs slider
		        System.out.println("📋 Step 4: Comparing front page movies with slider movies");
		        compareMovies(frontMovies, sliderMovies);
		        
		        // 5️⃣ Navigate back to home page for next provider
		        System.out.println("🔙 Navigating back to home page...");
		        driver.get("https://tldr.lumiolabs.ai/");
		        Thread.sleep(2000);
		        System.out.println("✅ Ready for next provider");
		    }
	}
	
	 public List<String> getFrontTopTenMovies() {
	        System.out.println("🔍 Fetching movies from front page...");
	        List<String> frontMovies = new ArrayList<>();

	        List<WebElement> movies = driver.findElements(
	                By.xpath("//img[@data-card-type='top-ten-card']")
	        );

	        System.out.println("📊 Found " + movies.size() + " movie elements on front page");

	        for (int i = 0; i < movies.size(); i++) {
	            WebElement movie = movies.get(i);
	            String title = getMovieTitle(movie);
	            if (title != null && !title.isEmpty()) {
	                frontMovies.add(title.trim());
	                System.out.println("   Movie " + (i + 1) + ": " + title.trim());
	            }
	        }

	        System.out.println("🎬 Front Page Movies List:");
	        frontMovies.forEach(m -> System.out.println(" ➤ " + m));

	        return frontMovies;
	    }
	
	
	 // ===================== SLIDER MOVIES =====================
	    public List<String> getSliderMovies(int expectedCount,SoftAssert soft) throws InterruptedException {
	        System.out.println("🔄 Starting slider navigation and validation...");
	        List<String> sliderMovies = new ArrayList<>();

	        for (int i = 0; i < expectedCount; i++) {
	            System.out.println("----------------------------------------");
	            System.out.println("🎯 Processing Slider Position: " + (i + 1) + "/" + expectedCount);

	            String movieName = getCurrentMovieName();

	            if (!movieName.equals("Not found") && !sliderMovies.contains(movieName)) {
	                sliderMovies.add(movieName);
	                System.out.println("✅ Movie Name: " + movieName);
	                metrics.addMovieValidated(movieName);
	            } else if (movieName.equals("Not found")) {
	                System.out.println("❌ Could not retrieve movie name at position " + (i + 1));
	                metrics.addIssue("Movie name not found at slider position " + (i + 1));
	            } else {
	               // System.out.println("⚠️ Duplicate movie detected: " + movieName);
	            	soft.fail("⚠️ Duplicate movie detected: " + movieName);
	            
	            	MetricsCollectorsSlider.duplicateMovies++;
	            	continue;
	            }

	            // Validate content
	            contentValidation(movieName, soft, i + 1);

	            // Navigate to next slide
	            driver.findElement(By.tagName("body")).sendKeys(Keys.ARROW_RIGHT);
	            System.out.println("⏭️ Navigated to next slide");
	            Thread.sleep(800);
	        }

	        System.out.println("----------------------------------------");
	        return sliderMovies;
	    }

	    // ===================== COMPARE =====================
	    public void compareMovies(List<String> front, List<String> slider) {
	        System.out.println("🔍 Comparing front page movies with slider movies...");

	        soft.assertEquals(
	                slider.size(),
	                front.size(),
	                "❌ Movie count mismatch (Front vs Slider)"
	        );

	        int matchCount = 0;
	        int mismatchCount = 0;

	        for (int i = 0; i < slider.size(); i++) {
	            String frontMovie = front.get(i).toLowerCase().trim();
	            String sliderMovie = slider.get(i).toLowerCase().trim();

	            boolean matches = sliderMovie.contains(frontMovie) || frontMovie.contains(sliderMovie);

	            if (matches) {
	                System.out.println("✅ Match at position " + (i + 1) + ": " + front.get(i));
	                matchCount++;
	            } else {
	                System.out.println("❌ Mismatch at position " + (i + 1) + 
	                         " | Front: " + front.get(i) + 
	                         " | Slider: " + slider.get(i));
	                mismatchCount++;
	                metrics.addIssue("Title mismatch at position " + (i + 1));
	            }

	            soft.assertTrue(matches,
	                    "❌ Title mismatch at index " + i +
	                            " | Front: " + front.get(i) +
	                            " | Slider: " + slider.get(i)
	            );
	        }

	        System.out.println("📊 Comparison Results: " + matchCount + " matches, " + mismatchCount + " mismatches");
	    }
	 
	 
	 
	 
	 
	// Helper method to get current month name from calendar
	    
	    public void contentValidation(String sliderMovie, SoftAssert soft, int movieIndex) {
	        System.out.println("🔍 Validating content for: " + sliderMovie + " (Position: " + movieIndex + ")");
	        
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	        String xpathofMovieBox = "//div[contains(@class,'swiper-slide-active')]//span[normalize-space()='Watch on']";
	        String xpathofTailerButton = "//div[contains(@class,'swiper-slide-active')]//span[normalize-space()='Play Trailer']";
	        String xpathofDescription = "//div[contains(@class,'swiper-slide-active')]//p[contains(@class,'red-hat-semi-bold')]";

	        try {
	            // ================= Description Validation =================
	            System.out.println("📝 Checking description...");
	            String disText = driver.findElement(By.xpath(xpathofDescription)).getText();

	            if (disText.isBlank()) {
	                System.out.println("❌ Description is MISSING for: " + sliderMovie);
	                metrics.addDescriptionMissing(sliderMovie);
	                takeScreenshot("Movie_" + sanitizeFileName(sliderMovie) + "_MissingDescription");
	                log.info("Description is missing for " + sliderMovie);
	                soft.fail("Description is missing for " + sliderMovie);
	            } else {
	                System.out.println("✅ Description found: " + disText.substring(0, Math.min(50, disText.length())) + "...");
	                metrics.addDescriptionFound(sliderMovie);
	            }

	            // Track validation status
	            boolean watchOnValidated = false;
	            boolean trailerValidated = false;

	            // ================= Watch On Validation =================
	            System.out.println("🔍 Validating 'Watch On' button for: " + sliderMovie);
	            if (validateWatchOn(wait, xpathofMovieBox, sliderMovie)) {
	                watchOnValidated = true;
	                metrics.incrementWatchOnValidated();
	                System.out.println("✅ 'Watch On' validation PASSED for: " + sliderMovie);
	                log.info("✅ 'Watch On' validation PASSED for: " + sliderMovie);
	            } else {
	                System.out.println("❌ 'Watch On' validation FAILED for: " + sliderMovie);
	                takeScreenshot("Movie_" + sanitizeFileName(sliderMovie) + "_WatchOnFailed");
	                soft.fail("Movie (" + sliderMovie + ") - Watch On validation failed");
	                log.info("Movie (" + sliderMovie + ") - Watch On validation failed");
	                metrics.incrementWatchOnFailed();
	            }

	            // ================= Play Trailer Validation =================
	            System.out.println("🔍 Validating 'Play Trailer' button for: " + sliderMovie);
	            if (validatePlayTrailer(wait, xpathofTailerButton, sliderMovie)) {
	                trailerValidated = true;
	                metrics.incrementTrailerValidated();
	                System.out.println("✅ 'Play Trailer' validation PASSED for: " + sliderMovie);
	                log.info("✅ 'Play Trailer' validation PASSED for: " + sliderMovie);
	                
	            } else {
	                System.out.println("❌ 'Play Trailer' validation FAILED for: " + sliderMovie);
	                takeScreenshot("Movie_" + sanitizeFileName(sliderMovie) + "_TrailerFailed");
	                soft.fail("Movie (" + sliderMovie + ") - Play Trailer validation failed");
	                log.info("Movie (" + sliderMovie + ") - Play Trailer validation failed");
	                metrics.incrementTrailerFailed();
	            }

	            // Close any open dialogs
	            new Actions(driver)
	                    .pause(Duration.ofSeconds(5))
	                    .sendKeys(Keys.ESCAPE)
	                    .perform();

	            // Final status log
	            String statusEmoji = (watchOnValidated && trailerValidated) ? "✅" : "⚠️";
	            System.out.println(statusEmoji + " Movie " + movieIndex + " (" + sliderMovie + ") - Validation Summary: " +
	                    "WatchOn=" + (watchOnValidated ? "PASS" : "FAIL") +
	                    ", Trailer=" + (trailerValidated ? "PASS" : "FAIL"));

	        } catch (Exception e) {
	            System.out.println("💥 EXCEPTION at movie index " + movieIndex + " (" + sliderMovie + "): " + e.getMessage());
	            e.printStackTrace();
	            takeScreenshot("Movie_" + sanitizeFileName(sliderMovie) + "_Exception");
	            soft.fail("Error at movie index " + movieIndex + " (" + sliderMovie + "): " + e.getMessage());

	            metrics.addIssue("Movie " + sliderMovie + " - Exception: " + e.getMessage());
	            
	            try {
	                driver.navigate().refresh();
	                System.out.println("🔄 Page refreshed after exception");
	            } catch (Exception ex) {
	                System.out.println("💥 CRITICAL: Failed to refresh page during cleanup: " + ex.getMessage());
	                takeScreenshot("Movie_" + sanitizeFileName(sliderMovie) + "_RefreshFailed");
	                soft.fail("Failed to refresh page during cleanup: " + ex.getMessage());
	            }
	        }
	    }    
	    

	    private String sanitizeFileName(String fileName) {
	        if (fileName == null || fileName.isEmpty()) {
	            return "Unknown";
	        }
	        return fileName.replaceAll("[^a-zA-Z0-9-_]", "_").substring(0, Math.min(fileName.length(), 50));
	    }

	    
	    
	    public String getCurrentMovieName() {
	        try {
	            WebElement activeSlide = wait.until(
	                    ExpectedConditions.presenceOfElementLocated(
	                            By.cssSelector(".swiper-slide-active .text-white.md\\:text-\\[28px\\]")
	                    )
	            );

	            String name = activeSlide.getText().trim();
	            return name.isEmpty() ? "Not found" : name;

	        } catch (Exception e) {
	            System.out.println("Could not retrieve current movie name: " + e.getMessage());
	            return "Not found";
	        }
	    }

	    // ================= WATCH ON VALIDATION =================
	    private boolean validateWatchOn(WebDriverWait wait, String xpath, String title) {
	        try {
	            String parentWindow = driver.getWindowHandle();

	            WebElement watchButton = findElementSafely(wait, By.xpath(xpath));
	            if (watchButton == null) {
	                System.out.println("❌ 'Watch On' button NOT FOUND for: " + title);
	                soft.fail("'Watch On' button not found for: " + title);
	                return false;
	            }

	            System.out.println("✅ 'Watch On' button found for: " + title);

	            ((JavascriptExecutor) driver)
	                    .executeScript("arguments[0].scrollIntoView({block:'center'});", watchButton);

	            try {
	                watchButton.click();
	                System.out.println("Clicked 'Watch On' button using standard click");
	            } catch (ElementNotInteractableException e) {
	                System.out.println("Using JavaScript click for 'Watch On' button");
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", watchButton);
	            }

	            // Wait for new window
	            try {
	                wait.until(d -> d.getWindowHandles().size() > 1);
	                System.out.println("New window opened successfully");
	            } catch (TimeoutException e) {
	                System.out.println("❌ New window DID NOT OPEN for: " + title);
	                soft.fail("New window didn't open for: " + title);
	                return false;
	            }

	            // Switch to new window
	            boolean windowSwitched = false;
	            for (String window : driver.getWindowHandles()) {
	                if (!window.equals(parentWindow)) {
	                    driver.switchTo().window(window);
	                    windowSwitched = true;
	                    try {
	                        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

	                        // Wait for full page load
	                        wait.until(webDriver ->
	                                ((JavascriptExecutor) webDriver)
	                                        .executeScript("return document.readyState")
	                                        .equals("complete")
	                        );

	                        String providerPageTitle = driver.getTitle();
	                        String providerPageUrl = driver.getCurrentUrl();

	                        System.out.println("✅ Provider page opened - Title: " + providerPageTitle);
	                        System.out.println("   Provider URL: " + providerPageUrl);

	                        metrics.addProviderPage(providerPageTitle, providerPageUrl);

	                        if (providerPageTitle.toLowerCase().contains(title.toLowerCase())) {
	                            System.out.println("✅ Provider page title matches movie: " + title);
	                        } else {
	                            System.out.println("⚠️ Provider page title may not match - Expected: " + title + " | Actual: " + providerPageTitle);
	                        }

	                    } catch (Exception e) {
	                        System.out.println("⚠️ Could not get provider page details: " + e.getMessage());
	                    }
	                    driver.close();
	                    System.out.println("Closed provider window");
	                    break;
	                }
	            }

	            driver.switchTo().window(parentWindow);

	            if (!windowSwitched) {
	                System.out.println("❌ Failed to switch to new window for: " + title);
	                return false;
	            }

	            return true;

	        } catch (Exception e) {
	            System.out.println("💥 Watch On validation exception for " + title + ": " + e.getMessage());
	            e.printStackTrace();
	            soft.fail("Watch On validation error for " + title + ": " + e.getMessage());
	            return false;
	        }
	    }

	    // ================= PLAY TRAILER VALIDATION =================
	    private boolean validatePlayTrailer(WebDriverWait wait, String xpath, String title) {
	        try {
	            WebElement playTrailerButton = findElementSafely(wait, By.xpath(xpath));
	            if (playTrailerButton == null) {
	                System.out.println("❌ 'Play Trailer' button NOT FOUND for: " + title);
	                soft.fail("'Play Trailer' button not found for: " + title);
	                return false;
	            }

	            System.out.println("✅ 'Play Trailer' button found for: " + title);

	            playTrailerButton.click();
	            System.out.println("Clicked 'Play Trailer' button successfully");

	            // Wait for video element or player to appear
	            try {
	                wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("video")));
	                System.out.println("✅ Trailer video element detected");
	            } catch (TimeoutException e) {
	                System.out.println("⚠️ Trailer video element not detected (may still be loading or using different player)");
	            }

	            return true;

	        } catch (Exception e) {
	            System.out.println("💥 Play Trailer validation exception for " + title + ": " + e.getMessage());
	            e.printStackTrace();
	            soft.fail("Play Trailer validation error for " + title + ": " + e.getMessage());
	            return false;
	        }
	    }    
	
	    private WebElement findElementSafely(WebDriverWait wait, By locator) {
	        try {
	            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	        } catch (TimeoutException | NoSuchElementException e) {
	            System.out.println("Element not found: " + locator);
	            return null;
	        }
	    }    
	    
	public String getCurrentMonthName() {
		try {
			String calendarYearMonth = driver.findElement(By.xpath("//span[contains(@role,'status')]")).getText();
			// Extract month name (e.g., "November 2025" -> "November")
			return calendarYearMonth.split(" ")[0];
		} catch (Exception e) {
			System.out.println("Error getting month name: " + e.getMessage());
			return "November"; // Fallback
		}
	}
	
	public void checkImageMoviesTitle(String tc) {

    List<WebElement> listofmoviesimages =
            driver.findElements(By.xpath("//img[@data-card-type='top-ten-card']"));

    log.info("Total no of movies " + listofmoviesimages.size());
    System.out.println("Total no of movies " + listofmoviesimages.size());

    for (int i = 0; i < listofmoviesimages.size(); i++) {

        try {
            // Re-fetch element to avoid stale element issues
            WebElement singlemovieimage = driver.findElements(
                    By.xpath("//img[@data-card-type='top-ten-card']"))
                    .get(i);

            // Ensure image is visible (important for headless)
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});",
                    singlemovieimage
            );

            String movieTitle = null;

            // ===============================
            // 1️⃣ PRIMARY (Headless-safe): alt attribute
            // ===============================
            movieTitle = singlemovieimage.getAttribute("alt");

            // ===============================
            // 2️⃣ FALLBACK: title attribute
            // ===============================
            if (movieTitle == null || movieTitle.trim().isEmpty()) {
                movieTitle = singlemovieimage.getAttribute("title");
            }

            // ===============================
            // 3️⃣ FINAL FALLBACK: DOM text (no hover wait)
            // ===============================
            if (movieTitle == null || movieTitle.trim().isEmpty()) {
                try {
                    WebElement titleElement = singlemovieimage.findElement(
                            By.xpath("./ancestor::div[contains(@class,'group')]//span[contains(@class,'red-hat-semi-bold')]")
                    );
                    movieTitle = titleElement.getText().trim();
                } catch (Exception ignored) {
                    // Intentionally ignored
                }
            }

            // ===============================
            // RESULT LOGGING
            // ===============================
            if (movieTitle == null || movieTitle.isEmpty()) {
                log.warn("⚠️ Movie index " + i + " - Title not available (headless-safe)");
                System.out.println("⚠️ Movie index " + i + " - Title not available");
            } else {
                log.info("✅ Movie index " + i + " | Title: " + movieTitle);
                System.out.println("Movie index " + i + " | Title: " + movieTitle);

                if ("TC_004".equals(tc)) {
                    MetricsCollector.totalTitlesVerifiedTC04++;
                } else {
                    MetricsCollector.totalTitlesVerified++;
                }
            }

        } catch (Exception e) {
            log.error("❌ Error processing movie index " + i + " : " + e.getMessage(), e);
            System.out.println("Error processing movie index " + i + " : " + e.getMessage());
        }
    }
}

	
	
	public void checkImageMoviesCount() {

    List<WebElement> listofmoviesimages =
            driver.findElements(By.xpath("//img[@data-card-type='top-ten-card']"));

    int totalMovies = listofmoviesimages.size();
    System.out.println("Total no of movies: " + totalMovies);

    // ✅ STRICT RULE: must be EXACTLY 10
    soft.assertEquals(
            totalMovies,
            10,
            "Movie count is not exactly 10. Found: " + totalMovies + " ProviderUrl has incorrect movies count"+ driver.getCurrentUrl()
    );

    int imageNumber = 1;
    for (WebElement singlemovieimage : listofmoviesimages) {
        soft.assertTrue(
                singlemovieimage.isDisplayed(),
                imageNumber + " Position movie image not displayed"
        );
        imageNumber++;
        
        MetricsCollector.totalMoviesProvider++;
    }
    }

	public String getMovieTitle(WebElement singleMovieImage) {

	    try {
	        // Ensure visibility (headless-safe)
	        ((JavascriptExecutor) driver).executeScript(
	                "arguments[0].scrollIntoView({block:'center'});",
	                singleMovieImage
	        );

	        // 1️⃣ BEST: alt attribute
	        String title = singleMovieImage.getAttribute("alt");
	        if (title != null && !title.trim().isEmpty()) {
	            return title.trim();
	        }

	        // 2️⃣ fallback: title attribute
	        title = singleMovieImage.getAttribute("title");
	        if (title != null && !title.trim().isEmpty()) {
	            return title.trim();
	        }

	        // 3️⃣ last fallback: DOM text (NO hover, NO wait)
	        try {
	            WebElement titleElement = singleMovieImage.findElement(
	                    By.xpath("./ancestor::div[contains(@class,'group')]//span[contains(@class,'red-hat-semi-bold')]")
	            );
	            title = titleElement.getText().trim();
	            if (!title.isEmpty()) {
	                return title;
	            }
	        } catch (Exception ignored) {}

	        // Nothing found
	        return null;

	    } catch (Exception e) {
	        log.error("Error getting movie title", e);
	        return null;
	    }
	}

	
}