package pe.com.ligasdeportivas.dto;

import lombok.Data;
import pe.com.ligasdeportivas.entity.Estado;

@Data
public class ResultadoDTO {

    private Long id;
    private int golesLocal;
    private int golesVisitante;
    private Estado estado;
    private Long partidoId;
    private String partidoInfo; // "EquipoA vs EquipoB - Fecha"

    // Constructor para facilitar creación
    public ResultadoDTO() {
    }

    public ResultadoDTO(Long id, int golesLocal, int golesVisitante, Estado estado, Long partidoId, String partidoInfo) {
        this.id = id;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
        this.estado = estado;
        this.partidoId = partidoId;
        this.partidoInfo = partidoInfo;
    }
}
