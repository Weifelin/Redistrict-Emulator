package com.giant.demo.preprocessing;

import com.giant.demo.entities.Demographics;
import com.giant.demo.entities.Precinct;
import com.giant.demo.enums.StateE;
import com.giant.demo.repositories.PrecinctRepository;
import com.giant.demo.services.PreprocessService;
import org.json.simple.parser.ParseException;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.simplify.TopologyPreservingSimplifier;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.springframework.beans.factory.annotation.Autowired;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import javax.management.ObjectInstance;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class PreProcess {
    @Autowired
    private PreprocessService preprocessService;
    private static int counter = 0;

    public PreProcess(){

    }

    public void loadPrecincts() {
        JSONParser parser = new JSONParser();
        Set<Precinct> precincts = new HashSet();

        Object obj = null;
        try {
            obj = parser.parse(new FileReader("src/main/resources/public/newprecincts.json"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jo = (JSONObject) obj;
        Map<Integer, Precinct> precinctMap = new HashMap<>();
        Set<Precinct> allPrecincts = new HashSet<>();
        for (Iterator iterator = jo.keySet().iterator(); iterator.hasNext(); ) {
            String key = (String) iterator.next();
            Map p = (Map) jo.get(key);
            Integer precinctID = Long.valueOf((long) p.get("precinctID")).intValue();
            String name = (String) p.get("name");
            Integer pop = Long.valueOf((long) p.get("pop")).intValue();
            Integer votes = (int) (double) p.get("votes");
            Double demo = (double)p.get("demo") ;
            Double rep = (double) p.get("rep");
            double africanAmerican = (double)p.get("africanAmerican") / (double)pop;
            double asian = (double) p.get("asian") / (double)pop;
            double latinAmerican = (double) p.get("latinAmerican") / (double)pop;
            double white = (double) p.get("white") / (double)pop;
            double other = (double) p.get("other") / (double)pop;
            Demographics demographics = new Demographics(africanAmerican, asian, latinAmerican, white, other, pop);

            JSONArray array = (JSONArray) p.get("neighbor");

            int[] numbers = new int[array.size()];
            int index = 0;
            for (Object o : array) {
                numbers[index++] = (int) (long) o;
            }

            //System.out.println(Arrays.toString(numbers));*/


            Map shape = (Map) p.get("shape");

            //how to read in shape file

            GeometryFactory geometryFactory = new GeometryFactory();
            //Polygon polygon = geometryFactory.createPolygon(shape.get("coordinates"));
            JSONArray coords = (JSONArray) shape.get("coordinates");
            coords = (JSONArray) coords.get(0);





            index = 0;
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
            Geometry polygon = geometryFactory.createPolygon(coordinateSequence);

            StateE stateE = StateE.NJ;

            if (stateE == StateE.VA){
                polygon = new TopologyPreservingSimplifier(polygon).getResultGeometry();
            }


            Precinct precinct = new Precinct(precinctID, name, pop, votes, demo, rep, polygon, demographics, stateE, numbers);
            precinctMap.put(precinctID, precinct);
            allPrecincts.add(precinct);
            preprocessService.savePrecinct(precinct);


        }
        for(Precinct precinct : allPrecincts) {
            Set<Precinct> neighbors = new HashSet<>();
            int[] tempNs = precinct.getTempNs();
            for (int i=0; i<tempNs.length; i++) {
                neighbors.add(precinctMap.get(tempNs[i]));
            }
            precinct.setNeighbours(neighbors);
            preprocessService.savePrecinct(precinct);
            System.out.println(counter++ + " out of " + allPrecincts.size());
            }
        System.out.println("Preprocessing has finished...");
        }
}
