package com.bootcamp.sb.demo_sb_restful.model;

import java.util.Optional;

public class CatDatabase {
    public static final Cat[] HOME = new Cat[5];

    public static boolean put(Cat cat) {
        for (int i = 0; i < HOME.length; i++) {
          if (HOME[i] == null) {
            HOME[i] = cat;
            return true;
          }
        }
        return false;
    }

    public static Optional<Cat> find(Long id) {
      for (Cat cat : HOME) {
        if (cat.getId() == id)
          return Optional.of(cat);
      }
      return Optional.empty();
    }

    public static Boolean delete(Long id) {
      for (int i = 0; i < HOME.length; i++) {
        if (HOME[i].getId() == id) {
          HOME[i] = null;
          return true;
        }
      }
      return false;
    }

    public static Boolean update(Long id, Cat cat) {
      for (int i = 0; i < HOME.length; i++) {
        if (HOME[i].getId() == id) {
          HOME[i] = cat;
          return true;
        }
      }
      return false;
    }

    // ! 1. Don't create cat, we should find the cat object, call setName()
    // ! 2. Other vales of this cat object remain unchanged.
    public static Boolean patchName(Long id, String catName) {
      for (Cat cat : HOME) {
        if (cat.getId() == id) 
          cat.setName(catName);
          return true;
      }
      return false;
    }
}
