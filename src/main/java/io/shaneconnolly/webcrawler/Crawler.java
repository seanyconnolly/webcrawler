package io.shaneconnolly.webcrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

@Component
public class Crawler {

    private final Logger logger = LoggerFactory.getLogger(Crawler.class);

    private ArrayList<String> visitedLinks;
    private ArrayList<String> unVisitedLinks;
    private static final int MAX_PAGES_TO_LOAD = 2000;
    private String hostURL;
    private static final String FILE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|pdf))$)";
    private static final String SITEMAP_FILE = "./sitemap.txt";
    private Pattern pattern;

    private static final String AGENT = "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6";
    private static final String REFERER = "http://www.google.com";

    public Crawler() {
        this.visitedLinks = new ArrayList<>();
        this.unVisitedLinks = new ArrayList<>();
        this.pattern = Pattern.compile(FILE_PATTERN);

    }

    void crawl(String hostUrl) {
        logger.info("Crawling :::>  :" + hostUrl);
        cleanSiteMap();

        this.hostURL = hostUrl;
        this.unVisitedLinks.add(hostUrl);

        while (this.unVisitedLinks.size() > 0 && this.visitedLinks.size() < MAX_PAGES_TO_LOAD) {
            String url = this.unVisitedLinks.remove(0);

            if (!this.visitedLinks.contains(url)) {
                this.visitedLinks.add(url);
                try {
                    writeSiteMap(this.visitedLinks.size() + " : " + url);

                    Document document = Jsoup.connect(url)
                            .userAgent(AGENT)
                            .referrer(REFERER)
                            .get();

                    Elements linksOnPage = document.select("a[href]");

                    for (Element page : linksOnPage) {
                        String pageLink = page.attr("abs:href");

                        if (shouldCrawlLink(pageLink)) {
                            this.unVisitedLinks.add(pageLink);
                        }
                        writeSiteMap("\t\t" + pageLink);
                    }
                } catch (IOException e) {
                    logger.error("ERROR FOR'" + url + "': " + e.getMessage());
                }
            }
        }
        report();
    }


    private boolean shouldCrawlLink(String link) {
        boolean ans = false;
        if (!this.unVisitedLinks.contains(link) &&
                !this.visitedLinks.contains(link) &&
                link.startsWith(this.hostURL) &&            // exclude external links
                !link.equals(this.hostURL + "/") &&
                !this.pattern.matcher(link).matches() &&    // avoid resources, img pdfs etc
                !link.equals("/")) {
            ans = true;
        }
        return ans;
    }


    private void writeSiteMap(String url) throws IOException {
        FileWriter fw = new FileWriter("./sitemap.txt", true);
        fw.write(url + "\n");
        fw.close();
    }


    private boolean checkIfVisitedIsInUnvisited() {
        int unVisitedSize = this.unVisitedLinks.size();
        this.unVisitedLinks.removeAll(this.visitedLinks);
        return unVisitedSize != this.unVisitedLinks.size();

    }

    private void cleanSiteMap() {
        try {
            if (Files.deleteIfExists(Paths.get(SITEMAP_FILE))) {
                logger.info("file deleted ok " + SITEMAP_FILE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void report() {
        if (this.unVisitedLinks.size() == 0) {
            logger.info("*************** SUCCESS *********************");
            logger.info("All links visited, Total : " + this.visitedLinks.size());


        }


        if (checkIfVisitedIsInUnvisited()) {
            logger.warn("Unvisited has Visited links!");
        }

        logger.info("SITEMAP available @ " + SITEMAP_FILE);
        logger.info("*************** xxxxxxx *********************");
    }

}
