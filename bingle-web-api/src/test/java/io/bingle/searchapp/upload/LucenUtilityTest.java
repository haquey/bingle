package io.bingle.searchapp.upload;

import static org.junit.Assert.assertTrue;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.nio.file.Files;

import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.bingle.searchapp.marketplace.Listing;

public class LucenUtilityTest{

    LucenUtility lucene = new LucenUtility();
    String pathToTestFiles = System.getProperty("user.dir") + "//src//test//java//io//bingle//searchapp//upload//";
    //File indexDirectory =  new File("Index");

    @Test
    public void indexedFileLocationTest() throws IOException{
        File testFile = new File(pathToTestFiles + "//testFileUpload.txt");
        lucene.indexFile(testFile, "", "csc01;sadman", "title", "tester");

        File fileRetrieved = new File("Index");
        assertTrue(fileRetrieved.exists() && fileRetrieved.isDirectory());
    }

    /**
    @Test
    public void searchFileTest() throws IOException, ParseException{
        ArrayList<String> fileNameResults = new ArrayList<String>();
        fileNameResults = lucene.searchByContents("Test");
        assertTrue(fileNameResults.contains("testFileUpload.txt"));

    }
    **/
    @Test
    public void localStorageListingTest() throws IOException{
        Listing testListing = mock(Listing.class);
        when(testListing.getTitle()).thenReturn("testTitle");
        when(testListing.getPrice()).thenReturn("123.99");
        when(testListing.getDescription()).thenReturn("testDescription");
        //when(testListing.getUserId()).thenReturn("fakeUserId");
        when(testListing.getPhoneNo()).thenReturn("1231231234");
        when(testListing.getCategory()).thenReturn("Electronics");
        testListing.id = "5";
    	LucenUtility lucene = new LucenUtility();
        lucene.indexListing(testListing);
        File expectedFile = new File("LocalStorage//" + testListing.getTitle());
        boolean exists = expectedFile.exists();
        expectedFile.delete();
        assertTrue(exists);
    }
}