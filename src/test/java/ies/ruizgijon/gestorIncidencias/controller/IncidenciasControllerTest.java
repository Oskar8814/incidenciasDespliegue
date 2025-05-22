package ies.ruizgijon.gestorIncidencias.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ies.ruizgijon.gestorIncidencias.GConstants;

import ies.ruizgijon.gestorincidencias.controller.IncidenciasController;
import ies.ruizgijon.gestorincidencias.model.EstadoIncidencia;
import ies.ruizgijon.gestorincidencias.model.Incidencia;
import ies.ruizgijon.gestorincidencias.model.Rol;
import ies.ruizgijon.gestorincidencias.model.Usuario;
import ies.ruizgijon.gestorincidencias.service.IIncidenciasService;
import ies.ruizgijon.gestorincidencias.service.INotaService;
import ies.ruizgijon.gestorincidencias.service.IUsuarioService;
import jakarta.servlet.http.HttpServletRequest;

class IncidenciasControllerTest {

    @Mock
    private IIncidenciasService incidenciasService; // Mock del servicio de incidencias
    
    @Mock
    private INotaService notaService; // Mock del servicio de notas

    @Mock
    private IUsuarioService usuarioService; // Mock del servicio de usuarios

    @Mock
    private RedirectAttributes redirectAttributes; // Mock de RedirectAttributes

    @Mock
    private Model model; // Mock del modelo

    @Mock
    private HttpServletRequest request; // Mock del request

    private IncidenciasController incidenciasController; // Instancia del controlador a probar

    @BeforeEach
    void setUp() {
        // Inicializa los mocks
        MockitoAnnotations.openMocks(this);
        // Inicializa el controlador con los mocks
        incidenciasController = new IncidenciasController(incidenciasService, usuarioService, notaService);
    }

    @Test
    void testListarIncidenciasPendientes_conIncidencias() {
        Incidencia incidencia = new Incidencia();
        incidencia.setId(1);
        incidencia.setEstado(EstadoIncidencia.PENDIENTE);

        List<Incidencia> lista = List.of(incidencia);

        when(incidenciasService.buscarPorEstado(EstadoIncidencia.PENDIENTE)).thenReturn(lista);

        String vista = incidenciasController.listarIncidenciasPendientes(model);

        verify(incidenciasService).buscarPorEstado(EstadoIncidencia.PENDIENTE);
        verify(model).addAttribute(GConstants.ATTRIBUTE_INCIDENCIASPENDIENTES, lista);
        verify(model, org.mockito.Mockito.never()).addAttribute(
                org.mockito.ArgumentMatchers.eq(GConstants.ATTRIBUTE_MESSAGENOPENDIENTE),
                org.mockito.ArgumentMatchers.any());

        assertEquals(GConstants.VIEW_INCIDENCIASPENDIENTES, vista);
    }

    @Test
    void testListarIncidenciasPendientes_sinIncidencias() {
        when(incidenciasService.buscarPorEstado(EstadoIncidencia.PENDIENTE)).thenReturn(List.of());

        String vista = incidenciasController.listarIncidenciasPendientes(model);

        verify(incidenciasService).buscarPorEstado(EstadoIncidencia.PENDIENTE);
        verify(model).addAttribute(GConstants.ATTRIBUTE_MESSAGENOPENDIENTE, "No hay incidencias pendientes.");
        verify(model).addAttribute(GConstants.ATTRIBUTE_INCIDENCIASPENDIENTES, List.of());

        assertEquals(GConstants.VIEW_INCIDENCIASPENDIENTES, vista);
    }

    @Test
    void testBuscarIncidenciasPendientes_conResultados() {
        Incidencia busqueda = new Incidencia();
        busqueda.setTitulo("pantalla"); // Título que se busca

        Incidencia resultado = new Incidencia();
        resultado.setId(1);
        resultado.setTitulo("Pantalla rota");
        resultado.setEstado(EstadoIncidencia.PENDIENTE);

        List<Incidencia> resultados = List.of(resultado);

        // Simula que la búsqueda devuelve coincidencias
        when(incidenciasService.buscarByExample(Mockito.any())).thenReturn(resultados);

        String vista = incidenciasController.buscarIncidenciasPendientes(busqueda, model);

        verify(incidenciasService).buscarByExample(Mockito.any());
        verify(model).addAttribute(GConstants.ATTRIBUTE_INCIDENCIASPENDIENTES, resultados);
        verify(model, Mockito.never()).addAttribute(Mockito.eq(GConstants.ATTRIBUTE_MESSAGENOPENDIENTE), Mockito.any());

        assertEquals(GConstants.VIEW_INCIDENCIASPENDIENTES, vista);
    }

