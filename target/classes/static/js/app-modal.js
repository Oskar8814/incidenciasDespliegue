//Modal imagenes 
// Se utiliza para mostrar la imagen, titulo y descripcion de la incidencia en un modal al hacer clic en el enlace correspondiente
const modalImagen = document.querySelector('#modal-imagen');
    
modalImagen.addEventListener('show.bs.modal', function(event) {
    const enlace = event.relatedTarget; // Elemento que disparó el modal

    const rutaImagen = enlace.getAttribute('data-bs-imagen'); // Obtener la ruta de la imagen
    const descripcionIncidencia = enlace.getAttribute('data-bs-descripcion'); // Obtener la descripción de la incidencia
    const tituloIncidencia = enlace.closest('tr').querySelector('td:nth-child(2)').textContent; // Obtener el título de la incidencia

    // Crear la imagen
    const imagen = document.createElement('IMG');
    imagen.src = `${rutaImagen}`; // Asignar la ruta de la imagen
    imagen.classList.add('img-fluid');
    imagen.classList.add('img-custom-m');
    imagen.alt = 'Imagen incidencia';

    // Obtener elementos del modal
    const contenidoModal = document.querySelector('.modal-body');
    const tituloModal = document.querySelector('#tittleModal');
    const descripcionModal = document.querySelector('#descripcionModal');

    // Limpiar el contenido anterior
    contenidoModal.textContent = '';

    // Asignar el título y la descripcion al modal
    tituloModal.textContent = tituloIncidencia;
    descripcionModal.textContent = descripcionIncidencia;
    // Agregar la imagen al modal
    contenidoModal.appendChild(imagen);
});


// Para cerrar el modal y limpiar contenido
modalImagen.addEventListener('hidden.bs.modal', function() {
    document.querySelector('.modal-body').textContent = '';
    document.querySelector('#tittleModal').textContent = '';
});