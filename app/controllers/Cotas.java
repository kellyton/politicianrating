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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import models.DeputadoFederal;
import models.Empresa;
import models.TotalTipo;
import models.util.GastoPartido;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class Cotas extends Controller {
	
	/*public static Result getCotas(){
		return ok(views.html.cotas.render());
	}
	
	@Transactional
	public static Result getGraficoCotas(){
		//String partidos[] = { 
    	//		"PR", "PT", "PSB", "PMN", "PMDB", "PSC", "PP", "PROS", "PDT", "PSDB", "PRB", "PV",
    	//		"PPS", "DEM", "PSD", "PCdoB", "SDD", "PTB", "PTdoB", "PRP", "PSOL" };
		
		String query = "SELECT partido, avg(gastoPorDia)" +
				" FROM deputadofederal" +
				" WHERE gastoPorDia > 0" + //exclude people without expenses: everyone agree this situation is an error
				" GROUP BY partido" +
				" ORDER BY 2 ASC";
    	
    	List<Object> resultList = JPA.em().createNativeQuery(query).getResultList();
    	List<GastoPartido> gastosPartidos = new ArrayList<GastoPartido>(21);
    	
    	GastoPartido partido;
    	
		for (Object result : resultList) {
			partido = new GastoPartido();
			
		    Object[] items = (Object[]) result;
		    try {
		    	partido.setPartido((String)items[0]);
		    	partido.setMedia((Double)items[1]);
		    	
		    	gastosPartidos.add(partido);
		    } catch (Exception e){
		    	e.printStackTrace();
		    }
		}
		
		//I prepared the final string here because it was getting error on template
		String gastos = "";
		for (GastoPartido gasto: gastosPartidos){
			gastos += gasto.getValorFormated() + "# ";
		}
		gastos = gastos.substring(0, gastos.length() -2);
		
		//problems with locale
		gastos = gastos.replace(",", ".");
		gastos = gastos.replace("#", ",");
		
		return ok(views.html.grafico_cotas.render(gastosPartidos, gastos));
	}*/
	
/*	@Transactional
	public static Result getCotasBak() {

    	String partidos[] = { 
    			"PR", "PT", "PSB", "PMN", "PMDB", "PSC", "PP", "PROS", "PDT", "PSDB", "PRB", "PV",
    			"PPS", "DEM", "PSD", "PCdoB", "SDD", "PTB", "PTdoB", "PRP", "PSOL" };
    	
    	List<DeputadoFederal> pr = JPA.em().createQuery("FROM DeputadoFederal WHERE partido = :id ORDER BY gastoPorDia")
    			.setParameter("id", "PR")
    			.getResultList();
    	
    	List<DeputadoFederal> pt = JPA.em().createQuery("FROM DeputadoFederal WHERE partido = :id ORDER BY gastoPorDia")
    			.setParameter("id", "PT")
    			.getResultList(); 
    	
    	List<DeputadoFederal> psb = JPA.em().createQuery("FROM DeputadoFederal WHERE partido = :id ORDER BY gastoPorDia")
    			.setParameter("id", "PSB")
    			.getResultList(); 
    	
    	List<DeputadoFederal> pmn = JPA.em().createQuery("FROM DeputadoFederal WHERE partido = :id ORDER BY gastoPorDia")
    			.setParameter("id", "PMN")
    			.getResultList();
    	
    	List<DeputadoFederal> pmdb = JPA.em().createQuery("FROM DeputadoFederal WHERE partido = :id ORDER BY gastoPorDia")
    			.setParameter("id", "PMDB")
    			.getResultList(); 
    	
    	List<DeputadoFederal> psc = JPA.em().createQuery("FROM DeputadoFederal WHERE partido = :id ORDER BY gastoPorDia")
    			.setParameter("id", "PSC")
    			.getResultList(); 
    	
    	List<DeputadoFederal> pp = JPA.em().createQuery("FROM DeputadoFederal WHERE partido = :id ORDER BY gastoPorDia")
    			.setParameter("id", "PP")
    			.getResultList();
    	
    	List<DeputadoFederal> PROS = JPA.em().createQuery("FROM DeputadoFederal WHERE partido = :id ORDER BY gastoPorDia")
    			.setParameter("id", "PROS")
    			.getResultList(); 
    	
    	List<DeputadoFederal> PDT = JPA.em().createQuery("FROM DeputadoFederal WHERE partido = :id ORDER BY gastoPorDia")
    			.setParameter("id", "PDT")
    			.getResultList(); 
    	
    	List<DeputadoFederal> PSDB = JPA.em().createQuery("FROM DeputadoFederal WHERE partido = :id ORDER BY gastoPorDia")
    			.setParameter("id", "PSDB")
    			.getResultList(); 
    	
    	List<DeputadoFederal> PRB = JPA.em().createQuery("FROM DeputadoFederal WHERE partido = :id ORDER BY gastoPorDia")
    			.setParameter("id", "PRB")
    			.getResultList(); 
    	
    	List<DeputadoFederal> PV = JPA.em().createQuery("FROM DeputadoFederal WHERE partido = :id ORDER BY gastoPorDia")
    			.setParameter("id", "PV")
    			.getResultList(); 
    	
    	List<DeputadoFederal> PPS = JPA.em().createQuery("FROM DeputadoFederal WHERE partido = :id ORDER BY gastoPorDia")
    			.setParameter("id", "PPS")
    			.getResultList(); 
    	
    	List<DeputadoFederal> DEM = JPA.em().createQuery("FROM DeputadoFederal WHERE partido = :id ORDER BY gastoPorDia")
    			.setParameter("id", "DEM")
    			.getResultList(); 
    	
    	List<DeputadoFederal> PSD = JPA.em().createQuery("FROM DeputadoFederal WHERE partido = :id ORDER BY gastoPorDia")
    			.setParameter("id", "PSD")
    			.getResultList(); 
    	
    	List<DeputadoFederal> PCdoB = JPA.em().createQuery("FROM DeputadoFederal WHERE partido = :id ORDER BY gastoPorDia")
    			.setParameter("id", "PCdoB")
    			.getResultList(); 
    	
    	List<DeputadoFederal> SDD = JPA.em().createQuery("FROM DeputadoFederal WHERE partido = :id ORDER BY gastoPorDia")
    			.setParameter("id", "SDD")
    			.getResultList(); 
    	
    	List<DeputadoFederal> PTB = JPA.em().createQuery("FROM DeputadoFederal WHERE partido = :id ORDER BY gastoPorDia")
    			.setParameter("id", "PTB")
    			.getResultList(); 
    	
    	List<DeputadoFederal> PTdoB = JPA.em().createQuery("FROM DeputadoFederal WHERE partido = :id ORDER BY gastoPorDia")
    			.setParameter("id", "PTdoB")
    			.getResultList(); 
    	
    	List<DeputadoFederal> PRP = JPA.em().createQuery("FROM DeputadoFederal WHERE partido = :id ORDER BY gastoPorDia")
    			.setParameter("id", "PRP")
    			.getResultList(); 
    	
    	List<DeputadoFederal> PSOL = JPA.em().createQuery("FROM DeputadoFederal WHERE partido = :id ORDER BY gastoPorDia")
    			.setParameter("id", "PSOL")
    			.getResultList(); 
    

		
    	return ok(views.html.cotas.render(pr, pt, psb, pmn, pmdb, psc, pp, PROS, PDT, PSDB, PRB, PV,
    			PPS, DEM, PSD, PCdoB, SDD, PTB, PTdoB, PRP, PSOL));
    			
    			*/
	
		/*		List<Float> medias = new ArrayList<Float>(); 
		//TreeMap<String, Float> partidoValor = new TreeMap<String, Float>();
		for (String partido: partidos){
			Float media = (Float)JPA.em()
				.createNativeQuery("select avg(gastoPorDia) from deputadofederal where partido = :id")
				.setParameter("id", partido)
				.getSingleResult();
			
			medias.add(media);
			//partidoValor.put(partido, media);
		}*/
		
	//}

}
