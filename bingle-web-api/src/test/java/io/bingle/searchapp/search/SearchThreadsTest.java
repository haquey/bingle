package io.bingle.searchapp.search;

import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.bingle.searchapp.upload.LucenUtility;
import io.bingle.searchapp.discussion.*;
import io.bingle.searchapp.discussion.Thread;
public class SearchThreadsTest{

	LucenUtility lucene = new LucenUtility();

	@Test
	public void testnewThread() throws IOException, ParseException{
		SearchThreads s = new SearchThreads();
		Thread newThread = new Thread("userID", "Name","CSCC01 Assingment 2", "PLs send help", 0, 0, null, 0);
		newThread.setId("testId");

		lucene.deleteThreadIndex(newThread);
		lucene.indexThread(newThread, true);

		assertTrue(s.searchThread("CSCC01").contains(newThread.getTitle()));
		assertTrue(s.searchByContents("help").contains(newThread.getTitle()));
		assertTrue(s.searchByName("Assingment").contains(newThread.getTitle()));

		lucene.deleteThreadIndex(newThread);


	}

	@Test
	public void testmanyThreads() throws IOException, ParseException{
		SearchThreads s = new SearchThreads();
		Set<String> unique = new HashSet<String>();


		Thread newThread1 = new Thread("userID", "Name","CSCC01 Assingment 2", "PLs send help", 0, 0, null, 0);
		Thread newThread2 = new Thread("userID2","Name", "Difficulty of CSCB07?", "How hard can I expected CSCB07 to be? How many hours on avarage do you think I will sepdn on it?", 0, 0, null, 0);
		Thread newThread3 = new Thread("userID", "Name","Study Rooms", "Anyone know where I can find empty study rooms???", 0, 0, null, 0);
		Thread newThread4 = new Thread("userID", "Name","CSCB07 prof?", "Who will be teaching b07 this semester?", 0, 0, null, 0);
		newThread1.setId("testId1");
		newThread2.setId("testId2");
		newThread3.setId("testId3");
		newThread4.setId("testId4");
		
		lucene.deleteThreadIndex(newThread1);
		lucene.deleteThreadIndex(newThread2);
		lucene.deleteThreadIndex(newThread3);
		lucene.deleteThreadIndex(newThread4);

		lucene.indexThread(newThread1, true);
		lucene.indexThread(newThread2, true);
		lucene.indexThread(newThread3, true);
		lucene.indexThread(newThread4, true);

		ArrayList<String> results1 = s.searchThread("CSCB07");

		unique.clear();
		unique.addAll(results1);

		assertTrue(unique.size() == 2);

		ArrayList<String> results2 = s.searchThread("CSC*");

		unique.clear();
		unique.addAll(results2);

		assertTrue(unique.size() == 3);

		ArrayList<String> results3 = s.searchThread("sepdn");
		unique.clear();
		unique.addAll(results3);

		assertTrue(unique.size() == 1);

		lucene.deleteThreadIndex(newThread1);
		lucene.deleteThreadIndex(newThread2);
		lucene.deleteThreadIndex(newThread3);
		lucene.deleteThreadIndex(newThread4);
	}

