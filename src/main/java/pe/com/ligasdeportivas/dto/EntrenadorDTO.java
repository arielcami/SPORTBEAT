package pe.com.ligasdeportivas.dto;

import lombok.Data;
import pe.com.ligasdeportivas.entity.Estado;

@Data
public class EntrenadorDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private Estado estado;
    private Long usuarioId;
    private String usuarioUsername;
    private int cantidadEquipos;

    public EntrenadorDTO() {
    }

    public EntrenadorDTO(Long id, String nombre, String apellido, Estado estado) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.estado = estado;
    }
}
