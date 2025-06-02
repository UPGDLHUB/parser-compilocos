/**
 * A Token is a pair of a value (string or word) and its type
 *
 * @author javiergs
 * @author OrganistaF
 * @author DJ2513
 * @author GabrielGuerra06
 * @version 1.0
 */

import java.util.HashMap;
import java.util.Vector;
import java.util.*;
public class TheParser {

    private Vector<TheToken> tokens;
    private int currentToken;

    public TheParser(Vector<TheToken> tokens) {
        this.tokens = tokens;
        currentToken = 0;
    }
    public void RULE_METHODS() {
        System.out.println("-- RULE_METHODS");
        if (!FirstsSet.methods().contains(tokens.get(currentToken).getValue())) {
            error(6);
            return;
        }
        String methodType = tokens.get(currentToken).getValue();
        RULE_TYPES();
        if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
            String methodName = tokens.get(currentToken).getValue();
            SemanticAnalizer.enterScope(methodName);
            currentToken++;
            System.out.println("- IDENTIFIER");
        } else {
            error(6);
            return;
        }
        if (tokens.get(currentToken).getValue().equals("(")) {
            currentToken++;
            System.out.println("- (");
        } else {
            error(7);
            return;
        }
        RULE_PARAMS();
        if (tokens.get(currentToken).getValue().equals(")")) {
            currentToken++;
            System.out.println("- )");
        } else {
            error(8);
            return;
        }
        if (tokens.get(currentToken).getValue().equals("{")) {
            currentToken++;
            System.out.println("- {");
            RULE_BODY();
            if (tokens.get(currentToken).getValue().equals("}")) {
                currentToken++;
                System.out.println("- }");
                SemanticAnalizer.exitScope();
            }
            else{
                error(2);
            }
        }
    }


    public void RULE_TYPES() {
        System.out.println("-- RULE_TYPES");
        if (!FirstsSet.types().contains(tokens.get(currentToken).getValue())) {
            error(12);
        }

        String type = tokens.get(currentToken).getValue();
        if (type.equals("int") || type.equals("float") ||
                type.equals("boolean") || type.equals("char") ||
                type.equals("string") || type.equals("void")) {
            currentToken++;
            System.out.println("- " + type);
        } else {
            error(12);
        }
    }
    public void RULE_GLOBAL_ATTRIBUTE(){
        System.out.println("-- RULE_GLOBAL_ATTRIBUTE");
        RULE_VARIABLE();
    }



    public void RULE_PARAMS() {
        System.out.println("-- RULE_PARAMS");
        if (!FirstsSet.params().contains(tokens.get(currentToken).getValue())) {
            error(12);
        }
        while (!tokens.get(currentToken).getValue().equals(")")) {
            String paramType = tokens.get(currentToken).getValue();
            RULE_TYPES();
            if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
                String paramId = tokens.get(currentToken).getValue();
                SemanticAnalizer.CheckVariable(paramType, paramId);
                System.out.println("--IDENTIFIER: " + tokens.get(currentToken).getValue());
                currentToken++;
            } else {
                error(16);
            }
            if (tokens.get(currentToken).getValue().equals(",")) {
                System.out.println("--IDENTIFIER: " + tokens.get(currentToken).getValue());
                currentToken++;
                RULE_PARAMS();
            }
        }


    }

    public void RULE_ASSIGNMENT() {
        System.out.println("--RULE_ASSIGNMENT");
        if (!FirstsSet.assignment().contains(tokens.get(currentToken).getType())) {
            error(12);
        }
        if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
            String variableId = tokens.get(currentToken).getValue();
            if (!SemanticAnalizer.CheckVariableUsage(variableId)) {
            }
            System.out.println("--IDENTIFIER");
            currentToken++;
            if (tokens.get(currentToken).getValue().equals("=")) {
                System.out.println("-- =");
                currentToken++;
                String expressionType = RULE_EXPRESSION();
                SemanticAnalizer.checkAssignment(variableId, expressionType);

                if (tokens.get(currentToken).getValue().equals(";")) {
                    System.out.println("-- ;");
                    currentToken++;
                }
            } else {
                error(10);
            }
        } else {
            error(10);
        }
    }

    public void RULE_VARIABLE() {
        System.out.println("--RULE_VARIABLE");
        if (!FirstsSet.variable().contains(tokens.get(currentToken).getValue())) {
            error(12);
        }
        String variableType = tokens.get(currentToken).getValue();
        RULE_TYPES();
        if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {

            String variableId = tokens.get(currentToken).getValue();
            SemanticAnalizer.CheckVariable(variableType, variableId);

            System.out.println("--IDENTIFIER");
            currentToken++;
            if (tokens.get(currentToken).getValue().equals("=")) {
                System.out.println("-- =");
                currentToken++;
                String expressionType = RULE_EXPRESSION();
                SemanticAnalizer.checkAssignment(variableId, expressionType);

                if (tokens.get(currentToken).getValue().equals(";")) {
                    currentToken++;
                    System.out.println("- ;");
                } else {
                    error(3);
                }
            } else if (tokens.get(currentToken).getValue().equals(";")) {
                System.out.println("-- ;");
                currentToken++;
            } else {
                error(9);
            }
        } else {
            error(10);
        }
    }


    public void RULE_RETURN() {
        System.out.println("-- RULE_RETURN");
        if (!FirstsSet._return().contains(tokens.get(currentToken).getValue())) {
            error(12);
        }
        if (tokens.get(currentToken).getValue().equals("return")) {
            currentToken++;
            if (!tokens.get(currentToken).getValue().equals(";")) {
                 RULE_EXPRESSION();
            }
            if (tokens.get(currentToken).getValue().equals(";")) {
                System.out.println("- ;");
                currentToken++;
            } else {
                error(20);
            }
        } else {
            error(19);
        }
    }

    public void RULE_CALL() {
        System.out.println("-- RULE_CALL");
        if (!FirstsSet.call().contains(tokens.get(currentToken).getType())) {
            error(12);
        }
        if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
            System.out.println("-- IDENTIFIER");
            currentToken++;
            if (tokens.get(currentToken).getValue().equals("(")) {
                currentToken++;
                System.out.println("- (");
                RULE_ARGUMENTS();
                if (tokens.get(currentToken).getValue().equals(")")) {
                    currentToken++;
                    System.out.println("- )");
                } else {
                    error(8);
                }
            } else {
                error(7);
            }
        } else {
            error(6);
        }
    }

    public void RULE_ARGUMENTS() {
        System.out.println("-- RULE_ARGUMENTS");
        if (!FirstsSet.arguments().contains(tokens.get(currentToken).getType())) {
            error(12);
        }
        if (!tokens.get(currentToken).getValue().equals(")")) {
            System.out.println("-- )");
            RULE_EXPRESSION();
            while (tokens.get(currentToken).getValue().equals(",")) {
                currentToken++;
                RULE_EXPRESSION();
            }
        }
    }

    public void RULE_FOR() {
        System.out.println("-- RULE_FOR");
        if (!FirstsSet._for().contains(tokens.get(currentToken).getValue())) {
            error(12);
        }
        currentToken++;
        SemanticAnalizer.enterScope("for");
        if (tokens.get(currentToken).getValue().equals("(")) {
            System.out.println("- (");
            currentToken++;
            RULE_VARIABLE();
            RULE_EXPRESSION();
            if (tokens.get(currentToken).getValue().equals(";")) {
                currentToken++;
                System.out.println("- ;");
                String nextValue = tokens.get(currentToken + 1).getValue();
                if (nextValue.equals("=")) {
                    RULE_ASSIGNMENT();
                }
                else{
                    RULE_EXPRESSION();
                }
                if (tokens.get(currentToken).getValue().equals(")")) {
                    currentToken++;
                    System.out.println("- )");
                    if (tokens.get(currentToken).getValue().equals("{")) {
                        currentToken++;
                        System.out.println("- {");
                        while (!tokens.get(currentToken).getValue().equals("}")) {
                            RULE_BODY();
                        }
                        if (tokens.get(currentToken).getValue().equals("}")) {
                            currentToken++;
                            System.out.println("- }");
                            SemanticAnalizer.exitScope();
                        } else {
                            error(2);
                        }
                    } else {
                        error(1);
                    }
                }
            } else {
                error(7);
            }
        } else {
            error(1);
        }
    }

    public void RULE_SWITCH() {
        System.out.println("-- RULE_SWITCH");
        if (!FirstsSet._switch().contains(tokens.get(currentToken).getValue())) {
            error(12);
        }
        currentToken++;

        if (tokens.get(currentToken).getValue().equals("(")) {
            System.out.println("-- (");
            currentToken++;
        } else {
            error(6);
        }

        RULE_EXPRESSION(); // Parse switch expression

        if (tokens.get(currentToken).getValue().equals(")")) {
            System.out.println("-- )");
            currentToken++;
        } else {
            error(1);
        }
        if (tokens.get(currentToken).getValue().equals("{")) {
            System.out.println("-- {");
            currentToken++;
        } else {
            error(2);
        }

        while (!tokens.get(currentToken).getValue().equals("}")) {
            if (tokens.get(currentToken).getValue().equals("case")) {
                System.out.println("-- case");
                currentToken++;

                // Handle case with colon attached (e.g., "1:")
                if (tokens.get(currentToken).getValue().contains(":")) {
                    String caseExpression = tokens.get(currentToken).getValue();
                    String caseValue = caseExpression.split(":")[0];
                    String colon = ":";

                    // Determine the correct token type for the case value
                    String tokenType = determineCaseValueType(caseValue);

                    tokens.remove(currentToken);
                    tokens.add(currentToken, new TheToken(caseValue, tokenType));
                    tokens.add(currentToken + 1, new TheToken(colon, "OPERATOR"));

                    System.out.println("-- case expression " + caseValue + " and colon detected");
                }

                // Parse the case value as a literal, not as a variable expression
                String caseValueType = parseCaseValue();

                if (tokens.get(currentToken).getValue().equals(":")) {
                    System.out.println("-- :");
                    currentToken++;
                } else {
                    error(17);
                }

                // Parse case body
                while (!tokens.get(currentToken).getValue().equals("case") &&
                        !tokens.get(currentToken).getValue().equals("default") &&
                        !tokens.get(currentToken).getValue().equals("}")) {
                    if (tokens.get(currentToken).getValue().equals("break")) {
                        System.out.println("-- break");
                        currentToken++;

                        if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals(";")) {
                            System.out.println("-- ;");
                            currentToken++;
                        } else {
                            error(18);
                        }
                        break;
                    } else if (tokens.get(currentToken).getValue().equals("break;")) {
                        System.out.println("-- break;");
                        currentToken++;
                        break;
                    } else {
                        RULE_BODY();
                    }
                }
            }
            else if (tokens.get(currentToken).getValue().equals("default") ||
                    tokens.get(currentToken).getValue().equals("default:")) {

                // Handle default case
                if (tokens.get(currentToken).getValue().contains(":")) {
                    String defaultExpression = tokens.get(currentToken).getValue();
                    String defaultKeyword = defaultExpression.split(":")[0];
                    String colon = ":";
                    tokens.remove(currentToken);
                    tokens.add(currentToken, new TheToken(defaultKeyword, "KEYWORD"));
                    tokens.add(currentToken + 1, new TheToken(colon, "OPERATOR"));
                }

                System.out.println("-- default");
                currentToken++; // Skip "default"

                if (tokens.get(currentToken).getValue().equals(":")) {
                    System.out.println("-- :");
                    currentToken++;
                } else {
                    error(17);
                }

                // Parse default case body
                while (!tokens.get(currentToken).getValue().equals("}")) {
                    if (tokens.get(currentToken).getValue().equals("break")) {
                        System.out.println("-- break");
                        currentToken++;

                        if (currentToken < tokens.size() && tokens.get(currentToken).getValue().equals(";")) {
                            System.out.println("-- ;");
                            currentToken++;
                        } else {
                            error(18);
                        }
                        break;
                    } else if (tokens.get(currentToken).getValue().equals("break;")) {
                        System.out.println("-- break;");
                        currentToken++;
                        break;
                    } else {
                        RULE_BODY();
                    }
                }
            } else {
                error(19);
            }
        }

        System.out.println("-- }");
        currentToken++;
    }

    private String determineCaseValueType(String value) {
        try {
            Integer.parseInt(value);
            return "INTEGER";
        } catch (NumberFormatException e1) {
            try {
                Float.parseFloat(value);
                return "FLOAT";
            } catch (NumberFormatException e2) {
                if (value.startsWith("'") && value.endsWith("'")) {
                    return "CHAR";
                } else if (value.startsWith("\"") && value.endsWith("\"")) {
                    return "STRING";
                } else if (value.equals("true") || value.equals("false")) {
                    return "BOOLEAN";
                } else {
                    return "IDENTIFIER"; // Fallback for variables
                }
            }
        }
    }

    // Helper method to parse case values as literals
    private String parseCaseValue() {
        String tokenType = tokens.get(currentToken).getType();
        String tokenValue = tokens.get(currentToken).getValue();

        System.out.println("-- Parsing case value: " + tokenValue + " of type: " + tokenType);

        String resultType = switch (tokenType) {
            case "INTEGER", "OCTAL", "HEXADECIMAL", "BINARY" -> {
                System.out.println("-- Case INTEGER value");
                yield "int";
            }
            case "FLOAT", "EXPONENTIAL" -> {
                System.out.println("-- Case FLOAT value");
                yield "float";
            }
            case "STRING" -> {
                System.out.println("-- Case STRING value");
                yield "string";
            }
            case "CHAR" -> {
                System.out.println("-- Case CHAR value");
                yield "char";
            }
            case "IDENTIFIER" -> {
                if (tokenValue.equals("true") || tokenValue.equals("false")) {
                    System.out.println("-- Case BOOLEAN value");
                    yield "boolean";
                } else {
                    // It's a variable reference in case - validate it exists
                    if (SemanticAnalizer.CheckVariableUsage(tokenValue)) {
                        yield SemanticAnalizer.getVariableType(tokenValue);
                    } else {
                        yield "error";
                    }
                }
            }
            default -> "error";
        };

        currentToken++;
        return resultType;
    }


    public void RULE_DOWHILE() {
        System.out.println("-- RULE_DO_WHILE");
        if (!FirstsSet.doWhile().contains(tokens.get(currentToken).getValue())) {
            error(12);
        }
        currentToken++;
        SemanticAnalizer.enterScope("dowhile");
        if (tokens.get(currentToken).getValue().equals("{")) {
            System.out.println("-- {");
            currentToken++;
            while (!tokens.get(currentToken).getValue().equals("}")) {
                RULE_BODY();
                currentToken++;
            }
            currentToken++;
            System.out.println("- }");
            SemanticAnalizer.exitScope();
        } else {
            error(1);
        }
        if (tokens.get(currentToken).getValue().equals("while")) {
            System.out.println("-- while");
            if (tokens.get(currentToken).getValue().equals("(")) {
                System.out.println("-- (");
                currentToken++;
            } else {
                error(1);
            }
            RULE_EXPRESSION();

            if (tokens.get(currentToken).getValue().equals(")")) {
                currentToken++;
                System.out.println("- )");
            } else {
                error(2);
            }
            if (tokens.get(currentToken).getValue().equals(";")) {
                currentToken++;
                System.out.println("- ;");
            } else {
                error(3);
            }
        }
    }


    public void RULE_IF(){
        System.out.println("-- RULE_IF");
        if (!FirstsSet._if().contains(tokens.get(currentToken).getValue())) {
            error(12);
        }
        currentToken++;
        System.out.println("Actual token: " + tokens.get(currentToken).getValue());
        if (tokens.get(currentToken).getValue().equals("(")) {
            System.out.println("-- (");
            currentToken++;
            RULE_EXPRESSION();
            if (tokens.get(currentToken).getValue().equals(")")) {
                currentToken++;
                System.out.println("- )");
            } else {
                error(2);
            }
        }
        SemanticAnalizer.enterScope("if");

        if (tokens.get(currentToken).getValue().equals("{")) {
            System.out.println("-- {");
            currentToken++;
            RULE_BODY();
            if (tokens.get(currentToken).getValue().equals("}")) {
                currentToken++;
                System.out.println("-- }");
                SemanticAnalizer.exitScope();
            } else {
                error(2);
            }
        }
        if (tokens.get(currentToken).getValue().equals("else")) {
            currentToken++;
            SemanticAnalizer.enterScope("else");
            if (tokens.get(currentToken).getValue().equals("if")) {
                System.out.println("-- ELSE_IF");
                RULE_IF();
            }
            else if (tokens.get(currentToken).getValue().equals("{")) {
                System.out.println("-- {");
                currentToken++;
                RULE_BODY();
                if (tokens.get(currentToken).getValue().equals("}")) {
                    currentToken++;
                    System.out.println("- }");
                } else {
                    error(2);
                }
            }
            else {
                System.out.println("- else");
                RULE_BODY();
                if(tokens.get(currentToken).getValue().equals(";")){
                    System.out.println("-- ;");
                    currentToken++;
                }
            }
            SemanticAnalizer.exitScope();
        } else {
            error(25);
        }

    }

    public void RULE_WHILE() {
        System.out.println("-- RULE_WHILE");
        if (!FirstsSet._while().contains(tokens.get(currentToken).getValue())) {
            error(12);

        }
        currentToken++;
        if (tokens.get(currentToken).getValue().equals("(")) {
            System.out.println("-- (");
            currentToken++;
        } else {
            error(1);
        }
        RULE_EXPRESSION();
        if (tokens.get(currentToken).getValue().equals(")")) {
            currentToken++;
            System.out.println("- )");
        } else {
            error(2);
        }
        SemanticAnalizer.enterScope("while");

        if (tokens.get(currentToken).getValue().equals("{")) {
            System.out.println("-- {");
            currentToken++;
            while (!tokens.get(currentToken).getValue().equals("}")) {
                RULE_BODY();
            }
            if (tokens.get(currentToken).getValue().equals("}")) {
                currentToken++;
                System.out.println("- }");
                SemanticAnalizer.exitScope();
            } else {
                error(2);
            }
            System.out.println("- }");
        } else {
            error(1);
        }
    }

    public void run() {
        SemanticAnalizer.initialize();
        RULE_PROGRAM();
    }

    private void RULE_PROGRAM() {
        System.out.println("- RULE_PROGRAM");

        if (!FirstsSet.program().contains(tokens.get(currentToken).getValue())) {
            error(1);
            return;
        }
        if (tokens.get(currentToken).getValue().equals("class")) {
            currentToken++;
            System.out.println("- class");
        } else {
            error(1);
            return;
        }
        if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
            currentToken++;
            System.out.println("- IDENTIFIER");
        } else {
            error(2);
            return;
        }
        if (tokens.get(currentToken).getValue().equals("{")) {
            currentToken++;
            System.out.println("- {");
        } else {
            error(3);
            return;
        }
        while (!tokens.get(currentToken).getValue().equals("}")) {
            String tokenValue = tokens.get(currentToken).getValue();
            if (tokenValue.equals("int") || tokenValue.equals("float") ||
                    tokenValue.equals("boolean") || tokenValue.equals("char") ||
                    tokenValue.equals("string") || tokenValue.equals("void")) {
                int savedPosition = currentToken;

                RULE_TYPES();

                if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
                    currentToken++;
                    if (tokens.get(currentToken).getValue().equals("(")) {
                        currentToken = savedPosition;
                        RULE_METHODS();
                    } else {
                        currentToken = savedPosition;
                        RULE_GLOBAL_ATTRIBUTE();
                    }
                } else {
                    error(10);
                }
            } else {
                error(13);
            }
        }
        if (tokens.get(currentToken).getValue().equals("}")) {
            currentToken++;
            System.out.println("- }");
        }
    }

    public void RULE_BODY() {
        System.out.println("-- RULE_BODY");
        if (!FirstsSet.body().contains(tokens.get(currentToken).getType())) {
            error(12);
        }
        while (!tokens.get(currentToken).getValue().equals("}")  ) {
            String tokenValue = tokens.get(currentToken).getValue();
            String tokenType = tokens.get(currentToken).getType();

            if (tokenValue.equals("int") || tokenValue.equals("float") ||
                    tokenValue.equals("boolean") || tokenValue.equals("char") ||
                    tokenValue.equals("string")) {
                RULE_VARIABLE();
            } else if (tokenType.equals("IDENTIFIER")) {
                String nextValue = tokens.get(currentToken + 1).getValue();

                if (nextValue.equals("=")) {
                    RULE_ASSIGNMENT();
                } else if (tokenValue.equals("if")) {
                    RULE_IF();
                } else if (tokenValue.equals("while")) {
                    System.out.println("-- WHILE");
                    RULE_WHILE();
                } else if (tokenValue.equals("for")) {
                    System.out.println("-- FOR");
                    RULE_FOR();
                } else if (tokenValue.equals("switch")) {
                    System.out.println("-- SWITCH");
                    RULE_SWITCH();
                } else if (tokenValue.equals("return")) {
                    RULE_RETURN();
                } else if (tokenValue.equals("do")) {
                    System.out.println("-- DO WHILE");
                    RULE_DOWHILE();
                } else if (nextValue.equals("(")) {
                    System.out.println("-- (");
                    RULE_CALL();
                } else {
                    break;
                }
            }else if(tokenValue.equals(";")){
                System.out.println("-- ;");
                break;
            }
            else {
                error(14);
            }
        }
    }


    public String RULE_EXPRESSION() {
        System.out.println("--- RULE_EXPRESSION");
        if (!FirstsSet.expression().contains(tokens.get(currentToken).getType()))  {
            error(12);
            return "error";
        }
        String leftType = RULE_X();
        while (tokens.get(currentToken).getValue().equals("||")) {
            String operator = tokens.get(currentToken).getValue();
            currentToken++;
            System.out.println("--- ||");
            String rightType = RULE_X();
            leftType = SemanticAnalizer.checkBinaryOperation(leftType, operator, rightType);
        }
        return leftType;
    }

    public String RULE_X() {
        System.out.println("---- RULE_X");
        if (!FirstsSet.x().contains(tokens.get(currentToken).getType())) {
            error(12);
            return "error";
        }
        String leftType = RULE_Y();
        while (tokens.get(currentToken).getValue().equals("&&")) {
            String operator = tokens.get(currentToken).getValue();
            currentToken++;
            System.out.println("---- &&");
            String rightType = RULE_Y();
            leftType = SemanticAnalizer.checkBinaryOperation(leftType, operator, rightType);
        }
        return leftType;
    }

    public String RULE_Y() {
        System.out.println("----- RULE_Y");
        if (!FirstsSet.y().contains(tokens.get(currentToken).getType())) {
            error(12);
            return "error";
        }
        String resultType = null;
        boolean hasUnaryNot = false;

        while (tokens.get(currentToken).getValue().equals("!")) {
            hasUnaryNot = true;
            currentToken++;
            System.out.println("----- !");
        }
        resultType = RULE_R();
        if (hasUnaryNot) {
            resultType = SemanticAnalizer.checkUnaryOperation("!", resultType);
        }
        return resultType;
    }

    public String RULE_R() {
        System.out.println("------ RULE_R");
        if (!FirstsSet.R().contains(tokens.get(currentToken).getType())) {
            error(12);
            return "error";
        }
        String leftType = RULE_E();
        while (tokens.get(currentToken).getValue().equals("<")
                | tokens.get(currentToken).getValue().equals(">")
                | tokens.get(currentToken).getValue().equals("==")
                | tokens.get(currentToken).getValue().equals("!=")
        ) {
            String operator = tokens.get(currentToken).getValue();
            currentToken++;
            System.out.println("------ relational operator");
            String rightType = RULE_E();
            leftType = SemanticAnalizer.checkBinaryOperation(leftType, operator, rightType);
        }
        return leftType;
    }

    public String RULE_E() {
        System.out.println("------- RULE_E");
        if (!FirstsSet.E().contains(tokens.get(currentToken).getType())) {
            error(12);
            return "error";
        }
        String leftType = RULE_A();

        while (tokens.get(currentToken).getValue().equals("-")
                | tokens.get(currentToken).getValue().equals("+")
        ) {
            String operator = tokens.get(currentToken).getValue();
            currentToken++;
            System.out.println("------- + or -");
            String rightType = RULE_A();
            leftType = SemanticAnalizer.checkBinaryOperation(leftType, operator, rightType);
        }
        return leftType;
    }

    public String RULE_A() {
        System.out.println("-------- RULE_A");
        if (!FirstsSet.A().contains(tokens.get(currentToken).getType())) {
            error(12);
            return "error";
        }

        String leftType = RULE_B();

        while (tokens.get(currentToken).getValue().equals("/")
                | tokens.get(currentToken).getValue().equals("*")
        ) {
            String operator = tokens.get(currentToken).getValue();
            currentToken++;
            System.out.println("-------- * or /");
            String rightType = RULE_B();

            leftType = SemanticAnalizer.checkBinaryOperation(leftType, operator, rightType);
        }

        return leftType;
    }

    public String RULE_B() {
        System.out.println("--------- RULE_B");
        if (!FirstsSet.B().contains(tokens.get(currentToken).getType())) {
            error(12);
            return "error";
        }

        boolean hasUnaryMinus = false;

        if (tokens.get(currentToken).getValue().equals("-")) {
            hasUnaryMinus = true;
            currentToken++;
            System.out.println("--------- -");
        }

        String resultType = RULE_C();

        if (hasUnaryMinus) {
            resultType = SemanticAnalizer.checkUnaryOperation("-", resultType);
        }

        return resultType;
    }

    public String RULE_C() {
        System.out.println("---------- RULE_C");
        if (!FirstsSet.C().contains(tokens.get(currentToken).getType())) {
            error(12);
            return "error";
        }

        if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
            String tokenValue = tokens.get(currentToken).getValue();
            currentToken++;
            System.out.println("---------- IDENTIFIER");

            if (tokens.get(currentToken).getValue().equals("(")) {
                currentToken--;
                System.out.println("---------- Function Call");
                RULE_CALL();
                return "int";
            } else {
                if (SemanticAnalizer.CheckVariableUsage(tokenValue)) {
                    return SemanticAnalizer.getVariableType(tokenValue);
                } else {
                    return "error";
                }
            }
        } else {
            String tokenType = SemanticAnalizer.getTokenType(tokens.get(currentToken));

            currentToken++;

            if (tokens.get(currentToken - 1).getType().equals("INTEGER")) {
                System.out.println("---------- INTEGER");
            } else if (tokens.get(currentToken - 1).getType().equals("FLOAT")) {
                System.out.println("---------- FLOAT");
            } else if (tokens.get(currentToken - 1).getType().equals("OCTAL")) {
                System.out.println("---------- OCTAL");
            } else if (tokens.get(currentToken - 1).getType().equals("HEXADECIMAL")) {
                System.out.println("---------- HEXADECIMAL");
            } else if (tokens.get(currentToken - 1).getType().equals("EXPONENTIAL")) {
                System.out.println("---------- EXPONENTIAL");
            } else if (tokens.get(currentToken - 1).getType().equals("BINARY")) {
                System.out.println("---------- BINARY");
            } else if (tokens.get(currentToken - 1).getType().equals("STRING")) {
                System.out.println("---------- STRING");
            } else if (tokens.get(currentToken - 1).getType().equals("CHAR")) {
                System.out.println("---------- CHAR");
            } else if (tokens.get(currentToken - 1).getValue().equals("true")) {
                System.out.println("---------- true");
            } else if (tokens.get(currentToken - 1).getValue().equals("false")) {
                System.out.println("---------- false");
            } else if (tokens.get(currentToken - 1).getValue().equals("(")) {
                System.out.println("---------- (");
                String exprType = RULE_EXPRESSION();
                if (tokens.get(currentToken).getValue().equals(")")) {
                    currentToken++;
                    System.out.println("---------- )");
                    return exprType;
                } else {
                    error(4);
                    return "error";
                }
            }

            return tokenType;
        }
    }

    private void error(int error) {
        System.out.println("Error " + error +
                " at line " + tokens.get(currentToken));

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String currentRule = stackTrace[2].getMethodName();

        Set<String> followSet = FollowsSet.FOLLOW_MAP.get(currentRule);
        if (followSet == null) {
            followSet = new HashSet<>(Arrays.asList(";", "}", ")", "$"));
        }

        while (currentToken < tokens.size() && !followSet.contains(tokens.get(currentToken).getValue())) {
            currentToken++;
        }
    }


}

