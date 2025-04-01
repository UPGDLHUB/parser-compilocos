import java.io.File;
import java.io.IOException;
import java.util.Vector;

/**
 * Main class to run the lexer
 *
 * @author javiergs
 * @author OrganistaF
 * @author DJ2513
 * @author GabrielGuerra06
 * @version 1.0
 */
public class TheMain {
	
	public static void main(String[] args) throws IOException {
		File file = new File("src/main/resources/input.txt");
		TheLexer lexer = new TheLexer(file);
		lexer.run();
		lexer.printTokens();
		
		Vector<TheToken> tokens = lexer.getTokens();
		TheParser parser = new TheParser(tokens);
		parser.run();
		
	}
	
}

