package com.espertech.esper.epl;
import org.antlr.v4.runtime.tree.ParseTreeProperty;


public class ClassFinder extends EsperEPL2GrammarBaseListener{
	ParseTreeProperty<String> newTexts = new ParseTreeProperty<String>();
	public static String classname = "";

	public void exitClassIdentifier(EsperEPL2GrammarParser.ClassIdentifierContext ctx) { 
		classname = ctx.getChild(0).getText();
	}
}
