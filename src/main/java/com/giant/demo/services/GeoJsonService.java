package com.giant.demo.services;

import com.giant.demo.entities.Precinct;
import com.giant.demo.enums.StateE;
import com.giant.demo.repositories.PrecinctRepository;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

@Service
public class GeoJsonService {
    @Autowired
    private PrecinctRepository precinctRepository;

    public void createGeoJson(){

        StringBuffer stringBuffer = new StringBuffer();
        List<Precinct> precinctList = precinctRepository.findAllByState(StateE.VA);
        ListIterator<Precinct> iterator = precinctList.listIterator();

        String head = "{" + "\n" + "\"type:\": \"FeatureCollection\",\n"+"\"features\": [";
        String tail = "}";
        stringBuffer.append(head);
        int size = precinctList.size();
        int counter = 0;
        while(iterator.hasNext()){
            String precinctComma = ","; /*Last Precinct don't have it*/
            if (iterator.nextIndex() == size-1){
                precinctComma = "";
            }
            Precinct precinct = iterator.next();
            Coordinate[] coordinates = precinct.getBoundaries().getCoordinates();
            StringBuffer precinctBuffer = new StringBuffer();

            String precinctHead = "{\"type\": \"Feature\",\n";
            String geoHead = " \"geometry\": {\n";
            String coordinateHead=  "\"type\": \"Polygon\",\n"+" \"coordinates\": [";
            String coordinateTail = "]";
            String geoTail = "},";

            String propertyHead = "\"properties\": {\n";
            StringBuffer propertyContent = propertyToString(precinct);
            String propertyTail = "}";

            String precinctTail = "}";

            StringBuffer coordinatesString = coordinatesArrayToString(coordinates);
            precinctBuffer.append(precinctHead);
            precinctBuffer.append(geoHead);
            precinctBuffer.append(coordinateHead);
            precinctBuffer.append(coordinatesString);
            precinctBuffer.append(coordinateTail);
            precinctBuffer.append(geoTail);
            precinctBuffer.append(propertyHead);
            precinctBuffer.append(propertyContent);
            precinctBuffer.append(propertyTail);
            precinctBuffer.append(precinctTail);
            precinctBuffer.append(precinctComma);
            stringBuffer.append(precinctBuffer);
            System.out.println(counter++);
        }
        stringBuffer.append("]"); //feature tail
        stringBuffer.append(tail);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("/Users/Red/Documents/GitHub/Giant/demo/src/main/resources/public/VAGeoPrecincts.json")));
            writer.write(stringBuffer.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("errors");
        }



    }

    private StringBuffer propertyToString(Precinct precinct) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\"precinctID\": "+precinct.getPrecinctID()+",\n");
//        stringBuffer.append("\"state\": "+"\""+precinct.getState().toString()+"\""+",\n");
        stringBuffer.append("\"population\": "+precinct.getPopulation()+",\n");
        stringBuffer.append("\"numDemo\": "+precinct.getNumDemo()+",\n");
        stringBuffer.append("\"numRep\": "+precinct.getNumRep()+",\n");
        stringBuffer.append("\"votes\": "+precinct.getVotes()+",\n");
        stringBuffer.append("\"name\": "+"\""+precinct.getName()+"\",\n");
        stringBuffer.append("\"africanAmerican\": "+precinct.getDemogrpahics().getAfricanAmerican()+",\n");
        stringBuffer.append("\"asian\": "+precinct.getDemogrpahics().getAsian()+",\n");
        stringBuffer.append("\"latinAmerican\": "+precinct.getDemogrpahics().getLatinAmerican()+",\n");
        stringBuffer.append("\"white\": "+precinct.getDemogrpahics().getWhite()+",\n");
        stringBuffer.append("\"other\": "+precinct.getDemogrpahics().getOther()+"\n");

        return stringBuffer;
    }

    public String coordinateToString(Coordinate coordinate){
        return "["+coordinate.getX()+","+coordinate.getY()+"]";
    }

    public StringBuffer coordinatesArrayToString(Coordinate[] coordinates){
        StringBuffer stringBuffer = new StringBuffer();
        int size = coordinates.length;
        int i;
        stringBuffer.append("[");
        for (i=0; i<size-1; i++){
            String string = coordinateToString(coordinates[i]);
            stringBuffer.append(string+",\n");
        }
        String string = coordinateToString(coordinates[i])+"\n";
        stringBuffer.append(string+"]");
        return stringBuffer;
    }

}
