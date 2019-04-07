package practica.Agents;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import jade.core.Agent;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import practica.Views.MainView;

/**
 *
 * @author edacal
 */
public class Main extends Agent{
    @Override
    protected void setup() {
        MainView mv;
        try {
            mv = new MainView();
            mv.setVisible(true);
            mv.setExtendedState(MAXIMIZED_BOTH);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
