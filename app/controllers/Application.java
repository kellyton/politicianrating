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
  
	private static final Long timeout = 20000L;
	
	public static final String hostCamaraFederal = "http://www.camara.gov.br";
	public static final String pathObterDeputados = "/SitCamaraWS/Deputados.asmx/ObterDeputados";
	// http://www.camara.gov.br/SitCamaraWS/Deputados.asmx/ObterDetalhesDeputados?ideCadastro=XXXX&numLegislatura=
	public static final String pathObterDeputadosDetails = "/SitCamaraWS/Deputados.asmx/ObterDetalhesDeputados";
	
	public static final String pathObterDeputadosGastos = "./data/AnoAtual.xml";
	
    public static Result index() {
        return ok(views.html.index.render());
    }
    
    public static Result estado() {
    	return ok(views.html.estado.render());
    }
    
    public static Result estatistica() {
    	return ok(views.html.estatistica.render());
    }
    
    /**
     * Dados de 2012 e 2013
     * @return
     */
    @Transactional
    public static Result calculaGastoMedio() {
    	//Pega o último dia com registro
    	String mostRecent = (String)JPA.em().createNativeQuery("Select max(datEmissao) FROM DeputadoFederalGasto").getSingleResult();
    	Date mostRecentDate;
    	Date olderDate;
    	
    	try {
			mostRecentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(mostRecent);
			olderDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse("2012-01-01");
		} catch (ParseException e) {
			return badRequest("Error getting mostRecentDate. " + e.getLocalizedMessage());
		}
    	
    	List<DeputadoFederal> depList = JPA.em().createQuery("FROM DeputadoFederal").getResultList();
    	
    	/**
    	 * Salva o gasto medio por dia
    	 */
    	
    	for (DeputadoFederal dep: depList){
    		System.out.println("Calculando gasto de " + dep.getNomeParlamentar());
    		int days = 0;
    		Date dataIni, dataFim;
    		
    		List<DeputadoFederalExercicio> exeList =
    				JPA.em().createQuery(" FROM DeputadoFederalExercicio e where e.ideCadastro = :id")
    				.setParameter("id", dep.getIdeCadastro())
    				.getResultList();
    		
    		for (DeputadoFederalExercicio exe: exeList){
    			dataFim = exe.getDataFim();
    			dataIni = exe.getDataInicio();
    			
    			if (dataFim == null) dataFim = mostRecentDate;
    			if (dataFim.getTime() < olderDate.getTime()) continue;
    			
    			if (dataIni.getTime() < olderDate.getTime()) dataIni = olderDate;
    			if (dataIni.getTime() > mostRecentDate.getTime()) continue;
    			
    			days += Days.daysBetween(new DateTime(dataIni), new DateTime(dataFim)).getDays();
    			
    		}
    		dep.setDiasTrabalhados(days);
    		/////////////////////////
    		Object o = JPA.em()
    				.createNativeQuery("select sum(vlrDocumento) as TotalGasto from DeputadoFederalGasto g where g.nuCarteiraParlamentar = :id")
    				.setParameter("id", dep.getMatricula())
    				.getSingleResult();
    		
    		if (o != null){
    			Double gasto = (Double)o; 
    			dep.setGastoTotal(gasto);
    		
    			dep.setGastoPorDia(gasto/days);
    		}
    		JPA.em().persist(dep);
    	}
    	
    	/**
    	 * Calcula o índice do candidato:
    	 * 100*gasto/max
    	 * Ou seja: o candidato que gastou MAIS tem valor 100, o candidato que gastou NADA tem valor 0 
    	 */
    	
    	Double maxGasto = (Double)JPA.em().createNativeQuery("Select max(gastoPorDia) FROM DeputadoFederal").getSingleResult();
    	Double gasto, depTempIndice;

    	for (DeputadoFederal dep: depList){
    		gasto = dep.getGastoPorDia();
    		
    		depTempIndice = (100 * gasto / maxGasto);
    		
    		dep.setIndice(depTempIndice.intValue());
    		
    		JPA.em().persist(dep);
    	}
    	
    	return ok("success");
    }
    
    /**
     * Uses SAX instead of DOM because file is huge.
     * @return
     */
    @play.db.jpa.Transactional
    public static Result updatePoliticiansProfile() {
    	System.out.println("Entrou");
    	String url = hostCamaraFederal + pathObterDeputados;
    	ArrayList<DeputadoFederal> deputados = new ArrayList<DeputadoFederal>();
    	
    	try {
    	
    		Promise<WS.Response> result = WS.url(url).get();
    		InputStream is = new ByteArrayInputStream(result.get(timeout).asByteArray());
    		
    		
    		System.out.println("Pegou o Stream. Vai processar");
    		
    		SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
		
			DeputadoXMLHandler dhandler = new DeputadoXMLHandler(deputados);
			saxParser.parse(is, dhandler);
			System.out.println("Terminou de parsear");
			
			//For each deputado, get details and insert
			for(DeputadoFederal df: deputados){
			//for (int i = 0; i < 10; i++){
				//DeputadoFederal df = deputados.get(i);
				
				
				try {
					System.out.println("Getting exercicios de " + df.getNome());
					getDetails(df);
					JPA.em().persist(df);
				} catch (Exception e) {
					System.out.println("Erro pegando de " + df.getNome());
					e.printStackTrace();
					//return badRequest("Error getting Exercicios. " + e.getMessage());
				}
			}
			
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return badRequest("Error of response parse. " + e.getMessage());
		} catch (SAXException e) {
			e.printStackTrace();
			return badRequest("Error reading xml response. " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			return badRequest("Error saving Deputados's data. " + e.getMessage());
		}
		
		return ok ("Sucess");
    }

    private static void getDetails(DeputadoFederal deputado) throws Exception {
    	String url = hostCamaraFederal + pathObterDeputadosDetails;
    	
    	//Auxiliary variables
    	String sDate;
    	Date date;
    	
    	Promise<WS.Response> result = 
    			WS.url(url)
    			.setQueryParameter("ideCadastro", deputado.getIdeCadastro())
    			.setQueryParameter("numLegislatura", " ")
    			.get();
    	
    	Document doc = result.get(timeout).asXml();
    	doc.getDocumentElement().normalize();
    	
    	NodeList nDepList = doc.getElementsByTagName("Deputado");
    	//Sometimes it returns 2 nodes. I dont know why
    	Node depNode = nDepList.item(0);
    	Element depElement = (Element) depNode;
    	
    	//Get Generic Data
    	deputado.setProfissao(depElement.getElementsByTagName("nomeProfissao").item(0).getTextContent());
    	sDate = depElement.getElementsByTagName("dataNascimento").item(0).getTextContent();
    	date = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(sDate);
    	deputado.setNascimento(date);
    	
    	//Get periods
    	//NodeList exList = depElement.getElementsByTagName("periodosExercicio").item(0).getChildNodes();
    	//NodeList exList = depElement.getElementsByTagName("periodosExercicio");
    	
    	NodeList exList = depElement.getElementsByTagName("periodoExercicio");
    	
    	for (int i = 0; i < exList.getLength(); i++) {
    	
    		Node exNode = exList.item(i);
    		Element exElement = (Element) exNode;
    		
    		DeputadoFederalExercicio exercicio = new DeputadoFederalExercicio(); 
        	exercicio.setIdeCadastro(deputado.getIdeCadastro());
        	exercicio.setIdHistorico(exElement.getElementsByTagName("idHistoricoExercicioParlamentar").item(0).getTextContent());
        	exercicio.setSituacao(exElement.getElementsByTagName("situacaoExercicio").item(0).getTextContent());
        	
        	sDate = exElement.getElementsByTagName("dataInicio").item(0).getTextContent();
        	date = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(sDate);
        	exercicio.setDataInicio(date);
        	
        	sDate = exElement.getElementsByTagName("dataFim").item(0).getTextContent();
        	if (sDate != null && !sDate.trim().equals("")) {
        		date = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(sDate);
        		exercicio.setDataFim(date);
        		exercicio.setDescricaoFim(exElement.getElementsByTagName("descricaoCausaFimExercicio").item(0).getTextContent());
        	}
        	
        	// insert exercicio
        	JPA.em().persist(exercicio);
    	}
	}

	/**
     * Uses SAX instead of DOM because file is huge.
     * @return
     */
    @play.db.jpa.Transactional
    public static Result updatePoliticiansExpenditures() {
    	System.out.println("Entrou");
    	
    	try {
    	
    		InputStream is = new FileInputStream(pathObterDeputadosGastos);
    		
    		System.out.println("Pegou o Stream. Vai processar");
    		
    		SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
		
			GastoDeputadoXMLHandler dhandler = new GastoDeputadoXMLHandler();
			saxParser.parse(is, dhandler);
			System.out.println("Terminou de parsear");
			
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return badRequest("Error of response parse.");
		} catch (SAXException e) {
			e.printStackTrace();
			return badRequest("Error reading xml response.");
		} catch (IOException e) {
			e.printStackTrace();
			return badRequest("Error reading file data.");
		}
		
		return ok ("Sucess");
    }
    
}

