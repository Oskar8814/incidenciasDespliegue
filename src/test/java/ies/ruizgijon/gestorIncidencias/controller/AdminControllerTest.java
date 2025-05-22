package ies.ruizgijon.gestorIncidencias.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ies.ruizgijon.gestorIncidencias.GConstants;
import ies.ruizgijon.gestorincidencias.controller.AdminController;
import ies.ruizgijon.gestorincidencias.model.EstadoIncidencia;
import ies.ruizgijon.gestorincidencias.model.Incidencia;
import ies.ruizgijon.gestorincidencias.model.Rol;
import ies.ruizgijon.gestorincidencias.model.Usuario;
import ies.ruizgijon.gestorincidencias.service.IIncidenciasService;
import ies.ruizgijon.gestorincidencias.service.IUsuarioService;
import jakarta.servlet.http.HttpServletRequest;

class AdminControllerTest {

    @Mock
    private IIncidenciasService incidenciasService; // Mock del servicio de incidencias

    @Mock
    private IUsuarioService usuarioService; // Mock del servicio de usuarios

    @Mock
    private RedirectAttributes redirectAttributes; // Mock de RedirectAttributes

    @Mock
    private HttpServletRequest request;

    @Mock
    private Model model; // Mock del modelo
    
    private AdminController adminController; // Instancia del controlador a probar

    @BeforeEach
    void setUp() {
        // Inicializa los mocks
        MockitoAnnotations.openMocks(this);
        // Inicializa el controlador con los mocks
        adminController = new AdminController(incidenciasService, usuarioService);
    }

    @Test
    void testEliminarIncidencia() {
        int id = 1; // ID de la incidencia a eliminar

        // Llama al método a probar
        String result = adminController.eliminarIncidencia(id, redirectAttributes);
        
        // Verifica que el método del servicio fue llamado con el ID correcto
        verify(incidenciasService).eliminarIncidencia(id);
        // Verifica que se agregó el mensaje de confirmación a los atributos de redirección
        verify(redirectAttributes).addFlashAttribute(GConstants.ATTRIBUTE_CONFIRMACION, "Incidencia "+ id +" eliminada con éxito.");
        
        // Verifica que se redirige a la URL correcta
        assertEquals("redirect:/incidencias/index", result); // Verifica la redirección
    }
    
    @Test
    void testEditarIncidencia() {
        int id = 1; // ID de la incidencia a editar
        
        Rol rol = new Rol(); // Simula un rol de prueba
        rol.setId(1); // Establece un ID de rol de prueba
        rol.setName("ADMIN"); // Establece un nombre de rol de prueba

        // Crea un usuario de prueba
        Usuario usuario = new Usuario(); // Simula un usuario de prueba
        usuario.setId(1); // Establece un ID de usuario de prueba
        usuario.setNombre("Usuario"); // Establece un nombre de usuario de prueba
        usuario.setApellido1("Prueba"); // Establece apellidos de usuario de prueba
        usuario.setMail("usuario.prueba@exameple.com"); // Establece un email de usuario de prueba
        usuario.setPassword("Password123456@"); // Establece una contraseña de usuario de prueba
        usuario.setRol(rol); // Establece el rol del usuario de prueba

        // Crea una incidencia de prueba
        Incidencia incidencia = new Incidencia(); // Simula la búsqueda de la incidencia por ID
        incidencia.setId(id); // Establece el ID de la incidencia
        incidencia.setTitulo("Título de prueba"); // Establece un título de prueba	
        incidencia.setDescripcion("Descripción de prueba"); // Establece una descripción de prueba
        incidencia.setAula("123"); // Establece un aula de prueba
        incidencia.setFecha(LocalDate.now()); // Establece una fecha de prueba
        incidencia.setImagen("imagen.jpg"); // Establece una imagen de prueba
        incidencia.setUsuario(usuario); // Establece el usuario de prueba
        incidencia.setCreador(usuario); // Establece el creador de la incidencia de prueba
        incidencia.setEstado(EstadoIncidencia.PENDIENTE); // Establece el estado de la incidencia de prueba
        
        // Simula el comportamiento del servicio al buscar la incidencia por ID
        when(incidenciasService.buscarIncidenciaPorId(id)).thenReturn(incidencia);

        // Simula el comportamiento del servicio al buscar todos los usuarios
        when(usuarioService.buscarTodos()).thenReturn(List.of(usuario)); // Simula la lista de usuarios

        // Llama al método a probar
        String result = adminController.editarIncidencia(id, model);
        
        // Verifica que el método del servicio fue llamado con el ID correcto
        verify(incidenciasService).buscarIncidenciaPorId(id);
        
        // Verifica que se agregó la incidencia al modelo
        verify(model).addAttribute(GConstants.ATTRIBUTE_INCIDENCIA, incidenciasService.buscarIncidenciaPorId(id));
        // Verifica que se agregó la lista de usuarios al modelo
        verify(model).addAttribute(GConstants.ATTRIBUTE_USUARIOS, usuarioService.buscarTodos());

        // Verifica que se redirige a la vista correcta
        assertEquals("crearIncidenciaForm", result); // Verifica la redirección
    }

