package com.giant.demo.preprocessing;

import com.giant.demo.entities.Precinct;
import com.giant.demo.repositories.PrecinctRepository;
import org.json.simple.parser.ParseException;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PreProcess {
    @Autowired
    private PrecinctRepository precinctRepository;
    public void loadPrecincts() {
        JSONParser parser = new JSONParser();
        Set<Precinct> precincts = new HashSet();

        Object obj = null;
        try {
            obj = parser.parse(new FileReader("precincts.json"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jo = (JSONObject) obj;
        for (Iterator iterator = jo.keySet().iterator(); iterator.hasNext(); ) {
            String key = (String) iterator.next();
            Map p = (Map) jo.get(key);
            Integer precinctID = (int) (long) p.get("precinctID");
            String name = (String) p.get("name");
            Integer pop = (int) (long) p.get("pop");
            Integer votes = (int) (double) p.get("votes");
            Double demo = (double) p.get("demo");
            Double rep = (double) p.get("rep");

            Map shape = (Map) p.get("shape");

            //how to read in shape file

            GeometryFactory geometryFactory = new GeometryFactory();
            //Polygon polygon = geometryFactory.createPolygon(shape.get("coordinates"));



            //System.out.println(polygon.npoints);


            //Precinct precinct = new Precinct(precinctID, name, pop, votes, demo, rep, polygon);
            //precinctRepository.save(precinct);
        }
    }

}
