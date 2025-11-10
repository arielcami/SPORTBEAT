package pe.com.ligasdeportivas.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import pe.com.ligasdeportivas.entity.UsuarioEntity;
import pe.com.ligasdeportivas.service.UsuarioService;

@RestController
@RequestMapping("/api/user")
public class UsuarioRestController {

	private final PasswordEncoder passwordEncoder;
	private final UsuarioService service;

	public UsuarioRestController(PasswordEncoder passwordEncoder, UsuarioService service) {
		this.passwordEncoder = passwordEncoder;
		this.service = service;
	}

	@GetMapping("/list")
	public List<UsuarioEntity> getAllUsuarios() {
	    return service.listarUsuarios();
	}

	@PostMapping("/create")
	public boolean crearUsuario(@RequestBody UsuarioEntity usuario) {
		usuario.setRol(usuario.getRol().toUpperCase());
		usuario.setEstado(usuario.getEstado().toUpperCase());
		return service.crearUsuario(usuario);
	}

	@PatchMapping("/update/{id}")
	public ResponseEntity<?> actualizarUsuarioParcial(@PathVariable Long id,
			@RequestBody Map<String, Object> camposActualizados) {

		try {
			// Validar que el Map no esté vacío
			if (camposActualizados == null || camposActualizados.isEmpty()) {
				return ResponseEntity.badRequest().body("No se proporcionaron campos para actualizar");
			}

			boolean resultado = service.actualizarUsuarioParcial(id, camposActualizados);

			if (resultado) {
				return ResponseEntity.ok().body("Usuario actualizado correctamente");
			} else {
				return ResponseEntity.badRequest().body(
						"Error al actualizar usuario. Verifique que el usuario exista y que los datos sean válidos.");
			}

		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Error interno del servidor");
		}
	}

	// Endpoint para login
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
	    try {
	        Optional<UsuarioEntity> usuarioOpt = service.buscarPorUsernameOEmail(loginRequest.getUsername());
	        
	        if (usuarioOpt.isEmpty()) {
	            // Retornar JSON en lugar de texto plano
	            Map<String, String> errorResponse = new HashMap<>();
	            errorResponse.put("error", "Usuario no encontrado");
	            return ResponseEntity.status(401).body(errorResponse);
	        }
	        
	        UsuarioEntity usuario = usuarioOpt.get();
	        
	        if (!passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword())) {
	            Map<String, String> errorResponse = new HashMap<>();	
	            errorResponse.put("error", "Contraseña incorrecta");
	            return ResponseEntity.status(401).body(errorResponse);
	        }
	        
	        if (!"ACTIVO".equals(usuario.getEstado())) {
	            Map<String, String> errorResponse = new HashMap<>();
	            errorResponse.put("error", "Usuario inactivo");
	            return ResponseEntity.status(401).body(errorResponse);
	        }

			// Crear objeto de sesión sin la contraseña
			UsuarioSession usuarioSession = new UsuarioSession(usuario.getId(), usuario.getUsername(),
					usuario.getEmail(), usuario.getRol(), usuario.getEstado());

			// Guardar en sesión con 2 HORAS de duración (7200 segundos)
			session.setAttribute("usuario", usuarioSession);
			session.setMaxInactiveInterval(2 * 60 * 60); // 2 horas = 7200 segundos

			LoginResponse response = new LoginResponse(usuario.getId(), usuario.getUsername(), usuario.getEmail(),
					usuario.getRol(), usuario.getEstado(), "Login exitoso");

			return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        Map<String, String> errorResponse = new HashMap<>();
	        errorResponse.put("error", "Error interno del servidor");
	        return ResponseEntity.internalServerError().body(errorResponse);
	    }
	}
	
	
	// Cerrar sesión
	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpSession session) {
	    try {
	        session.invalidate();
	        Map<String, String> response = new HashMap<>();
	        response.put("mensaje", "Sesión cerrada correctamente");
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        Map<String, String> errorResponse = new HashMap<>();
	        errorResponse.put("error", "Error al cerrar sesión");
	        return ResponseEntity.internalServerError().body(errorResponse);
	    }
	}

	// Clase interna para la request de login
	public static class LoginRequest {
		private String username;
		private String password;

		// Getters y Setters
		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}

	// Verificar sesión activa
	@GetMapping("/sesion")
	public ResponseEntity<?> verificarSesion(HttpSession session) {
		UsuarioSession usuarioSession = (UsuarioSession) session.getAttribute("usuario");

		if (usuarioSession != null) {
			// Calcular tiempo restante de sesión
			long tiempoTranscurrido = System.currentTimeMillis() - usuarioSession.getTimestamp();
			long tiempoRestante = (2 * 60 * 60 * 1000) - tiempoTranscurrido; // 2 horas en milisegundos
			long minutosRestantes = tiempoRestante / (60 * 1000);

			Map<String, Object> response = new HashMap<>();
			response.put("usuario", usuarioSession);
			response.put("minutosRestantes", minutosRestantes > 0 ? minutosRestantes : 0);
			response.put("sesionActiva", true);

			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.status(401).body("No hay sesión activa");
		}
	}

	// Clase interna para la response de login
	public static class LoginResponse {
		private final Long id;
		private final String username;
		private final String email;
		private final String rol;
		private final String estado;
		private final String mensaje;

		public LoginResponse(Long id, String username, String email, String rol, String estado, String mensaje) {
			this.id = id;
			this.username = username;
			this.email = email;
			this.rol = rol;
			this.estado = estado;
			this.mensaje = mensaje;
		}

		// Getters
		public Long getId() {
			return id;
		}

		public String getUsername() {
			return username;
		}

		public String getEmail() {
			return email;
		}

		public String getRol() {
			return rol;
		}

		public String getEstado() {
			return estado;
		}

		public String getMensaje() {
			return mensaje;
		}
	}

	public static class UsuarioSession {
		private final Long id;
		private final String username;
		private final String email;
		private final String rol;
		private final String estado;
		private final long timestamp;

		public UsuarioSession(Long id, String username, String email, String rol, String estado) {
			this.id = id;
			this.username = username;
			this.email = email;
			this.rol = rol;
			this.estado = estado;
			this.timestamp = System.currentTimeMillis(); // Marca de tiempo
		}

		// Getters
		public Long getId() {
			return id;
		}

		public String getUsername() {
			return username;
		}

		public String getEmail() {
			return email;
		}

		public String getRol() {
			return rol;
		}

		public String getEstado() {
			return estado;
		}

		public long getTimestamp() {
			return timestamp;
		}
	}
}