class DeputadoXMLHandler extends DefaultHandler {
	private boolean bIdeCadastro;
	private boolean bMatricula;
	private boolean bIdParlamentar;
	private boolean bNome;
	private boolean bNomeParlamentar;
	private boolean bSexo;
	private boolean bUf;
	private boolean bPartido;
	private boolean bFone;
	private boolean bEmail;
	
	ArrayList<DeputadoFederal> deputados;
	DeputadoFederal deputado;
	
	public DeputadoXMLHandler(ArrayList<DeputadoFederal> deputados) {
		this.deputados = deputados;
	}

	public void startElement(String uri, String localName,String qName, 
            Attributes attributes) throws SAXException {
		
		if (qName.equalsIgnoreCase("ideCadastro")) {
			bIdeCadastro = true;
		} else if (qName.equalsIgnoreCase("matricula")) {
			bMatricula = true;
		} else if (qName.equalsIgnoreCase("idParlamentar")) {
			bIdParlamentar = true;
		} else if (qName.equalsIgnoreCase("nome")) {
			bNome = true;
		} else if (qName.equalsIgnoreCase("nomeParlamentar")) {
			bNomeParlamentar = true;
		} else if (qName.equalsIgnoreCase("sexo")) {
			bSexo = true;
		} else if (qName.equalsIgnoreCase("uf")) {
			bUf = true;
		} else if (qName.equalsIgnoreCase("partido")) {
			bPartido = true;
		} else if (qName.equalsIgnoreCase("fone")) {
			bFone = true;
		} else if (qName.equalsIgnoreCase("email")) {
			bEmail = true;
		}
	}
	
