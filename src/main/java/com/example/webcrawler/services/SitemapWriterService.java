package com.example.webcrawler.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.webcrawler.models.Link;

@Service
public class SitemapWriterService {

	public void writer(List<Link> links) {
		links.stream().forEach(l -> write(l));
	}

	public void write(Link l) {
		System.out.println(l.toString());
	}
	
}
