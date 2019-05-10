package com.giant.demo.preprocessing;

import com.giant.demo.entities.Precinct;
import com.giant.demo.repositories.PrecinctRepository;
import org.json.simple.parser.ParseException;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
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

    public PreProcess(){
        this.precinctRepository = null;
        this.loadPrecincts();
    }

    public void loadPrecincts() {
        JSONParser parser = new JSONParser();
        Set<Precinct> precincts = new HashSet();

        Object obj = null;
        try {
            obj = parser.parse(new FileReader("C:\\Users\\wwalt\\OneDrive\\Documents\\GitHub\\demo\\src\\main\\resources\\public\\precincts.json"));
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
            JSONArray coords = (JSONArray) shape.get("coordinates");
            coords = (JSONArray) coords.get(0);




            int index = 0;
            if(coords.size() == 1){
                coords = (JSONArray) coords.get(0);
            }
            Coordinate[] coordinates = new Coordinate[coords.size()];
            for(Object object : coords){
                JSONArray coord = (JSONArray) object;
                double x = (double) coord.get(0);
                double y = (double) coord.get(1);
                coordinates[index++] = new Coordinate(x, y);

            }
            CoordinateSequence coordinateSequence = new CoordinateArraySequence(coordinates);
            Polygon polygon = geometryFactory.createPolygon(coordinateSequence);


            Precinct precinct = new Precinct(precinctID, name, pop, votes, demo, rep, polygon);
            precinctRepository.save(precinct);

        }
    }

}
