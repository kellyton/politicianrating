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
