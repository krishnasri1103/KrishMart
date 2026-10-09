package com.krishna.krishmart.model;

import java.sql.Timestamp;

/** User entity stored in the users table. */
public class User {
    private long id;
    private String name;
    private String email;
    private String passwordHash;
    private Role role;
    private Timestamp createdAt;
    public User() {}
    public User(long id, String name, String email, String passwordHash, Role role, Timestamp createdAt) {
        this.id=id; this.name=name; this.email=email; this.passwordHash=passwordHash; this.role=role; this.createdAt=createdAt;
    }
    public long getId(){return id;} public void setId(long v){id=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getPasswordHash(){return passwordHash;} public void setPasswordHash(String v){passwordHash=v;}
    public Role getRole(){return role;} public void setRole(Role v){role=v;}
    public Timestamp getCreatedAt(){return createdAt;} public void setCreatedAt(Timestamp v){createdAt=v;}
}