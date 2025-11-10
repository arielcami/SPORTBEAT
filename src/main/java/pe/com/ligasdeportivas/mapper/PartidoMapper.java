package pe.com.ligasdeportivas.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pe.com.ligasdeportivas.dto.PartidoDTO;
import pe.com.ligasdeportivas.entity.PartidoEntity;

@Component
public class PartidoMapper {

    @Autowired
    private ResultadoMapper resultadoMapper;

    public PartidoDTO toDTO(PartidoEntity entity) {
        if (entity == null) {
            return null;
        }

        PartidoDTO dto = new PartidoDTO();
        dto.setId(entity.getId());
        dto.setFecha(entity.getFecha());
        dto.setHora(entity.getHora());
        dto.setLugar(entity.getLugar());
        dto.setEstado(entity.getEstado());

        // Información de la liga
        if (entity.getLiga() != null) {
            dto.setLigaId(entity.getLiga().getId());
            dto.setLigaNombre(entity.getLiga().getNombre());
        }

        // Información del equipo local
        if (entity.getEquipoLocal() != null) {
            dto.setEquipoLocalId(entity.getEquipoLocal().getId());
            dto.setEquipoLocalNombre(entity.getEquipoLocal().getNombre());
        }

        // Información del equipo visitante
        if (entity.getEquipoVisitante() != null) {
            dto.setEquipoVisitanteId(entity.getEquipoVisitante().getId());
            dto.setEquipoVisitanteNombre(entity.getEquipoVisitante().getNombre());
        }

        // Resultado (si existe)
        if (entity.getResultado() != null) {
            dto.setResultado(resultadoMapper.toDTO(entity.getResultado()));
        }

        return dto;
    }

    public PartidoEntity toEntity(PartidoDTO dto) {
        if (dto == null) {
            return null;
        }

        return PartidoEntity.builder()
                .id(dto.getId())
                .fecha(dto.getFecha())
                .hora(dto.getHora())
                .lugar(dto.getLugar())
                .estado(dto.getEstado())
                .build();
        // Nota: Las relaciones se manejan por separado
    }
}
