package com.craig.auth.service;

import com.craig.auth.dto.TokenDto;
import com.craig.auth.dto.UserDto;
import com.craig.auth.util.AESUtil;
import com.craig.auth.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {
    @Autowired
    private UserService userService;

    private Map<String, String> tokenDb = new ConcurrentHashMap<>(); //mock token db

    @Value("${APP.CONFIG.SECRET:B5XBO440LiUcA41p7pSucl43187C0R19MaXIsiCundgWiB5bb8C3S5SDnFVLYKbRdqjQJj300fA8z9ZCeuE0NvDYh17loeBCN9S5HPy84HA59244J157Af3k5bnZ76xo}")
    private String secret; //do not use default secret,it's just for test

    @Value("${APP.CONFIG.TOKEN.EXPIRE:2}")
    private int expiredHour;

    public UserDto isValidToken(String token) {
        try {
            String decryptContent = AESUtil.decrypt(Base64.getDecoder().decode(token), secret);
            TokenDto user = JsonUtil.parse(decryptContent, TokenDto.class);
            if(user == null){
                return null;
            }
            Calendar now = Calendar.getInstance();

            Calendar expiredTime = Calendar.getInstance();
            expiredTime.setTime(user.getCreateTime());
            expiredTime.add(Calendar.HOUR, expiredHour);

            if(now.compareTo(expiredTime) >0){
                //expired
                tokenDb.put(user.getName(), "");
                return null;
            }

            String storedToken = tokenDb.get(user.getName());
            if(storedToken == null){
                return null;
            }
            if(storedToken.equals(token)){
                return user;
            }
            return  null;
        } catch (Exception e) {
            return null;
        }
    }

    public String authenticate(String userName, String pwd) {
        try {
            UserDto user = userService.getUserByPwd(userName, pwd);
            if (user == null) {
                return null;
            }

            TokenDto tokenPairs = new TokenDto();
            tokenPairs.setCreateTime(new Date());
            tokenPairs.setName(user.getName());

            String content = JsonUtil.toJson(tokenPairs);
            String token = Base64.getEncoder().encodeToString(AESUtil.encrypt(content, secret));
            tokenDb.put(user.getName(), token);
            return token;
        } catch (Exception e) {
            return null;
        }
    }

    public void invalidToken(String token){
        UserDto user = isValidToken(token);
        if(user == null){
            return;
        }

        tokenDb.put(user.getName(), "");
    }
}
