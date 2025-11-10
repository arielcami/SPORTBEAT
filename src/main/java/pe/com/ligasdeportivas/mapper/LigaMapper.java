package pe.com.ligasdeportivas.mapper;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pe.com.ligasdeportivas.dto.LigaDTO;
import pe.com.ligasdeportivas.entity.LigaEntity;

@Component
public class LigaMapper {

    @Autowired
    private EquipoMapper equipoMapper;

    public LigaDTO toDTO(LigaEntity entity) {
        if (entity == null) {
            return null;
        }

        LigaDTO dto = new LigaDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setDistrito(entity.getDistrito());
        dto.setEstado(entity.getEstado());

        if (entity.getEquipos() != null) {
            dto.setCantidadEquipos(entity.getEquipos().size());
            dto.setEquipos(entity.getEquipos().stream()
                    .map(equipoMapper::toDTO)
                    .collect(Collectors.toList()));
        }

        if (entity.getPartidos() != null) {
            dto.setCantidadPartidos(entity.getPartidos().size());
        }

        return dto;
    }

    public LigaEntity toEntity(LigaDTO dto) {
        if (dto == null) {
            return null;
        }

        return LigaEntity.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .distrito(dto.getDistrito())
                .estado(dto.getEstado())
                .build();
    }
}
