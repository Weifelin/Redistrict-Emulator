package com.giant.demo.repositories;

import com.giant.demo.entities.Demographics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemoGraphicsRepository extends JpaRepository<Demographics, Integer> {
}
