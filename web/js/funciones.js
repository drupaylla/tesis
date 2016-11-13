function flogin() {
    var username = document.getElementById("username").value;
    var clave = document.getElementById("clave").value;
    $("#preloader").show();
    $("#preloader").append('<div class="indeterminate"></div>');
    $.get("LoguinServlet", {
        username: username,
        clave: clave
    },
            function (data) {
                if (data === "true") {
                    window.location.href = "home2.jsp";
                } else {
                    $("#preloader").hide();
                    alert("¡Usuario o contraseña incorrecta!");
                }
            });
}

function fregistrar() {
    var nombre = document.getElementById("nombre").value;
    var apellido = document.getElementById("apellido").value;
    var username = document.getElementById("username").value;
    var clave = document.getElementById("password").value;
    $.get("RegistrarDatosServlet", {
        nombre: nombre,
        apellido: apellido,
        username: username,
        clave: clave
    },
            function (data) {
                if (data === "true") {
                    alert("¡Registro Correcto!\n Logeate para ingresar")
                    window.location.href = "index.html";
                } else {
                    alert("Error al registrar");
                }
            });
}

//function factualizar() {
//    var nombre = document.getElementById("nombre").value;
//    var apellido = document.getElementById("apellido").value;
//    var edad = document.getElementById("edad").value;
//    var username = document.getElementById("username").value;
//
//    $.get("ActualizarDatosServlet", {
//        nombre: nombre,
//        apellido: apellido,
//        edad: edad,
//        username: username
//    },
//    function(data) {
//        if (data === "true") {
//            window.location.href = "home.jsp";
//        } else {
//            alert("Datos modificados!");
//        }
//    });
//}

//function frecomendaciones() {
//    $.get("RecomendacionesServlet",
//            function (cadena) {
//                $("#recomendaciones").append(cadena);
//            });
//}

function ftodos() {
    $.get("ListarTodosLibrosServlet",
            function (cadena) {
                $("#data").append(cadena);
            });
}

/*function fbusqueda(){
 var titulo = document.getElementById("titulo").value;
 var autor = document.getElementById("autor").value;
 var categoria = document.getElementById("categoria").value;
 $.get("BuscarLibroServlet",
 function(cadena) {
 $("#buscar").append(cadena);
 });
 }
 */

/*function fmas() {
 $.get("MostrarDetalleLibroServlet2",
 function (cadena) {
 $("#detalle").append(cadena);
 });
 }*/

function getRadios(idL, tamaño) {
    var rpta = new Array();
    for (i = 0; i < 4; i++) {
        for (j = 0; j < 4; j++) {
            for (t = 0; t < tamaño; t++) {
                if (document.getElementById("opt" + i + j + "-" + t) !== null) {
                    if (document.getElementById("opt" + i + j + "-" + t).checked) {
                        rpta[i] = document.getElementById("opt" + i + j + "-" + t).value;
                    }
                }
            }
        }
    }
    var sp0 = rpta[0].split("-");
    var sp1 = rpta[1].split("-");
    var sp2 = rpta[2].split("-");
    var sp3 = rpta[3].split("-");

    $.get("ObtenerRespuesta",
            {
                idlibro: idL,
                preg1: sp0[1],
                preg2: sp1[1],
                preg3: sp2[1],
                preg4: sp3[1],
                rpta1: sp0[0],
                rpta2: sp1[0],
                rpta3: sp2[0],
                rpta4: sp3[0]
            },
            function (data) {
                alert(data);
            }
    );
}

function fsalir() {
    $.get("SalirServlet");
}

function fbuscar() {
    $('#buscar thead th').each(function () {
        var title = $(this).text();
        $(this).html('<input type="text" placeholder="Search ' + title + '" />');

        var table = $('#buscar').DataTable();
        table.columns().every(function () {
            var that = this;
            //cuando la tecla se levanta. keydown cuando presiona, keyup levanta
            $('input', this.header()).on('keyup change', function () {
                if (that.search() !== this.value) {
                    that.search(this.value).draw();
                }
            });
        });
    });
}

$(document).ready(function () {

    $(".rating input:radio").attr("checked", false);
    $('.rating input').click(function () {
        $(".rating span").removeClass('checked');
        $(this).parent().addClass('checked');
    });

    $('.rating input:radio').change(
            function () {
                var rpta = this.value.split("-");
                console.log(rpta);
                console.log(rpta[0]);
                console.log(rpta[1]);
                var userRating = rpta[0];
                var id = rpta[1];

                $.get("PuntuarLibroServlet",
                        {
                            idlibro: id,
                            userRate: userRating
                        },
                        function (data) {
                            alert(data);
                        }
                );
            });
});

function mostrarFecha(idUsuario, idLibro) {
    var d = new Date();
    //alert(d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds());
    $.get("TimerInicioServlet", {
        d: d.getMinutes(), idu: idUsuario, idl: idLibro}
    );
}

function mostrarFecha2(idUsuario, idLibro) {
    var d = new Date();
    //alert(d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds());
    $.get("TimerFinServlet", {
        d: d.getMinutes(), idu: idUsuario, idl: idLibro}
    );
}

function searchTitulo() {
    //alert('titulo buscado');

    var tit = document.getElementById("tit").value;
    $.get("SearchTitulo", {titulo: tit},
            function (cadena) {
                $("#data").html();
                $("#data").html(cadena);
            });
}

function searchCategoria() {
    //alert('categoria buscado');

    var cate = document.getElementById("cat").value;
    console.log(cate);
    $.get("SearchCategoria", {categoria: cate},
            function (cadena) {
                $("#data").html();
                $("#data").html(cadena);
            });
}

function searchAutor() {
    //alert('autor buscado');
    var autor = document.getElementById("aut").value;
    $.get("SearchAutor", {autor: autor},
            function (cadena) {
                $("#data").html();
                $("#data").html(cadena);
            });
}

$(document).ready(function () {

    (function ($) {

        $('#f1').keyup(function () {

            var rex = new RegExp($(this).val(), 'i');
            $('.searchable tr').hide();
            $('.searchable tr').filter(function () {
                return rex.test($(this).text());
            }).show();
        });
    }(jQuery));

});
    