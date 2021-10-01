package com.yawen.blog.utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {
    private static final String JWT_TOKEN = "123456Mszl!@#$$";

    public static String createToken(Long userId){
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",userId);
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,JWT_TOKEN)//签发算法，密钥为token
                .setClaims(claims)//body数据，要唯一，自行设置
                .setIssuedAt(new Date())//设置签发时间
                .setExpiration(new Date(System.currentTimeMillis()+24*60*60*1000));//一天的有效时间
        String token = jwtBuilder.compact();
        return token;
    }

    public static Map<String,Object> checkToken(String token){
        try{
            Jwt parse = Jwts.parser().setSigningKey(JWT_TOKEN).parse(token);
            return (Map<String, Object>) parse.getBody();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String token = JWTUtils.createToken(1234L);
        Map<String, Object> stringObjectMap = JWTUtils.checkToken("TOKEN_eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MjkwMzA1MTUsInVzZXJJZCI6bnVsbCwiaWF0IjoxNjI4OTQ0MTE1fQ.6NtDFeRa4NFpAKsKL6jCPngJMRIEES4Yh4dIVEMFi-g");
        System.out.println(token);
        System.out.println(stringObjectMap.get("userId"));
    }
}
