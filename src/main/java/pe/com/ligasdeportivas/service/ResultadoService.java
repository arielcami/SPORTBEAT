package pe.com.ligasdeportivas.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import pe.com.ligasdeportivas.dto.ResultadoDTO;
import pe.com.ligasdeportivas.entity.Estado;

public interface ResultadoService {

    List<ResultadoDTO> findAll();

    Optional<ResultadoDTO> findById(Long id);

    ResultadoDTO save(ResultadoDTO resultadoDTO);

    ResultadoDTO update(Long id, ResultadoDTO resultadoDTO);

    ResultadoDTO patch(Long id, Map<String, Object> updates);

    void deleteById(Long id);

    Optional<ResultadoDTO> findByPartidoId(Long partidoId);

    List<ResultadoDTO> findByEstado(Estado estado);
}
