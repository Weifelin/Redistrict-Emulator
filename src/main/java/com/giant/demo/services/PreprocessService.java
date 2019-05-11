package com.giant.demo.services;

import com.giant.demo.entities.Precinct;
import com.giant.demo.repositories.DemoGraphicsRepository;
import com.giant.demo.repositories.PrecinctRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PreprocessService {
    @Autowired
    private PrecinctRepository precinctRepository;
    @Autowired
    private DemoGraphicsRepository demoGraphicsRepository;

    public void savePrecinct(Precinct precinct){
        demoGraphicsRepository.save(precinct.getDemogrpahics());
        precinctRepository.save(precinct);
    }
}
