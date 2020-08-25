package io.bingle.searchapp.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.queryparser.classic.ParseException;

import io.bingle.searchapp.upload.LucenUtility;

public class SearchListing implements SearchEngine{
	private LucenUtility lucene = new LucenUtility();
	
	public ArrayList<String> searchListings(String query) throws ParseException, IOException{
		ArrayList<String> retList = new ArrayList<String>();
        ArrayList<String> fileNames = lucene.searchListingByContents(query);
        for (String filename : fileNames){
            File currFile = new File("LocalStorage//" + filename);
            if (currFile.exists() && currFile.isFile()){
                retList.add(currFile.getName());
            }
        }

        fileNames = lucene.searchListingByTitle(query);
        for (String filename : fileNames){
            File currFile = new File("LocalStorage//" + filename);
            if (currFile.exists() && currFile.isFile()){
                retList.add(currFile.getName());
            }
        }
        return retList;
	}
}
