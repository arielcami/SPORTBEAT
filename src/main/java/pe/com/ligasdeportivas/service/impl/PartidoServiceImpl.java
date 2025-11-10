package pe.com.ligasdeportivas.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.ligasdeportivas.dto.PartidoDTO;
import pe.com.ligasdeportivas.entity.Estado;
import pe.com.ligasdeportivas.entity.PartidoEntity;
import pe.com.ligasdeportivas.mapper.PartidoMapper;
import pe.com.ligasdeportivas.repository.PartidoRepository;
import pe.com.ligasdeportivas.service.PartidoService;
import pe.com.ligasdeportivas.util.PatchUtil;

@Service
public class PartidoServiceImpl implements PartidoService {

    @Autowired
    private PartidoRepository partidoRepository;

    @Autowired
    private PartidoMapper partidoMapper;

    @Override
    public List<PartidoDTO> findAll() {
        return partidoRepository.findAll()
                .stream()
                .map(partidoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PartidoDTO> findById(Long id) {
        return partidoRepository.findById(id)
                .map(partidoMapper::toDTO);
    }

    @Override
    public PartidoDTO save(PartidoDTO partidoDTO) {
        if (partidoDTO.getEquipoLocalId() != null
                && partidoDTO.getEquipoVisitanteId() != null
                && partidoDTO.getEquipoLocalId().equals(partidoDTO.getEquipoVisitanteId())) {
            throw new IllegalArgumentException("Un equipo no puede jugar contra sí mismo");
        }

        PartidoEntity entity = partidoMapper.toEntity(partidoDTO);
        PartidoEntity savedEntity = partidoRepository.save(entity);
        return partidoMapper.toDTO(savedEntity);
    }

    @Override
    public PartidoDTO update(Long id, PartidoDTO partidoDTO) {
        if (partidoRepository.existsById(id)) {
            if (partidoDTO.getEquipoLocalId() != null
                    && partidoDTO.getEquipoVisitanteId() != null
                    && partidoDTO.getEquipoLocalId().equals(partidoDTO.getEquipoVisitanteId())) {
                throw new IllegalArgumentException("Un equipo no puede jugar contra sí mismo");
            }

            PartidoEntity entity = partidoMapper.toEntity(partidoDTO);
            entity.setId(id);
            PartidoEntity updatedEntity = partidoRepository.save(entity);
            return partidoMapper.toDTO(updatedEntity);
        }
        throw new RuntimeException("Partido no encontrado con id: " + id);
    }

    @Override
    public PartidoDTO patch(Long id, Map<String, Object> updates) {
        Optional<PartidoEntity> partidoOptional = partidoRepository.findById(id);
        if (partidoOptional.isPresent()) {
            PartidoEntity partido = partidoOptional.get();
            PatchUtil.applyPatch(partido, updates);
            PartidoEntity patchedEntity = partidoRepository.save(partido);
            return partidoMapper.toDTO(patchedEntity);
        }
        throw new RuntimeException("Partido no encontrado con id: " + id);
    }

    @Override
    public void deleteById(Long id) {
        partidoRepository.deleteById(id);
    }

    @Override
    public List<PartidoDTO> findByLigaId(Long ligaId) {
        return partidoRepository.findByLigaId(ligaId)
                .stream()
                .map(partidoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PartidoDTO> findByEstado(Estado estado) {
        return partidoRepository.findByEstado(estado)
                .stream()
                .map(partidoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PartidoDTO> findByFecha(LocalDate fecha) {
        return partidoRepository.findByFecha(fecha)
                .stream()
                .map(partidoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PartidoDTO> findByFechaBetween(LocalDate startDate, LocalDate endDate) {
        return partidoRepository.findByFechaBetween(startDate, endDate)
                .stream()
                .map(partidoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PartidoDTO> findByEquipoId(Long equipoId) {
        return partidoRepository.findByEquipoLocalIdOrEquipoVisitanteId(equipoId, equipoId)
                .stream()
                .map(partidoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PartidoDTO> findProximosPartidos() {
        LocalDate hoy = LocalDate.now();
        return partidoRepository.findByFechaGreaterThanEqualAndEstado(hoy, Estado.PROGRAMADO)
                .stream()
                .map(partidoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PartidoDTO> findPartidosJugados() {
        return partidoRepository.findByEstado(Estado.JUGADO)
                .stream()
                .map(partidoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PartidoDTO> findPartidosEsteMes() { 
        LocalDate hoy = LocalDate.now();
        LocalDate inicioMes = hoy.withDayOfMonth(1);
        LocalDate finMes = hoy.withDayOfMonth(hoy.lengthOfMonth());
        return partidoRepository.findByFechaBetween(inicioMes, finMes)
                .stream()
                .map(partidoMapper::toDTO)
                .collect(Collectors.toList());
    }
}
