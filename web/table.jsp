<%-- 
    Document   : table
    Created on : 27-oct-2016, 10:28:18
    Author     : DSRS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <script src="js/jquery-3.1.0.min.js" type="text/javascript"></script>
    <script src="js/funciones.js" type="text/javascript"></script>
    <script>
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
    </script>
    <body onload="ftodos()">
        <h1>Hello World!</h1>
        
        Buscar: <input type="text" id="f1" class="form-control">
        <table>
            <thead>
                <tr>
                    <th>Libro</th>
                    <th>Categoria</th>
                    <th>Autor</th>
                </tr>                
            </thead>
            <tbody id="data" class="searchable" data-filter="#f1"></tbody>
        </table>
    </body>
</html>
