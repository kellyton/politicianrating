package controllers;

import static play.data.Form.form;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import models.Empresa;
import models.util.PoliticoValor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import play.data.DynamicForm;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.WS;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import util.CnpjCpf;

public class Empresas extends Controller{
	
	private static HashMap<String, Session> sessionMap = new HashMap<String, Session>();  
	
	private static final int PAGE_SIZE = 5000;
	
	/**
	 * Return a page with a list of all companies
	 * @return
	 */
    @Transactional
    public static Result empresas() {  	
    	return empresas(1);
    }
    
    private static Result empresas(int page){
    	int begin = (page - 1) * PAGE_SIZE;
    	
    	List<Empresa> empresas = JPA.em().createQuery("FROM Empresa ORDER BY totalRecebido DESC")
    	.setFirstResult(begin)
    	.setMaxResults(PAGE_SIZE)
    	.getResultList();    	
    	return ok(views.html.empresa.render(empresas, page));
    }
    
    /**
     * Return a page with detailed data of the company with id = cnpj 
     * @param id
     * @return
     */
	@Transactional
	public static Result show(String cnpj){
		//Pega os dados da empresa
		
		Object o = JPA.em().createQuery("FROM Empresa WHERE cnpj = :cnpj")
				.setParameter("cnpj", cnpj)
				.getSingleResult();
		
		Empresa empresa;
		if (o != null){
			empresa = (Empresa)o;
			
			if (empresa.isDadosAtualizados()){
				List<PoliticoValor> politicoValores = getEmpresaExpenses(cnpj);
				return ok(views.html.detalheempresa.render(empresa, politicoValores));
			} else {
				return (getCaptcha(cnpj));
			}
		} else {
			//TODO se a empresa não está salva, faço o que? Isso é erro, TODAS deveriam estar salvas
		}
		
		return redirect(controllers.routes.Empresas.empresas());
		
		
	}
	
