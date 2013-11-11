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
import models.util.GastoTotal;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import util.NamesMap;

public class Estatisticas extends Controller {
	
	@Transactional
	public static Result estatisticas(){
		
		List<GastoTotal> gastosPartidos = getGastosPartidos();
		List<GastoTotal> gastosTipoDeputado = getGastosTipoDeputado();
		List<GastoTotal> gastosTipoSenador = getGastosTipoSenador();
		
		//I prepared the final string here because it was getting error on template
		String gastosP = "";
		for (GastoTotal gasto: gastosPartidos){
			gastosP += gasto.getValorFormated() + "# ";
		}
		gastosP = gastosP.substring(0, gastosP.length() -2);
		//problems with locale
		gastosP = gastosP.replace(",", ".");
		gastosP = gastosP.replace("#", ",");
		
		return ok(views.html.cotas.render(gastosP, gastosPartidos, gastosTipoDeputado, gastosTipoSenador));
	}

	private static List<GastoTotal> getGastosTipoDeputado() {
		String query = "SELECT txtDescricao, sum(vlrDocumento)" +
				" FROM deputadofederalgasto" +
				" GROUP BY txtDescricao" +
				" ORDER BY 2 DESC";
	
    	List<Object> resultList = JPA.em().createNativeQuery(query).getResultList();
    	List<GastoTotal> gastosTipo = new ArrayList<GastoTotal>(15);
    	
    	GastoTotal gastoTipo;
    	
		for (Object result : resultList) {
			gastoTipo = new GastoTotal();
			
		    Object[] items = (Object[]) result;
		    try {
		    	gastoTipo.setNome(
		    			NamesMap.getShortName(NamesMap.DEPUTADO, (String)items[0])
		    		);
		    	gastoTipo.setMedia((Double)items[1]);
		    	
		    	gastosTipo.add(gastoTipo);
		    } catch (Exception e){
		    	e.printStackTrace();
		    }
		}
		
		return gastosTipo;
	}
	
	private static List<GastoTotal> getGastosTipoSenador() {
		String query = "SELECT tipo_depesa, sum(valor_reembolsado)" +
				" FROM senadorGasto WHERE ano = :ano" +
				" GROUP BY tipo_depesa ORDER BY 2 DESC";
	
    	List<Object> resultList = JPA.em().createNativeQuery(query)
    			.setParameter("ano", "2013")
    			.getResultList();
    	List<GastoTotal> gastosTipo = new ArrayList<GastoTotal>(15);
    	
    	GastoTotal gastoTipo;
    	
		for (Object result : resultList) {
			gastoTipo = new GastoTotal();
			
		    Object[] items = (Object[]) result;
		    try {
		    	gastoTipo.setNome(
		    			NamesMap.getShortName(NamesMap.SENADOR, (String)items[0])
		    		);
		    	gastoTipo.setMedia((Double)items[1]);
		    	
		    	gastosTipo.add(gastoTipo);
		    } catch (Exception e){
		    	e.printStackTrace();
		    }
		}
		
		return gastosTipo;
	}

	private static List<GastoTotal> getGastosPartidos(){
		String query = "SELECT partido, avg(gastoPorDia)" +
				" FROM deputadofederal" +
				" WHERE gastoPorDia > 0" + //exclude people without expenses: everyone agree this situation is an error
				" GROUP BY partido" +
				" ORDER BY 2 ASC";
    	
    	List<Object> resultList = JPA.em().createNativeQuery(query).getResultList();
    	List<GastoTotal> gastosPartidos = new ArrayList<GastoTotal>(21);
    	
    	GastoTotal partido;
    	
		for (Object result : resultList) {
			partido = new GastoTotal();
			
		    Object[] items = (Object[]) result;
		    try {
		    	partido.setNome((String)items[0]);
		    	partido.setMedia((Double)items[1]);
		    	
		    	gastosPartidos.add(partido);
		    } catch (Exception e){
		    	e.printStackTrace();
		    }
		}
		
		return gastosPartidos;
	}

}
