package io.bingle.searchapp.Crawler;

import com.google.common.io.Files;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;
import io.bingle.searchapp.GoogleOCR;
import io.bingle.searchapp.upload.LucenUtility;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.common.io.Files;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.HtmlParser;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;

public class Crawler extends WebCrawler {

	private static File storageFolder;

	private static final Pattern FILTERS = Pattern.compile(".*(\\.(pdf|jpe?g|png|bmp|gif|doc|doc" + "|html))$");

	private static final Pattern CONTENT_FILTER = Pattern.compile(".*(\\/(pdf|jpe?g|png|bmp|gif|doc|doc" + "|html))$");

	private final static Pattern BLACKLIST = Pattern.compile(".*(\\.(css.*|js.*|mp3|mp4|zip|gz|txt|tex|"
			+ "odt|wav|wma|rar|7z|bin|iso|csv|sql.*|xml.*|tar|log|bat|jar|exe|svg))$");

	private static final Pattern PDF_FILTER = Pattern.compile(".*(\\/(pdf))$");
	public static String[] whiteList;

	private LucenUtility utility = new LucenUtility();

	private static GoogleOCR ocr = new GoogleOCR();

	private static IndexWriter w;

	private static String username;

	private static String crawl_mode = "Files";
	public Crawler() {

	}

	public static void configure(String[] domain, String storageFolderName, IndexWriter writer, String name, String mode) {

		whiteList = domain;

		storageFolder = new File("LocalStorage");
		if (!storageFolder.exists()) {
			storageFolder.mkdirs();
		}

		w = writer;
		crawl_mode = mode;
		username = name;

	}

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {

		String href = url.getURL().toLowerCase();
		

		if (crawl_mode.equals("Files"))
		{
			if (domainCheck(href)) {
	
				if (!BLACKLIST.matcher(href).matches()) {
		
					return true;
				}
	
			}
		}
		else {
			if (!BLACKLIST.matcher(href).matches()) {
				
				return true;
			}
		}

		return false;
	}

	public boolean domainCheck(String href) {

		for (String url : whiteList) {

			if (href.startsWith(url)) {
				return true;
			}
		}

		return false;
	}

	public String getContentType(String path) {

		try {
			URL url = new URL(path);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("HEAD");
			connection.connect();
			if (connection.getContentType() != null) {
				return connection.getContentType().split(";")[0];
			}

			return "";

		} catch (IOException e) {

			System.err.println(e.toString());
			return "bad-url";
		}
	}

	@Override
	public void visit(Page page) {

		logger.info("Visited: {}", page.getWebURL().getURL());
		String url = page.getWebURL().getURL();

		if (page.getContentType().equals("application/pdf") && crawl_mode.equals("Files")) {

			// get a unique name for storing this image
			String extension = url.substring(url.lastIndexOf('.'));
			String title = url.substring(url.lastIndexOf('/'));
			title = title.substring(1).replaceAll("%20", " ");

			String hashedName = UUID.randomUUID() + extension;
			// store image
			String filename = storageFolder.getAbsolutePath() + "/" + hashedName;
			try {
				Files.write(page.getContentData(), new File(filename));

				utility.massIndexer(w, new File(filename), "", "", title, username);

			} catch (IOException iox) {
				WebCrawler.logger.error("Failed to write file: " + filename, iox);
			}
		} else if (page.getContentType().split("/")[0].equals("image") && !crawl_mode.equals("Files")) {
			// get a unique name for storing this image
			String extension = url.substring(url.lastIndexOf('.'));
			String title = url.substring(url.lastIndexOf('/'));
			title = title.substring(1).replaceAll("%20", " ");

			String hashedName = UUID.randomUUID() + extension;

			String filename = storageFolder.getAbsolutePath() + "/" + hashedName;
			try {
				Files.write(page.getContentData(), new File(filename));

				ocr.upload(page.getContentData(), hashedName, page.getContentType());

				String annotations = ocr.getAnnotations(hashedName);
				Pattern p = Pattern.compile("description\": \"([a-z0-9\\s]*)\"");
				Matcher m = p.matcher(annotations);
				StringBuilder sb = new StringBuilder();

				while (m.find()) {
					sb.append(m.group(1) + ";");
				}
				System.err.println(sb.toString());
				utility.massIndexerImages(w, hashedName, sb.toString(), title, username);
			} catch (IOException iox) {
				WebCrawler.logger.error("Failed to write file: " + filename, iox);
			}

		}
	}

}
