package org.usedcar.rest.webservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.usedcar.rest.webservice.exception.TokenIsRevokedException;
import org.usedcar.rest.webservice.model.Token;
import org.usedcar.rest.webservice.model.TokenType;
import org.usedcar.rest.webservice.model.User;
import org.springframework.stereotype.Component;
import org.usedcar.rest.webservice.repository.TokenRepository;
import org.usedcar.rest.webservice.util.HttpResponse;
import org.usedcar.rest.webservice.util.RestResponseUtil;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class Auth0JwtUtils {

    public Auth0JwtUtils(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
        init();
    }

    private Algorithm algorithm;
    private JWTVerifier verifier;
    private final String Issuer = "usedcar.org";

    @Value("${jwt.access_token.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh_token.expiration}")
    private long refreshTokenExpiration;

    private final TokenRepository tokenRepository;

    private void init() {
        algorithm = Algorithm.HMAC256("secret_key");

        verifier = JWT.require(algorithm)
                .withIssuer(Issuer)
                .build();
    }

    public String generateAccessToken(UserDetails user) {
        String accessToken = JWT.create()
                .withIssuer(Issuer)
                .withSubject("Access-token")
                .withClaim("email", user.getUsername())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(";")))
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpiration)) //1 hour
                .withJWTId(UUID.randomUUID().toString())
                .sign(algorithm);
        return saveToken(new Token(accessToken, TokenType.ACCESS_TOKEN)).getToken();
    }

    public String generateRefreshToken(UserDetails user) {
        String refreshToken = JWT.create()
                .withIssuer(Issuer)
                .withSubject("Refresh-token")
                .withClaim("email", user.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpiration)) //2 hour
                .withJWTId(UUID.randomUUID().toString())
                .sign(algorithm);
        return saveToken(new Token(refreshToken, TokenType.REFRESH_TOKEN)).getToken();
    }

    private Token saveToken(Token token) {
        return tokenRepository.save(token);
    }

    public DecodedJWT validateJwtToken(String jwtToken, HttpServletResponse response) throws IOException {
        try {
            checkTokenIsRevoked(jwtToken);
            DecodedJWT decodedJWT = verifier.verify(jwtToken);
            return decodedJWT;
        } catch (JWTVerificationException | TokenIsRevokedException ex) {
            log.warn("Token verification failure: {}", ex.getMessage());
            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            HttpResponse httpResponse = new HttpResponse(
                    HttpStatus.UNAUTHORIZED.value(),
                    HttpStatus.UNAUTHORIZED,
                    ex.getMessage()
            );

            RestResponseUtil.sendHttpResponse(response, RestResponseUtil.createJsonStringResponse(httpResponse));
        }
        return null;
    }

    private void checkTokenIsRevoked(String jwtToken) throws TokenIsRevokedException{
        Token token = tokenRepository.findTokenByToken(jwtToken);
        if (token != null) {
            if (token.isRevoked()) {
                throw new TokenIsRevokedException("Your token is revoked, please login again!");
            }
        }
    }

    public void revokeToken(String jwtToken) {
        log.info("Revoking token {}", jwtToken);
        Token token = tokenRepository.findTokenByToken(jwtToken);
        token.setRevoked(true);
        tokenRepository.save(token);
    }
}
