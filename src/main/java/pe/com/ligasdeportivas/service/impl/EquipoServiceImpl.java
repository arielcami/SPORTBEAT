package pe.com.ligasdeportivas.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.ligasdeportivas.dto.EquipoDTO;
import pe.com.ligasdeportivas.entity.EquipoEntity;
import pe.com.ligasdeportivas.entity.Estado;
import pe.com.ligasdeportivas.mapper.EquipoMapper;
import pe.com.ligasdeportivas.repository.EquipoRepository;
import pe.com.ligasdeportivas.service.EquipoService;
import pe.com.ligasdeportivas.util.PatchUtil;

@Service
public class EquipoServiceImpl implements EquipoService {

	@Autowired
	private EquipoRepository equipoRepository;

	@Autowired
	private EquipoMapper equipoMapper;

	@Override
	public List<EquipoDTO> findAll() {
		return equipoRepository.findAll().stream().map(equipoMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public Optional<EquipoDTO> findById(Long id) {
		return equipoRepository.findById(id).map(equipoMapper::toDTO);

	}

	@Override
	public EquipoDTO save(EquipoDTO equipoDTO) {
		EquipoEntity entity = equipoMapper.toEntity(equipoDTO);
		EquipoEntity savedEntity = equipoRepository.save(entity);
		return equipoMapper.toDTO(savedEntity);

	}

	@Override
	public EquipoDTO update(Long id, EquipoDTO equipoDTO) {
		if (equipoRepository.existsById(id)) {
			EquipoEntity entity = equipoMapper.toEntity(equipoDTO);
			entity.setId(id);
			EquipoEntity updatedEntity = equipoRepository.save(entity);
			return equipoMapper.toDTO(updatedEntity);
		}
		throw new RuntimeException("Equipo no encontrado con id: " + id);
	}

	@Override
	public EquipoDTO patch(Long id, Map<String, Object> updates) {
		Optional<EquipoEntity> equipoOptional = equipoRepository.findById(id);
		if (equipoOptional.isPresent()) {
			EquipoEntity equipo = equipoOptional.get();
			PatchUtil.applyPatch(equipo, updates);
			EquipoEntity patchedEntity = equipoRepository.save(equipo);
			return equipoMapper.toDTO(patchedEntity);
		}
		throw new RuntimeException("Equipo no encontrado con id: " + id);
	}

	@Override
	public void deleteById(Long id) {
		equipoRepository.deleteById(id);
	}

	@Override
	public List<EquipoDTO> findByLigaId(Long ligaId) {
		return equipoRepository.findByLigaId(ligaId).stream().map(equipoMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public List<EquipoDTO> findByEstado(Estado estado) {
		return equipoRepository.findByEstado(estado).stream().map(equipoMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public List<EquipoDTO> findByEntrenadorId(Long entrenadorId) {
		return equipoRepository.findByEntrenadorId(entrenadorId).stream().map(equipoMapper::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public boolean existsByNombre(String nombre) {
		return equipoRepository.existsByNombre(nombre);
	}
}
