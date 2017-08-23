package com.example.webcrawler.models;

import java.net.URL;
import java.util.Optional;

public class Link {

	private String attemptedUrl;
	private Optional<URL> url;
	private Optional<Exception> error;
	private LinkType linkType;
	private Integer depth;
	
	public Link(String attemptedUrl, URL url, LinkType linkType, Integer depth) {
		this.attemptedUrl = attemptedUrl;
		this.url = Optional.of(url);
		this.error = Optional.empty();
		this.linkType = linkType;
		this.depth = depth;
	}

	public Link(String attemptedUrl, Exception error, LinkType linkType, Integer depth) {
		this.attemptedUrl = attemptedUrl;
		this.url = Optional.empty();
		this.error = Optional.of(error);
		this.linkType = linkType;
		this.depth = depth;
	}

	public Optional<URL> getUrl() {
		return url;
	}

	public void setUrl(Optional<URL> url) {
		this.url = url;
	}

	public Optional<Exception> getError() {
		return error;
	}

	public void setError(Optional<Exception> error) {
		this.error = error;
	}

	public LinkType getLinkType() {
		return linkType;
	}

	public void setLinkType(LinkType linkType) {
		this.linkType = linkType;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public String getAttemptedUrl() {
		return attemptedUrl;
	}

	public void setAttemptedUrl(String attemptedUrl) {
		this.attemptedUrl = attemptedUrl;
	}

	public boolean isHref() {
		if (this.linkType == LinkType.HREF) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {

		String indent = "";
		for(int i = 0;i<depth;i++) {
			indent += "*";
		}
		
		if (getUrl().isPresent()) {
			return indent + " " + getUrl().get().toString();
		}
		else if (getError().isPresent()) {
			return indent + " " + attemptedUrl + " " + getError().get().getMessage();
		}
		else {
			return "uninitialised link object!";
		}
	}

	public boolean isInternalLink(Link rootLink) {

		if (getUrl().isPresent() && rootLink.getUrl().isPresent()) {
			String thisUrlString = getUrl().get().toString();
			String rootUrlString = rootLink.getUrl().get().toString();
			
			if (thisUrlString.contains(rootUrlString)) {
				return true;
			}
		}
		
		return false;
	}
}
