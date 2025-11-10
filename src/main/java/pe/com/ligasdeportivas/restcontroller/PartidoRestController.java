package pe.com.ligasdeportivas.restcontroller;

import java.time.LocalDate;
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

import pe.com.ligasdeportivas.dto.PartidoDTO;
import pe.com.ligasdeportivas.service.PartidoService;

@RestController
@RequestMapping("/api/partidos")
public class PartidoRestController {

    @Autowired
    private PartidoService partidoService;

    @GetMapping
    public List<PartidoDTO> getAllPartidos() {
        return partidoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartidoDTO> getPartidoById(@PathVariable Long id) {
        Optional<PartidoDTO> partido = partidoService.findById(id);
        return partido.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createPartido(@RequestBody PartidoDTO partidoDTO) {
        try {
            PartidoDTO savedPartido = partidoService.save(partidoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPartido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePartido(@PathVariable Long id, @RequestBody PartidoDTO partidoDTO) {
        try {
            PartidoDTO updatedPartido = partidoService.update(id, partidoDTO);
            return ResponseEntity.ok(updatedPartido);
        } catch (IllegalArgumentException e) {
            // Validación de negocio (equipos iguales)
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            // Recurso no encontrado
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PartidoDTO> patchPartido(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        try {
            PartidoDTO patchedPartido = partidoService.patch(id, updates);
            return ResponseEntity.ok(patchedPartido);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartido(@PathVariable Long id) {
        if (partidoService.findById(id).isPresent()) {
            partidoService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/liga/{ligaId}")
    public List<PartidoDTO> getPartidosByLiga(@PathVariable Long ligaId) {
        return partidoService.findByLigaId(ligaId);
    }

    @GetMapping("/estado/{estado}")
    public List<PartidoDTO> getPartidosByEstado(@PathVariable String estado) {
        return partidoService.findByEstado(pe.com.ligasdeportivas.entity.Estado.valueOf(estado));
    }

    @GetMapping("/fecha/{fecha}")
    public List<PartidoDTO> getPartidosByFecha(@PathVariable String fecha) {
        LocalDate localDate = LocalDate.parse(fecha);
        return partidoService.findByFecha(localDate);
    }

    @GetMapping("/equipo/{equipoId}")
    public List<PartidoDTO> getPartidosByEquipo(@PathVariable Long equipoId) {
        return partidoService.findByEquipoId(equipoId);
    }

    @GetMapping("/proximos")
    public List<PartidoDTO> getProximosPartidos() {
        return partidoService.findProximosPartidos();
    }

    @GetMapping("/jugados")
    public List<PartidoDTO> getPartidosJugados() {
        return partidoService.findPartidosJugados();
    }

    @GetMapping("/rango-fechas")
    public List<PartidoDTO> getPartidosByRangoFechas(
            @RequestParam String inicio,
            @RequestParam String fin) {
        LocalDate startDate = LocalDate.parse(inicio);
        LocalDate endDate = LocalDate.parse(fin);
        return partidoService.findByFechaBetween(startDate, endDate);
    }
}
