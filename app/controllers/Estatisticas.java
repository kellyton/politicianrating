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

public class Estatisticas extends Controller {
	
	@Transactional
	public static Result estatisticas(){
		
		List<GastoPartido> gastosTipoDeputado = getGastosTipoDeputado();
		List<GastoPartido> gastosTipoSenador = getGastosTipoSenador();
		
		double totalGastosDeputado = 0;
		double totalGastosSenador = 0;
		
		for (GastoPartido gasto: gastosTipoDeputado){
			totalGastosDeputado += gasto.getValor();
		}
		
		for (GastoPartido gasto: gastosTipoSenador){
			totalGastosSenador += gasto.getValor();
		}
		
		String totalDeputado, totalSenador;
		totalDeputado = String.valueOf(totalGastosDeputado);
		totalDeputado = totalDeputado.substring(0, totalDeputado.indexOf(".") + 2); 
		
		totalSenador = String.valueOf(totalGastosSenador);
		totalSenador = totalSenador.substring(0, totalSenador.indexOf(".") + 2);
		
		return ok(views.html.cotas.render(totalDeputado, gastosTipoDeputado, totalSenador, gastosTipoSenador));
	}
	
	 
	
	private static List<GastoPartido> getGastosTipoDeputado() {
		String query = "SELECT txtDescricao, sum(vlrDocumento)" +
				" FROM deputadofederalgasto" +
				" GROUP BY txtDescricao" +
				" ORDER BY 2 DESC";
	
    	List<Object> resultList = JPA.em().createNativeQuery(query).getResultList();
    	List<GastoPartido> gastosTipo = new ArrayList<GastoPartido>(15);
    	
    	GastoPartido gastoTipo;
    	
		for (Object result : resultList) {
			gastoTipo = new GastoPartido();
			
		    Object[] items = (Object[]) result;
		    try {
		    	gastoTipo.setPartido((String)items[0]);
		    	gastoTipo.setMedia((Double)items[1]);
		    	
		    	gastosTipo.add(gastoTipo);
		    } catch (Exception e){
		    	e.printStackTrace();
		    }
		}
		
		return gastosTipo;
	}
	
	private static List<GastoPartido> getGastosTipoSenador() {
		String query = "SELECT tipo_depesa, sum(valor_reembolsado)" +
				" FROM senadorGasto WHERE ano = :ano" +
				" GROUP BY tipo_depesa ORDER BY 2 DESC";
	
    	List<Object> resultList = JPA.em().createNativeQuery(query)
    			.setParameter("ano", "2013")
    			.getResultList();
    	List<GastoPartido> gastosTipo = new ArrayList<GastoPartido>(15);
    	
    	GastoPartido gastoTipo;
    	
		for (Object result : resultList) {
			gastoTipo = new GastoPartido();
			
		    Object[] items = (Object[]) result;
		    try {
		    	gastoTipo.setPartido((String)items[0]);
		    	gastoTipo.setMedia((Double)items[1]);
		    	
		    	gastosTipo.add(gastoTipo);
		    } catch (Exception e){
		    	e.printStackTrace();
		    }
		}
		
		return gastosTipo;
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
	}

}
