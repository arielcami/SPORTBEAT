class AdminPanel {
	constructor() {
		this.serverPath = '/sportbeat/api';
		this.init();
	}

	init() {
		this.setupAxiosInterceptors();
		this.setupGlobalErrorHandling();
		this.initializeApplication();
	}

	setupAxiosInterceptors() {
		// Request Interceptor
		axios.interceptors.request.use(
			(config) => {
				console.log(`Making ${config.method?.toUpperCase()} request to: ${config.url}`);
				// Aquí podríamos agregar tokens de autenticación
				config.timeout = 10000; // 10 segundos timeout
				return config;
			},
			(error) => {
				console.error('Request Interceptor Error:', error);
				return Promise.reject(error);
			}
		);

		// Response Interceptor
		axios.interceptors.response.use(
			(response) => {
				console.log(`Response received from: ${response.config.url}`, response.status);
				return response;
			},
			(error) => {
				console.error('Response Interceptor Error:', error);

				if (error.response) {
					// Server responded with error status
					this.handleHttpError(error.response);
				} else if (error.request) {
					// Request was made but no response received
					this.showNotification('Error de conexión', 'No se pudo conectar con el servidor', 'error');
				} else {
					// Something else happened
					this.showNotification('Error', 'Error inesperado en la aplicación', 'error');
				}

				return Promise.reject(error);
			}
		);
	}

	setupGlobalErrorHandling() {
		window.addEventListener('error', (event) => {
			console.error('Global Error:', event.error);
			this.showNotification('Error', 'Ha ocurrido un error inesperado', 'error');
		});

		window.addEventListener('unhandledrejection', (event) => {
			console.error('Unhandled Promise Rejection:', event.reason);
			this.showNotification('Error', 'Error en operación asíncrona', 'error');
			event.preventDefault();
		});
	}

	async initializeApplication() {
		try {
			await this.verificarSesion();
			await this.cargarDashboard();
			this.inicializarEventListeners();
		} catch (error) {
			console.error('Error inicializando aplicación:', error);
			this.showNotification('Error', 'No se pudo inicializar la aplicación', 'error');
		}
	}

	async verificarSesion() {
		try {
			const response = await axios.get(`${this.serverPath}/user/sesion`);
			if (response.data.sesionActiva) {
				console.log('Sesión activa:', response.data.usuario);
				this.actualizarUIUsuario(response.data.usuario);
			} else {
				this.redirectToLogin();
			}
		} catch (error) {
			console.warn('No hay sesión activa o error verificando sesión');
			this.redirectToLogin();
		}
	}

	actualizarUIUsuario(usuario) {
		const usernameDisplay = document.getElementById('usernameDisplay');
		if (usernameDisplay && usuario.username) {
			usernameDisplay.textContent = usuario.username;
		}
	}

	redirectToLogin() {
		this.showNotification('Sesión Expirada', 'Por favor, inicie sesión nuevamente', 'warning');
		setTimeout(() => {
			window.location.href = '/sportbeat/v/login';
		}, 2000);
	}

	async cargarDashboard() {
		await Promise.allSettled([
			this.cargarEstadisticasEnTiempoReal(),
			this.cargarActividadReciente()
		]);
	}

	async cargarEstadisticasEnTiempoReal() {
		try {
			this.mostrarLoadingEstadisticas(true);

			const response = await axios.get(`${this.serverPath}/dashboard/estadisticas`);

			if (this.isValidEstadisticas(response.data)) {
				this.actualizarEstadisticas(response.data);
				await this.cargarDatosGraficos();
			} else {
				throw new Error('Datos de estadísticas inválidos');
			}

		} catch (error) {
			console.warn('Error cargando estadísticas principales, usando respaldo:', error);
			await this.cargarEstadisticasIndividuales();
		} finally {
			this.mostrarLoadingEstadisticas(false);
		}
	}

	mostrarLoadingEstadisticas(mostrar) {
		const elementos = document.querySelectorAll('[data-estadistica]');
		elementos.forEach(elemento => {
			if (mostrar) {
				elemento.innerHTML = '<div class="animate-pulse bg-gray-200 h-6 w-12 rounded"></div>';
			}
		});
	}

	isValidEstadisticas(estadisticas) {
		return estadisticas &&
			typeof estadisticas.totalUsuarios === 'number' &&
			typeof estadisticas.equiposActivos === 'number' &&
			typeof estadisticas.totalLigas === 'number' &&
			typeof estadisticas.partidosEsteMes === 'number';
	}

	actualizarEstadisticas(estadisticas) {
		const mapeoEstadisticas = {
			'totalUsuarios': estadisticas.totalUsuarios,
			'equiposActivos': estadisticas.equiposActivos,
			'totalLigas': estadisticas.totalLigas,
			'partidosEsteMes': estadisticas.partidosEsteMes
		};

		Object.entries(mapeoEstadisticas).forEach(([key, value]) => {
			const elemento = document.querySelector(`[data-estadistica="${key}"]`);
			if (elemento) {
				elemento.textContent = this.formatearNumero(value);
			}
		});
	}

	formatearNumero(numero) {
		return new Intl.NumberFormat('es-PE').format(numero);
	}

	async cargarEstadisticasIndividuales() {
		try {
			const [usuariosResponse, equiposResponse, ligasResponse, partidosResponse] = await Promise.allSettled([
				axios.get(`${this.serverPath}/user/list`),
				axios.get(`${this.serverPath}/equipos`),
				axios.get(`${this.serverPath}/ligas`),
				axios.get(`${this.serverPath}/partidos`)
			]);

			const usuarios = usuariosResponse.status === 'fulfilled' ? usuariosResponse.value.data : [];
			const equipos = equiposResponse.status === 'fulfilled' ? equiposResponse.value.data : [];
			const ligas = ligasResponse.status === 'fulfilled' ? ligasResponse.value.data : [];
			const partidos = partidosResponse.status === 'fulfilled' ? partidosResponse.value.data : [];

			const estadisticas = {
				totalUsuarios: usuarios.length,
				equiposActivos: equipos.filter(e => e.estado === 'ACTIVO').length,
				totalLigas: ligas.length,
				partidosEsteMes: partidos.filter(p => {
					if (!p.fecha) return false;
					const fechaPartido = new Date(p.fecha);
					const hoy = new Date();
					const inicioMes = new Date(hoy.getFullYear(), hoy.getMonth(), 1);
					return fechaPartido >= inicioMes && fechaPartido <= hoy;
				}).length
			};

			this.actualizarEstadisticas(estadisticas);
		} catch (error) {
			console.error('Error cargando estadísticas individuales:', error);
			this.mostrarEstadisticasPorDefecto();
		}
	}

	mostrarEstadisticasPorDefecto() {
		const estadisticasPorDefecto = {
			totalUsuarios: 0,
			equiposActivos: 0,
			totalLigas: 0,
			partidosEsteMes: 0
		};
		this.actualizarEstadisticas(estadisticasPorDefecto);
	}

	async cargarDatosGraficos() {
		try {
			const [usuariosResponse, equiposResponse, ligasResponse] = await Promise.allSettled([
				axios.get(`${this.serverPath}/user/list`),
				axios.get(`${this.serverPath}/equipos`),
				axios.get(`${this.serverPath}/ligas`)
			]);

			const usuarios = usuariosResponse.status === 'fulfilled' ? usuariosResponse.value.data : [];
			const equipos = equiposResponse.status === 'fulfilled' ? equiposResponse.value.data : [];
			const ligas = ligasResponse.status === 'fulfilled' ? ligasResponse.value.data : [];

			this.actualizarGraficos(usuarios, equipos, ligas);
		} catch (error) {
			console.error('Error cargando datos para gráficos:', error);
		}
	}

	actualizarGraficos(usuarios, equipos, ligas) {
		this.actualizarGraficoUsuarios(usuarios);
		this.actualizarGraficoDeportes(equipos, ligas);
	}

	actualizarGraficoUsuarios(usuarios) {
		const usuariosPorMes = this.calcularUsuariosPorMes(usuarios);
		const userChart = Chart.getChart("userChart");

		if (userChart) {
			userChart.data.datasets[0].data = usuariosPorMes;
			userChart.update('active');
		}
	}

	actualizarGraficoDeportes(equipos, ligas) {
		const distribucionDeportes = this.calcularDistribucionDeportes(equipos, ligas);
		const sportsChart = Chart.getChart("sportsChart");

		if (sportsChart) {
			sportsChart.data.datasets[0].data = distribucionDeportes.valores;
			sportsChart.data.labels = distribucionDeportes.labels;
			sportsChart.update('active');
		}
	}

	calcularUsuariosPorMes(usuarios) {
		// Implementación básica - mejorar según necesidades
		const meses = [0, 0, 0, 0, 0, 0]; // Últimos 6 meses
		usuarios.forEach(usuario => {
			if (usuario.fechaRegistro) {
				const fecha = new Date(usuario.fechaRegistro);
				const mes = fecha.getMonth();
				if (mes >= 0 && mes < 6) {
					meses[mes]++;
				}
			}
		});
		return meses;
	}

	calcularDistribucionDeportes(equipos, ligas) {
		const deportes = {
			'Fútbol': 0,
			'Voleibol': 0,
			'Básquetbol': 0,
			'Otros': 0
		};

		equipos.forEach(equipo => {
			const deporte = this.obtenerDeporteDeEquipo(equipo, ligas);
			deportes[deporte] = (deportes[deporte] || 0) + 1;
		});

		const labels = Object.keys(deportes);
		const valores = Object.values(deportes);

		return { labels, valores };
	}

	obtenerDeporteDeEquipo(equipo, ligas) {
		if (!equipo.ligaId) return 'Otros';

		const liga = ligas.find(l => l.id === equipo.ligaId);
		if (!liga || !liga.nombre) return 'Otros';

		const nombreLiga = liga.nombre.toLowerCase();
		if (nombreLiga.includes('fútbol') || nombreLiga.includes('futbol')) return 'Fútbol';
		if (nombreLiga.includes('voleibol')) return 'Voleibol';
		if (nombreLiga.includes('básquet') || nombreLiga.includes('basquet')) return 'Básquetbol';

		return 'Otros';
	}

	inicializarEventListeners() {
		// Event listeners para botones de acciones rápidas
		document.querySelectorAll('.accion-rapida').forEach(button => {
			button.addEventListener('click', (e) => {
				e.preventDefault();
				const accion = button.dataset.accion;
				this.ejecutarAccionRapida(accion);
			});
		});

		// Event listener para recargar actividad
		const btnRecargarActividad = document.querySelector('[onclick="cargarActividadReciente()"]');
		if (btnRecargarActividad) {
			btnRecargarActividad.addEventListener('click', (e) => {
				e.preventDefault();
				this.cargarActividadReciente();
			});
		}

		// Event listener para cerrar sesión
		const btnCerrarSesion = document.querySelector('[onclick="cerrarSesion()"]');
		if (btnCerrarSesion) {
			btnCerrarSesion.addEventListener('click', (e) => {
				e.preventDefault();
				this.cerrarSesion();
			});
		}
	}

	ejecutarAccionRapida(accion) {
		const rutas = {
			'registrar-jugador': '/sportbeat/v/registro-jugador',
			'crear-equipo': '/sportbeat/v/registro-equipo',
			'nueva-liga': '/sportbeat/v/ligas',
			'programar-partido': '/sportbeat/v/calendario'
		};

		if (rutas[accion]) {
			window.location.href = rutas[accion];
		} else {
			console.warn(`Acción no reconocida: ${accion}`);
		}
	}

	async cargarActividadReciente() {
		try {
			this.mostrarLoadingActividad(true);

			const [jugadoresResponse, partidosResponse, ligasResponse] = await Promise.allSettled([
				axios.get(`${this.serverPath}/jugadores`),
				axios.get(`${this.serverPath}/partidos`),
				axios.get(`${this.serverPath}/ligas`)
			]);

			const jugadores = jugadoresResponse.status === 'fulfilled' ? jugadoresResponse.value.data : [];
			const partidos = partidosResponse.status === 'fulfilled' ? partidosResponse.value.data : [];
			const ligas = ligasResponse.status === 'fulfilled' ? ligasResponse.value.data : [];

			console.log('Datos recibidos para actividad:', { jugadores, partidos, ligas });

			const actividad = this.generarActividadReciente(jugadores, partidos, ligas);
			this.mostrarActividadReciente(actividad);

		} catch (error) {
			console.error('Error cargando actividad reciente:', error);
			this.mostrarActividadError();
		} finally {
			this.mostrarLoadingActividad(false);
		}
	}

	mostrarLoadingActividad(mostrar) {
		const container = document.getElementById('actividadReciente');
		if (!container) return;

		if (mostrar) {
			container.innerHTML = `
                <div class="flex items-center justify-center py-4">
                    <div class="animate-spin rounded-full h-6 w-6 border-b-2 border-blue-600"></div>
                    <span class="ml-2 text-gray-500">Cargando actividad...</span>
                </div>
            `;
		}
	}

	generarActividadReciente(jugadores, partidos, ligas) {
		const actividad = [];

		// Últimos jugadores registrados (máximo 2)
		const jugadoresRecientes = Array.isArray(jugadores) ? jugadores.slice(-2).reverse() : [];
		jugadoresRecientes.forEach(jugador => {
			if (jugador.nombre && jugador.apellido) {
				actividad.push({
					tipo: 'jugador',
					descripcion: `Nuevo jugador: ${jugador.nombre} ${jugador.apellido}`,
					tiempo: this.calcularTiempoRelativo(jugador.fechaRegistro)
				});
			}
		});

		// Partidos recientes (máximo 2)
		const partidosRecientes = Array.isArray(partidos) ? partidos.slice(-2).reverse() : [];
		partidosRecientes.forEach(partido => {
			if (partido.equipoLocalNombre && partido.equipoVisitanteNombre) {
				actividad.push({
					tipo: 'partido',
					descripcion: `Partido ${partido.estado || 'programado'}: ${partido.equipoLocalNombre} vs ${partido.equipoVisitanteNombre}`,
					tiempo: this.calcularTiempoRelativo(partido.fecha)
				});
			}
		});

		// Ordenar por timestamp (si está disponible) y limitar a 4 elementos
		return actividad
			.sort((a, b) => new Date(b.timestamp || 0) - new Date(a.timestamp || 0))
			.slice(0, 4);
	}

	calcularTiempoRelativo(fecha) {
		if (!fecha) return 'Reciente';

		try {
			const fechaObj = new Date(fecha);
			const ahora = new Date();
			const diferencia = ahora - fechaObj;
			const minutos = Math.floor(diferencia / (1000 * 60));
			const horas = Math.floor(diferencia / (1000 * 60 * 60));
			const dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));

			if (minutos < 1) return 'Ahora mismo';
			if (minutos < 60) return `Hace ${minutos} minuto${minutos !== 1 ? 's' : ''}`;
			if (horas < 24) return `Hace ${horas} hora${horas !== 1 ? 's' : ''}`;
			if (dias < 7) return `Hace ${dias} día${dias !== 1 ? 's' : ''}`;

			return fechaObj.toLocaleDateString('es-PE');
		} catch (error) {
			return 'Reciente';
		}
	}

	mostrarActividadReciente(actividad) {
		const container = document.getElementById('actividadReciente');
		if (!container) return;

		if (actividad.length > 0) {
			container.innerHTML = actividad.map(item => `
                <div class="flex items-center space-x-3 animate-fade-in">
                    <div class="w-8 h-8 ${this.getColorClass(item.tipo)} rounded-full flex items-center justify-center flex-shrink-0">
                        <span class="text-sm">${this.getIcono(item.tipo)}</span>
                    </div>
                    <div class="flex-1 min-w-0">
                        <p class="text-sm font-medium text-gray-900 truncate">${this.escapeHtml(item.descripcion)}</p>
                        <p class="text-xs text-gray-500">${this.escapeHtml(item.tiempo)}</p>
                    </div>
                </div>
            `).join('');
		} else {
			container.innerHTML = `
                <div class="text-center text-gray-500 py-4">
                    <p>No hay actividad reciente</p>
                    <button onclick="adminPanel.cargarActividadReciente()" class="text-blue-600 hover:text-blue-800 text-sm mt-2">
                        Reintentar
                    </button>
                </div>
            `;
		}
	}

	mostrarActividadError() {
		const container = document.getElementById('actividadReciente');
		if (!container) return;

		container.innerHTML = `
            <div class="text-center text-gray-500 py-4">
                <p>Error al cargar actividad</p>
                <button onclick="adminPanel.cargarActividadReciente()" class="text-blue-600 hover:text-blue-800 text-sm mt-2">
                    Reintentar
                </button>
            </div>
        `;
	}

	getColorClass(tipo) {
		const colores = {
			'jugador': 'bg-blue-100 border border-blue-200',
			'partido': 'bg-green-100 border border-green-200',
			'programacion': 'bg-purple-100 border border-purple-200',
			'liga': 'bg-orange-100 border border-orange-200',
			'info': 'bg-gray-100 border border-gray-200'
		};
		return colores[tipo] || 'bg-gray-100 border border-gray-200';
	}

	getIcono(tipo) {
		const iconos = {
			'jugador': '👤',
			'partido': '🏆',
			'programacion': '📅',
			'liga': '🏟️',
			'info': 'ℹ️'
		};
		return iconos[tipo] || '📝';
	}

	escapeHtml(unsafe) {
		if (typeof unsafe !== 'string') return unsafe;
		return unsafe
			.replace(/&/g, "&amp;")
			.replace(/</g, "&lt;")
			.replace(/>/g, "&gt;")
			.replace(/"/g, "&quot;")
			.replace(/'/g, "&#039;");
	}

	handleHttpError(response) {
		const { status, data } = response;

		switch (status) {
			case 401:
				this.showNotification('No Autorizado', 'Su sesión ha expirado', 'warning');
				this.redirectToLogin();
				break;
			case 403:
				this.showNotification('Prohibido', 'No tiene permisos para esta acción', 'error');
				break;
			case 404:
				this.showNotification('No Encontrado', 'Recurso no encontrado', 'warning');
				break;
			case 500:
				this.showNotification('Error del Servidor', 'Error interno del servidor', 'error');
				break;
			default:
				this.showNotification('Error', `Error ${status}: ${data?.message || 'Error desconocido'}`, 'error');
		}
	}

	async cerrarSesion() {
		try {
			await axios.post(`${this.serverPath}/user/logout`);
			this.showNotification('Sesión Cerrada', 'Ha cerrado sesión correctamente', 'success');
			setTimeout(() => {
				window.location.href = '/sportbeat/v/login';
			}, 1500);
		} catch (error) {
			console.error('Error cerrando sesión:', error);
			// Redirigir de todas formas
			window.location.href = '/sportbeat/v/login';
		}
	}

	showNotification(titulo, mensaje, tipo = 'info') {
		if (typeof Swal !== 'undefined') {
			Swal.fire({
				title: titulo,
				text: mensaje,
				icon: tipo,
				toast: true,
				position: 'top-end',
				showConfirmButton: false,
				timer: 3000,
				timerProgressBar: true
			});
		} else {
			// Fallback básico
			console.log(`${tipo.toUpperCase()}: ${titulo} - ${mensaje}`);
		}
	}
}

// Inicializar la aplicación cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
	window.adminPanel = new AdminPanel();
});

// Exportar para uso global si es necesario
if (typeof module !== 'undefined' && module.exports) {
	module.exports = AdminPanel;
}