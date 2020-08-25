package io.bingle.searchapp.discussion;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ThreadRepository extends MongoRepository<Thread, String> {
	
	@Query(value= "{'userId' : ?0 }")
	List<Thread> findByuserId(String userId);
}