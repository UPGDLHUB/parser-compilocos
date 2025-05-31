public class SemanticCube {
    public static final int OP_PLUS       = 0;  // +
    public static final int OP_MINUS      = 1;  // -
    public static final int OP_MULT       = 2;  // *
    public static final int OP_DIV        = 3;  // /
    public static final int OP_MOD        = 4;  // %
    public static final int OP_ASSIGN     = 5;  // =
    public static final int OP_AND        = 6;  // &&
    public static final int OP_OR         = 7;  // ||
    public static final int OP_NOT        = 8;  // !
    public static final int OP_LESS       = 9;  // <
    public static final int OP_GREATER    = 10; // >
    public static final int OP_LESS_EQ    = 11; // <=
    public static final int OP_GREATER_EQ = 12; // >=
    public static final int OP_EQUAL      = 13; // ==
    public static final int OP_NOT_EQUAL  = 14; // !=

    // Número total de operadores
    public static final int NUM_OPS = 15;


    public static final int INTEGER = 0;
    public static final int FLOAT   = 1;
    public static final int CHAR    = 2;
    public static final int STRING  = 3;
    public static final int BOOLEAN = 4;
    public static final int VOID    = 5;

    // Número total de tipos
    public static final int NUM_TYPES = 6;

    public static final int TYPE_ERROR = -1;

    private static final int[][][] cube = new int[NUM_OPS][NUM_TYPES][NUM_TYPES];

    static {
        for (int op = 0; op < NUM_OPS; op++) {
            for (int t1 = 0; t1 < NUM_TYPES; t1++) {
                for (int t2 = 0; t2 < NUM_TYPES; t2++) {
                    cube[op][t1][t2] = TYPE_ERROR;
                }
            }
        }
        // OPERADOR +
        cube[OP_PLUS][INTEGER][INTEGER] = INTEGER;
        cube[OP_PLUS][INTEGER][FLOAT]   = FLOAT;
        cube[OP_PLUS][FLOAT][INTEGER]   = FLOAT;
        cube[OP_PLUS][FLOAT][FLOAT]     = FLOAT;
        for (int t = 0; t < NUM_TYPES; t++) {
            if (t != VOID) {
                cube[OP_PLUS][STRING][t] = STRING;
                cube[OP_PLUS][t][STRING] = STRING;
            }
        }
        cube[OP_PLUS][CHAR][CHAR] = STRING;
        // OPERADOR -
        cube[OP_MINUS][INTEGER][INTEGER] = INTEGER;
        cube[OP_MINUS][INTEGER][FLOAT]   = FLOAT;
        cube[OP_MINUS][FLOAT][INTEGER]   = FLOAT;
        cube[OP_MINUS][FLOAT][FLOAT]     = FLOAT;
        // OPERADOR *
        cube[OP_MULT][INTEGER][INTEGER] = INTEGER;
        cube[OP_MULT][INTEGER][FLOAT]   = FLOAT;
        cube[OP_MULT][FLOAT][INTEGER]   = FLOAT;
        cube[OP_MULT][FLOAT][FLOAT]     = FLOAT;
        // OPERADOR /
        cube[OP_DIV][INTEGER][INTEGER] = FLOAT;
        cube[OP_DIV][INTEGER][FLOAT]   = FLOAT;
        cube[OP_DIV][FLOAT][INTEGER]   = FLOAT;
        cube[OP_DIV][FLOAT][FLOAT]     = FLOAT;
        // OPERADOR %
        cube[OP_MOD][INTEGER][INTEGER] = INTEGER;
        // OPERADOR =
        cube[OP_ASSIGN][INTEGER][INTEGER] = INTEGER;
        cube[OP_ASSIGN][FLOAT][FLOAT]     = FLOAT;
        cube[OP_ASSIGN][FLOAT][INTEGER]   = FLOAT;
        cube[OP_ASSIGN][INTEGER][FLOAT]  = TYPE_ERROR;
        cube[OP_ASSIGN][CHAR][CHAR]      = CHAR;
        cube[OP_ASSIGN][STRING][STRING]  = STRING;
        cube[OP_ASSIGN][BOOLEAN][BOOLEAN] = BOOLEAN;
        // BOOLEAN
        cube[OP_AND][BOOLEAN][BOOLEAN] = BOOLEAN;
        cube[OP_OR][BOOLEAN][BOOLEAN] = BOOLEAN;
        // OPERADOR !
        for (int t = 0; t < NUM_TYPES; t++) {
            cube[OP_NOT][t][0] = TYPE_ERROR; // inicializamos todos a error
        }
        cube[OP_NOT][BOOLEAN][0] = BOOLEAN;
        // OPERADOR < (LESS)
        cube[OP_LESS][INTEGER][INTEGER] = BOOLEAN;
        cube[OP_LESS][INTEGER][FLOAT]   = BOOLEAN;
        cube[OP_LESS][FLOAT][INTEGER]   = BOOLEAN;
        cube[OP_LESS][FLOAT][FLOAT]     = BOOLEAN;
        // OPERADOR > (GREATER)
        cube[OP_GREATER][INTEGER][INTEGER] = BOOLEAN;
        cube[OP_GREATER][INTEGER][FLOAT]   = BOOLEAN;
        cube[OP_GREATER][FLOAT][INTEGER]   = BOOLEAN;
        cube[OP_GREATER][FLOAT][FLOAT]     = BOOLEAN;
        // OPERADOR <= (LESS_EQ)
        cube[OP_LESS_EQ][INTEGER][INTEGER] = BOOLEAN;
        cube[OP_LESS_EQ][INTEGER][FLOAT]   = BOOLEAN;
        cube[OP_LESS_EQ][FLOAT][INTEGER]   = BOOLEAN;
        cube[OP_LESS_EQ][FLOAT][FLOAT]     = BOOLEAN;
        // OPERADOR >= (GREATER_EQ)
        cube[OP_GREATER_EQ][INTEGER][INTEGER] = BOOLEAN;
        cube[OP_GREATER_EQ][INTEGER][FLOAT]   = BOOLEAN;
        cube[OP_GREATER_EQ][FLOAT][INTEGER]   = BOOLEAN;
        cube[OP_GREATER_EQ][FLOAT][FLOAT]     = BOOLEAN;
        // OPERADOR == (EQUAL)
        cube[OP_EQUAL][INTEGER][INTEGER] = BOOLEAN;
        cube[OP_EQUAL][INTEGER][FLOAT]   = BOOLEAN;
        cube[OP_EQUAL][FLOAT][INTEGER]   = BOOLEAN;
        cube[OP_EQUAL][FLOAT][FLOAT]     = BOOLEAN;
        cube[OP_EQUAL][STRING][STRING]   = BOOLEAN;
        cube[OP_EQUAL][CHAR][CHAR]       = BOOLEAN;
        cube[OP_EQUAL][BOOLEAN][BOOLEAN] = BOOLEAN;
        // OPERADOR != (NOT_EQUAL)
        cube[OP_NOT_EQUAL][INTEGER][INTEGER] = BOOLEAN;
        cube[OP_NOT_EQUAL][INTEGER][FLOAT]   = BOOLEAN;
        cube[OP_NOT_EQUAL][FLOAT][INTEGER]   = BOOLEAN;
        cube[OP_NOT_EQUAL][FLOAT][FLOAT]     = BOOLEAN;
        cube[OP_NOT_EQUAL][STRING][STRING]   = BOOLEAN;
        cube[OP_NOT_EQUAL][CHAR][CHAR]       = BOOLEAN;
        cube[OP_NOT_EQUAL][BOOLEAN][BOOLEAN] = BOOLEAN;
    }

    public static int getResultType(int op, int t1, int t2) {
        if (op < 0 || op >= NUM_OPS || t1 < 0 || t1 >= NUM_TYPES || t2 < 0 || t2 >= NUM_TYPES) {
            return TYPE_ERROR;
        }
        return cube[op][t1][t2];
    }

    private static String typeName(int t) {
        switch (t) {
            case INTEGER:    return "INTEGER";
            case FLOAT:      return "FLOAT";
            case CHAR:       return "CHAR";
            case STRING:     return "STRING";
            case BOOLEAN:    return "BOOLEAN";
            case VOID:       return "VOID";
            case TYPE_ERROR: return "ERROR";
            default:         return "UNKNOWN";
        }
    }
}
