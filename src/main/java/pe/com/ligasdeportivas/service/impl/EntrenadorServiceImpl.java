package pe.com.ligasdeportivas.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.com.ligasdeportivas.dto.EntrenadorDTO;
import pe.com.ligasdeportivas.entity.EntrenadorEntity;
import pe.com.ligasdeportivas.entity.Estado;
import pe.com.ligasdeportivas.mapper.EntrenadorMapper;
import pe.com.ligasdeportivas.repository.EntrenadorRepository;
import pe.com.ligasdeportivas.service.EntrenadorService;
import pe.com.ligasdeportivas.util.PatchUtil;

@Service
public class EntrenadorServiceImpl implements EntrenadorService {

	@Autowired
	private EntrenadorRepository entrenadorRepository;

	@Autowired
	private EntrenadorMapper entrenadorMapper;

	@Override
	public List<EntrenadorDTO> findAll() {
		return entrenadorRepository.findAll().stream().map(entrenadorMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public Optional<EntrenadorDTO> findById(Long id) {
		return entrenadorRepository.findById(id).map(entrenadorMapper::toDTO);
	}

	@Override
	public EntrenadorDTO save(EntrenadorDTO entrenadorDTO) {
		// Validar campos requeridos
		if (entrenadorDTO.getUsuarioId() == null) {
			throw new RuntimeException("El campo usuarioId es requerido");
		}

		EntrenadorEntity entity = entrenadorMapper.toEntity(entrenadorDTO);
		EntrenadorEntity savedEntity = entrenadorRepository.save(entity);
		return entrenadorMapper.toDTO(savedEntity);
	}

	@Override
	public EntrenadorDTO update(Long id, EntrenadorDTO entrenadorDTO) {
		if (entrenadorRepository.existsById(id)) {
			EntrenadorEntity entity = entrenadorMapper.toEntity(entrenadorDTO);
			entity.setId(id);
			EntrenadorEntity updatedEntity = entrenadorRepository.save(entity);
			return entrenadorMapper.toDTO(updatedEntity);
		}
		throw new RuntimeException("Entrenador no encontrado con id: " + id);
	}

	@Override
	public EntrenadorDTO patch(Long id, Map<String, Object> updates) {
		Optional<EntrenadorEntity> entrenadorOptional = entrenadorRepository.findById(id);
		if (entrenadorOptional.isPresent()) {
			EntrenadorEntity entrenador = entrenadorOptional.get();
			PatchUtil.applyPatch(entrenador, updates);
			EntrenadorEntity patchedEntity = entrenadorRepository.save(entrenador);
			return entrenadorMapper.toDTO(patchedEntity);
		}
		throw new RuntimeException("Entrenador no encontrado con id: " + id);
	}

	@Override
	public void deleteById(Long id) {
		entrenadorRepository.deleteById(id);
	}

	@Override
	public Optional<EntrenadorDTO> findByUsuarioId(Long usuarioId) {
		return entrenadorRepository.findByUsuarioId(usuarioId).map(entrenadorMapper::toDTO);
	}

	@Override
	public List<EntrenadorDTO> findByEstado(Estado estado) {
		return entrenadorRepository.findByEstado(estado).stream().map(entrenadorMapper::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public List<EntrenadorDTO> buscarPorNombre(String nombre) {
		return entrenadorRepository.findByNombreContainingIgnoreCase(nombre).stream().map(entrenadorMapper::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public List<EntrenadorDTO> buscarPorApellido(String apellido) {
		return entrenadorRepository.findByApellidoContainingIgnoreCase(apellido).stream().map(entrenadorMapper::toDTO)
				.collect(Collectors.toList());
	}
}