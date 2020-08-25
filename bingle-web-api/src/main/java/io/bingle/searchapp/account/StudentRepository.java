package io.bingle.searchapp.account;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends MongoRepository<Student, String>{

    public Optional<Student> findByusername(String username);
    
    public Optional<Instructor> findByemail(String email);


}
