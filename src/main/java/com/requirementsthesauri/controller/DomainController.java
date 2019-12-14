package com.requirementsthesauri.controller;

import com.requirementsthesauri.model.Domain;
import com.requirementsthesauri.service.DomainService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("domains")
public class DomainController{

    private DomainService domainService = new DomainService();

    @GetMapping("/getAllDomains")
    public ResponseEntity<?> getAllDomains(){
        return domainService.getAllDomains();
    }

    @GetMapping(value = "/{domainID}", produces = {"application/json",
            "application/ld+json",
            "application/n-triples",
            "application/rdf+xml",
            "application/turtle",
            "application/rdf+json"})
    public ResponseEntity<?> getDomain(@PathVariable(value="domainID") String domainID, @RequestHeader("Accept") String accept){
        return domainService.getDomain(domainID, accept);
    }


    @PostMapping(path = "/createDomainsList", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createDomainList(@RequestBody List<Domain> domains){
        return domainService.createDomain(domains);
    }

    @PostMapping(path = "/createDomain", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createDomain(@RequestBody Domain domain){
        List<Domain> domains  = new ArrayList<>();
        domains.add(domain);
        return domainService.createDomain(domains);
    }

    @PutMapping(value = "/{domainID}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateDomain(@PathVariable(value="domainID") String domainID, @RequestBody Domain newDomain) {
        return domainService.updateDomain(domainID, newDomain);
    }

}
