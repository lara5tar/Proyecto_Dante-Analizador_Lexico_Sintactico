package analizador;

import java.util.LinkedList;
import java.util.List;

public class Sintactico {

    private ErrorAnalizador errorAnalizador;
    private List<Token> tokens;
    private int indiceActual;
    private Token tokenActual;

    private String nombreTabla;
    private LinkedList<String> variables;
    private LinkedList<String> tipos;

    public Sintactico(ErrorAnalizador errorAnalizador) {
        this.errorAnalizador = errorAnalizador;
    }

    public boolean analizar(List<Token> tokens) {
        this.tokens = tokens;
        this.indiceActual = 0;
        this.nombreTabla = "";
        this.variables = new LinkedList<>();
        this.tipos = new LinkedList<>();

        if (this.tokens.isEmpty()) {
            errorAnalizador.mostrarError("No hay tokens para analizar.");
            return false;
        }
        avanzar(); // Cargar el primer token

        return parseCreateTable();
    }

    private void avanzar() {
        if (indiceActual < tokens.size()) {
            tokenActual = tokens.get(indiceActual++);
        } else {
            tokenActual = new Token("EOF", -1, -1, -1); // End Of File token
        }
    }

    private boolean esperar(String tipoEsperado) {
        if (tokenActual.token.equals(tipoEsperado)) {
            avanzar();
            return true;
        } else {
            errorAnalizador.mostrarError("Error Sintáctico en fila " + tokenActual.fila + ", columna " + tokenActual.x
                    + ": Se esperaba '" + tipoEsperado + "' pero se encontró '" + tokenActual.token + "'.");
            return false;
        }
    }

    private boolean parseCreateTable() {
        if (!esperar("Create"))
            return false;
        if (!esperar("Table"))
            return false;

        if (esIdentificador(tokenActual.token)) {
            nombreTabla = tokenActual.token;
            avanzar();
        } else {
            errorAnalizador.mostrarError("Error Sintáctico en fila " + tokenActual.fila + ", columna " + tokenActual.x
                    + ": Se esperaba un nombre de tabla válido.");
            return false;
        }

        if (!esperar("{"))
            return false;

        while (!tokenActual.token.equals("}") && !tokenActual.token.equals("EOF")) {
            if (!parseDeclaracionVariable())
                return false;
        }

        if (!esperar("}"))
            return false;

        if (variables.isEmpty()) {
            errorAnalizador.mostrarError("Error Sintáctico: La tabla debe tener al menos una variable definida.");
            return false;
        }

        System.out.println("Tabla: " + nombreTabla);
        for (int i = 0; i < variables.size(); i++) {
            System.out.println("Variable: " + variables.get(i) + " Tipo: " + tipos.get(i));
        }

        return true; // Análisis sintáctico exitoso
    }

    private boolean parseDeclaracionVariable() {
        String tipoVariable;
        String nombreVariable;

        if (tokenActual.token.equals("Text") || tokenActual.token.equals("Number")
                || tokenActual.token.equals("Bool")) {
            tipoVariable = tokenActual.token;
            avanzar();
        } else {
            errorAnalizador.mostrarError("Error Sintáctico en fila " + tokenActual.fila + ", columna " + tokenActual.x
                    + ": Tipo de variable no válido ('" + tokenActual.token + "'). Se esperaba Text, Number o Bool.");
            return false;
        }

        if (esIdentificador(tokenActual.token)) {
            nombreVariable = tokenActual.token;
            if (variables.contains(nombreVariable)) {
                errorAnalizador.mostrarError("Error Semántico en fila " + tokenActual.fila + ", columna "
                        + tokenActual.x + ": Variable '" + nombreVariable + "' ya definida.");
                return false;
            }
            avanzar();
        } else {
            errorAnalizador.mostrarError("Error Sintáctico en fila " + tokenActual.fila + ", columna " + tokenActual.x
                    + ": Se esperaba un nombre de variable válido.");
            return false;
        }

        if (!esperar(";"))
            return false;

        tipos.add(tipoVariable);
        variables.add(nombreVariable);
        return true;
    }

    private boolean esIdentificador(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        if (s.equals("Create") || s.equals("Table") || s.equals("Text") || s.equals("Number") || s.equals("Bool")
                || s.equals("{") || s.equals("}") || s.equals(";")) {
            return false; // Keywords are not identifiers
        }
        char primeraLetra = s.charAt(0);
        if (!Character.isLetter(primeraLetra) && primeraLetra != '_') {
            return false;
        }
        for (int i = 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '_') {
                return false;
            }
        }
        return true;
    }

    public String getNombreTabla() {
        return nombreTabla;
    }

    public LinkedList<String> getVariables() {
        return variables;
    }

    public LinkedList<String> getTipos() {
        return tipos;
    }

    // Example usage for testing
    public static void main(String[] args) {
        ErrorAnalizador errorWin = new ErrorAnalizador();
        Lexico lexico = new Lexico(errorWin);
        Sintactico sintactico = new Sintactico(errorWin);

        String pruebaCorrecta = "Create Table MiTabla { \n Text Nombre; \n Number Edad; \n Bool Activo; \n }";
        String pruebaErrorSintaxis = "Create MiTabla { Text Nombre; }"; // Missing 'Table'
        String pruebaErrorVariable = "Create Table MiTabla { Text Nombre Nombre; }"; // Repeated variable name
        // (semantic, but caught as syntax
        // here due to structure)
        String pruebaSinVariables = "Create Table MiTabla { }";
        String pruebaTipoInvalido = "Create Table MiTabla { Integer Edad; }";

        System.out.println("Probando sintaxis correcta:");
        List<Token> tokensCorrectos = lexico.analizar(pruebaCorrecta);
        if (sintactico.analizar(tokensCorrectos)) {
            System.out.println("Análisis sintáctico correcto.");
            System.out.println("Tabla: " + sintactico.getNombreTabla());
            System.out.println("Variables: " + sintactico.getVariables());
            System.out.println("Tipos: " + sintactico.getTipos());
        } else {
            System.out.println("Análisis sintáctico fallido.");
        }

        System.out.println("\nProbando error de sintaxis (falta Table):");
        List<Token> tokensErrorSintaxis = lexico.analizar(pruebaErrorSintaxis);
        sintactico.analizar(tokensErrorSintaxis);

        System.out.println("\nProbando error de variable (nombre repetido):");
        List<Token> tokensErrorVariable = lexico.analizar(pruebaErrorVariable);
        sintactico.analizar(tokensErrorVariable);

        System.out.println("\nProbando tabla sin variables:");
        List<Token> tokensSinVariables = lexico.analizar(pruebaSinVariables);
        sintactico.analizar(tokensSinVariables);

        System.out.println("\nProbando tipo de dato inválido:");
        List<Token> tokensTipoInvalido = lexico.analizar(pruebaTipoInvalido);
        sintactico.analizar(tokensTipoInvalido);
    }
}
