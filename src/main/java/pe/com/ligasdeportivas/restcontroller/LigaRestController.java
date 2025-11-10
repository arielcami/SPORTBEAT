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
import org.springframework.web.bind.annotation.RestController;
import pe.com.ligasdeportivas.dto.LigaDTO;
import pe.com.ligasdeportivas.service.LigaService;

@RestController
@RequestMapping("/api/ligas")
public class LigaRestController {

	@Autowired
	private LigaService ligaService;

	@GetMapping
	public List<LigaDTO> getAllLigas() {
		return ligaService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<LigaDTO> getLigaById(@PathVariable Long id) {
		Optional<LigaDTO> liga = ligaService.findById(id);
		return liga.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public LigaDTO createLiga(@RequestBody LigaDTO liga) {
		return ligaService.save(liga);
	}

	@PutMapping("/{id}")
	public ResponseEntity<LigaDTO> updateLiga(@PathVariable Long id, @RequestBody LigaDTO liga) {
		try {
			LigaDTO updatedLiga = ligaService.update(id, liga);
			return ResponseEntity.ok(updatedLiga);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PatchMapping("/{id}")
	public ResponseEntity<LigaDTO> patchLiga(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
		try {
			LigaDTO patchedLiga = ligaService.patch(id, updates);
			return ResponseEntity.ok(patchedLiga);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteLiga(@PathVariable Long id) {
		if (ligaService.findById(id).isPresent()) {
			ligaService.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/distrito/{distrito}")
	public ResponseEntity<?> getLigasByDistrito(@PathVariable String distrito) {
	    List<LigaDTO> ligas = ligaService.findByDistrito(distrito);
	    
	    if (ligas.isEmpty()) {
	        Map<String, String> response = new HashMap<>();
	        response.put("mensaje", "No se encontraron ligas en el distrito: " + distrito);
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	    }
	    
	    return ResponseEntity.ok(ligas);
	}

	@GetMapping("/estado/{estado}")
	public List<LigaDTO> getLigasByEstado(@PathVariable String estado) {
		return ligaService.findByEstado(pe.com.ligasdeportivas.entity.Estado.valueOf(estado));
	}

}