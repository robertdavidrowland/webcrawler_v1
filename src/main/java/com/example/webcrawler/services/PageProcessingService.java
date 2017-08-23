package com.example.webcrawler.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.webcrawler.models.Link;
import com.example.webcrawler.models.LinkType;

@Service
public class PageProcessingService {
	
	private static final Logger LOG = LoggerFactory.getLogger(PageProcessingService.class);

	public List<Link> processLink(Link link) throws IOException {

		LOG.debug("processLink link {} {}", link);

		if (link.getUrl().isPresent()) {
			URL url = link.getUrl().get();
			
			String baseUrl = link.getUrl().get().getHost();

			Document doc = Jsoup.connect(url.toString()).get();
			doc.setBaseUri(baseUrl);
			
			return processDoc(doc, link.getDepth());
		}
		else {
			List<Link> noLinks = new ArrayList<>();
			return noLinks;
		}
	}

	public List<Link> processPage(String baseUrl, String content) {
		
		LOG.debug("processPage baseUrl {} content {}", baseUrl, content);
		
		List<Link> links = new ArrayList<>();

		Document doc = Jsoup.parse(content);
		doc.setBaseUri(baseUrl);
		
		return processDoc(doc, 0);
	}

	public List<Link> processDoc(Document doc, Integer depth) {
				
		List<Link> links = new ArrayList<>();

		Elements linkElements = doc.select("a,img");
		
		for (Element element : linkElements) {
			switch (element.nodeName()){
				case "a" : 
					Link hrefLink = processUrl(element.attr("abs:href"), LinkType.HREF, depth + 1);
					links.add(hrefLink);
					break;
				case "img" :
					Link imageLink = processUrl(element.attr("abs:src"), LinkType.IMAGE, depth + 1);
					links.add(imageLink);
					break;
			}
		}
		
		return links;
	}
	
	public Link processUrl(String url, LinkType linkType, Integer depth) {
		try {
			return new Link(url, new URL(url), linkType, depth);
		} catch (MalformedURLException e) {
			return new Link(url, e, linkType, depth);
		}
	}
}
