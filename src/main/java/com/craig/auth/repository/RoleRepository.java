package com.craig.auth.repository;

import com.craig.auth.domain.Role;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * mock persistence layer, store date in map;
 */
@Repository
public class RoleRepository {
    Map<String, Role> roleDb = new ConcurrentHashMap<>();

    public Role get(String roleName) {
        return roleDb.get(roleName);
    }

    public Role insert(Role role) {
        if (role == null) {
            throw new RuntimeException("invalid op, argument can't be null");
        }
        if (roleDb.containsKey(role.getName())) {
            throw new RuntimeException("role name " + role.getName() + " already exist");
        }
        roleDb.putIfAbsent(role.getName(), role);

        return role;
    }

    public boolean deleteRole(Role role) {
        if (roleDb.containsKey(role.getName())) {
            roleDb.remove(role.getName());
            return true;
        }
        return false;
    }

    public List<Role> list() {
        return new ArrayList<>(roleDb.values());
    }
}
