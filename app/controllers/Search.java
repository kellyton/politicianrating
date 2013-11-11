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

import java.util.ArrayList;
import java.util.List;

import models.DeputadoFederal;
import models.Senador;
import views.html.*;
import play.data.DynamicForm;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.*;

public class Search extends Controller {
  
	@Transactional
	public static Result search(){
		DynamicForm dynamicForm = form().bindFromRequest();
		String words = dynamicForm.get("q");
		String queryBegin;
		String queryMiddle = "";
		String queryEnd = "";

		// Deputados
		List<DeputadoFederal> depList = new ArrayList<DeputadoFederal>();
		
		if (words != null && words.trim().length() > 0){
			
			queryBegin = "FROM DeputadoFederal WHERE (";
			String []parts = words.split(" ");
			
			for (int i = 0; i < parts.length; i++){
				queryMiddle += " UPPER(nome) like UPPER('%" + parts[i] + "%') OR";
				queryMiddle += " UPPER(nomeParlamentar) like UPPER('%" + parts[i] + "%') OR";
			}
			
			queryMiddle = queryMiddle.substring(0, queryMiddle.length() - 3);
			queryEnd += ") ORDER BY nomeParlamentar";
			
			String query = queryBegin + queryMiddle + queryEnd;
			
			depList = (List<DeputadoFederal>)JPA.em().createQuery(query).getResultList();
		
		} else {
			depList = (List<DeputadoFederal>)JPA.em().createQuery("FROM DeputadoFederal").getResultList();
		}
		
    	// TODO Probably get once and then get 5 best and worst is faster
    	List<DeputadoFederal> depMelhores = JPA.em().createQuery("FROM DeputadoFederal ORDER BY gastopordia").setMaxResults(5).getResultList();
    	List<DeputadoFederal> depPiores = JPA.em().createQuery("FROM DeputadoFederal ORDER BY gastopordia desc").setMaxResults(5).getResultList();
    	
    	// Senadores
    	List<Senador> senList = new ArrayList<Senador>();
		
		if (words != null && words.trim().length() > 0){
			
			queryBegin = "FROM Senador WHERE (";
			String query = queryBegin + queryMiddle + queryEnd;
			
			senList = (List<Senador>)JPA.em().createQuery(query).getResultList();
		
		} else {
			senList = (List<Senador>)JPA.em().createQuery("FROM Senador").getResultList();
		}
		
    	// TODO Probably get once and then get 5 best and worst is faster
    	List<Senador> senMelhores = JPA.em().createQuery("FROM Senador ORDER BY gastopordia").setMaxResults(5).getResultList();
    	List<Senador> senPiores = JPA.em().createQuery("FROM Senador ORDER BY gastopordia desc").setMaxResults(5).getResultList();
    	
    	////
    	
    	return ok(views.html.depfederal.render(depList, depMelhores, depPiores, senList, senMelhores, senPiores));
	}
    
}
