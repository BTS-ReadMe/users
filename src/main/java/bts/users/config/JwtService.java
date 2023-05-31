package bts.users.config;

import bts.users.user.model.User;
import bts.users.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {

    @Value("${secret_key}")
    private String SECRET_KEY;

    private final UserRepository userRepository;

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        return new UsernamePasswordAuthenticationToken(token, claims);
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    //        public String generateToken(UserDetails userDetail) {
    public String generateToken(User user) {

        Claims claims = Jwts.claims() //payload에 들어가는 정보
            .setSubject("accessToken")
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)); //1h

        claims.put("role", user.getUserRole());
        if (user.getUuid() != null) {
            claims.put("uuid", user.getUuid());
        }

        return Jwts.builder()
            .setHeaderParam("type", "jwt")
            .setClaims(claims)
            .signWith(getSignKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    //    public Boolean isTokenValid(String jwt, UserDetails userDetails) {
    public Boolean isTokenValid(String jwt, User user) {
        final String userName = extractUsername(jwt);
//        return userDetails.getUsername().equals(userName) && !isTokenExpired(jwt);
        return user.getUserUuid().equals(userName) && !isTokenExpired(jwt);
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
//        final Claims claims = extractAllClaims(jwt);
        final Claims claims = parseClaims(jwt);
        return claimsResolver.apply(claims);
    }

//    private Claims extractAllClaims(String jwt) {
//        return Jwts
//            .parserBuilder()
//            .setSigningKey(getSignKey())
//            .build()
//            .parseClaimsJws(jwt)
//            .getBody();
//    }

    private Key getSignKey() {
        byte[] key = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(key);
    }
}