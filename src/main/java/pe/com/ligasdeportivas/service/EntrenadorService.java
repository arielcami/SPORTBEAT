package pe.com.ligasdeportivas.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import pe.com.ligasdeportivas.dto.EntrenadorDTO;
import pe.com.ligasdeportivas.entity.Estado;

public interface EntrenadorService {

	List<EntrenadorDTO> findAll();

	Optional<EntrenadorDTO> findById(Long id);

	EntrenadorDTO save(EntrenadorDTO entrenadorDTO);

	EntrenadorDTO update(Long id, EntrenadorDTO entrenadorDTO);

	EntrenadorDTO patch(Long id, Map<String, Object> updates);

	void deleteById(Long id);

	Optional<EntrenadorDTO> findByUsuarioId(Long usuarioId);

	List<EntrenadorDTO> findByEstado(Estado estado);

	List<EntrenadorDTO> buscarPorNombre(String nombre);

	List<EntrenadorDTO> buscarPorApellido(String apellido);
}