package example;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;
import org.testng.Assert;

import utility.SeleniumUtility;

/**
 * 
 * @author LearnNclear <www.learnnclear.com>
 *
 */
public class PDFReader {
	
	public static void main(String[] args) throws IOException, ParserConfigurationException {
		SeleniumUtility util = new SeleniumUtility();
		/**
		 * 
		 * Verify the text "Page Object Model"  present at 2nd pages of pdf hosted in download link
		 * Get the download link URL and use the same in verifyPdfContentFromURL
		 * 
		 * Note: if your system configure with proxy, uncomment the proxy details property and enter appropriate proxy in method verifyPdfContentFromURL
		 * 
		 */
		Assert.assertTrue(util.verifyPdfContentFromURL("http://www.learnnclear.com/LearnNClear-syllabus.pdf", 2, "Page Object Model"),
				"'Page Object Model' text not present");
		
		System.out.println("Verify the text 'Page Object Model' in pdf");		
		
		/**
		 * 
		 * Use to convert the PDF to HTML and save it in local folder
		 * Then use driver.get method to launch and verify your content using driver method
		 * 
		 * Note: if your system configure with proxy, uncomment the proxy details property and enter appropriate proxy in method verifyPdfContentFromURL
		 * 
		 */
		
		
		util.pdftoHtmlfromUrl("http://www.learnnclear.com/LearnNClear-syllabus.pdf","LearnNClear-syllabus.html");
		
	}
}
