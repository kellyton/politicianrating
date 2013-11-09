package controllers;

import java.util.List;

import models.DeputadoFederal;
import models.Senador;
import play.mvc.*;
import play.db.jpa.Transactional;

import views.html.*;

public class Application extends Controller {
  
	public static final String version = "20131101 0702";
	
	@Transactional
    public static Result index() {
    	List<DeputadoFederal> dep = Deputados.getPiores();
    	List<Senador> sen = Senadores.getPiores();
        return ok(views.html.index.render(dep, sen));
    }
    
	public static Result version(){
		return ok("Version: " + version);
	}
	
	public static Result teste(){
		return ok(views.html.teste.render());
	}  
	
	public static Result code(){
		return ok("Em breve... (antes de 10/11/2013");
	}  
	
	public static Result docs(){
		return ok("Em breve... (antes de 10/11/2013");
	}  
	
	public static Result license(){
		return ok("Em breve... (antes de 10/11/2013");
	}  
	
}
