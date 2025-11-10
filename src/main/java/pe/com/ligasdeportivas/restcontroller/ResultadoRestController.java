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

import pe.com.ligasdeportivas.dto.ResultadoDTO;
import pe.com.ligasdeportivas.service.ResultadoService;

@RestController
@RequestMapping("/api/resultados")
public class ResultadoRestController {

    @Autowired
    private ResultadoService resultadoService;

    @GetMapping
    public List<ResultadoDTO> getAllResultados() {
        return resultadoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultadoDTO> getResultadoById(@PathVariable Long id) {
        Optional<ResultadoDTO> resultado = resultadoService.findById(id);
        return resultado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResultadoDTO createResultado(@RequestBody ResultadoDTO resultadoDTO) {
        return resultadoService.save(resultadoDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultadoDTO> updateResultado(@PathVariable Long id, @RequestBody ResultadoDTO resultadoDTO) {
        try {
            ResultadoDTO updatedResultado = resultadoService.update(id, resultadoDTO);
            return ResponseEntity.ok(updatedResultado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResultadoDTO> patchResultado(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        try {
            ResultadoDTO patchedResultado = resultadoService.patch(id, updates);
            return ResponseEntity.ok(patchedResultado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResultado(@PathVariable Long id) {
        if (resultadoService.findById(id).isPresent()) {
            resultadoService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/partido/{partidoId}")
    public ResponseEntity<ResultadoDTO> getResultadoByPartido(@PathVariable Long partidoId) {
        Optional<ResultadoDTO> resultado = resultadoService.findByPartidoId(partidoId);
        return resultado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    public List<ResultadoDTO> getResultadosByEstado(@PathVariable String estado) {
        return resultadoService.findByEstado(pe.com.ligasdeportivas.entity.Estado.valueOf(estado));
    }
}
