package controllers;

import static play.libs.Json.toJson;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import models.DeputadoFederal;
import models.DeputadoFederalExercicio;
import models.DeputadoFederalGasto;
import models.Senador;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


import play.libs.F.Promise;
import play.libs.F;
import play.libs.WS;
import play.mvc.*;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import views.html.*;

public class Application extends Controller {
  
	public static final String version = "20131027 1515";
	
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
	
}