    @Test
    void testBuscarIncidenciasPendientes_sinResultados() {
        Incidencia busqueda = new Incidencia();
        busqueda.setTitulo("no-existe");

        when(incidenciasService.buscarByExample(Mockito.any())).thenReturn(List.of());

        String vista = incidenciasController.buscarIncidenciasPendientes(busqueda, model);

        verify(incidenciasService).buscarByExample(Mockito.any());
        verify(model).addAttribute(GConstants.ATTRIBUTE_MESSAGENOPENDIENTE,
                "No hay incidencias pendientes con el título indicado.");
        verify(model).addAttribute(GConstants.ATTRIBUTE_INCIDENCIASPENDIENTES, List.of());

        assertEquals(GConstants.VIEW_INCIDENCIASPENDIENTES, vista);
    }

    @Test
    void testListarIncidenciasProgreso_conIncidencias() {
        Incidencia incidencia = new Incidencia();
        incidencia.setId(1);
        incidencia.setEstado(EstadoIncidencia.REPARACION);

        List<Incidencia> lista = List.of(incidencia);

        when(incidenciasService.buscarPorEstado(EstadoIncidencia.REPARACION)).thenReturn(lista);

        String vista = incidenciasController.listarIncidenciasProgreso(model);

        verify(incidenciasService).buscarPorEstado(EstadoIncidencia.REPARACION);
        verify(model).addAttribute(GConstants.ATTRIBUTE_INCIDENCIASPROGRESO, lista);
        verify(model, never()).addAttribute(eq(GConstants.ATTRIBUTE_MESSAGENOPROGRESO), any());

        assertEquals(GConstants.VIEW_INCIDENCIASPROGRESO, vista);
    }

    @Test
    void testListarIncidenciasProgreso_sinIncidencias() {
        when(incidenciasService.buscarPorEstado(EstadoIncidencia.REPARACION)).thenReturn(List.of());

        String vista = incidenciasController.listarIncidenciasProgreso(model);

        verify(incidenciasService).buscarPorEstado(EstadoIncidencia.REPARACION);
        verify(model).addAttribute(GConstants.ATTRIBUTE_MESSAGENOPROGRESO, "No hay incidencias en progreso.");
        verify(model).addAttribute(GConstants.ATTRIBUTE_INCIDENCIASPROGRESO, List.of());

        assertEquals(GConstants.VIEW_INCIDENCIASPROGRESO, vista);
    }

    @Test
    void testBuscarIncidenciasProgreso_conResultados() {
        Incidencia filtro = new Incidencia();
        filtro.setTitulo("Red");

        Incidencia incidencia = new Incidencia();
        incidencia.setId(1);
        incidencia.setTitulo("Problema Red Aula");
        incidencia.setEstado(EstadoIncidencia.REPARACION);

        List<Incidencia> resultado = List.of(incidencia);

        when(incidenciasService.buscarByExample(any())).thenReturn(resultado);

        String vista = incidenciasController.buscarIncidenciasProgreso(filtro, model);

        verify(model).addAttribute(GConstants.ATTRIBUTE_INCIDENCIASPROGRESO, resultado);
        verify(model, never()).addAttribute(eq(GConstants.ATTRIBUTE_MESSAGENOPROGRESO), any());
        assertEquals(GConstants.VIEW_INCIDENCIASPROGRESO, vista);
    }

    @Test
    void testBuscarIncidenciasProgreso_sinResultados() {
        Incidencia filtro = new Incidencia();
        filtro.setTitulo("Inexistente");

        when(incidenciasService.buscarByExample(any())).thenReturn(List.of());

        String vista = incidenciasController.buscarIncidenciasProgreso(filtro, model);

        verify(model).addAttribute(GConstants.ATTRIBUTE_MESSAGENOPROGRESO,
                "No hay incidencias en reparación con el título y/o encargado indicado.");
        verify(model).addAttribute(GConstants.ATTRIBUTE_INCIDENCIASPROGRESO, List.of());
        assertEquals(GConstants.VIEW_INCIDENCIASPROGRESO, vista);
    }

