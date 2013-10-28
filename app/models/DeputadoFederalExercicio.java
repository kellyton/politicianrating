package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.joda.time.DateTime;
import org.joda.time.Days;

@Entity
public class DeputadoFederalExercicio {

	/*
	 * The key should be a composite key (ideCadastro and idHistorico) to avoid duplicity on insert
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
	private String ideCadastro;
	
	@Column(nullable = true)
	private String idHistorico;
	
	@Column
	private String situacao;
	
	@Column(nullable = false)
	private Date dataInicio;
	
	@Column(nullable = true)
	private Date dataFim;
	
	@Column
	private String descricaoFim;
	
	@Column(nullable = false)
	private Date createdAt;

	@Column(nullable = true)
	private Date updatedAt;
	
	/////////////////////////////////////////////////
	
	/**
	 * O cálculo do número de dias é feito até hoje, se em aberto, ou até o dia final real
	 * @return
	 */
	public int getNumDays(){
		int days = 0;
		if (dataFim != null){
			days = Days.daysBetween(new DateTime(dataInicio), new DateTime(dataFim)).getDays();
		} else {
			days = Days.daysBetween(new DateTime(dataInicio), new DateTime(new Date())).getDays();
		}
		
		return days;
	}
	
	////////////////////////////////////////////////
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIdeCadastro() {
		return ideCadastro;
	}
	
	public void setIdeCadastro(String ideCadastro) {
		this.ideCadastro = ideCadastro;
	}

	public String getIdHistorico() {
		return idHistorico;
	}

	public void setIdHistorico(String idHistorico) {
		this.idHistorico = idHistorico;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Date getDataInicio() {
		return dataInicio;
	}
	
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	
	public Date getDataFim() {
		return dataFim;
	}
	
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	
	public String getDescricaoFim() {
		return descricaoFim;
	}

	public void setDescricaoFim(String descricaoFim) {
		this.descricaoFim = descricaoFim;
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
		return ideCadastro + " - " + dataInicio + " - " + dataFim + " - " + getNumDays();
	}
}
