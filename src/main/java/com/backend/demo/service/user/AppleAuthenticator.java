package com.backend.demo.service.user;

import com.backend.demo.model.AppleUser;
import feign.FeignException;
import io.jsonwebtoken.ExpiredJwtException;
import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
public class AppleAuthenticator {

    @Value("${apple.client_id}")
    private String clientId;

    @Value("${apple.uri}")
    private String appleAuthUrl;

    @Value("${apple.uri.authkey}")
    private String appleAuthKey;


    public AppleUser authenticateWithApple(String idToken) throws FeignException, ExpiredJwtException {
        try {
            HttpsJwks httpsJwks = new HttpsJwks(appleAuthKey);
            HttpsJwksVerificationKeyResolver httpsJwksResolver = new HttpsJwksVerificationKeyResolver(httpsJwks);

            JwtConsumer jwtConsumer = new JwtConsumerBuilder().setVerificationKeyResolver(httpsJwksResolver).
                    setExpectedIssuer(appleAuthUrl).setExpectedAudience(clientId).build();

            JwtClaims jwtClaims = jwtConsumer.processToClaims(idToken);
            Map<String, Object> claims = jwtClaims.getClaimsMap();
            String email = (String) claims.get("email");
            Long expiryDate = (Long) claims.get("exp");
            Instant time = Instant.ofEpochSecond(expiryDate);
            if (time.isBefore(Instant.now())) {
                throw new ExpiredJwtException(null, null, "Token provided is expired");
            }
            return new AppleUser(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
