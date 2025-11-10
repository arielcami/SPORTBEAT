package pe.com.ligasdeportivas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.ligasdeportivas.entity.EntrenadorEntity;
import pe.com.ligasdeportivas.entity.Estado;

@Repository
public interface EntrenadorRepository extends JpaRepository<EntrenadorEntity, Long> {

    Optional<EntrenadorEntity> findByUsuarioId(Long usuarioId);

    List<EntrenadorEntity> findByEstado(Estado estado);

    List<EntrenadorEntity> findByNombreContainingIgnoreCase(String nombre);

    List<EntrenadorEntity> findByApellidoContainingIgnoreCase(String apellido);

    boolean existsByUsuarioId(Long usuarioId);

    Optional<EntrenadorEntity> findByNombreAndApellido(String nombre, String apellido);
    
}
