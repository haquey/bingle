package io.bingle.searchapp.search;

import java.util.Date;

public class ResultEntry {

  private String title;

  private String path;

  private long lastModified;

  private String snippet;

  private String tags;

  private String name;

  public long getLastModified() {
    return lastModified;
  }

  public String getUsername() { return name; }

  public void setUsername(String username) { this.name = username; }

  public void setLastModified(long lastModified) {
    this.lastModified = lastModified;
  }

  public String getSnippet() {
    return snippet;
  }

  public void setSnippet(String snippet) {
    this.snippet = snippet;
  }


  public ResultEntry(String title, String path, long lastModified, String snippet, String tags,
      String username) {

    this.title = title;
    this.path = path;
    this.snippet = snippet;
    this.lastModified = lastModified;
    this.tags = tags;
    this.name = username;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getTags() { return tags; }

  public void setTags(String tags) { this.tags = tags; }

}
