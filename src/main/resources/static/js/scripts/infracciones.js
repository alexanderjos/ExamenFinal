const url = "/v1/infracciones";

function ajaxRequest(type, endpoint, data = null) {
    return $.ajax({
        type,
        url: endpoint,
        data: data ? JSON.stringify(data) : null,
        dataType: "json",
        contentType: data ? "application/json" : undefined,
        cache: false,
        timeout: 600000,
    });
}

function save(bandera) {
    const id = $("#guardar").data("id");
    
    // Convertir la fecha al formato UTC
    const fechaLocal = $("#fecha").val();
    const fechaUTC = new Date(fechaLocal).toISOString();  // Convierte la fecha a formato UTC

    const registro = {
        id,
        dni: $("#dni").val(),
        fecha: fechaUTC, // Enviar la fecha en formato UTC
        placa: $("#placa").val(),
        infracción: $("#infraccion").val(),
        descripcion: $("#descripcion").val(),
    };

    const type = bandera === 1 ? "POST" : "PUT";
    const endpoint = bandera === 1 ? url : `${url}/${id}`;

    ajaxRequest(type, endpoint, registro)
        .done((data) => {
            if (data.ok) {
                $("#modal-update").modal("hide");
                getTabla();
                $("#error-message").addClass("d-none");
                Swal.fire({
                    icon: 'success',
                    title: `Se ha ${bandera === 1 ? 'guardado' : 'actualizado'} la infracción`,
                    showConfirmButton: false,
                    timer: 1500
                });
                clear();
            } else {
                showError(data.message);
            }
        }).fail(function (jqXHR) {
            let errorMessage = jqXHR.responseJSON && jqXHR.responseJSON.message ? jqXHR.responseJSON.message : "Error inesperado. Código: " + jqXHR.status;
            showError(errorMessage);
        });
}


function showError(message) {
    $("#error-message").text(message).removeClass("d-none");
}

function deleteFila(id) {
    ajaxRequest("DELETE", `${url}/${id}`)
        .done((data) => {
            if (data.ok) {
                Swal.fire({
                    icon: 'success',
                    title: 'Se ha eliminado la infracción',
                    showConfirmButton: false,
                    timer: 1500
                });
                getTabla();
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: data.message,
                    confirmButtonText: 'Aceptar'
                });
            }
        })
        .fail(handleError);
}

function getTabla() {
    ajaxRequest("GET", url)
        .done((data) => {
            const t = $("#tablaRegistros").DataTable();
            t.clear().draw(false);

            if (data.ok) {
                $.each(data.body, (index, infraccion) => {
                    const botonera = `
                        <button type="button" class="btn btn-warning btn-sm editar">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button type="button" class="btn btn-danger btn-sm eliminar">
                            <i class="fas fa-trash"></i>
                        </button>`;
                        const fechaFormateada = formatFecha(infraccion.fecha);

                    t.row.add([botonera, infraccion.id, infraccion.dni, fechaFormateada, infraccion.placa, infraccion.infracción, infraccion.descripcion]).draw(false);
                });
                t.draw(false);
            } else {
                console.error("Error en la respuesta: ", data.message);
            }
        })
        .fail(handleError);
}

function getFila(id) {
    ajaxRequest("GET", `${url}/${id}`)
        .done((data) => {
            if (data.ok) {
                $("#modal-title").text("Editar infracción");
                $("#dni").val(data.body.dni);
                const fechaOriginal = data.body.fecha; // Obtén la fecha del JSON
                const fecha = new Date(fechaOriginal);    // Convierte a un objeto Date

                // Restar 5 horas
                fecha.setHours(fecha.getHours() - 5);

                // Formatea la fecha para el input datetime-local (YYYY-MM-DDTHH:MM)
                const formattedDate = fecha.toISOString().slice(0, 16);

                // Asigna la fecha formateada al input
                $("#fecha").val(formattedDate);

                $("#placa").val(data.body.placa);
                $("#infraccion").val(data.body.infracción);
                $("#descripcion").val(data.body.descripcion);
                $("#guardar").data("id", data.body.id).data("bandera", 0);
                $("#modal-update").modal("show");
            } else {
                showError(data.message);
            }
        })
        .fail(handleError);
}

function formatFecha(fecha) {
    if (!fecha) return ''; 
    const date = new Date(fecha);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); 
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    
    return `${year}-${month}-${day} ${hours}:${minutes}`; 
}



function clear() {
    $("#modal-title").text("Nueva infracción");
    $("#dni").val("");
    $("#fecha").val("");
    $("#placa").val("");
    $("#infraccion").val("");
    $("#descripcion").val("");
    $("#guardar").data("id", 0).data("bandera", 1);
}

function handleError(jqXHR) {
    const errorMessage = jqXHR.responseJSON?.message || `Error inesperado. Código: ${jqXHR.status}`;
    Swal.fire({
        icon: 'error',
        title: 'Error',
        text: errorMessage,
        confirmButtonText: 'Aceptar'
    });
}

$(document).ready(function () {
    $("#tablaRegistros").DataTable({
        language: {
            lengthMenu: "Mostrar _MENU_ registros",
            zeroRecords: "No se encontraron coincidencias",
            info: "Mostrando del _START_ al _END_ de _TOTAL_ registros",
            infoEmpty: "Sin resultados",
            search: "Buscar: ",
            paginate: {
                first: "Primero",
                last: "Último",
                next: "Siguiente",
                previous: "Anterior",
            },
        },
        columnDefs: [
            { targets: 0, orderable: false }
        ],
    });

    clear();

    $("#nuevo").click(clear);
    
    $("#guardar").click(() => save($("#guardar").data("bandera")));

    $(document).on('click', '.eliminar', function () {
        const id = $(this).closest('tr').find('td:eq(1)').text();
        Swal.fire({
            title: 'Eliminar infracción',
            text: "¿Está seguro de querer eliminar esta infracción?",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Sí'
        }).then((result) => {
            if (result.isConfirmed) {
                deleteFila(id);
            }
        });
    });

    $(document).on('click', '.editar', function () {
        const id = $(this).closest('tr').find('td:eq(1)').text();
        getFila(id);
    });

    getTabla();
    
    $('#liAlmacen').addClass("menu-open");
    $('#liInfraccion').addClass("active");
});