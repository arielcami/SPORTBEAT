package pe.com.ligasdeportivas.dto;

import lombok.Data;

@Data
public class UsuarioDTO {

    private Long id;
    private String username;
    private String email;
    private String rol;
    private String estado;

    public UsuarioDTO() {
    }

    public UsuarioDTO(Long id, String username, String email, String rol, String estado) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.rol = rol;
        this.estado = estado;
    }
}
