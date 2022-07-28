package com.craig.auth.api;

import com.craig.auth.App;
import com.craig.auth.dto.Result;
import com.craig.auth.dto.UserDto;
import com.craig.auth.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Before;
import org.junit.BeforeClass;
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
public class UserControllerTest {

    protected MockMvc mvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void create() throws Exception {
        UserDto user = new UserDto();
        user.setName("test");
        user.setPwd("123");
        Result<UserDto> response = createUser(user);

        assertEquals(true, response.isSuccess());
        assertEquals("test", response.getData().getName());
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

    @Test
    public void delete() throws Exception {

        UserDto user = new UserDto();
        user.setName("test-to-delete");
        user.setPwd("123");
        createUser(user);

        String url = "/user/test-to-delete";
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .delete(url)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());

        Result<Boolean> response = JsonUtil.parse(result.getResponse().getContentAsString(), new TypeReference<Result<Boolean>>() {
        });

        assertTrue(response.isSuccess());
        assertEquals(true, response.getData());
    }

    @Test
    public void deleteNotExist() throws Exception {
        String url = "/user/test-not-exist";
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .delete(url)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());

        Result<Boolean> response = JsonUtil.parse(result.getResponse().getContentAsString(), new TypeReference<Result<Boolean>>() {
        });

        assertFalse(response.isSuccess());
        assertEquals("user doesn't exist", response.getMsg());
    }
}