package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SenadorGasto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	int ano;
	
	@Column
	int mes;
	
	@Column
	private String senador; //Senador.nomeParlamentar 
	
	@Column
	private String tipo_depesa;
	
	@Column
	private String cnpj_cpf;
	
	@Column
	private String fornecedor;
	
	@Column
	private String documento;
	
	@Column
	private Date data;
	
	@Column
	private String detalhamento;
	
	@Column
	private float valor_reembolsado;

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public String getSenador() {
		return senador;
	}

	public void setSenador(String senador) {
		this.senador = senador;
	}

	public String getTipo_depesa() {
		return tipo_depesa;
	}

	public void setTipo_depesa(String tipo_depesa) {
		this.tipo_depesa = tipo_depesa;
	}

	public String getCnpj_cpf() {
		return cnpj_cpf;
	}

	public void setCnpj_cpf(String cnpj_cpf) {
		this.cnpj_cpf = cnpj_cpf;
	}

	public String getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getDetalhamento() {
		return detalhamento;
	}

	public void setDetalhamento(String detalhamento) {
		this.detalhamento = detalhamento;
	}

	public float getValor_reembolsado() {
		return valor_reembolsado;
	}

	public void setValor_reembolsado(float valor_reembolsado) {
		this.valor_reembolsado = valor_reembolsado;
	}

}
