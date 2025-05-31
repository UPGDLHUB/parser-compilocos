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
