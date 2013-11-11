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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Entity
public class Comissao implements Comparable<Comissao>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;
	
	@Column (nullable = false)
	String sigla;
	
	@Column (nullable = false)
	long orgao;
	
	@Column(columnDefinition="TEXT")
	String nome;

	@Column(nullable = false)
	private Date createdAt;

	@Column(nullable = true)
	private Date updatedAt;
	
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public long getOrgao() {
		return orgao;
	}

	public void setOrgao(long orgao) {
		this.orgao = orgao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	@PrePersist
	private void setCreatedAt() {
		this.createdAt = new Date();
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	@PreUpdate
	private void setUpdatedAt() {
		this.updatedAt = new Date();
	}
	@Override
	public String toString(){
		return sigla + " - " + nome;
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof Comissao){
			
			Comissao other = (Comissao)o;
			return sigla.equals(other.sigla);
		} else {
			return false;
		}
	}
	
	@Override
	public int compareTo(Comissao o) {
		if (o == null) throw new NullPointerException();
		
		return (sigla.compareTo(o.sigla));
	}
}
