package io.bingle.searchapp;

import java.util.List;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import io.bingle.searchapp.account.Account;
import io.bingle.searchapp.account.Student;
import io.bingle.searchapp.account.StudentRepository;
import io.bingle.searchapp.account.AccountRepository;
import io.bingle.searchapp.account.Instructor;
import io.bingle.searchapp.account.InstructorRepository;

import io.bingle.searchapp.discussion.ThreadRepository;
import io.bingle.searchapp.marketplace.Listing;
import io.bingle.searchapp.marketplace.ListingRepository;
import io.bingle.searchapp.upload.LucenUtility;
import io.bingle.searchapp.discussion.Post;
import io.bingle.searchapp.discussion.PostRepository;
import io.bingle.searchapp.discussion.Thread;

@Component
public class DatabaseSeeder implements CommandLineRunner{
	
	// accounts
	private StudentRepository userRepo;
	private InstructorRepository adminRepo;
	
	// discussion board
	private ThreadRepository threadRepo;
	private PostRepository postRepo;

	// listings
	private ListingRepository listingRepo;
	
	private UserService user;

	// filapath repo
	
	public DatabaseSeeder(StudentRepository userRepo, InstructorRepository adminRepo, ThreadRepository threadRepo, 
			PostRepository postRepo, ListingRepository listingRepo, UserService user) {
		this.userRepo = userRepo;
		this.adminRepo = adminRepo;
		this.threadRepo = threadRepo;
		this.postRepo = postRepo;
		this.listingRepo = listingRepo;
		this.user = user;
	}

