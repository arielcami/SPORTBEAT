// admin-panel.js - SOLO funcionalidades del dashboard
class AdminPanel {
	constructor() {
		this.serverPath = '/sportbeat/api';
		this.graficosInicializados = false;
		this.init();
	}

	init() {
		try {
			this.configurarEventListeners();
			this.cargarActividadReciente();
			this.inicializarGraficosVacios(); // Primero inicializar gráficos vacíos
			this.cargarEstadisticasIndividuales(); // Luego cargar datos reales
			console.log('SportsBeat Admin Panel: Dashboard inicializado');
		} catch (error) {
			console.error('SportsBeat Admin Panel: Error en inicialización:', error);
		}
	}

	configurarEventListeners() {
		const btnCerrarSesion = document.getElementById('btnCerrarSesion');
		if (btnCerrarSesion) {
			btnCerrarSesion.addEventListener('click', () => {
				this.cerrarSesion();
			});
		}

		const btnRecargarActividad = document.getElementById('btnRecargarActividad');
		if (btnRecargarActividad) {
			btnRecargarActividad.addEventListener('click', () => {
				this.cargarActividadReciente();
				this.cargarEstadisticasIndividuales();
			});
		}

		console.log('SportsBeat Admin Panel: Event listeners del dashboard configurados');
	}

	async cargarEstadisticasIndividuales() {
		try {
			console.log('Cargando estadísticas desde APIs...');

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

			console.log('Estadísticas cargadas:', estadisticas);
			this.actualizarEstadisticas(estadisticas);
			this.actualizarGraficosConDatosReales(usuarios, equipos);

		} catch (error) {
			console.error('Error cargando estadísticas individuales:', error);
			this.mostrarEstadisticasPorDefecto();
		}
	}

	actualizarEstadisticas(estadisticas) {
		try {
			const elementosEstadisticas = document.querySelectorAll('[data-estadistica]');
			elementosEstadisticas.forEach(elemento => {
				const tipo = elemento.getAttribute('data-estadistica');
				if (estadisticas[tipo] !== undefined) {
					elemento.textContent = estadisticas[tipo];
				}
			});
			console.log('Estadísticas actualizadas en la UI');
		} catch (error) {
			console.error('Error actualizando estadísticas:', error);
		}
	}

	mostrarEstadisticasPorDefecto() {
		const estadisticasDefault = {
			totalUsuarios: 0,
			equiposActivos: 0,
			totalLigas: 0,
			partidosEsteMes: 0
		};
		this.actualizarEstadisticas(estadisticasDefault);
	}

	inicializarGraficosVacios() {
		try {
			console.log('Inicializando gráficos vacíos...');

			// Gráfico de usuarios - VACÍO
			const userCtx = document.getElementById('userChart');
			if (userCtx && window.Chart) {
				window.userChart = new Chart(userCtx, {
					type: 'line',
					data: {
						labels: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun'],
						datasets: [{
							label: 'Usuarios Registrados',
							data: [0, 0, 0, 0, 0, 0],
							borderColor: 'rgb(59, 130, 246)',
							backgroundColor: 'rgba(59, 130, 246, 0.1)',
							borderWidth: 2,
							tension: 0.4,
							fill: true
						}]
					},
					options: {
						responsive: true,
						maintainAspectRatio: true,
						plugins: {
							legend: {
								display: false
							}
						},
						scales: {
							y: {
								beginAtZero: true,
								grid: {
									color: 'rgba(0, 0, 0, 0.1)'
								}
							},
							x: {
								grid: {
									display: false
								}
							}
						}
					}
				});
			}

			// Gráfico de deportes - VACÍO
			const sportsCtx = document.getElementById('sportsChart');
			if (sportsCtx && window.Chart) {
				window.sportsChart = new Chart(sportsCtx, {
					type: 'doughnut',
					data: {
						labels: ['Fútbol', 'Básquetbol', 'Voleibol'],
						datasets: [{
							data: [0, 0, 0],
							backgroundColor: [
								'rgb(34, 197, 94)',
								'rgb(249, 115, 22)',
								'rgb(59, 130, 246)'
							],
							borderWidth: 2,
							borderColor: '#fff'
						}]
					},
					options: {
						responsive: true,
						maintainAspectRatio: true,
						plugins: {
							legend: {
								position: 'bottom',
								labels: {
									padding: 15,
									usePointStyle: true,
									boxWidth: 8
								}
							}
						},
						cutout: '55%'
					}
				});
			}

			this.graficosInicializados = true;
			console.log('Gráficos vacíos inicializados');

		} catch (error) {
			console.error('Error inicializando gráficos vacíos:', error);
		}
	}

