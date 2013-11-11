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

package models.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class GastoPartido {

	String partido;
	Double valor;
	
	public String getPartido() {
		return partido;
	}
	public void setPartido(String partido) {
		this.partido = partido;
	}
	public double getMedia() {
		return valor;
	}
	public void setMedia(double valor) {
		this.valor = valor;
	}
	public Double getValor(){
		return valor;
	}
	public String getValorFormated(){
		//return new DecimalFormat("#,###.00").format(valor);
		Locale LOCAL = new Locale("pt","BR");
		
		DecimalFormat df = new DecimalFormat("###,##0.00", new DecimalFormatSymbols(LOCAL));  
		String s = df.format(valor);  
		return s;
	}	   
}
