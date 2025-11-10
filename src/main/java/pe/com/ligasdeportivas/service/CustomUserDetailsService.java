package pe.com.ligasdeportivas.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.com.ligasdeportivas.entity.UsuarioEntity;
import pe.com.ligasdeportivas.repository.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findByUsername(username);

		if (usuarioOpt.isPresent()) {
			UsuarioEntity usuario = usuarioOpt.get();
			return User.builder().username(usuario.getUsername()).password(usuario.getPassword())
					.roles(usuario.getRol()) // "ADMIN", "ENTRENADOR", etc.
					.disabled(!usuario.getEstado().equals("ACTIVO")).build();
		}

		// Para desarrollo, si no existe el usuario, creamos uno por defecto
		return User.builder().username(username).password(passwordEncoder.encode("password")) // contraseña por defecto
				.roles("USER").build();
	}
}