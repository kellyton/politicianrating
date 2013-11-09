package controllers;

import java.io.File;

import play.mvc.*;

public class FileService extends Controller {

	public static String path = "./public/dynamicfiles/";
	
	public static Result getFile(String file){
        //File myfile = new File (System.getenv("PWD") + path + file);
		File myfile = new File (path + file);
        return ok(myfile);
	}
	
}
