package com.espertech.esper.epl;

import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.math.MathException;
import org.matheclipse.core.eval.Console;
import java.lang.reflect.Field;
import java.util.TreeSet;

public class ProposedPreProcessor extends EsperEPL2GrammarBaseListener{
	ParseTreeProperty<String> newTexts = new ParseTreeProperty<String>();
	private String eplresult=""; //revised epl 
	private String eventclass;
	private String[] selectlist;
	int streamExstate = 0;
	int selectioncount = 0;
	
	public String getEPL(){
		return eplresult;
	}
	
	public String getEventClass(){
		return eventclass;
	}
	
	public String[] getSelectList(){
		return selectlist;
	}
	
	//remove Duplicate Select element
	public Object[] removeDuplicateArray(String[] array){
		  Object[] removeArray=null;
		  TreeSet ts=new TreeSet();
		  
		  for(int i=0; i<array.length; i++){
			  ts.add(array[i]);
		  }
		  removeArray= ts.toArray();
		  return removeArray;
	   }
	
	
	//remove the string into an Array 
	public String[] removeElementArray(Object[] array, String delete){
		String[] arr = new String[array.length-1];
		int count = 0;
		for(int i=0;i<arr.length;i++){
			if(array[i].equals(delete)){
				continue;
			}
			arr[count++] = (String)array[i];
		}
		return arr;
	}
	
	//distinguish sign : only '=' return true
	public boolean Distinction(String s){
		if(s.contains("=") && s.indexOf("<=")<0 && s.indexOf(">=")<0 && s.indexOf("!=")<0)
			return true;
		else 
			return false;
	}

	
	//the top node with epl result
	public void exitStartEPLExpressionRule(EsperEPL2GrammarParser.StartEPLExpressionRuleContext ctx){
		String startrule;
		startrule = newTexts.get(ctx.eplExpression());
		eplresult = startrule;
		newTexts.put(ctx, startrule);
		
		String temp = newTexts.get(ctx);
		System.out.println(temp);
		System.out.println("-----StartEPLExpressionRule------\n");
	}
	
	//EPL expression  
	public void exitEplExpression(EsperEPL2GrammarParser.EplExpressionContext ctx){
		String eplexp = "";
		eplexp += newTexts.get(ctx.selectExpr()) + " ";
		newTexts.put(ctx, eplexp);
		
		String temp = newTexts.get(ctx);
		System.out.println(temp);
		System.out.println("-----EplExpression------\n");
	}
	
	//selectExpr
	public void exitSelectExpr(EsperEPL2GrammarParser.SelectExprContext ctx){
		String selectexp = "" ;

		//selectClause
		selectexp += "select " + newTexts.get(ctx.selectClause());
		
		//fromClause
		if(ctx.fromClause() != null){
			selectexp += " from " + newTexts.get(ctx.fromClause());
			//remove whereClause and add condition to StreamFilter
			if(streamExstate == 1 && ctx.whereClause() != null) 
			{
				String exp = newTexts.get(ctx.whereClause());
				String resultexp;
				Console utill = new Console();
	            String ip = utill.interpreter(exp);
	                   
	            resultexp =  ip.toString();
				selectexp += "(" + resultexp + ")";
				
			}
		}
				
		//whereClause 
		if(ctx.whereClause() != null && streamExstate == 0){
				selectexp += " where " + newTexts.get(ctx.whereClause());
		}
		
		//groupbyClause
		if(ctx.groupByListExpr() != null){
			String where[];
			String having[];
			String wc = "";
			String hc = "";
			
			// whereClause contains only '=' : 
			if(ctx.whereClause() != null && Distinction(newTexts.get(ctx.whereClause()))){
				wc = newTexts.get(ctx.whereClause());
				where = wc.split("=");
				if(!where[0].equals(newTexts.get(ctx.groupByListExpr()))){
					selectexp += " group by " + newTexts.get(ctx.groupByListExpr());
				}
			}else {
				selectexp += " group by " + newTexts.get(ctx.groupByListExpr());
			}
		
			//havingClause contains only '=' : 
			if(ctx.havingClause() != null &&  Distinction(newTexts.get(ctx.havingClause()))){
				hc = newTexts.get(ctx.havingClause());
				having = hc.split("=");
				
				if(!having[0].equals(newTexts.get(ctx.groupByListExpr()))){
					selectexp += " having " + newTexts.get(ctx.havingClause());
				}
			}
		}
		
		//orderbyListExpr
		if(ctx.orderByListExpr() != null){
			selectexp += " order by " + newTexts.get(ctx.orderByListExpr());
		}
		newTexts.put(ctx, selectexp);
	
		String temp = newTexts.get(ctx);
		System.out.println(temp);	
		System.out.println("-----SelectExpr------\n");
	}
	
