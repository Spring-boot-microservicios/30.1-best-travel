package com.angelfg.best_travel.api.controllers;

import com.angelfg.best_travel.infraestructure.abstract_mongo_services.ModifyUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(path = "users")
@AllArgsConstructor
@Tag(name = "Users")
public class AppUserController {

    private final ModifyUserService modifyUserService;

    @Operation(summary = "Enabled or disabled user")
    @PatchMapping(path = "enabled-or-disabled")
    public ResponseEntity<Map<String, Boolean>> enabledOrDisabled(@RequestParam String username) {
        return ResponseEntity.ok(this.modifyUserService.enabled(username));
    }

    @Operation(summary = "Add role user")
    @PatchMapping(path = "add-role")
    public ResponseEntity<Map<String, Set<String>>> addRole(@RequestParam String username, @RequestParam String role) {
        return ResponseEntity.ok(this.modifyUserService.addRole(username, role));
    }

    @Operation(summary = "Add role user")
    @PatchMapping(path = "remove-role")
    public ResponseEntity<Map<String, Set<String>>> removeRole(@RequestParam String username, @RequestParam String role) {
        return ResponseEntity.ok(this.modifyUserService.removeRole(username, role));
    }

}
