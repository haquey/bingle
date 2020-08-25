package io.bingle.searchapp.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.bingle.searchapp.discussion.Thread;

public abstract class Account {

    public String id;

    protected String username;
    protected String password;
    protected boolean isStudent;
    protected boolean isAdmin;
    protected String firstName;
    protected String lastName;
    protected HashMap<String, Integer> uploads = new HashMap<String, Integer>();
    protected HashMap<String, Integer> tags = new HashMap<String, Integer>();
    protected String avatar;
    protected HashMap<String, Integer> tagsSearched = new HashMap<String, Integer>();
    protected String phoneNum = "N/A";
    protected String email = "N/A";
    protected HashMap<String, Integer> voted = new HashMap<String, Integer>();


    public Account() {}

    public Account(String username, String password, boolean isStudent,
    		String firstName, String lastName) {
		this.username = username;
		this.password = password;
		this.isStudent = isStudent;
		this.isAdmin = !isStudent;
		this.firstName = firstName;
		this.lastName = lastName;
	}
    
    
    public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	/*
     * Will be called during the login process, will compare the given password
     * to the one associated on this account
     */
    public boolean authenticate(String password){
    	return (this.password == password);
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isStudent() {
		return isStudent;
	}

	public void setStudent(boolean isStudent) {
		this.isStudent = isStudent;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getPhoneNum() {
		return this.phoneNum;
	}

	public void setPhoneNum(String phone_num) {
		this.phoneNum = phone_num;
	}
	
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setUploadType(String fileType) {
		if (!this.uploads.containsKey(fileType))
		{
			this.uploads.put(fileType, 0);
		}
		this.uploads.put(fileType, this.uploads.get(fileType) + 1);
	}
	
	public HashMap<String, Integer> getUploads(){
		return this.uploads;
	}
	
	public void setTags(String tag) {
		if (!this.tags.containsKey(tag))
		{
			this.tags.put(tag, 0);
		}
		this.tags.put(tag, this.tags.get(tag) + 1);
	}
	
	public HashMap<String, Integer> getTags(){
		return this.tags;
	}
	
	public void setTagsSearched(String tag) {
		if (!this.tagsSearched.containsKey(tag))
		{
			this.tagsSearched.put(tag, 0);
		}
		this.tagsSearched.put(tag, this.tagsSearched.get(tag) + 1);
	}
	
	public HashMap<String, Integer> getTagsSearched(){
		return this.tagsSearched;
	}
	public void setVoted(String id, Integer vote) {
		if (!this.voted.containsKey(id))
		{
			this.voted.put(id, 0);
		}
		this.voted.put(id, vote);
	}
	
	public HashMap<String, Integer> getVoted(){
		return this.voted;
	}
	
	public Integer hasVoted(String id) {
		if (this.voted.containsKey(id))
		{
			this.voted.get(id);
		}
		return 0;
	}
	
	@Override
    public String toString() {
        return String.format(
                "Account[id=%s, username='%s', password='%s']",
                id, username, password);
    }

}
