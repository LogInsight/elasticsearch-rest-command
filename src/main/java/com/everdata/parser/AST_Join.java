/* Generated By:JJTree: Do not edit this line. AST_Join.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.everdata.parser;

import com.everdata.command.JoinQuery.Join;
import com.everdata.command.Option;

public
class AST_Join extends SimpleNode {
  public AST_Join(int id) {
    super(id);
  }

  public AST_Join(CommandParser p, int id) {
    super(p, id);
  }
  private String[] fields = null;
  private AST_Search search = null;
  
  private Join join = null;  
  
  public Join getJoin(){
	  
	  if(join == null){
		  traverseAST();
		  join = new Join(fields, search);
	  }
	  
	  return join;
	  
  }
  
  private void traverseAST() {
		
		for (Node n : children) {
			if (n instanceof AST_IdentList) {				
				fields = ((AST_IdentList) n).getNames();
			}else if (n instanceof AST_Search) {
				search = (AST_Search) n;
			}
		}
	}

}
/* JavaCC - OriginalChecksum=dd2028b5b56d704556c128ba5dc3cf1c (do not edit this line) */
