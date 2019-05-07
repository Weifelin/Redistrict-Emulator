package com.giant.demo.repositories;

import com.giant.demo.entities.Precinct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrecinctRepository extends JpaRepository<Precinct, String> {
}
