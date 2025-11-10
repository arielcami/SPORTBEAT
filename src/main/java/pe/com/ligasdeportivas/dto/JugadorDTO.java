package pe.com.ligasdeportivas.dto;

import lombok.Data;
import pe.com.ligasdeportivas.entity.Estado;

@Data
public class JugadorDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private int edad;
    private String posicion;
    private Estado estado;
    private Long usuarioId;
    private String usuarioUsername;
    private Long equipoId;
    private String equipoNombre;

    public JugadorDTO() {
    }

    public JugadorDTO(Long id, String nombre, String apellido, int edad, String posicion, Estado estado) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.posicion = posicion;
        this.estado = estado;
    }
}
