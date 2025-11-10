package pe.com.ligasdeportivas.restcontroller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.ligasdeportivas.service.EquipoService;
import pe.com.ligasdeportivas.service.LigaService;
import pe.com.ligasdeportivas.service.PartidoService;
import pe.com.ligasdeportivas.service.UsuarioService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardRestController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private EquipoService equipoService;

	@Autowired
	private LigaService ligaService;

	@Autowired
	private PartidoService partidoService;

	@GetMapping("/estadisticas")
	public ResponseEntity<Map<String, Object>> getEstadisticas() {
		try {
			Map<String, Object> estadisticas = new HashMap<>();

			// Obtener datos reales de los servicios
			long totalUsuarios = usuarioService.listarUsuarios().size();
			long equiposActivos = equipoService.findAll().stream()
					.filter(equipo -> "ACTIVO".equals(equipo.getEstado().name())).count();
			long totalLigas = ligaService.findAll().size();

			// Partidos este mes
			LocalDate hoy = LocalDate.now();
			LocalDate inicioMes = hoy.withDayOfMonth(1);
			long partidosEsteMes = partidoService.findAll().stream().filter(partido -> partido.getFecha() != null
					&& !partido.getFecha().isBefore(inicioMes) && !partido.getFecha().isAfter(hoy)).count();

			estadisticas.put("totalUsuarios", totalUsuarios);
			estadisticas.put("equiposActivos", equiposActivos);
			estadisticas.put("totalLigas", totalLigas);
			estadisticas.put("partidosEsteMes", partidosEsteMes);

			return ResponseEntity.ok(estadisticas);

		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "Error al cargar estadísticas");
			return ResponseEntity.internalServerError().body(error);
		}
	}

	@GetMapping("/actividad")
	public ResponseEntity<List<Map<String, String>>> getActividadReciente() {
		try {
			// Por ahora datos de ejemplo - luego integraremos con servicios reales
			List<Map<String, String>> actividad = Arrays.asList(
					crearActividad("jugador", "Nuevo jugador registrado: Ariel Camilo", "hace 5 minutos"),
					crearActividad("partido", "Partido finalizado: Águilas 3-1 Tigres", "hace 15 minutos"),
					crearActividad("programacion", "Nuevo partido programado para el sábado", "hace 30 minutos"),
					crearActividad("liga", "Liga de Fútbol Miraflores activada", "hace 1 hora"));

			return ResponseEntity.ok(actividad);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	private Map<String, String> crearActividad(String tipo, String descripcion, String tiempo) {
		Map<String, String> actividad = new HashMap<>();
		actividad.put("tipo", tipo);
		actividad.put("descripcion", descripcion);
		actividad.put("tiempo", tiempo);
		return actividad;
	}
}