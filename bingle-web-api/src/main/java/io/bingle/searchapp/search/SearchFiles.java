package io.bingle.searchapp.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;

import io.bingle.searchapp.index.IndexTable;
import io.bingle.searchapp.upload.LucenUtility;

public class SearchFiles implements SearchEngine{
    private LucenUtility lucene = new LucenUtility();

    public SearchFiles(){
    }

    public ArrayList<ResultEntry> searchFile(String query, String option) throws IOException, ParseException{
        ArrayList<ResultEntry> result = new ArrayList<ResultEntry>();

        if(option.equals("All")) {

          result = lucene.searchAllFields(query);
        }
        else if(option.equals("Content")) {
          result = lucene.searchFileByContents(query);

        } else if (option.equals("Title")) {

          result = lucene.searchFileByName(query);
        } else if (option.equals("Tag")) {

          result = lucene.searchFileByTags(query);
        }
        return result;
    }

	public void setLucene(LucenUtility lucene) {
		this.lucene = lucene;
	}

	public ArrayList<ResultEntry> searchByName(String query) throws ParseException, IOException {
		return lucene.searchFileByName(query);
	}


	public ArrayList<ResultEntry> searchByContents(String query) throws ParseException, IOException {
		return lucene.searchFileByContents(query);
	}
}