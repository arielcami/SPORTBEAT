package pe.com.ligasdeportivas.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import pe.com.ligasdeportivas.dto.EquipoDTO;
import pe.com.ligasdeportivas.entity.Estado;

public interface EquipoService {

	List<EquipoDTO> findAll();

	Optional<EquipoDTO> findById(Long id);

	EquipoDTO save(EquipoDTO equipo);

	EquipoDTO update(Long id, EquipoDTO equipo);

	EquipoDTO patch(Long id, Map<String, Object> updates);

	void deleteById(Long id);

	List<EquipoDTO> findByLigaId(Long ligaId);

	List<EquipoDTO> findByEstado(Estado estado);

	List<EquipoDTO> findByEntrenadorId(Long entrenadorId);

	boolean existsByNombre(String nombre);
}
