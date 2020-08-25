package io.bingle.searchapp.upload;

import io.bingle.searchapp.search.ResultEntry;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.document.Field;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import io.bingle.searchapp.UserService;
import io.bingle.searchapp.account.Account;
import io.bingle.searchapp.discussion.Post;
import io.bingle.searchapp.discussion.Thread;
import io.bingle.searchapp.marketplace.Listing;

public class LucenUtility {

	private static StandardAnalyzer analyzer = new StandardAnalyzer();
	private static String indexPath = "Index";
	private static String indexThreadPath = "IndexThreads";
	private static String indexListingPath = "IndexListings";

	private static Directory indexDir = null;
	private static Directory indexThreadDir = null;
	private static Directory indexListingDir = null;

	public LucenUtility() {

		try {
			new File(indexPath).mkdirs();
			indexDir = FSDirectory.open(Paths.get(indexPath));

			new File(indexThreadPath).mkdirs();
			indexThreadDir = FSDirectory.open(Paths.get(indexThreadPath));

			new File(indexListingPath).mkdirs();
			indexListingDir = FSDirectory.open(Paths.get(indexListingPath));

		} catch (IOException e) {

			System.out.println("Error creating Index directory");
		}
	}

	public void indexFile(File file, String id, String tags, String filename, String username) {

		try {
			IndexWriterConfig config = new IndexWriterConfig(analyzer);

			IndexWriter w = new IndexWriter(indexDir, config);
			addDoc(w, file, tags, id, filename, username);
			w.close();

		} catch (IOException e) {

			System.err.println(e);
		}
	}

	public void indexImage(File file, String id, String tags, String filename, String username) {

		try {
			IndexWriterConfig config = new IndexWriterConfig(analyzer);

			IndexWriter w = new IndexWriter(indexDir, config);
			Document doc = new Document();
			try {
				doc.add(new TextField("filename", filename, Field.Store.YES));
				doc.add(new TextField("title", file.getName(), Field.Store.YES));
				doc.add(new TextField("id", id, Store.YES));
				doc.add(new TextField("username", username, Store.YES));
				doc.add(new TextField("tags", tags, Store.YES));

				// if(FilenameUtils.getExtension(file.getName()).equals("pdf")) {
				//
				// //System.out.println("content " + convertPDFtoText(file));
				//
				// doc.add(new TextField("content", convertPDFtoText(file), Field.Store.YES));
				//
				// } else {
				// doc.add(new TextField("content", new BufferedReader(new FileReader(file))));
				// }

				// System.out.println("file name" + file.getName());
				w.addDocument(doc);
				w.close();
			} catch (IOException e) {

				System.out.println("error: can't index");
			}
			w.close();

		} catch (IOException e) {

			System.err.println(e);
		}
	}

	 public void indexThread(Thread thread, Boolean update)
	  {
	    FileManagementSystem fs = new FileManagementSystem();
	    Path path = null;

	    PrintWriter writer = null;
	    try {
	      File file = new File("LocalStorage//" + thread.getTitle());
	      if (update)
	      {
	        path =  fs.localStorage(thread.getTitle());
	        file.delete();
	      }
	      else if (file.exists())
	      {
	        String newPath = thread.getTitle() + "_1";
	        path =  fs.localStorage(newPath);
	      }
	      else {
	        path =  fs.localStorage(thread.getTitle());
	      }
	      writer = new PrintWriter(path.toString(), "UTF-8");

	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
	      e.printStackTrace();
	    }
	    writer.append(thread.getTitle() + "\n");
	    writer.append(thread.getId() + "\n");
	    writer.append(thread.getName() + "\n");
	    writer.append(" \n");
	    for (String tag : thread.getTags())
	    {
	    	writer.append(tag + " ");
	    }
	    writer.append("\n");
	    writer.append(thread.getContent() + "\n");
	    
	    writer.flush();
	    writer.close();

	    
	    try {
	        IndexWriterConfig config = new IndexWriterConfig(analyzer);

	        IndexWriter w = new IndexWriter(this.indexThreadDir, config);
	        addDoc(w, new File(path.toString()), "", thread.getId());
	        w.close();

	      } catch(IOException e) {

	        System.err.println(e);
	      }
	    
	    //this.indexFile(new File(path.toString()), thread.getId());
	  }

