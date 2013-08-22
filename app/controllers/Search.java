package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.List;

import models.DeputadoFederal;
import views.html.*;
import play.data.DynamicForm;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.*;

public class Search extends Controller {
  
	@Transactional
	public static Result search(){

		List<DeputadoFederal> depList = new ArrayList<DeputadoFederal>();
		
		DynamicForm dynamicForm = form().bindFromRequest();
		String words = dynamicForm.get("q");
		
		
		if (words != null && words.trim().length() > 0){
			
			String query = "FROM DeputadoFederal WHERE (";
			String []parts = words.split(" ");
			
			for (int i = 0; i < parts.length; i++){
				query += " UPPER(nome) like UPPER('%" + parts[i] + "%') OR";
			}
			
			query = query.substring(0, query.length() - 3);
			query += ") ORDER BY nomeParlamentar";
			
			depList = (List<DeputadoFederal>)JPA.em().createQuery(query).getResultList();
		
		} else {
			depList = (List<DeputadoFederal>)JPA.em().createQuery("FROM DeputadoFederal").getResultList();
		}
		
		/*if (words != null && words.trim().length() > 0){
		
			String query = "SELECT * FROM DeputadoFederal WHERE (";
			
			String []parts = words.split(" ");
			
			for (int i = 0; i < parts.length; i++){
				query += " UPPER(nome) like UPPER('%" + parts[i] + "%') OR";
			}
			
			query = query.substring(0, query.length() - 3);
			query += ") ORDER BY nomeParlamentar";
			
			depList = (List<DeputadoFederal>)JPA.em().createNativeQuery(query).getResultList();
		
		} else {
			depList = (List<DeputadoFederal>)JPA.em().createQuery("FROM DeputadoFederal").getResultList();
		}*/
		
    	// TODO Probably get once and then get 5 best and worst is faster
    	List<DeputadoFederal> depMelhores = JPA.em().createQuery("FROM DeputadoFederal ORDER BY gastopordia").setMaxResults(5).getResultList();
    	List<DeputadoFederal> depPiores = JPA.em().createQuery("FROM DeputadoFederal ORDER BY gastopordia desc").setMaxResults(5).getResultList();
    	
    	return ok(views.html.depfederal.render(depList, depMelhores, depPiores));

	}
    
}
