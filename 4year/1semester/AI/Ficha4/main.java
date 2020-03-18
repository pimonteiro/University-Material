package tutorial;
import jess.*;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			// Run a Jess program...
	        Rete engine = new Rete();
	        engine.batch("txt.clp");
	        engine.reset();
	        
	        //engine.run();
	        
	        /*
			engine.executeCommand("(deffacts ages (person Rosa 30) (person Maria 5) (person Tiago 33))");
			engine.run();

			engine.executeCommand("(defrule add-person (person ?na ?id) => (assert (name ?na)) && (assert (age ?id)))");
			engine.run();
	         
	        engine.executeCommand("(facts)");
	        engine.run();
			*/
			
			
		} catch (JessException ex) {
			System.err.println(ex);
		}

	}

}
