/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.disim.seas.watersystemplanner;

import org.springframework.web.bind.annotation.GetMapping;


/**
 *
 * @author valerio
 *
 **/
@org.springframework.web.bind.annotation.RestController
public class RestController {
    
    @GetMapping("/")
	public String greeting() {
		return "Hello cazzone 12";
	}
    
}
