package controllers;

/**
 * TODO: Fazer um cálculo decente de quantos dias ele realmente estava em exercício.
 * Tem que procurar onde achar esse dado
 */
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import models.DeputadoFederal;
import models.Senador;
import models.SenadorGasto;
import models.TotalData;
import models.TotalTipo;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.WS;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;

public class Estados extends Controller {

	//20 seconds timeout
	private static final Long timeout = 20000L;
	
	public static final String hostSenado = "http://legis.senado.gov.br/dadosabertos";
	public static final String pathGetSenadores = "/senador/lista/atual";
	
	@SuppressWarnings("unchecked")
	@Transactional
    public static Result estado(String id) {
		
		List<DeputadoFederal> depList = JPA.em()
				.createQuery("FROM DeputadoFederal WHERE uf = :id ORDER BY nomeParlamentar")
				.setParameter("id", id)
				.getResultList();
    	
		List<Senador> senList = JPA.em()
				.createQuery("FROM Senador WHERE uf = :id ORDER BY nomeParlamentar")
				.setParameter("id", id)
				.getResultList();  	

    	// TODO Probably get once and then get 5 best and worst is faster
    	List<DeputadoFederal> depMelhores = JPA.em()
    			.createQuery("FROM DeputadoFederal WHERE uf = :id ORDER BY gastopordia ASC")
				.setParameter("id", id)
    			.setMaxResults(5)
    			.getResultList();	
    	List<DeputadoFederal> depPiores = JPA.em()
    			.createQuery("FROM DeputadoFederal WHERE uf = :id ORDER BY gastopordia DESC")
				.setParameter("id", id)
    			.setMaxResults(5)
    			.getResultList();	
    	List<Senador> senMelhores = JPA.em()
    			.createQuery("FROM Senador WHERE uf = :id ORDER BY gastopordia ASC")
    			.setParameter("id", id)
    			.setMaxResults(5)
    			.getResultList();	
    	List<Senador> senPiores = JPA.em()
    			.createQuery("FROM Senador WHERE uf = :id ORDER BY gastopordia DESC")
    			.setParameter("id", id)
    			.setMaxResults(5)
    			.getResultList();
    	
    	return ok(views.html.depfederal.render(depList, depMelhores, depPiores, senList, senMelhores, senPiores));
    }
}
