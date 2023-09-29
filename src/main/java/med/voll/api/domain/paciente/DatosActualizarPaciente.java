package med.voll.api.domain.paciente;

import med.voll.api.domain.direccion.DatosDireccion;

public record DatosActualizarPaciente(long id, String nombre, String email, DatosDireccion dirrecion) {

}
