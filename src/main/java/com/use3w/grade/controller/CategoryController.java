package com.use3w.grade.controller;

import com.use3w.grade.model.ECategory;
import com.use3w.grade.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public ResponseEntity<Map<ECategory, String>> listCategories() {
        return ResponseEntity.ok(service.listCategories());
    }
}
