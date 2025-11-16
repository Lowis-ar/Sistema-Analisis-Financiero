/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anf175;

import controlador.ControladorFiador;
import vista.VistaFiador;

/**
 *
 * @author Kevin
 */
public class TestFiador {
    public static void main(String[] args) {
         java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            VistaFiador vista = new VistaFiador();
            ControladorFiador controlador = new ControladorFiador(vista);
            vista.setVisible(true);
        }
    });
    }
}
