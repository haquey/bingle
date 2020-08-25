package io.bingle.searchapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Collections;
import java.util.Comparator;

import javax.servlet.http.HttpSession;

import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.json
import org.springframework.boot.actuate.trace.http.HttpTrace.Response;

import io.bingle.searchapp.account.*;
import io.bingle.searchapp.discussion.*;
import io.bingle.searchapp.discussion.Thread;
import io.bingle.searchapp.marketplace.Listing;
import io.bingle.searchapp.marketplace.ListingRepository;
import io.bingle.searchapp.search.ResultEntry;
import io.bingle.searchapp.upload.LucenUtility;

@Controller
public class ViewController {
	//private AccountRepository userRepo;
	@Autowired
	private StudentRepository studentRepo;
	
	@Autowired
	private InstructorRepository instructorRepo;
	
	@Autowired
	private ThreadRepository threadRepo;
	
	@Autowired
	private PostRepository postRepo;

	@Autowired
	private ListingRepository listingRepo;

	@Autowired
	private UserService user;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		return "index";
	}
	
	@RequestMapping(value = "/discussion", method = RequestMethod.GET)
	public String discussion(Model model) {
		List<Thread> threads = threadRepo.findAll();
		
		model.addAttribute("threads", threads);
		
		return "discussion";
	}
	
	@RequestMapping(value = "/marketplace", method = RequestMethod.GET)
	public String marketplace(Model model, HttpSession session) {

		List<Listing> listings = listingRepo.findAll();
		model.addAttribute("listings", listings);
		if (session.getAttribute("user") != null) {
			model.addAttribute("curr_user", ((Account) session.getAttribute("user")).getUsername());
			model.addAttribute("is_admin", ((Account) session.getAttribute("user")).isAdmin());
		}
		return "marketplace";
	}

	
	@RequestMapping(value = "/discussion/posts/{threadid}", method = RequestMethod.GET)
	public String post(Model model, @PathVariable("threadid") String id) {
		List<Post> posts = postRepo.findByThreadId(id);
		
		Optional<Thread> th = threadRepo.findById(id);
		
		if (th.isPresent())
		{
			Thread tUpdate = th.get();
			int newCount = th.get().getViewCount();
			tUpdate.setViewCount(newCount+1);
			
			this.threadRepo.save(tUpdate);
		}
				
		model.addAttribute("thread", threadRepo.findById(id).get());
		model.addAttribute("posts", posts);
		
		return "posts";
	}

  @RequestMapping(value = "/resultpage", method = RequestMethod.GET)
  public String resultpage(Model model) {

    List<Listing> listings = listingRepo.findAll();
    model.addAttribute("listings", listings);

    return "resultpage";
  }
  
  @RequestMapping(value = "/profile", method = RequestMethod.GET)
  public String profile(Model model, HttpSession session)
  {
	LucenUtility lucene = new LucenUtility();

	if (session == null) // Incase someone tries to go directly to profile without logging in
	{
		return null;
	}

	List<Thread> threads = threadRepo.findByuserId(user.getAccount().getId());
	List<Post> posts = postRepo.findByuserId(user.getAccount().getId());
	List<Listing> listings = listingRepo.findByusername(user.getAccount().getUsername());
	List<ResultEntry> uploads = null;
	try {
		uploads = lucene.searchByUsername(user.getAccount().getUsername());
	} catch (ParseException | IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	model.addAttribute("account", user.getAccount());
	model.addAttribute("threads", threads);
	model.addAttribute("posts", posts);
	model.addAttribute("listings", listings);
	model.addAttribute("uploads", uploads);

	return "profile";  
  }
  
  @RequestMapping(value = "/leaderboard", method = RequestMethod.GET)
  public String leaderboard(Model model)
  {
	  
	  
	  HashMap<String, Integer> uploads = new HashMap<String, Integer>();
	  HashMap<String, Integer> tags_used = new HashMap<String, Integer>();
	  HashMap<String, Integer> tags_searched = new HashMap<String, Integer>();
	  List<Student> studentList = studentRepo.findAll();
	  
	  int length = studentList.size();
	  ArrayList<Student> ten;
	  if (length > 10) {
		ten = new ArrayList<Student>(studentList.subList(0,9));
	  } else {
		ten = new ArrayList<Student>(studentList);
	  }
	  
	  // insertion sort cause im lazy to make comparator
	  Student temp;
      for (int i = 1; i < ten.size(); i++) {
          for(int j = i ; j > 0 ; j--){
              if(ten.get(j).getBinglePoints() > ten.get(j-1).getBinglePoints()){
                  temp = ten.get(j);
                  ten.set(j, ten.get(j-1));
                  ten.set(j-1, temp);
              }
          }
      }
	  List<HashMap<String, String>> topten = new ArrayList<>();
	  for (Student stu : ten) {
		HashMap<String, String> stud = new HashMap<>();
		stud.put("user", stu.getUsername());
		stud.put("points", String.valueOf(stu.getBinglePoints()));
		stud.put("email", stu.getEmail());
		stud.put("fullname", stu.getFirstName() + " " + stu.getLastName());
		System.out.println(stu.getBinglePoints());
		topten.add(stud);
	  }
	  List<Account> accounts = new ArrayList<Account>();
	  
	  accounts.addAll(this.instructorRepo.findAll());
	  accounts.addAll(this.studentRepo.findAll());
	  for (Account a : accounts)
	  {
		  System.out.println(a.getUsername());
		  for (String key : a.getUploads().keySet())
		  {
			  if (!uploads.containsKey(key))
			  {
				  uploads.put(key, 0);
			  }
			  uploads.put(key, a.getUploads().get(key) + uploads.get(key));
		  }
		  
		  for (String key : a.getTags().keySet())
		  {
			  if (!tags_used.containsKey(key))
			  {
				  tags_used.put(key, 0);
			  }
			  tags_used.put(key, a.getTags().get(key) + tags_used.get(key));
		  }
		  
		  for (String key : a.getTagsSearched().keySet())
		  {
			  if (!tags_searched.containsKey(key))
			  {
				  tags_searched.put(key, 0);
			  }
			  tags_searched.put(key, a.getTagsSearched().get(key) + tags_searched.get(key));
		  }
	  }
	  
	  model.addAttribute("uploads", uploads);
	  model.addAttribute("tagsUsed", tags_used);
	  model.addAttribute("tagsSearched", tags_searched);
	  model.addAttribute("topten", topten);

	  return "leaderboard";
  }
}
