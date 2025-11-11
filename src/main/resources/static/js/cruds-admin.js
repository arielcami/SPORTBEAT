// cruds-admin.js - Sistema de Gestión CRUD para SportsBeat
class CrudsAdmin {
	constructor() {
		this.baseURL = '/sportbeat/api';
		this.init();
	}

	init() {
		this.configurarEventListeners();
		this.configurarSidebarListeners();
		console.log('SportsBeat CRUD Admin: Módulo inicializado correctamente');
	}

	configurarEventListeners() {
		try {
			const botonesAccion = document.querySelectorAll('.accion-rapida');

			if (botonesAccion.length === 0) {
				console.warn('SportsBeat CRUD Admin: No se encontraron botones de acción rápida');
				return;
			}

			botonesAccion.forEach(boton => {
				boton.addEventListener('click', (e) => {
					const accion = e.currentTarget.getAttribute('data-accion');
					console.log(`SportsBeat CRUD Admin: Acción ejecutada: ${accion}`);
					this.ejecutarAccion(accion);
				});
			});

			console.log(`SportsBeat CRUD Admin: ${botonesAccion.length} listeners configurados`);
		} catch (error) {
			console.error('SportsBeat CRUD Admin: Error configurando event listeners:', error);
		}
	}

	configurarSidebarListeners() {
		try {
			const enlacesSidebar = document.querySelectorAll('a[th\\:href*="registro"]');
			enlacesSidebar.forEach(enlace => {
				enlace.addEventListener('click', (e) => {
					e.preventDefault();
					const href = e.currentTarget.getAttribute('th:href');
					this.manejarEnlaceSidebar(href);
				});
			});
			console.log(`SportsBeat CRUD Admin: ${enlacesSidebar.length} listeners de sidebar configurados`);
		} catch (error) {
			console.error('SportsBeat CRUD Admin: Error configurando sidebar listeners:', error);
		}
	}

	manejarEnlaceSidebar(href) {
		const acciones = {
			'/v/registro-jugador': 'registrar-jugador',
			'/v/registro-equipo': 'crear-equipo',
			'/v/registro-resultado': 'programar-partido'
		};

		const accion = acciones[href];
		if (accion) {
			console.log(`SportsBeat CRUD Admin: Navegación desde sidebar a: ${accion}`);
			this.ejecutarAccion(accion);
		} else {
			console.warn(`SportsBeat CRUD Admin: Enlace de sidebar no manejado: ${href}`);
		}
	}

	ejecutarAccion(accion) {
		const accionesValidas = {
			'registrar-jugador': () => this.abrirModalRegistroJugador(),
			'crear-equipo': () => this.abrirModalCrearEquipo(),
			'nueva-liga': () => this.abrirModalNuevaLiga(),
			'programar-partido': () => this.abrirModalProgramarPartido()
		};

		if (accionesValidas[accion]) {
			accionesValidas[accion]();
		} else {
			console.warn(`SportsBeat CRUD Admin: Acción no reconocida: ${accion}`);
			this.mostrarError('Acción no disponible en este momento');
		}
	}

	// ========== REGISTRO DE JUGADOR ==========
	abrirModalRegistroJugador() {
		try {
			Swal.fire({
				title: 'Registrar Nuevo Jugador',
				html: this.getFormularioJugadorCompleto(),
				showCancelButton: true,
				confirmButtonText: 'Registrar Jugador',
				cancelButtonText: 'Cancelar',
				confirmButtonColor: '#3B82F6',
				cancelButtonColor: '#6B7280',
				width: '600px',
				preConfirm: () => {
					return this.procesarRegistroJugadorCompleto();
				},
				didOpen: () => {
					console.log('SportsBeat CRUD Admin: Modal de registro de jugador abierto');
				}
			});
		} catch (error) {
			console.error('SportsBeat CRUD Admin: Error abriendo modal de jugador:', error);
			this.mostrarError('No se pudo abrir el formulario de registro');
		}
	}

