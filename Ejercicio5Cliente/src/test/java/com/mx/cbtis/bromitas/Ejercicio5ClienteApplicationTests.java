package com.mx.cbtis.bromitas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.mx.cbtis.Ejercicio5ClienteApplication;
import com.mx.cbtis.controller.GestorController;
import com.mx.cbtis.modelo.Asignaciones;
import com.mx.cbtis.modelo.Comentarios;
import com.mx.cbtis.modelo.Proyectos;
import com.mx.cbtis.modelo.Tareas;
import com.mx.cbtis.modelo.Usuario;
import com.sun.net.httpserver.HttpServer;

@SpringBootTest(classes = Ejercicio5ClienteApplication.class)
class Ejercicio5ClienteApplicationTests {

	private static HttpServer mockBackend;

	@BeforeAll
	static void startMockBackend() throws IOException {
		mockBackend = HttpServer.create(new InetSocketAddress(8081), 0);
		mockBackend.createContext("/", exchange -> {
			exchange.getRequestBody().readAllBytes();
			String path = exchange.getRequestURI().getPath();
			String jsonResponse;

			if (path.equals("/buscarProyectos") || path.equals("/buscarUsuarios")
					|| path.equals("/getTarea") || path.equals("/getComentario")
					|| path.equals("/buscarTareas") || path.equals("/buscarComentarios")) {
				jsonResponse = "[{}]";
			} else if (path.equals("/buscarProyecto") || path.equals("/getUsuarioLogin")
					|| path.equals("/buscarTarea")) {
				jsonResponse = "{}";
			} else {
				jsonResponse = "1";
			}

			byte[] bytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
			exchange.getResponseHeaders().add("Content-Type", "application/json");
			exchange.sendResponseHeaders(200, bytes.length);
			try (OutputStream os = exchange.getResponseBody()) {
				os.write(bytes);
			}
		});
		mockBackend.start();
	}

	@AfterAll
	static void stopMockBackend() {
		if (mockBackend != null) {
			mockBackend.stop(0);
		}
	}

	@Test
	void contextLoads() {
		Ejercicio5ClienteApplication app = new Ejercicio5ClienteApplication();
		assertNotNull(app);
	}

	@Test
	void testModelosCobertura() {
		Asignaciones asig = new Asignaciones(1, 2, 3, "2026-10-20");
		asig.setId_asignacion(10);
		asig.setId_usuario(20);
		asig.setId_tarea(30);
		asig.setFecha_asignacion("2026-10-21");
		assertEquals(10, asig.getId_asignacion());
		assertEquals(20, asig.getId_usuario());
		assertEquals(30, asig.getId_tarea());
		assertEquals("2026-10-21", asig.getFecha_asignacion());
		assertNotNull(new Asignaciones());

		Comentarios com = new Comentarios(1, "user1", 2, "Texto", "2026-10-20");
		com.setId_comentario(11);
		com.setId_usuario("user2");
		com.setId_tarea(22);
		com.setTexto_comentario("Nuevo texto");
		com.setFecha_hora("2026-10-21");
		assertEquals(11, com.getId_comentario());
		assertEquals("user2", com.getId_usuario());
		assertEquals(22, com.getId_tarea());
		assertEquals("Nuevo texto", com.getTexto_comentario());
		assertEquals("2026-10-21", com.getFecha_hora());
		assertNotNull(new Comentarios());

		Proyectos proy = new Proyectos(1, "Nombre", "Desc", "2026-01-01", "2026-12-31", "Activo", "fondo.png");
		proy.setId_proyecto(100);
		proy.setNombre("Proyecto X");
		proy.setDescripcion("Descripcion X");
		proy.setFecha_inicio("2026-02-01");
		proy.setFecha_finalizacion("2026-11-30");
		proy.setEstado_proyecto("En progreso");
		proy.setFondo("azul.png");
		assertEquals(100, proy.getId_proyecto());
		assertEquals("Proyecto X", proy.getNombre());
		assertEquals("Descripcion X", proy.getDescripcion());
		assertEquals("2026-02-01", proy.getFecha_inicio());
		assertEquals("2026-11-30", proy.getFecha_finalizacion());
		assertEquals("En progreso", proy.getEstado_proyecto());
		assertEquals("azul.png", proy.getFondo());
		assertNotNull(new Proyectos());

		Tareas tar = new Tareas(1, "Tarea 1", "Desc T1", "2026-05-01", "Por hacer", "Pendiente", 100);
		tar.setId_tarea(50);
		tar.setNombre("Tarea 2");
		tar.setDescripcion("Desc T2");
		tar.setFecha_vencimiento("2026-06-01");
		tar.setLista("En proceso");
		tar.setEstado("Completada");
		tar.setId_proyecto(200);
		assertEquals(50, tar.getId_tarea());
		assertEquals("Tarea 2", tar.getNombre());
		assertEquals("Desc T2", tar.getDescripcion());
		assertEquals("2026-06-01", tar.getFecha_vencimiento());
		assertEquals("En proceso", tar.getLista());
		assertEquals("Completada", tar.getEstado());
		assertEquals(200, tar.getId_proyecto());
		assertNotNull(new Tareas());

		Usuario usu = new Usuario(1, "Admin", "admin@test.com", "1234", "ADMIN", "avatar.png");
		usu.setId_usuario(99);
		usu.setNombre("User");
		usu.setCorreo_electronico("user@test.com");
		usu.setContrasena("5678");
		usu.setRol("USER");
		usu.setImagen_usuario("perfil.png");
		assertEquals(99, usu.getId_usuario());
		assertEquals("User", usu.getNombre());
		assertEquals("user@test.com", usu.getCorreo_electronico());
		assertEquals("5678", usu.getContrasena());
		assertEquals("USER", usu.getRol());
		assertEquals("perfil.png", usu.getImagen_usuario());
		assertNotNull(new Usuario());
	}

