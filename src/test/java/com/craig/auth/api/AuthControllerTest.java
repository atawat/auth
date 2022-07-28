package com.craig.auth.api;

import com.craig.auth.App;
import com.craig.auth.dto.LoginDto;
import com.craig.auth.dto.Result;
import com.craig.auth.dto.UserDto;
import com.craig.auth.service.AuthService;
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

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class AuthControllerTest {

    protected MockMvc mvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    AuthService authService;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void invalidateToken() throws Exception {
        UserDto user = new UserDto();
        user.setName("invalidateTokenUser");
        user.setPwd("invalidateTokenUser");
        createUser(user);

        LoginDto loginDto = new LoginDto();
        loginDto.setUserName(user.getName());
        loginDto.setPassword(user.getPwd());
        Result<String> tokenResult = login(loginDto);
        assertNotNull(tokenResult.getData());

        String url = "/auth/invalidate";

        assertEquals(user.getName(), authService.isValidToken(tokenResult.getData()).getName());

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(url)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .header("authorization", tokenResult.getData())
        ).andReturn();
        assertEquals(200, result.getResponse().getStatus());

        assertNull(authService.isValidToken(tokenResult.getData()));
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
}