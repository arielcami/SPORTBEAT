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
import pe.com.ligasdeportivas.dto.EntrenadorDTO;
import pe.com.ligasdeportivas.service.EntrenadorService;

@RestController
@RequestMapping("/api/entrenadores")
public class EntrenadorRestController {

	@Autowired
	private EntrenadorService entrenadorService;

	@GetMapping
	public List<EntrenadorDTO> getAllEntrenadores() {
		return entrenadorService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<EntrenadorDTO> getEntrenadorById(@PathVariable Long id) {
		Optional<EntrenadorDTO> entrenador = entrenadorService.findById(id);
		return entrenador.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public EntrenadorDTO createEntrenador(@RequestBody EntrenadorDTO entrenadorDTO) {
		return entrenadorService.save(entrenadorDTO);
	}

	@PutMapping("/{id}")
	public ResponseEntity<EntrenadorDTO> updateEntrenador(@PathVariable Long id,
			@RequestBody EntrenadorDTO entrenadorDTO) {
		try {
			EntrenadorDTO updatedEntrenador = entrenadorService.update(id, entrenadorDTO);
			return ResponseEntity.ok(updatedEntrenador);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PatchMapping("/{id}")
	public ResponseEntity<EntrenadorDTO> patchEntrenador(@PathVariable Long id,
			@RequestBody Map<String, Object> updates) {
		try {
			EntrenadorDTO patchedEntrenador = entrenadorService.patch(id, updates);
			return ResponseEntity.ok(patchedEntrenador);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEntrenador(@PathVariable Long id) {
		if (entrenadorService.findById(id).isPresent()) {
			entrenadorService.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/usuario/{usuarioId}")
	public ResponseEntity<EntrenadorDTO> getEntrenadorByUsuario(@PathVariable Long usuarioId) {
		Optional<EntrenadorDTO> entrenador = entrenadorService.findByUsuarioId(usuarioId);
		return entrenador.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/estado/{estado}")
	public List<EntrenadorDTO> getEntrenadoresByEstado(@PathVariable String estado) {
		return entrenadorService.findByEstado(pe.com.ligasdeportivas.entity.Estado.valueOf(estado));
	}

	@GetMapping("/buscar/nombre")
	public ResponseEntity<?> buscarPorNombre(@RequestParam String nombre) {
	    List<EntrenadorDTO> entrenadores = entrenadorService.buscarPorNombre(nombre);
	    
	    if (entrenadores.isEmpty()) {
	        Map<String, String> response = new HashMap<>();
	        response.put("mensaje", "No se encontraron entrenadores con el nombre: " + nombre);
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	    }
	    
	    return ResponseEntity.ok(entrenadores);
	}

	@GetMapping("/buscar/apellido")
	public ResponseEntity<?> buscarPorApellido(@RequestParam String apellido) {
	    List<EntrenadorDTO> entrenadores = entrenadorService.buscarPorApellido(apellido);
	    
	    if (entrenadores.isEmpty()) {
	        Map<String, String> response = new HashMap<>();
	        response.put("mensaje", "No se encontraron entrenadores con el apellido: " + apellido);
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	    }
	    
	    return ResponseEntity.ok(entrenadores);
	}
}