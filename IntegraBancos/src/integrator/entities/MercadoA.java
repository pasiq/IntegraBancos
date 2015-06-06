package integrator.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: MercadoA
 *
 */

@Entity
@Table(name="mercado_a")
public class MercadoA implements Serializable {

	   
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private String produto;
	private BigDecimal preco;
	private static final long serialVersionUID = 1L;

	public MercadoA() {
		super();
	}   
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}   
	public String getDescricao() {
		return this.produto;
	}

	public void setDescricao(String produto) {
		this.produto = produto;
	}
	
	public BigDecimal getPreco(BigDecimal preco) {
		return this.preco;
	}
	
	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}
}
