package io.bingle.searchapp.account;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface InstructorRepository extends MongoRepository<Instructor, String>{

    public Optional<Instructor> findByusername(String username);

    public Optional<Instructor> findByemail(String email);

}
