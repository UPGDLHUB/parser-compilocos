import java.util.Vector;

public class TheParser {

	private Vector<TheToken> tokens;
	private int currentToken;

	public TheParser(Vector<TheToken> tokens) {
		this.tokens = tokens;
		currentToken = 0;
	}


	public void RULE_GLOBAL_ATTRIBUTE(){

	}

	public void RULE_METHODS(){
		RULE_TYPES();
		if (tokens.get(currentToken).getValue().equals("identifier")) {
			currentToken++;
			System.out.println("- identifier");
		} else {
			error(6);
		}
		if (tokens.get(currentToken).getValue().equals("(")) {
			currentToken++;
			System.out.println("- (");
		} else {
			error(7);
		}
		RULE_PARAMS();
		if (tokens.get(currentToken).getValue().equals(")")) {
			currentToken++;
		} else {
			error(8);
		}
		if (tokens.get(currentToken).getValue().equals("{")) {
			currentToken++;
			System.out.println("- {");
			RULE_BODY();
			if (tokens.get(currentToken).getValue().equals("}")) {
				currentToken++;
				System.out.println("- }");
			}
		}
	}


	public void RULE_TYPES() {
		System.out.println("- RULE_TYPES");
		String type = tokens.get(currentToken).getValue();
		if (type.equals("INTEGER") || type.equals("FLOAT") ||
				type.equals("BOOLEAN") || type.equals("CHAR") ||
				type.equals("STRING") ) {
			currentToken++;
			System.out.println("- " + type);
		} else {
			error(8);
		}
	}
  
	public void RULE_PARAMS(){

	}
  
	public void RULE_ASSIGNMENT(){
		if (tokens.get(currentToken).getType().equals("IDENTIFIER"))
		{
			currentToken++;
			if (tokens.get(currentToken).getValue().equals("=")) {
				currentToken++;
				RULE_EXPRESSION();
			}
		}else {
			error(10);
		}
	}
  
	public void RULE_VARIABLE() {
		RULE_TYPES();
		if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
			String variableName = tokens.get(currentToken).getValue();
			currentToken++;
			if (tokens.get(currentToken).getValue().equals("=")) {
				currentToken++;
				RULE_EXPRESSION();
			}
			if (tokens.get(currentToken).getValue().equals(";")) {
				currentToken++;
				System.out.println("Declared variable: " + variableName);
			} else {
				error(9);
			}
		} else {
			error(10);
		}
	}

	public void RULE_STATEMENT(){

	}
	public void RULE_RETURN(){

	}
	public void RULE_CALL(){

	}


	public void run() {
		RULE_PROGRAM();
	}

	private void RULE_PROGRAM() {
		System.out.println("- RULE_PROGRAM");
		if (tokens.get(currentToken).getValue().equals("class")) {
			currentToken++;
			System.out.println("- class");
		} else {
			error(1);
		}
		if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
			currentToken++;
			System.out.println("- IDENTIFIER");
		} else {
			error(2);
		}
		if (tokens.get(currentToken).getValue().equals("{")) {
			currentToken++;
			System.out.println("- {");
		} else {
			error(3);
		}
		while (!tokens.get(currentToken).getValue().equals("}")) {
			RULE_METHODS();
		}
		if (tokens.get(currentToken).getValue().equals("}")) {
			currentToken++;
			System.out.println("- }");
		}
	}
  
int flag = 0;
  
	public void RULE_BODY() {
		System.out.println("-- RULE_BODY");
		while (!tokens.get(currentToken).getValue().equals("}")) {
			if (tokens.get(currentToken).getType().equals("identifier")) {
				RULE_METHODS();
				flag = 1;
			} else if (tokens.get(currentToken).getType().equals("identifier")) {
				RULE_ASSIGNMENT();
				flag = 1;
			} else if (tokens.get(currentToken).getType().equals("INTEGER")) {
				RULE_VARIABLES();
				flag = 1;
			} else if (tokens.get(currentToken).getValue().equals("return")) {
				RULE_RETURN();
			} else if (tokens.get(currentToken).getValue().equals("while")) {
				RULE_STATEMENT();
			} else if (tokens.get(currentToken).getValue().equals("if")) {
				RULE_STATEMENT();
			} else error(5);

			if (flag == 1){
				//Review comma
			}


//			RULE_EXPRESSION();
//			if (tokens.get(currentToken).getValue().equals(";")) {
//				System.out.println("-- ;");
//				currentToken++;
//			} else {
//				error(3);
//			}
		}
	}

	public void RULE_EXPRESSION() {
		System.out.println("--- RULE_EXPRESSION");
		RULE_X();
		while (tokens.get(currentToken).getValue().equals("|")) {
			currentToken++;
			System.out.println("--- |");
			RULE_X();
		}
	}

	public void RULE_X() {
		System.out.println("---- RULE_X");
		RULE_Y();
		while (tokens.get(currentToken).getValue().equals("&")) {
			currentToken++;
			System.out.println("---- |");
			RULE_Y();
		}
	}

	public void RULE_Y() {
		System.out.println("----- RULE_Y");
		if (tokens.get(currentToken).getValue().equals("!")) {
			currentToken++;
			System.out.println("----- !");
		}
		RULE_R();
	}

	public void RULE_R() {
		System.out.println("------ RULE_R");
		RULE_E();
		while (tokens.get(currentToken).getValue().equals("<")
				| tokens.get(currentToken).getValue().equals(">")
				| tokens.get(currentToken).getValue().equals("==")
				| tokens.get(currentToken).getValue().equals("!=")
		) {
			currentToken++;
			System.out.println("------ relational operator");
			RULE_E();
		}
	}

	public void RULE_E() {
		System.out.println("------- RULE_E");
		RULE_A();
		while (tokens.get(currentToken).getValue().equals("-")
				| tokens.get(currentToken).getValue().equals("+")
		) {
			currentToken++;
			System.out.println("------- + or -");
			RULE_A();
		}

	}

	public void RULE_A() {
		System.out.println("-------- RULE_A");
		RULE_B();
		while (tokens.get(currentToken).getValue().equals("/")
				| tokens.get(currentToken).getValue().equals("*")
		) {
			currentToken++;
			System.out.println("-------- * or /");
			RULE_B();
		}

	}

	public void RULE_B() {
		System.out.println("--------- RULE_B");
		if (tokens.get(currentToken).getValue().equals("-")) {
			currentToken++;
			System.out.println("--------- -");
		}
		RULE_C();
	}

	public void RULE_C() {
		System.out.println("---------- RULE_C");
		if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
			currentToken++;
			System.out.println("---------- IDENTIFIER");
		} else if (tokens.get(currentToken).getType().equals("INTEGER")) {
			currentToken++;
			System.out.println("---------- INTEGER");
		} else if (tokens.get(currentToken).getValue().equals("(")) {
			currentToken++;
			System.out.println("---------- (");
			RULE_EXPRESSION();
			if (tokens.get(currentToken).getValue().equals(")")) {
				currentToken++;
				System.out.println("---------- )");
			} else {
				error(4);
			}
		} else {
			error(5);
		}
	}

	private void error(int error) {
		System.out.println("Error " + error +
				" at line " + tokens.get(currentToken));
		System.exit(1);
	}

}

