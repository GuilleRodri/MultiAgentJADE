/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica.Ontology;

import jade.content.Concept;


/**
 *
 * @author edacal
 */
public class MassWater implements Concept{
    private float volumen;
    private float SS;
    private float DBO;
    private float DQO;
    private float TN; 
    private float TS;
    
    public MassWater(){ //The constructor builds an empty Mass Water
        volumen = 0f;
        SS = 0f;
        DBO = 0f;
        DQO = 0f;
        TN = 0f;
        TS = 0f;
    }
    
    public float getVolume() {
        return volumen;
    }
    
    public void setVolume(float volumen) {
        this.volumen = volumen;
    }
    public float getSS() {
        return SS;
    }
    public void setSS(float SS) {
        this.SS = SS;
    }
    public float getDBO() {
        return DBO;
    }
    public void setDBO(float DBO) {
        this.DBO = DBO;
    }
    public float getDQO() {
        return DQO;
    }
    public void setDQO(float DQO) {
        this.DQO = DQO;
    }
    public float getTN() {
        return TN;
    }
    public void setTN(float TN) {
        this.TN = TN;
    }
    public float getTS() {
        return TS;
    }
    public void setTS(float TS) {
        this.TS = TS;
    }
    
    public void mixWater(MassWater m1, MassWater m2){
        this.volumen = m1.getVolume()+m2.getVolume();
        float p1 = m1.getVolume()/volumen;
        float p2 = m2.getVolume()/volumen;
        this.DBO = p1*m1.getDBO()+p2*m2.getDBO();
        this.DQO = p1*m1.getDQO()+p2*m2.getDQO();
        this.SS = p1*m1.getSS()+p2*m2.getSS();
        this.TN = p1*m1.getTN()+p2*m2.getTN();
        this.TS = p1*m1.getTS()+p2*m2.getTS();
    }
}
