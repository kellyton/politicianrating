package controllers;

import static play.data.Form.form;

import java.util.LinkedList;
import java.util.List;

import models.DeputadoFederal;
import models.TotalData;
import models.TotalTipo;
import play.data.DynamicForm;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class Deputados extends Controller {

	@Transactional
    public static Result show(String id) {		
		DeputadoFederal deputado;
		List<TotalTipo> totalTipo;
		List<TotalData> totalData;
		List<Object> resultList;
		
		System.out.println("ID = " + id);
		
		//Traz o deputado
		try {
			deputado = (DeputadoFederal)JPA.em().createQuery("FROM DeputadoFederal where ideCadastro = :id")
					.setParameter("id", id).getSingleResult();
		} catch (Exception e){
			e.printStackTrace();
			return badRequest("Error getting Deputado  " + id + ". Message: " + e.getMessage() + " - " + e.getLocalizedMessage());
		}
		
		//Traz os resultados agrupados por tipo de gasto
		try {
			// urzeni (o mais ladrao) eh 616
			String nuCarteiraParlamentar = deputado.getMatricula();
		
			String query = 
					"select txtCNPJCPF, txtDescricao, txtDescricaoEspecificacao," + 
					" sum(vlrDocumento) as TotalGasto" +
					" from deputadofederalgasto" + 
					" where nuCarteiraParlamentar = :id" + 
					" group by txtDescricaoEspecificacao order by 2,3,4 desc";
			
			resultList = JPA.em().createNativeQuery(query)
					.setParameter("id", nuCarteiraParlamentar).getResultList();
			
			totalTipo = new LinkedList<TotalTipo>();
			TotalTipo tt;
			
			for (Object result : resultList) {
				tt = new TotalTipo();
				
			    Object[] items = (Object[]) result;
			    tt.setCnpj((String)items[0]);
			    tt.setDescricao((String)items[1]);
			    tt.setDetalhe((String)items[2]);
			    tt.setTotalGasto((Double)items[3]);
			    
			    totalTipo.add(tt);
			}  
			
		} catch (Exception e){
			e.printStackTrace();
			return badRequest("Error getting Deputado  " + id + ". Message: " + e.getMessage() + " - " + e.getLocalizedMessage());
		}
		
		//Traz os resultados por data
		try {
			// urzeni (o mais ladrao) eh 616
			String nuCarteiraParlamentar = deputado.getMatricula();
		
			String query = 
					" select numAno, numMes," +
					" sum(vlrDocumento) as TotalGasto" +
					" from deputadofederalgasto" +
					" where nuCarteiraParlamentar = :id" +
					" group by numAno,numMes order by 1,2";
			
			resultList = JPA.em().createNativeQuery(query)
					.setParameter("id", nuCarteiraParlamentar).getResultList();
			
			totalData = new LinkedList<TotalData>();
			TotalData td;
			
			for (Object result : resultList) {
				td = new TotalData();
				
			    Object[] items = (Object[]) result;    
			    td.setAno((Integer)items[0]);
			    td.setMes((Integer)items[1]);
			    td.setTotalGasto((Double)items[2]);
			    
			    totalData.add(td);
			} 
			
		} catch (Exception e){
			e.printStackTrace();
			return badRequest("Error getting Deputado  " + id + ". Message: " + e.getMessage() + " - " + e.getLocalizedMessage());
		}
    	
    	return ok(views.html.depfederaldetalhe.render(deputado, totalTipo, totalData));
    }
	
}
