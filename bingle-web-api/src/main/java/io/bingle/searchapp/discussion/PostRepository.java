package io.bingle.searchapp.discussion;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends MongoRepository<Post, String>{
	
	
	@Query(value= "{'threadId' : ?0 }")
	List<Post> findByThreadId(String threadId);
	
	@Query(value= "{'threadId' : ?0 , 'parentPostId' : ?1}")
	List<Post> findParentPostByThreadId(String threadId, String parentPostId);
	
	@Query(value= "{'parentPostId' : ?0 }")
	List<Post> findByParentPostId(String parentPostId);
	
	@Query(value= "{'userId' : ?0 }")
	List<Post> findByuserId(String userId);
}