	@Test
	public void testThreadsDeleteIndexContentOnly() throws IOException, ParseException{
		SearchThreads s = new SearchThreads();
		Set<String> unique = new HashSet<String>();

		Thread newThread1 = new Thread("userID", "Name", "CSCC01 Assingment 2", "PLs send help CSCC01 qq", 0, 0, null, 0);
		Thread newThread2 = new Thread("userID2", "Name","Difficulty of CSCB07?", "How hard can I expected it to be? How many hours on avarage do you think I will sepdn on it?", 0, 0, null, 0);
		Thread newThread3 = new Thread("userID", "Name","Study Rooms", "Anyone know where I can find empty study rooms???", 0, 0, null, 0);
		Thread newThread4 = new Thread("userID", "Name","CSCB07 prof?", "Who will be teaching b07 this semester? And how hard is it?", 0, 0, null, 0);
		newThread1.setId("testId1");
		newThread2.setId("testId2");
		newThread3.setId("testId3");
		newThread4.setId("testId4");
		
		
		lucene.deleteThreadIndex(newThread1);
		lucene.deleteThreadIndex(newThread2);
		lucene.deleteThreadIndex(newThread3);
		lucene.deleteThreadIndex(newThread4);

		lucene.indexThread(newThread1, true);
		lucene.indexThread(newThread3, true);
		lucene.indexThread(newThread2, true);
		lucene.indexThread(newThread4, true);


		ArrayList<String> results1 = s.searchByContents("CSCB07");

		unique.clear();
		unique.addAll(results1);

		assertTrue(unique.size() == 2);

		ArrayList<String> results2 = s.searchByContents("MAT*");

		unique.clear();
		unique.addAll(results2);

		assertTrue(unique.size() == 0);

		ArrayList<String> results3 = s.searchByContents("hard");

		unique.clear();
		unique.addAll(results3);

		assertTrue(unique.size() == 2);

		lucene.deleteThreadIndex(newThread1);
		lucene.deleteThreadIndex(newThread2);
		lucene.deleteThreadIndex(newThread3);
		lucene.deleteThreadIndex(newThread4);
	}

	@Test
	public void testThreadsAndPosts() throws IOException, ParseException{
		Thread newThread = new Thread("userID", "Name", "CSCC01 Assingment 2", "A Thread", 0, 5, null, 0);
		newThread.setId("threadId1");
		Post postFour = new Post("userID", "", "tID", "-1", "Im also in c01.", 0, new Date().toGMTString());
		Post postOne = new Post("userID", "", "tID", "-1", "It was a cool assignment", 0, new Date().toGMTString());
		Post postTwo = new Post("userID", "", "tID", "-1", "qq", 2, new Date().toGMTString());
		Post postThree = new Post("userID", "", "tID", "-1", "gg wp c01!",  1, new Date().toGMTString());
		postOne.setId("postId1");
		postTwo.setId("postId2");
		postThree.setId("postId3");
		postFour.setId("postId4");
		List<Post> posts = Arrays.asList(postOne, postTwo, postThree, postFour);

		SearchThreads s = new SearchThreads();
		Set<String> unique = new HashSet<String>();

		lucene.indexThreadAndPosts(newThread, posts);

		ArrayList<String> results1 = s.searchThread("c01");

		unique.clear();
		unique.addAll(results1);

		assertEquals(unique.size(), 1);

		ArrayList<String> results2 = s.searchThread("qq");


		unique.clear();
		unique.addAll(results2);

		//assertEquals(unique.size(), 1);

		ArrayList<String> results3 = s.searchThread("NOT_IN_ANY_POST");


		unique.clear();
		unique.addAll(results3);

		assertTrue(unique.size() == 0);

		lucene.deleteThreadIndex(newThread);

	}

	@Test
	public void testThreadsDuplicateTitle() throws IOException, ParseException{
		Thread newThread1 = new Thread("userID","Name", "CSCC01 Assignment 2", "This has content from the course", 0, 5, null, 0);
		Thread newThread2 = new Thread("userID2","Name", "CSCC01 Assignment 2", "This has content from the class", 0, 5, null, 0);
		newThread1.setId("testId1");
		newThread2.setId("testId2");
		
		File f = new File("LocalStorage//CSCC01 Assignment 2");
		f.delete();
		f = new File("LocalStorage//CSCC01 Assingment 2_1");
		f.delete();
		SearchThreads s = new SearchThreads();
		Set<String> unique = new HashSet<String>();

		lucene.indexThread(newThread1, false);
		lucene.indexThread(newThread2, false);

		ArrayList<String> results1 = s.searchThread("course");

		unique.clear();
		unique.addAll(results1);

		assertTrue(unique.size() == 1);

		ArrayList<String> results2 = s.searchThread("class");


		unique.clear();
		unique.addAll(results2);

		assertTrue(unique.size() == 1);

		ArrayList<String> results3 = s.searchThread("content");


		unique.clear();
		unique.addAll(results3);

		assertTrue(unique.size() == 2);

		lucene.deleteThreadIndex(newThread1);
		lucene.deleteThreadIndex(newThread2);



	}
}