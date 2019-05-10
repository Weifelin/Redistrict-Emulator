package com.giant.demo.services;

import com.giant.demo.entities.Precinct;
import com.giant.demo.repositories.PrecinctRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PreprocessService {
    @Autowired
    private PrecinctRepository precinctRepository;

    public void savePrecinct(Precinct precinct){
        precinctRepository.save(precinct);
    }
}
