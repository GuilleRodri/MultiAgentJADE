/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica.Ontology;

import jade.content.Predicate;

/**
 *
 * @author eric.dacal
 */
public class At implements Predicate{
    int section;
    public int getSection() { 
        return section; 
    } 
    public void setSection(int section) { 
        this.section = section; 
    } 
    
}
