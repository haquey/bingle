package io.bingle.searchapp.marketplace;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.bingle.searchapp.search.SearchListing;
import io.bingle.searchapp.upload.LucenUtility;
import io.bingle.searchapp.UserService;
import io.bingle.searchapp.account.Account;

@RestController
@RequestMapping("/marketplace")
public class ListingController{

    @Autowired
	private ListingRepository listingRepo;
	
    @Autowired
    private UserService user;
    
	/**
	 * Retrieves an ArrayList<Listing> of all listings found on the mlab remote MongoDB
	 * or local depending on the application.properties file
	 */
    @RequestMapping("/all")
    public List<Listing> listAllListings(){
        List<Listing> retList = listingRepo.findAll();
        return retList;
    }
    
    /**
     * This method uses a Listing object listing, and populates the connected MongoDB database with said listing.
     * Part of the listing attributes includes a username, which is set to the user on the session parameter session.
     * This username acts as a seller will be used to determine permissions for the listing for deletion
     * @param listing
     * @param session
     * @return io.bingle.searchapp.marketplace.Listing
     */
    @PutMapping("/create")
    public Listing addListing(@RequestBody Listing listing, HttpSession session) {
    	if (session.getAttribute("user") != null) {
    		Account user = (Account) session.getAttribute("user");
        	listing.setUsername(user.getUsername());
		}
		LucenUtility lucene = new LucenUtility();
		//Index the listing
		Listing addListing = listingRepo.insert(listing);
		try {
			lucene.indexListing(addListing);
		} catch (Exception e) {
			System.err.println("Could not index listing");
		}
    	return addListing;
    }
    /**
     * Returns you an optional object, which is either a listing if it is located in the datbase, or nothing
     * if the listing id id is not found
     * @param id
     * @return
     */
    @RequestMapping("/{id}")
    public Optional<Listing> getListing(@PathVariable("id") String id) {
    	return listingRepo.findById(id);
    }
    
    /**
     * Checks if the listing with id id is present in the database, if it is then increment its view count by 1.
     * @param id
     * @return
     */
    @PostMapping("/{id}")
    public Optional<Listing> getListingIncView(@PathVariable("id") String id) {
    	Optional<Listing> listing = listingRepo.findById(id);
    	if (listing.isPresent()) {
    		Listing getListing = listing.get();
    		//Increment a listing's view count and save the db
    		getListing.setViewCount(getListing.getViewCount() + 1);
    		listingRepo.save(getListing);
    	}
    	return listing;
	}
	
    /**
     * Returns a List<Listing> containing all the listings that matched query on 
     * either file name/content/tags
     * @param query
     * @return
     */
    @GetMapping("/search/{query}")
	public List<Listing> searchListing(@PathVariable("query") String query)
	{
		SearchListing search = new SearchListing();
		List<String> res = null;
		Set<String> ids = new HashSet<String>();
		//Retrieve all files that are in the index table that satisfy the query
		try {
			res = search.searchListings(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Confirm that the files that were indexed are also in local storage
		for (String name : res)
		{
			File tfile = new File("LocalStorage//" + name);
			Scanner input = null;
			try {
				input = new Scanner(tfile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			ids.add(input.nextLine());
		}
		
		List<Listing> allListings = (List<Listing>) listingRepo.findAllById(ids);

	
		return allListings;		
	}
    
    /**
     * Checks if a listing with the id listingId exists, if so, check if the user on the session is either an
     * admin or the seller of said listing, if so, delete the listing, if not then print to stdout: "Error: 
     * Not enough permissions to delete"
     * @param listingId
     * @param session
     */
	@DeleteMapping(value = "/delete/{listingId}")
	public void deleteListing(@PathVariable String listingId, HttpSession session){
		LucenUtility lucene = new LucenUtility();
		Optional<Listing> tempListing = listingRepo.findById(listingId);
		//Listing exists
		if (tempListing.isPresent()){
			if (session.getAttribute("user") != null){
				Account user = (Account) session.getAttribute("user");
				String sellerUsername = tempListing.get().getUsername();
				//User on session is a seller or admin
				if (user.getUsername().equals(sellerUsername) || user.isAdmin()){
					listingRepo.deleteById(listingId);
					System.err.println("THIS IS THE ID WERE TRYING TO DELETE: "+listingId);
					File listingFile = new File("LocalStorage//" + tempListing.get().getTitle());
					listingFile.delete();
					lucene.deleteListingIndex(tempListing.get());
				} else {
					System.out.print("Error: Not enough permissions to delete");
				}
			}
		}
	}
	/**
	 * Setter for testing. This allows a mock instance of the listing repository
	 * @param listingRepo
	 */
	public void setListingRepo(ListingRepository listingRepo) {
		this.listingRepo = listingRepo;
	}
}