package com.pixx.Dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
@Data
public class UserDto {
    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private List<String> role;

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
