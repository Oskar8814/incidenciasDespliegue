package ies.ruizgijon.gestorIncidencias.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ies.ruizgijon.gestorIncidencias.GConstants;
import ies.ruizgijon.gestorincidencias.controller.UsuarioController;
import ies.ruizgijon.gestorincidencias.model.Rol;
import ies.ruizgijon.gestorincidencias.model.Usuario;
import ies.ruizgijon.gestorincidencias.service.IRolService;
import ies.ruizgijon.gestorincidencias.service.IUsuarioService;
import jakarta.servlet.http.HttpServletRequest;

class UsuarioControllerTest {

    @Mock
    private IRolService rolService; // Mock del servicio de roles

    @Mock
    private IUsuarioService usuarioService; // Mock del servicio de usuarios

    @Mock
    private RedirectAttributes redirectAttributes; // Mock de RedirectAttributes

    @Mock
    private Model model; // Mock del modelo

    @Mock
    private HttpServletRequest request; // Mock del request

    private UsuarioController usuarioController; // Instancia del controlador a probar

    @BeforeEach
    void setUp() {
        // Inicializa los mocks
        MockitoAnnotations.openMocks(this);
        // Inicializa el controlador con los mocks
        usuarioController = new UsuarioController(usuarioService, rolService);
    }

    @Test
    void testListarUsuarios_conUsuarios() {
        // Simula una lista de usuarios con un usuario de prueba
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Juan");

        List<Usuario> usuarios = List.of(usuario);

        // Simula el comportamiento del servicio
        when(usuarioService.buscarTodos()).thenReturn(usuarios);

        // Llama al método a probar
        String vista = usuarioController.listarUsuarios(model);

        // Verifica que se añade la lista de usuarios al modelo
        verify(model).addAttribute(GConstants.ATTRIBUTE_USUARIOS, usuarios);

        // Verifica que no se añade el mensaje de lista vacía
        verify(model, org.mockito.Mockito.never()).addAttribute(org.mockito.Mockito.eq("mensajeNoUsuarios"),
                org.mockito.Mockito.any());

        // Verifica la vista devuelta
        assertEquals("usuarios", vista);
    }

    @Test
    void testListarUsuarios_sinUsuarios() {
        // Simula una lista vacía
        when(usuarioService.buscarTodos()).thenReturn(List.of());

        // Llama al método a probar
        String vista = usuarioController.listarUsuarios(model);

        // Verifica que se añade el mensaje de lista vacía
        verify(model).addAttribute("mensajeNoUsuarios", "No hay usuarios registrados.");

        // Verifica que se añade la lista vacía también al modelo
        verify(model).addAttribute(GConstants.ATTRIBUTE_USUARIOS, List.of());

        // Verifica la vista devuelta
        assertEquals("usuarios", vista);
    }

    @Test
    void testEliminarUsuario_existente() {
        int idUsuario = 1;

        // Simula que el usuario existe
        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);
        when(usuarioService.buscarUsuarioPorId(idUsuario)).thenReturn(usuario);

        // Llama al método a probar
        String vista = usuarioController.eliminarUsuario(idUsuario, model, redirectAttributes);

        // Verifica que se llama al servicio de eliminación
        verify(usuarioService).eliminarUsuario(idUsuario);

        // Verifica que se añade el mensaje de éxito a los atributos flash
        verify(redirectAttributes).addFlashAttribute(GConstants.ATTRIBUTE_CONFIRMACION, "Usuario eliminado con éxito.");

