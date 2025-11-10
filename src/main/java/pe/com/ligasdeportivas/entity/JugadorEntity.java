package pe.com.ligasdeportivas.entity;

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
@Table(name = "jugadores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JugadorEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String nombre;

	@Column(nullable = false, length = 100)
	private String apellido;

	@Column(nullable = false)
	private int edad;

	@Column(length = 50)
	private String posicion; // Ej: Defensa, Delantero, Arquero

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Estado estado; // ACTIVO, INACTIVO

	// Relación con Usuario
	@OneToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	private UsuarioEntity usuario;

	// Relación con Equipo
	@ManyToOne
	@JoinColumn(name = "equipo_id", nullable = false)
	private EquipoEntity equipo;
}
