package es.safareyes.swapcloset.repositorios;

public interface PerfilCoincidenteProyeccion {
    Integer getIdCoincidente();
    Integer getIdUsuario();
    String getNombreCompleto();
    String getCiudad();
    Long getNumEstilosComunes();
    Double getReputacion();
    String getEstilosComunes();
}