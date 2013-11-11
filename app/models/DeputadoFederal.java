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

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import play.db.jpa.JPA;

/**
 * O numero da matricula aqui é o NuCarteiraParlamentar nos gastos 
 * @author kellyton
 *
 */

@Entity
public class DeputadoFederal implements IPolitico{

	@Id
	private String ideCadastro;
	
	@Column(unique = true, nullable = false)
	private String matricula;
	
	@Column(unique = true, nullable = false)
	private String idParlamentar;
	
	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private String nomeParlamentar;
	
	@Column
	private String sexo;
	
	@Column
	private String uf;
	
	@Column
	private String partido;
	
	@Column
	private String fone;
	
	@Column
	private String email;
	
	@Column
	private String profissao;
	
	@Column
	private Date nascimento;
	
	@Column
	private String photoURL;
	
	@Column
	private double gastoTotal;
	
	@Column
	private double gastoPorDia;
	
	@Column
	private long diasTrabalhados;
	
	@Column
	private int indice;
	
	@Column(nullable = false)
	private Date createdAt;

	@Column(nullable = true)
	private Date updatedAt;
	
	public String getIdeCadastro() {
		return ideCadastro;
	}
	public void setIdeCadastro(String ideCadastro) {
		this.ideCadastro = ideCadastro;
	}
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public String getIdParlamentar() {
		return idParlamentar;
	}
	public void setIdParlamentar(String idParlamentar) {
		this.idParlamentar = idParlamentar;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getNomeParlamentar() {
		return nomeParlamentar;
	}
	public void setNomeParlamentar(String nomeParlamentar) {
		this.nomeParlamentar = nomeParlamentar;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public String getPartido() {
		return partido;
	}
	public void setPartido(String partido) {
		this.partido = partido;
	}
	public String getFone() {
		return fone;
	}
	public void setFone(String fone) {
		this.fone = fone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getProfissao() {
		return profissao;
	}
	public void setProfissao(String profissao) {
		this.profissao = profissao;
	}
	public Date getNascimento() {
		return nascimento;
	}
	public void setNascimento(Date nascimento) {
		this.nascimento = nascimento;
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
	public double getGastoTotal() {
		return gastoTotal;
	}
	public void setGastoTotal(double gastoTotal) {
		this.gastoTotal = gastoTotal;
	}
	public String getGastoTotalFormated() {
		return new DecimalFormat("#,###.00").format(gastoTotal);  
	}
	public double getGastoPorDia() {
		return gastoPorDia;
	}
	public String getGastoPorDiaFormated() {
		return new DecimalFormat("#,###.00").format(gastoPorDia);  
	}
	public void setGastoPorDia(double gastoPorDia) {
		this.gastoPorDia = gastoPorDia;
	}
	public long getDiasTrabalhados() {
		return diasTrabalhados;
	}
	public void setDiasTrabalhados(long diasTrabalhados) {
		this.diasTrabalhados = diasTrabalhados;
	}
	public int getIndice() {
		return indice;
	}
	public void setIndice(int indice) {
		this.indice = indice;
	}
	@Override
	public String toString(){
		return nome + " - " + uf + " - " + partido;
	}
	@Override
	public String getPhotoURL() {
		return null;
	}
	
	////////////////////////
    /*public static List<DeputadoFederal> getAll() {
        List<DeputadoFederal> data = JPA.em()
            .createQuery("from deputadofederal")
            .getResultList();
        return data;
    }*/
	
}
