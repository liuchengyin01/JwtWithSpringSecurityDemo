package pers.liuchengyin.security;

import pers.liuchengyin.security.security.TokenManager;

/**
 * @ClassName Test
 * @Description
 * @Author 柳成荫
 * @Date 2021/3/25
 */
public class Test {
    public static void main(String[] args) {
        TokenManager tokenManager = new TokenManager();
        String token = tokenManager.createToken("liuchengyin");
        System.out.println(token);
        System.out.println("-----------------------------");
        String userName = tokenManager.getUserFromToken(token);
        System.out.println(userName);
    }
}
