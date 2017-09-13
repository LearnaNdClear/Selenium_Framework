package example;


import org.testng.Assert;

import utility.SeleniumUtility;

/**
 * 
 * @author LearnNclear <www.learnnclear.com>
 *
 */
public class PDFReader {
	
	public static void main(String[] args) {
		SeleniumUtility util = new SeleniumUtility();
		/**
		 * 
		 * Verify the text "Page Object Model"  present at 2 pages of pdf hosted in download link
		 * Get the download link URL and use the same in verifyPdfContentFromURL
		 * 
		 * Note: if your system configure with proxy, uncomment the proxy details property and enter appropriate proxy in method verifyPdfContentFromURL
		 * 
		 */
		Assert.assertTrue(util.verifyPdfContentFromURL("http://www.learnnclear.com/LearnNClear-syllabus.pdf", 2, "Page Object Model"),
				"'Page Object Model' text not present");
		
		System.out.println("Verify the text 'Page Object Model' in pdf");
	}
}