	@Override
	public void run(String... args) throws Exception {
		Account admin = new Instructor("Admin", "admin123", "Admin", "Admin");
		Account abbas = new Instructor("abbasA", "studentSuffering123", "Abbas", "HisLastName");
		Account yasir = new Student("haqueya1", "lulialmosttypedinmyrealpass", "Yasir", "Haque");
		Account kareem = new Student("kreemy", "ihopethishelps", "Kareem", "Mohamed");
		Account sadman = new Student("sadmanrafid", "pegasus07", "Sadman", "Rafid");

		admin.setUploadType("PDF");
		admin.setUploadType("PDF");
		admin.setUploadType("PDF");
		admin.setUploadType("PDF");
		admin.setUploadType("PDF");
		admin.setUploadType("PDF");
		admin.setUploadType("PDF");
		admin.setUploadType("PDF");
		admin.setUploadType("PDF");
		admin.setUploadType("PDF");

		admin.setUploadType("HS");
		admin.setUploadType("HS");
		admin.setUploadType("HS");
		admin.setUploadType("TXT");
		admin.setUploadType("TXT");
		
		admin.setTags("CSCC24");
		admin.setTags("CSCC24");
		admin.setTags("CSCC24");
		admin.setTags("CSCC24");
		admin.setTags("CSCC24");
		admin.setTags("Notes");
		admin.setTags("Notes");
		admin.setTags("Notes");
		admin.setTags("Notes");
		admin.setTags("Notes");
		admin.setTags("Recursion");
		admin.setTags("Haskell");
		admin.setTags("Haskell");


		yasir.setUploadType("IMG");
		yasir.setUploadType("IMG");
		yasir.setUploadType("IMG");
		yasir.setUploadType("IMG");
		yasir.setUploadType("IMG");
		yasir.setUploadType("IMG");
		yasir.setUploadType("PDF");
		yasir.setUploadType("PDF");
		yasir.setUploadType("PDF");

		Thread welcome = new Thread("account_id123","F1 L1","Welcome to Bingle", "This \"is\" a test  CompSci.", 0, 7, new Date(), 0);
		welcome.setTags("welcome");
		welcome.setTags("greeting");
		welcome.setTags("hi");

		Thread welcome2 = new Thread("account_id33","F2 L2", "Welcome to Bingle2", "This is a test2 CSCC01", 1, 0, new Date(), 1);
		welcome2.setTags("blah");
		welcome2.setTags("test");
		welcome2.setTags("this is a very long tag");

		Thread welcome3 = new Thread("account_id12","F3 L3", "Welcome to Bingle3", "This is a test3 CSCC01 UTSC", 2, 0, new Date(), 2);
		
		Listing listing1 = new Listing("Fake Textbook", "44.00", "testDescription", "Admin", "admin@mail.com", "1231231234", "textbook");
		listing1.setTags(Arrays.asList("fake", "mock", "hardcover"));
		Listing listing8 = new Listing("Fake Textboook", "44.00", "testDescription", "Admin", "admin@mail.com", "1231231234", "textbook");
		Listing listing9 = new Listing("Fake Textboook", "44.00", "testDescription", "Admin", "admin@mail.com", "1231231234", "textbook");
		Listing listing2 = new Listing("Mock Ebook", "10.00", "testDescription2", "Admin", "admin@mail.com", "1231231234", "ebook");
		Listing listing3 = new Listing("Some Course Note", "5.00", "testDescription3", "Admin", "admin@mail.com", "1231231234", "coursenote");
		Listing listing10 = new Listing("Some Course Note", "5.00", "testDescription3", "Admin", "admin@mail.com", "1231231234", "coursenote");
		Listing listing4 = new Listing("CSCC01 Old Assignment X", "12.00", "testDescription3", "Admin", "admin@mail.com", "1231231234", "coursematerial");
		Listing listing7 = new Listing("1080Ti Calculator", "20.00", "testDescription3", "Admin", "admin@mail.com", "1231231234", "utencil");
		Listing listing5 = new Listing("UTSC Hoodie", "30.00", "testDescription3", "Admin", "admin@mail.com", "1231231234", "clothing");
		Listing listing6 = new Listing("Trek Bicycle", "109.00", "testDescription3", "Admin", "admin@mail.com", "1231231234", "misc");
		// DROP TABLE
		this.userRepo.deleteAll();
		this.adminRepo.deleteAll();
		this.threadRepo.deleteAll();
		this.postRepo.deleteAll();
		this.listingRepo.deleteAll();
		
		//Set up arrays with all the objects
		List<Instructor> admins = Arrays.asList((Instructor) admin, (Instructor) abbas);
		List<Student> people = Arrays.asList( (Student) yasir, (Student) kareem, (Student) sadman);
		List<Thread> threads = Arrays.asList(welcome, welcome2, welcome3);
		List<Listing> listings = Arrays.asList(listing1, listing2, listing3, listing4, listing5, listing6, listing7, listing8, listing9, listing10);
		
		
		//Save data into repos
		this.userRepo.saveAll(people);
		this.adminRepo.saveAll(admins);
		this.threadRepo.saveAll(threads);
		this.listingRepo.saveAll(listings);
		
		
		
		Account firstAcc = this.adminRepo.findAll().get(0);
		Thread firstThread = this.threadRepo.findAll().get(0);
		
		Post postFour = new Post(firstAcc.getId(), "F1 L1", firstThread.getId(), "-1", "This is a post title4.", 0, new Date().toGMTString());
		postFour.setUsername("Bboy112");
		Post postOne = new Post(firstAcc.getId(), "F1 L1", firstThread.getId(), "-1", "This is a post title.", 0, new Date().toGMTString());
		Post postTwo = new Post(firstAcc.getId(), "F3 L3",firstThread.getId(), "-1", "This is a post title2.", 2, new Date().toGMTString());
		Post postThree = new Post(firstAcc.getId(), "F2 L2",firstThread.getId(), "-1", "This is a post title3.",  1, new Date().toGMTString());
		List<Post> posts = Arrays.asList(postOne, postTwo, postThree, postFour);
		this.postRepo.saveAll(posts);
		
		Post childPostOne = new Post(firstAcc.getId(), "F2 L2",firstThread.getId(), postOne.getId(), "This is a CHILD post title.", 0, new Date().toGMTString());
		Post childPostTwo = new Post(firstAcc.getId(), "F3 L3",firstThread.getId(), postOne.getId(), "This is a CHILD post title 222.", 0, new Date().toGMTString());

		this.postRepo.save(childPostOne);
		this.postRepo.save(childPostTwo);
		
		Post childChildPostOne = new Post(firstAcc.getId(), "F1 L1",firstThread.getId(), childPostOne.getId(), "This is a Cchild of a CHILD post title.", 0, new Date().toGMTString());
		
		this.postRepo.save(childChildPostOne);
		
		LucenUtility utility = new LucenUtility();
		
		List<Listing> dbListings = this.listingRepo.findAll();
		

		for (Listing l : dbListings){
			utility.deleteListingIndex(l);
		}

		for (Listing listing : dbListings) {
			utility.indexListing(listing);
		}

		utility.deleteThreadIndex(welcome);
		utility.deleteThreadIndex(welcome2);
		utility.deleteThreadIndex(welcome3);
		
		GoogleOCR q = new GoogleOCR();
//		q.upload();
		
		File f = new File("LocalStorage//Welcome to Bingle");
    	f.delete();
    	f = new File("LocalStorage//Welcome to Bingle2");
    	f.delete();
    	f = new File("LocalStorage//Welcome to Bingle3");
    	f.delete();

    	utility.indexThreadAndPosts(welcome, posts);
    	utility.indexThread(welcome2, false);
    	utility.indexThread(welcome3, false);
	}

}