    @Test
    void testListarIncidenciasResueltas_conIncidencias() {
        // Crear una incidencia resuelta de prueba
        Incidencia incidencia = new Incidencia();
        incidencia.setId(1);
        incidencia.setEstado(EstadoIncidencia.RESUELTA);

        Page<Incidencia> paginaIncidencias = new PageImpl<>(List.of(incidencia)); // Crear una página con una incidencia

        Pageable pageable = PageRequest.of(0, 10); // Configurar la paginación
        when(incidenciasService.buscarIncidenciasPorEstadoPaginadas(EstadoIncidencia.RESUELTA, pageable))
                .thenReturn(paginaIncidencias);

        String vista = incidenciasController.listarIncidenciasResueltas(model, pageable);

        verify(model).addAttribute(GConstants.ATTRIBUTE_INCIDENCIASRESUELTAS, paginaIncidencias);
        verify(model, never()).addAttribute(eq(GConstants.ATTRIBUTE_MESSAGENORESUELTA), any());

        assertEquals(GConstants.VIEW_INCIDENCIASRESUELTAS, vista);
    }

    @Test
    void testListarIncidenciasResueltas_sinIncidencias() {
        Page<Incidencia> paginaVacia = new PageImpl<>(List.of());

        Pageable pageable = PageRequest.of(0, 10);
        when(incidenciasService.buscarIncidenciasPorEstadoPaginadas(EstadoIncidencia.RESUELTA, pageable))
                .thenReturn(paginaVacia);

        String vista = incidenciasController.listarIncidenciasResueltas(model, pageable);

        verify(model).addAttribute(GConstants.ATTRIBUTE_MESSAGENORESUELTA, "No hay incidencias resueltas.");
        verify(model).addAttribute(GConstants.ATTRIBUTE_INCIDENCIASRESUELTAS, paginaVacia);

        assertEquals(GConstants.VIEW_INCIDENCIASRESUELTAS, vista);
    }

    @Test
    void testBuscarIncidenciasResueltas_conResultados() {
        Incidencia busqueda = new Incidencia();
        busqueda.setTitulo("Red");

        Incidencia incidencia = new Incidencia();
        incidencia.setId(1);
        incidencia.setTitulo("Incidencia en la red resuelta");
        incidencia.setEstado(EstadoIncidencia.RESUELTA);

        Page<Incidencia> paginaIncidencias = new PageImpl<>(List.of(incidencia));

        Pageable pageable = PageRequest.of(0, 10);
        when(incidenciasService.buscarIncidenciasByExamplePaginadas(any(), eq(pageable)))
                .thenReturn(paginaIncidencias);

        String vista = incidenciasController.buscarIncidenciasResueltas(busqueda, model, pageable);

        verify(model).addAttribute(GConstants.ATTRIBUTE_INCIDENCIASRESUELTAS, paginaIncidencias);
        verify(model, never()).addAttribute(eq(GConstants.ATTRIBUTE_MESSAGENORESUELTA), any());

        assertEquals(GConstants.VIEW_INCIDENCIASRESUELTAS, vista);
    }

    @Test
    void testBuscarIncidenciasResueltas_sinResultados() {
        Incidencia busqueda = new Incidencia();
        busqueda.setTitulo("Inexistente");

        Page<Incidencia> paginaVacia = new PageImpl<>(List.of());

        Pageable pageable = PageRequest.of(0, 10);
        when(incidenciasService.buscarIncidenciasByExamplePaginadas(any(), eq(pageable)))
                .thenReturn(paginaVacia);

        String vista = incidenciasController.buscarIncidenciasResueltas(busqueda, model, pageable);

        verify(model).addAttribute(GConstants.ATTRIBUTE_MESSAGENORESUELTA,
                "No hay incidencias resueltas con el título indicado.");
        verify(model).addAttribute(GConstants.ATTRIBUTE_INCIDENCIASRESUELTAS, paginaVacia);

        assertEquals(GConstants.VIEW_INCIDENCIASRESUELTAS, vista);
    }