	//selectionListElement
	public void exitSelectionListElement(EsperEPL2GrammarParser.SelectionListElementContext ctx) {
		String selectelement =  ctx.getChild(0).getText();
		newTexts.put(ctx, selectelement);
		
		String temp = newTexts.get(ctx);
		System.out.println(temp);	
		System.out.println("-----SelectionListElement------\n");
	}
	
	//selectionList
	public void exitSelectionList(EsperEPL2GrammarParser.SelectionListContext ctx) { 
		int propertycount = 0;
		String selectionlist =  "";
		String[] selement;
		Object[] element;
		String[] delete;
		int count = 0;
		
		//removed ',' with String array resize
		selement = new String[ctx.getChildCount()/2+1];
		delete = new String[ctx.getChildCount()/2+1];
		selectlist = new String[selement.length];
		
		//remove ',' child
		for(int i=0;i<ctx.getChildCount(); i++){
			if(ctx.getChild(i).getText().equals(","))
				continue;
			selectlist[count] = ctx.getChild(i).getText();
			selement[count++] = ctx.getChild(i).getText();
		}
		
		if(selectlist[0].equals("*")){
			
			try{
			Class c = Class.forName("com.espertech.esper.epl.dataclass." + ClassFinder.classname);
			Field m[] = c.getDeclaredFields();
			selectlist = new String[m.length];
			for (int i = 0; i < m.length; i++){
				selectlist[i] = m[i].getName();
			}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		//remove duplicate select element
		element = removeDuplicateArray(selement);

		//reflection 
		try {
			Class c = Class.forName("com.espertech.esper.epl.dataclass." + ClassFinder.classname);
			 Field m[] = c.getDeclaredFields();
			 for (int i = 0; i < m.length; i++){
		          for(int j=0;j<element.length;j++){
		        	  
		        	  if(m[i].getName().equals(element[j])){	
		        		  delete[propertycount] = (String)element[j];
		        		  propertycount++;
		        		  break;
		        	  }
		          }
			 }

			 // select all element from tables
			 if(propertycount == m.length){
				 for(int i=0;i<propertycount;i++){
					 element = removeElementArray(element,delete[i]);
				 }
				 //substitute all elements with wildcard(*)
				 selectionlist+= "*";
				 if(element.length != 0){
					 for(int i=0;i<element.length;i++){
						 selectionlist += "," + (String)element[i];
					 }
				 }
			 }else{
					for(int i=0;i<element.length;i++){
						selectionlist += element[i];
						if(i+1 != element.length){
							selectionlist += ",";
						}
					}
			 }
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newTexts.put(ctx, selectionlist);
		
		String temp = newTexts.get(ctx);
		System.out.println(temp);	
		System.out.println("-----SelectionList------\n");
	}
	
	//selectClause
	public void exitSelectClause(EsperEPL2GrammarParser.SelectClauseContext ctx){
		String selectcl = "";
		
		//only selectionList
		if(ctx.getChildCount() == 1)
			selectcl += newTexts.get(ctx.selectionList());
		//IStream || RStream || IRStream || Distinct + selectionList
		else if(ctx.getChildCount() == 2)
			selectcl += ctx.getChild(0).getText() + " " + newTexts.get(ctx.selectionList());
		//IStream || RStream || IRStream + Distinct + selectionList
		else if(ctx.getChildCount() == 3)
			selectcl += ctx.getChild(0).getText() + " " + ctx.getChild(1).getText() + " " +newTexts.get(ctx.selectionList());
		newTexts.put(ctx, selectcl);
		
		String temp = newTexts.get(ctx);
		System.out.println(temp);	
		System.out.println("-----SelectClause------\n");
	}
	
	//expressionList
	public void exitExpressionList(EsperEPL2GrammarParser.ExpressionListContext ctx) { 
		String exp = ctx.getChild(0).getText();
		String resultexp = "";
	
		//symja interpreter : expression optimize
		try {
            Console utill = new Console();
            String ip = utill.interpreter(exp);
                   
            resultexp =  ip.toString();
            // disable trace mode if the step listener isn't necessary anymore
        } catch (SyntaxError e) {
            // catch Symja parser errors here
            System.out.println(e.getMessage());
        } catch (MathException me) {
            // catch Symja math errors here
            System.out.println(me.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
		newTexts.put(ctx,resultexp);
		
		String temp = newTexts.get(ctx);
		System.out.println(temp);	
		System.out.println("-----ExpressionList------\n");
	}

	//classidentifier
	public void exitClassIdentifier(EsperEPL2GrammarParser.ClassIdentifierContext ctx) { 
		String classident = ctx.getChild(0).getText();
		eventclass = classident;
		newTexts.put(ctx, classident);
		
		String temp = newTexts.get(ctx);
		System.out.println(temp);	
		System.out.println("-----Classidentifier------\n");
	}
	
	//eventFilterExpression
	public void exitEventFilterExpression(EsperEPL2GrammarParser.EventFilterExpressionContext ctx) { 
		String explist = newTexts.get(ctx.expressionList());
		String filterexp = newTexts.get(ctx.classIdentifier());
		
		if(explist != null){
			filterexp +=  "(" + explist + ")";
		}else{
			streamExstate = 1;
		}
		newTexts.put(ctx, filterexp);
		
		String temp = newTexts.get(ctx);
		System.out.println(temp);	
		System.out.println("-----EventFilterExpression------\n");
	}
	
	//streamExpression
	public void exitStreamExpression(EsperEPL2GrammarParser.StreamExpressionContext ctx) { 
		String streamexp = newTexts.get(ctx.eventFilterExpression());
		newTexts.put(ctx, streamexp);
		
		String temp = newTexts.get(ctx);
		System.out.println(temp);	
		System.out.println("-----StreamExpression------\n");
	}
	
	//fromClause
	public void exitFromClause(EsperEPL2GrammarParser.FromClauseContext ctx){
		String fromcl = newTexts.get(ctx.streamExpression());
		newTexts.put(ctx, fromcl);
	
		String temp = newTexts.get(ctx);
		System.out.println(temp);	
		System.out.println("-----FromClause------\n");
	}
	
	//whereClause
	public void exitWhereClause(EsperEPL2GrammarParser.WhereClauseContext ctx){
		String wherecl = ctx.getChild(0).getText();
		newTexts.put(ctx, wherecl);
		
		String temp = newTexts.get(ctx);
		System.out.println(temp);	
		System.out.println("-----WhereClause------\n");
	}
	
	//groupByListExpr
	public void exitGroupByListExpr(EsperEPL2GrammarParser.GroupByListExprContext ctx){
		String groupbyexp =  ctx.getChild(0).getText();
		newTexts.put(ctx, groupbyexp);
		
		String temp = newTexts.get(ctx);
		System.out.println(temp);	
		System.out.println("-----GroupByListExpr------\n");
	}

	//havingClause
	public void exitHavingClause(EsperEPL2GrammarParser.HavingClauseContext ctx){
		String havingcl =  ctx.getChild(0).getText();
		newTexts.put(ctx, havingcl);
		
		String temp = newTexts.get(ctx);
		System.out.println(temp);	
		System.out.println("-----HavingClause------\n");
	}
	
	//orderByListExpr
	public void exitOrderByListExpr(EsperEPL2GrammarParser.OrderByListExprContext ctx){
		String orderbylist = "";
		int i = 0;
		while(ctx.orderByListElement(i) != null){
			orderbylist += newTexts.get(ctx.orderByListElement(i));
			i++;
			if(ctx.orderByListElement(i) != null)
			orderbylist += ", ";
		}	
		newTexts.put(ctx, orderbylist);
		
		String temp = newTexts.get(ctx);
		System.out.println(temp);	
		System.out.println("-----OrderByListExpr------\n");
	}
	
	//orderByListElement
	public void exitOrderByListElement(EsperEPL2GrammarParser.OrderByListElementContext ctx){
		String orderbyelement = "";
		
		//expression
		if(ctx.getChildCount() == 1)
			orderbyelement += ctx.getChild(0).getText();
		//expression + (ASC | DESC)
		else if(ctx.getChildCount() == 2)
			orderbyelement +=  ctx.getChild(0).getText() + " " + ctx.getChild(1).getText();
		newTexts.put(ctx, orderbyelement);
		
		String temp = newTexts.get(ctx);
		System.out.println(temp);	
		System.out.println("-----OrderByListElement------\n");
	}
}
