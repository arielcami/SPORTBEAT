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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ligas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 100)
	private String nombre;

	@Column(nullable = false, length = 100)
	private String distrito;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Estado estado; // ACTIVO, INACTIVO, FINALIZADO

	// Relación con equipos
	@OneToMany(mappedBy = "liga", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EquipoEntity> equipos;

	// Relación con partidos
	@OneToMany(mappedBy = "liga", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PartidoEntity> partidos;
}