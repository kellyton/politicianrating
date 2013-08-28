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

public class Senadores extends Controller {

	//20 seconds timeout
	private static final Long timeout = 20000L;
	
	public static final String hostSenado = "http://legis.senado.gov.br/dadosabertos";
	public static final String pathGetSenadores = "/senador/lista/atual";
	
	@Transactional
	public static List<Senador> getMelhores(){
    	List<Senador> senMelhores = JPA.em()
    			.createQuery("FROM Senador ORDER BY gastopordia ASC")
    			.setMaxResults(5)
    			.getResultList();
    	return senMelhores;
	}
	
	@Transactional
	public static List<Senador> getPiores(){
		List<Senador> senPiores = JPA.em()
    			.createQuery("FROM Senador ORDER BY gastopordia DESC")
    			.setMaxResults(5)
    			.getResultList();
		return senPiores;
	}
	
	@Transactional
	public static Result show(String id){
		Senador senador;
		List<TotalTipo> totalTipo;
		List<TotalData> totalData;
		List<Object> resultList;
		
		System.out.println("ID = " + id);
		
		//Traz o senador
		try {
			senador = (Senador)JPA.em().createQuery("FROM Senador where codParlamentar = :id")
					.setParameter("id", id).getSingleResult();
		} catch (Exception e){
			e.printStackTrace();
			return badRequest("Error getting Senador " + id + ". Message: " + e.getMessage() + " - " + e.getLocalizedMessage());
		}
		
		//Traz os resultados agrupados por tipo de gasto
		try {
			String query = 
					"select cnpj_cpf, tipo_depesa, detalhamento," + 
					" sum(valor_reembolsado) as TotalGasto" +
					" from SenadorGasto" + 
					" where senador = :id" + 
					" group by tipo_depesa, detalhamento order by 2,3,4 desc";
			
			resultList = JPA.em().createNativeQuery(query)
					.setParameter("id", senador.getNomeParlamentar()).getResultList();
			
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
			return badRequest("Error getting Senador  " + senador.getNomeParlamentar() + ". Message: " + e.getMessage() + " - " + e.getLocalizedMessage());
		}
		
		//Traz os resultados por data
		try {
			String query = 
					" select ano, mes," +
					" sum(valor_reembolsado) as TotalGasto" +
					" from SenadorGasto" +
					" where senador = :id" +
					" group by ano, mes order by 1,2";
			
			resultList = JPA.em().createNativeQuery(query)
					.setParameter("id", senador.getNomeParlamentar()).getResultList();
			
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
    	
    	return ok(views.html.depfederaldetalhe.render(senador, totalTipo, totalData));
	}
	
	@Transactional
    public static Result senador() {
		
    	List<Senador> senList = JPA.em().createQuery("FROM Senador ORDER BY nomeParlamentar").getResultList();

    	// TODO Probably get once and then get 5 best and worst is faster
    	List<Senador> senMelhores = JPA.em().createQuery("FROM Senador ORDER BY gastopordia asc").setMaxResults(5).getResultList();
    	
    	List<Senador> senPiores = JPA.em().createQuery("FROM Senador ORDER BY gastopordia desc").setMaxResults(5).getResultList();
    	
    	return ok(views.html.depfederal.render(null, null, null, senList, senMelhores, senPiores));
    }
	
	@Transactional
    public static Result getAllData() {
		
		
		try {
			getProfileData();
			getExpensesData();
			getTotalsData();
			return ok("Success");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return badRequest(e.getLocalizedMessage());
			
		}
		
	}

	private static void getProfileData() {
		String url = hostSenado + pathGetSenadores;
		ArrayList<Senador> senadores = new ArrayList<Senador>();
		
		try {
			
			Promise<WS.Response> result = WS.url(url).get();
    		InputStream is = new ByteArrayInputStream(result.get(timeout).asByteArray());
			
    		System.out.println("Pegou o Stream. Vai processar");
    		
    		SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
		
			SenadorXMLHandler dhandler = new SenadorXMLHandler(senadores);
			saxParser.parse(is, dhandler);
			System.out.println("Terminou de parsear");
			
			//For each deputado, get details and insert
			for(Senador senador: senadores){
				try {
					JPA.em().persist(senador);
				} catch (Exception e) {
					System.out.println("Erro salvando " + senador.getNomeParlamentar() + ". " + e.getMessage());
					e.printStackTrace();
				}
			}
		} catch (Exception e){
			System.out.println("Erro salvando senadores: " + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	private static void getExpensesData() throws IOException {
		
		int semData = 0;
		
		String [] filenames = {
				"./data/CotaSenado2009.csv",
				"./data/CotaSenado2010.csv",
				"./data/CotaSenado2011.csv",
				"./data/CotaSenado2012.csv", 
				"./data/CotaSenado2013.csv"};
		
		BufferedReader br;
		String line;
		String fields[];
		String csvSplitBy = "\";\"";
		String d;
		
		LinkedList<SenadorGasto> gastos;
		SenadorGasto gasto;
		
		//For each file
		for (int i = 0; i < filenames.length; i++){
			
			System.out.println("Processing file: " + filenames[i]);
					
			br = new BufferedReader(new FileReader(filenames[i]));
			
			//Jump 2 first lines: header
			line = br.readLine();
			line = br.readLine();
			while ((line = br.readLine()) != null) {
				gasto = new SenadorGasto();
				
				if (line.length() < 5) continue;
				
				//remove first and last "
				line = line.substring(1, line.length()-1);
				
				fields = line.split(csvSplitBy);
				if (fields.length < 10) continue; 

				gasto.setAno(Integer.valueOf(fields[0]));
				gasto.setMes(Integer.valueOf(fields[1]));
				gasto.setSenador(fields[2]);
				gasto.setTipo_depesa(fields[3]);
				gasto.setCnpj_cpf(fields[4]);
				gasto.setFornecedor(fields[5]);
				gasto.setDocumento(fields[6]);
				d = fields[7];
				if (d!=null && d.trim().length() > 0){
					try {
						gasto.setData(new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH).parse(d));
					} catch (ParseException e) {
						System.out.println("Error getting date from expenditure of Senador: " + gasto.getSenador()+ ". Error: " + e.getMessage() + ". Setting data to 01/01/2009");
						//All missing dates are from 2009
						try {
							gasto.setData(new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH).parse("01/01/2009"));
						} catch (ParseException e1) {
							System.out.println();
						}
					}
				} else {
					++semData;
				}
				
				if (fields[8].length() > 255){
					fields[8] = fields[8].substring(0, 254);
				}
				gasto.setDetalhamento(fields[8]);
				gasto.setValor_reembolsado(Float.valueOf(fields[9].replace(',', '.')));
				
				JPA.em().persist(gasto);
				
			}
			System.out.println("Sem data: " + semData);
			semData = 0;
		}
	}
	
	/**
	 * Dados de 2010 até a última data disponível
	 * TODO: Calcular os dias corretamente, com os afastamentos
	 * @throws Exception 
	 */
	private static void getTotalsData() throws Exception {

		
    	//Pega o último dia com registro
		Date mostRecentDate = (Date)JPA.em()
    			.createNativeQuery("Select max(data) FROM SenadorGasto WHERE data < NOW()").getSingleResult();
    	
    	
    	Date olderDate;
    	
    	try {
			olderDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse("2010-01-01");
		} catch (ParseException e) {
			throw new Exception("Error getting mostRecentDate. " + e.getLocalizedMessage());
		}
		   	
		int days = Days.daysBetween(new DateTime(olderDate), new DateTime(mostRecentDate)).getDays();
		
		List<Senador> senadores = JPA.em().createQuery("FROM Senador").getResultList();
		
		Double gasto;
		for (Senador senador: senadores){
			senador.setDiasTrabalhados(days);
    			
    		Object o = JPA.em()
    				
    				.createNativeQuery("select sum(valor_reembolsado) from SenadorGasto where senador = :nome")
    				.setParameter("nome", senador.getNomeParlamentar())
    				.getSingleResult();
    		
    		if (o != null){
    			gasto = (Double)o; 
    			senador.setGastoTotal(gasto);
    		
    			senador.setGastoPorDia(gasto/days);
    		}
    		JPA.em().persist(senador);
		}
	
		/**
		 * Calcula o índice do candidato
		 * 100*gasto/max
		 * Ou seja: o candidato que gastou MAIS tem valor 0, o candidato que gastou NADA tem valor 100 
		 */
		
		Double maxGasto = (Double)JPA.em().createNativeQuery("Select max(gastoPorDia) FROM Senador").getSingleResult();
		Double senTempIndice;
	
		for (Senador sen: senadores){
			gasto = sen.getGastoPorDia();
			
			senTempIndice = (100 * gasto / maxGasto);
			
			sen.setIndice(senTempIndice.intValue());
			
			JPA.em().persist(sen);
		}
	}
	
}

class SenadorXMLHandler extends DefaultHandler{
	private boolean codParlamentar;
	private boolean nome;
	private boolean nomeParlamentar;
	private boolean sexo;
	private boolean uf;
	private boolean partido;
	private boolean fone;
	private boolean email;
	private boolean nascimento;
	private boolean photoURL;
	
	ArrayList<Senador> senadores;
	Senador senador;
	
	public SenadorXMLHandler (ArrayList<Senador> senadores){
		this.senadores = senadores;
	}
	
	public void startElement(String uri, String localName,String qName, 
            Attributes attributes) throws SAXException {
		
		if (qName.equalsIgnoreCase("CodigoParlamentar")) {
			codParlamentar = true;
		} else if (qName.equalsIgnoreCase("nomeParlamentar")) {
			nomeParlamentar = true;
		} else if (qName.equalsIgnoreCase("NomeCompleto")) {
			nome = true;
		} else if (qName.equalsIgnoreCase("Sexo")) {
			sexo = true;
		} else if (qName.equalsIgnoreCase("Foto")) {
			photoURL = true;
		} else if (qName.equalsIgnoreCase("EnderecoEletronico")) {
			email = true;
		} else if (qName.equalsIgnoreCase("DataNascimento")) {
			nascimento = true;
		} else if (qName.equalsIgnoreCase("TelefoneParlamentar")) {
			fone = true;
		} else if (qName.equalsIgnoreCase("SiglaPartido")) {
			partido = true;
		} else if (qName.equalsIgnoreCase("SiglaUf")) {
			uf = true;
		}
	}
	public void endElement(String uri, String localName,
			String qName) throws SAXException {
	}
	
	public void characters(char ch[], int start, int length) throws SAXException {
		if (codParlamentar) {
			//Começando novo deputado
			senador = new Senador();
			String s = new String(ch, start, length);
			senador.setCodParlamentar(s);
			codParlamentar = false;
		}
		if (nome) {
			String s = new String(ch, start, length);
			senador.setNome(s);
			nome = false;
		}
		if (nomeParlamentar) {
			String s = new String(ch, start, length);
			senador.setNomeParlamentar(s);
			nomeParlamentar = false;
		}
		if (sexo) {
			String s = new String(ch, start, length);
			senador.setSexo(s);
			sexo = false;
		}
		if (partido) {
			String s = new String(ch, start, length);
			senador.setPartido(s);
			partido = false;
		}
		if (fone) {
			String s = new String(ch, start, length);
			senador.setFone(s);
			fone = false;
		}
		if (email) {
			String s = new String(ch, start, length);
			senador.setEmail(s);
			email = false;
		}
		if (nascimento) {
			String s = new String(ch, start, length);
			
			try {
				Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(s);	
				senador.setNascimento(d);
			} catch (Exception e){
				System.out.println("Error getting born date of: " + senador.getNomeParlamentar() + " - " + e.getMessage());
			}
			nascimento = false;
		}
		if (photoURL) {
			String s = new String(ch, start, length);
			senador.setPhotoURL(s);
			photoURL = false;
		}
		if (uf) {
			String s = new String(ch, start, length);
			senador.setUf(s);
			uf = false;
			//Acabou o senador. Adicionar na lista
			senadores.add(senador);
			senador = new Senador();
		}
 
	}
}
