package model;

import java.io.Serializable;

public class Volunteer implements Entity<Integer>, Serializable {
    private Integer id;
    private String username;
    private String name;
    private String password;

    public Volunteer(String username, String name, String password) {
        this.username = username;
        this.name = name;
        this.password = password;
    }

    public Volunteer(Integer id, String username, String name, String password) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id=id;
    }

    @Override
    public String toString() {
        return "Volunteer{" +
                "id=" + id +
                '}';
    }
}
