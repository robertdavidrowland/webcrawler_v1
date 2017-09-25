package com.example.webcrawler.models;

import java.util.List;

public class Page {

	private Link page;
	private List<Link> links;
	private boolean isPrinted;
	
	public Page() {
	}
	
	public Page(Link page, List<Link> links) {
		this.page = page;
		this.links = links;
	}

	public Link getPage() {
		return page;
	}

	public void setPage(Link page) {
		this.page = page;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public boolean isPrinted() {
		return isPrinted;
	}

	public void setPrinted(boolean isPrinted) {
		this.isPrinted = isPrinted;
	}
	
	@Override
	public String toString() {
		String str = "Page: " + page.toString() + "\n";
//		str += "Links: " + links.stream().map(Link::toString).collect(Collectors.joining("\n"));
//		str += "\n";
		
		return str;
	}
}
