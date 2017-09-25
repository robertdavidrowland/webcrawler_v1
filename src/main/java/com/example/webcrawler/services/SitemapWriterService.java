package com.example.webcrawler.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.webcrawler.models.Link;
import com.example.webcrawler.models.Page;

@Service
public class SitemapWriterService {

	public void write(List<Link> links) {
		links.stream().forEach(l -> write(l));
	}

	public void write(Link l) {
		System.out.println(l.toString());
	}
	
	public void write(Page p) {
		System.out.println(p.toString());
	}
}
