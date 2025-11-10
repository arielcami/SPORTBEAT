package pe.com.ligasdeportivas.mapper;

import org.springframework.stereotype.Component;

import pe.com.ligasdeportivas.dto.ResultadoDTO;
import pe.com.ligasdeportivas.entity.ResultadoEntity;

@Component
public class ResultadoMapper {

    public ResultadoDTO toDTO(ResultadoEntity entity) {
        if (entity == null) {
            return null;
        }

        ResultadoDTO dto = new ResultadoDTO();
        dto.setId(entity.getId());
        dto.setGolesLocal(entity.getGolesLocal());
        dto.setGolesVisitante(entity.getGolesVisitante());
        dto.setEstado(entity.getEstado());

        if (entity.getPartido() != null) {
            dto.setPartidoId(entity.getPartido().getId());
            String equipoLocal = entity.getPartido().getEquipoLocal() != null
                    ? entity.getPartido().getEquipoLocal().getNombre() : "Desconocido";
            String equipoVisitante = entity.getPartido().getEquipoVisitante() != null
                    ? entity.getPartido().getEquipoVisitante().getNombre() : "Desconocido";
            dto.setPartidoInfo(equipoLocal + " vs " + equipoVisitante);
        }

        return dto;
    }

    public ResultadoEntity toEntity(ResultadoDTO dto) {
        if (dto == null) {
            return null;
        }

        return ResultadoEntity.builder()
                .id(dto.getId())
                .golesLocal(dto.getGolesLocal())
                .golesVisitante(dto.getGolesVisitante())
                .estado(dto.getEstado())
                .build();
        // Nota: La relación con partido se maneja por separado
    }
}
