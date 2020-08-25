package io.bingle.searchapp.search;

import io.bingle.searchapp.discussion.Thread;
import io.bingle.searchapp.discussion.ThreadRepository;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import java.util.Scanner;
import org.apache.lucene.queryparser.classic.ParseException;

import io.bingle.searchapp.upload.LucenUtility;
import org.springframework.beans.factory.annotation.Autowired;

public class SearchThreads implements SearchEngine{
  private LucenUtility lucene = new LucenUtility();

  @Autowired
  private ThreadRepository threadRepo;

    
    public ArrayList<String> searchThread(String query) throws IOException, ParseException{
        ArrayList<String> retList = new ArrayList<String>();
        ArrayList<String> fileNames = lucene.searchThreadByContents(query);
        for (String filename : fileNames){
            File currFile = new File("LocalStorage//" + filename);
            if (currFile.exists() && currFile.isFile()){
                retList.add(currFile.getName());
            }
        }

        fileNames = lucene.searchThreadByName(query);
        for (String filename : fileNames){
            File currFile = new File("LocalStorage//" + filename);
            if (currFile.exists() && currFile.isFile()){
                retList.add(currFile.getName());
            }
        }
        return retList;
    }


	  public ArrayList<String> searchByName(String query) throws ParseException, IOException {
		  return lucene.searchThreadByName(query);
	  }


	  public ArrayList<String> searchByContents(String query) throws ParseException, IOException {
		  return lucene.searchThreadByContents(query);
	  }

  public ArrayList<String> masterSearchThread(String query) throws IOException, ParseException{
    ArrayList<String> retList = new ArrayList<String>();
    ArrayList<Thread> result = new ArrayList<Thread>();
    ArrayList<String> fileNames = lucene.searchThreadByContents(query);
    String id = null;

    for (String filename : fileNames){
      File currFile = new File("LocalStorage//" + filename);
      if (currFile.exists() && currFile.isFile()){
        retList.add(currFile.getName());
      }
    }

    fileNames = lucene.searchThreadByName(query);
    for (String filename : fileNames){
      File currFile = new File("LocalStorage//" + filename);
      if (currFile.exists() && currFile.isFile()){
        retList.add(currFile.getName());
      }
    }

    for (String name : retList)
    {
      File tfile = new File("LocalStorage//" + name);
      Scanner input = null;
      try {
        input = new Scanner(tfile);
      } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      input.nextLine();
      id = input.nextLine();
      Thread thread = this.threadRepo.findById(id).get();
      if (thread != null)
      {
        result.add(this.threadRepo.findById(id).get());
      }
    }
    return retList;
  }

}
