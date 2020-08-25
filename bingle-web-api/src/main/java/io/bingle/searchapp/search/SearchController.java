package io.bingle.searchapp.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;

import io.bingle.searchapp.UserService;
import io.bingle.searchapp.account.Account;
import io.bingle.searchapp.upload.FileManagementSystem;
import io.bingle.searchapp.upload.LucenUtility;

@RestController
public class SearchController {

	@Autowired
	private UserService user;

    @GetMapping(value = "/search/{queryString}/{option}")
    @ResponseBody
    public String handleSearch(@PathVariable("queryString") String queryString,
        @PathVariable("option") String option) {
    	
    	SearchFiles fileSearch = new SearchFiles();


    	try {
			ArrayList<ResultEntry> resultsName = fileSearch.searchFile(queryString, option);

			// More searching to come
			
			Account account = user.getAccount();
			System.err.println(resultsName.toString());
			if (account != null && resultsName.size() > 0)
			{
				for(ResultEntry r : resultsName)
				{
						for (String tag : r.getTags().split(";"))
						{
							account.setTagsSearched(tag);
						}
				}
			}
					
			user.saveAccount();
			
			return (new Gson().toJson(resultsName));
			
		} catch (ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return "index";
    }
}
