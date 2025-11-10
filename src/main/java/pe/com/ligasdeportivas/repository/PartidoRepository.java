package pe.com.ligasdeportivas.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.ligasdeportivas.entity.Estado;
import pe.com.ligasdeportivas.entity.PartidoEntity;

@Repository
public interface PartidoRepository extends JpaRepository<PartidoEntity, Long> {

    List<PartidoEntity> findByLigaId(Long ligaId);

    List<PartidoEntity> findByEstado(Estado estado);

    List<PartidoEntity> findByFecha(LocalDate fecha);

    List<PartidoEntity> findByFechaBetween(LocalDate startDate, LocalDate endDate);
    
    List<PartidoEntity> findByEquipoLocalIdOrEquipoVisitanteId(Long equipoLocalId, Long equipoVisitanteId);

    List<PartidoEntity> findByLigaIdAndEstado(Long ligaId, Estado estado);

    List<PartidoEntity> findByEquipoLocalIdAndEstado(Long equipoId, Estado estado);

    List<PartidoEntity> findByEquipoVisitanteIdAndEstado(Long equipoId, Estado estado);

    Optional<PartidoEntity> findByResultadoId(Long resultadoId);

    List<PartidoEntity> findByFechaGreaterThanEqualAndEstado(LocalDate fecha, Estado estado);
}
