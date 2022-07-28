package com.craig.auth.api;

import com.craig.auth.dto.Result;
import com.craig.auth.dto.RoleDto;
import com.craig.auth.dto.UserDto;
import com.craig.auth.service.AuthService;
import com.craig.auth.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController("/role")
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    @RequestMapping(method = RequestMethod.POST)
    public Result<RoleDto> createRole(@RequestBody RoleDto role) {
        RoleDto dto = roleService.createRole(role);
        if (dto == null) {
            return Result.failed("role already exist");
        }
        return Result.success(dto);
    }

    @RequestMapping(value = "/{roleName}", method = RequestMethod.DELETE)
    public Result<Boolean> deleteRole(@PathVariable("roleName") String roleName) {
        RoleDto dto = new RoleDto();
        dto.setName(roleName);
        if (roleService.deleteRole(dto)) {
            return Result.success(true);
        }
        return Result.failed("role doesn't exist");
    }

    @RequestMapping(value = "/{roleName}", method = RequestMethod.PUT)
    public Result<Boolean> addRoleToUser(@PathVariable("roleName") String roleName,
                                         @RequestParam("userName") String userName) {
        RoleDto roleDto = new RoleDto();
        roleDto.setName(roleName);

        UserDto userDto = new UserDto();
        userDto.setName(userName);

        boolean result = roleService.addRoleToUser(roleDto, userDto);
        if (result)
            return Result.success(result);
        return Result.failed("param error");
    }

    @RequestMapping(method = RequestMethod.GET)
    public Result<List<RoleDto>> list(@RequestHeader("authorization") String token, HttpServletResponse response) {
        UserDto user = authService.isValidToken(token);
        if (user == null) {
            response.setStatus(401);
            return Result.failed("invalid token");
        }

        return Result.success(roleService.list());
    }

    @RequestMapping(value = "/{roleName}/check", method = RequestMethod.POST)
    public Result<Boolean> checkRole(@RequestHeader("authorization") String token,
                                     @PathVariable("roleName") String roleName,
                                     HttpServletResponse response) {
        UserDto user = authService.isValidToken(token);
        if (user == null) {
            response.setStatus(401);
            return Result.failed("invalid token");
        }
        RoleDto role = new RoleDto();
        role.setName(roleName);

        return Result.success(roleService.checkRole(role, user));
    }
}
