import java.io.*;
import java.util.Objects;
import java.util.Vector;

/**
 * Lexer class to analyze the input file
 * This one is an initial version that uses a DFA to recognize binary numbers
 *
 * @author organistaf
 * @author GabrielGuerra06
 * @author DJ2513
 * @author javiergs
 * @version 0.1
 */
public class TheLexer {

    private File file;
    private TheAutomata dfa;
    private Vector<TheToken> tokens;


    public TheLexer(File file) {
        this.file = file;
        tokens = new Vector<>();
        dfa = new TheAutomata();

        ////////////////////Accepted states for S0/////////////////////////////////////////////////
//		Delimiters
        dfa.addTransition("S0", "{", "S9");
        dfa.addTransition("S0", "}", "S9");
        dfa.addTransition("S0", "(", "S9");
        dfa.addTransition("S0", ")", "S9");
        dfa.addTransition("SO", "[", "S9");
        dfa.addTransition("S0", "]", "S9");
        dfa.addTransition("S0", ",", "S9");
        dfa.addTransition("S0", ";", "S9");

//		Identifiers
        dfa.addChars("S0", 'a', 'z', "S10");
        dfa.addChars("S0", 'A', 'Z', "S10");
        dfa.addTransition("S0", "$",  "S10");
        dfa.addTransition("S0", "_",  "S10");
        dfa.addTransition("S0", "@",  "S10");

        //int
        dfa.addNumbers("S0", '1', '9',  "S11");
        dfa.addTransition("S0", "0",  "S19");

        //OPERATOR
        dfa.addTransition("S0", "=",  "S18");
        dfa.addTransition("S0", "+",  "S18");
        dfa.addTransition("S0", "-",  "S18");
        dfa.addTransition("S0", "*",  "S18");
        dfa.addTransition("S0", "/",  "S18");
        dfa.addTransition("S0", "%",  "S18");
        dfa.addTransition("S0", "<",  "S18");
        dfa.addTransition("S0", ">",  "S18");

        dfa.addTransition("S0", ".",  "S2");

//		String
        dfa.addTransition("S0", " \" ", "S3");
        dfa.addTransition("S0", " ' ", "S4");

        //Space
        dfa.addTransition("S0", "  ", "S0");

        //////////////////////////////////////////////////////////////

        /////////////////////Accepted states for SX////////////////////////////
        dfa.addNumbers("SX", '0','9', "S13");
        ///////////////////////////////////////////////////////////////////////


        /////////////////////Accepted states for S2////////////////////////////
        dfa.addNumbers("S2", '0','9', "S12");
        ///////////////////////////////////////////////////////////////////////

        /////////////////////Accepted states for S3 and S4 (STRINGS)////////////////////////////
        for (char c = 32; c <= 126; c++) { // ASCII printable characters
            if (c != '\'') dfa.addTransition("S4", String.valueOf(c), "S22");
            if (c != '"') dfa.addTransition("S3", String.valueOf(c), "S20");
        }
        System.out.println();
        dfa.addTransition("S20", "\"", "S24");
        dfa.addTransition("S22", "'", "S24");
        for (char c = 32; c <= 126; c++) { // ASCII printable characters
            if (c != '\'') dfa.addTransition("S22", String.valueOf(c), "S23");
            if (c != '"') dfa.addTransition("S20", String.valueOf(c), "S21");
        }
        for (char c = 32; c <= 126; c++) { // ASCII printable characters
            if (c != '"') dfa.addTransition("S21", String.valueOf(c), "S21");
            if (c != '\'') dfa.addTransition("S23", String.valueOf(c), "S23");
        }

        dfa.addTransition("S21", "\"", "S17");
        dfa.addTransition("S23", "'", "S17");
        ///////////////////////////////////////////////////////////////////////

        /////////////////////Accepted states for S5////////////////////////////
        dfa.addTransition("S5", "+", "SX");
        dfa.addTransition("S5", "-", "SX");
        ///////////////////////////////////////////////////////////////////////

        /////////////////////Accepted states for S5////////////////////////////
        dfa.addNumbers("S6", '0', '9', "S14");
        dfa.addChars("S6", 'A', 'F', "S14");
        dfa.addChars("S6", 'a', 'f', "S14");
        ///////////////////////////////////////////////////////////////////////


        /////////////////////Accepted states for S7////////////////////////////
        dfa.addNumbers("S7", '0','1',"S15");
        ///////////////////////////////////////////////////////////////////////



        ///////////////Accepted states for S_Identificador//////////////////////
        dfa.addChars("S10", 'A', 'Z', "S10");
        dfa.addChars("S10", 'a', 'z', "S10");
        dfa.addTransition("S10", "_", "S10");
        dfa.addNumbers("S10", '0', '9',"S10");
        dfa.addTransition("S10", ".", "S10");
        ///////////////////////////////////////////////////////////////////////


        ////////////////////Accepted states for S_Int///////////////////////////
        dfa.addTransition("S19", "E", "S5");
        dfa.addTransition("S19", "e", "S5");
        dfa.addTransition("S19", ".", "S12");
        dfa.addTransition("S19", "X", "S6");
        dfa.addTransition("S19", "x", "S6");
        dfa.addTransition("S19", "B", "S7");
        dfa.addTransition("S19", "b", "S7");
        dfa.addNumbers("S19", '0', '7',"S16");

        ///////////////////////////////////////////////////////////////////////

        ////////////////////Accepted states for S_Float///////////////////////////
        dfa.addNumbers("S12", '0', '9',"S12");
        dfa.addTransition("S12", "E", "S5");
        dfa.addTransition("S12", "e", "S5");
        ///////////////////////////////////////////////////////////////////////
        // S_exp
        dfa.addNumbers("S13", '0','9', "S13");
        // S_hexa
        dfa.addChars("S14", 'a','f', "S14");
        dfa.addChars("S14", 'A','F', "S14");
        dfa.addNumbers("S14", '0','9', "S14");
        // s_bin
        dfa.addNumbers("S15", '0','1', "S15");
        // s_oct
        dfa.addNumbers("S16", '0','7', "S16");
        dfa.addNumbers("S16", '8','9', "S11");
        dfa.addTransition("S16",".", "S12");
        // s_operator
        // nothing
        // s_string
        // nothing


        ////////////////////Accepted states for S_11///////////////////////////
        dfa.addNumbers("S11", '0','9', "S11");
        dfa.addTransition("S11","e", "S5");
        dfa.addTransition("S11","E", "S5");
        dfa.addTransition("S11",".", "S12");


        ///////////////////////ACCEPTED STATES////////////////////////////////
        dfa.addAcceptState("S9", "DELIMITER");
        dfa.addAcceptState("S10", "IDENTIFIER");
        dfa.addAcceptState("S11", "INTEGER");
        dfa.addAcceptState("S12", "FLOAT");
        dfa.addAcceptState("S13", "EXPONENTIAL");
        dfa.addAcceptState("S14", "HEXADECIMAL");
        dfa.addAcceptState("S15", "BINARY");
        dfa.addAcceptState("S16", "OCTAL");
        dfa.addAcceptState("S17", "STRING");
        dfa.addAcceptState("S18", "OPERATOR");
        dfa.addAcceptState("S24", "CHAR");

    }

