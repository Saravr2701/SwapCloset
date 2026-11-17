package es.safareyes.swapcloset.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    private ProblemDetail construir(HttpStatus estado, String titulo, String detalle) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(estado, detalle);
        problema.setTitle(titulo);
        problema.setProperty("timestamp", LocalDateTime.now());
        return problema;
    }
}
