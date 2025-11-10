// En tu sportbeatlog.html - Modificar el login
document.addEventListener('DOMContentLoaded', function() {
	const loginForm = document.getElementById("loginForm");

	// Solo agregar el event listener si el formulario existe (página de login)
	if (loginForm) {
		loginForm.addEventListener("submit", function(e) {
			e.preventDefault();
			const username = document.getElementById("username").value;
			const password = document.getElementById("password").value;

			// Validación básica
			if (!username || !password) {
				Swal.fire({
					icon: 'warning',
					title: 'Campos incompletos',
					text: 'Por favor ingrese usuario y contraseña',
					confirmButtonColor: '#000'
				});
				return;
			}

			// Mostrar loading
			Swal.fire({
				title: 'Iniciando sesión...',
				text: 'Verificando credenciales',
				allowOutsideClick: false,
				didOpen: () => {
					Swal.showLoading();
				}
			});

			fetch('/sportbeat/api/user/login', {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify({ username, password })
			})
				.then(async (res) => {
					// Intentar parsear como JSON, si falla, usar texto
					const text = await res.text();
					let data;

					try {
						data = JSON.parse(text);
					} catch (e) {
						data = text; // Si no es JSON, usar el texto directamente
					}

					if (res.ok) {
						return data;
					} else {
						// Manejar diferentes tipos de error
						let mensajeError = 'Error al iniciar sesión';

						if (res.status === 401) {
							const mensaje = typeof data === 'string' ? data : 'Credenciales incorrectas';

							if (mensaje.includes('no encontrado') || mensaje.includes('Usuario no encontrado')) {
								mensajeError = 'Usuario no encontrado';
							} else if (mensaje.includes('contraseña') || mensaje.includes('Contraseña incorrecta')) {
								mensajeError = 'Contraseña incorrecta';
							} else if (mensaje.includes('inactivo') || mensaje.includes('Usuario inactivo')) {
								mensajeError = 'Usuario inactivo. Contacte al administrador';
							} else {
								mensajeError = 'Credenciales inválidas';
							}
						}

						throw new Error(mensajeError);
					}
				})
				.then(data => {
					// Cerrar loading
					Swal.close();

					// Mensaje de éxito personalizado según rol
					let mensajeBienvenida = `¡Bienvenido ${data.username}!`;

					if (data.rol === 'ADMIN') {
						mensajeBienvenida += ' (Administrador)';
					} else if (data.rol === 'ENTRENADOR') {
						mensajeBienvenida += ' (Entrenador)';
					} else if (data.rol === 'JUGADOR') {
						mensajeBienvenida += ' (Jugador)';
					}

					Swal.fire({
						icon: 'success',
						title: '¡Inicio de sesión exitoso!',
						html: mensajeBienvenida,
						showConfirmButton: false,
						timer: 2500,
						timerProgressBar: true,
						didClose: () => {
							// Guardar en sessionStorage
							sessionStorage.setItem('usuario', JSON.stringify(data));
							sessionStorage.setItem('sesionActiva', 'true');
							sessionStorage.setItem('loginTime', new Date().getTime());

							// Redirigir
							redirigirSegunRol(data.rol);
						}
					});
				})
				.catch(err => {
					// Cerrar loading
					Swal.close();

					// Mostrar mensaje de error específico
					Swal.fire({
						icon: 'error',
						title: 'Error de autenticación',
						text: err.message || 'Error al iniciar sesión',
						confirmButtonText: 'Intentar nuevamente',
						confirmButtonColor: '#000'
					});
					console.error('Error:', err);
				});
		});
	}

	// Verificar sesión en todas las páginas (excepto login)
	if (!window.location.pathname.includes('/login')) {
		verificarSesion();
	}
});

function redirigirSegunRol(rol) {
	let ruta = "/v/dashboard";

	if (rol === 'ADMIN') {
		ruta = "/sportbeat/v/paneladm";
	} else if (rol === 'ENTRENADOR') {
		ruta = "/sportbeat/v/coach";
	} else if (rol === 'JUGADOR') {
		ruta = "/sportbeat/v/player";
	}

	// Redirigir después del mensaje de éxito
	window.location.href = ruta;
}

// Verificar sesión al cargar la página (en otras páginas)
function verificarSesion() {
	fetch('/sportbeat/api/user/sesion')
		.then(async (res) => {
			const text = await res.text();
			let data;

			try {
				data = JSON.parse(text);
			} catch (e) {
				data = text;
			}

			if (!res.ok) {
				// Sesión expirada o inválida
				sessionStorage.clear();

				// Mostrar mensaje de sesión expirada
				Swal.fire({
					icon: 'warning',
					title: 'Sesión expirada',
					text: 'Por favor inicie sesión nuevamente',
					confirmButtonText: 'Ir al login',
					confirmButtonColor: '#000'
				}).then(() => {
					window.location.href = "/sportbeat/v/login";
				});
				return;
			}
			return data;
		})
		.then(data => {
			if (data) {
				console.log('Sesión activa:', data);
			}
		})
		.catch(err => {
			console.error('Error verificando sesión:', err);
			sessionStorage.clear();
			window.location.href = "/v/login";
		});
}

// Función para cerrar sesión con SweetAlert
function cerrarSesion() {
	Swal.fire({
		title: '¿Cerrar sesión?',
		text: 'Está seguro que desea salir del sistema',
		icon: 'question',
		showCancelButton: true,
		confirmButtonText: 'Sí, cerrar sesión',
		cancelButtonText: 'Cancelar',
		confirmButtonColor: '#000',
		cancelButtonColor: '#6c757d'
	}).then((result) => {
		if (result.isConfirmed) {
			fetch('/sportbeat/api/user/logout', {
				method: 'POST'
			})
				.then(() => {
					sessionStorage.clear();
					Swal.fire({
						icon: 'success',
						title: 'Sesión cerrada',
						text: 'Ha cerrado sesión correctamente',
						timer: 1500,
						showConfirmButton: false
					}).then(() => {
						window.location.href = "/sportbeat/v/login";
					});
				})
				.catch(err => {
					console.error('Error al cerrar sesión:', err);
					sessionStorage.clear();
					window.location.href = "/sportbeat/v/login";
				});
		}
	});
}