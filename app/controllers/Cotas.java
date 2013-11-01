package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import models.DeputadoFederal;
import models.TotalTipo;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class Cotas extends Controller {
	
	@Transactional
	public static Result getCotas() {

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
		
    	return ok(views.html.cotas.render(pr, pt, psb, pmn, pmdb, psc, pp, PROS, PDT, PSDB, PRB, PV,
    			PPS, DEM, PSD, PCdoB, SDD, PTB, PTdoB, PRP, PSOL));
		
	}

}