	getFormularioJugadorCompleto() {
		return `
            <form id="formRegistroJugador" class="text-left space-y-4">
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Nombre *</label>
                        <input type="text" name="nombre" required 
                               class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                               placeholder="Ej: José">
                    </div>
                    
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Apellido *</label>
                        <input type="text" name="apellido" required 
                               class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                               placeholder="Ej: Canseco">
                    </div>
                </div>

                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Username *</label>
                        <input type="text" name="username" required 
                               class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                               placeholder="Ej: josecanseco">
                    </div>
                    
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Password *</label>
                        <input type="password" name="password" required minlength="6"
                               class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                               placeholder="Mínimo 6 caracteres">
                    </div>
                </div>

                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Email *</label>
                        <input type="email" name="email" required 
                               class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                               placeholder="Ej: jose34canseco@gmail.com">
                    </div>
                    
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Edad *</label>
                        <input type="number" name="edad" required min="16" max="50"
                               class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                               placeholder="Entre 16 y 50 años">
                    </div>
                </div>

                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Posición *</label>
                    <select name="posicion" required class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <option value="">Seleccionar posición</option>
                        <option value="Portero">Portero</option>
                        <option value="Defensa">Defensa</option>
                        <option value="Mediocampista">Mediocampista</option>
                        <option value="Delantero">Delantero</option>
                    </select>
                </div>

                <div class="text-xs text-gray-500 mt-2">
                    * Campos obligatorios. El sistema creará automáticamente un usuario para el jugador.
                </div>
            </form>
        `;
	}

	async procesarRegistroJugadorCompleto() {
		try {
			const form = document.getElementById('formRegistroJugador');
			if (!form) {
				throw new Error('Formulario no encontrado');
			}

			const formData = new FormData(form);
			const datosFormulario = Object.fromEntries(formData.entries());

			// Validación básica del formulario
			if (!this.validarFormularioJugador(datosFormulario)) {
				return false;
			}

			console.log('SportsBeat CRUD Admin: Iniciando registro de jugador:', datosFormulario);

			// Paso 1: Crear usuario
			const datosUsuario = {
				username: datosFormulario.username,
				password: datosFormulario.password,
				email: datosFormulario.email,
				rol: "JUGADOR",
				estado: "ACTIVO"
			};

			console.log('SportsBeat CRUD Admin: Creando usuario:', datosUsuario.username);

			Swal.showLoading();
			const responseUsuario = await axios.post(`${this.baseURL}/user/create`, datosUsuario);

			if (![200, 201].includes(responseUsuario.status)) {
				throw new Error(`Error HTTP ${responseUsuario.status} al crear usuario`);
			}

			const usuarioId = responseUsuario.data.id;
			console.log('SportsBeat CRUD Admin: Usuario creado ID:', usuarioId);

			// Paso 2: Crear jugador
			const datosJugador = {
				nombre: datosFormulario.nombre,
				apellido: datosFormulario.apellido,
				edad: parseInt(datosFormulario.edad),
				posicion: datosFormulario.posicion,
				estado: "ACTIVO",
				usuarioId: usuarioId,
				equipoId: null
			};

			console.log('SportsBeat CRUD Admin: Creando jugador para usuario ID:', usuarioId);

			const responseJugador = await axios.post(`${this.baseURL}/jugadores`, datosJugador);

			if (![200, 201].includes(responseJugador.status)) {
				throw new Error(`Error HTTP ${responseJugador.status} al crear jugador`);
			}

			console.log('SportsBeat CRUD Admin: Jugador registrado exitosamente');
			this.mostrarMensajeExito('Jugador registrado correctamente en el sistema');
			return true;

		} catch (error) {
			console.error('SportsBeat CRUD Admin: Error en registro de jugador:', error);

			const mensajeError = error.response?.data?.message ||
				error.message ||
				'Error desconocido al registrar jugador';

			this.mostrarError(`Error en registro: ${mensajeError}`);
			return false;
		}
	}

