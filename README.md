# Webcrawler v1

## Overview

The crawler is limited to one domain. Given a starting URL it should visit all pages within the domain, but not follow the links to external sites such as Google or Twitter.

The intent is for output to be a simple site map, showing links to other pages under the same domain, links to static content such as images, and to external URLs.

This was a quick stab at this task, put together over a morning. On consideration the solution would be better reworked using the page rather than the link as 
the significant object.  This will allow the data returned to better represent relationships between pages which would be a better representation for a sitemap.

The solution is not multi-threaded which would be desirable for webcrawler.

The solution is not throttled which would be desirable for webcrawler.

The solution ignores robots.txt files which would also be desirable for webcrawler.

## Implementation details

This has been set up as Spring Boot project using Java 8.  The application is built with Maven.

## Example

```
> mvn package
> java -jar target\webcrawler-0.0.1-SNAPSHOT.jar http://www.example.com/
* http://www.example.com/
** http://www.example.com/childpage1/
** http://www.example.com/childpage2/
** http://www.example.com/childpage3/
..
..
```

