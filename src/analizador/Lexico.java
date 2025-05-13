package analizador;

import java.util.LinkedList;
import java.util.List;

public class Lexico {

    private ErrorAnalizador errorAnalizador;

    public Lexico(ErrorAnalizador errorAnalizador) {
        this.errorAnalizador = errorAnalizador;
    }

    private boolean esDelimitador(char c) {
        return c == '{' || c == '}' || c == ';';
    }

    public List<Token> analizar(String codigoFuente) {
        LinkedList<Token> tokens = new LinkedList<>();
        if (codigoFuente == null) {
            return tokens;
        }

        String[] lineas = codigoFuente.split("\\r?\\n");
        int fila = 0;

        for (String linea : lineas) {
            fila++;
            Token tokenActual = null;
            StringBuilder tokenBuffer = new StringBuilder();
            int inicioTokenX = -1;

            for (int i = 0; i < linea.length(); i++) {
                char caracter = linea.charAt(i);

                if (tokenActual == null) { // No estamos dentro de un token
                    if (Character.isLetter(caracter)) { // Inicio de identificador o palabra clave
                        tokenActual = new Token();
                        tokenActual.fila = fila;
                        tokenActual.x = i;
                        tokenBuffer.append(caracter);
                    } else if (esDelimitador(caracter)) {
                        Token delimiterToken = new Token();
                        delimiterToken.fila = fila;
                        delimiterToken.token = String.valueOf(caracter);
                        delimiterToken.x = i;
                        delimiterToken.y = i;
                        tokens.add(delimiterToken);
                    } else if (Character.isWhitespace(caracter)) {
                        // Ignorar espacio en blanco
                    } else {
                        errorAnalizador.mostrarError("Error Léxico en fila " + fila + ", columna " + i
                                + ": Carácter inesperado '" + caracter + "'.");
                        return new LinkedList<>(); // Detener análisis si hay error léxico grave
                    }
                } else { // Estamos dentro de un token (identificador o palabra clave)
                    if (Character.isLetterOrDigit(caracter)) {
                        tokenBuffer.append(caracter);
                    } else {
                        // Finalizar token actual
                        tokenActual.token = tokenBuffer.toString();
                        tokenActual.y = i - 1; // La columna anterior fue el fin del token
                        tokens.add(tokenActual);
                        tokenActual = null;
                        tokenBuffer.setLength(0);

                        // Procesar el caracter actual que no es parte del token anterior
                        if (esDelimitador(caracter)) {
                            Token delimiterToken = new Token();
                            delimiterToken.fila = fila;
                            delimiterToken.token = String.valueOf(caracter);
                            delimiterToken.x = i;
                            delimiterToken.y = i;
                            tokens.add(delimiterToken);
                        } else if (Character.isWhitespace(caracter)) {
                            // Ignorar espacio en blanco
                        } else {
                            errorAnalizador.mostrarError(
                                    "Error Léxico en fila " + fila + ", columna " + i + ": Carácter inesperado '"
                                            + caracter + "' después de '" + tokens.getLast().token + "' .");
                            return new LinkedList<>(); // Detener análisis
                        }
                    }
                }
            }

            // Si un token quedó pendiente al final de la línea
            if (tokenActual != null) {
                tokenActual.token = tokenBuffer.toString();
                tokenActual.y = linea.length() - 1;
                tokens.add(tokenActual);
            }
        }
        return tokens;
    }

    // Example usage for testing
    public static void main(String[] args) {
        ErrorAnalizador errorWin = new ErrorAnalizador(); // Para pruebas
        Lexico lexico = new Lexico(errorWin);
        String prueba = "Create Table MiTabla { \n Text Nombre; \n Number Edad; \n Bool Activo; \n }";
        System.out.println("Entrada de prueba:\n" + prueba);
        List<Token> resultadoTokens = lexico.analizar(prueba);
        System.out.println("\nTokens generados:");
        for (Token token : resultadoTokens) {
            System.out.println(token);
        }

        String pruebaConErrorVisual = "Text NombreCompleto;";
        System.out.println("\nEntrada de prueba (error visual):\n" + pruebaConErrorVisual);
        resultadoTokens = lexico.analizar(pruebaConErrorVisual);
        System.out.println("\nTokens generados:");
        for (Token token : resultadoTokens) {
            System.out.println(token);
        }
    }
}
