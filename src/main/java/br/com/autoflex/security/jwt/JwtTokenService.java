package br.com.autoflex.security.jwt;

import br.com.autoflex.dto.TokenResponse;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;


@Service
public class JwtTokenService {

    private final String secret;
    private final String issuer;
    private final long expirationMinutes;

    public JwtTokenService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.expirationMinutes}") long expirationMinutes
    ) {
        this.secret = secret;
        this.issuer = issuer;
        this.expirationMinutes = expirationMinutes;
    }

    public TokenResponse generate(Authentication auth) {
        Instant now = Instant.now();
        Instant exp = now.plus(expirationMinutes, ChronoUnit.MINUTES);

        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        try {
            JWSSigner signer = new MACSigner(secret.getBytes(StandardCharsets.UTF_8));

            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .issuer(issuer)
                    .subject(auth.getName())
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(exp))
                    .claim("roles", roles)
                    .build();

            SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
            jwt.sign(signer);

            long expiresIn = Duration.between(now, exp).getSeconds();
            return new TokenResponse(jwt.serialize(), "Bearer", expiresIn);

        } catch (JOSEException e) {
            throw new RuntimeException("Erro ao gerar JWT", e);
        }
    }
}