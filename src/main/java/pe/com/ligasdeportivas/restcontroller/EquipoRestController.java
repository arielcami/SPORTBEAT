package pe.com.ligasdeportivas.restcontroller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.ligasdeportivas.dto.EquipoDTO;
import pe.com.ligasdeportivas.service.EquipoService;

@RestController
@RequestMapping("/api/equipos")
public class EquipoRestController {

	@Autowired
	private EquipoService equipoService;

	@GetMapping
	public List<EquipoDTO> getAllEquipos() {
		return equipoService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<EquipoDTO> getEquipoById(@PathVariable Long id) {
		Optional<EquipoDTO> equipo = equipoService.findById(id);
		return equipo.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public EquipoDTO createEquipo(@RequestBody EquipoDTO equipo) {
		return equipoService.save(equipo);
	}

	@PutMapping("/{id}")
	public ResponseEntity<EquipoDTO> updateEquipo(@PathVariable Long id, @RequestBody EquipoDTO equipo) {
		try {
			EquipoDTO updatedEquipo = equipoService.update(id, equipo);
			return ResponseEntity.ok(updatedEquipo);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PatchMapping("/{id}")
	public ResponseEntity<EquipoDTO> patchEquipo(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
		try {
			EquipoDTO patchedEquipo = equipoService.patch(id, updates);
			return ResponseEntity.ok(patchedEquipo);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEquipo(@PathVariable Long id) {
		if (equipoService.findById(id).isPresent()) {
			equipoService.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/liga/{ligaId}")
	public List<EquipoDTO> getEquiposByLiga(@PathVariable Long ligaId) {
		return equipoService.findByLigaId(ligaId);
	}

	@GetMapping("/entrenador/{entrenadorId}")
	public List<EquipoDTO> getEquiposByEntrenador(@PathVariable Long entrenadorId) {
		return equipoService.findByEntrenadorId(entrenadorId);
	}
}
