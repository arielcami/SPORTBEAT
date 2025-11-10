package pe.com.ligasdeportivas.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import pe.com.ligasdeportivas.dto.PartidoDTO;
import pe.com.ligasdeportivas.entity.Estado;

public interface PartidoService {

    List<PartidoDTO> findAll();

    Optional<PartidoDTO> findById(Long id);

    PartidoDTO save(PartidoDTO partidoDTO);

    PartidoDTO update(Long id, PartidoDTO partidoDTO);

    PartidoDTO patch(Long id, Map<String, Object> updates);

    void deleteById(Long id);

    List<PartidoDTO> findByLigaId(Long ligaId);

    List<PartidoDTO> findByEstado(Estado estado);

    List<PartidoDTO> findByFecha(LocalDate fecha);

    List<PartidoDTO> findByFechaBetween(LocalDate startDate, LocalDate endDate);

    List<PartidoDTO> findByEquipoId(Long equipoId);

    List<PartidoDTO> findProximosPartidos();

    List<PartidoDTO> findPartidosJugados();

    List<PartidoDTO> findPartidosEsteMes();
}
