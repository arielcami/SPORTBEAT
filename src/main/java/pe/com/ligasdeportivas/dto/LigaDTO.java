package pe.com.ligasdeportivas.dto;

import java.util.List;

import lombok.Data;
import pe.com.ligasdeportivas.entity.Estado;

@Data
public class LigaDTO {

    private Long id;
    private String nombre;
    private String distrito;
    private Estado estado;
    private List<EquipoDTO> equipos;
    private int cantidadEquipos;
    private int cantidadPartidos;

    public LigaDTO() {
    }

    public LigaDTO(Long id, String nombre, String distrito, Estado estado) {
        this.id = id;
        this.nombre = nombre;
        this.distrito = distrito;
        this.estado = estado;
    }
}
