package pe.com.ligasdeportivas.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.com.ligasdeportivas.dto.JugadorDTO;
import pe.com.ligasdeportivas.entity.EquipoEntity;
import pe.com.ligasdeportivas.entity.JugadorEntity;
import pe.com.ligasdeportivas.entity.UsuarioEntity;
import pe.com.ligasdeportivas.repository.EquipoRepository;
import pe.com.ligasdeportivas.repository.UsuarioRepository;

@Component
public class JugadorMapper {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private EquipoRepository equipoRepository;

	public JugadorDTO toDTO(JugadorEntity entity) {
		if (entity == null) {
			return null;
		}

		JugadorDTO dto = new JugadorDTO();
		dto.setId(entity.getId());
		dto.setNombre(entity.getNombre());
		dto.setApellido(entity.getApellido());
		dto.setEdad(entity.getEdad());
		dto.setPosicion(entity.getPosicion());
		dto.setEstado(entity.getEstado());

		if (entity.getUsuario() != null) {
			dto.setUsuarioId(entity.getUsuario().getId());
			dto.setUsuarioUsername(entity.getUsuario().getUsername());
		}

		if (entity.getEquipo() != null) {
			dto.setEquipoId(entity.getEquipo().getId());
			dto.setEquipoNombre(entity.getEquipo().getNombre());
		}

		return dto;
	}

	public JugadorEntity toEntity(JugadorDTO dto) {
		if (dto == null) {
			return null;
		}

		JugadorEntity.JugadorEntityBuilder builder = JugadorEntity.builder().id(dto.getId()).nombre(dto.getNombre())
				.apellido(dto.getApellido()).edad(dto.getEdad()).posicion(dto.getPosicion()).estado(dto.getEstado());

		// Cargar la entidad Usuario si se proporciona usuarioId
		if (dto.getUsuarioId() != null) {
			UsuarioEntity usuario = usuarioRepository.findById(dto.getUsuarioId())
					.orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + dto.getUsuarioId()));
			builder.usuario(usuario);
		}

		// Cargar la entidad Equipo si se proporciona equipoId
		if (dto.getEquipoId() != null) {
			EquipoEntity equipo = equipoRepository.findById(dto.getEquipoId())
					.orElseThrow(() -> new RuntimeException("Equipo no encontrado con id: " + dto.getEquipoId()));
			builder.equipo(equipo);
		}

		return builder.build();
	}
}