package pe.com.ligasdeportivas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.ligasdeportivas.entity.EquipoEntity;
import pe.com.ligasdeportivas.entity.Estado;

@Repository
public interface EquipoRepository extends JpaRepository<EquipoEntity, Long> {

    Optional<EquipoEntity> findByNombre(String nombre);

    List<EquipoEntity> findByLigaId(Long ligaId);

    List<EquipoEntity> findByEstado(Estado estado);

    List<EquipoEntity> findByLigaIdAndEstado(Long ligaId, Estado estado);

    boolean existsByNombre(String nombre);

    List<EquipoEntity> findByEntrenadorId(Long entrenadorId);
}