        // Verifica la vista redirigida
        assertEquals(GConstants.REDIRECT_USUARIOLISTAR, vista);
    }

    @Test
    void testEliminarUsuario_noExistente() {
        int idUsuario = 99;

        // Simula que el usuario no existe
        when(usuarioService.buscarUsuarioPorId(idUsuario)).thenReturn(null);

        // Llama al método a probar
        String vista = usuarioController.eliminarUsuario(idUsuario, model, redirectAttributes);

        // Verifica que se añade el mensaje de error al modelo
        verify(model).addAttribute(GConstants.ATTRIBUTE_MESSAGEERROR, "Usuario no encontrado con ID: " + idUsuario);

        // Verifica que **no** se intenta eliminar un usuario
        verify(usuarioService, org.mockito.Mockito.never()).eliminarUsuario(idUsuario);

        // Verifica la vista redirigida
        assertEquals(GConstants.REDIRECT_USUARIOLISTAR, vista);
    }

    @Test
    void testCrearUsuario() {
        // Simula una lista de roles
        Rol rol = new Rol();
        rol.setId(1);
        rol.setName("ADMIN");

        List<Rol> roles = List.of(rol);

        // Simula el comportamiento del servicio
        when(rolService.buscarTodos()).thenReturn(roles);

        // Llama al método a probar
        String vista = usuarioController.crearUsuario(model);

        // Verifica que se añade una nueva instancia de Usuario al modelo
        verify(model).addAttribute("usuario", new Usuario());

        // Verifica que se añade la lista de roles al modelo
        verify(model).addAttribute(GConstants.ATTRIBUTE_ROLES, roles);

        // Verifica que se devuelve la vista correcta
        assertEquals("crearUsuarioForm", vista);
    }

    @Test
    void testGuardarUsuario_usuarioExistente() {
        // Simula un usuario que ya existe
        Usuario usuario = new Usuario();
        usuario.setMail("test@example.com");

        when(usuarioService.buscarUsuarioPorMail(usuario.getMail())).thenReturn(usuario);

        // Llama al método a probar
        String vista = usuarioController.guardarUsuario(usuario, model, redirectAttributes);

        // Verifica que se llama a modificarUsuario
        verify(usuarioService).modificarUsuario(usuario);

        // Verifica que se agrega el mensaje de modificación
        verify(redirectAttributes).addFlashAttribute(GConstants.ATTRIBUTE_CONFIRMACION,
                "Usuario modificado con éxito.");

        // Verifica la redirección
        assertEquals(GConstants.REDIRECT_USUARIOLISTAR, vista);
    }

    @Test
    void testGuardarUsuario_nuevoUsuario() {
        // Simula un nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setMail("nuevo@example.com");

        // Simula que no existe en la base de datos
        when(usuarioService.buscarUsuarioPorMail(usuario.getMail())).thenReturn(null);

        // Llama al método a probar
        String vista = usuarioController.guardarUsuario(usuario, model, redirectAttributes);

        // Verifica que se llama a guardarUsuario
        verify(usuarioService).guardarUsuario(usuario);

        // Verifica que se agrega el mensaje de guardado
        verify(redirectAttributes).addFlashAttribute(GConstants.ATTRIBUTE_CONFIRMACION, "Usuario guardado con éxito.");

        // Verifica la redirección
        assertEquals(GConstants.REDIRECT_USUARIOLISTAR, vista);
    }

    @Test
    void testEditarUsuario_existente() {
        int idUsuario = 1;

        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);

        Rol rol = new Rol();
        rol.setId(1);
        rol.setName("ADMIN");

        List<Rol> roles = List.of(rol);

        // Simula comportamiento del servicio
        when(usuarioService.buscarUsuarioPorId(idUsuario)).thenReturn(usuario);
        when(rolService.buscarTodos()).thenReturn(roles);

        // Llama al método a probar
        String vista = usuarioController.editarUsuario(idUsuario, model);

        // Verifica que se obtienen los datos correctamente
        verify(usuarioService).buscarUsuarioPorId(idUsuario);
        verify(rolService).buscarTodos();

        // Verifica que se añade el usuario y los roles al modelo
        verify(model).addAttribute("usuario", usuario);
        verify(model).addAttribute(GConstants.ATTRIBUTE_ROLES, roles);

        // Verifica la vista correcta
        assertEquals("editUsuarioForm", vista);
    }

    @Test
    void testEditarUsuario_noExistente() {
        int idUsuario = 999;

        when(usuarioService.buscarUsuarioPorId(idUsuario)).thenReturn(null);

        // Llama al método a probar
        String vista = usuarioController.editarUsuario(idUsuario, model);

        // Verifica que se añade el mensaje de error al modelo
        verify(model).addAttribute(GConstants.ATTRIBUTE_MESSAGEERROR, "Usuario no encontrado con ID: " + idUsuario);

        // Verifica que no se llama a buscar roles
        verify(rolService, org.mockito.Mockito.never()).buscarTodos();

        // Verifica que se redirige a la lista de usuarios
        assertEquals(GConstants.REDIRECT_USUARIOLISTAR, vista);
    }

    @Test
    void testSetGenericos() {
        // Llama al método a probar
        usuarioController.setGenericos(model, request);

        Usuario usuario = usuarioService.getCurrentUser(); // Simula la obtención del usuario actual

        // Verifica que se agregó el usuario actual al modelo
        verify(model).addAttribute(GConstants.ATTRIBUTE_CURRENTUSER, usuario); // Verifica que se agregó el usuario actual al modelo
    }
}
