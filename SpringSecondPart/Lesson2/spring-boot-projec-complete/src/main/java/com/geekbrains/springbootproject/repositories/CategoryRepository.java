package com.geekbrains.springbootproject.repositories;

import com.geekbrains.springbootproject.entities.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
}
