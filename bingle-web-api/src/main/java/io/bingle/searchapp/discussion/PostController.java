package io.bingle.searchapp.discussion;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.bingle.searchapp.UserService;
import io.bingle.searchapp.account.Account;
import io.bingle.searchapp.account.Instructor;
import io.bingle.searchapp.account.InstructorRepository;
import io.bingle.searchapp.account.Student;
import io.bingle.searchapp.account.StudentRepository;
import io.bingle.searchapp.upload.LucenUtility;


@RestController
@RequestMapping("/posts")
public class PostController {
	
	LucenUtility utility = new LucenUtility();
	
	@Autowired
	private PostRepository postRepo;
	
	@Autowired
	private StudentRepository studentRepo;
	
	@Autowired
	private InstructorRepository instructorRepo;

	@Autowired
	private ThreadRepository threadRepo;

	@Autowired
	private InstructorRepository adminRepo;
	
	@Autowired
	private UserService user;
	
	public PostController(PostRepository postRepo) {
		this.postRepo = postRepo;
	}
	
	@GetMapping("/all")
	public List<Post> getAll() {
		List<Post> posts = postRepo.findAll();
		
		return posts;
	}
	
	@PostMapping("{threadid}/new")
	public Post newPost(@RequestParam Map<String, String> req, @PathVariable("threadid") String id)
	{
		Account account = user.getAccount();
		if (account == null)
		{
			return null;
		}
		//Add a point to the student
		if (account.isStudent()){
			Student s = (Student) account;
			s.addBinglePoints(1);
			studentRepo.save(s);
		}
		Post nPost = new Post(account.getId(), account.getFirstName() + " " + account.getLastName() , id, req.get("parent"), req.get("content"), 0, new Date().toGMTString());
		nPost.setAvatar(account.getAvatar());
		nPost.setUsername(account.getUsername());
		nPost.setAdmin(account.isAdmin());
		this.postRepo.save(nPost);
		
		Optional<Thread> th = this.threadRepo.findById(id);
		
		if (th.isPresent())
		{
			Thread thread = th.get();
			int comments = thread.getPostCount();
			
			thread.setPostCount(comments+1);
			
			this.threadRepo.save(thread);
			
			utility.indexThreadAndPosts(thread, this.postRepo.findByThreadId(id));
			
		}
		
		return nPost;
	}
	
	@PostMapping("/interact")
	public void updatePost(@RequestParam Map<String, String> req)
	{
		Optional<Post> thrd = this.postRepo.findById(req.get("id"));
		
		if (thrd.isPresent())
		{
			Post post = thrd.get();
			
			Account account = user.getAccount();

			int vote_status = account.hasVoted(post.getId());
			if (vote_status == Integer.parseInt(req.get("voteCount")))
			{
				return;
			}
			
			if (req.get("voteCount").equals("-2"))
			{
				post.incrementVoteCount(-2);
				account.setVoted(post.getId(), -1);
			}
			else if (req.get("voteCount").equals("2")){
				post.incrementVoteCount(2);
				account.setVoted(post.getId(), 1);
			}
			else if (req.get("voteCount").equals("-3"))
			{
				post.incrementVoteCount(-1);
				account.setVoted(post.getId(), 0);
			}
			else if (req.get("voteCount").equals("3"))
			{
				post.incrementVoteCount(1);
				account.setVoted(post.getId(), 0);
			}
			else {
				account.setVoted(post.getId(), Integer.parseInt(req.get("voteCount")));
				post.incrementVoteCount(Integer.parseInt(req.get("voteCount")));
			}
			this.postRepo.save(post);
			
			Optional<Student> opt_stu = this.studentRepo.findById(post.getUserId());

			
			//Add a point to the student
			if (opt_stu.isPresent()){
				Student s = opt_stu.get();
				s.addBinglePoints((req.get("voteCount").contains("-") ? -1 : 1));
				studentRepo.save(s);
			}
				
			if (account.isAdmin())
			{
				this.instructorRepo.save((Instructor)account);
			}
			
		}		
		
	}

	@DeleteMapping(value = "/delete")
	public void deletePost(Post post, HttpSession session){
		String userID = post.getUserId();
		Optional<Instructor> adminTemp = adminRepo.findByusername(String.valueOf(session.getAttribute("user_name")));
		Optional<Student> studentTemp = studentRepo.findByusername(String.valueOf(session.getAttribute("user_name")));
		if (adminTemp.isPresent()){
			postRepo.delete(post);
		} else if (studentTemp.isPresent()){
			//Delete if the author is the student
			if (userID.equals(studentTemp.get().getId())){
				postRepo.delete(post);
			}
		} 
	}
}
