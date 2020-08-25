package io.bingle.searchapp.marketplace;

import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Test;

import io.bingle.searchapp.search.SearchListing;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class ListingTest{
	
    @Test
    public void testGetters(){
        Listing testListing = new Listing("Fake Textboook", "44.00", "testDescription", "Admin", "admin@mail.com", "1231231234", "textbook");
        assertEquals("Fake Textboook", testListing.getTitle());
        assertEquals("44.00", testListing.getPrice());
        assertEquals("testDescription", testListing.getDescription());
        assertEquals("Admin", testListing.getUsername());
        assertEquals("admin@mail.com", testListing.getEmail());
        assertEquals("1231231234", testListing.getPhoneNo());
        assertEquals("textbook", testListing.getCategory());
    }
    @Test
    public void testListListings(){
    	ListingRepository mockLR = mock(ListingRepository.class);
    	ListingController lc = new ListingController();
    	lc.setListingRepo(mockLR);
    	List<Listing> mockLists = new ArrayList<Listing>();
    	when(mockLR.findAll()).thenReturn(mockLists);
    	assertEquals(mockLists, lc.listAllListings());
    }
    @Test
    public void testSearchListing() throws IOException, ParseException{
    	SearchListing mockSL = mock(SearchListing.class);
    	ListingRepository mockLR = mock(ListingRepository.class);
    	ListingController lc = new ListingController();
    	lc.setListingRepo(mockLR);
    	String testQuery = "test_query";
    	
    	File testListingFile1 = new File("LocalStorage//testListingFile1");
    	File testListingFile2 = new File("LocalStorage//testListingFile2");
    	File testListingFile3 = new File("LocalStorage//testListingFile3");
    	testListingFile1.createNewFile();
    	testListingFile2.createNewFile();
    	testListingFile3.createNewFile();
    	
    	Listing testListingObject1 = new Listing();
    	testListingObject1.id = "id1";
    	Listing testListingObject2 = new Listing();
    	testListingObject2.id = "id2";
    	Listing testListingObject3 = new Listing();
    	testListingObject3.id = "id3";
    	List<Listing> listingObjects = new ArrayList<Listing>();
    	listingObjects.add(testListingObject1);
    	listingObjects.add(testListingObject2);
    	listingObjects.add(testListingObject3);
    	
    	Writer output = new BufferedWriter(new FileWriter(testListingFile1, true));
    	output.write(testListingObject1.id);
    	output.close();
    	output = new BufferedWriter(new FileWriter(testListingFile2, true));
    	output.write(testListingObject2.id);
    	output.close();
    	output = new BufferedWriter(new FileWriter(testListingFile3, true));
    	output.write(testListingObject3.id);
    	output.close();
    	
    	List<String> testListingFiles = new ArrayList<String>();
    	testListingFiles.add(testListingFile1.getName());
    	testListingFiles.add(testListingFile2.getName());
    	testListingFiles.add(testListingFile3.getName());
    	
    	when(mockSL.searchListings(testQuery)).thenReturn((ArrayList<String>) testListingFiles);
    	when(mockLR.findAllById(any())).thenReturn(listingObjects);
    	testListingFile1.delete();
    	testListingFile2.delete();
    	testListingFile3.delete();
    	assertEquals(listingObjects, lc.searchListing(testQuery));
    }
}