    public void run() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            algorithm(line);
        }
    }

    private void algorithm(String line) {
        String currentState = "S0";
        String nextState;
        String string = "";
        int index = 0;
        boolean inString = false;
        char stringDelimiter = 0;

        while (index < line.length()) {
            char currentChar = line.charAt(index);
            if (inString) {
                string += currentChar;

                if (currentChar == stringDelimiter) {
                    if(string.length() == 3){
                        tokens.add(new TheToken(string, "CHAR"));
                    }
                    else{
                        tokens.add(new TheToken(string, "STRING"));
                    }
                    inString = false;
                    string = "";
                    currentState = "S0";
                }
            } else if (currentChar == '"' || currentChar == '\'') {
                if (currentState.equals("S10")) {
                    tokens.add(new TheToken(string + currentChar, "ERROR"));
                    string = "";
                    currentState = "S0";
                }
                else{
                    inString = true;
                    stringDelimiter = currentChar;
                    string = "" + currentChar;
                }

            } else if (!(isOperator(currentChar) || isDelimiter(currentChar) || isSpace(currentChar))) {
                nextState = dfa.getNextState(currentState, currentChar);
                string += currentChar;
                currentState = nextState;
            }
            else {
                if (dfa.isAcceptState(currentState)) {
                    String stateName = dfa.getAcceptStateName(currentState);
                    tokens.add(new TheToken(string, stateName));
                }  else if (inString || !Objects.equals(currentState, "S0")) {
                    tokens.add(new TheToken(string, "ERROR"));
                }
                if (isOperator(currentChar)) {
                    tokens.add(new TheToken(currentChar + "", "OPERATOR"));
                } else if (isDelimiter(currentChar)) {
                    tokens.add(new TheToken(currentChar + "", "DELIMITER"));
                }
                currentState = "S0";
                string = "";
            }
            index++;
        }
        // last word
        if (dfa.isAcceptState(currentState) ) {
            String stateName = dfa.getAcceptStateName(currentState);
            tokens.add(new TheToken(string, stateName));

        } else if (inString || currentState != "S0") {
            tokens.add(new TheToken(string, "ERROR"));
        }
    }

    private boolean isSpace(char c) {
        return c == ' ' || c == '\t' || c == '\n';
    }

    private boolean isDelimiter(char c) {
        return c == ',' || c == ';' || c== '(' || c==')' || c=='{' || c== '}' || c=='[' || c==']' ;
    }

    private boolean isOperator(char c) {
        return c == '=' || c == '+' || c == '-' || c == '*' || c == '/';
    }

    public void printTokens() {
        for (TheToken token : tokens) {
            System.out.printf("%10s\t|\t%s\n", token.getValue(), token.getType());
        }
    }

    public Vector<TheToken> getTokens() {
        return tokens;
    }

}
