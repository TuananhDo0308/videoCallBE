package com.example.demo.Controller;

import com.example.demo.Model.Role;
import com.example.demo.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    RoleRepository roleRepository;

    @PostMapping("/addRole")
    public Role addRole(@RequestBody Role role) {
        return roleRepository.save(role);
    }

    @GetMapping("")
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