	private static List<PoliticoValor> getEmpresaExpenses(String cnpj) {
		
		List<Object> resultList;
		List<PoliticoValor> politicoValores;
		
		try {
			//Se resolver o charset, pegar por aqui. Mas atualmente o nome sai tronxo
			/* String query =
					"select txNomeParlamentar, sum(vlrDocumento)" +
					" FROM deputadofederalgasto" +
					" WHERE txtCNPJCPF = :cnpj" +
					" group by txNomeParlamentar" +
					" ORDER BY 2 DESC"; */ 
			
			String query = 
					"SELECT ideCadastro, nomeParlamentar, uf, partido, sum(vlrDocumento)" +
					" FROM deputadofederal, deputadofederalgasto" + 
					" WHERE deputadofederal.matricula = deputadofederalgasto.nucarteiraparlamentar" + 
						" AND txtCNPJCPF = :cnpj" + 
					" GROUP BY nomeParlamentar" +
					" ORDER BY 5 DESC";
			
			resultList = JPA.em().createNativeQuery(query)
					.setParameter("cnpj", cnpj)
					.getResultList();
			
			politicoValores = new LinkedList<PoliticoValor>();
			PoliticoValor pv;
			
			for (Object result : resultList) {
				pv = new PoliticoValor();
				
			    Object[] items = (Object[]) result;
			    pv.setIdeCadastro((String)items[0]);
			    pv.setNome((String)items[1]);
			    pv.setEstado((String)items[2]);
			    pv.setPartido((String)items[3]);
			    pv.setValor((Double)items[4]);
			    politicoValores.add(pv);
			}  
			
			return politicoValores;
			
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static Result getCaptcha(String cnpj){
		if (CnpjCpf.isCNPJ(cnpj)){
			return validaCNPJ(cnpj);
		} else {
			return validaCPF(cnpj);
		}
	}
		
	/**
	 * It has a problem trying to show the link on user browser. It only works if
	 * we run the application in the same server than client.
	 * So, we have to save image locally and than show that image to the client
	 * @param cnpj
	 * @return
	 */
	private static Result validaCPF(String cnpj) {
		try {
			String url = "http://www.receita.fazenda.gov.br/aplicacoes/atcta/cpf/consultapublica.asp";
			
			Response res = Jsoup.connect(url).execute();
			Map<String, String> cookies = res.cookies();
			
			Document form = res.parse();
			
			Element captchaLink = form.select("img#imgcaptcha").first();
			String viewState = form.select("#viewstate").first().attr("value");
			
			//Save image locally
			String imageOriginalLink = captchaLink.attr("abs:src");
			String filename = imageOriginalLink.substring(imageOriginalLink.indexOf("guid=")+5)+".jpg";			
			String imageLocalPath = FileService.path + filename; 	
			
			saveImage(imageOriginalLink, imageLocalPath);
			
			Session session = new Session();
			session.cookies = cookies;
			session.viewState = viewState;
			
			sessionMap.put(filename, session);
			
			return ok(views.html.validacaptcha.render(filename, cnpj));
			
		} catch (IOException e) {
			return badRequest(e.getMessage());
		}
	}

	private static Result validaCNPJ(String cnpj){
		try {
			String url = "http://www.receita.fazenda.gov.br/pessoajuridica/cnpj/cnpjreva/Cnpjreva_Solicitacao2.asp";
			
			Response res = Jsoup.connect(url).execute();
			Map<String, String> cookies = res.cookies();
			
			Document form = res.parse();
			
			Element captchaLink = form.select("img#imgcaptcha").first();
			String viewState = form.select("#viewstate").first().attr("value");
			
			//Save image locally
			String imageOriginalLink = captchaLink.attr("abs:src");
			String filename = imageOriginalLink.substring(imageOriginalLink.indexOf("guid=")+5)+".jpg";			
			String imageLocalPath = FileService.path + filename; 
			
			saveImage(imageOriginalLink, imageLocalPath);
			
			Session session = new Session();
			session.cookies = cookies;
			session.viewState = viewState;
			
			sessionMap.put(filename, session);
			
			return ok(views.html.validacaptcha.render(filename, cnpj));
			
		} catch (Exception e){
			return badRequest(e.getMessage());
		}
	}
	
	private static void saveImage(String imageUrl, String destinationFile) throws IOException {
		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}
		is.close();
		os.close();
	}
	
	@Transactional
	public static Result captchaValidado(){
		DynamicForm dynamicForm = form().bindFromRequest();
		String typedCaptcha = dynamicForm.get("captcha");
		String filename = dynamicForm.get("filename");
		String cnpj = dynamicForm.get("cnpj");
		
		if (CnpjCpf.isCNPJ(cnpj)){
			return finalizaValidacaoCNPJ(typedCaptcha, filename, cnpj);
		} else {
			return finalizaValidacaoCPF(typedCaptcha, filename, cnpj);
		}
	}
		
	private static Result finalizaValidacaoCPF(String typedCaptcha, String filename, String cnpj) {
		Session session = sessionMap.remove(filename);
		Map<String, String> cookies;
		
		//Lost session
		try {
			 cookies = session.cookies;
			if (cookies == null) return redirect(controllers.routes.Empresas.getCaptcha(cnpj)); 
			
		} catch (NullPointerException n){
			return redirect(controllers.routes.Empresas.getCaptcha(cnpj));
		}
		
		String viewState = session.viewState;
		
		String url2 = "http://www.receita.fazenda.gov.br/aplicacoes/atcta/cpf/ConsultaPublicaExibir.asp";
		
		Response finalResponse;
		Document result = null;
		try {
			finalResponse = Jsoup.connect(url2)
					.data( "viewstate", viewState,
							"txtCPF", cnpj, 
							"captcha", typedCaptcha, 
							"captchaAudio", "",
							"Enviar", "Consultar")
					.method(Method.POST)
					.cookies(cookies)
					.execute();
			
			result = finalResponse.parse();
				
			result.outputSettings().charset("iso-8859-1");
			Empresa e = parseDocumentCPF(result);
			
			//Save empresa
			e = atualizarEmpresaCPF(e);
			
			try {
				new File(FileService.path + filename).delete();
			} catch (Exception ex){
				//do nothing
			}
			
			return redirect(controllers.routes.Empresas.show(cnpj));
			
		} catch (Exception e){ //wrong captcha. Try again
			flash("error", "Captcha incorreto. Por favor digite as letras novamente");
			return redirect(controllers.routes.Empresas.show(cnpj));
		}
	}
	
	private static Empresa parseDocumentCPF(Document doc) {
		Empresa pessoa = new Empresa();
		
		// Remove partes desnecessarias da arvore DOM
		Elements elements = doc.select(".clConteudoDados");
		
		for (int index=0; index < elements.size(); index++) {
			String node = elements.get(index).text();
			
			if (node.contains("CPF")) {
				pessoa.setCnpj(node.split(":")[1].trim());
			} else if (node.contains("Nome da Pessoa")) {
				pessoa.setNome(node.split(":")[1].trim());
				pessoa.setFantasia(node.split(":")[1].trim());
			} else if (node.contains("Cadastral")) {
				pessoa.setSituacao(node.split(":")[1].trim());
			}
		}
		pessoa.setDataSituacao(new Date());
		pessoa.setDadosAtualizados(true);
		
		return pessoa;
	}
	
	private static Empresa atualizarEmpresaCPF(Empresa e) {
		return atualizarEmpresa(e);
	}
	
	public static Result finalizaValidacaoCNPJ(String typedCaptcha, String filename, String cnpj){
		Session session = sessionMap.remove(filename);
		Map<String, String> cookies;
		
		try {
			 cookies = session.cookies;
			if (cookies == null) return redirect(controllers.routes.Empresas.getCaptcha(cnpj)); 
			
		} catch (NullPointerException n){
			return redirect(controllers.routes.Empresas.getCaptcha(cnpj));
		}
		
		String viewState = session.viewState;
		
		String url2 = "http://www.receita.fazenda.gov.br/pessoajuridica/cnpj/cnpjreva/valida.asp";
		
		Response finalResponse;
		Document result = null;
		try {
			finalResponse = Jsoup.connect(url2)
					.data("origem", "comprovante", 
							"viewstate", viewState, 
							"cnpj",cnpj, 
							"captcha", typedCaptcha, 
							"captchaAudio", "",
							"submit1", "Consultar",
							"search_type", "cnpj")
					.method(Method.POST)
					.cookies(cookies)
					.execute();
			
			result = finalResponse.parse();
				
			result.outputSettings().charset("iso-8859-1");
			Empresa e = parseDocumentCNPJ(result);
			
			//Save empresa
			e = atualizarEmpresa(e);
			
			try {
				new File(FileService.path + filename).delete();
			} catch (Exception ex){
				//do nothing
			}
			
			return redirect(controllers.routes.Empresas.show(cnpj));
			
		} catch (Exception e){ //wrong captcha. Try again
			flash("error", "Captcha incorreto. Por favor digite as letras novamente");
			return redirect(controllers.routes.Empresas.show(cnpj));
		}
	}

	private static Empresa atualizarEmpresa(Empresa newEmpresa) {
		Object o = JPA.em().createQuery("FROM Empresa WHERE cnpj = :cnpj")
				.setParameter("cnpj", newEmpresa.getCnpj())
				.getSingleResult();
		
		Empresa oldEmpresa = null;
		
		if (o != null){
			oldEmpresa = (Empresa)o;
			oldEmpresa.setDataAbertura(newEmpresa.getDataAbertura());
			oldEmpresa.setNome(newEmpresa.getNome());
			//Some companies have "****" as name fantasia
			if (newEmpresa.getFantasia().contains("***")){
				oldEmpresa.setFantasia(newEmpresa.getNome());
			} else {
				oldEmpresa.setFantasia(newEmpresa.getFantasia());
			}
			oldEmpresa.setAtividadePrincipal(newEmpresa.getAtividadePrincipal());
			oldEmpresa.setNaturezaJuridica(newEmpresa.getNaturezaJuridica());
			oldEmpresa.setLogradouro(newEmpresa.getLogradouro());
			oldEmpresa.setNumero(newEmpresa.getNumero());
			oldEmpresa.setComplemento(newEmpresa.getComplemento());
			oldEmpresa.setCep(newEmpresa.getCep());
			oldEmpresa.setBairro(newEmpresa.getBairro());
			oldEmpresa.setCidade(newEmpresa.getCidade());
			oldEmpresa.setEstado(newEmpresa.getEstado());
			oldEmpresa.setSituacao(newEmpresa.getSituacao());
			oldEmpresa.setDataSituacao(newEmpresa.getDataSituacao());
			oldEmpresa.setDadosAtualizados(true);
			
			if (oldEmpresa.isCNPJ()){
				oldEmpresa = setLocation(oldEmpresa);
			}
			
			JPA.em().persist(oldEmpresa);
		} else {
			//insert or do nothing? Company MUST exist before this method
		}
		
		return oldEmpresa;
	}

	/**
	 * Using google geocode API
	 * @param oldEmpresa
	 * @return
	 */
	private static Empresa setLocation(Empresa empresa) {
		//Example: http://maps.googleapis.com/maps/api/geocode/json?address=santa+cruz&components=country:ES&sensor=false
		
		String address = "";
		
		String []parts ={empresa.getLogradouro(), empresa.getNumero(), empresa.getComplemento(), empresa.getCep(), 
				empresa.getBairro(), empresa.getCidade(), empresa.getEstado()}; 
		
		for (int i = 0; i < parts.length; i++) {
			if (parts[i]!=null && parts[i].length() > 0){
				address += ", " + parts[i];
			}
		}
		
		if (address.length() > 0){
			address = address.substring(2);
		} else {
			return empresa;
		}
		
		Promise<WS.Response> response = WS.url("http://maps.googleapis.com/maps/api/geocode/xml")
				.setQueryParameter("address", address)
				.setQueryParameter("components", "country:BR")
				.setQueryParameter("sensor", "false")
				.get();
		
		org.w3c.dom.Document doc = response.get().asXml();
		doc.getDocumentElement().normalize();
		
		//Process XML
		String lat = doc.getElementsByTagName("lat").item(0).getTextContent();
		String lng = doc.getElementsByTagName("lng").item(0).getTextContent();
		
		empresa.setLatitude(Double.parseDouble(lat));
		empresa.setLongitude(Double.parseDouble(lng));
		
		return empresa;
	}

	/**
	 * Problems with charset 
	 * @param doc
	 * @return
	 */
	private static Empresa parseDocumentCNPJ(Document doc) {
		Empresa empresa = new Empresa();
		
		// Remove partes desnecessarias da arvore DOM
		Elements elements = doc.select("table[border=1] table[border=0] font");
		
		for (int index=0; index < elements.size(); index++) {
			String node = elements.get(index).text();
			
			//System.out.println(node);
			
			if (node.contains("MERO DE INSCRI")) //TODO fix it
				empresa.setCnpj(elements.get(index+1).text());
						
			else if(node.equalsIgnoreCase("DATA DE ABERTURA"))
				empresa.setDataAbertura(elements.get(index+1).text());
			
			else if (node.equalsIgnoreCase("NOME EMPRESARIAL"))
				empresa.setNome(elements.get(index+1).text());
			
			else if (node.contains("NOME DE FANTASIA"))
				empresa.setFantasia(elements.get(index+1).text());
			
			else if (node.contains("DA ATIVIDADE ECON"))
				empresa.setAtividadePrincipal(elements.get(index+1).text());
			
			else if (node.contains("DA NATUREZA"))
				empresa.setNaturezaJuridica(elements.get(index+1).text());

			else if (node.equalsIgnoreCase("LOGRADOURO"))
				empresa.setLogradouro(elements.get(index+1).text());
			
			else if (node.equalsIgnoreCase("CEP"))
				empresa.setCep(elements.get(index+1).text());
			
			else if (node.equalsIgnoreCase("BAIRRO/DISTRITO"))
				empresa.setBairro(elements.get(index+1).text());
			
			else if (node.contains("MERO")) //TODO fix it
				empresa.setNumero(elements.get(index+1).text());
			
			else if (node.equalsIgnoreCase("COMPLEMENTO"))
				empresa.setComplemento(elements.get(index+1).text());
			
			else if (node.contains("MUNIC")) //TODO fix it
				empresa.setCidade(elements.get(index+1).text());
			
			else if (node.equalsIgnoreCase("UF"))
				empresa.setEstado(elements.get(index+1).text());
			
			else if (node.trim().startsWith("SITUA") && node.trim().endsWith("CADASTRAL"))
				empresa.setSituacao(elements.get(index+1).text());
			
			else if (node.trim().startsWith("DATA DA SITUA") && node.trim().endsWith("CADASTRAL"))
				empresa.setDataSituacao(elements.get(index+1).text());
			
			else
				continue;
		}
		
		return empresa;
		
	}
}

class Session {
	Map<String, String> cookies;
	String viewState;
}