    @Test
    void testCrearIncidencia() {
        // Simular un modelo
        String vista = incidenciasController.crearIncidencia(model);

        // Verificar que el modelo contenga un atributo 'incidencia' que es una nueva instancia de Incidencia
        verify(model).addAttribute(GConstants.ATTRIBUTE_INCIDENCIA, new Incidencia());

        // Verificar que la vista correcta es devuelta
        assertEquals("crearIncidenciaForm", vista);
    }

    @Test
    void testGuardarIncidencia_nuevaIncidencia() {
        // Crear un usuario de prueba
        Rol rol = new Rol(); // Simula un rol de prueba
        rol.setId(1); // Establece un ID de rol de prueba
        rol.setName("ADMIN"); // Establece un nombre de rol de prueba
        Usuario usuario = new Usuario(); // Simula un usuario de prueba
        usuario.setId(1); // Establece un ID de usuario de prueba
        usuario.setNombre("Usuario"); // Establece un nombre de usuario de prueba
        usuario.setApellido1("Prueba"); // Establece apellidos de usuario de prueba
        usuario.setMail("prueba.prueba@example.com"); // Establece un email de usuario de prueba
        usuario.setPassword("Password123456@"); // Establece una contraseña de usuario de prueba
        usuario.setRol(rol); // Establece el rol del usuario de prueba
        
        // Crear una incidencia de prueba
        int id = 1; // ID de la incidencia a crear
        Incidencia incidencia = new Incidencia(); // Simula la búsqueda de la incidencia por ID
        incidencia.setId(id); // Establece el ID de la incidencia
        incidencia.setTitulo("Título de prueba"); // Establece un título de prueba	
        incidencia.setDescripcion("Descripción de prueba"); // Establece una descripción de prueba
        incidencia.setAula("123"); // Establece un aula de prueba
        incidencia.setFecha(LocalDate.now()); // Establece una fecha de prueba
        incidencia.setImagen("imagen.jpg"); // Establece una imagen de prueba
        incidencia.setUsuario(null); // Establece el usuario de prueba
        incidencia.setCreador(usuario); // Establece el creador de la incidencia de prueba
        incidencia.setEstado(EstadoIncidencia.PENDIENTE); // Establece el estado de la incidencia de prueba
        

        // Llamar al método a probar
        String vista = incidenciasController.guardarIncidencia(incidencia, redirectAttributes);
        
        // Verificar que el método 'guardarIncidencia' del servicio fue llamado con la incidencia correcta
        verify(incidenciasService).guardarIncidencia(incidencia);
        // Verificar que el mensaje de confirmación es agregado a los atributos de la redirección
        verify(redirectAttributes).addFlashAttribute(GConstants.ATTRIBUTE_CONFIRMACION,
                "Incidencia creada o modificada con éxito.");

        // Verificar que se redirige correctamente
        assertEquals("redirect:/incidencias/index", vista);
    }

    @Test
    void testGuardarIncidencia_modificada() {
        // Crear una incidencia de prueba
        Incidencia incidencia = new Incidencia();
        incidencia.setId(1); // Suponiendo que ya existe una incidencia con este ID
        incidencia.setTitulo("Incidencia modificada");

        // Llamar al método a probar
        String vista = incidenciasController.guardarIncidencia(incidencia, redirectAttributes);
        
        // Verificar que el método 'guardarIncidencia' del servicio fue llamado con la incidencia correcta
        verify(incidenciasService).guardarIncidencia(incidencia);
        // Verificar que el mensaje de confirmación es agregado a los atributos de la redirección
        verify(redirectAttributes).addFlashAttribute(GConstants.ATTRIBUTE_CONFIRMACION,
                "Incidencia creada o modificada con éxito.");

        // Verificar que se redirige correctamente
        assertEquals("redirect:/incidencias/index", vista);
    }

    @Test
    void testCambiarEstadoIncidenciaProgreso() {
        // Simular la incidencia con un ID
        int idIncidencia = 1;

        // Simular el usuario actual
        Usuario usuario = new Usuario();
        usuario.setId(1); // Establecer un ID de usuario de ejemplo

        // Simular el servicio de usuario
        when(usuarioService.getCurrentUser()).thenReturn(usuario);

        // Llamar al método que estamos probando
        String result = incidenciasController.cambiarEstadoIncidenciaProgreso(idIncidencia, redirectAttributes);

        // Verificar que el servicio 'asignarIncidencia' fue llamado con los parámetroscorrectos
        verify(incidenciasService).asignarIncidencia(idIncidencia, usuario.getId());

        // Verificar que se agregó el mensaje de confirmación al 'RedirectAttributes'
        verify(redirectAttributes).addFlashAttribute(GConstants.ATTRIBUTE_CONFIRMACION,
                "Incidencia " + idIncidencia + " en progreso.");

        // Verificar que el redireccionamiento es hacia la lista de incidencias en progreso
        assertEquals("redirect:/incidencias/incidenciasProgreso", result);
    }

