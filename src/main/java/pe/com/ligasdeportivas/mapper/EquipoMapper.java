package pe.com.ligasdeportivas.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pe.com.ligasdeportivas.dto.EquipoDTO;
import pe.com.ligasdeportivas.entity.EntrenadorEntity;
import pe.com.ligasdeportivas.entity.EquipoEntity;
import pe.com.ligasdeportivas.entity.LigaEntity;
import pe.com.ligasdeportivas.repository.EntrenadorRepository;
import pe.com.ligasdeportivas.repository.LigaRepository;

@Component
public class EquipoMapper {

	@Autowired
	private LigaRepository ligaRepository;

	@Autowired
	private EntrenadorRepository entrenadorRepository;

	public EquipoDTO toDTO(EquipoEntity entity) {
		if (entity == null) {
			return null;
		}

		EquipoDTO dto = new EquipoDTO();
		dto.setId(entity.getId());
		dto.setNombre(entity.getNombre());
		dto.setEstado(entity.getEstado());

		if (entity.getLiga() != null) {
			dto.setLigaId(entity.getLiga().getId());
			dto.setLigaNombre(entity.getLiga().getNombre());
		}

		if (entity.getEntrenador() != null) {
			dto.setEntrenadorId(entity.getEntrenador().getId());
			dto.setEntrenadorNombre(entity.getEntrenador().getNombre() + " " + entity.getEntrenador().getApellido());
		}

		if (entity.getJugadores() != null) {
			dto.setCantidadJugadores(entity.getJugadores().size());
		}

		return dto;
	}

	public EquipoEntity toEntity(EquipoDTO dto) {
		if (dto == null) {
			return null;
		}

		EquipoEntity.EquipoEntityBuilder builder = EquipoEntity.builder().id(dto.getId()).nombre(dto.getNombre())
				.estado(dto.getEstado());

		// Cargar Liga si se proporciona ligaId
		if (dto.getLigaId() != null) {
			LigaEntity liga = ligaRepository.findById(dto.getLigaId())
					.orElseThrow(() -> new RuntimeException("Liga no encontrada con id: " + dto.getLigaId()));
			builder.liga(liga);
		}

		// Cargar Entrenador si se proporciona entrenadorId
		if (dto.getEntrenadorId() != null) {
			EntrenadorEntity entrenador = entrenadorRepository.findById(dto.getEntrenadorId()).orElseThrow(
					() -> new RuntimeException("Entrenador no encontrado con id: " + dto.getEntrenadorId()));
			builder.entrenador(entrenador);
		}

		return builder.build();
	}
}
