package io.bingle.searchapp.upload;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FileInfoRepository extends MongoRepository<FileInfo, String> {

  @Query("{_id : ?0}")
  public FileInfo findUserById(String id);
}
