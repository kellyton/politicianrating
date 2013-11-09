package models.util;

import java.text.DecimalFormat;

public class PoliticoValor {

	String ideCadastro;
	String nome;
	String estado;
	String partido;
	double valor;
	
	public String getIdeCadastro() {
		return ideCadastro;
	}
	public void setIdeCadastro(String ideCadastro) {
		this.ideCadastro = ideCadastro;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public double getValor() {
		return valor;
	}
	public String getValorFormated(){
		return new DecimalFormat("#,###.00").format(valor);
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getPartido() {
		return partido;
	}
	public void setPartido(String partido) {
		this.partido = partido;
	}
}
