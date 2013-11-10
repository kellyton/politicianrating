package controllers;

import java.io.File;

import play.mvc.*;

public class MapService extends Controller {
	
	public static Result showMap(double lat, double lng, String fantasia){
		if (lat == 0 || lng == 0){
			return ok ("Mapa não disponível.");
		} else {
			return ok(views.html.mapa.render(lat, lng, fantasia));
		}
	}	
}
