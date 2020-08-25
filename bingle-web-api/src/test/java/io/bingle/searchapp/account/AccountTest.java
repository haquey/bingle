package io.bingle.searchapp.account;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class AccountTest {
    Account testAdmin = new Instructor("testUser", "testPassword", "firstName", "lastName");
	Account testStudent = new Student("kareemM", "passwurd", "Kareem", "Mohamed");
	
    @Test
    public void authenticateTest(){
        assertTrue(testAdmin.authenticate("testPassword"));
        assertFalse(testAdmin.authenticate("wrongPassword"));
    }
    @Test
    public void authenticateAfterChangeTest(){
        testAdmin.setPassword("newPassword");
        
        assertTrue(testAdmin.authenticate("newPassword"));
        assertFalse(testAdmin.authenticate("testPassword"));
        
        testAdmin.setPassword("testPassword");
    }
    @Test
    public void updateInfoTest(){
    	testStudent.setFirstName("Yasir");
    	testStudent.setLastName("Haque");
    	testStudent.setUsername("haqueya1");
    	testStudent.setPassword("lulialmosttypedinmyrealpass");
    	
    	assertEquals(testStudent.getFirstName(), "Yasir");
    	assertEquals(testStudent.getLastName(), "Haque");
    	assertEquals(testStudent.getUsername(), "haqueya1");
    	assertEquals(testStudent.getPassword(), "lulialmosttypedinmyrealpass");
    }
    @Test
    public void adminAccessTest(){
    	assertTrue(testAdmin.isAdmin());
    	assertFalse(testAdmin.isStudent());
    	
    	assertFalse(testStudent.isAdmin());
    	assertTrue(testStudent.isStudent());
    }
    @Test
    public void addBinglePointsTest(){
    	assertEquals(((Student) testStudent).getBinglePoints(), 0);
    	((Student) testStudent).addBinglePoints(5);
    	((Student) testStudent).addBinglePoints(1);
    	assertEquals(((Student) testStudent).getBinglePoints(), 6);
    }
    @Test
    public void negativePointsTest(){
    	((Student) testStudent).addBinglePoints(-1000);
    	assertTrue(((Student) testStudent).getBinglePoints() < 0);
    }
    @Test
    public void ListAdminsTest(){
    	AccountController ac = new AccountController();
    	InstructorRepository mockIR = mock(InstructorRepository.class);
        ac.setAdminRepo(mockIR);
        List<Instructor> mockInstructors = new ArrayList<Instructor>();
        when(mockIR.findAll()).thenReturn(mockInstructors);
        //AccountController ac = new AccountController();
        assertEquals(ac.listAllAdmins(), mockInstructors);
    }
    @Test
    public void ListStudentsTest(){
    	AccountController ac = new AccountController();
    	StudentRepository mockSR = mock(StudentRepository.class);
        ac.setStudentRepo(mockSR);
        List<Student> mockStudents = new ArrayList<Student>();
        when(mockSR.findAll()).thenReturn(mockStudents);
        //AccountController ac = new AccountController();
        assertEquals(ac.listAllUsers(), mockStudents);
    }
}
