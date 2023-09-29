package med.voll.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import med.voll.api.domain.usuarios.DatosAuntenticacionUsuario;
import med.voll.api.domain.usuarios.Usuario;
import med.voll.api.infra.security.DatosJWTToken;
import med.voll.api.infra.security.TokenService;

@RestController
@RequestMapping("/login")
public class AuntenticacionController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping
	public ResponseEntity autenticarUsuario(@RequestBody @Valid DatosAuntenticacionUsuario datosAuntenticacionUsuario){
				
		Authentication auhtToken = new UsernamePasswordAuthenticationToken(datosAuntenticacionUsuario.login(),
				datosAuntenticacionUsuario.clave());
				 	
		var usuarioAutenticado = authenticationManager.authenticate(auhtToken);
		var JWTToken = tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal());
		
		return ResponseEntity.ok(new DatosJWTToken(JWTToken));
		
	}

}
