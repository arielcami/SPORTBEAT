package pe.com.ligasdeportivas.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import pe.com.ligasdeportivas.entity.Estado;
import pe.com.ligasdeportivas.service.EquipoService;
import pe.com.ligasdeportivas.service.LigaService;
import pe.com.ligasdeportivas.service.PartidoService;
import pe.com.ligasdeportivas.service.UsuarioService;

@Controller
@RequestMapping("/v")
public class DashboardController {

	private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

	@Autowired
	private LigaService ligaService;

	@Autowired
	private EquipoService equipoService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private PartidoService partidoService;

	@GetMapping("/paneladm")
	public String panelAdmin(Model model, HttpSession session) {
		try {
			// Obtener usuario de la sesión
			Object usuarioSession = session.getAttribute("usuario");

			if (usuarioSession == null) {
				logger.warn("Intento de acceso sin sesión - Redirigiendo al login");
				return "redirect:/v/login";
			}

			// Agregar usuario al modelo para Thymeleaf
			model.addAttribute("usuario", usuarioSession);
			logger.info("Usuario en sesión: {}", usuarioSession.toString());

			// Descomentar cuando todos los métodos estén implementados
			EstadisticasDashboard estadisticas = new EstadisticasDashboard((int) usuarioService.count(),
					equipoService.findByEstado(Estado.ACTIVO).size(), ligaService.findAll().size(),
					partidoService.findPartidosEsteMes().size());

			model.addAttribute("estadisticas", estadisticas);
			logger.info("Dashboard cargado exitosamente - Usuario: {}, Estadísticas: {}/{}/{}/{}",
					usuarioSession.toString(), estadisticas.getTotalUsuarios(), estadisticas.getEquiposActivos(),
					estadisticas.getTotalLigas(), estadisticas.getPartidosEsteMes());

		} catch (Exception e) {
			logger.error("Error cargando estadísticas del dashboard: {}", e.getMessage(), e);

			EstadisticasDashboard estadisticas = new EstadisticasDashboard(125, 24, 8, 45);
			model.addAttribute("estadisticas", estadisticas);
			logger.warn("Usando datos de ejemplo para el dashboard debido a error");
		}

		return "admin";
	}

	@GetMapping("/login")
	public String verLogin() {
		logger.info("Cargando vista de login");
		return "sportbeatlog";
	}

	// Clase interna para estadísticas
	public static class EstadisticasDashboard {
		private final int totalUsuarios;
		private final int equiposActivos;
		private final int totalLigas;
		private final int partidosEsteMes;

		public EstadisticasDashboard(int totalUsuarios, int equiposActivos, int totalLigas, int partidosEsteMes) {
			this.totalUsuarios = totalUsuarios;
			this.equiposActivos = equiposActivos;
			this.totalLigas = totalLigas;
			this.partidosEsteMes = partidosEsteMes;
		}

		// Getters
		public int getTotalUsuarios() {
			return totalUsuarios;
		}

		public int getEquiposActivos() {
			return equiposActivos;
		}

		public int getTotalLigas() {
			return totalLigas;
		}

		public int getPartidosEsteMes() {
			return partidosEsteMes;
		}
	}
}