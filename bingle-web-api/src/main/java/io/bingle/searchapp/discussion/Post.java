package io.bingle.searchapp.discussion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Posts")
public class Post {
	
	@Id
	private String id;
	private String userId;
	private String threadId;
	private String parentPostId;
	private String content;
	private int voteCount;
	private String createdAt;
	private String name;
    private String avatar;
    private Boolean admin;
    private String username;

	protected Post() {}

	public Post(String userId, String name, String threadId, String parentPostId, String content, int voteCount, String createdAt) {
		super();
		this.userId = userId;
		this.name = name;
		this.threadId = threadId;
		this.parentPostId = parentPostId;
		this.content = content;
		this.voteCount = voteCount;
		this.createdAt = createdAt;
	}
	
	

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getThreadId() {
		return threadId;
	}

	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}

	public String getParentPostId() {
		return parentPostId;
	}

	public void setParentPostId(String parentPostId) {
		this.parentPostId = parentPostId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}
	public void incrementVoteCount(int inc) {
		this.voteCount += inc;
	}
	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
