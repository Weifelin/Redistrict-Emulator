package com.giant.demo.repositories;

import com.giant.demo.entities.Precinct;
import com.giant.demo.enums.StateE;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrecinctRepository extends JpaRepository<Precinct, Integer> {
    List<Precinct> findAllByState(StateE state);
}