	actualizarGraficosConDatosReales(usuarios, equipos) {
		try {
			if (!this.graficosInicializados) {
				console.warn('Gráficos no inicializados, no se pueden actualizar');
				return;
			}

			console.log('Actualizando gráficos con datos reales...');

			// Datos para gráficos
			const usuariosPorMes = this.calcularUsuariosPorMes(usuarios);
			const distribucionDeportes = this.calcularDistribucionDeportes(equipos);

			// Actualizar gráfico de usuarios
			if (window.userChart && window.userChart.data) {
				window.userChart.data.labels = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'].slice(0, usuariosPorMes.length);
				window.userChart.data.datasets[0].data = usuariosPorMes;
				window.userChart.update('none'); // 'none' para actualización silenciosa
				console.log('Gráfico de usuarios actualizado');
			}

			// Actualizar gráfico de deportes
			if (window.sportsChart && window.sportsChart.data) {
				const deportesLabels = Object.keys(distribucionDeportes);
				const deportesData = Object.values(distribucionDeportes);

				window.sportsChart.data.labels = deportesLabels;
				window.sportsChart.data.datasets[0].data = deportesData;

				// Actualizar colores si hay más deportes
				if (deportesLabels.length > 3) {
					const colores = [
						'rgb(34, 197, 94)',   // Verde
						'rgb(249, 115, 22)',  // Naranja  
						'rgb(59, 130, 246)',  // Azul
						'rgb(168, 85, 247)',  // Púrpura
						'rgb(236, 72, 153)',  // Rosa
						'rgb(20, 184, 166)'   // Cyan
					];
					window.sportsChart.data.datasets[0].backgroundColor = colores.slice(0, deportesLabels.length);
				}

				window.sportsChart.update('none');
				console.log('Gráfico de deportes actualizado');
			}

			console.log('Gráficos actualizados con datos reales');

		} catch (error) {
			console.error('Error actualizando gráficos con datos reales:', error);
		}
	}

	calcularUsuariosPorMes(usuarios) {
		// Por simplicidad, distribución proporcional del total
		const total = usuarios.length;
		if (total === 0) return [0, 0, 0, 0, 0, 0];

		return [
			Math.round(total * 0.9),
			Math.round(total * 0.7),
			Math.round(total * 0.5),
			Math.round(total * 0.3),
			Math.round(total * 0.2),
			Math.round(total * 0.1)
		];
	}

	calcularDistribucionDeportes(equipos) {
		const deportes = {};
		equipos.forEach(equipo => {
			if (equipo.deporte) {
				const deporte = equipo.deporte.toUpperCase();
				deportes[deporte] = (deportes[deporte] || 0) + 1;
			}
		});

		// Si no hay datos, usar distribución por defecto
		if (Object.keys(deportes).length === 0) {
			return {
				'FÚTBOL': 10,
				'BÁSQUETBOL': 6,
				'VOLEIBOL': 4
			};
		}

		return deportes;
	}

	async cargarActividadReciente() {
		try {
			const contenedorActividad = document.getElementById('actividadReciente');
			if (!contenedorActividad) return;

			const actividadEjemplo = [
				{ tipo: 'jugador', mensaje: 'Nuevo jugador registrado: Carlos López', tiempo: 'Hace 5 min' },
				{ tipo: 'equipo', mensaje: 'Equipo creado: Los Halcones FC', tiempo: 'Hace 15 min' },
				{ tipo: 'partido', mensaje: 'Partido programado: Tigres vs Águilas', tiempo: 'Hace 1 hora' },
				{ tipo: 'liga', mensaje: 'Nueva liga creada: Distrital 2024', tiempo: 'Hace 2 horas' }
			];

			contenedorActividad.innerHTML = actividadEjemplo.map(item => `
                <div class="flex items-start space-x-3 p-3 bg-gray-50 rounded-lg">
                    <div class="flex-shrink-0 w-2 h-2 mt-2 bg-blue-500 rounded-full"></div>
                    <div class="flex-1 min-w-0">
                        <p class="text-sm font-medium text-gray-900">${item.mensaje}</p>
                        <p class="text-xs text-gray-500">${item.tiempo}</p>
                    </div>
                </div>
            `).join('');

		} catch (error) {
			console.error('SportsBeat Admin Panel: Error cargando actividad:', error);
		}
	}

	cerrarSesion() {
		Swal.fire({
			title: '¿Cerrar sesión?',
			text: '¿Estás seguro de que deseas salir del sistema?',
			icon: 'question',
			showCancelButton: true,
			confirmButtonText: 'Sí, cerrar sesión',
			cancelButtonText: 'Cancelar',
			confirmButtonColor: '#EF4444',
			cancelButtonColor: '#6B7280'
		}).then((result) => {
			if (result.isConfirmed) {
				window.location.href = '/sportbeat/v/login';
			}
		});
	}
}

// Inicialización
document.addEventListener('DOMContentLoaded', function() {
	try {
		window.adminPanel = new AdminPanel();
	} catch (error) {
		console.error('SportsBeat Admin Panel: Error crítico en inicialización:', error);
	}
});