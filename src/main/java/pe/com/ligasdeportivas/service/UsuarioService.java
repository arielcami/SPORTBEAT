package pe.com.ligasdeportivas.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import pe.com.ligasdeportivas.entity.Estado;
import pe.com.ligasdeportivas.entity.UsuarioEntity;

public interface UsuarioService {

    // Firmas de los métodos del servicio de Usuario
    public List<UsuarioEntity> listarUsuarios();

    // Método para actualización parcial
    boolean actualizarUsuarioParcial(Long id, Map<String, Object> camposActualizados);

    // Método para buscar usuario por ID (opcional, útil para validaciones)
    Optional<UsuarioEntity> buscarPorId(Long id);

    // Método para crear usuario
    boolean crearUsuario(UsuarioEntity usuario);

    Optional<UsuarioEntity> buscarPorUsernameOEmail(String usernameOEmail);

    long count();

    List<UsuarioEntity> findByEstado(Estado estado);

}