	validarFormularioJugador(datos) {
		if (!datos.nombre || !datos.apellido || !datos.username || !datos.password || !datos.email || !datos.edad || !datos.posicion) {
			this.mostrarError('Todos los campos obligatorios deben ser completados');
			return false;
		}

		if (datos.password.length < 6) {
			this.mostrarError('La contraseña debe tener al menos 6 caracteres');
			return false;
		}

		if (datos.edad < 16 || datos.edad > 50) {
			this.mostrarError('La edad debe estar entre 16 y 50 años');
			return false;
		}

		return true;
	}

	// ========== CREACIÓN DE EQUIPO ==========
	abrirModalCrearEquipo() {
		try {
			Swal.fire({
				title: 'Crear Nuevo Equipo',
				html: this.getFormularioEquipo(),
				showCancelButton: true,
				confirmButtonText: 'Crear Equipo',
				cancelButtonText: 'Cancelar',
				confirmButtonColor: '#10B981',
				cancelButtonColor: '#6B7280',
				width: '500px',
				preConfirm: () => {
					return this.procesarCreacionEquipo();
				},
				didOpen: () => {
					console.log('SportsBeat CRUD Admin: Modal de creación de equipo abierto');
				}
			});
		} catch (error) {
			console.error('SportsBeat CRUD Admin: Error abriendo modal de equipo:', error);
			this.mostrarError('No se pudo abrir el formulario de equipo');
		}
	}

	getFormularioEquipo() {
		return `
            <form id="formCrearEquipo" class="text-left space-y-4">
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Nombre del Equipo *</label>
                    <input type="text" name="nombre" required 
                           class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                           placeholder="Ej: Los Tigres del Licey">
                </div>
                
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Liga ID *</label>
                    <input type="number" name="ligaId" required min="1"
                           class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                           placeholder="Ej: 3">
                </div>

                <div class="text-xs text-gray-500 mt-2">
                    * El estado del equipo se establecerá automáticamente como ACTIVO
                </div>
            </form>
        `;
	}

	async procesarCreacionEquipo() {
		try {
			const form = document.getElementById('formCrearEquipo');
			if (!form) {
				throw new Error('Formulario de equipo no encontrado');
			}

			const formData = new FormData(form);
			const datosEquipo = {
				nombre: formData.get('nombre'),
				estado: "ACTIVO",
				ligaId: formData.get('ligaId')
			};

			// Validación
			if (!datosEquipo.nombre || !datosEquipo.ligaId) {
				this.mostrarError('Nombre y Liga ID son obligatorios');
				return false;
			}

			console.log('SportsBeat CRUD Admin: Creando equipo:', datosEquipo);

			Swal.showLoading();
			const response = await axios.post(`${this.baseURL}/equipos`, datosEquipo);

			if (![200, 201].includes(response.status)) {
				throw new Error(`Error HTTP ${response.status} al crear equipo`);
			}

			console.log('SportsBeat CRUD Admin: Equipo creado exitosamente');
			this.mostrarMensajeExito('Equipo creado correctamente');
			return true;

		} catch (error) {
			console.error('SportsBeat CRUD Admin: Error creando equipo:', error);

			const mensajeError = error.response?.data?.message ||
				error.message ||
				'Error desconocido al crear equipo';

			this.mostrarError(`Error al crear equipo: ${mensajeError}`);
			return false;
		}
	}

	// ========== CREACIÓN DE LIGA ==========
	abrirModalNuevaLiga() {
		try {
			Swal.fire({
				title: 'Crear Nueva Liga',
				html: this.getFormularioLiga(),
				showCancelButton: true,
				confirmButtonText: 'Crear Liga',
				cancelButtonText: 'Cancelar',
				confirmButtonColor: '#8B5CF6',
				cancelButtonColor: '#6B7280',
				width: '500px',
				preConfirm: () => {
					return this.procesarCreacionLiga();
				},
				didOpen: () => {
					console.log('SportsBeat CRUD Admin: Modal de creación de liga abierto');
				}
			});
		} catch (error) {
			console.error('SportsBeat CRUD Admin: Error abriendo modal de liga:', error);
			this.mostrarError('No se pudo abrir el formulario de liga');
		}
	}

