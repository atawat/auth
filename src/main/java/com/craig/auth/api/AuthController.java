package com.craig.auth.api;

import com.craig.auth.dto.LoginDto;
import com.craig.auth.dto.Result;
import com.craig.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController("/auth")
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result<String> login(@RequestBody LoginDto loginDto){
        String token = authService.authenticate(loginDto.getUserName(), loginDto.getPassword());
        if(StringUtils.isEmpty(token)){
            return Result.failed("login failed");
        }

        return Result.success(token);
    }

    @RequestMapping(value = "/invalidate", method = RequestMethod.POST)
    public Result<Void> invalidateToken(@RequestHeader("authorization") String token, HttpServletResponse response){
        if(authService.isValidToken(token) == null){
            response.setStatus(401);
            return Result.failed("invalid token");
        }
        authService.invalidToken(token);

        return Result.success(null);
    }
}
