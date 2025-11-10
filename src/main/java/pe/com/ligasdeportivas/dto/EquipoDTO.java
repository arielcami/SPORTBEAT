package pe.com.ligasdeportivas.dto;

import lombok.Data;
import pe.com.ligasdeportivas.entity.Estado;

@Data
public class EquipoDTO {

    private Long id;
    private String nombre;
    private Estado estado;
    private Long ligaId;
    private String ligaNombre;
    private Long entrenadorId;
    private String entrenadorNombre;
    private int cantidadJugadores;

    public EquipoDTO() {
    }

    public EquipoDTO(Long id, String nombre, Estado estado, Long ligaId, String ligaNombre) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
        this.ligaId = ligaId;
        this.ligaNombre = ligaNombre;
    }
}
