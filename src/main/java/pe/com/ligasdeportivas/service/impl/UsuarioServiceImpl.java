package pe.com.ligasdeportivas.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pe.com.ligasdeportivas.entity.Estado;
import pe.com.ligasdeportivas.entity.UsuarioEntity;
import pe.com.ligasdeportivas.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements pe.com.ligasdeportivas.service.UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inyectar el encriptador

    @Override
    public List<UsuarioEntity> listarUsuarios() {
        List<UsuarioEntity> usuarios = usuarioRepository.findAll();

        // Ocultar la contraseña
        for (UsuarioEntity usuario : usuarios) {
            usuario.setPassword("*");
        }
        return usuarios;
    }

    @Override
    public boolean crearUsuario(UsuarioEntity usuario) {
        try {
            // ENCRIPTAR LA CONTRASEÑA ANTES DE GUARDAR
            String passwordEncriptada = passwordEncoder.encode(usuario.getPassword());
            usuario.setPassword(passwordEncriptada);

            usuarioRepository.save(usuario);
            return true;
        } catch (Exception e) {
            e.printStackTrace(); // Para debugging en Postman
            return false;
        }
    }

    @Override
    public boolean actualizarUsuarioParcial(Long id, Map<String, Object> camposActualizados) {
        try {
            Optional<UsuarioEntity> usuarioExistenteOpt = usuarioRepository.findById(id);

            if (usuarioExistenteOpt.isEmpty()) {
                return false;
            }

            UsuarioEntity usuarioExistente = usuarioExistenteOpt.get();

            for (Map.Entry<String, Object> entry : camposActualizados.entrySet()) {
                String campo = entry.getKey();
                Object valor = entry.getValue();

                switch (campo) {
                    case "username" -> {
                        String nuevoUsername = (String) valor;
                        if (!usuarioExistente.getUsername().equals(nuevoUsername)
                                && usuarioRepository.existsByUsernameAndIdNot(nuevoUsername, id)) {
                            return false;
                        }
                        usuarioExistente.setUsername(nuevoUsername);
                    }
                    case "password" -> {
                        // ENCRIPTAR LA NUEVA CONTRASEÑA SI SE ACTUALIZA
                        String nuevaPasswordEncriptada = passwordEncoder.encode((String) valor);
                        usuarioExistente.setPassword(nuevaPasswordEncriptada);
                    }
                    case "email" -> {
                        String nuevoEmail = (String) valor;
                        if (!usuarioExistente.getEmail().equals(nuevoEmail)
                                && usuarioRepository.existsByEmailAndIdNot(nuevoEmail, id)) {
                            return false;
                        }
                        usuarioExistente.setEmail(nuevoEmail);
                    }
                    case "rol" ->
                        usuarioExistente.setRol(((String) valor).toUpperCase());
                    case "estado" ->
                        usuarioExistente.setEstado(((String) valor).toUpperCase());
                    default -> {
                        // Ignorar campos no reconocidos
                    }
                }
            }

            usuarioRepository.save(usuarioExistente);
            return true;

        } catch (Exception e) {
            e.printStackTrace(); // Para debugging en Postman
            return false;
        }
    }

    @Override
    public Optional<UsuarioEntity> buscarPorUsernameOEmail(String usernameOEmail) {
        // Buscar primero por username
        Optional<UsuarioEntity> usuarioPorUsername = usuarioRepository.findByUsername(usernameOEmail);
        if (usuarioPorUsername.isPresent()) {
            return usuarioPorUsername;
        }

        // Si no encuentra por username, buscar por email
        return usuarioRepository.findByEmail(usernameOEmail);
    }

    @Override
    public Optional<UsuarioEntity> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public long count() {
        return usuarioRepository.count();
    }

    @Override
    public List<UsuarioEntity> findByEstado(Estado estado) {
        return usuarioRepository.findByEstado(estado);
    }
}
