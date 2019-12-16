package com.requirementsthesauri.controller;

import com.requirementsthesauri.model.SystemType;
import com.requirementsthesauri.service.SystemTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("systemTypes")
public class SystemTypeController {

    SystemTypeService systemTypeService = new SystemTypeService();

    @GetMapping("/getAllSystemTypes")
    public ResponseEntity<?> getAllSystemTypes(){
        return systemTypeService.getAllSystemTypes();
    }

    @GetMapping(value = "/{systemTypeID}", produces = {"application/json",
            "application/xml",
            "application/ld+json",
            "application/n-triples",
            "application/rdf+xml",
            "application/turtle",
            "application/rdf+json"})
    public ResponseEntity<?> getSystemType(@PathVariable(value="systemTypeID") String systemTypeID, @RequestHeader("Accept") String accept){
        return systemTypeService.getSystemType(systemTypeID, accept);
    }

    @PostMapping(path = "/createSystemTypesList", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createSystemTypesList(@RequestBody List<SystemType> systemTypes){
        return systemTypeService.createSystemType(systemTypes);
    }

    @PostMapping(path = "/createSystemType", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createSystemTypesList(@RequestBody SystemType systemType){
        List<SystemType> systemTypes  = new ArrayList<>();
        systemTypes.add(systemType);
        return systemTypeService.createSystemType(systemTypes);
    }

    @PutMapping(value = "/{systemTypeID}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateSystemType(@PathVariable(value="systemTypeID") String systemTypeID, @RequestBody SystemType newSystemType) {
        return systemTypeService.updateSystemType(systemTypeID, newSystemType);
    }

    @DeleteMapping(value = "/{systemTypeID}")
    public ResponseEntity<?> deleteSystemType(@PathVariable(value="systemTypeID") String systemTypeID) {
        systemTypeService.deleteSystemType(systemTypeID);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
