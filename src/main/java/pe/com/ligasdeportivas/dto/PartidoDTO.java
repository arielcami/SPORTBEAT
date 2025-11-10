package pe.com.ligasdeportivas.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;
import pe.com.ligasdeportivas.entity.Estado;

@Data
public class PartidoDTO {

    private Long id;
    private LocalDate fecha;
    private LocalTime hora;
    private String lugar;
    private Estado estado;
    private Long ligaId;
    private String ligaNombre;
    private Long equipoLocalId;
    private String equipoLocalNombre;
    private Long equipoVisitanteId;
    private String equipoVisitanteNombre;
    private ResultadoDTO resultado;

    public PartidoDTO() {
    }

    public PartidoDTO(Long id, LocalDate fecha, LocalTime hora, String lugar, Estado estado,
            Long ligaId, String ligaNombre, Long equipoLocalId, String equipoLocalNombre,
            Long equipoVisitanteId, String equipoVisitanteNombre) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.lugar = lugar;
        this.estado = estado;
        this.ligaId = ligaId;
        this.ligaNombre = ligaNombre;
        this.equipoLocalId = equipoLocalId;
        this.equipoLocalNombre = equipoLocalNombre;
        this.equipoVisitanteId = equipoVisitanteId;
        this.equipoVisitanteNombre = equipoVisitanteNombre;
    }
}
