package com.pixx.Repository;

import com.pixx.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface RoleRepo extends JpaRepository<Role, Long> {

    @Query(value = "SELECT * FROM Roles", nativeQuery = true)
    Set<Role> find(List<String> roles);
}