	  public void indexThreadAndPosts(Thread thread, List<Post> list)
	  {
	    Path path =  new FileManagementSystem().localStorage(thread.getTitle());

	    PrintWriter writer = null;
	    try {
	      File file = new File(path.toString());
	      file.delete();
	      deleteThreadIndex(thread);
	      writer = new PrintWriter(path.toString(), "UTF-8");

	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
	      e.printStackTrace();
	    }
	    writer.append(thread.getTitle() + "\n");
	    writer.append(thread.getId() + "\n");
	    writer.append(thread.getName() + "\n");
	    writer.append(" \n");
	    for (String tag : thread.getTags())
	    {
	    	writer.append(tag + " ");
	    }
	    writer.append("\n");
	    
	    writer.append(thread.getContent() + "\n");
	    for (Post p : list)
	    {
	      writer.append(p.getContent() + "\n");
	    }
	    writer.flush();
	    writer.close();

	    try {
	        IndexWriterConfig config = new IndexWriterConfig(analyzer);

	        IndexWriter w = new IndexWriter(this.indexThreadDir, config);
	        addDoc(w, new File(path.toString()), "", thread.getId());
	        w.close();

	      } catch(IOException e) {

	        System.err.println(e);
	      }
	    
	    //this.indexFile(new File(path.toString()), thread.getId());
	  }

	public static void addDoc(IndexWriter w, File file, String tags, String id) {
		Document doc = new Document();
		try {
			doc.add(new TextField("title", file.getName(), Field.Store.YES));
			doc.add(new TextField("id", id, Store.YES));
			doc.add(new TextField("tags", tags, Store.YES));
			if (FilenameUtils.getExtension(file.getName()).equals("pdf")) {

				// System.out.println("content " + convertPDFtoText(file));

				doc.add(new TextField("content", convertPDFtoText(file), Field.Store.YES));

			} else {

				BufferedReader reader = new BufferedReader(new FileReader(file));
				String st;
				String content = "";
				while ((st = reader.readLine()) != null) {
					content = content + st;
				}

				doc.add(new TextField("content", content, Field.Store.YES));
			}

			// System.out.println("file name" + file.getName());
			w.addDocument(doc);
			w.close();
		} catch (IOException e) {

			System.out.println("error: can't index");
		}
	}

	public static void addDoc(IndexWriter w, File file, String tags, String id, String filename, String username) {
		Document doc = new Document();
		try {
			doc.add(new TextField("filename", filename, Field.Store.YES));
			doc.add(new TextField("title", file.getName(), Field.Store.YES));
			doc.add(new TextField("username", username, Store.YES));
			doc.add(new TextField("tags", tags, Store.YES));

			if (FilenameUtils.getExtension(file.getName()).equals("pdf")) {

				// System.out.println("content " + convertPDFtoText(file));

				doc.add(new TextField("content", convertPDFtoText(file), Field.Store.YES));

			} else {

				BufferedReader reader = new BufferedReader(new FileReader(file));
				String st;
				String content = "";
				while ((st = reader.readLine()) != null) {
					content = content + st;
				}

				doc.add(new TextField("content", content, Field.Store.YES));

			}

			// System.out.println("file name" + file.getName());
			w.addDocument(doc);
			w.close();
		} catch (IOException e) {

			System.out.println("error: can't index");
		}
	}

	public String searchTest(String term) {

		try {

			/*
			 * 1. Query object, created that encapusulates the user query
			 *
			 * 2. IndexReader that allows you to read the index.
			 *
			 * 3. IndexSearcher that allows you to take the query and search the index.
			 */
			Query q = new QueryParser("content", analyzer).parse(term);

			// MultiFieldQueryParser q = new MultiFieldQueryParser(Version.LUCENE_35, new
			// String[]{"title", "isbn"}, analyzer).parse(querystr);
			int hitsPerPage = 10000;
			IndexReader reader = DirectoryReader.open(indexDir);
			IndexSearcher searcher = new IndexSearcher(reader);

			TopDocs docs = searcher.search(q, hitsPerPage);

			ScoreDoc[] hits = docs.scoreDocs;
			StringBuilder responseBackToUser = new StringBuilder();
			responseBackToUser.append("Found " + hits.length + " hits.");

			for (int i = 0; i < hits.length; ++i) {

				int docId = hits[i].doc;
				Document d = searcher.doc(docId);

				String[] frags = highlightMatched(q, reader, docId, d.get("content"));
				for (String frag : frags) {
					System.out.println("=======================");
					System.out.println(frag);
				}

				System.out.println((i + 1) + " " + d.get("id") + " " + d.get("title"));

				return d.get("id");
			}
			reader.close();
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}

		return "";

	}

