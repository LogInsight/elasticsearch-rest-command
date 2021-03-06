/* Generated By:JJTree: Do not edit this line. AST_Top.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
/*package com.everdata.parser;


import java.util.HashMap;
import java.util.Map;




import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;

import com.everdata.command.CommandException;
import com.everdata.command.Option;
import com.everdata.command.Search;

public class AST_Top extends SimpleNode {
	
	Map<Integer, String> options = new HashMap<Integer, String> ();;
	String[] bucketFields = new String[0];
	String[] topFields = new String[0];
	TermsBuilder top = null;
	
	public AST_Top(int id) {
		super(id);
	}

	public AST_Top(CommandParser p, int id) {
		super(p, id);
	}
	
	public String[] bucketFields(){
		return bucketFields;
	}
	
	public String[] topFields(){
		return topFields;
	}

	// top <top-opt>* <field-list> [<by-clause>]
	
	private void traverseAST() {
		options.put(Option.LIMIT, "10");
		
		for (Node n : children) {
			if (n instanceof AST_TopOption) {				
				options.put(((AST_TopOption) n).opt.type, ((AST_TopOption) n).opt.value);
			}else if (n instanceof AST_IdentList) {				
				topFields = ((AST_IdentList) n).getNames();
			}else if (n instanceof AST_ByIdentList) {
				bucketFields = ((AST_ByIdentList) n).getNames();
			}
		}
	}
	
	
	

	private TermsBuilder genAggregation() throws CommandException{
		
		traverseAST();
		
		TermsBuilder local = AST_Stats.newTermsBucket("top", Integer.parseInt(options.get(Option.LIMIT)), topFields);
		
		if(options.get(Option.MINCOUNT) != null){
			local.minDocCount(Long.parseLong(options.get(Option.MINCOUNT)));
		}
		
		if(bucketFields.length > 0){
			local = Search.newTermsBucket("topWithBy", Integer.parseInt(options.get(Option.LIMIT)), bucketFields).subAggregation(local);
		}
		
		return local;
	}
	
	public TermsBuilder getTop() throws CommandException{
		if(top == null)
			top = genAggregation();
		
		return top;
		
	}
	
}
*/
/*
 * JavaCC - OriginalChecksum=472d83b96c6710de8a99d69edca4d23b (do not edit this
 * line)
 */
