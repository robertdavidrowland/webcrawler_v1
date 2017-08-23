package com.example.webcrawler.services;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.example.webcrawler.models.Link;

public class PageProcessingServiceTest {

	PageProcessingService pageProcessingService;
	
	String baseUrl;
	
	@Before
	public void setup() {
		pageProcessingService = new PageProcessingService();
		baseUrl = "http://www.internal.com";
	}
	
	@Test
	public void processPageWithNoLinks() throws IOException {
		
		File testPage = new File("src/test/data/test_page_no_links.html");
		String testContent = new String(Files.readAllBytes(testPage.toPath()));
		
		List<Link> links = pageProcessingService.processPage(baseUrl, testContent);
		
		assertEquals(0, links.size());
	}
	
	@Test
	public void processPageWithHrefs() throws IOException {
		
		File testPage = new File("src/test/data/test_page_with_hrefs.html");
		String testContent = new String(Files.readAllBytes(testPage.toPath()));
		
		List<Link> links = pageProcessingService.processPage(baseUrl, testContent);

		assertEquals(2, links.size());
	}

	@Test
	public void processPageWithImagesAndHrefs() throws IOException {
		
		File testPage = new File("src/test/data/test_page_with_images_and_hrefs.html");
		String testContent = new String(Files.readAllBytes(testPage.toPath()));
		
		List<Link> links = pageProcessingService.processPage(baseUrl, testContent);

		assertEquals(4, links.size());
	}
}
