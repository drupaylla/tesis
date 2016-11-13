<%@page import="DTO.UsuarioxLibro"%>
<%@page import="DAO.Pregunta"%>
<%@page import="DAO.Prediccion"%>
<%@page import="java.util.List"%>
<%@page import="DAO.Libro"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Leer es ¡Gratis!</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="css/bootstrap.css">
        <script src="js/jquery-3.1.0.min.js" type="text/javascript"></script>
        <!--<script src="js/jquery.dataTables.min.js" type="text/javascript"></script>-->
        <script src="js/funciones.js" type="text/javascript"></script>
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/business-casual.css" rel="stylesheet">
        <script>
            <%
                Cookie[] cookies = null;
                cookies = request.getCookies();
                String idUsuario = "";
                for (int i = 0; i < cookies.length; i++) {
                    System.out.println("cookies: " + cookies[i].getValue() + "-" + cookies[i].getName());
                    if (cookies[i].getName().equals("idusuario")) {
                        idUsuario = cookies[i].getValue();
                        System.out.println("cookie: " + idUsuario);
                    }
                }%>
        </script>
    </head>
    <body>

        <div class="container">
            <!--<h2 style="color: #FFFFFF;float: right">Aplicación Web de Lectura</h2>-->
            <ul class="nav nav-tabs">
                <li class="active"><a data-toggle="tab" href="#home" style="size: 100px">Bienvenido</a></li>
                <li><a data-toggle="tab" href="#menu1" style="size: 100px">Acerca</a></li>
                <li><a data-toggle="tab" href="#menu2" style="size: 100px">Leer</a></li>
            </ul>
            <div class="tab-content">
                <div id="home" class="tab-pane fade in active">
                    <div class="brand">¡ Leer es genial !</div>
                    <div class="address-bar">Una plataforma para leer gratis</div>
                    <div id="myCarousel" class="carousel slide" data-ride="carousel">
                        <!-- Indicators -->
                        <ol class="carousel-indicators">
                            <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
                            <li data-target="#myCarousel" data-slide-to="1"></li>
                            <!--<li data-target="#myCarousel" data-slide-to="2"></li>
                            <li data-target="#myCarousel" data-slide-to="3"></li>-->
                        </ol>

                        <!-- Wrapper for slides -->
                        <div class="carousel-inner" role="listbox">

                            <div class="item active">
                                <img src="img/verde.jpg" alt="Chania" style="width: 300px;height: 300px;display: inline-block">
                                <img src="img/acerca.jpg" alt="Chania" style="width: 300px;height: 300px;display: inline-block">
                            </div>

                            <div class="item">
                                <img src="img/fondito.jpg" alt="Flower" style="width: 300px;height: 300px;display: inline-block">
                                <img src="img/todo.jpg" alt="Flower" style="width: 300px;height: 300px;display: inline-block">
                            </div>

                        </div>

                        <!-- Left and right controls -->
                        <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
                            <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                            <span class="sr-only">Previous</span>
                        </a>
                        <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
                            <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                            <span class="sr-only">Next</span>
                        </a>
                    </div>
                    <div>
                        <h3 align="center"> <a href="SalirServlet">Salir </a></h3>
                    </div>
                </div>
                <div id="menu1" class="tab-pane fade">
                    <div class="container">
                        <div class="row">
                            <div class="box">
                                <div class="col-lg-12">
                                    <hr>
                                    <h2 class="intro-text text-center">Acerca de
                                        <strong>¡ Leer es genial !</strong>
                                    </h2>
                                    <hr>
                                </div>
                                <div class="col-md-6">
                                    <img class="img-responsive img-border-left" src="img/acerca.jpg" alt="">
                                </div>
                                <div class="col-md-6">
                                    <p>Leer es genial es una aplicación web que te permite leer libros de manera gratuita.</p>
                                    <p>Asi tambien, podrás calificar en una escala de 1 a 5 a un libro, si te pareció muy buena, le pondrás 5.</p>
                                    <p>Luego de terminar el libro, tienes la opción de relizar una pequeña evaluación acerca de la lectura. Se trata de 4 preguntas de opción multiple, donde luego, tendrás una nota del 1 al 4, siendo 4, si respondes todo bien</p>
                                    <p>Recuerda que leer es bueno, alimenta tu labia y bagaje cultural, ademas de ser divertido y aprender muchas nuevas cosas.</p>
                                </div>
                                <div class="clearfix"></div>
                            </div>
                        </div>

                        <div>
                            <h3 align="center"> <a href="SalirServlet">Salir </a></h3>
                        </div>
                    </div>
                </div>
                <div id="menu2" class="tab-pane fade">
                    <div class="container">
                        <div class="row">
                            <div class="box">
                                <div class="col-md-8">
                                    <input placeholder="Ingrese consulta..." type="text" id="f1" class="form-control" style="max-width: 600px;margin-left: 10%">
                                    <div class="container" style="overflow-y:scroll;max-height: 450px;max-width: 600px">

                                        <table id="buscar" class="table table-stripped" cellspacing="0" width="100%">
                                            <thead>
                                                <tr>
                                                    <th>Titulo</th>
                                                    <th>Categoria</th>
                                                    <th>Autor</th>
                                                </tr>
                                            </thead>                                        
                                            <tbody id="buscados" class="searchable" data-filter="#f1">
                                                <%Libro lib = new Libro();
                                                    List<Libro> libros = lib.obtenerTodosTitulosLibros();%>
                                                <%for (int i = 0; i < libros.size(); i++) {%>
                                                <tr>
                                                    <td><button type="button" class="btn btn-link" data-toggle="modal" data-target="#myModal<%=i%>"><h8 style="size: 15px;width: 250px"><%=libros.get(i).getTitulo()%></h8></button></td>
                                                    <td><%=libros.get(i).getCategoria()%></td>
                                                    <td><%=libros.get(i).getAutor()%></td>

                                                    <!--Pop up del resumen -->
                                            <div id="myModal<%=i%>" class="modal fade" role="dialog">
                                                <div class="modal-dialog">
                                                    <!-- Modal content-->
                                                    <div class="modal-content">
                                                        <div class="modal-header">
                                                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                                                            <h4 class="modal-title">Dale una puntuación!</h4>
                                                            <div class="rating">
                                                                <span><input type="radio" name="rating" id="str5" value="1-<%=libros.get(i).getIdLibro()%>"><label for="str5"></label></span>
                                                                <span><input type="radio" name="rating" id="str4" value="2-<%=libros.get(i).getIdLibro()%>"><label for="str4"></label></span>
                                                                <span><input type="radio" name="rating" id="str3" value="3-<%=libros.get(i).getIdLibro()%>"><label for="str3"></label></span>
                                                                <span><input type="radio" name="rating" id="str2" value="4-<%=libros.get(i).getIdLibro()%>"><label for="str2"></label></span>
                                                                <span><input type="radio" name="rating" id="str1" value="5-<%=libros.get(i).getIdLibro()%>"><label for="str1"></label></span>                                    
                                                            </div>
                                                        </div>
                                                        <div class="modal-body">
                                                            <div class="w3-container">
                                                                <h2>Detalles del Libro</h2>
                                                                <div class="w3-card-4" style="width:50%;">
                                                                    <header class="w3-container w3-blue">
                                                                        <h1>Resumen</h1>
                                                                    </header>
                                                                    <div class="w3-container">
                                                                        <%Libro detalles = lib.obtenerDetalleLibro(libros.get(i).getIdLibro());%>
                                                                        <p><%=detalles.getTitulo()%></p>
                                                                        <p><%=detalles.getResumen()%></p>
                                                                        <p><%=detalles.getCategoria()%></p>
                                                                    </div>
                                                                    <footer class="w3-container w3-blue">
                                                                    </footer>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
                                                            <button type="button" class="btn btn-info btn-lg" data-toggle="modal" onclick="mostrarFecha(<%=idUsuario%>,<%=libros.get(i).getIdLibro()%>)" data-target="#pdf<%=i%>">PDF</button>
                                                            <button type="button" class="btn btn-info btn-lg" data-toggle="modal" data-target="#evaluacion<%=i%>">Evaluación</button>
                                                        </div>
                                                    </div>

                                                </div>
                                            </div>   

                                            <!--pop up de pdf-->
                                            <div id="pdf<%=i%>" class="modal fade" role="dialog" >
                                                <div class="modal-dialog">
                                                    <!-- Modal content-->
                                                    <div class="modal-content">
                                                        <div class="modal-header">
                                                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                                                            <h4 class="modal-title">Texto del Libro:</h4>
                                                        </div>
                                                        <div class="modal-body">

                                                            <iframe src="http://docs.google.com/gview?url=<%=libros.get(i).getUrl()%>&embedded=true" 
                                                                    style="width:500px; height:500px;margin-top: 15px" frameborder="0"></iframe>
                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-default" onclick="mostrarFecha2(<%=idUsuario%>,<%=libros.get(i).getIdLibro()%>)" data-dismiss="modal">Cerrar</button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <!--pop up de preguntas--> 
                                            <div id="evaluacion<%=i%>" class="modal fade" role="dialog">
                                                <div class="modal-dialog">
                                                    <!-- Modal content-->
                                                    <div class="modal-content">
                                                        <div class="modal-header">
                                                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                                                            <h4 class="modal-title">Preguntas</h4>
                                                        </div>
                                                        <div class="modal-fade">
                                                            <%Pregunta p = new Pregunta();
                                                                List<Pregunta> preg = p.obtenerPreguntas(libros.get(i).getIdLibro());%>
                                                            <input type="text" hidden="">
                                                            <% for (int j = 0; j < preg.size(); j++) {%>
                                                            <p>Pregunta</p>
                                                            <br>
                                                            <%=preg.get(j).getDescripcion()%>    
                                                            <br>                            
                                                            <%List<String> opc = p.mostrarOpciones(preg.get(j).getIdPregunta());%>
                                                            <% for (int k = 0; k < opc.size(); k++) {%>
                                                            <br> 
                                                            <input type="radio" id="opt<%=j%><%=k%>-<%=libros.get(i).getIdLibro()%>" name ="radio<%=j%>" value="<%=opc.get(k)%>-<%=preg.get(j).getIdPregunta()%>"><%=opc.get(k)%>
                                                            <br>
                                                            <br>                           
                                                            <%}%>
                                                            <%}%>
                                                            <input type="button" onclick="getRadios(<%=libros.get(i).getIdLibro()%>,<%=libros.size()%>)" class="btn btn-primary btn-block btn-lg" tabindex="7" data-dismiss="modal" value="Finalizar">
                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>                        

                                            </tr>
                                            <%}%>
                                            </tbody>
                                        </table>
                                    </div>

                                </div>
                                <div class="col-md-4">
                                    <h2> Te recomendamos :</h2>
                                    <div class="container" style="overflow-y:scroll;max-height: 500px;max-width: 360px">
                                        <table>
                                            <tbody id="recomendaciones">
                                                <%Prediccion uxl = new Prediccion();
                                                    List<Libro> rec = uxl.getLibrosRecomendados(Integer.parseInt(idUsuario));%>
                                                <%for (int n = 0; n < rec.size(); n++) {%>
                                                <tr>
                                                    <td>
                                                        <button style="size: 25px" type="button" class="btn btn-link" data-toggle="modal" data-target="#myModal<%=n%>"><h4><%=rec.get(n).getTitulo()%></h4></button>
                                                        <p style="margin-left: 30px;size: 12px"><%=rec.get(n).getCategoria()%> - <%=rec.get(n).getPuntuacion()%></p>
                                                    </td>
                                                </tr>
                                                <%}%>
                                            </tbody>
                                        </table>                   
                                    </div> 
                                </div>
                            </div>
                        </div>
                        <div>
                            <h3 align="center"> <a href="SalirServlet">Salir </a></h3>
                        </div>
                    </div>
                </div>
            </div>
        </div>