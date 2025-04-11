//package com.vtit.project.thuctap.security;
//import java.text.ParseException;
//import java.util.Objects;
//import javax.crypto.spec.SecretKeySpec;
//
//import com.nimbusds.jose.JOSEException;
//import com.vtit.project.thuctap.dto.request.IntrospectRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.JwtException;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//import org.springframework.stereotype.Component;
//
//
//
//@Component
//public class CustomJwtDecoder implements JwtDecoder{
//    @Value("${jwt.signerKey}")
//    private String signerKey;
//
//    @Autowired
//    private AuthenticationService authenticationService;
//
//    private NimbusJwtDecoder nimbusJwtDecoder = null;
//
//    @Override
//    public Jwt decode(String token) throws JwtException {
//        // Kiểm tra token qua authenticationService
//        try {
//            var response = authenticationService.introspect(
//                    IntrospectRequest.builder().token(token).build());
//
//            if (!response.isValid()) throw new JwtException("Token invalid");
//        } catch (JOSEException | ParseException e) {
//            throw new JwtException(e.getMessage());
//        }
//        // Giải mã token
//        if (Objects.isNull(nimbusJwtDecoder)) {
//            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
//            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
//                    .macAlgorithm(MacAlgorithm.HS512)
//                    .build();
//        }
//
//        return nimbusJwtDecoder.decode(token);
//    }
//}
