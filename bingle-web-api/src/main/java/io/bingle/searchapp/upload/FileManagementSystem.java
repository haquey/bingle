package io.bingle.searchapp.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManagementSystem {
	
	public FileManagementSystem()
	{
		
	}

    public  Path localStorage(MultipartFile file) {

        try {

            new File("LocalStorage").mkdirs();
            byte[] bytes = file.getBytes();
            Path path = Paths.get("LocalStorage//" + file.getOriginalFilename());
            Files.write(path, bytes);

            return path;

        } catch(IOException e) {

            e.printStackTrace();

            return null;
        }
    }
    
    
    public  Path localStorage(String file) {

        try {

        	
            new File("LocalStorage").mkdirs();
            byte[] bytes = file.getBytes();
            Path path = Paths.get("LocalStorage//" + file.replaceAll("[\\\\/:*?\"<>|]", "_"));
            Files.write(path, bytes);

            return path;

        } catch(IOException e) {

            e.printStackTrace();

            return null;
        }
    }

}
