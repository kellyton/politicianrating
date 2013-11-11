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

/**
 * O NuCarteiraParlamentar aqui é o mesmo numero da matricula dos DeputadosFederais
 * Dicionario de dados:
 * http://www2.camara.leg.br/transparencia/cota-para-exercicio-da-atividade-parlamentar/explicacoes-sobre-o-formato-dos-arquivos-xml  
 * @author kellyton
 *
 */

@Entity
public class DeputadoFederalGasto {
	
	/*
	 * The key should be a composite key to avoid duplicity on insert
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
	private String txNomeParlamentar;
	
	@Column
	private String nuCarteiraParlamentar;
	
	@Column
	private String nuLegislatura;
	
	@Column
	private String sgUF;
	
	@Column
	private String sgPartido;
	
	@Column
	private String codLegislatura;
	
	@Column
	private String numSubCota;
	
	@Column
	private String txtDescricao;
	
	@Column
	private String numEspecificacaoSubCota;
	
	@Column
	private String txtDescricaoEspecificacao;
	
	@Column
	private String txtFornecedor;
	
	@Column
	private String txtCNPJCPF;
	
	@Column
	private String txtNumero;
	
	@Column
	private String indTipoDocumento;
	
	@Column
	private String datEmissao;
	
	@Column
	private float vlrDocumento;
	
	@Column
	private float vlrGlosa;
	
	@Column
	private float vlrLiquido;
	
	@Column
	private int numMes;
	
	@Column
	private int numAno;
	
	@Column
	private int numParcela;
	
	@Column
	private String txtPassageiro;
	
	@Column
	private String txtTrecho;
	
	@Column
	private String numLote;
	
	@Column
	private String numRessarcimento;

	@Column(nullable = false)
	private Date createdAt;

	@Column(nullable = true)
	private Date updatedAt;
	
	public String getTxNomeParlamentar() {
		return txNomeParlamentar;
	}

	public void setTxNomeParlamentar(String txNomeParlamentar) {
		this.txNomeParlamentar = txNomeParlamentar;
	}

	public String getNuCarteiraParlamentar() {
		return nuCarteiraParlamentar;
	}

	public void setNuCarteiraParlamentar(String nuCarteiraParlamentar) {
		this.nuCarteiraParlamentar = nuCarteiraParlamentar;
	}

	public String getNuLegislatura() {
		return nuLegislatura;
	}

	public void setNuLegislatura(String nuLegislatura) {
		this.nuLegislatura = nuLegislatura;
	}

	public String getSgUF() {
		return sgUF;
	}

	public void setSgUF(String sgUF) {
		this.sgUF = sgUF;
	}

	public String getSgPartido() {
		return sgPartido;
	}

	public void setSgPartido(String sgPartido) {
		this.sgPartido = sgPartido;
	}

	public String getCodLegislatura() {
		return codLegislatura;
	}

	public void setCodLegislatura(String codLegislatura) {
		this.codLegislatura = codLegislatura;
	}

	public String getNumSubCota() {
		return numSubCota;
	}

	public void setNumSubCota(String numSubCota) {
		this.numSubCota = numSubCota;
	}

	public String getTxtDescricao() {
		return txtDescricao;
	}

	public void setTxtDescricao(String txtDescricao) {
		this.txtDescricao = txtDescricao;
	}

	public String getNumEspecificacaoSubCota() {
		return numEspecificacaoSubCota;
	}

	public void setNumEspecificacaoSubCota(String numEspecificacaoSubCota) {
		this.numEspecificacaoSubCota = numEspecificacaoSubCota;
	}

	public String getTxtDescricaoEspecificacao() {
		return txtDescricaoEspecificacao;
	}

	public void setTxtDescricaoEspecificacao(String txtDescricaoEspecificacao) {
		this.txtDescricaoEspecificacao = txtDescricaoEspecificacao;
	}

	public String getTxtFornecedor() {
		return txtFornecedor;
	}

	public void setTxtFornecedor(String txtFornecedor) {
		this.txtFornecedor = txtFornecedor;
	}

	public String getTxtCNPJCPF() {
		return txtCNPJCPF;
	}

	public void setTxtCNPJCPF(String txtCNPJCPF) {
		this.txtCNPJCPF = txtCNPJCPF;
	}

	public String getTxtNumero() {
		return txtNumero;
	}

	public void setTxtNumero(String txtNumero) {
		this.txtNumero = txtNumero;
	}

	public String getIndTipoDocumento() {
		return indTipoDocumento;
	}

	public void setIndTipoDocumento(String indTipoDocumento) {
		this.indTipoDocumento = indTipoDocumento;
	}

	public String getDatEmissao() {
		return datEmissao;
	}

	public void setDatEmissao(String datEmissao) {
		this.datEmissao = datEmissao;
	}

	public float getVlrDocumento() {
		return vlrDocumento;
	}

	public void setVlrDocumento(float vlrDocumento) {
		this.vlrDocumento = vlrDocumento;
	}

	public float getVlrGlosa() {
		return vlrGlosa;
	}

	public void setVlrGlosa(float vlrGlosa) {
		this.vlrGlosa = vlrGlosa;
	}

	public float getVlrLiquido() {
		return vlrLiquido;
	}

	public void setVlrLiquido(float vlrLiquido) {
		this.vlrLiquido = vlrLiquido;
	}

	public int getNumMes() {
		return numMes;
	}

	public void setNumMes(int numMes) {
		this.numMes = numMes;
	}

	public int getNumAno() {
		return numAno;
	}

	public void setNumAno(int numAno) {
		this.numAno = numAno;
	}

	public int getNumParcela() {
		return numParcela;
	}

	public void setNumParcela(int numParcela) {
		this.numParcela = numParcela;
	}

	public String getTxtPassageiro() {
		return txtPassageiro;
	}

	public void setTxtPassageiro(String txtPassageiro) {
		this.txtPassageiro = txtPassageiro;
	}

	public String getTxtTrecho() {
		return txtTrecho;
	}

	public void setTxtTrecho(String txtTrecho) {
		this.txtTrecho = txtTrecho;
	}

	public String getNumLote() {
		return numLote;
	}

	public void setNumLote(String numLote) {
		this.numLote = numLote;
	}

	public String getNumRessarcimento() {
		return numRessarcimento;
	}

	public void setNumRessarcimento(String numRessarcimento) {
		this.numRessarcimento = numRessarcimento;
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
	
}