	public ArrayList<String> searchThreadByName(String query) throws ParseException, IOException {

		ArrayList<Integer> resultIds = new ArrayList<Integer>();

		Query q = new QueryParser("title", analyzer).parse(query);
		int hitsPerPage = 10000;
		IndexReader reader = DirectoryReader.open(this.indexThreadDir);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopDocs docs = searcher.search(q, hitsPerPage);
		ScoreDoc[] hits = docs.scoreDocs;

		for (int i = 0; i < hits.length; ++i) {
			resultIds.add(hits[i].doc);
		}
		ArrayList<String> results = new ArrayList<String>();
		for (int i : resultIds) {
			Document d = searcher.doc(i);
			results.add(d.get("title"));
		}

		reader.close();
		return results;

	}

	public ArrayList<String> searchListingByTitle(String query) throws ParseException, IOException {
		ArrayList<Integer> retIds = new ArrayList<>();

		Query q = new QueryParser("title", analyzer).parse(query);
		int hitsPerPage = 10000;
		IndexReader reader = DirectoryReader.open(this.indexListingDir);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopDocs docs = searcher.search(q, hitsPerPage);
		ScoreDoc[] hits = docs.scoreDocs;

		for (int i = 0; i < hits.length; ++i) {
			retIds.add(hits[i].doc);
		}
		ArrayList<String> results = new ArrayList<String>();
		for (int i : retIds) {
			Document d = searcher.doc(i);
			results.add(d.get("title"));
		}

		reader.close();

		return results;
	}

	public ArrayList<String> searchListingByContents(String query) throws ParseException, IOException {
		ArrayList<Integer> resultIds = new ArrayList<Integer>();

		Query q = new QueryParser("content", analyzer).parse(query);
		int hitsPerPage = 10000;
		IndexReader reader = DirectoryReader.open(this.indexListingDir);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopDocs docs = searcher.search(q, hitsPerPage);
		ScoreDoc[] hits = docs.scoreDocs;

		for (int i = 0; i < hits.length; ++i) {
			resultIds.add(hits[i].doc);
		}
		ArrayList<String> results = new ArrayList<String>();
		for (int i : resultIds) {
			Document d = searcher.doc(i);
			results.add(d.get("title"));
		}
		reader.close();
		return results;

	}

	public ArrayList<String> searchThreadByContents(String query) throws ParseException, IOException {

		ArrayList<Integer> resultIds = new ArrayList<Integer>();

		Query q = new QueryParser("content", analyzer).parse(query);
		int hitsPerPage = 10000;
		IndexReader reader = DirectoryReader.open(this.indexThreadDir);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopDocs docs = searcher.search(q, hitsPerPage);
		ScoreDoc[] hits = docs.scoreDocs;

		for (int i = 0; i < hits.length; ++i) {
			resultIds.add(hits[i].doc);
		}
		ArrayList<String> results = new ArrayList<String>();
		for (int i : resultIds) {
			Document d = searcher.doc(i);
			results.add(d.get("title"));
		}
		reader.close();
		return results;

	}

	public static String convertPDFtoText(File file) {

		try {
			BodyContentHandler handler = new BodyContentHandler();
			Metadata metadata = new Metadata();
			ParseContext parseContext = new ParseContext();
			FileInputStream inputStream = new FileInputStream(file);

			PDFParser pdfparser = new PDFParser();
			pdfparser.parse(inputStream, handler, metadata, parseContext);

			return handler.toString();

		} catch (IOException | TikaException | SAXException e) {

			System.out.print("error during pdf conversion");

			return null;
		}

	}

