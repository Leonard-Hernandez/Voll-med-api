package med.voll.api.domain.paciente;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.domain.direccion.Direccion;

@Table(name = "pacientes")
@Entity(name = "Paciente")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Paciente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id; 
	private String nombre; 
	private String email; 
	private String telefono;
	private String documento;
	private Boolean activo;
	@Embedded
	private Direccion direccion;
	
	public Paciente(@Valid DatosRegistroPaciente datosRegistroPaciente) {
		this.nombre = datosRegistroPaciente.nombre();
		this.email = datosRegistroPaciente.email();
		this.telefono = datosRegistroPaciente.telefono();
		this.documento = datosRegistroPaciente.documento();
		this.activo = true;
		this.direccion = new Direccion(datosRegistroPaciente.direccion());
	}

	public void actualizarPaciente(DatosActualizarPaciente datosActualizarPaciente) {
		if(datosActualizarPaciente.nombre() != null) {
			this.nombre = datosActualizarPaciente.nombre();
		}
		
		if(datosActualizarPaciente.email() != null) {
			this.email = datosActualizarPaciente.email();
		}
		
		if (datosActualizarPaciente.dirrecion() != null) {
			this.direccion = new Direccion().actualizarDireccion(datosActualizarPaciente.dirrecion());
		}
		
	}

	public void desactivarPaciente() {
		this.activo = false;
		
	}
	

}
