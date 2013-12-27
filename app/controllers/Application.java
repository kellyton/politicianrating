/* Copyright 2013 de Kellyton Brito. Este arquivo é parte 
* do programa MeuCongressoNacional.com . O MeuCongressoNacional.com 
* é um software livre; você pode redistribuí-lo e/ou modificá-lo 
* dentro dos termos da GNU Affero General Public License como 
* publicada pela Fundação do Software Livre (FSF) na versão 3 
* da Licença. Este programa é distribuído na esperança que possa 
* ser útil, mas SEM NENHUMA GARANTIA; sem uma garantia implícita 
* de ADEQUAÇÃO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. Veja 
* a licença para maiores detalhes, disponível em 
* meucongressonacional.com/license. Você deve ter recebido uma cópia 
* da GNU Affero General Public License, sob o título "LICENCA.txt", 
* junto com este programa, se não, acesse http://www.gnu.org/licenses/
**/

package controllers;

import static play.data.Form.form;

import java.util.List;

import models.DeputadoFederal;
import models.Email;
import models.Senador;
import play.mvc.*;
import play.data.DynamicForm;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import views.html.*;

import java.util.regex.*; 


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
		return redirect("https://github.com/misawsneto/politicianrating");
	}  
	
	public static Result docs(){
		return ok(views.html.sobre.render());
	}  
	
	public static Result license(){
		//return redirect("http://www.gnu.org/licenses/agpl-3.0.html");
		return redirect(controllers.routes.FileService.getFile("LICENCA.txt"));
	}
	
	public static Result sobre(){
		return ok(views.html.sobre.render());
	} 
	
	public static Result contato(){
		return TODO;
	}
	
	@Transactional
	public static Result cadastraEmail(){
		DynamicForm dynamicForm = form().bindFromRequest();
		String email = dynamicForm.get("email");
		
		//valida o e-mail  
	    Pattern pattern = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$");   
	    Matcher matcher = pattern.matcher(email);   
	    if (matcher.find()){//ok
	    	Email e = new Email();
	    	e.setEmail(email);
	    	try {
	    		JPA.em().persist(e);
	    		flash("message", "E-mail cadastrado com sucesso! =D");
	    	} catch (Exception ex){
	    		flash("message", "Erro cadastrando e-mail. Ele já não foi cadastrado? =/");
	    	}
	    	return index();
	    } else {//falhou
	    	flash("message", "Erro na formatação. Isso é MESMO um e-mail? =D");
	    	return index();
	    }
	}
}
