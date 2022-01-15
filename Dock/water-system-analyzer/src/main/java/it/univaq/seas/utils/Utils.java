/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.seas.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author valerio
 */
public class Utils {
    
    public static Map<String,String> convertJSONStringToMap(String jsonString){
        
        ObjectMapper mapper = new ObjectMapper();
   
        try {

            Map<String, String> map = mapper.readValue(jsonString, Map.class);

            System.out.println(map);
            return map;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
