package com.requirementsthesauri.controller;

import com.requirementsthesauri.model.Domain;
import com.requirementsthesauri.service.DomainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("domains")
public class DomainController{

    private DomainService domainService = new DomainService();

    @GetMapping
    public ResponseEntity<?> getAllDomains(){
        return new ResponseEntity<>(domainService.getAllDomains(), HttpStatus.OK);
    }

    @PostMapping(path = "/createDomainsList")
    public ResponseEntity<?> createDomainList(@RequestBody List<Domain> domains){
        return new ResponseEntity<>(domainService.createDomain(domains), HttpStatus.OK);
    }

    @PostMapping(path = "/createDomain")
    public ResponseEntity<?> createDomain(@RequestBody Domain domain){
        List<Domain> domains  = new ArrayList<>();
        domains.add(domain);
        return new ResponseEntity<>(domainService.createDomain(domains), HttpStatus.OK);
    }

}
