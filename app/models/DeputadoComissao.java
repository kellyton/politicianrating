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

package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Representa a tabela que relaciona quais deputados participam de quais comissões
 * @author kellyton
 * TODO: Trocar para uma chave composta decente
 */

@Entity
public class DeputadoComissao implements Comparable<DeputadoComissao>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;
	
	@Column (nullable = false)
	String ideCadastroDeputado; //id do deputado
	
	@Column (nullable = false)
	String siglaComissao;//sigla da comissão
	
	
	public String getIdeCadastroDeputado() {
		return ideCadastroDeputado;
	}
	public void setIdeCadastroDeputado(String ideCadastroDeputado) {
		this.ideCadastroDeputado = ideCadastroDeputado;
	}

	public String getSiglaComissao() {
		return siglaComissao;
	}

	public void setSiglaComissao(String siglaComissao) {
		this.siglaComissao = siglaComissao;
	}

	@Override
	public String toString(){
		return ideCadastroDeputado + " - " + siglaComissao;
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof DeputadoComissao){
			DeputadoComissao other = (DeputadoComissao)o;
			
			if (ideCadastroDeputado.equals(other.ideCadastroDeputado) && siglaComissao.equals(other.siglaComissao) ){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	@Override
	public int compareTo(DeputadoComissao o) {
		if (o == null) throw new NullPointerException();
		
		if (!ideCadastroDeputado.equals(o.ideCadastroDeputado)){
			return ideCadastroDeputado.compareTo(o.ideCadastroDeputado);
		} else {
			return siglaComissao.compareTo(o.siglaComissao);
		}
	}
}
