package io.bingle.searchapp.upload;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UploadControllerTest {
	
	String pathToTestFiles = System.getProperty("user.dir") + "//src//test//java//io//bingle//searchapp//upload//";
	File localStorageDirectory =  new File("LocalStorage");
	
	@Test
	public void uploadFileTest() throws IOException{ 
		File file = new File(pathToTestFiles + "testFileUpload.txt");
		FileInputStream input = new FileInputStream(file);
		MultipartFile mFile = new MockMultipartFile("file", file.getName(), "text/plain", 
				IOUtils.toByteArray(input));
		
		new FileManagementSystem().localStorage(mFile);
		File fileRetrieved = new File("LocalStorage//" + file.getName());
		assertTrue(fileRetrieved.exists());
		
		Files.delete(Paths.get(localStorageDirectory.getPath() + "//" + file.getName()));
	}
	
	@Test
	public void noDuplicateTest() throws IOException{
		File file = new File(pathToTestFiles + "testFileUpload.txt");
		FileInputStream input = new FileInputStream(file);
		MultipartFile mFile = new MockMultipartFile("file", file.getName(), "text/plain", 
				IOUtils.toByteArray(input));
		
		new FileManagementSystem().localStorage(mFile);
		new FileManagementSystem().localStorage(mFile);
		
		int filecount = 0;
		for (File f : localStorageDirectory.listFiles()){
			if (f.getName().equals("testFileUpload.txt")){
				filecount++;
			}
		}
		
		assertEquals(filecount, 1);
		
		Files.delete(Paths.get(localStorageDirectory.getPath() + "//" + file.getName()));
	}
	
	@Test
	public void multipleFileTest() throws IOException{
		File file = new File(pathToTestFiles + "testFileUpload.txt");
		FileInputStream input = new FileInputStream(file);
		MultipartFile mFile = new MockMultipartFile("file", file.getName(), "text/plain", 
				IOUtils.toByteArray(input));
		
		File file2 = new File(pathToTestFiles + "testFileUpload2.txt");
		FileInputStream input2 = new FileInputStream(file2);
		MultipartFile mFile2 = new MockMultipartFile("file", file2.getName(), "text/plain", 
				IOUtils.toByteArray(input));
		
		new FileManagementSystem().localStorage(mFile);
		new FileManagementSystem().localStorage(mFile2);
		
		int filecount = 0;
		for (File f : localStorageDirectory.listFiles()){
			if (f.getName().equals("testFileUpload.txt") || f.getName().equals("testFileUpload2.txt")){
				filecount++;
			}
		}
		
		assertEquals(filecount, 2);
		

		Files.delete(Paths.get(localStorageDirectory.getPath() + "//" + file.getName()));
		Files.delete(Paths.get(localStorageDirectory.getPath() + "//" + file2.getName()));
	}
	
}
