package io.bingle.searchapp.upload;

import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Document(collection = "fileInfo")
public class FileInfo {

  @Id
  private String id;

  private String fileTitle;

  private String fileLocation;

  @DateTimeFormat(iso = ISO.DATE_TIME)
  private Date createdDate;

  public FileInfo(String fileTitle, String fileLocation) {

    this.fileTitle = fileTitle;
    this.fileLocation = fileLocation;
    this.createdDate = new Date();
  }
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFileTitle() {
    return fileTitle;
  }

  public void setFileTitle(String fileTitle) {
    this.fileTitle = fileTitle;
  }

  public String getFileLocation() {
    return fileLocation;
  }

  public void setFileLocation(String fileLocation) {
    this.fileLocation = fileLocation;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

}
