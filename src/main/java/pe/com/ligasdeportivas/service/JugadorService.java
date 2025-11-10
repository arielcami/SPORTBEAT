package pe.com.ligasdeportivas.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import pe.com.ligasdeportivas.dto.JugadorDTO;
import pe.com.ligasdeportivas.entity.Estado;

public interface JugadorService {
	List<JugadorDTO> findAll();

	Optional<JugadorDTO> findById(Long id);

	JugadorDTO save(JugadorDTO jugadorDTO);

	JugadorDTO update(Long id, JugadorDTO jugadorDTO);

	JugadorDTO patch(Long id, Map<String, Object> updates);

	void deleteById(Long id);

	List<JugadorDTO> findByEquipoId(Long equipoId);

	List<JugadorDTO> findByEstado(Estado estado);

	List<JugadorDTO> findByPosicion(String posicion);

	Optional<JugadorDTO> findByUsuarioId(Long usuarioId);

	List<JugadorDTO> findByEdadBetween(int minEdad, int maxEdad);
}