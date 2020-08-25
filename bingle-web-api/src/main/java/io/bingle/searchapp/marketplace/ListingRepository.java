package io.bingle.searchapp.marketplace;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingRepository extends MongoRepository<Listing, String>{

	List<Listing> findByusername(String username);
    
}