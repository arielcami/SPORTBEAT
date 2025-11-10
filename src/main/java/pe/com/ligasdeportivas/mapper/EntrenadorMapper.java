package pe.com.ligasdeportivas.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.com.ligasdeportivas.dto.EntrenadorDTO;
import pe.com.ligasdeportivas.entity.EntrenadorEntity;
import pe.com.ligasdeportivas.entity.UsuarioEntity;
import pe.com.ligasdeportivas.repository.UsuarioRepository;

@Component
public class EntrenadorMapper {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public EntrenadorDTO toDTO(EntrenadorEntity entity) {
		if (entity == null) {
			return null;
		}

		EntrenadorDTO dto = new EntrenadorDTO();
		dto.setId(entity.getId());
		dto.setNombre(entity.getNombre());
		dto.setApellido(entity.getApellido());
		dto.setEstado(entity.getEstado());

		if (entity.getUsuario() != null) {
			dto.setUsuarioId(entity.getUsuario().getId());
			dto.setUsuarioUsername(entity.getUsuario().getUsername());
		}

		if (entity.getEquipos() != null) {
			dto.setCantidadEquipos(entity.getEquipos().size());
		}

		return dto;
	}

	public EntrenadorEntity toEntity(EntrenadorDTO dto) {
		if (dto == null) {
			return null;
		}

		EntrenadorEntity.EntrenadorEntityBuilder builder = EntrenadorEntity.builder().id(dto.getId())
				.nombre(dto.getNombre()).apellido(dto.getApellido()).estado(dto.getEstado());

		// Cargar la entidad Usuario si se proporciona usuarioId
		if (dto.getUsuarioId() != null) {
			UsuarioEntity usuario = usuarioRepository.findById(dto.getUsuarioId())
					.orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + dto.getUsuarioId()));
			builder.usuario(usuario);
		} else {
			throw new RuntimeException("usuarioId es requerido para crear un entrenador");
		}

		return builder.build();
	}
}