	getFormularioLiga() {
		return `
            <form id="formCrearLiga" class="text-left space-y-4">
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Nombre de la Liga *</label>
                    <input type="text" name="nombre" required 
                           class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
                           placeholder="Ej: Juvenil FC">
                </div>
                
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Distrito *</label>
                    <input type="text" name="distrito" required 
                           class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
                           placeholder="Ej: Santiago de Surco">
                </div>
                
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Cantidad de Equipos *</label>
                        <input type="number" name="cantidadEquipos" required min="2" max="20"
                               class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
                               placeholder="Ej: 6">
                    </div>
                    
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Cantidad de Partidos *</label>
                        <input type="number" name="cantidadPartidos" required min="1" max="100"
                               class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
                               placeholder="Ej: 16">
                    </div>
                </div>

                <div class="text-xs text-gray-500 mt-2">
                    * El estado de la liga se establecerá automáticamente como ACTIVO
                </div>
            </form>
        `;
	}

	async procesarCreacionLiga() {
		try {
			const form = document.getElementById('formCrearLiga');
			if (!form) {
				throw new Error('Formulario de liga no encontrado');
			}

			const formData = new FormData(form);
			const datosLiga = {
				nombre: formData.get('nombre'),
				distrito: formData.get('distrito'),
				estado: "ACTIVO",
				cantidadEquipos: parseInt(formData.get('cantidadEquipos')),
				cantidadPartidos: parseInt(formData.get('cantidadPartidos'))
			};

			// Validación
			if (!datosLiga.nombre || !datosLiga.distrito || !datosLiga.cantidadEquipos || !datosLiga.cantidadPartidos) {
				this.mostrarError('Todos los campos son obligatorios');
				return false;
			}

			if (datosLiga.cantidadEquipos < 2) {
				this.mostrarError('La liga debe tener al menos 2 equipos');
				return false;
			}

			console.log('SportsBeat CRUD Admin: Creando liga:', datosLiga);

			Swal.showLoading();
			const response = await axios.post(`${this.baseURL}/ligas`, datosLiga);

			if (![200, 201].includes(response.status)) {
				throw new Error(`Error HTTP ${response.status} al crear liga`);
			}

			console.log('SportsBeat CRUD Admin: Liga creada exitosamente');
			this.mostrarMensajeExito('Liga creada correctamente');
			return true;

		} catch (error) {
			console.error('SportsBeat CRUD Admin: Error creando liga:', error);

			const mensajeError = error.response?.data?.message ||
				error.message ||
				'Error desconocido al crear liga';

			this.mostrarError(`Error al crear liga: ${mensajeError}`);
			return false;
		}
	}

	// ========== PROGRAMACIÓN DE PARTIDO ==========
	abrirModalProgramarPartido() {
		try {
			Swal.fire({
				title: 'Programar Nuevo Partido',
				html: this.getFormularioPartido(),
				showCancelButton: true,
				confirmButtonText: 'Programar Partido',
				cancelButtonText: 'Cancelar',
				confirmButtonColor: '#F59E0B',
				cancelButtonColor: '#6B7280',
				width: '500px',
				preConfirm: () => {
					return this.procesarProgramacionPartido();
				},
				didOpen: () => {
					console.log('SportsBeat CRUD Admin: Modal de programación de partido abierto');
				}
			});
		} catch (error) {
			console.error('SportsBeat CRUD Admin: Error abriendo modal de partido:', error);
			this.mostrarError('No se pudo abrir el formulario de partido');
		}
	}

