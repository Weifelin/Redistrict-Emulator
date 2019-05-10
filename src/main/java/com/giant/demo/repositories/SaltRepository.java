package com.giant.demo.repositories;

import com.giant.demo.entities.Salt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaltRepository extends JpaRepository<Salt, Integer> {

}
