package com.bootcamp.sb.demo_sb_restful.service.impl;

import org.springframework.stereotype.Service;

import com.bootcamp.sb.demo_sb_restful.model.Cat;
import com.bootcamp.sb.demo_sb_restful.model.CatDatabase;
import com.bootcamp.sb.demo_sb_restful.service.CatService;

// Person.class -> name -> setName() -> getName() -> state
// Person.class -> weight / height -> bmi() -> state

@Service // Bean
public class CatServiceImpl implements CatService{
    // stateless object, can be a bean
    @Override
    public boolean put(Cat cat) {
        for (int i = 0; i < CatDatabase.HOME.length; i++) {
            if (CatDatabase.HOME[i] == null) {
                CatDatabase.HOME[i] = cat;
                return true;
            }
        }
        return false;
    }
}
