package com.craig.auth.api;

import com.craig.auth.dto.Result;
import com.craig.auth.dto.UserDto;
import com.craig.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public Result<UserDto> create(@RequestBody UserDto user){
        UserDto dto = userService.createUser(user);
        if(dto == null){
          return Result.failed("user already exist");
        }
        return Result.success(dto);
    }

    @RequestMapping(value = "/user/{userName}", method = RequestMethod.DELETE)
    public Result<Boolean> delete(@PathVariable("userName") String userName){
        UserDto dto = new UserDto();
        dto.setName(userName);
        boolean result = userService.deleteUser(dto);
        if(result){
            return Result.success(result);
        }
        return Result.failed("user doesn't exist");
    }
}
