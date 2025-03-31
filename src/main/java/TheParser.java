import java.sql.SQLOutput;
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
		System.out.println("-- RULE_METHODS");
		RULE_TYPES();
		if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
			currentToken++;
			System.out.println("- IDENTIFIER");
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
			System.out.println("- )");
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
		System.out.println("-- RULE_TYPES");
		String type = tokens.get(currentToken).getValue();
		if (type.equals("int") || type.equals("float") ||
				type.equals("boolean") || type.equals("char") ||
				type.equals("string") || type.equals("void") ) {
			currentToken++;
			System.out.println("- " + type);
		} else {
			error(12);
		}
	}

	public void RULE_PARAMS(){
		System.out.println("-- RULE_PARAMS");
		while(!tokens.get(currentToken).getValue().equals(")")){
			RULE_TYPES();
			if(tokens.get(currentToken).getType().equals("IDENTIFIER")){
				System.out.println("--IDENTIFIER: " + tokens.get(currentToken).getValue());
				currentToken++;
			}
			else{
				error(16);
			}
			if(tokens.get(currentToken).getValue().equals(",")){
				System.out.println("--IDENTIFIER: " + tokens.get(currentToken).getValue());
				currentToken++;
				RULE_PARAMS();
			}
		}


	}
  
	public void RULE_ASSIGNMENT(){
		System.out.println("--RULE_ASSIGNMENT");
		if (tokens.get(currentToken).getType().equals("IDENTIFIER"))
		{
			System.out.println("--IDENTIFIER");
			currentToken++;
			if (tokens.get(currentToken).getValue().equals("=")) {
				System.out.println("-- =");
				currentToken++;
				RULE_EXPRESSION();
				if (tokens.get(currentToken).getValue().equals(";")) {
					System.out.println("-- ;");
					currentToken++;
				} else {
					error(11);
				}
			} else {
				error(10);
			}
		}else {
			error(10);
		}
	}
  
	public void RULE_VARIABLE() {
		System.out.println("--RULE_VARIABLE");
		RULE_TYPES();
		if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
			System.out.println("--IDENTIFIER");
			String variableName = tokens.get(currentToken).getValue();
			currentToken++;
			if (tokens.get(currentToken).getValue().equals("=")) {
				System.out.println("-- =");
				currentToken++;
				RULE_EXPRESSION();
			}
			else if (tokens.get(currentToken).getValue().equals(";")) {
				System.out.println("-- ;");
				currentToken++;
			} else {
				error(9);
			}
		} else {
			error(10);
		}
	}

	public void RULE_RETURN(){
		System.out.println("-- RULE_RETURN");
		if (tokens.get(currentToken).getValue().equals("return")) {
			currentToken++;
			if (!tokens.get(currentToken).getValue().equals(";")) {
				System.out.println("-- " + tokens.get(currentToken).getValue());
				RULE_EXPRESSION();
			}
			else{
				currentToken++;
				System.out.println("- ;");
			}

		} else{
			error(19);
		}
	}

	public void RULE_CALL(){
		System.out.println("-- RULE_CALL");
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
		if (!tokens.get(currentToken).getValue().equals(")")) {
			System.out.println("-- )");
			RULE_EXPRESSION();
			while (tokens.get(currentToken).getValue().equals(",")) {
				currentToken++;
				RULE_EXPRESSION();
			}
		}
	}

	public void RULE_FOR(){
		System.out.println("-- RULE_FOR");
		currentToken++;
		if (tokens.get(currentToken).getValue().equals("(")){
			System.out.println("- (");
			currentToken++;
			RULE_VARIABLE();
			RULE_EXPRESSION();
			if (tokens.get(currentToken).getValue().equals(";")){
				System.out.println("- ;");
				RULE_EXPRESSION();
				if(tokens.get(currentToken).getValue().equals(")")){
					currentToken++;
					System.out.println("- )");
					if(tokens.get(currentToken).getValue().equals("{")){
						currentToken++;
						System.out.println("- {");
						while(!tokens.get(currentToken).getValue().equals("}")){
							RULE_BODY();
							currentToken++;
						}
						currentToken++;
						System.out.println("- }");
					}else{
						error(19);
					}
//					RULE_BODY();
//					if(tokens.get(currentToken).getValue().equals("}")){
//						currentToken++;
//						System.out.println("- }");
//					}else{
//						error(2);
//					}
				}
			} else{
				error(7);
			}
		} else {
			error(100);
		}

	}

	public void RULE_SWITCH(){

	}

	public void RULE_DOWHILE() {
		System.out.println("-- RULE_DO_WHILE");
		currentToken++;
		if (tokens.get(currentToken).getValue().equals("{")) {
			System.out.println("-- {");
			currentToken++;
			currentToken++;
			System.out.println("- {");
			while(!tokens.get(currentToken).getValue().equals("}")){
				RULE_BODY();
				currentToken++;
			}
			currentToken++;
			System.out.println("- }");
		} else {
			error(31);
		}
//		RULE_BODY();
//		if (tokens.get(currentToken).getValue().equals("}")) {
//			currentToken++;
//			System.out.println("- }");
//		} else {
//			error(2);
//		}
		if (tokens.get(currentToken).getValue().equals("while")) {
			System.out.println("-- while");
			if (tokens.get(currentToken).getValue().equals("(")) {
				System.out.println("-- (");
				currentToken++;
			} else {
				error(31);
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
		if (tokens.get(currentToken).getValue().equals("{")) {
			System.out.println("-- {");
			currentToken++;
			RULE_BODY();
			if (tokens.get(currentToken).getValue().equals("}")) {
				currentToken++;
				System.out.println("-- }");
			} else {
				error(2);
			}
		}
		if (tokens.get(currentToken).getValue().equals("else")) {
			currentToken++;
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
			else if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
				System.out.println("-- IDENTIFIER");
				currentToken++;
			}
		} else {
			error(25);
		}



	}

	public void RULE_WHILE(){
		System.out.println("-- RULE_WHILE");
		currentToken++;
		if (tokens.get(currentToken).getValue().equals("(")) {
			System.out.println("-- (");
			currentToken++;
		} else {
			error(21);
		}
		RULE_EXPRESSION();
		if (tokens.get(currentToken).getValue().equals(")")) {
			currentToken++;
			System.out.println("- )");
		} else {
			error(2);
		}

		if (tokens.get(currentToken).getValue().equals("{")) {
			System.out.println("-- {");
			currentToken++;
			while(!tokens.get(currentToken).getValue().equals("}")){
				RULE_BODY();
				currentToken++;
			}
			currentToken++;
			System.out.println("- }");
		} else {
			error(47);
		}
//		RULE_BODY();
//		if (tokens.get(currentToken).getValue().equals("}")) {
//			currentToken++;
//			System.out.println("- }");
//		} else {
//			error(2);
//		}
	}

	public void run() {
		RULE_PROGRAM();
	}

	private void RULE_PROGRAM() {
		System.out.println("- RULE_PROGRAM");
		if (tokens.get(currentToken).getValue().equals("class")) {
			currentToken++;
			System.out.println("- class");
		}
		//else {
		//	error(37);
		//}
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

	public void RULE_BODY() {
		System.out.println("-- RULE_BODY");
		while (!tokens.get(currentToken).getValue().equals("}")) {
			String tokenValue = tokens.get(currentToken).getValue();
			String tokenType = tokens.get(currentToken).getType();

			if (tokenValue.equals("int") || tokenValue.equals("float") ||
					tokenValue.equals("boolean") || tokenValue.equals("char") ||
					tokenValue.equals("string")) {
				RULE_VARIABLE();
			}
			else if (tokenType.equals("IDENTIFIER")) {
				String nextValue = tokens.get(currentToken + 1).getValue();

				if (nextValue.equals("=")) {
					RULE_ASSIGNMENT();
				}
				else if (tokenValue.equals("if")) {
					RULE_IF();
				}else if (tokenValue.equals("while")) {
					System.out.println("-- WHILE");
					RULE_WHILE();
				}
				else if (tokenValue.equals("for")) {
					System.out.println("-- FOR");
					RULE_FOR();
				}
				else if (tokenValue.equals("switch")) {
					System.out.println("-- SWITCH");
					RULE_SWITCH();
				}
				else if(tokenValue.equals("return")){
					RULE_RETURN();
				}
				else if(tokenValue.equals("do")){
					System.out.println("-- DO WHILE");
					RULE_DOWHILE();
				}
				else if (nextValue.equals("(")) {
					System.out.println("-- (");
					RULE_CALL();
				}
				else {
					error(14);
				}
			}

			else {
				error(14);
			}
		}
	}

	public void RULE_EXPRESSION() {
		System.out.println("--- RULE_EXPRESSION");
		RULE_X();
		while (tokens.get(currentToken).getValue().equals("||")) {
			currentToken++;
			System.out.println("--- ||");
			RULE_X();
		}
	}

	public void RULE_X() {
		System.out.println("---- RULE_X");
		RULE_Y();
		while (tokens.get(currentToken).getValue().equals("&&")) {
			currentToken++;
			System.out.println("---- &&");
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
			if (tokens.get(currentToken).getValue().equals("(")) {
				currentToken--;
				System.out.println("---------- Function Call");
				RULE_CALL();
			}
		} else if (tokens.get(currentToken).getType().equals("INTEGER")) {
			currentToken++;
			System.out.println("---------- INTEGER");
		}  else if (tokens.get(currentToken).getType().equals("FLOAT")) {
			currentToken++;
			System.out.println("---------- FLOAT");
		} else if (tokens.get(currentToken).getType().equals("OCTAL")) {
			currentToken++;
			System.out.println("---------- OCTAL");
		}  else if (tokens.get(currentToken).getType().equals("HEXADECIMAL")) {
			currentToken++;
			System.out.println("---------- HEXADECIMAL");
		}  else if (tokens.get(currentToken).getType().equals("EXPONENTIAL")) {
			currentToken++;
			System.out.println("---------- EXPONENTIAL");
		}  else if (tokens.get(currentToken).getType().equals("BINARY")) {
			currentToken++;
			System.out.println("---------- BINARY");
		}
		else if (tokens.get(currentToken).getType().equals("STRING")) {
			currentToken++;
			System.out.println("---------- STRING");
		} else if (tokens.get(currentToken).getType().equals("CHAR")) {
			currentToken++;
			System.out.println("---------- CHAR");
		}  else if (tokens.get(currentToken).getValue().equals("true")) {
			currentToken++;
			System.out.println("---------- true");
		} else if (tokens.get(currentToken).getValue().equals("false")) {
			currentToken++;
			System.out.println("---------- false");
		}
		else if (tokens.get(currentToken).getValue().equals("(")) {
			currentToken++;
			System.out.println("---------- (");
			RULE_EXPRESSION();
			if (tokens.get(currentToken).getValue().equals(")")) {
				currentToken++;
				System.out.println("---------- )");
			} else {
				error(4);
			}
		}
		else {
			error(5);
		}
	}

	private void error(int error) {
		System.out.println("Error " + error +
				" at line " + tokens.get(currentToken));
		System.exit(1);
	}

}

