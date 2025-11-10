package pe.com.ligasdeportivas.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.com.ligasdeportivas.dto.JugadorDTO;
import pe.com.ligasdeportivas.entity.Estado;
import pe.com.ligasdeportivas.entity.JugadorEntity;
import pe.com.ligasdeportivas.exception.ResourceAlreadyExistsException;
import pe.com.ligasdeportivas.mapper.JugadorMapper;
import pe.com.ligasdeportivas.repository.EntrenadorRepository;
import pe.com.ligasdeportivas.repository.JugadorRepository;
import pe.com.ligasdeportivas.service.JugadorService;
import pe.com.ligasdeportivas.util.PatchUtil;

@Service
public class JugadorServiceImpl implements JugadorService {

    @Autowired
	private JugadorRepository jugadorRepository;

	@Autowired
	private JugadorMapper jugadorMapper;

    JugadorServiceImpl(EntrenadorRepository entrenadorRepository) {
    }

	@Override
	public List<JugadorDTO> findAll() {
		return jugadorRepository.findAll().stream().map(jugadorMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public Optional<JugadorDTO> findById(Long id) {
		return jugadorRepository.findById(id).map(jugadorMapper::toDTO);
	}

	@Override
	public JugadorDTO save(JugadorDTO jugadorDTO) {
	    // Validar si el usuario ya está asignado a otro jugador
	    if (jugadorDTO.getUsuarioId() != null) {
	        Optional<JugadorEntity> existingJugador = jugadorRepository.findByUsuarioId(jugadorDTO.getUsuarioId());
	        if (existingJugador.isPresent()) {
	            throw new ResourceAlreadyExistsException("Ya existe un jugador con el usuario ID: " + jugadorDTO.getUsuarioId());
	        }
	    }
	    
	    JugadorEntity entity = jugadorMapper.toEntity(jugadorDTO);
	    JugadorEntity savedEntity = jugadorRepository.save(entity);
	    return jugadorMapper.toDTO(savedEntity);
	}
	@Override
	public JugadorDTO update(Long id, JugadorDTO jugadorDTO) {
		if (jugadorRepository.existsById(id)) {
			JugadorEntity entity = jugadorMapper.toEntity(jugadorDTO);
			entity.setId(id);
			JugadorEntity updatedEntity = jugadorRepository.save(entity);
			return jugadorMapper.toDTO(updatedEntity);
		}
		throw new RuntimeException("Jugador no encontrado con id: " + id);
	}

	@Override
	public JugadorDTO patch(Long id, Map<String, Object> updates) {
		Optional<JugadorEntity> jugadorOptional = jugadorRepository.findById(id);
		if (jugadorOptional.isPresent()) {
			JugadorEntity jugador = jugadorOptional.get();
			PatchUtil.applyPatch(jugador, updates);
			JugadorEntity patchedEntity = jugadorRepository.save(jugador);
			return jugadorMapper.toDTO(patchedEntity);
		}
		throw new RuntimeException("Jugador no encontrado con id: " + id);
	}

	@Override
	public void deleteById(Long id) {
		jugadorRepository.deleteById(id);
	}

	@Override
	public List<JugadorDTO> findByEquipoId(Long equipoId) {
		return jugadorRepository.findByEquipoId(equipoId).stream().map(jugadorMapper::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public List<JugadorDTO> findByEstado(Estado estado) {
		return jugadorRepository.findByEstado(estado).stream().map(jugadorMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public List<JugadorDTO> findByPosicion(String posicion) {
		return jugadorRepository.findByPosicion(posicion).stream().map(jugadorMapper::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<JugadorDTO> findByUsuarioId(Long usuarioId) {
		return jugadorRepository.findByUsuarioId(usuarioId).map(jugadorMapper::toDTO);
	}

	@Override
	public List<JugadorDTO> findByEdadBetween(int minEdad, int maxEdad) {
		List<JugadorEntity> jugadores = jugadorRepository.findAll();
		return jugadores.stream().filter(j -> j.getEdad() >= minEdad && j.getEdad() <= maxEdad)
				.map(jugadorMapper::toDTO).collect(Collectors.toList());
	}
}