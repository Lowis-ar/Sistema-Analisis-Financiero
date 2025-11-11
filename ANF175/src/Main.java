import controlador.ClienteNaturalController;

public class Main {
    public static void main(String[] args) {
        // Ejecutar en el hilo de EDT de Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
            new ClienteNaturalController();
        });
    }
}