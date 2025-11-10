package pe.com.ligasdeportivas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.ligasdeportivas.entity.Estado;
import pe.com.ligasdeportivas.entity.ResultadoEntity;

@Repository
public interface ResultadoRepository extends JpaRepository<ResultadoEntity, Long> {

    Optional<ResultadoEntity> findByPartidoId(Long partidoId);

    List<ResultadoEntity> findByEstado(Estado estado);

    List<ResultadoEntity> findByGolesLocalGreaterThan(int goles);

    List<ResultadoEntity> findByGolesVisitanteGreaterThan(int goles);

    List<ResultadoEntity> findByGolesLocalAndGolesVisitante(int golesLocal, int golesVisitante);
}
