package top.kuoer.shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

public class JwtUtil {

    private static final String SECRET = "qq1275886165";

    /**
     * 生成token
     * @param map 要加密的信息
     * @return 令牌
     */
    public static String sign(Map<String, String> map) {
        //设置过期时间 默认3天
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 3);
        JWTCreator.Builder builder = JWT.create();

        //payload
        map.forEach(builder::withClaim);
        //指定令牌过期时间与加密
        return builder.withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * 验证token合法性/获取token全部信息
     * @param token 令牌
     * @return verify
     */
    public static DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }

    /**
     * 获取token单个信息
     * @param token 令牌
     * @param key
     * @return value
     */
    public static String getInfo(String token, String key) {
        try {
            DecodedJWT verify = verify(token);
            return verify.getClaim(key).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

}