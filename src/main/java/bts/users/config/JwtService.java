package bts.users.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtService {

	@Value("${secret_key}")
	private String SECRET_KEY;

//	public Authentication getAuthentication(String token){
//		Claims claims = parseClaims(token);
//		return new UsernamePasswordAuthenticationToken(token, claims);
//	}
//
//	private Claims parseClaims(String token){
//		try{
//			return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
//		}catch (ExpiredJwtException e){
//			return e.getClaims();
//		}
//	}

	public String generateToken(UserDetails userDetail) {
		return Jwts
				.builder()
				.setSubject(userDetail.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) //1h
				.signWith(getSignKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	public Boolean isTokenValid(String jwt, UserDetails userDetails) {
		final String userName = extractUsername(jwt);
		return userDetails.getUsername().equals(userName) && !isTokenExpired(jwt);
	}

	private Boolean isTokenExpired(String jwt) {
		return extractExpiration(jwt).before(new Date());
	}

	public String extractUsername(String jwt) {
		return extractClaim(jwt, Claims::getSubject);
	}

	private Date extractExpiration(String jwt) {
		return extractClaim(jwt, Claims::getExpiration);
	}

	public <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(jwt);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String jwt) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(jwt)
				.getBody();
	}

	private Key getSignKey() {
		byte[] key = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(key);
	}
}