    @Test
    void testDesasignarIncidencia() {
        // Simular la incidencia con un ID
        int idIncidencia = 1;

        // Llamar al método que estamos probando
        String result = incidenciasController.desasignarIncidencia(idIncidencia, redirectAttributes);

        // Verificar que el servicio 'desasignarIncidencia' fue llamado con el ID correcto
        verify(incidenciasService).desasignarIncidencia(idIncidencia);

        // Verificar que se agregó el mensaje de confirmación al 'RedirectAttributes'
        verify(redirectAttributes).addFlashAttribute(GConstants.ATTRIBUTE_CONFIRMACION,
                "Incidencia " + idIncidencia + " desasignada.");

        // Verificar que el redireccionamiento es hacia la lista de incidencias
        assertEquals("redirect:/incidencias/index", result);
    }

    @Test
    void testCambiarEstadoIncidenciaResuelta() {
        // Simular la incidencia con un ID
        int idIncidencia = 1;

        // Llamar al método que estamos probando
        String result = incidenciasController.cambiarEstadoIncidenciaResuelta(idIncidencia, redirectAttributes);

        // Verificar que el servicio 'cerrarIncidencia' fue llamado con el ID correcto
        verify(incidenciasService).cerrarIncidencia(idIncidencia);

        // Verificar que se agregó el mensaje de confirmación al 'RedirectAttributes'
        verify(redirectAttributes).addFlashAttribute(GConstants.ATTRIBUTE_CONFIRMACION, "Incidencia  " + idIncidencia + "  resuelta.");

        // Verificar que el redireccionamiento es hacia la lista de incidencias resueltas
        assertEquals("redirect:/incidencias/incidenciasResueltas", result);
    }


    @Test
    void testSetGenericos() {
        // Crear datos simulados
        // Crear un usuario de prueba
        Rol rol = new Rol(); // Simula un rol de prueba
        rol.setId(1); // Establece un ID de rol de prueba
        rol.setName("ADMIN"); // Establece un nombre de rol de prueba
        Usuario usuarioLogueado = new Usuario(); // Simula un usuario de prueba
        usuarioLogueado.setId(1); // Establece un ID de usuario de prueba
        usuarioLogueado.setNombre("Usuario"); // Establece un nombre de usuario de prueba
        usuarioLogueado.setApellido1("Prueba"); // Establece apellidos de usuario de prueba
        usuarioLogueado.setMail("prueba.prueba@example.com"); // Establece un email de usuario de prueba
        usuarioLogueado.setPassword("Password123456@"); // Establece una contraseña de usuario de prueba
        usuarioLogueado.setRol(rol); // Establece el rol del usuario de prueba


        List<Usuario> listaUsuarios = Arrays.asList(
                new Usuario(2, "usuario2","apellido1", "apellido2","prueba2.prueba@example.com","Password123456@",rol),
                new Usuario(3, "usuario3","apellido3", "apellido3","prueba3.prueba@example.com","Password123556@",rol)
        );
        
        // Configurar los mocks para que devuelvan los datos simulados
        when(usuarioService.getCurrentUser()).thenReturn(usuarioLogueado);
        when(usuarioService.buscarTodos()).thenReturn(listaUsuarios);

        // Llamar al método que estamos probando
        incidenciasController.setGenericos(model, request);

        // Verificar que el modelo ha sido actualizado correctamente
        verify(model).addAttribute(GConstants.ATTRIBUTE_CURRENTUSER, usuarioLogueado); // Verificar que el usuario logueado se haya agregado al modelo
        verify(model).addAttribute("search", new Incidencia()); // Verificar que el objeto "search" se haya agregado al modelo
        verify(model).addAttribute(GConstants.ATTRIBUTE_USUARIOS, listaUsuarios); // Verificar que la lista de usuarios se haya agregado al modelo
    }
}
