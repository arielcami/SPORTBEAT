package pe.com.ligasdeportivas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.ligasdeportivas.entity.Estado;
import pe.com.ligasdeportivas.entity.JugadorEntity;

@Repository
public interface JugadorRepository extends JpaRepository<JugadorEntity, Long> {

    List<JugadorEntity> findByEquipoId(Long equipoId);

    List<JugadorEntity> findByEstado(Estado estado);

    List<JugadorEntity> findByPosicion(String posicion);

    List<JugadorEntity> findByEquipoIdAndEstado(Long equipoId, Estado estado);

    Optional<JugadorEntity> findByUsuarioId(Long usuarioId);

    boolean existsByUsuarioId(Long usuarioId);

    List<JugadorEntity> findByEdadGreaterThanEqual(int edad);

    List<JugadorEntity> findByEdadLessThanEqual(int edad);

}
