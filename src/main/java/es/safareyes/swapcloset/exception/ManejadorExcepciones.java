package es.safareyes.swapcloset.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ManejadorExcepciones {
    @ExceptionHandler(SinTipoSolicitud.class)
    public ProblemDetail sinTipoSolicitud(SinTipoSolicitud e) {
        return construir(HttpStatus.valueOf(422), "Tipo de solicitud no introducido",
                e.getMessage());
    }

    @ExceptionHandler(RelacionNoEncontrada.class)
    public ProblemDetail relacionNoEncontrada(RelacionNoEncontrada e) {
        return construir(HttpStatus.valueOf(404), "La relación indicada no existe",
                e.getMessage());
    }

    @ExceptionHandler(DatoNoEncontrado.class)
    public ProblemDetail DatoNoEncontrado(DatoNoEncontrado e) {
        return construir(HttpStatus.valueOf(404), "El campo introducido no se ha encontrado el los registros de la base de datos",
                e.getMessage());
    }

    @ExceptionHandler(SolicitudPrendaPropia.class)
    public ProblemDetail SolicitudPrendaPropia(SolicitudPrendaPropia e) {
        return construir(HttpStatus.valueOf(422), "No puedes solicitar una prenda que te pertenece",
                e.getMessage());
    }

    @ExceptionHandler(PrendaNoDisponible.class)
    public ProblemDetail PrendaNoDisponible(PrendaNoDisponible e) {
        return construir(HttpStatus.valueOf(409), "La prenda no está disponible",
                e.getMessage());
    }

    @ExceptionHandler(TipoSolicitudNoPermitido.class)
    public ProblemDetail TipoSolicitudNoPermitido(TipoSolicitudNoPermitido e) {
        return construir(HttpStatus.valueOf(422), "Esta prenda no admite ese tipo de solicitud",
                e.getMessage());
    }

    @ExceptionHandler(FechaNoValida.class)
    public ProblemDetail FechaNoValida(FechaNoValida e) {
        return construir(HttpStatus.valueOf(422), "Las fechas introducidas no son válidas",
                e.getMessage());
    }

    @ExceptionHandler(DuracionNoValida.class)
    public ProblemDetail DuracionNoValida(DuracionNoValida e) {
        return construir(HttpStatus.valueOf(422), "La duración de la solicitud excede del máximo establecido",
                e.getMessage());
    }

    @ExceptionHandler(PrestamosActivosExcedidos.class)
    public ProblemDetail PrestamosActivosExcedidos(PrestamosActivosExcedidos e) {
        return construir(HttpStatus.valueOf(422), "No se pueden tener más de 3 préstamos de manera simultánea",
                e.getMessage());
    }

    @ExceptionHandler(SolicitudDuplicada.class)
    public ProblemDetail SolicitudDuplicada(SolicitudDuplicada e) {
        return construir(HttpStatus.valueOf(409), "Ya has solicitado esta prenda anteriormente, está pendiente de aprobación",
                e.getMessage());
    }

    @ExceptionHandler(Inexistente.class)
    public ProblemDetail SolicitudInexistente(Inexistente e) {
        return construir(HttpStatus.valueOf(404), "El mensaje no se puede mandar ya que no existe la solicitud de la prenda",
                e.getMessage());
    }

    @ExceptionHandler(NoDisponible.class)
    public ProblemDetail NoDisponible(NoDisponible e) {
        return construir(HttpStatus.valueOf(409), "Esta conversación no está disponible",
                e.getMessage());
    }

    @ExceptionHandler(PerfilNoEstaEnLaSolicitud.class)
    public ProblemDetail PerfilNoEstaEnLaSolicitud(PerfilNoEstaEnLaSolicitud e) {
        return construir(HttpStatus.valueOf(403), "El perfil indicado no pertenece a la solicitud",
                e.getMessage());
    }

    @ExceptionHandler(SolicitudNoExiste.class)
    public ProblemDetail SolicitudNoExiste(SolicitudNoExiste e) {
        return construir(HttpStatus.valueOf(404), "La solicitud indicada no existe",
                e.getMessage());
    }

    @ExceptionHandler(SolicitudRechazada.class)
    public ProblemDetail SolicitudRechazada(SolicitudRechazada e) {
        return construir(HttpStatus.valueOf(409), "El estado de la solicitud no acepta valoraciones",
                e.getMessage());
    }

    @ExceptionHandler(SolicitudEnCurso.class)
    public ProblemDetail SolicitudEnCurso(SolicitudEnCurso e) {
        return construir(HttpStatus.valueOf(409), "El estado de la solicitud no acepta valoraciones",
                e.getMessage());
    }

    @ExceptionHandler(ValoracionNoValida.class)
    public ProblemDetail ValoracionNoValida(ValoracionNoValida e) {
        return construir(HttpStatus.valueOf(409), "Valoración no válida",
                e.getMessage());
    }

    @ExceptionHandler(EstadoNoValido.class)
    public ProblemDetail EstadoNoValido(EstadoNoValido e) {
        return construir(HttpStatus.valueOf(422), "El estado de la solicitud no permite este cambio",
                e.getMessage());
    }

    @ExceptionHandler(BajaNoPermitida.class)
    public ProblemDetail BajaNoPermitida(BajaNoPermitida e) {
        return construir(HttpStatus.valueOf(409), "Baja de la prenda no permitida",
                e.getMessage());
    }

    @ExceptionHandler(BandejaNoValida.class)
    public ProblemDetail BandejaNoValida(BandejaNoValida e) {
        return construir(HttpStatus.valueOf(409), "La bandeja introducida no es válida",
                e.getMessage());
    }

    @ExceptionHandler(SinColor.class)
    public ProblemDetail SinColor(SinColor e) {
        return construir(HttpStatus.valueOf(409), "El color de la prenda no puede estar vacio",
                e.getMessage());
    }

    @ExceptionHandler(AccesoInformeNoPermitido.class)
    public ProblemDetail AccesoInformeNoPermitido(AccesoInformeNoPermitido e) {
        return construir(HttpStatus.valueOf(403), "No tienes permitido el acceso al informe mensual",
                e.getMessage());
    }

    @ExceptionHandler(PaginacionNoValida.class)
    public ProblemDetail PaginacionNoValida(PaginacionNoValida e) {
        return construir(HttpStatus.valueOf(400), "Los parámetros de paginación son incorrectos",
                e.getMessage());
    }


    //Para mostrar mensaje de error bonito cuando se realiza la validación en el DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail validarCampos(MethodArgumentNotValidException e) {
        String mensaje = e.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        return construir(HttpStatus.valueOf(400),
                "Campo obligatorio o incorrecto",
                mensaje);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail manejarJsonIncorrecto(
            HttpMessageNotReadableException e) {

        ProblemDetail problema = ProblemDetail.forStatus(
                HttpStatus.BAD_REQUEST);

        problema.setTitle("Datos incorrectos");
        problema.setDetail("El JSON contiene algún valor o formato no válido");

        return problema;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail argumentoIncorrecto(MethodArgumentTypeMismatchException e) {
        return construir(
                HttpStatus.BAD_REQUEST,
                "Parámetro incorrecto",
                "El valor introducido en el parámetro "
                        + e.getName() + " no es válido"
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ProblemDetail parametroObligatorio(
            MissingServletRequestParameterException e) {

        return construir(
                HttpStatus.BAD_REQUEST,
                "Parámetro obligatorio",
                "Debes introducir el parámetro " + e.getParameterName()
        );
    }

    private ProblemDetail construir(HttpStatus estado, String titulo, String detalle) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(estado, detalle);
        problema.setTitle(titulo);
        problema.setProperty("timestamp", LocalDateTime.now());
        return problema;
    }
}
