package med.voll.api.domain.consulta;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.validaciones.ValidadorDeConsultas;
import med.voll.api.domain.consulta.DatosDetalleConsulta;
import med.voll.api.domain.consulta.desafio.ValidadorCancelamientoDeConsulta;
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
	@Autowired
	List<ValidadorDeConsultas> validadores;
	@Autowired
    private List<ValidadorCancelamientoDeConsulta> validadoresCancelamiento;
	
	public DatosDetalleConsulta agendar(DatosAgendarConsulta datosAgendarConsulta) {
		
		if(!pacienteRepository.findById(datosAgendarConsulta.idPaciente()).isPresent()) {
			throw new ValidacionDeIntegridad("Este Id para paciente no fue encontrado");			
		}
		
		if(datosAgendarConsulta.idMedico()!= null && !medicoRepository.existsById(datosAgendarConsulta.idMedico())) {
			throw new ValidacionDeIntegridad("Este Id para medico no fue encontrado");
		}
		
		validadores.forEach(v->v.validar(datosAgendarConsulta)); 
		
		var paciente = pacienteRepository.findById(datosAgendarConsulta.idPaciente()).get();
		
		var medico = seleccionarMedico(datosAgendarConsulta);
		
		if(medico==null){
            throw new ValidacionDeIntegridad("no existen medicos disponibles para este horario y especialidad");
        }
				
		var consulta = new Consulta(medico, paciente, datosAgendarConsulta.fecha());
		
		consultaRepository.save(consulta);
		
		return new DatosDetalleConsulta(consulta);
		
	}

	private Medico seleccionarMedico(DatosAgendarConsulta datosAgendarConsulta) {
		if(datosAgendarConsulta.idMedico()!= null) {
			
			return medicoRepository.getReferenceById(datosAgendarConsulta.idMedico());
			
		}
		if(datosAgendarConsulta.especialidad() == null) {
			throw new ValidacionDeIntegridad("Debe selecionarse una especialidad para el medico");
		}
		
		return medicoRepository.seleccionarMedicoConEspecialidadEnFecha(datosAgendarConsulta.especialidad(),
				datosAgendarConsulta.fecha());
	}
	
	public void cancelar(DatosCancelamientoConsulta datos) {
		System.out.println(datos.idConsulta() + "   " + datos.motivo());
        if (!consultaRepository.existsById(datos.idConsulta())) {
            throw new ValidacionDeIntegridad("Id de la consulta informado no existe!");
        }

        validadoresCancelamiento.forEach(v -> v.validar(datos));

        var consulta = consultaRepository.getReferenceById(datos.idConsulta());
        consulta.cancelar(datos.motivo());
    }

}
