package med.voll.api.domain.consulta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.ValidationException;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.errores.ValidacionDeIntegridad;

@Service
public class AgendaDeConsultaService {
	
	@Autowired
	private MedicoRepository medicoRepository;
	@Autowired
	private PacienteRepository pacienteRepository;
	@Autowired
	private ConsultaRepository consultaRepository;
	
	public void agendar(DatosAgendarConsulta datosAgendarConsulta) {
		
		if(pacienteRepository.findById(datosAgendarConsulta.idPaciente()).isPresent()) {
			throw new ValidacionDeIntegridad("Este Id para paciente no fue encontrado");			
		}
		
		if(datosAgendarConsulta.idMedico()!= null && medicoRepository.existsById(datosAgendarConsulta.idMedico())) {
			throw new ValidacionDeIntegridad("Este Id para medico no fue encontrado");
		}
		
		var paciente = pacienteRepository.findById(datosAgendarConsulta.idPaciente()).get();
		var medico = seleccionarMedico(datosAgendarConsulta);
		
		
		
		var consulta = new Consulta(null,medico, paciente, datosAgendarConsulta.fecha());		
		consultaRepository.save(consulta);
		
	}

	private Medico seleccionarMedico(DatosAgendarConsulta datosAgendarConsulta) {
		if(datosAgendarConsulta.id()== null) {
			
			return medicoRepository.getReferenceById(datosAgendarConsulta.idMedico());
			
		}
		if(datosAgendarConsulta.especialidad() == null) {
			throw new ValidacionDeIntegridad("Debe selecionarse una especialidad para el medico");
		}
		
		return medicoRepository.seleccionarMedicoConEspecialidadEnFecha(datosAgendarConsulta.especialidad(), datosAgendarConsulta.fecha());
	}

}
