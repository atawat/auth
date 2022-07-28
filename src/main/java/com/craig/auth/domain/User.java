package com.craig.auth.domain;

import java.util.List;

/*
 * user entity
 */
public class User {

    private String name;

    /**
     * MD5(pwd + salt)
     */
    private String userPwd;

    private String salt;

    /**
     *  1 user has many roles
     */
    private List<Role> roles;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
