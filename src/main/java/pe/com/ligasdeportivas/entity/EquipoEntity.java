package pe.com.ligasdeportivas.entity;

import java.util.List;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "equipos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 100)
	private String nombre;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Estado estado; // ACTIVO, INACTIVO, SUSPENDIDO

	// Relación con Liga
	@ManyToOne
	@JoinColumn(name = "liga_id", nullable = false)
	private LigaEntity liga;

	// Relación con Entrenador
	@ManyToOne
	@JoinColumn(name = "entrenador_id")
	private EntrenadorEntity entrenador;

	// Relación con Jugadores
	@OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<JugadorEntity> jugadores;

	// Relación con partidos como equipo local
	@OneToMany(mappedBy = "equipoLocal")
	private List<PartidoEntity> partidosLocal;

	// Relación con partidos como equipo visitante
	@OneToMany(mappedBy = "equipoVisitante")
	private List<PartidoEntity> partidosVisitante;
}
