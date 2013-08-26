package controllers;

import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class Senadores extends Controller {

	@Transactional
    public static Result getAllData() {
		
		getProfileData();
		//Get profile data from webservices
		
		//Get spenditure data from files
		
		return TODO;
	}

	private static void getProfileData() {
		// TODO Auto-generated method stub
		
	}
	
}
