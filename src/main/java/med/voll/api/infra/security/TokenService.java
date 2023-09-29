package med.voll.api.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import med.voll.api.domain.usuarios.Usuario;

@Service
public class TokenService {
		
	@Value("${api.security.secret}")
	private String apiSecret; 
	
	public String generarToken(Usuario usuario) {
		try {
		    Algorithm algorithm = Algorithm.HMAC256(apiSecret);
		    return JWT.create()
		        .withIssuer("voll med")
		        .withSubject(usuario.getLogin())
		        .withClaim("id", usuario.getId())
		        .withExpiresAt(generarFechaExpiracion())
		        .sign(algorithm);
		} catch (JWTCreationException exception){
		    throw new RuntimeException();
		}	
	}
	
	public String getSubject(String jwtToken) {
		if(jwtToken == null) {
			throw new RuntimeException("Token null");
		}
		DecodedJWT verifier = null;
		try {
		    Algorithm algorithm = Algorithm.HMAC256(apiSecret); // validando firma
		    verifier = JWT.require(algorithm)
		            .withIssuer("voll med")
		            .build()
		            .verify(jwtToken);
		} catch (JWTVerificationException exception) {
		    System.out.println(exception.toString());
		}
		if (verifier == null || verifier.getSubject() == null) {
		    throw new RuntimeException("Verifier invalido");
		}
		return verifier.getSubject();
	}
	
	private Instant generarFechaExpiracion() {
		
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00")); 
		
	}
	
	

}
