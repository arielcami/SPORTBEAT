package pe.com.ligasdeportivas.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "partidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartidoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate fecha;

	private LocalTime hora;

	@Column(nullable = false, length = 100)
	private String lugar;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Estado estado; // PROGRAMADO, JUGADO, CANCELADO

	// Relación con Liga
	@ManyToOne
	@JoinColumn(name = "liga_id", nullable = false)
	private LigaEntity liga;

	// Relación con equipo local
	@ManyToOne
	@JoinColumn(name = "equipo_local_id", nullable = false)
	private EquipoEntity equipoLocal;

	// Relación con equipo visitante
	@ManyToOne
	@JoinColumn(name = "equipo_visitante_id", nullable = false)
	private EquipoEntity equipoVisitante;

	// Relación con Resultado
	@OneToOne(mappedBy = "partido", cascade = CascadeType.ALL, orphanRemoval = true)
	private ResultadoEntity resultado;
}
