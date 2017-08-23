package com.example.webcrawler.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.webcrawler.models.Link;
import com.example.webcrawler.models.LinkType;

@Service
public class QueueProcessingService {

    private static final Logger LOG = LoggerFactory.getLogger(QueueProcessingService.class);

	PageProcessingService pageProcessingService;

	SitemapWriterService sitemapWriterService;
	
	@Autowired
	public QueueProcessingService(PageProcessingService pageProcessingService, 
			SitemapWriterService sitemapWriterService) {
		
		this.pageProcessingService = pageProcessingService;

		this.sitemapWriterService = sitemapWriterService;
	}
	
	public void processQueue(String url) {
	
		LOG.debug("run queue with base url {}", url);
				
		Link rootLink = pageProcessingService.processUrl(url, LinkType.HREF, 0);
		
		HashSet<String> visitedUrls = new HashSet<>(); 
		
		Stack<Link> linkQueue = new Stack<>();
		linkQueue.add(rootLink);
		
		while(!linkQueue.isEmpty()) {
			Link link = linkQueue.pop();

			LOG.trace("link {}, isvisited {}", link, visitedUrls.contains(link.getAttemptedUrl()));
			
			if (link.isHref() && 
				link.getUrl().isPresent() && 
				link.isInternalLink(rootLink) &&
				!visitedUrls.contains(link.getAttemptedUrl())) {
				
				visitedUrls.add(link.getAttemptedUrl());
				
				try {
					
					List<Link> links = pageProcessingService.processLink(link);
					
					sitemapWriterService.writer(links);
					
					linkQueue.addAll(links);
					
				} catch (Exception e) {
					LOG.error("problem parsing link", e);
					link.setError(Optional.of(e));					
					sitemapWriterService.write(link);
				}
			}
		}
	}
}
