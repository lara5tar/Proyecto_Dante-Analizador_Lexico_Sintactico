package analizador;

public class Token {
    String token;
    int fila;
    int x, y;

    Token() {
        token = "";
        fila = 0;
        x = 0;
        y = 0;
    }

    Token(String token, int fila, int x, int y) {
        this.token = token;
        this.fila = fila;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Token{" +
                "token='" + token + '\\' +
                ", fila=" + fila +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
