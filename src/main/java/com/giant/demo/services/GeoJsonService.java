package com.giant.demo.services;

import com.giant.demo.entities.Precinct;
import com.giant.demo.repositories.PrecinctRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wololo.jts2geojson.GeoJSONWriter;

import java.util.List;
import java.util.ListIterator;

@Service
public class GeoJsonService {
    @Autowired
    private PrecinctRepository precinctRepository;

    public void createGeoJson(){
        List<Precinct> precincts = precinctRepository.findAll();
        ListIterator<Precinct> iterator = precincts.listIterator();

        GeoJSONWriter writer = new GeoJSONWriter();
    }

}
