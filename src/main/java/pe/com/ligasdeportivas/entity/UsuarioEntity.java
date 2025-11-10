package pe.com.ligasdeportivas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 50)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, unique = true, length = 100)
	private String email;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Rol rol; // ADMIN, ENTRENADOR, JUGADOR

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Estado estado; // ACTIVO, INACTIVO, SUSPENDIDO

	// getters
	public String getRol() {
		return this.rol.name();
	}

	public String getEstado() {
		return this.estado.name();
	}

	// setters
	public void setRol(String rol) {
		this.rol = Rol.valueOf(rol);
	}
	
	public void setEstado(String estado) {
		this.estado = Estado.valueOf(estado);
	}

}
