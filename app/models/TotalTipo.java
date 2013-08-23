package models;

import java.text.DecimalFormat;

import javax.persistence.Entity;

public class TotalTipo {

	String cnpj, descricao, detalhe;
	double totalGasto;
	
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getDetalhe() {
		return detalhe;
	}
	public void setDetalhe(String detalhe) {
		this.detalhe = detalhe;
	}
	public double getTotalGasto() {
		return totalGasto;
	}
	public void setTotalGasto(double totalGasto) {
		this.totalGasto = totalGasto;
	}
	public String getTotalGastoFormated() {
		return new DecimalFormat("#,###.00").format(totalGasto);  
	}
	
	public String toString(){
		return cnpj + "#" + descricao + "#" + detalhe + ":" +  getTotalGastoFormated();
	}
}