	public void deleteThreadIndex(Thread thread) {
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter w = null;
		try {
			w = new IndexWriter(this.indexThreadDir, config);
			Document doc = new Document();
			// Term term = new Term("title", thread.getTitle());
			Query query = new QueryParser("content", analyzer).parse(thread.getTitle());
			w.deleteDocuments(query);
			// w.deleteDocuments(term);
			w.flush();
			w.close();

			File threadFile = new File("LocalStorage//" + thread.getTitle());
			threadFile.delete();

		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void deleteListingIndex(Listing listing) {
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter w = null;
		try {
			w = new IndexWriter(this.indexListingDir, config);
			Document doc = new Document();
			Query query = new MultiFieldQueryParser(new String[] { "content", "tags", "filename", "username" },
					analyzer).parse(listing.getTitle());
			w.deleteDocuments(query);
			w.flush();
			w.close();
		} catch (IOException | ParseException e) {
			System.err.print("Error deleting Listing file");
		}
	}

	public ArrayList<ResultEntry> searchFileByName(String query) throws ParseException, IOException {

		Query q = new QueryParser("filename", analyzer).parse(query);
		int hitsPerPage = 10000;
		IndexReader reader = DirectoryReader.open(indexDir);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopDocs docs = searcher.search(q, hitsPerPage);
		ScoreDoc[] hits = docs.scoreDocs;

		ArrayList<ResultEntry> result = generateResultEntryList(hits, reader, searcher, q);
		reader.close();
		return result;

	}

	public ArrayList<ResultEntry> searchFileByContents(String query) throws ParseException, IOException {

		ArrayList<ResultEntry> results = new ArrayList<ResultEntry>();

		Query q = new QueryParser("content", analyzer).parse(query);
		int hitsPerPage = 10000;
		IndexReader reader = DirectoryReader.open(indexDir);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopDocs docs = searcher.search(q, hitsPerPage);
		ScoreDoc[] hits = docs.scoreDocs;

		ArrayList<ResultEntry> result = generateResultEntryList(hits, reader, searcher, q);
		reader.close();
		return result;
	}

	public ArrayList<ResultEntry> searchFileByTags(String query) throws ParseException, IOException {

		ArrayList<ResultEntry> results = new ArrayList<ResultEntry>();

		Query q = new QueryParser("tags", analyzer).parse(query);
		int hitsPerPage = 10000;

		IndexReader reader = DirectoryReader.open(indexDir);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopDocs docs = searcher.search(q, hitsPerPage);
		ScoreDoc[] hits = docs.scoreDocs;

		ArrayList<ResultEntry> result = generateResultEntryList(hits, reader, searcher, q);
		reader.close();
		return result;
	}

	public ArrayList<ResultEntry> generateResultEntryList(ScoreDoc[] hits, IndexReader reader, IndexSearcher searcher,
			Query q) {

		ArrayList<ResultEntry> results = new ArrayList<ResultEntry>();
		try {
			for (int i = 0; i < hits.length; ++i) {
				int docId = hits[i].doc;
				Document d = searcher.doc(docId);
				String frag[] = highlightMatched(q, reader, docId, d.get("content"));
				results.add(generateResultEntry(d, frag));
			}
		} catch (IOException e) {

			System.out.println(e);
		}

		return results;

	}

	public ResultEntry generateResultEntry(Document doc, String[] fragments) {

		String path = "LocalStorage//" + doc.get("title");
		String summary = "";

		File currFile = new File(path);

		if (currFile.exists() && currFile.isFile()) {

			for (String frag : fragments) {
				summary = summary + "..." + frag;
			}

			ResultEntry entry = new ResultEntry(doc.get("filename"), path, currFile.lastModified(), summary,
					doc.get("tags"), doc.get("username"));

			return entry;

		}

		return null;
	}

	public String[] highlightMatched(Query q, IndexReader reader, int docId, String content) {
		// Initialize highlighter
		Formatter formatter = new SimpleHTMLFormatter();
		QueryScorer scorer = new QueryScorer(q);
		Highlighter highlighter = new Highlighter(formatter, scorer);

		/// break the text into smaller fragments
		Fragmenter fragmenter = new SimpleSpanFragmenter(scorer, 50);
		highlighter.setTextFragmenter(fragmenter);

		try {

			TokenStream stream = TokenSources.getAnyTokenStream(reader, docId, "content", analyzer);
			String[] frags = highlighter.getBestFragments(stream, content, 10);
			return frags;

		} catch (Exception e) {

			return new String[0];

		}
	}

	public void indexListing(Listing listing) throws IOException {
		FileManagementSystem fs = new FileManagementSystem();
		Path path = null;
		PrintWriter writer = null;
		// File newListingFile = new File(indexListingPath);
		path = fs.localStorage(listing.getTitle());
		writer = new PrintWriter(path.toString(), "UTF-8");
		writer.append(listing.id + "\n");
		writer.append(listing.getTitle() + "\n");
		writer.append(listing.getDescription() + "\n");
		writer.append(listing.getCategory() + "\n");
		// writer.append(listing.getUserId() + "\n");
		writer.append(listing.getPhoneNo() + "\n");
		writer.append(listing.getPrice() + "\n");
		if (listing.getTags() != null) {
			writer.append(listing.getTags().toString());
		}
		writer.flush();
		writer.close();

		IndexWriterConfig config = new IndexWriterConfig(analyzer);

		IndexWriter w = new IndexWriter(this.indexListingDir, config);
		addDoc(w, new File(path.toString()), "", listing.id);
		;
	}

	public void massIndexer(IndexWriter writer, File file, String id, String tags, String filename, String username) {

		Document doc = new Document();
		try {
			doc.add(new TextField("filename", filename, Field.Store.YES));
			doc.add(new TextField("title", file.getName(), Field.Store.YES));
			doc.add(new TextField("username", username, Store.YES));
			doc.add(new TextField("tags", tags, Store.YES));

			if (FilenameUtils.getExtension(file.getName()).equals("pdf")) {

				// System.out.println("content " + convertPDFtoText(file));

				doc.add(new TextField("content", convertPDFtoText(file), Field.Store.YES));

			} else if (file != null) {
				doc.add(new TextField("content", new BufferedReader(new FileReader(file))));
			}

			// System.out.println("file name" + file.getName());
			writer.addDocument(doc);
		} catch (IOException e) {

			System.out.println("error: can't index");
		}
	}

	public void massIndexerImages(IndexWriter writer, String title, String tags, String filename, String username) {

		Document doc = new Document();
		try {
			doc.add(new TextField("filename", filename, Field.Store.YES));
			doc.add(new TextField("title", title, Field.Store.YES));
			doc.add(new TextField("username", username, Store.YES));
			doc.add(new TextField("tags", tags, Store.YES));
			doc.add(new TextField("content", "", Store.YES));

			// System.out.println("file name" + file.getName());
			writer.addDocument(doc);
		} catch (IOException e) {

			System.out.println("error: can't index");
		}
	}

	public ArrayList<ResultEntry> searchByUsername(String query) throws ParseException, IOException {
		ArrayList<ResultEntry> results = new ArrayList<ResultEntry>();

		try {

			Query q = new MultiFieldQueryParser(new String[] { "username" }, analyzer).parse(query);
			int hitsPerPage = 10000;
			IndexReader reader = DirectoryReader.open(indexDir);
			IndexSearcher searcher = new IndexSearcher(reader);
			TopDocs docs = searcher.search(q, hitsPerPage);
			ScoreDoc[] hits = docs.scoreDocs;

			ArrayList<ResultEntry> result = generateResultEntryList(hits, reader, searcher, q);
			reader.close();
			return result;
		} catch (IndexNotFoundException e) {

			System.out.println("no index found");
			return results;
		}
	}

	public ArrayList<ResultEntry> searchAllFields(String query) throws ParseException, IOException {

		ArrayList<ResultEntry> results = new ArrayList<ResultEntry>();

		try {

			Query q = new MultiFieldQueryParser(new String[] { "content", "tags", "filename", "username" }, analyzer)
					.parse(query);
			int hitsPerPage = 10000;

			IndexReader reader = DirectoryReader.open(indexDir);
			IndexSearcher searcher = new IndexSearcher(reader);
			TopDocs docs = searcher.search(q, hitsPerPage);
			ScoreDoc[] hits = docs.scoreDocs;

			ArrayList<ResultEntry> result = generateResultEntryList(hits, reader, searcher, q);
			reader.close();
			return result;
		} catch (IndexNotFoundException e) {

			System.out.println("no index found");
			return results;
		}
	}
}
