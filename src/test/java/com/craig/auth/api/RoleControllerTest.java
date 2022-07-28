package com.craig.auth.api;

import com.craig.auth.App;
import com.craig.auth.dto.LoginDto;
import com.craig.auth.dto.Result;
import com.craig.auth.dto.RoleDto;
import com.craig.auth.dto.UserDto;
import com.craig.auth.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class RoleControllerTest {


    protected MockMvc mvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void createRole() throws Exception {
        createRole("test-role1");
    }

    private void createRole(String roleName) throws Exception {
        String url = "/role";
        RoleDto role = new RoleDto();
        role.setName(roleName);
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(url)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(role))
        ).andReturn();
        assertEquals(200, result.getResponse().getStatus());

        Result<RoleDto> response = JsonUtil.parse(result.getResponse().getContentAsString(), new TypeReference<Result<RoleDto>>() {
        });

        assertTrue(response.isSuccess());
        assertEquals(roleName, response.getData().getName());
    }

    @Test
    public void deleteRole() throws Exception {
        createRole("test-role-delete");

        Result<Boolean> response = deleteRole("test-role-delete");

        assertTrue(response.isSuccess());
        assertEquals(true, response.getData());

        Result<Boolean> notExistResponse = deleteRole("not-exist");

        assertFalse(notExistResponse.isSuccess());
        assertEquals("role doesn't exist", notExistResponse.getMsg());
    }

    private Result<Boolean> deleteRole(String roleName) throws Exception {
        String url = "/role/" + roleName;
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .delete(url)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        assertEquals(200, result.getResponse().getStatus());

        Result<Boolean> response = JsonUtil.parse(result.getResponse().getContentAsString(), new TypeReference<Result<Boolean>>() {
        });
        return response;
    }

    @Test
    public void addRoleToUser() throws Exception {
        createRole("bind-role1");
        createRole("bind-role2");
        UserDto user = new UserDto();
        user.setName("bind-user1");
        user.setPwd("bind-user1");
        createUser(user);

        Result<Boolean> response1 = addRoleToUser("bind-role1", "bind-user1");
        Result<Boolean> response2 = addRoleToUser("bind-role2", "bind-user1");

        assertTrue(response1.isSuccess());
        assertTrue(response2.isSuccess());
        Result<Boolean> response3 = addRoleToUser("bind-role-not-exist", "bind-user1");

        assertFalse(response3.isSuccess());
    }

    @Test
    public void list() throws Exception {
        UserDto user = new UserDto();
        user.setName("listViewer");
        user.setPwd("listViewer");
        createUser(user);

        createRole("list-role1");
        createRole("list-role2");

        LoginDto loginDto = new LoginDto();
        loginDto.setUserName(user.getName());
        loginDto.setPassword(user.getPwd());
        Result<String> response = login(loginDto);

        assertNotNull(response.getData());


        MvcResult wrongResponse = getRoles("wrong token");
        assertEquals(401, wrongResponse.getResponse().getStatus());

        MvcResult result = getRoles(response.getData());
        assertEquals(200, result.getResponse().getStatus());
        Result<List<RoleDto>> listResponse = JsonUtil.parse(result.getResponse().getContentAsString(), new TypeReference<Result<List<RoleDto>>>() {
        });

        assertTrue(listResponse.isSuccess());
        assertTrue(listResponse.getData().size() >= 2);
    }

    private MvcResult getRoles(String token) throws Exception {
        String url = "/role";
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get(url)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .header("authorization", token)
        ).andReturn();

        return result;
    }

    @Test
    public void checkRole() throws Exception {
        createRole("check-role1");
        UserDto user = new UserDto();
        user.setName("checkUser1");
        user.setPwd("checkUser1");
        createUser(user);

        addRoleToUser("check-role1", "checkUser1");

        LoginDto loginDto = new LoginDto();
        loginDto.setUserName(user.getPwd());
        loginDto.setPassword(user.getName());
        Result<String> tokenResult = login(loginDto);

        assertNotNull(tokenResult.getData());

        Result<Boolean> result = checkRole(tokenResult.getData(), "check-role1");
        assertTrue(result.isSuccess());
    }

    private Result<Boolean> checkRole(String token, String roleName) throws Exception {
        String url = "/role/" + roleName + "/check";

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(url)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .header("authorization", token)
        ).andReturn();
        assertEquals(200, result.getResponse().getStatus());

        Result<Boolean> response = JsonUtil.parse(result.getResponse().getContentAsString(), new TypeReference<Result<String>>() {
        });

        return response;
    }

    private Result<String> login(LoginDto login) throws Exception {
        String url = "/auth/login";

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(url)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(login))
        ).andReturn();
        assertEquals(200, result.getResponse().getStatus());

        Result<String> response = JsonUtil.parse(result.getResponse().getContentAsString(), new TypeReference<Result<String>>() {
        });
        return response;
    }

    private Result<UserDto> createUser(UserDto user) throws Exception {
        String url = "/user";

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(url)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(user))
        ).andReturn();
        assertEquals(200, result.getResponse().getStatus());

        Result<UserDto> response = JsonUtil.parse(result.getResponse().getContentAsString(), new TypeReference<Result<UserDto>>() {
        });
        return response;
    }

    private Result<Boolean> addRoleToUser(String roleName, String userName) throws Exception {
        String url = "/role/" + roleName;
        url += "?userName=" + userName;
        RoleDto role = new RoleDto();
        role.setName(roleName);
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .put(url)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        assertEquals(200, result.getResponse().getStatus());

        Result<Boolean> response = JsonUtil.parse(result.getResponse().getContentAsString(), new TypeReference<Result<Boolean>>() {
        });

        return response;
    }
}