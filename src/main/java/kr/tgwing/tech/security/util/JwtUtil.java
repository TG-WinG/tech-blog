package kr.tgwing.tech.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private SecretKey secretKey;

    public JwtUtil(@Value(".${spring.jwt.secretKey}")String secret) {

        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }


    public String getStudentId(String token) {

        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String getRole(String token) {

        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().get("role", String.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
                .getBody().getExpiration().before(new Date());
    }

    public String createJwt(String studentId, String profilePicture, String role, Long expiredTime) {
        // name : token에 들어있는 것으로 사용함
        // secretKey : 서명

        Claims claims = Jwts.claims();
        // claim안에 내가 담고자 하는 정보 담아두기 가능함.

        claims.setSubject(studentId); //jwt 주체를 사용자 이름으로 설정
        claims.put("profilePicture", profilePicture);
        claims.put("role", role); // 내용


        return Jwts.builder()
                .setClaims(claims) // jwt 공간안에 claim에 담은 정보 넣기
                .setIssuedAt(new Date(System.currentTimeMillis())) // 보내는 시간
                .setExpiration(new Date(System.currentTimeMillis() + expiredTime)) // 유효시간
                .signWith(secretKey)
                .compact();
    }
}
