import java.util.Vector;

public class TheParser {

	private Vector<TheToken> tokens;
	private int currentToken;

	public TheParser(Vector<TheToken> tokens) {
		this.tokens = tokens;
		currentToken = 0;
	}

	public void run() {
		RULE_PROGRAM();
	}

	private void RULE_PROGRAM() {
		System.out.println("- RULE_PROGRAM");
		// Handle global attributes before main block
		while (currentToken < tokens.size() &&
				!tokens.get(currentToken).getValue().equals("{")) {
			RULE_GLOBAL_ATTRIBUTE();
		}

		if (tokens.get(currentToken).getValue().equals("{")) {
			currentToken++;
			System.out.println("- {");
		} else {
			error(1);
		}
		RULE_BODY();
		if (tokens.get(currentToken).getValue().equals("}")) {
			currentToken++;
			System.out.println("- }");
		} else {
			error(2);
		}
	}

	private void RULE_GLOBAL_ATTRIBUTE() {
		System.out.println("- RULE_GLOBAL_ATTRIBUTE");
		RULE_TYPES();
		if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
			currentToken++;
			System.out.println("- IDENTIFIER");
			if (tokens.get(currentToken).getValue().equals("=")) {
				currentToken++;
				System.out.println("- =");
				RULE_EXPRESSION();
			}
			if (tokens.get(currentToken).getValue().equals(";")) {
				currentToken++;
				System.out.println("- ;");
			} else {
				error(6);
			}
		} else {
			error(7);
		}
	}

	private void RULE_TYPES() {
		System.out.println("- RULE_TYPES");
		String type = tokens.get(currentToken).getValue();
		if (type.equals("INTEGER") || type.equals("FLOAT") ||
				type.equals("BOOLEAN") || type.equals("CHAR") ||
				type.equals("STRING")) {
			currentToken++;
			System.out.println("- " + type);
		} else {
			error(8);
		}
	}

	public void RULE_BODY() {
		System.out.println("-- RULE_BODY");
		while (!tokens.get(currentToken).getValue().equals("}")) {
			String tokenValue = tokens.get(currentToken).getValue();
			if (tokenValue.equals("if")) {
				RULE_IF();
			} else if (tokenValue.equals("while")) {
				RULE_WHILE();
			} else if (tokenValue.equals("do")) {
				RULE_DO_WHILE();
			} else if (tokenValue.equals("for")) {
				RULE_FOR();
			} else if (tokenValue.equals("switch")) {
				RULE_SWITCH();
			} else if (tokenValue.equals("return")) {
				RULE_RETURN();
			} else if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
				if (tokens.size() > currentToken + 1 &&
						tokens.get(currentToken + 1).getValue().equals("(")) {
					RULE_CALL_METHOD();
				} else {
					RULE_ASSIGNMENT();
				}
			} else {
				RULE_EXPRESSION();
			}

			if (tokens.get(currentToken).getValue().equals(";")) {
				System.out.println("-- ;");
				currentToken++;
			} else if (!tokenValue.equals("}") &&
					!tokenValue.equals("else")) {
				error(3);
			}
		}
	}

	private void RULE_IF() {
		System.out.println("---- RULE_IF");
		currentToken++; // consume 'if'
		System.out.println("---- if");
		if (tokens.get(currentToken).getValue().equals("(")) {
			currentToken++;
			System.out.println("---- (");
			RULE_EXPRESSION();
			if (tokens.get(currentToken).getValue().equals(")")) {
				currentToken++;
				System.out.println("---- )");
				RULE_BODY();
				if (tokens.get(currentToken).getValue().equals("else")) {
					currentToken++;
					System.out.println("---- else");
					RULE_BODY();
				}
			} else {
				error(9);
			}
		} else {
			error(10);
		}
	}

	private void RULE_WHILE() {
		System.out.println("---- RULE_WHILE");
		currentToken++; // consume 'while'
		System.out.println("---- while");
		if (tokens.get(currentToken).getValue().equals("(")) {
			currentToken++;
			System.out.println("---- (");
			RULE_EXPRESSION();
			if (tokens.get(currentToken).getValue().equals(")")) {
				currentToken++;
				System.out.println("---- )");
				RULE_BODY();
			} else {
				error(9);
			}
		} else {
			error(10);
		}
	}

	private void RULE_DO_WHILE() {
		System.out.println("---- RULE_DO_WHILE");
		currentToken++; // consume 'do'
		System.out.println("---- do");
		RULE_BODY();
		if (tokens.get(currentToken).getValue().equals("while")) {
			currentToken++;
			System.out.println("---- while");
			if (tokens.get(currentToken).getValue().equals("(")) {
				currentToken++;
				System.out.println("---- (");
				RULE_EXPRESSION();
				if (tokens.get(currentToken).getValue().equals(")")) {
					currentToken++;
					System.out.println("---- )");
					if (tokens.get(currentToken).getValue().equals(";")) {
						currentToken++;
						System.out.println("---- ;");
					} else {
						error(11);
					}
				} else {
					error(9);
				}
			} else {
				error(10);
			}
		} else {
			error(12);
		}
	}

	private void RULE_FOR() {
		System.out.println("---- RULE_FOR");
		currentToken++; // consume 'for'
		System.out.println("---- for");
		if (tokens.get(currentToken).getValue().equals("(")) {
			currentToken++;
			System.out.println("---- (");
			// Initialization
			if (!tokens.get(currentToken).getValue().equals(";")) {
				if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
					RULE_ASSIGNMENT();
				} else {
					RULE_EXPRESSION();
				}
			}
			if (tokens.get(currentToken).getValue().equals(";")) {
				currentToken++;
				System.out.println("---- ;");
			} else {
				error(11);
			}
			// Condition
			if (!tokens.get(currentToken).getValue().equals(";")) {
				RULE_EXPRESSION();
			}
			if (tokens.get(currentToken).getValue().equals(";")) {
				currentToken++;
				System.out.println("---- ;");
			} else {
				error(11);
			}
			// Increment
			if (!tokens.get(currentToken).getValue().equals(")")) {
				RULE_EXPRESSION();
			}
			if (tokens.get(currentToken).getValue().equals(")")) {
				currentToken++;
				System.out.println("---- )");
				RULE_BODY();
			} else {
				error(9);
			}
		} else {
			error(10);
		}
	}

	private void RULE_SWITCH() {
		System.out.println("---- RULE_SWITCH");
		currentToken++; // consume 'switch'
		System.out.println("---- switch");
		if (tokens.get(currentToken).getValue().equals("(")) {
			currentToken++;
			System.out.println("---- (");
			RULE_EXPRESSION();
			if (tokens.get(currentToken).getValue().equals(")")) {
				currentToken++;
				System.out.println("---- )");
				if (tokens.get(currentToken).getValue().equals("{")) {
					currentToken++;
					System.out.println("---- {");
					while (!tokens.get(currentToken).getValue().equals("}")) {
						if (tokens.get(currentToken).getValue().equals("case")) {
							currentToken++;
							System.out.println("---- case");
							RULE_EXPRESSION();
							if (tokens.get(currentToken).getValue().equals(":")) {
								currentToken++;
								System.out.println("---- :");
								while (!tokens.get(currentToken).getValue().equals("case") &&
										!tokens.get(currentToken).getValue().equals("default") &&
										!tokens.get(currentToken).getValue().equals("}")) {
									RULE_STATEMENT();
								}
							} else {
								error(13);
							}
						} else if (tokens.get(currentToken).getValue().equals("default")) {
							currentToken++;
							System.out.println("---- default");
							if (tokens.get(currentToken).getValue().equals(":")) {
								currentToken++;
								System.out.println("---- :");
								while (!tokens.get(currentToken).getValue().equals("}")) {
									RULE_STATEMENT();
								}
							} else {
								error(13);
							}
						}
					}
					if (tokens.get(currentToken).getValue().equals("}")) {
						currentToken++;
						System.out.println("---- }");
					} else {
						error(2);
					}
				} else {
					error(1);
				}
			} else {
				error(9);
			}
		} else {
			error(10);
		}
	}

	private void RULE_ASSIGNMENT() {
		System.out.println("---- RULE_ASSIGNMENT");
		if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
			currentToken++;
			System.out.println("---- IDENTIFIER");
			if (tokens.get(currentToken).getValue().equals("=")) {
				currentToken++;
				System.out.println("---- =");
				RULE_EXPRESSION();
			} else {
				error(14);
			}
		} else {
			error(15);
		}
	}

	private void RULE_RETURN() {
		System.out.println("---- RULE_RETURN");
		currentToken++; // consume 'return'
		System.out.println("---- return");
		if (!tokens.get(currentToken).getValue().equals(";")) {
			RULE_EXPRESSION();
		}
	}

	private void RULE_CALL_METHOD() {
		System.out.println("---- RULE_CALL_METHOD");
		if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
			currentToken++;
			System.out.println("---- IDENTIFIER");
			if (tokens.get(currentToken).getValue().equals("(")) {
				currentToken++;
				System.out.println("---- (");
				RULE_PARAM_VALUES();
				if (tokens.get(currentToken).getValue().equals(")")) {
					currentToken++;
					System.out.println("---- )");
				} else {
					error(9);
				}
			} else {
				error(10);
			}
		} else {
			error(15);
		}
	}

	private void RULE_PARAM_VALUES() {
		System.out.println("---- RULE_PARAM_VALUES");
		while (!tokens.get(currentToken).getValue().equals(")")) {
			RULE_EXPRESSION();
			if (tokens.get(currentToken).getValue().equals(",")) {
				currentToken++;
				System.out.println("---- ,");
			} else {
				break;
			}
		}
	}

	private void RULE_STATEMENT() {
		String tokenValue = tokens.get(currentToken).getValue();
		if (tokenValue.equals("if")) {
			RULE_IF();
		} else if (tokenValue.equals("while")) {
			RULE_WHILE();
		} else if (tokenValue.equals("do")) {
			RULE_DO_WHILE();
		} else if (tokenValue.equals("for")) {
			RULE_FOR();
		} else if (tokenValue.equals("switch")) {
			RULE_SWITCH();
		} else if (tokenValue.equals("return")) {
			RULE_RETURN();
		} else if (tokens.get(currentToken).getType().equals("IDENTIFIER")) {
			if (tokens.get(currentToken + 1).getValue().equals("(")) {
				RULE_CALL_METHOD();
			} else {
				RULE_ASSIGNMENT();
			}
		} else {
			RULE_EXPRESSION();
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
			System.out.println("---- &");
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
		String op = tokens.get(currentToken).getValue();
		while (op.equals("<") || op.equals(">") ||
				op.equals("<=") || op.equals(">=") ||
				op.equals("==") || op.equals("!=")) {
			currentToken++;
			System.out.println("------ " + op);
			RULE_E();
			op = tokens.get(currentToken).getValue();
		}
	}

	public void RULE_E() {
		System.out.println("------- RULE_E");
		RULE_A();
		while (tokens.get(currentToken).getValue().equals("+") ||
				tokens.get(currentToken).getValue().equals("-")) {
			currentToken++;
			System.out.println("------- " + tokens.get(currentToken-1).getValue());
			RULE_A();
		}
	}

	public void RULE_A() {
		System.out.println("-------- RULE_A");
		RULE_B();
		while (tokens.get(currentToken).getValue().equals("*") ||
				tokens.get(currentToken).getValue().equals("/") ||
				tokens.get(currentToken).getValue().equals("%")) {
			currentToken++;
			System.out.println("-------- " + tokens.get(currentToken-1).getValue());
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
				RULE_CALL_METHOD();
			}
		} else if (tokens.get(currentToken).getType().equals("INTEGER") ||
				tokens.get(currentToken).getType().equals("FLOAT") ||
				tokens.get(currentToken).getType().equals("BOOLEAN") ||
				tokens.get(currentToken).getType().equals("CHAR") ||
				tokens.get(currentToken).getType().equals("STRING")) {
			currentToken++;
			System.out.println("---------- " + tokens.get(currentToken-1).getType());
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