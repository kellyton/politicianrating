package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ProjetoDeLei {

	@Id
	private long id;
	
	@Column
	private String nome;
	
	@Column(nullable = false)
	private String sigla;
	
	@Column
	private int ano;
	
	@Column
	private String dataApresentacao;
	
	@Column(columnDefinition="TEXT")
	private String ementa;
	
	@Column(unique = true, nullable = false)
	private String nomeAutor;
	
	@Column
	private String cadastroDeputado;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public String getDataApresentacao() {
		return dataApresentacao;
	}

	public void setDataApresentacao(String dataApresentacao) {
		this.dataApresentacao = dataApresentacao;
	}

	public String getEmenta() {
		return ementa;
	}

	public void setEmenta(String ementa) {
		this.ementa = ementa;
	}

	public String getNomeAutor() {
		return nomeAutor;
	}

	public void setNomeAutor(String nomeAutor) {
		this.nomeAutor = nomeAutor;
	}

	public String getCadastroDeputado() {
		return cadastroDeputado;
	}

	public void setCadastroDeputado(String cadastroDeputado) {
		this.cadastroDeputado = cadastroDeputado;
	}
	
	@Override
	public String toString(){
		return sigla + " - " + nome;
	}
	
}
