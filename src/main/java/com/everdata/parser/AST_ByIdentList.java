/* Generated By:JJTree: Do not edit this line. AST_ByIdentList.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.everdata.parser;

import java.util.ArrayList;
import java.util.HashSet;

public class AST_ByIdentList extends SimpleNode {

	public static class By {
		public String as = null;
		public String name = null;
		public boolean keyorder = true;
		public boolean desc = true;
		public boolean script = false;
	}

	public AST_ByIdentList(int id) {
		super(id);
	}

	public AST_ByIdentList(CommandParser p, int id) {
		super(p, id);
	}

	/** Names of the columns/tables. */
	public ArrayList<By> byList = new ArrayList<By>();

	public void addField(String name) {
		By b = new By();
		b.name = name;
		byList.add(b);
	}

	public void setCountOrder() {
		byList.get(byList.size() - 1).keyorder = false;
	}

	public void setDesc(boolean desc) {
		byList.get(byList.size() - 1).desc = desc;
	}
	
	public void setScript() {
		byList.get(byList.size() - 1).script = true;
	}
	
	public void setAs(String as){
		byList.get(byList.size() - 1).as = as;
	}

}
/*
 * JavaCC - OriginalChecksum=e20a995b9b9e507db3cad93fb586861e (do not edit this
 * line)
 */
