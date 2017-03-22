package QueryOptimizer;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import com.espertech.esper.epl.ClassFinder;
import com.espertech.esper.epl.EsperEPL2GrammarLexer;
import com.espertech.esper.epl.EsperEPL2GrammarParser;
import com.espertech.esper.epl.ProposedPreProcessor;

public class Antlr {
	private	String inputepl;
	private String opepl;
	private String[] selectList;
	private String eventclass;
	
	public void setOptimize(){
		EsperEPL2GrammarLexer lexer = new EsperEPL2GrammarLexer(
				new org.antlr.v4.runtime.ANTLRInputStream(inputepl));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		EsperEPL2GrammarParser parser = new EsperEPL2GrammarParser(tokens);
		ParseTree tree = parser.startEPLExpressionRule();
		ParseTreeWalker walker = new ParseTreeWalker();
		ProposedPreProcessor pprocessor = new ProposedPreProcessor();
		walker.walk(new ClassFinder(), tree);
		walker.walk(pprocessor, tree);
		opepl = pprocessor.getEPL();
		selectList = pprocessor.getSelectList();
		eventclass  = pprocessor.getEventClass();
		
	}
	
	public String getOptimzedEPL(){
		return opepl;
	}
	
	public String[] getSelectList(){
		return selectList;
	}
	
	public String getEventClass(){
		return eventclass;
	}
	
	public void setInputEPL(String epl){
		inputepl = epl;
	}
	
}
