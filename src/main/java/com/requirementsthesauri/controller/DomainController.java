package com.requirementsthesauri.controller;

import com.requirementsthesauri.service.DomainService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.json.JsonObject;
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
    public ResponseEntity<?> createDomain(JsonObject domains){
        return new ResponseEntity<>(domainService.createDomain(domains), HttpStatus.OK);
    }

}
