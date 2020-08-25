package io.bingle.searchapp.account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.gson.annotations.JsonAdapter;

import io.bingle.searchapp.discussion.Thread;
import io.bingle.searchapp.UserService;
import io.bingle.searchapp.discussion.Post;
import io.bingle.searchapp.discussion.PostRepository;
import io.bingle.searchapp.discussion.ThreadRepository;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Null;

@Controller
public class AccountController {
	//private AccountRepository userRepo;
	@Autowired
	private StudentRepository studentRepo;
	
	@Autowired
	private InstructorRepository adminRepo;
	
	@Autowired
	private PostRepository postRepo;
	
	@Autowired
	private ThreadRepository threadRepo;
	
	public void setStudentRepo(StudentRepository studentRepo) {
		this.studentRepo = studentRepo;
	}

	public void setAdminRepo(InstructorRepository adminRepo) {
		this.adminRepo = adminRepo;
	}

	@Autowired
	private UserService user;

	@RequestMapping("/all-students")
	public List<Student> listAllUsers() {
		List<Student> students = this.studentRepo.findAll();
		return students;
	}
	
	@RequestMapping("/all-admins")
	public List<Instructor> listAllAdmins() {
		List<Instructor> admins = this.adminRepo.findAll();
		return admins;
	}

	@GetMapping(value = "/login")
	public String login(HttpSession session)
	{
		return "login";
	}

	@PostMapping(value = "/login")
	@ResponseBody
	public ResponseEntity<?> signin(@RequestParam("username") String username, @RequestParam("password") String password,
						 HttpSession session, HttpServletResponse response) {

		Optional<Student> tempStudent = studentRepo.findByusername(username);
		Optional<Instructor> tempAdmin = adminRepo.findByusername(username);
		Account account = null;

		// if username not found in student check the teacher repo
		if(tempStudent.isPresent()){
			account = tempStudent.get();
		} else if (tempAdmin.isPresent()){
			account = tempAdmin.get();
		}

		if(account != null) {

			JSONObject obj = new JSONObject();

			if (account.getPassword().equals(password)) {
				
				session.setAttribute("user", account);

				Cookie cookie = new Cookie("user", username);
				cookie.setMaxAge(265 * 24 * 60 * 60);  // (s)
				cookie.setPath("/");
				response.addCookie(cookie);
				System.out.println("logged in");
				user.setAccount(account);

				return ResponseEntity.ok().body("success");

			}
			
			System.err.println("HERE");

		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong password or username");
	}


	@GetMapping (value = "/logout")
	public String signout(HttpSession session, HttpServletResponse response) {

		session.removeAttribute("user");

		Cookie cookie = new Cookie("user", null);
		cookie.setMaxAge(265 * 24 * 60 * 60);  // (s)
		cookie.setPath("/");
		response.addCookie(cookie);
		user.setAccount(null);

		
		return "redirect:/";
	}

	@PutMapping(value = "/register")
	public ResponseEntity<?> handleRegister(@RequestBody Map<String, String> info, HttpSession session, 
			HttpServletResponse response) {
		Account account = null;
		System.out.println("qqqqq");

		if (this.studentRepo.findByusername(info.get("username")).isPresent() ||
			this.adminRepo.findByusername(info.get("username")).isPresent())
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already taken");
		}
		if (this.studentRepo.findByemail(info.get("email")).isPresent() ||
				this.adminRepo.findByemail(info.get("email")).isPresent())
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already taken");
		}
		// Info Validation
		if (info.get("account_type").contains("Student"))
		{
			account = new Student(info.get("username"), info.get("password"), info.get("first_name"), info.get("last_name"));
			account.setEmail(info.get("email"));
			studentRepo.save((Student)account);

		}
		else {
			account = new Instructor(info.get("username"), info.get("password"), info.get("first_name"), info.get("last_name"));
			account.setEmail(info.get("email"));
			adminRepo.save((Instructor)account);
		}
		System.out.println("qqqqq");
		session.setAttribute("user", account);

		Cookie cookie = new Cookie("user", account.getUsername());
		cookie.setMaxAge(265 * 24 * 60 * 60);  // (s)
		cookie.setPath("/");
		response.addCookie(cookie);
		System.out.println("logged in");
		user.setAccount(account);

		return ResponseEntity.ok().body("success");
		
	}
	
	@PostMapping(value = "/avatar")
	@ResponseBody
	public ResponseEntity<?> addAvatar(@RequestBody Map<String, String> imgMap, HttpSession session) {
		if (session.getAttribute("user") != null) {
			Account user = (Account) session.getAttribute("user");
			user.setAvatar(imgMap.get("image"));
			if (user.isStudent)
			{
				this.studentRepo.save((Student) user);
			}
			else {
				this.adminRepo.save((Instructor) user);

			}
			System.err.println(user.getAvatar());
		}
		
		return ResponseEntity.ok().body("success");
	}
}
