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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Entity
public class Senador implements IPolitico{
	
	@Id
	private String codParlamentar;
	
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
	
	public String getCodParlamentar() {
		return codParlamentar;
	}
	public void setCodParlamentar(String codParlamentar) {
		this.codParlamentar = codParlamentar;
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
	public Date getNascimento() {
		return nascimento;
	}
	public void setNascimento(Date nascimento) {
		this.nascimento = nascimento;
	}
	public String getPhotoURL() {
		return photoURL;
	}
	public void setPhotoURL(String photoURL) {
		this.photoURL = photoURL;
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
	public boolean isDeputado() {
		return false;
	}
	
}
