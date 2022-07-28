package com.craig.auth.util;

import com.craig.auth.domain.Role;
import com.craig.auth.dto.RoleDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class JsonUtilTest {

    @Test
    public void toJson() throws IOException {
        RoleDto role = new RoleDto();
        role.setName("testRole");
        String json = JsonUtil.toJson(role);

        RoleDto parseObj = JsonUtil.parse(json, RoleDto.class);

        assertNotNull(parseObj);

        assertEquals("testRole", parseObj.getName());
    }
}