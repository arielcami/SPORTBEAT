package pe.com.ligasdeportivas.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import pe.com.ligasdeportivas.dto.LigaDTO;
import pe.com.ligasdeportivas.entity.Estado;

public interface LigaService {
	List<LigaDTO> findAll();

	Optional<LigaDTO> findById(Long id);

	LigaDTO save(LigaDTO ligaDTO);

	LigaDTO update(Long id, LigaDTO ligaDTO);

	LigaDTO patch(Long id, Map<String, Object> updates);

	void deleteById(Long id);

	List<LigaDTO> findByDistrito(String distrito);

	List<LigaDTO> findByEstado(Estado estado);

	boolean existsByNombre(String nombre);
}