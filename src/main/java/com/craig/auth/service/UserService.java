package com.craig.auth.service;

import com.craig.auth.domain.User;
import com.craig.auth.dto.UserDto;
import com.craig.auth.repository.UserRepository;
import com.craig.auth.util.Md5Util;
import com.craig.auth.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private static int SALT_LENGTH = 6;

    public UserDto createUser(UserDto user){
        User userEntity = new User();
        userEntity.setName(user.getName());
        userEntity.setSalt(RandomUtil.getRandomString(SALT_LENGTH));
        userEntity.setUserPwd(getPwdMd5(user.getPwd(), userEntity.getSalt()));
        try {
            userRepository.insert(userEntity);
            user.setPwd(null);
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean deleteUser(UserDto user){
        User userEntity = new User();
        userEntity.setName(user.getName());
        return userRepository.deleteUser(userEntity);
    }

    public UserDto getUserByPwd(String userName, String pwd){
        User userEntity = userRepository.getUser(userName);
        if(userEntity == null){
            return null;
        }
        String pwdWithSalt = getPwdMd5(pwd,userEntity.getSalt());
        if(pwdWithSalt.equals(userEntity.getUserPwd())){
            UserDto dto = new UserDto();
            dto.setName(userEntity.getName());
            return dto;
        }
        return null;
    }

    private String getPwdMd5(String pwd, String salt){
        String pwdWithSalt = pwd + "_" + salt;
        return Md5Util.encrypt(pwdWithSalt);
    }
}
