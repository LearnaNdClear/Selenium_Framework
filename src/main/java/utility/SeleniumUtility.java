package utility;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FromTerm;
import javax.mail.search.SearchTerm;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

/**
 * 
 * @author LearnNclear <www.learnnclear.com>
 *
 */

public class SeleniumUtility {
	
	RemoteWebDriver driver;
	WebElement element = null;
	String parentWindowID = null;
	boolean windowPresence = false;
	WebDriver childWindowdriver = null;
	
	/**
	 * This method launches required browser and loads the URL in it.
	 * @param browserName
	 * @param URLAddress
	 */
	public void launchBrowserAndLoadURL(String browserName, String URLAddress){
		if(browserName.equalsIgnoreCase("firefox")){
			System.setProperty("webdriver.gecko.driver","Driver/geckodriver.exe");
			driver = new FirefoxDriver();			
		}else if(browserName.equalsIgnoreCase("internet")){
			System.setProperty("webdriver.ie.driver", "Driver/IEDriverServer.exe");
			driver = new InternetExplorerDriver();
		}else if(browserName.equalsIgnoreCase("chrome")){
			System.setProperty("webdriver.chrome.driver", "Driver/chromedriver.exe");
			driver = new ChromeDriver();
		}else{
			try {
				throw new Exception("invalid browser name");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get(URLAddress);
	}
	
	public void Ale() throws Exception{
		Alert ale=driver.switchTo().alert();
		ale.accept();
		
	}
	
	
	public void OpenURL(String Url){
		try {
			driver.get(Url);
		} catch (Exception e) {
			Assert.fail("Unable to open URL " + Url);
		}
	}
	
	public WebElement locateElement(String locatorType, String locator) throws Exception{
		try {
			if (locatorType.equalsIgnoreCase("id")) {
				element=driver.findElement(By.id(locator));
			} else if (locatorType.equalsIgnoreCase("name")) {
				element=driver.findElement(By.name(locator));
			} else if (locatorType.equalsIgnoreCase("class")) {
				element=driver.findElement(By.className(locator));
			} else if (locatorType.equalsIgnoreCase("xpath")) {
				element=driver.findElement(By.xpath(locator));
			}else if(locatorType.equalsIgnoreCase("css")){
				element=driver.findElement(By.cssSelector(locator));
			}
			else if(locatorType.equalsIgnoreCase("link")) {
				element=driver.findElement(By.linkText(locator));
			}
			else{
				throw new Exception("invalid locator");
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	
		return element;
	}
	
	/**
	 * Handles text box
	 * @param locatorType id,name,class,xpath,css
	 * @param locator
	 * @param textValue
	 * @throws Exception 
	 */
	public void textBoxHandling(String locatorType, String locator, String textValue) throws Exception{
		try {
			//locateElement(locatorType, locator).sendKeys(textValue);
			element = locateElement(locatorType, locator);
			element.sendKeys(textValue);
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	
	public void dropDownHandling(String locatorType, String locator, int valueToBeSelected) throws Exception{
		locateElement(locatorType, locator);
		
		try {
			Select select = new Select(element);
			select.selectByIndex(valueToBeSelected);
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	
	public void buttonHandling(String locatorType, String locator) throws Exception{
		locateElement(locatorType, locator).click();
	}	
	
	
	
	public void TakeScreenshot(String path){
		try {
			File sFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(sFile, new File(path));
		} catch (Exception e) {
			Assert.fail("Unable to take screenshot");
		}
	}
	
	public void checkBoxHandling(String locatorType, String locator){
		try {
			element=locateElement(locatorType, locator);
			if(!element.isSelected()){
				element.click();
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public WebDriver windowHandling(String titleOfExpectedWindow){
		parentWindowID = driver.getWindowHandle();
		System.out.println("parent window ID " + parentWindowID);
		
		// get all window IDs
		Set<String> windowIDs = driver.getWindowHandles();

		/**
		 * loop thru window IDs and switch to each window & do getTitle action to verify whether the current window title
		 * matches with expected window title
		 *  
		 */
		
		for (String windowID : windowIDs) {
			System.out.println(windowID);
			
			childWindowdriver = driver.switchTo().window(windowID);
			String title = childWindowdriver.getTitle();
			System.out.println("TITLE is " + title);
			
			if (title.contains(titleOfExpectedWindow)) {
				break;
			}

		}
		
		return childWindowdriver;

	}

	public void goBackParentWindow(String titleOfExpectedWindow){
		childWindowdriver.switchTo().window(parentWindowID);
	}

	public void framesHandling(String locatorType, String locator, String titleOfExpectedWindow) throws Exception{
		driver.switchTo().frame(locateElement(locatorType, locator));
	}
	
	public void goOutOfFrame(String titleOfExpectedWindow){
		driver.switchTo().defaultContent();
	}
	public void screenShot(){
//		Random random = new Random();
//		random.ints(4444, 5555);
		
/**		File file = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(file, new File("C:\\screenShot"+random.nextInt()));
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		
		
		try {
			File file = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(file, new File("C:\\Users\\njayapal\\Desktop\\screenShot.png"));
		} catch (WebDriverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String readData(String keyName, String fileName) {
		String value = null;
		try {

			Properties prop = new Properties();
			File file = new File(fileName);

			if (file.exists()) {
				FileInputStream fileinput = new FileInputStream(file);
				prop.load(fileinput);

				value = prop.getProperty(keyName);
			} 
				
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return value;
	}
	//added to new method to pause and close browser - Vinoth
	public void pause(int ms){
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			//cleanup
			Thread.currentThread().interrupt();
		}
	}
	
	public void closeBrowser(){
		driver.close();
	}
	
	/**
	 * Verify the content of PDF present in local path
	 * 
	 * @param filePath
	 * @param pageNumber
	 * @param verifyText
	 * @return
	 */
	public boolean verifyPdfContent(String filePath, int pageNumber, String verifyText) {
		boolean blnTxtPresent =false;
		try {
			BufferedInputStream file = new BufferedInputStream(new FileInputStream(new File(filePath)));
			PDFParser parser = new PDFParser(file);			
			parser.parse();
			COSDocument cosDoc = parser.getDocument();
			PDFTextStripper pdfStripper = new PDFTextStripper();
			pdfStripper.setStartPage(pageNumber);
			pdfStripper.setEndPage(pageNumber);
			PDDocument pdDoc = new PDDocument(cosDoc);
			String parsedText = pdfStripper.getText(pdDoc);
			blnTxtPresent = parsedText.contains(verifyText);
		} catch (Exception e) {
			System.out.println("Unable to read content of pdf file ");
		}
				
		return blnTxtPresent;		
	}
	
	/**
	 * Verify the content of PDF which is hosted in web
	 * 
	 * @param pdfUrl
	 * @param pageNumber
	 * @param verifyText
	 * @return
	 */
	
	public boolean verifyPdfContentFromURL(String pdfUrl, int pageNumber, String verifyText) {
		boolean blnTxtPresent =false;
		try {
			
			/*Add url Proxy details if you any and uncomment it
			System.setProperty("http.proxyHost", "127.0.0.1");
		    System.setProperty("http.proxyPort","3128");*/
			
			URL url = new URL(pdfUrl);
			BufferedInputStream file = new BufferedInputStream(url.openStream()); 
			PDFParser parser = new PDFParser(file);			
			parser.parse();
			COSDocument cosDoc = parser.getDocument();
			PDFTextStripper pdfStripper = new PDFTextStripper();
			pdfStripper.setStartPage(pageNumber);
			pdfStripper.setEndPage(pageNumber);
			PDDocument pdDoc = new PDDocument(cosDoc);
			String parsedText = pdfStripper.getText(pdDoc);
			blnTxtPresent = parsedText.contains(verifyText);
		} catch (Exception e) {
			System.out.println("Unable to read content of pdf file ");
		}
				
		return blnTxtPresent;		
	}
	
	/**
	 * Create file with given characters in particular location
	 * 
	 * @param fileName
	 * @param dir
	 * @param content
	 * @throws IOException
	 */
	public void createFile(String fileName, String dir, String content) throws IOException {		
		File theDir = new File(dir);
		//if directory doesn't exist then create it
		if (!theDir.exists()) {
			theDir.mkdir();
		}
				
		File file = new File(theDir + "/" + fileName);
		FileOutputStream fop = new FileOutputStream(file);
		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}
		// get the content in bytes
		byte[] contentInBytes = content.getBytes();

		fop.write(contentInBytes);
		fop.flush();
		fop.close();
	}
	
	/**
	 * Delete file inside the folder and folder itself
	 * 
	 * @param folderPath
	 */
	public void deleteFolder(String folderPath) {
		File index = new File(folderPath);
		if(index.exists()) {
			String[]entries = index.list();
			for(String s: entries){
			    File currentFile = new File(index.getPath(),s);
			    currentFile.delete();
			}
			index.delete();
		}		
	}
	
	/**
	 * Read the outlook mail and save the content in file
	 * @param userName
	 * @param pswd
	 * @param sender
	 * @param subject
	 * @param fileName
	 * @param dir
	 */
	public void readMailStoreAsHtml(String userName, String pswd, String sender, String subject, String fileName, String dir) {
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
        props.put("mail.imap-mail.outlook.com.ssl.enable", "true");
        props.put("mail.pop3.host", "outlook.com");
        props.put("mail.pop3.port", "995");
        props.put("mail.pop3.starttls.enable", "true");
        try {
            Session session = Session.getInstance(props, null);
            Store store = session.getStore();
            store.connect("imap-mail.outlook.com", userName, pswd);
            session.setDebug(true);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            SearchTerm oSender = new FromTerm(new InternetAddress(sender));
            Message[] messages = inbox.search(oSender);
            for (int i = 0 ; i < messages.length ; i++) {
            	if(messages[i].getSubject() != null) {
            		if(messages[i].getSubject().contains(subject)) {
            			createFile(fileName, dir, messages[i].getContent().toString());
            		}
            	}              	
            }
            store.close();

        } catch (Exception mex) {
            mex.printStackTrace();
        }
		
	}
	
	/**
	 * Launch the file under particular directory in browser
	 * @param fileName
	 * @param dir
	 */
	public void launchFile(String fileName, String dir) {
		driver.get(System.getProperty("user.dir") +"/" + dir + "/" + fileName);
	}

}