	public void endElement(String uri, String localName,
			String qName) throws SAXException {
	}
	
	public void characters(char ch[], int start, int length) throws SAXException {
		if (bIdeCadastro) {
			//Começando novo deputado
			deputado = new DeputadoFederal();
			String s = new String(ch, start, length);
			deputado.setIdeCadastro(s);
			bIdeCadastro = false;
		}
		if (bMatricula) {
			String s = new String(ch, start, length);
			deputado.setMatricula(s);
			bMatricula = false;
		}
		if (bIdParlamentar) {
			String s = new String(ch, start, length);
			deputado.setIdParlamentar(s);
			bIdParlamentar = false;
		}
		if (bNome) {
			String s = new String(ch, start, length);
			deputado.setNome(s);
			bNome = false;
		}
		if (bNomeParlamentar) {
			String s = new String(ch, start, length);
			deputado.setNomeParlamentar(s);
			bNomeParlamentar = false;
		}
		if (bSexo) {
			String s = new String(ch, start, length);
			deputado.setSexo(s);
			bSexo = false;
		}
		if (bUf) {
			String s = new String(ch, start, length);
			deputado.setUf(s);
			bUf = false;
		}
		if (bPartido) {
			String s = new String(ch, start, length);
			deputado.setPartido(s);
			bPartido = false;
		}
		if (bFone) {
			String s = new String(ch, start, length);
			deputado.setFone(s);
			bFone = false;
		}
		if (bEmail) {
			String s = new String(ch, start, length);
			deputado.setEmail(s);
			bEmail = false;
			
			//Acabou o deputado
			deputados.add(deputado);
			deputado = new DeputadoFederal();
			
		}		
 
	}
}
	
	class GastoDeputadoXMLHandler extends DefaultHandler {
		private boolean bTxNomeParlamentar;
		private boolean bNuCarteiraParlamentar;
		private boolean bNuLegislatura;
		private boolean bSgUF;
		private boolean bSgPartido;
		private boolean bCodLegislatura;
		private boolean bNumSubCota;
		private boolean bTxtDescricao;
		private boolean bNumEspecificacaoSubCota;
		private boolean bTxtDescricaoEspecificacao;
		private boolean bTxtFornecedor;
		private boolean bTxtCNPJCPF;
		private boolean bTxtNumero;
		private boolean bIndTipoDocumento;
		private boolean bDatEmissao;
		private boolean bVlrDocumento;
		private boolean bVlrGlosa;
		private boolean bVlrLiquido;
		private boolean bNumMes;
		private boolean bNumAno;
		private boolean bNumParcela;
		private boolean bTxtPassageiro;
		private boolean bTxtTrecho;
		private boolean bNumLote;
		private boolean bNumRessarcimento;
		
		DeputadoFederalGasto gasto;

		public void startElement(String uri, String localName,String qName, 
	            Attributes attributes) throws SAXException {
			
			if (qName.equalsIgnoreCase("txNomeParlamentar")) {
				bTxNomeParlamentar = true;
			} else if (qName.equalsIgnoreCase("nuCarteiraParlamentar")) {
				bNuCarteiraParlamentar = true;
			} else if (qName.equalsIgnoreCase("nuLegislatura")) {
				bNuLegislatura = true;
			} else if (qName.equalsIgnoreCase("sgUF")) {
				bSgUF = true;
			} else if (qName.equalsIgnoreCase("sgPartido")) {
				bSgPartido = true;
			} else if (qName.equalsIgnoreCase("codLegislatura")) {
				bCodLegislatura = true;
			} else if (qName.equalsIgnoreCase("numSubCota")) {
				bNumSubCota = true;
			} else if (qName.equalsIgnoreCase("txtDescricao")) {
				bTxtDescricao = true;
			} else if (qName.equalsIgnoreCase("numEspecificacaoSubCota")) {
				bNumEspecificacaoSubCota = true;
			} else if (qName.equalsIgnoreCase("txtDescricaoEspecificacao")) {
				bTxtDescricaoEspecificacao = true;
			} else if (qName.equalsIgnoreCase("txtFornecedor")) {
				bTxtFornecedor = true;
			} else if (qName.equalsIgnoreCase("txtCNPJCPF")) {
				bTxtCNPJCPF = true;
			} else if (qName.equalsIgnoreCase("txtNumero")) {
				bTxtNumero = true;
			} else if (qName.equalsIgnoreCase("indTipoDocumento")) {
				bIndTipoDocumento = true;
			} else if (qName.equalsIgnoreCase("datEmissao")) {
				bDatEmissao = true;
			} else if (qName.equalsIgnoreCase("vlrDocumento")) {
				bVlrDocumento = true;
			} else if (qName.equalsIgnoreCase("vlrGlosa")) {
				bVlrGlosa = true;
			} else if (qName.equalsIgnoreCase("vlrLiquido")) {
				bVlrLiquido = true;
			} else if (qName.equalsIgnoreCase("numMes")) {
				bNumMes = true;
			} else if (qName.equalsIgnoreCase("numAno")) {
				bNumAno = true;
			} else if (qName.equalsIgnoreCase("numParcela")) {
				bNumParcela = true;
			} else if (qName.equalsIgnoreCase("txtPassageiro")) {
				bTxtPassageiro = true;
			} else if (qName.equalsIgnoreCase("txtTrecho")) {
				bTxtTrecho = true;
			} else if (qName.equalsIgnoreCase("numLote")) {
				bNumLote = true;
			} else if (qName.equalsIgnoreCase("numRessarcimento")) {
				bNumRessarcimento = true;
			}
			
		}
		
		public void endElement(String uri, String localName,
				String qName) throws SAXException {
		}
		
		public void characters(char ch[], int start, int length) throws SAXException {
			if (bTxNomeParlamentar) {
				//Começando novo deputado
				gasto = new DeputadoFederalGasto();
				String s = new String(ch, start, length);
				gasto.setTxNomeParlamentar(s);
				bTxNomeParlamentar = false;
			} else if (bNuCarteiraParlamentar) {
				String s = new String(ch, start, length);
				gasto.setNuCarteiraParlamentar(s);
				bNuCarteiraParlamentar = false;
			} else if (bNuLegislatura) {
				String s = new String(ch, start, length);
				gasto.setNuLegislatura(s);
				bNuLegislatura = false;
			} else if (bSgUF) {
				String s = new String(ch, start, length);
				gasto.setSgUF(s);
				bSgUF = false;
			} else if (bSgPartido) {
				String s = new String(ch, start, length);
				gasto.setSgPartido(s);
				bSgPartido = false;
			} else if (bCodLegislatura) {
				String s = new String(ch, start, length);
				gasto.setCodLegislatura(s);
				bCodLegislatura = false;
			} else if (bNumSubCota) {
				String s = new String(ch, start, length);
				gasto.setNumSubCota(s);
				bNumSubCota = false;
			} else if (bTxtDescricao) {
				String s = new String(ch, start, length);
				gasto.setTxtDescricao(s);
				bTxtDescricao = false;
			} else if (bNumEspecificacaoSubCota) {
				String s = new String(ch, start, length);
				gasto.setNumEspecificacaoSubCota(s);
				bNumEspecificacaoSubCota = false;
			} else if (bTxtDescricaoEspecificacao) {
				String s = new String(ch, start, length);
				gasto.setTxtDescricaoEspecificacao(s);
				bTxtDescricaoEspecificacao = false;				
			} else if (bTxtFornecedor) {
				String s = new String(ch, start, length);
				gasto.setTxtFornecedor(s);
				bTxtFornecedor = false;				
			} else if (bTxtCNPJCPF) {
				String s = new String(ch, start, length);
				gasto.setTxtCNPJCPF(s);
				bTxtCNPJCPF = false;				
			} else if (bTxtNumero) {
				String s = new String(ch, start, length);
				gasto.setTxtNumero(s);
				bTxtNumero = false;				
			} else if (bIndTipoDocumento) {
				String s = new String(ch, start, length);
				gasto.setIndTipoDocumento(s);
				bIndTipoDocumento = false;				
			} else if (bDatEmissao) {
				String s = new String(ch, start, length);
				/*try {
					gasto.setDatEmissao(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(s));
				} catch (ParseException e) {
					e.printStackTrace();
					throw new SAXException(e);
				}*/
				gasto.setDatEmissao(s);
				bDatEmissao = false;				
			} else if (bVlrDocumento) {
				String s = new String(ch, start, length);
				gasto.setVlrDocumento(Float.valueOf(s));
				bVlrDocumento = false;				
			} else if (bVlrGlosa) {
				String s = new String(ch, start, length);
				gasto.setVlrGlosa(Float.valueOf(s));
				bVlrGlosa = false;				
			} else if (bVlrLiquido) {
				String s = new String(ch, start, length);
				gasto.setVlrLiquido(Float.valueOf(s));
				bVlrLiquido = false;				
			} else if (bNumMes) {
				String s = new String(ch, start, length);
				gasto.setNumMes(Integer.valueOf(s));
				bNumMes = false;				
			} else if (bNumAno) {
				String s = new String(ch, start, length);
				gasto.setNumAno(Integer.valueOf(s));
				bNumAno = false;				
			} else if (bNumParcela) {
				String s = new String(ch, start, length);
				gasto.setNumParcela(Integer.valueOf(s));
				bNumParcela = false;				
			} else if (bTxtPassageiro) {
				String s = new String(ch, start, length);
				gasto.setTxtPassageiro(s);
				bTxtPassageiro = false;				
			} else if (bTxtTrecho) {
				String s = new String(ch, start, length);
				gasto.setTxtTrecho(s);
				bTxtTrecho = false;				
			} else if (bNumLote) {
				String s = new String(ch, start, length);
				gasto.setNumLote(s);
				bNumLote = false;				
			} else if (bNumRessarcimento) {
				String s = new String(ch, start, length);
				gasto.setNumRessarcimento(s);
				bNumRessarcimento = false;
				
				saveGastoDeputadoFederal(gasto);
			}		
	 
		}

		private void saveGastoDeputadoFederal(DeputadoFederalGasto gasto) {
			JPA.em().persist(gasto);
		}
	
}
