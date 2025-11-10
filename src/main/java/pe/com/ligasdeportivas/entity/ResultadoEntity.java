package pe.com.ligasdeportivas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resultados")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultadoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private int golesLocal;

	@Column(nullable = false)
	private int golesVisitante;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Estado estado;

	// Relación con Partido
	@OneToOne
	@JoinColumn(name = "partido_id", nullable = false, unique = true)
	private PartidoEntity partido;
}
