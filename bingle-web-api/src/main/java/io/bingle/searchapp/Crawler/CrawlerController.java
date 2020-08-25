package io.bingle.searchapp.Crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import io.bingle.searchapp.account.Account;
import io.bingle.searchapp.upload.LucenUtility;
import java.io.IOException;
import java.nio.file.Paths;
import javax.servlet.http.HttpSession;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class CrawlerController {


@PostMapping("/crawl")
public void crawl(@RequestParam("url") String url, @RequestParam("depth") int depth, @RequestParam("page") int maxPage, @RequestParam("mode") String mode, HttpSession session) throws IOException {

  String crawlStorageFolder = "./storage";
  String[] seedList = url.split(";");
  LucenUtility utility = new LucenUtility();
  Account userAccount = (Account) session.getAttribute("user");

  StandardAnalyzer analyzer = new StandardAnalyzer();
  String indexPath  = "Index";
  Directory indexDir = FSDirectory.open(Paths.get(indexPath));
  IndexWriterConfig indexConfig = new IndexWriterConfig(analyzer);
  IndexWriter w = new IndexWriter(indexDir, indexConfig);
  
  Crawler.configure(seedList, "/LocalStorage", w, userAccount.getUsername(), mode);
  int numberOfCrawlers = 7;

  CrawlConfig config = new CrawlConfig();
  config.setCrawlStorageFolder(crawlStorageFolder);
  config.setPolitenessDelay(0);
  config.setMaxDepthOfCrawling(depth);
  config.setMaxPagesToFetch(maxPage);
  config.setIncludeBinaryContentInCrawling(true);
  config.setResumableCrawling(false);
  config.setMaxDownloadSize(100000000);
  PageFetcher pageFetcher = new PageFetcher(config);
  RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
  RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

  try {
    CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

    for(String seed : seedList) {
      controller.addSeed(seed);
    }
    
    session.setAttribute("crawler", true);
    controller.start(Crawler.class, numberOfCrawlers);
    session.removeAttribute("crawler");
  } catch(Exception e) {
	  System.err.println(e);
  }

  w.close();

}

@GetMapping("/crawl")
public String crawl() {

  return "crawl";
}
}
