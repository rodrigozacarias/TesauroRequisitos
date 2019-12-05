package com.requirementsthesauri.controller;

import com.requirementsthesauri.model.Domain;
import com.requirementsthesauri.service.DomainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("domains")
public class DomainController{

    private DomainService domainService = new DomainService();

    @GetMapping
    public ResponseEntity<?> getAllDomains(){
        return new ResponseEntity<>(domainService.getAllDomains(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createDomain(@Valid @RequestBody Domain domain){
        return new ResponseEntity<>(domainService.createDomain(domain), HttpStatus.OK);
    }

}
