/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.seas.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.univaq.seas.model.SymptomsMessage;

/**
 *
 * @author valerio
 */
public class Utils {

    public static boolean dockerized = true;

    public static String convertMessageToJSONString(SymptomsMessage message){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);
        } catch (JsonProcessingException ex) {
            // Do notting
        }
        return null;
    }
}
