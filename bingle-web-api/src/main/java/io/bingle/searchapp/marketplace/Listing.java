package io.bingle.searchapp.marketplace;

import io.bingle.searchapp.account.Account;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Listings")
public class Listing {

    @Id
    public String id;

    private String title;
    private String price;
    private String description;
    private String username;
    private String email;
    private int viewCount;
	private LocalDateTime createdAt;
    private String phoneNo;
    private String image;
    private List<String> tags;
    private String category; //TODO: Determine if this will have its own class
    
    public Listing() {}

    public Listing(String title, String price, String description, String username, String email, String phoneNo, String category){
        this.title = title;
        this.price = price;
        this.description = description;
        this.username = username;
        this.email = email;
        this.viewCount = 0;
        this.phoneNo = phoneNo;
        this.category = category;
    }

    public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTitle(){
        return this.title;
    }

    public String getPrice(){
        return this.price;
    }

    public String getDescription(){
        return this.description;
    }

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getCategory(){
        return this.category;
    }

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
}