	getFormularioPartido() {
		return `
            <form id="formProgramarPartido" class="text-left space-y-4">
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Liga ID *</label>
                    <input type="number" name="ligaId" required min="1"
                           class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500"
                           placeholder="Ej: 1">
                </div>
                
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Equipo Local ID *</label>
                        <input type="number" name="equipoLocalId" required min="1"
                               class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500"
                               placeholder="Ej: 3">
                    </div>
                    
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Equipo Visitante ID *</label>
                        <input type="number" name="equipoVisitanteId" required min="1"
                               class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500"
                               placeholder="Ej: 1">
                    </div>
                </div>
                
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Fecha *</label>
                        <input type="date" name="fecha" required 
                               class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500">
                    </div>
                    
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Hora *</label>
                        <input type="time" name="hora" required 
                               class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500">
                    </div>
                </div>
                
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Lugar</label>
                    <input type="text" name="lugar" 
                           class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500"
                           placeholder="Ej: Estadio Municipal de Miraflores">
                </div>

                <div class="text-xs text-gray-500 mt-2">
                    * El estado del partido se establecerá automáticamente como ACTIVO
                </div>
            </form>
        `;
	}

	async procesarProgramacionPartido() {
		try {
			const form = document.getElementById('formProgramarPartido');
			if (!form) {
				throw new Error('Formulario de partido no encontrado');
			}

			const formData = new FormData(form);
			const datosPartido = {
				ligaId: parseInt(formData.get('ligaId')),
				equipoLocalId: parseInt(formData.get('equipoLocalId')),
				equipoVisitanteId: parseInt(formData.get('equipoVisitanteId')),
				fecha: formData.get('fecha'),
				hora: formData.get('hora') + ':00', // Formato HH:MM:SS
				lugar: formData.get('lugar') || '',
				estado: "ACTIVO"
			};

			// Validación
			if (!datosPartido.ligaId || !datosPartido.equipoLocalId || !datosPartido.equipoVisitanteId ||
				!datosPartido.fecha || !datosPartido.hora) {
				this.mostrarError('Todos los campos obligatorios deben ser completados');
				return false;
			}

			if (datosPartido.equipoLocalId === datosPartido.equipoVisitanteId) {
				this.mostrarError('El equipo local y visitante no pueden ser el mismo');
				return false;
			}

			console.log('SportsBeat CRUD Admin: Programando partido:', datosPartido);

			Swal.showLoading();
			const response = await axios.post(`${this.baseURL}/partidos`, datosPartido);

			if (![200, 201].includes(response.status)) {
				throw new Error(`Error HTTP ${response.status} al programar partido`);
			}

			console.log('SportsBeat CRUD Admin: Partido programado exitosamente');
			this.mostrarMensajeExito('Partido programado correctamente');
			return true;

		} catch (error) {
			console.error('SportsBeat CRUD Admin: Error programando partido:', error);

			const mensajeError = error.response?.data?.message ||
				error.message ||
				'Error desconocido al programar partido';

			this.mostrarError(`Error al programar partido: ${mensajeError}`);
			return false;
		}
	}

	// ========== UTILIDADES ==========
	mostrarMensajeExito(mensaje) {
		Swal.fire({
			title: '¡Éxito!',
			text: mensaje,
			icon: 'success',
			confirmButtonColor: '#10B981',
			timer: 3000,
			timerProgressBar: true
		});
	}

	mostrarError(mensaje) {
		Swal.fire({
			title: 'Error',
			text: mensaje,
			icon: 'error',
			confirmButtonColor: '#EF4444'
		});
	}
}

// Inicialización robusta con manejo de errores
document.addEventListener('DOMContentLoaded', function() {
	try {
		if (typeof Swal === 'undefined') {
			console.error('SportsBeat CRUD Admin: SweetAlert2 no está cargado');
			return;
		}

		if (typeof axios === 'undefined') {
			console.error('SportsBeat CRUD Admin: Axios no está cargado');
			return;
		}

		window.crudsAdmin = new CrudsAdmin();
		console.log('SportsBeat CRUD Admin: Sistema completamente inicializado y listo');

	} catch (error) {
		console.error('SportsBeat CRUD Admin: Error crítico en inicialización:', error);
	}
});