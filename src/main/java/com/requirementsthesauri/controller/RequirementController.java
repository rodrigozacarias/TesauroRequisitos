package com.requirementsthesauri.controller;

import com.requirementsthesauri.model.Domain;
import com.requirementsthesauri.model.Requirement;
import com.requirementsthesauri.service.RequirementService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("requirements")
public class RequirementController {

    RequirementService requirementService = new RequirementService();

    @GetMapping("/getAllRequirements")
    public ResponseEntity<?> getAllRequirements(){
        return requirementService.getAllRequirements();
    }

    @PostMapping(path = "/createRequirementsList", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createRequirementsList(@RequestBody List<Requirement> requirements){
        return requirementService.createRequirement(requirements);
    }

    @PostMapping(path = "/createRequirement", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createRequirement(@RequestBody Requirement requirement){
        List<Requirement> requirements  = new ArrayList<>();
        requirements.add(requirement);
        return requirementService.createRequirement(requirements);
    }
}
