package vn.edu.iuh.fit.olachatbackend.utils;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.JWTClaimsSet;

public class extractUserIdFromJwt {

    public static String extractUserIdFromJwt(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            return claims.getStringClaim("userId"); // ✅ Phải có trong token
        } catch (Exception e) {
            throw new RuntimeException("Không thể trích xuất userId từ JWT", e);
        }
    }
}
