package analizador;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ErrorAnalizador extends JFrame {
    private JLabel messageLabel;
    private JButton okButton;

    public ErrorAnalizador() {
        setTitle("Error de Análisis");
        setSize(300, 150);
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        messageLabel = new JLabel();
        messageLabel.setHorizontalAlignment(JLabel.CENTER);

        okButton = new JButton("Aceptar");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the error window
            }
        });

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(okButton);

        setLayout(new BorderLayout());
        add(messagePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void mostrarError(String mensaje) {
        messageLabel
                .setText("<html><div style='text-align: center;'>" + mensaje.replace("\n", "<br>") + "</div></html>");
        setVisible(true);
    }

    // For testing purposes, not strictly needed for the final app
    public static void main(String[] args) {
        ErrorAnalizador errorWindow = new ErrorAnalizador();
        errorWindow.mostrarError("Este es un mensaje de error de prueba.\nSegunda línea.");
    }
}
