package pe.com.ligasdeportivas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.com.ligasdeportivas.dto.ResultadoDTO;
import pe.com.ligasdeportivas.entity.Estado;
import pe.com.ligasdeportivas.entity.ResultadoEntity;
import pe.com.ligasdeportivas.mapper.ResultadoMapper;
import pe.com.ligasdeportivas.repository.ResultadoRepository;
import pe.com.ligasdeportivas.service.ResultadoService;
import pe.com.ligasdeportivas.util.PatchUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResultadoServiceImpl implements ResultadoService {

    @Autowired
    private ResultadoRepository resultadoRepository;

    @Autowired
    private ResultadoMapper resultadoMapper;

    @Override
    public List<ResultadoDTO> findAll() {
        return resultadoRepository.findAll()
                .stream()
                .map(resultadoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ResultadoDTO> findById(Long id) {
        return resultadoRepository.findById(id)
                .map(resultadoMapper::toDTO);
    }

    @Override
    public ResultadoDTO save(ResultadoDTO resultadoDTO) {
        ResultadoEntity entity = resultadoMapper.toEntity(resultadoDTO);
        ResultadoEntity savedEntity = resultadoRepository.save(entity);
        return resultadoMapper.toDTO(savedEntity);
    }

    @Override
    public ResultadoDTO update(Long id, ResultadoDTO resultadoDTO) {
        if (resultadoRepository.existsById(id)) {
            ResultadoEntity entity = resultadoMapper.toEntity(resultadoDTO);
            entity.setId(id);
            ResultadoEntity updatedEntity = resultadoRepository.save(entity);
            return resultadoMapper.toDTO(updatedEntity);
        }
        throw new RuntimeException("Resultado no encontrado con id: " + id);
    }

    @Override
    public ResultadoDTO patch(Long id, Map<String, Object> updates) {
        Optional<ResultadoEntity> resultadoOptional = resultadoRepository.findById(id);
        if (resultadoOptional.isPresent()) {
            ResultadoEntity resultado = resultadoOptional.get();
            PatchUtil.applyPatch(resultado, updates);
            ResultadoEntity patchedEntity = resultadoRepository.save(resultado);
            return resultadoMapper.toDTO(patchedEntity);
        }
        throw new RuntimeException("Resultado no encontrado con id: " + id);
    }

    @Override
    public void deleteById(Long id) {
        resultadoRepository.deleteById(id);
    }

    @Override
    public Optional<ResultadoDTO> findByPartidoId(Long partidoId) {
        return resultadoRepository.findByPartidoId(partidoId)
                .map(resultadoMapper::toDTO);
    }

    @Override
    public List<ResultadoDTO> findByEstado(Estado estado) {
        return resultadoRepository.findByEstado(estado)
                .stream()
                .map(resultadoMapper::toDTO)
                .collect(Collectors.toList());
    }
}
