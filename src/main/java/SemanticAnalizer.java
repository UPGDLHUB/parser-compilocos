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
        String newScope = scopeName + "_" + scopeCounter;
        scopeStack.push(newScope);
        currentScope = newScope;
        System.out.println("Entering scope: " + currentScope);
    }

    public static void exitScope() {
        if (!scopeStack.isEmpty()) {
            String exitedScope = scopeStack.pop();
            System.out.println("Exiting scope: " + exitedScope);
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

        // Look for the variable in current scope, then parent scopes
        SymbolTableItem foundSymbol = findVariableInScope(id);
        if (foundSymbol != null) {
            System.out.println("Variable '" + id + "' found in scope '" + foundSymbol.getScope() + "'");
            return true;
        } else {
            System.err.println("Semantic Error: Variable '" + id + "' is not accessible in current scope");
            return false;
        }
    }

    public static boolean CheckAssignment(String variableId, String valueType) {
        SymbolTableItem variable = findVariableInScope(variableId);
        if (variable == null) {
            System.err.println("Semantic Error: Variable '" + variableId + "' is not declared");
            return false;
        }

        String variableType = variable.getType();
        int varTypeInt = stringTypeToInt(variableType);
        int valTypeInt = stringTypeToInt(valueType);

        if (varTypeInt == -1 || valTypeInt == -1) {
            System.err.println("Semantic Error: Unknown type in assignment");
            return false;
        }

        int resultType = SemanticCube.getResultType(SemanticCube.OP_ASSIGN, varTypeInt, valTypeInt);

        if (resultType == SemanticCube.TYPE_ERROR) {
            System.err.println("Semantic Error: Cannot assign " + valueType + " to variable '" +
                    variableId + "' of type " + variableType);
            return false;
        }

        System.out.println("Assignment valid: " + valueType + " to " + variableType);
        return true;
    }

    private static int stringTypeToInt(String type) {
        return switch (type.toLowerCase()) {
            case "int" -> SemanticCube.INTEGER;
            case "float" -> SemanticCube.FLOAT;
            case "char" -> SemanticCube.CHAR;
            case "string" -> SemanticCube.STRING;
            case "boolean" -> SemanticCube.BOOLEAN;
            case "void" -> SemanticCube.VOID;
            default -> -1;
        };
    }
    private static String intTypeToString(int type) {
        return switch (type) {
            case SemanticCube.INTEGER -> "int";
            case SemanticCube.FLOAT -> "float";
            case SemanticCube.CHAR -> "char";
            case SemanticCube.STRING -> "string";
            case SemanticCube.BOOLEAN -> "boolean";
            case SemanticCube.VOID -> "void";
            default -> "unknown";
        };
    }

    public static boolean CheckTypeCompatibility(String leftType, String rightType) {
        if (leftType.equals(rightType)) {
            return true;
        }

        // Allow some implicit conversions
        if ((leftType.equals("float") && rightType.equals("int")) ||
                (leftType.equals("double") && (rightType.equals("int") || rightType.equals("float")))) {
            return true;
        }

        System.err.println("Semantic Error: Type mismatch - cannot assign " + rightType + " to " + leftType);
        return false;
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

        // First check current scope
        for (SymbolTableItem symbol : symbols) {
            if (symbol.getScope().equals(currentScope)) {
                return symbol;
            }
        }

        // Then check parent scopes (from most recent to oldest)
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
