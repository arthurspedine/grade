package com.use3w.grade.service;

import com.use3w.grade.model.ECategory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

@Service
public class CategoryService {

    public Map<ECategory, String> listCategories() {
        ECategory[] categories = ECategory.values();
        Map<ECategory, String> result = new TreeMap<>();
        for (ECategory category : categories) {
            result.put(category, category.getDescription());
        }
        return result;
    }
}
