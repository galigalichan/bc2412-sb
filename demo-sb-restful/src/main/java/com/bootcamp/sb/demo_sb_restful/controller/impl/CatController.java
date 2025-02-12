package com.bootcamp.sb.demo_sb_restful.controller.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.sb.demo_sb_restful.controller.CatOperation;
import com.bootcamp.sb.demo_sb_restful.model.Cat;
import com.bootcamp.sb.demo_sb_restful.model.CatDatabase;
import com.bootcamp.sb.demo_sb_restful.service.CatService;


// ! Restful API -> GET/POST/DELETE/PUT/PATCH
// Control single resource by GET/POST/DELETE/PUT/PATCH

// Controller -> The ways to control Cat resource
// insert, update, delete, select
@RestController
public class CatController implements CatOperation {
    // Controller -> Service -> CatDatbase

    // Dependency Injection (Spring Core Concept)
    // Autowired: Try to find an object which fits into catService. (Beofe Server start complete)
    // ! If fail, server start fail.

    // Field Injection
    @Autowired
    private CatService catService;

    // Construction Injection
    // @Autowired
    // public CatController(CatService catService) {
    //     this.catService = catService;
    // }

    @Override    
    public Cat createCat(Cat cat) {
        if (this.catService.put(cat)) // Null pointer exception? // Spring Boot ensures everything is all right before starting the server -> no null cases will occur
            return cat;
        return null;
    } // unit test: return true -> cat; return false -> null

    // Arrays.asList() vs List.of() vs new ArrayList<>()

    // Get All Cats
    public List<Cat> getCats() {
        return Arrays.asList(CatDatabase.HOME);
    }

    // Get Cat by id
    // http://localhost:8082/cat?id=1
    // Deserialization
    public Cat getCat(Long id) {
        new Cat(id, null, null);
        return CatDatabase.find(id).orElse(null);
    }

    // http://localhost:8082/cat?id=1
    public Boolean deleteCat(Long id) {
        return CatDatabase.delete(id);
    }

    // HashMap.put() -> if exists, override, otherwise, create new
    public Boolean updateCat(Long id, Cat cat) {
        return CatDatabase.update(id, cat);
    }

    public Boolean patchCatName(Long id, String name) {
        return CatDatabase.patchName(id, name);
    }
    
}