	@Test
	void testGestorControllerEndpoints() {
		GestorController controller = new GestorController();
		Model model = new ConcurrentModel();

		assertEquals("home", controller.getHome());
		assertEquals("login", controller.getLogin());
		assertEquals("register", controller.getRegister(model));
		assertEquals("inicio", controller.getInicio(model));
		assertEquals("proyecto", controller.getTarea(model));
		assertEquals("proyecto", controller.getProyecto(model));

		Usuario usuario = new Usuario(1, "Test", "test@mail.com", "pass", "ADMIN", "img.png");
		ModelAndView mavUsuario = controller.setSaveUsuario(usuario);
		assertEquals("redirect:/login", mavUsuario.getViewName());

		ResponseEntity<Usuario> respLogin = controller.verificarUsuario(usuario);
		assertEquals(200, respLogin.getStatusCode().value());

		ResponseEntity<Object> respUsuarios = controller.consultarUsuarios(model);
		assertEquals(200, respUsuarios.getStatusCode().value());

		Proyectos proyecto = new Proyectos(1, "Proy", "Desc", "2026-01-01", "2026-12-31", "Activo", "bg.png");
		ModelAndView mavProy = controller.setSaveProyecto(proyecto);
		assertEquals("redirect:/inicio", mavProy.getViewName());

		ResponseEntity<Object> respProy = controller.consultarProyectos(model);
		assertEquals(200, respProy.getStatusCode().value());

		ModelAndView mavDelProy = controller.eliminarProyecto(proyecto);
		assertEquals("redirect:/inicio", mavDelProy.getViewName());

		ResponseEntity<Object> respUpdProy = controller.updateProyecto(proyecto);
		assertEquals(200, respUpdProy.getStatusCode().value());

		Tareas tarea = new Tareas(1, "T1", "D1", "2026-10-10", "L1", "Activo", 1);
		ResponseEntity<List<Tareas>> respObtenerTareas = controller.obtenerTareas(tarea, model);
		assertEquals(200, respObtenerTareas.getStatusCode().value());

		ModelAndView mavSaveTarea = controller.setSaveTareas(tarea);
		assertEquals("redirect:/proyecto", mavSaveTarea.getViewName());

		ResponseEntity<Object> respUpdTarea = controller.updateTarea(tarea);
		assertEquals(200, respUpdTarea.getStatusCode().value());

		ModelAndView mavDelTarea = controller.eliminarTarea(tarea);
		assertEquals("redirect:/proyecto", mavDelTarea.getViewName());

		ResponseEntity<Integer> respVerifTarea = controller.verificarTarea(tarea);
		assertEquals(200, respVerifTarea.getStatusCode().value());

		Comentarios comentario = new Comentarios(1, "1", 1, "Comentario", "2026-10-10");
		ResponseEntity<List<Comentarios>> respObtenerCom = controller.obtenerComentarios(comentario, model);
		assertEquals(200, respObtenerCom.getStatusCode().value());

		assertEquals("proyecto", controller.setSaveComentario(comentario));
	}

}
