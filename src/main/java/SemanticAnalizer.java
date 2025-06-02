import java.util.Hashtable;
import java.util.Vector;
import java.util.Stack;

public class SemanticAnalizer {
    private static Hashtable<String, Vector<SymbolTableItem>> symbolTable = new Hashtable<>();
    private static Stack<String> scopeStack = new Stack<>();
    private static String currentScope = "global";
    private static int scopeCounter = 0;

    public static void initialize() {
        symbolTable.clear();
        scopeStack.clear();
        scopeStack.push("global");
        currentScope = "global";
        scopeCounter = 0;
    }


    public static void enterScope(String scopeName) {
        scopeCounter++;
        String newScope = scopeName ;
        scopeStack.push(newScope);
        currentScope = newScope;
    }

    public static void exitScope() {
        if (!scopeStack.isEmpty()) {
            String exitedScope = scopeStack.pop();
            if (!scopeStack.isEmpty()) {
                currentScope = scopeStack.peek();
            } else {
                currentScope = "global";
            }
        }
    }

    public static void CheckVariable(String type, String id){
        Vector<SymbolTableItem> symbols  = symbolTable.get(id);
        if(symbols == null || symbols.isEmpty()){
            String defaultValue = getDefaultValue(type);
            SymbolTableItem newSymbol = new SymbolTableItem(type, currentScope, defaultValue);

            Vector<SymbolTableItem> symbolList = new Vector<>();
            symbolList.add(newSymbol);
            symbolTable.put(id, symbolList);

            System.out.println("Variable '" + id + "' declared with type '" + type + "' in scope '" + currentScope + "'");
        } else {
            boolean existsInCurrentScope = false;
            for (SymbolTableItem symbol : symbols) {
                if (symbol.getScope().equals(currentScope)) {
                    existsInCurrentScope = true;
                    break;
                }
            }
            if (existsInCurrentScope) {
                System.err.println("Semantic Error: Variable '" + id + "' is already defined in scope '" + currentScope + "'");
            } else {
                String defaultValue = getDefaultValue(type);
                SymbolTableItem newSymbol = new SymbolTableItem(type, currentScope, defaultValue);
                symbols.add(newSymbol);
                System.out.println("Variable '" + id + "' declared with type '" + type + "' in scope '" + currentScope + "' (shadows outer scope)");
            }
        }
    }

    public static boolean CheckVariableUsage(String id) {
        Vector<SymbolTableItem> symbols = symbolTable.get(id);

        if (symbols == null || symbols.isEmpty()) {
            System.err.println("Semantic Error: Variable '" + id + "' is not declared");
            return false;
        }

        SymbolTableItem foundSymbol = findVariableInScope(id);
        if (foundSymbol != null) {
            return true;
        } else {
            System.err.println("Semantic Error: Variable '" + id + "' is not accessible in current scope");
            return false;
        }
    }




    public static String getVariableType(String id) {
        SymbolTableItem symbol = findVariableInScope(id);
        if (symbol != null) {
            return symbol.getType();
        }
        return null;
    }

    private static SymbolTableItem findVariableInScope(String id) {
        Vector<SymbolTableItem> symbols = symbolTable.get(id);
        if (symbols == null) return null;

        for (SymbolTableItem symbol : symbols) {
            if (symbol.getScope().equals(currentScope)) {
                return symbol;
            }
        }

        for (int i = scopeStack.size() - 2; i >= 0; i--) {
            String parentScope = scopeStack.get(i);
            for (SymbolTableItem symbol : symbols) {
                if (symbol.getScope().equals(parentScope)) {
                    return symbol;
                }
            }
        }

        return null;
    }

    public static int getTypeConstant(String type) {
        if (type == null) return SemanticCube.TYPE_ERROR;

        return switch (type.toLowerCase()) {
            case "int" -> SemanticCube.INTEGER;
            case "float" -> SemanticCube.FLOAT;
            case "char" -> SemanticCube.CHAR;
            case "string" -> SemanticCube.STRING;
            case "boolean" -> SemanticCube.BOOLEAN;
            case "void" -> SemanticCube.VOID;
            default -> SemanticCube.TYPE_ERROR;
        };
    }


