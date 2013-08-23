package models;

import java.text.DecimalFormat;

import javax.persistence.Entity;

public class TotalData {

	int ano, mes;
	double totalGasto;
	
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
		return ano + "/" + mes + ":" +  getTotalGastoFormated();
	}
	
}
