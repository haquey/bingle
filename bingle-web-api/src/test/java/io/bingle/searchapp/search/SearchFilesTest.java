package io.bingle.searchapp.search;

import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.io.IOException;
import java.util.ArrayList;

import io.bingle.searchapp.upload.LucenUtility;

public class SearchFilesTest{

    @Test
    public void testExistingFile() throws IOException, ParseException{
    	LucenUtility lucene = mock(LucenUtility.class);
        SearchFiles s = new SearchFiles();
        s.setLucene(lucene);
        ResultEntry mockRE = new ResultEntry("fakeTitle", "fakePath", 100000, "fakeSnippet", "faketag1;tag2", "fakeUsername");
        ArrayList<ResultEntry> mockEntries = new ArrayList<ResultEntry>();
        mockEntries.add(mockRE);
        when(lucene.searchFileByContents(any())).thenReturn(mockEntries);
        when(lucene.searchAllFields(any())).thenReturn(mockEntries);
        when(lucene.searchFileByTags(any())).thenReturn(mockEntries);
        when(lucene.searchFileByName(any())).thenReturn(mockEntries);
        assertEquals(mockEntries, s.searchFile("query", "Content"));
        assertEquals(mockEntries, s.searchFile("query", "Title"));
        assertEquals(mockEntries, s.searchFile("query", "Tag"));
        assertEquals(mockEntries, s.searchFile("query", "All"));
    }
}