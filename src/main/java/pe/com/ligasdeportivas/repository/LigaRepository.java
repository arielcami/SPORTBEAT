package pe.com.ligasdeportivas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.ligasdeportivas.entity.Estado;
import pe.com.ligasdeportivas.entity.LigaEntity;

@Repository
public interface LigaRepository extends JpaRepository<LigaEntity, Long> {

    Optional<LigaEntity> findByNombre(String nombre);

    List<LigaEntity> findByEstado(Estado estado);

    List<LigaEntity> findByDistrito(String distrito);

    boolean existsByNombre(String nombre);

}
