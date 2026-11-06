package com.mx.cbtis.gestor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.mx.cbtis.gestor.controller.AsignacionesController;
import com.mx.cbtis.gestor.controller.ComentariosController;
import com.mx.cbtis.gestor.controller.ProyectosController;
import com.mx.cbtis.gestor.controller.TareasController;
import com.mx.cbtis.gestor.controller.UsuarioController;
import com.mx.cbtis.gestor.modelo.Asignaciones;
import com.mx.cbtis.gestor.modelo.Comentarios;
import com.mx.cbtis.gestor.modelo.Proyectos;
import com.mx.cbtis.gestor.modelo.Tareas;
import com.mx.cbtis.gestor.modelo.Usuario;
import com.mx.cbtis.gestor.repository.AsignacionesRepository;
import com.mx.cbtis.gestor.repository.ComentariosRepository;
import com.mx.cbtis.gestor.repository.ProtectosRepository;
import com.mx.cbtis.gestor.repository.TareasRepository;
import com.mx.cbtis.gestor.repository.UsuarioRepository;

class Ejercicio5MyApiGestorApplicationTests {

	@Test
	void contextLoads() {
		Ejercicio5MyApiGestorApplication app = new Ejercicio5MyApiGestorApplication();
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
	void testAsignacionesController() {
		AsignacionesRepository repo = mock(AsignacionesRepository.class);
		AsignacionesController ctrl = new AsignacionesController();
		ReflectionTestUtils.setField(ctrl, "asignacionestable", repo);

		Asignaciones a = new Asignaciones(1, 1, 1, "2026-10-20");
		when(repo.findById(1)).thenReturn(Optional.of(a));
		when(repo.findById(2)).thenReturn(Optional.empty());
		when(repo.findAll()).thenReturn(Collections.singletonList(a));

		assertEquals(1, ctrl.getAsignacion(1).getId_asignacion());
		assertNotNull(ctrl.getAsignacion(2));
		assertEquals(1, ctrl.addAsignacion(a));
		assertEquals(0, ctrl.addAsignacion(null));
		assertEquals(1, ctrl.updateAsignacion(a));
		assertEquals(0, ctrl.updateAsignacion(null));
	}

	@Test
	void testComentariosController() {
		ComentariosRepository repo = mock(ComentariosRepository.class);
		ComentariosController ctrl = new ComentariosController();
		ReflectionTestUtils.setField(ctrl, "comentariostable", repo);

		Comentarios c = new Comentarios(1, "1", 10, "Hola", "2026-10-20");
		when(repo.findByTarea(10)).thenReturn(Collections.singletonList(c));
		when(repo.findById(1)).thenReturn(Optional.of(c));
		when(repo.findById(2)).thenReturn(Optional.empty());
		when(repo.findAll()).thenReturn(Collections.singletonList(c));

		assertEquals(1, ctrl.getComentario(c).size());
		assertEquals(1, ctrl.getComentario(1).getId_comentario());
		assertNotNull(ctrl.getComentario(2));
		assertEquals(1, ctrl.addComentario(c));
		assertEquals(0, ctrl.addComentario(null));
		assertEquals(1, ctrl.updateComentario(c));
		assertEquals(0, ctrl.updateComentario(null));
	}

	@Test
	void testProyectosController() {
		ProtectosRepository repo = mock(ProtectosRepository.class);
		ProyectosController ctrl = new ProyectosController();
		ReflectionTestUtils.setField(ctrl, "proyectostable", repo);

		Proyectos p = new Proyectos(1, "P1", "D1", "2026-01-01", "2026-12-31", "Activo", "bg.png");
		when(repo.findById(1)).thenReturn(Optional.of(p));
		when(repo.findById(2)).thenReturn(Optional.empty());
		when(repo.findAll()).thenReturn(Collections.singletonList(p));

		assertEquals(1, ctrl.getProyecto(1).getId_proyecto());
		assertNotNull(ctrl.getProyecto(2));
		assertEquals(1, ctrl.addProyecto(p));
		assertEquals(0, ctrl.addProyecto(null));
		assertEquals(1, ctrl.updateProyecto(p));
		assertEquals(0, ctrl.updateProyecto(null));
		assertEquals(1, ctrl.deleteProyecto(p));
		assertEquals(0, ctrl.deleteProyecto(null));
		assertEquals(1, ctrl.getProyectos().size());
	}

	@Test
	void testTareasController() {
		TareasRepository repo = mock(TareasRepository.class);
		TareasController ctrl = new TareasController();
		ReflectionTestUtils.setField(ctrl, "tareastable", repo);

		Tareas t = new Tareas(1, "T1", "D1", "2026-05-01", "L1", "Activo", 10);
		when(repo.findByIdProyecto(10)).thenReturn(Collections.singletonList(t));
		when(repo.findById(1)).thenReturn(Optional.of(t));
		when(repo.findById(2)).thenReturn(Optional.empty());
		when(repo.findAll()).thenReturn(Collections.singletonList(t));

		assertEquals(1, ctrl.getTarea(t).size());
		assertEquals(1, ctrl.getTarea(1).getId_tarea());
		assertNotNull(ctrl.getTarea(2));
		assertEquals(1, ctrl.addTareas(t));
		assertEquals(0, ctrl.addTareas(null));
		assertEquals(1, ctrl.updateTareas(t));
		assertEquals(0, ctrl.updateTareas(null));
		assertEquals(1, ctrl.deleteTareas(t));
		assertEquals(0, ctrl.deleteTareas(null));
		assertEquals(1, ctrl.getTareas().size());
	}

	@Test
	void testUsuarioController() {
		UsuarioRepository repo = mock(UsuarioRepository.class);
		UsuarioController ctrl = new UsuarioController();
		ReflectionTestUtils.setField(ctrl, "usuariotable", repo);

		Usuario u = new Usuario(1, "Juan", "juan@mail.com", "1234", "ADMIN", "img.png");
		when(repo.findByCorreoPass("juan@mail.com", "1234")).thenReturn(Optional.of(u));
		when(repo.findByCorreoPass("otro@mail.com", "0000")).thenReturn(Optional.empty());
		when(repo.findById(1)).thenReturn(Optional.of(u));
		when(repo.findById(2)).thenReturn(Optional.empty());
		when(repo.findAll()).thenReturn(Collections.singletonList(u));

		assertEquals("Juan", ctrl.getUsuarioLogin(u).getNombre());
		Usuario uFail = new Usuario(2, "X", "otro@mail.com", "0000", "USER", "img.png");
		assertEquals("000", ctrl.getUsuarioLogin(uFail).getNombre());
		assertEquals(1, ctrl.getUsuario(1).getId_usuario());
		assertNotNull(ctrl.getUsuario(2));
		assertEquals(1, ctrl.addUsuario(u));
		assertEquals(0, ctrl.addUsuario(null));
		assertEquals(1, ctrl.updateUsuario(u));
		assertEquals(0, ctrl.updateUsuario(null));
		assertEquals(1, ctrl.deleteUsuario(u));
		assertEquals(0, ctrl.deleteUsuario(null));
		assertEquals(1, ctrl.getUsuarios().size());
	}

}
