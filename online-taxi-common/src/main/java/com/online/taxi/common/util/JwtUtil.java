package com.online.taxi.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

import java.util.Date;

/**
 * @author yueyi2019
 */
public class JwtUtil {
    /**
     * 密钥，仅服务端存储
     */
    private static String secret = "ko346134h_we]rg3in_yip1!";

    /**
     * @param subject
     * @param issueDate 签发时间
     * @return
     */
    public static String createToken(String subject, Date issueDate) {
        // setIssuer()     jwt签发者
        // setSubject()    jwt所面向的用户
        // setIssuedAt()   jwt的签发时间
        // setExpiration() jwt的过期时间
        // setNotBefore()  定义在什么时间之前，该jwt都是不可用的
        // setAudience()   接收jwt的一方
        String compactJws = Jwts.builder()
                .setSubject(subject)     //Payload有效载荷
                .setIssuedAt(issueDate)  //Payload有效载荷
                .setExpiration(new Date(System.currentTimeMillis() + 10000))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, secret)
                .compact();
        return compactJws;
    }

    /**
     * 解密 jwt
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static String parseToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            if (claims != null) {
                return claims.getSubject();
            }
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            System.out.println("jwt过期了");
        }
        return "";
    }

    public static void main(String[] args) {
        String subject = "wo";
        String token = createToken(subject, new Date());
        System.out.println(token);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("原始值：" + parseToken(token));
    }

}