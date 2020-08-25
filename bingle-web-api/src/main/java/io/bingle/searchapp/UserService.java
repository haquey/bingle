package io.bingle.searchapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.bingle.searchapp.account.Account;
import io.bingle.searchapp.account.Instructor;
import io.bingle.searchapp.account.InstructorRepository;
import io.bingle.searchapp.account.Student;
import io.bingle.searchapp.account.StudentRepository;

@Service
public class UserService {
	
	@Autowired
	private InstructorRepository instructorRepo;
	
	@Autowired
	private StudentRepository studentRepo;
	
	
	private Account account = null;
	
	public UserService() {
	}
	
	public void setAccount(Account account)
	{
		this.account = account;
	}
	
	public Account getAccount() {
		return this.account;
	}
	
	public void saveAccount() {
		if (this.account != null)
		{
			if (this.account.isStudent())
			{
				this.studentRepo.save((Student) this.account);
			}
			else {
				this.instructorRepo.save((Instructor)this.account);
			}
		}
	}
}
