package io.bingle.searchapp.upload;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.nio.file.Path;

import io.bingle.searchapp.GoogleOCR;
import io.bingle.searchapp.UserService;
import io.bingle.searchapp.account.Account;
import io.bingle.searchapp.account.Instructor;
import io.bingle.searchapp.account.InstructorRepository;
import io.bingle.searchapp.account.Student;
import io.bingle.searchapp.account.StudentRepository;
import io.bingle.searchapp.upload.FileManagementSystem;
import io.bingle.searchapp.upload.LucenUtility;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

@Controller
public class UploadController {

  @Autowired
  private FileInfoRepository fileRepo;

	@Autowired
	private StudentRepository studentRepo;
	
	@Autowired
	private InstructorRepository instructorRepo;
	
	@Autowired
	private UserService user;
	
	@Autowired
	private GoogleOCR ocr;
	
    @GetMapping("/upload")
    public String upload() {

      LucenUtility utility = new LucenUtility();
      return "upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("filesToUpload") MultipartFile file,
        @RequestParam("file_name") String filename, @RequestParam("tags") String tags,
        RedirectAttributes redirectAttributes, HttpSession session) throws IOException {
		

		Account account = user.getAccount();

		Account userAccount = (Account) session.getAttribute("user");


		
		try {
			account.setUploadType(FilenameUtils.getExtension(file.getOriginalFilename()).toUpperCase());
			String[] tag_processed = tags.split(";");
	        for (String t : tag_processed)
	        {
	        	account.setTags(t);
	        }
			//Add a point to the student
			if (account.isStudent()){
				((Student)account).addBinglePoints(10);
			}
			user.saveAccount();
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
        Path path =  new FileManagementSystem().localStorage(file);
        FileInfo info = new FileInfo(file.getName(), path.toFile().getPath());
        fileRepo.save(info);
        LucenUtility utility = new LucenUtility();
		if ((file.getContentType().split("/")[0]).equals("image"))
		{
			ocr.upload(file);

			String annotations = ocr.getAnnotations(file);
			Pattern p = Pattern.compile("description\": \"([a-z0-9\\s]*)\"");
			Matcher m = p.matcher(annotations);
			StringBuilder sb = new StringBuilder();

			while(m.find())
			{
				sb.append(m.group(1) + ";");
			}
			
			sb.append(tags);
			utility.indexImage(path.toFile(), info.getId(), sb.toString(), filename, userAccount.getUsername());
			
		}
		else {
	        utility.indexFile(path.toFile(), info.getId(), tags, filename, userAccount.getUsername());

		}

        return "redirect:/upload";
    }

}
