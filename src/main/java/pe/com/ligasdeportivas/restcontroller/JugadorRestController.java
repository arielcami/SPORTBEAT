package pe.com.ligasdeportivas.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pe.com.ligasdeportivas.dto.JugadorDTO;
import pe.com.ligasdeportivas.exception.ResourceAlreadyExistsException;
import pe.com.ligasdeportivas.service.JugadorService;

@RestController
@RequestMapping("/api/jugadores")
public class JugadorRestController {

	@Autowired
	private JugadorService jugadorService;

	@GetMapping
	public List<JugadorDTO> getAllJugadores() {
		return jugadorService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<JugadorDTO> getJugadorById(@PathVariable Long id) {
		Optional<JugadorDTO> jugador = jugadorService.findById(id);
		return jugador.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<?> createJugador(@RequestBody JugadorDTO jugadorDTO) {
		try {
			JugadorDTO createdJugador = jugadorService.save(jugadorDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdJugador);
		} catch (ResourceAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<JugadorDTO> updateJugador(@PathVariable Long id, @RequestBody JugadorDTO jugadorDTO) {
		try {
			JugadorDTO updatedJugador = jugadorService.update(id, jugadorDTO);
			return ResponseEntity.ok(updatedJugador);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PatchMapping("/{id}")
	public ResponseEntity<JugadorDTO> patchJugador(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
		try {
			JugadorDTO patchedJugador = jugadorService.patch(id, updates);
			return ResponseEntity.ok(patchedJugador);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteJugador(@PathVariable Long id) {
		if (jugadorService.findById(id).isPresent()) {
			jugadorService.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/equipo/{equipoId}")
	public List<JugadorDTO> getJugadoresByEquipo(@PathVariable Long equipoId) {
		return jugadorService.findByEquipoId(equipoId);
	}

	@GetMapping("/posicion/{posicion}")
	public ResponseEntity<?> getJugadoresByPosicion(@PathVariable String posicion) {
		List<JugadorDTO> jugadores = jugadorService.findByPosicion(posicion);

		if (jugadores.isEmpty()) {
			Map<String, String> response = new HashMap<>();
			response.put("mensaje", "No se encontraron jugadores en la posición: " + posicion);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}

		return ResponseEntity.ok(jugadores);
	}

	@GetMapping("/usuario/{usuarioId}")
	public ResponseEntity<JugadorDTO> getJugadorByUsuario(@PathVariable Long usuarioId) {
		Optional<JugadorDTO> jugador = jugadorService.findByUsuarioId(usuarioId);
		return jugador.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/edad")
	public ResponseEntity<?> getJugadoresByEdadRange(@RequestParam int minEdad, @RequestParam int maxEdad) {
		List<JugadorDTO> jugadores = jugadorService.findByEdadBetween(minEdad, maxEdad);

		if (jugadores.isEmpty()) {
			Map<String, String> response = new HashMap<>();
			response.put("mensaje", "No se encontraron jugadores en el rango de edad: " + minEdad + "-" + maxEdad);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}

		return ResponseEntity.ok(jugadores);
	}
}