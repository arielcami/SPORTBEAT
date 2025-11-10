package pe.com.ligasdeportivas.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.com.ligasdeportivas.dto.LigaDTO;
import pe.com.ligasdeportivas.entity.Estado;
import pe.com.ligasdeportivas.entity.LigaEntity;
import pe.com.ligasdeportivas.mapper.LigaMapper;
import pe.com.ligasdeportivas.repository.LigaRepository;
import pe.com.ligasdeportivas.service.LigaService;
import pe.com.ligasdeportivas.util.PatchUtil;

@Service
public class LigaServiceImpl implements LigaService {

	@Autowired
	private LigaRepository ligaRepository;

	@Autowired
	private LigaMapper ligaMapper;

	@Override
	public List<LigaDTO> findAll() {
		return ligaRepository.findAll().stream().map(ligaMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public Optional<LigaDTO> findById(Long id) {
		return ligaRepository.findById(id).map(ligaMapper::toDTO);
	}

	@Override
	public LigaDTO save(LigaDTO ligaDTO) {
		LigaEntity entity = ligaMapper.toEntity(ligaDTO);
		LigaEntity savedEntity = ligaRepository.save(entity);
		return ligaMapper.toDTO(savedEntity);
	}

	@Override
	public LigaDTO update(Long id, LigaDTO ligaDTO) {
		if (ligaRepository.existsById(id)) {
			LigaEntity entity = ligaMapper.toEntity(ligaDTO);
			entity.setId(id);
			LigaEntity updatedEntity = ligaRepository.save(entity);
			return ligaMapper.toDTO(updatedEntity);
		}
		throw new RuntimeException("Liga no encontrada con id: " + id);
	}

	@Override
	public LigaDTO patch(Long id, Map<String, Object> updates) {
		Optional<LigaEntity> ligaOptional = ligaRepository.findById(id);
		if (ligaOptional.isPresent()) {
			LigaEntity liga = ligaOptional.get();
			PatchUtil.applyPatch(liga, updates);
			LigaEntity patchedEntity = ligaRepository.save(liga);
			return ligaMapper.toDTO(patchedEntity);
		}
		throw new RuntimeException("Liga no encontrada con id: " + id);
	}

	@Override
	public void deleteById(Long id) {
		ligaRepository.deleteById(id);
	}

	@Override
	public List<LigaDTO> findByDistrito(String distrito) {
		return ligaRepository.findByDistrito(distrito).stream().map(ligaMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public List<LigaDTO> findByEstado(Estado estado) {
		return ligaRepository.findByEstado(estado).stream().map(ligaMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public boolean existsByNombre(String nombre) {
		return ligaRepository.existsByNombre(nombre);
	}
}