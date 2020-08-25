package io.bingle.searchapp.search;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import io.bingle.searchapp.index.IndexTable;
import io.bingle.searchapp.upload.*;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


public interface SearchEngine {

    /**
    public ArrayList<String> searchByName(String query) throws ParseException, IOException;
    public ArrayList<String> searchByContents(String query) throws ParseException, IOException;
     **/
    
}
