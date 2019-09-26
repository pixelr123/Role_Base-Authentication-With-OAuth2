package com.pixx.Service;

import com.pixx.Dto.UserDto;
import com.pixx.Model.Role;
import com.pixx.Model.RoleType;
import com.pixx.Model.User;
import com.pixx.Repository.RoleRepo;
import com.pixx.Repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Validated
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(userId);
        if(user == null){
            log.error("Invalid username or password");
            throw new UsernameNotFoundException("Invalid username or password");
        }

        Set<GrantedAuthority> grantedAuthorities = getAuthorities(user);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }

    private Set<GrantedAuthority> getAuthorities(User user){
        Set<Role> roleByuserId = user.getRoles();
        final Set<GrantedAuthority> authorities = roleByuserId.stream().map(role -> new SimpleGrantedAuthority(("ROLE_" + role.getName().toString().toUpperCase()))).collect(Collectors.toSet());
        return authorities;
    }

    public List<UserDto> findAll(){
        List<UserDto> users = new ArrayList<>();
        userRepo.findAll().iterator().forEachRemaining(user -> users.add(user.toUserDto()));
        return users;
    }

    public User findOne(long id){
        return userRepo.findById(id).get();
    }

    public void delete(long id){
        userRepo.deleteById(id);
    }


    public UserDto save(UserDto userDto) throws ConstraintViolationException {
        User userWithDuplicateUsername = userRepo.findByUsername(userDto.getUsername());
        if(userWithDuplicateUsername != null && userDto.getId() != userWithDuplicateUsername.getId()) {
            log.error(String.format("Duplicate username", userDto.getUsername()));
            throw new RuntimeException("Duplicate username.");
        }
        User userWithDuplicateEmail = userRepo.findByEmail(userDto.getEmail());
        if(userWithDuplicateEmail != null && userDto.getId() != userWithDuplicateEmail.getId()) {
            log.error(String.format("Duplicate email", userDto.getEmail()));
            throw new RuntimeException("Duplicate email.");
        }
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        List<RoleType> roleTypes = new ArrayList<>();
        userDto.getRole().stream().map(role -> roleTypes.add(RoleType.valueOf(role)));
        user.setRoles(roleRepo.find(userDto.getRole()));
        userRepo.save(user);
        return userDto;
    }
}
