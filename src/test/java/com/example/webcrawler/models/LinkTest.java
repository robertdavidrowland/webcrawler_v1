package com.example.webcrawler.models;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

public class LinkTest {

	URL rootUrl;
	
	Link rootLink;
	
	@Before
	public void setup() throws MalformedURLException {

		String attemptedUrl = "http://www.example.com";
		rootUrl = new URL(attemptedUrl);
		rootLink = new Link(attemptedUrl, rootUrl, LinkType.HREF, 0);	
	}
	
	@Test
	public void linkIsInternalLink() throws MalformedURLException {
		
		String attemptedUrl = "http://www.example.com/this/is/a/link";
		URL url = new URL(attemptedUrl);
		Link link = new Link(attemptedUrl, url, LinkType.HREF, 0);
		
		boolean isInternal = link.isInternalLink(rootLink);

		assertEquals(true, isInternal);
	}
	
	@Test
	public void linkIsExternalLink() throws MalformedURLException {
		
		String attemptedUrl = "http://www.anotherexample.com/this/is/a/link";
		URL url = new URL(attemptedUrl);
		Link link = new Link(attemptedUrl, url, LinkType.HREF, 0);
		
		boolean isInternal = link.isInternalLink(rootLink);
		
		assertEquals(false, isInternal);
	}	
}
