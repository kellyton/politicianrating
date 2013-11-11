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
import java.text.ParseException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.swing.text.MaskFormatter;

import util.CnpjCpf;
import util.DateUtil;

@Entity
public class Empresa {

	@Id
	String cnpj; //can be cpf
	@Column
	Date dataAbertura;
	@Column
	String nome;
	@Column
	String fantasia;
	@Column
	String atividadePrincipal;
	@Column
	String naturezaJuridica;
	@Column
	String logradouro;
	@Column
	String numero;
	@Column
	String complemento;
	@Column
	String cep;
	@Column
	String bairro;
	@Column
	String cidade;
	@Column
	String estado;
	@Column
	String situacao;
	@Column
	Date dataSituacao;
	@Column
	double totalRecebido;
	@Column
	double latitude;
	@Column
	double longitude;
	@Column
	boolean dadosAtualizados;
	
	@Column(nullable = false)
	private Date createdAt;

	@Column(nullable = true)
	private Date updatedAt;
	
	public String getCnpj() {
		return cnpj;
	}
	
	public String getCnpjFormatted(){
		MaskFormatter mask;
        try {
        	if (cnpj.length() == 11){ //cpf
        		mask = new MaskFormatter("###.###.###-##");
        	} else {
        		mask = new MaskFormatter("##.###.###/####-##");
        	}
		    mask.setValueContainsLiteralCharacters(false);
		    return mask.valueToString(cnpj);
        } catch (ParseException e) {
			return "";
		}
	}
	
	public void setCnpj(String cnpj) {
		cnpj = cnpj.replace(".","")
				.replace("/", "")
				.replace("-", "");
		
		if (cnpj.length() > 14){
			cnpj = cnpj.substring(0, 14);
		}
		this.cnpj = cnpj;
	}
	
	public Date getDataAbertura() {
		return dataAbertura;
	}
	public String getDataAberturaFormatted(){
		if (dataAbertura != null) {
			return DateUtil.getFormattedDate(dataAbertura);
		} else {
			return "";
		}
	}
	public void setDataAbertura(String data) {
		this.dataAbertura = DateUtil.parseToDate(data);
	}
	public void setDataAbertura(Date data) {
		this.dataAbertura = data;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getFantasia() {
		return fantasia;
	}
	public void setFantasia(String fantasia) {
		this.fantasia = fantasia;
	}
	public String getAtividadePrincipal() {
		return atividadePrincipal;
	}
	public void setAtividadePrincipal(String atividadePrincipal) {
		this.atividadePrincipal = atividadePrincipal;
	}
	public String getNaturezaJuridica() {
		return naturezaJuridica;
	}
	public void setNaturezaJuridica(String naturezaJuridica) {
		this.naturezaJuridica = naturezaJuridica;
	}
	public String getLogradouro() {
		return logradouro;
	}
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getSituacao() {
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	public Date getDataSituacao() {
		return dataSituacao;
	}
	public String getDataSituacaoFormatted(){
		if (dataSituacao != null) {
			return DateUtil.getFormattedDate(dataSituacao);
		} else {
			return "";
		}
	}
	public void setDataSituacao(String dataSituacao) {
		this.dataSituacao = DateUtil.parseToDate(dataSituacao);
	}
	public void setDataSituacao(Date dataSituacao) {
		this.dataSituacao = dataSituacao;
	}
	public double getTotalRecebido() {
		return totalRecebido;
	}
	public String getTotalRecebidoFormated() {
		return new DecimalFormat("#,###.00").format(totalRecebido);  
	}
	public void setTotalRecebido(double totalRecebido) {
		this.totalRecebido = totalRecebido;
	}

	public boolean isDadosAtualizados() {
		return dadosAtualizados;
	}

	public void setDadosAtualizados(boolean dadosAtualizados) {
		this.dadosAtualizados = dadosAtualizados;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
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
	public String getUpdatedAtFormatted(){
		if (updatedAt != null) {
			return DateUtil.getFormattedDate(updatedAt);
		} else {
			return "";
		}
	}
	@PreUpdate
	private void setUpdatedAt() {
		this.updatedAt = new Date();
	}		
	
	public boolean isCPF(){
		return CnpjCpf.isCPF(cnpj);
	}
	
	public boolean isCNPJ(){
		return CnpjCpf.isCNPJ(cnpj);
	}
	
	@Override
	public String toString(){
		return getCnpjFormatted() + " - " + fantasia;
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof Empresa){
			
			Empresa other = (Empresa)o;
			return this.cnpj.equals(other.cnpj);
		} else {
			return false;
		}
	}

}