    public static String getTypeString(int typeConstant) {
        return switch (typeConstant) {
            case SemanticCube.INTEGER -> "int";
            case SemanticCube.FLOAT -> "float";
            case SemanticCube.CHAR -> "char";
            case SemanticCube.STRING -> "string";
            case SemanticCube.BOOLEAN -> "boolean";
            case SemanticCube.VOID -> "void";
            default -> "error";
        };
    }

    public static int getOperatorConstant(String operator) {
        return switch (operator) {
            case "+" -> SemanticCube.OP_PLUS;
            case "-" -> SemanticCube.OP_MINUS;
            case "*" -> SemanticCube.OP_MULT;
            case "/" -> SemanticCube.OP_DIV;
            case "%" -> SemanticCube.OP_MOD;
            case "=" -> SemanticCube.OP_ASSIGN;
            case "&&" -> SemanticCube.OP_AND;
            case "||" -> SemanticCube.OP_OR;
            case "!" -> SemanticCube.OP_NOT;
            case "<" -> SemanticCube.OP_LESS;
            case ">" -> SemanticCube.OP_GREATER;
            case "<=" -> SemanticCube.OP_LESS_EQ;
            case ">=" -> SemanticCube.OP_GREATER_EQ;
            case "==" -> SemanticCube.OP_EQUAL;
            case "!=" -> SemanticCube.OP_NOT_EQUAL;
            default -> -1;
        };
    }

    public static String checkBinaryOperation(String leftType, String operator, String rightType) {
        int leftTypeConst = getTypeConstant(leftType);
        int rightTypeConst = getTypeConstant(rightType);
        int opConst = getOperatorConstant(operator);

        if (leftTypeConst == SemanticCube.TYPE_ERROR || rightTypeConst == SemanticCube.TYPE_ERROR || opConst == -1) {
            System.err.println("Semantic Error: Invalid types or operator in expression: " + leftType + " " + operator + " " + rightType);
            return "error";
        }

        int resultType = SemanticCube.getResultType(opConst, leftTypeConst, rightTypeConst);

        if (resultType == SemanticCube.TYPE_ERROR) {
            System.err.println("Semantic Error: Incompatible types for operation: " + leftType + " " + operator + " " + rightType);
            return "error";
        }

        return getTypeString(resultType);
    }

    public static String checkUnaryOperation(String operator, String operandType) {
        int operandTypeConst = getTypeConstant(operandType);
        int opConst = getOperatorConstant(operator);

        if (operandTypeConst == SemanticCube.TYPE_ERROR || opConst == -1) {
            System.err.println("Semantic Error: Invalid type or operator in unary expression: " + operator + operandType);
            return "error";
        }

        // For unary operations, use 0 as second operand (dummy)
        int resultType = SemanticCube.getResultType(opConst, operandTypeConst, 0);

        if (resultType == SemanticCube.TYPE_ERROR) {
            System.err.println("Semantic Error: Incompatible type for unary operation: " + operator + operandType);
            return "error";
        }

        return getTypeString(resultType);
    }

    public static boolean checkAssignment(String variableId, String valueType) {
        String variableType = getVariableType(variableId);
        if (variableType == null) {
            return false; // Variable not found, error already reported
        }

        String resultType = checkBinaryOperation(variableType, "=", valueType);
        if (resultType.equals("error")) {
            System.err.println("Semantic Error: Cannot assign " + valueType + " to variable '" + variableId + "' of type " + variableType);
            return false;
        }

        return true;
    }

    public static String getTokenType(TheToken token) {
        String tokenType = token.getType();
        String tokenValue = token.getValue();

        return switch (tokenType) {
            case "INTEGER", "OCTAL", "HEXADECIMAL", "BINARY" -> "int";
            case "FLOAT", "EXPONENTIAL" -> "float";
            case "STRING" -> "string";
            case "CHAR" -> "char";
            case "IDENTIFIER" -> {
                if (tokenValue.equals("true") || tokenValue.equals("false")) {
                    yield "boolean";
                } else {
                    // It's a variable, get its type from symbol table
                    yield getVariableType(tokenValue);
                }
            }
            default -> "error";
        };
    }


    private static String getDefaultValue(String type) {
        return switch (type.toLowerCase()) {
            case "int", "float", "double" -> "0";
            case "boolean" -> "false";
            case "char" -> "''";
            case "string" -> "\"\"";
            default -> "";
        };
    }


}
