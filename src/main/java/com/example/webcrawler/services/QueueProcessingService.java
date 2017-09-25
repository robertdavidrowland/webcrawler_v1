package com.example.webcrawler.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.webcrawler.models.Link;
import com.example.webcrawler.models.LinkType;
import com.example.webcrawler.models.Page;

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
	
		LOG.debug("run async queue with base url {}", url);
				
		Link rootLink = pageProcessingService.processUrl(url, LinkType.HREF, 0);
		
		List<Page> pages = new ArrayList<>();
		
		HashSet<String> visitedUrls = new HashSet<>(); 
		
		Stack<Link> linkQueue = new Stack<>();
		linkQueue.add(rootLink);

		List<Future<List<Link>>> ayncLinklist = new ArrayList<>();
		
		while(!linkQueue.isEmpty()) {
			Link link = linkQueue.pop();

			LOG.trace("link {}, isHref {}, isPresent {}, isInternal {}, isvisited {}", link, link.isHref(), link.getUrl().isPresent(), link.isInternalLink(rootLink), visitedUrls.contains(link.getAttemptedUrl()));
			
			if (link.isHref() && 
				link.getUrl().isPresent() && 
				link.isInternalLink(rootLink) &&
				!visitedUrls.contains(link.getAttemptedUrl())) {
				
				visitedUrls.add(link.getAttemptedUrl());
				
				try {
					LOG.trace("process link {}", link);
					Future<List<Link>> futureLinks = pageProcessingService.processLink(link);
					ayncLinklist.add(futureLinks);
					
				} catch (Exception e) {
					LOG.error("problem parsing link", e);
					link.setError(Optional.of(e));					
				}
			}
		
			if (linkQueue.isEmpty() && !ayncLinklist.isEmpty()) {
				// linkQueue is empty but ayncLinklist is not, so must wait until something comes back from ayncLinklist
				boolean somethingIsDone = false;
				while(!somethingIsDone) {
					for(Future<List<Link>> future: ayncLinklist) {
						if (future.isDone()) {
							somethingIsDone = true;
						}
					}
				}
			}

				
			// check our async calls
			Iterator<Future<List<Link>>> ayncLinklistIterator = ayncLinklist.iterator();
			while (ayncLinklistIterator.hasNext()) {
				Future<List<Link>> future = ayncLinklistIterator.next();

				LOG.trace("future {} {}", future.isDone(), future.isCancelled());

				if (future.isDone()) {
					try {
						List<Link> links = future.get();
						pages.add(new Page(link, links));
						linkQueue.addAll(links);
						ayncLinklistIterator.remove();
					} catch (CancellationException | InterruptedException | ExecutionException e) {
						LOG.error("something wrong with async call: ", e.getMessage());
					}
				}
				else if (future.isCancelled()) {
					LOG.error("something wrong with async call: call was cancelled");					
					ayncLinklistIterator.remove();
				}
			}
			
		}
		
		for(Page page: pages) {
			if (page.isPrinted() == false) {
				sitemapWriterService.write(page);
				page.setPrinted(true);
			}
		}
	}
}
