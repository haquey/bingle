package io.bingle.searchapp.discussion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Threads")
public class Thread {
	
	@Id
	private String id;
	private String userId;
	private String title;
	private String content;
	private int voteCount;
	private int postCount;
	private Date createdAt;
	private int viewCount;
	private String name;
	private List<String> tags = new ArrayList<String>();
	
	protected Thread() {

	}

	public Thread(String userId, String name,String title, String content, int voteCount, int postCount, Date createdAt, int viewCount) {
		super();
		this.userId = userId;
		this.name = name;
		this.title = title;
		this.content = content;
		this.voteCount = voteCount;
		this.postCount = postCount;
		this.createdAt = createdAt;
		this.viewCount = viewCount;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(String tag) {
		this.tags.add(tag);
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public int getPostCount() {
		return postCount;
	}

	public void setPostCount(int postCount) {
		this.postCount = postCount;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
