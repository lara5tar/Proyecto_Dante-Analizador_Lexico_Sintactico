import analizador.ErrorAnalizador;
import analizador.Lexico;
import analizador.Sintactico;
import analizador.Token;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Main extends JFrame {

    private JTextArea areaCodigo;
    private JButton botonAnalizar;
    private JTextArea areaResultados;
    private ErrorAnalizador errorAnalizador;
    private Lexico lexico;
    private Sintactico sintactico;

    public Main() {
        setTitle("Analizador Léxico y Sintáctico Simplificado");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        initComponents();
        initAnalizadores();
    }

    private void initAnalizadores() {
        errorAnalizador = new ErrorAnalizador();
        lexico = new Lexico(errorAnalizador);
        sintactico = new Sintactico(errorAnalizador);
    }

    private void initComponents() {
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Área de texto para el código
        areaCodigo = new JTextArea();
        areaCodigo.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollCodigo = new JScrollPane(areaCodigo);
        scrollCodigo.setBorder(BorderFactory.createTitledBorder("Ingrese Código Aquí:"));

        // Área de texto para los resultados
        areaResultados = new JTextArea();
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaResultados.setEditable(false);
        JScrollPane scrollResultados = new JScrollPane(areaResultados);
        scrollResultados.setBorder(BorderFactory.createTitledBorder("Resultados del Análisis:"));

        // Panel superior para dividir código y resultados
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollCodigo, scrollResultados);
        splitPane.setResizeWeight(0.5);
        panelPrincipal.add(splitPane, BorderLayout.CENTER);

        // Botón de análisis
        botonAnalizar = new JButton("Analizar Código");
        botonAnalizar.setFont(new Font("Arial", Font.BOLD, 14));
        botonAnalizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                analizarCodigo();
            }
        });

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.add(botonAnalizar);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private void analizarCodigo() {
        String codigo = areaCodigo.getText();
        areaResultados.setText(""); // Limpiar resultados anteriores

        if (codigo.trim().isEmpty()) {
            errorAnalizador.mostrarError("No hay código para analizar.");
            return;
        }

        // Análisis Léxico
        areaResultados.append("--- Tokens Encontrados ---\n");
        List<Token> tokens = lexico.analizar(codigo);
        if (tokens.isEmpty() && !codigo.trim().isEmpty()) {
            // Si no hay tokens pero había código, el lexer podría haber encontrado un
            // problema no reportado a ErrorAnalizador
            // o el ErrorAnalizador ya mostró el error.
            // No se necesita hacer nada más aquí si ErrorAnalizador ya está manejando los
            // errores léxicos.
        } else {
            for (Token token : tokens) {
                areaResultados.append(token.toString() + "\n");
            }
        }
        areaResultados.append("\n--- Fin Tokens ---\n\n");

        // Análisis Sintáctico
        areaResultados.append("--- Análisis Sintáctico ---\n");
        if (sintactico.analizar(tokens)) {
            areaResultados.append("Análisis sintáctico completado exitosamente.\n");
            areaResultados.append("Tabla: " + sintactico.getNombreTabla() + "\n");
            areaResultados.append("Variables: " + sintactico.getVariables().toString() + "\n");
            areaResultados.append("Tipos: " + sintactico.getTipos().toString() + "\n");
        } else {
            // El error ya debería haber sido mostrado por el ErrorAnalizador llamado desde
            // Sintactico
            areaResultados.append("El análisis sintáctico falló. Revise la ventana de errores.\n");
        }
        areaResultados.append("--- Fin Análisis Sintáctico ---\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Opcional: Mejorar el Look and Feel
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new Main().setVisible(true);
            }
        });
    }
}
