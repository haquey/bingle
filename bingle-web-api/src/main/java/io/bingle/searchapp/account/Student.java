package io.bingle.searchapp.account;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Students")
public class Student extends Account implements Comparable<Student>{
	
	@Id
	public String id;
	private int binglePoints;

	public Student(String username, String password, String firstName, String lastName) {
		super();
		this.username = username;
		this.password = password;
		this.isStudent = true;
		this.isAdmin = false;
		this.firstName = firstName;
		this.lastName = lastName;
		this.binglePoints = 0;
	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getBinglePoints() {
		return binglePoints;
	}

	public void addBinglePoints(int binglePoints) {
		this.binglePoints += binglePoints;
	}
	
	public int compareTo(Student s){
		return this.binglePoints - s.getBinglePoints();
	}
}
