/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica.Ontology;

import jade.content.AgentAction;

/**
 *
 * @author eric.dacal
 */
public class TakeWater implements AgentAction {
    private int section;
    private float volume;
    
    public void setSection(int section) {
        this.section = section;
    }
    
    public int getSection() {
        return section;
    }
    
    public void setVolume(float volume) {
        this.volume = volume;
    }
    
    public float getVolume() {
        return volume;
    }
}
