package io.bingle.searchapp.discussion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.util.Date;

import org.apache.lucene.queryparser.classic.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import io.bingle.searchapp.UserService;
import io.bingle.searchapp.account.Account;
import io.bingle.searchapp.account.AccountRepository;
import io.bingle.searchapp.account.Instructor;
import io.bingle.searchapp.account.InstructorRepository;
import io.bingle.searchapp.search.SearchThreads;
import io.bingle.searchapp.upload.FileManagementSystem;
import io.bingle.searchapp.upload.LucenUtility;
import io.bingle.searchapp.account.Student;
import io.bingle.searchapp.account.StudentRepository;

@RestController
@RequestMapping("/threads")
public class ThreadController {
	
	@Autowired
	private ThreadRepository threadRepo;
	
	@Autowired
	private InstructorRepository instructorRepo;
	
	@Autowired
	private StudentRepository studentRepo;

	@Autowired
	private UserService user;
	
	public ThreadController(ThreadRepository threadRepo) {
		this.threadRepo = threadRepo;
	}
	
	@GetMapping("/all")
	public List<Thread> getAll() {
		List<Thread> threads = threadRepo.findAll();
		
		return threads;
	}

	@PostMapping("/new")
	public Thread newThread(@RequestParam Map<String, String> req, HttpSession session)
	{

		Account account = user.getAccount();
		if (account == null)
		{
			return null;
		}
		//Add a point to the student
		if (account.isStudent()){
			((Student)account).addBinglePoints(1);
		}
		
		Date d = new Date();
		Thread nThread = new Thread(account.getId(), account.getFirstName() + " " + account.getLastName(), req.get("title"), req.get("text"), 1, 0, d, 0); // REPLACE WITH ACTUALY USER ID
		
		for (String tag : req.get("tags").split(";"))
		{
			if (tag != "")
			{
				nThread.setTags(tag);
			}
		}
		
		threadRepo.save(nThread);
		
		user.saveAccount();
		
		LucenUtility utility = new LucenUtility();
		utility.indexThread(nThread, false);
		
		return nThread;
		
	}
	
	@PostMapping("/interact")
	public void updateThread(@RequestParam Map<String, String> req)
	{
		Optional<Thread> thrd = this.threadRepo.findById(req.get("id"));
		
		if (thrd.isPresent())
		{
			Thread thread = thrd.get();
			Account account = user.getAccount();

			int vote_status = account.hasVoted(thread.getId());
			if (vote_status == Integer.parseInt(req.get("voteCount")))
			{
				return;
			}
			
			if (req.get("voteCount").equals("-2"))
			{
				thread.incrementVoteCount(-2);
				account.setVoted(thread.getId(), -1);
			}
			else if (req.get("voteCount").equals("2")){
				thread.incrementVoteCount(2);
				account.setVoted(thread.getId(), 1);
			}
			else if (req.get("voteCount").equals("-3"))
			{
				thread.incrementVoteCount(-1);
				account.setVoted(thread.getId(), 0);
			}
			else if (req.get("voteCount").equals("3"))
			{
				thread.incrementVoteCount(1);
				account.setVoted(thread.getId(), 0);
			}
			else {
				account.setVoted(thread.getId(), Integer.parseInt(req.get("voteCount")));
				thread.incrementVoteCount(Integer.parseInt(req.get("voteCount")));
			}
			Optional<Student> opt_stu = this.studentRepo.findById(thread.getUserId());
			
			if (opt_stu.isPresent())
			{
				Student student = opt_stu.get();
				student.addBinglePoints((req.get("voteCount").contains("-") ? -1 : 1));
				studentRepo.save(student);
			}
			
			if (account.isAdmin())
			{
				this.instructorRepo.save((Instructor)account);
			}
			this.threadRepo.save(thread);
		}		
		
	}
	
	@GetMapping("/search")
	public Set<Thread> searchThread(@RequestParam Map<String, String> req)
	{
		SearchThreads search = new SearchThreads();
		List<String> res = null;
		Set<Thread> result = new HashSet<Thread>();
		String id = null;
		try {
			res = search.searchThread(req.get("query"));
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Set<String> resSet = new HashSet<String>(res);
		for (String name : resSet)
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
			if (this.threadRepo.findById(id).isPresent())
			{
				result.add(this.threadRepo.findById(id).get());
			}
		}
		
		Account user = this.user.getAccount();
		
		if (user != null)
		{
			for (Thread r : result)
			{
				if (r.getTags() != null)
				{
					for (String t : r.getTags())
					{
						user.setTagsSearched(t);
					}
				}
			}
			this.user.saveAccount();

		}
			
		return result;
	}

	@DeleteMapping(value = "/delete/{threadId}")
	public void deletePost(@PathVariable String threadId, HttpSession session){
		LucenUtility lucene = new LucenUtility();
		if (session.getAttribute("user") != null) {
			Account user = (Account) session.getAttribute("user");
			Optional<Thread> tempThread = threadRepo.findById(threadId);
			if (tempThread.isPresent()){
				if (user.isAdmin()) {
					lucene.deleteThreadIndex(tempThread.get());
					threadRepo.deleteById(threadId);
				}
			}
		}
		
	}
}
