package med.voll.api.domain.consulta.validaciones;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import med.voll.api.domain.paciente.PacienteRepository;

@Component
public class PacienteActivo implements ValidadorDeConsultas{
	
	@Autowired
	private PacienteRepository pacienteRepository;
	
	public void validar(DatosAgendarConsulta datos) {
		
		if(datos.idPaciente() == null ) {
			return;
		}
		
		var pacienteActivo = pacienteRepository.findActivoById(datos.idPaciente());
		
		if (pacienteActivo == false) {
			throw new ValidationException("no se puede permitir agendas con pacientes inactivos");		
		}
		
	}

}