    @Test
    void testEditarIncidenciaProgreso() {
        int id = 1; // ID de la incidencia a editar
        
        Rol rol = new Rol(); // Simula un rol de prueba
        rol.setId(1); // Establece un ID de rol de prueba
        rol.setName("ADMIN"); // Establece un nombre de rol de prueba

        // Crea un usuario de prueba
        Usuario usuario = new Usuario(); // Simula un usuario de prueba
        usuario.setId(1); // Establece un ID de usuario de prueba
        usuario.setNombre("Usuario"); // Establece un nombre de usuario de prueba
        usuario.setApellido1("Prueba"); // Establece apellidos de usuario de prueba
        usuario.setMail("usuario.prueba@exameple.com"); // Establece un email de usuario de prueba
        usuario.setPassword("Password123456@"); // Establece una contraseña de usuario de prueba
        usuario.setRol(rol); // Establece el rol del usuario de prueba

        // Crea una incidencia de prueba
        Incidencia incidencia = new Incidencia(); // Simula la búsqueda de la incidencia por ID
        incidencia.setId(id); // Establece el ID de la incidencia
        incidencia.setTitulo("Título de prueba"); // Establece un título de prueba	
        incidencia.setDescripcion("Descripción de prueba"); // Establece una descripción de prueba
        incidencia.setAula("123"); // Establece un aula de prueba
        incidencia.setFecha(LocalDate.now()); // Establece una fecha de prueba
        incidencia.setImagen("imagen.jpg"); // Establece una imagen de prueba
        incidencia.setUsuario(usuario); // Establece el usuario de prueba
        incidencia.setCreador(usuario); // Establece el creador de la incidencia de prueba
        incidencia.setEstado(EstadoIncidencia.PENDIENTE); // Establece el estado de la incidencia de prueba

        // Simula el comportamiento del servicio al buscar la incidencia por ID
        when(incidenciasService.buscarIncidenciaPorId(id)).thenReturn(incidencia);

        // Simula el comportamiento del servicio al buscar todos los usuarios
        when(usuarioService.buscarTodos()).thenReturn(List.of(usuario)); // Simula la lista de usuarios

        // Llama al método a probar
        String result = adminController.editarIncidenciaProgreso(id, model);

        // Verifica que el método del servicio fue llamado con el ID correcto
        verify(incidenciasService).buscarIncidenciaPorId(id);
        
        // Verifica que se agregó la incidencia al modelo
        verify(model).addAttribute(GConstants.ATTRIBUTE_INCIDENCIA, incidenciasService.buscarIncidenciaPorId(id));
        // Verifica que se agregó la lista de usuarios al modelo
        verify(model).addAttribute(GConstants.ATTRIBUTE_USUARIOS, usuarioService.buscarTodos());

        // Verifica que se redirige a la vista correcta
        assertEquals("editarIncidenciaForm", result); // Verifica la redirección
    }

    @Test
    void testEditarIncidenciaPost() {
        // Crea una incidencia de prueba
        Incidencia incidencia = new Incidencia();
        // Llama al método a probar
        String result = adminController.editarIncidencia(incidencia, redirectAttributes);
        
        // Verifica que el método del servicio fue llamado para guardar la incidencia editada
        verify(incidenciasService).guardarIncidencia(incidencia);
        
        // Verifica que se agregó el mensaje de confirmación a los atributos de redirección
        verify(redirectAttributes).addFlashAttribute(GConstants.ATTRIBUTE_CONFIRMACION, "Incidencia editada con éxito.");

        // Verifica que se redirige a la URL correcta
        assertEquals("redirect:/incidencias/index", result); // Verifica la redirección
    }
    
    @Test
    void testSetGenericos(){
        // Llama al método a probar
        adminController.setGenericos(model, request);
        
        Usuario usuario = usuarioService.getCurrentUser(); //Simula la obtención del usuario actual 

        // Verifica que se agregó el usuario actual al modelo
        verify(model).addAttribute(GConstants.ATTRIBUTE_CURRENTUSER, usuario); // Verifica que se agregó el usuario actual al modelo
    }
}
