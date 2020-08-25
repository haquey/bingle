package io.bingle.searchapp.account;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Instructors")
public class Instructor extends Account {
	
	@Id
	public String id;

	public Instructor(String username, String password, String firstName, String lastName) {
		super();
		this.username = username;
		this.password = password;
		this.isStudent = false;
		this.isAdmin = true;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public boolean isAdmin(){
		return this.isAdmin;
	}

}
