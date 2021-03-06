/* Generated By:JJTree: Do not edit this line. AST_Search.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.everdata.parser;


import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

import com.everdata.command.CommandException;
import com.everdata.command.Option;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AST_Search extends SimpleNode {

	HashMap<Integer, Object> options = null;
	FilterBuilder filterBuilder = null;
	QueryBuilder queryBuilder = null;
	BoolQueryBuilder joinFieldsQuery = null;
	List<AST_OrExpr> childExpressions = new ArrayList<AST_OrExpr>();
	AST_OrExpr parentExpression = null;
	

	public AST_Search(int id) {
		super(id);
	}

	public AST_Search(CommandParser p, int id) {
		super(p, id);
	}
	
	public boolean hasCondition(){		
		
		for (Node n : children) {
			if (n instanceof AST_OrExpr) {
				return true;
			}
		}
		
		return false;
	}

	public Object getOption(int optionType) {

		if (options != null) return options.get(optionType);
		
		options = new HashMap<Integer, Object>();
		options.put(Option.INDEX, "_all");
		
		ArrayList<String> hasChildTypes = new ArrayList<String>();
		
		for (Node n : children) {
			if (n instanceof AST_SearchOption) {
								
				if(((AST_SearchOption) n).opt.type == Option.HASCHILD){
					childExpressions.add((AST_OrExpr) ((AST_SearchOption) n).children[0]);
					hasChildTypes.add(((AST_SearchOption) n).opt.value);
					options.put(((AST_SearchOption) n).opt.type, hasChildTypes);
					
				}else if(((AST_SearchOption) n).opt.type == Option.HASPARENT){
					parentExpression = (AST_OrExpr) ((AST_SearchOption) n).children[0];
					options.put(((AST_SearchOption) n).opt.type, ((AST_SearchOption) n).opt.value);
				}else{
					options.put(((AST_SearchOption) n).opt.type, ((AST_SearchOption) n).opt.value);
				}
				
				
			}
		}
		
		return options.get(optionType);

		/*
		 * options.put(Option, arg1); options.put(arg0, arg1); options.put(arg0,
		 * arg1);
		 */

	}
	
	private static Object convert(String value){
		try{
			return Integer.parseInt(value);
		}catch( NumberFormatException e){
			try{	return Long.parseLong(value); }catch( NumberFormatException e0){
				try{	return Float.parseFloat(value); }catch( NumberFormatException e1){
					try{	return Float.parseFloat(value); }catch( NumberFormatException e2){
						return null;
					}
				}
			}
		}
	}
	
	private static QueryBuilder fromValueTypeQ(String field, String value, int valueType){
		if(value.contains("*") || value.contains("?")){
			if( value.length() > 1 && value.indexOf('*') == (value.length()-1))
				return QueryBuilders.prefixQuery(field, value.substring(0, value.length()-1));
			else
				return QueryBuilders.wildcardQuery(field, value);
		}else if(value.equalsIgnoreCase("")){
			
			return QueryBuilders.boolQuery()
					.should(QueryBuilders.termQuery(field, value))
					.should(QueryBuilders.constantScoreQuery(FilterBuilders.missingFilter(field).nullValue(true).existence(true)));
		}
		
		switch(valueType){
		case AST_TermExpression.TERM:
			return QueryBuilders.termQuery(field, value);
		case AST_TermExpression.PHRASE:			
			return QueryBuilders.matchPhraseQuery(field, value);
		}
		return null;
	}

	private static FilterBuilder fromValueType(String field, String value, int valueType){
		if(value.contains("*") || value.contains("?")){
			if( value.length() > 1 && value.indexOf('*') == (value.length()-1))
				return FilterBuilders.prefixFilter(field, value.substring(0, value.length()-1));
			else
				return FilterBuilders.queryFilter(QueryBuilders.wildcardQuery(field, value));
		}
		
		switch(valueType){
		case AST_TermExpression.TERM:
			return FilterBuilders.termFilter(field, value);
		case AST_TermExpression.PHRASE:
			/*for(byte b: value.getBytes()){
				System.out.printf("0x%02X ", b);		        
			}*/
			return FilterBuilders.queryFilter(QueryBuilders.matchPhraseQuery(field, value));
		}
		return null;
	}
	/*
	private static FilterBuilder genFilterBuilder(SimpleNode tree) throws CommandException{
		
		
		//logic expression
				
		
		switch(tree.id){
		case CommandParserTreeConstants.JJT_TERMEXPRESSION:
			AST_TermExpression t = (AST_TermExpression)tree;
			return fromValueType("_all", t.term, t.type);
			
		case CommandParserTreeConstants.JJT_COMPARISONEXPRESSION:
			Expression expr = ((AST_ComparisonExpression)tree).expr;
			switch(expr.oper){
			case Expression.EQ:
				return fromValueType(expr.field,expr.value, expr.valueType);				
			case Expression.NEQ:
				return FilterBuilders.notFilter(fromValueType(expr.field,expr.value, expr.valueType));
			default:
				//Object number = convert(expr.value);
				//if( number == null )
				//	throw new CommandException("不支持针对非数字类型的值做Range类型的查询");
				if( expr.oper == Expression.GT)			
					return FilterBuilders.rangeFilter(expr.field).gt(expr.value);
				else if( expr.oper == Expression.GTE)
					return FilterBuilders.rangeFilter(expr.field).gte(expr.value);
				else if( expr.oper == Expression.LT)
					return FilterBuilders.rangeFilter(expr.field).lt(expr.value);
				else if( expr.oper == Expression.LTE)
					return FilterBuilders.rangeFilter(expr.field).lte(expr.value);

			}

		case CommandParserTreeConstants.JJT_PREDICATEEXPRESSION:
			if(tree.children.length > 1){
				AndFilterBuilder fb = FilterBuilders.andFilter();
				for(Node n: tree.children){
					fb.add(genFilterBuilder((SimpleNode)n));
				}
				
				return fb;
			}else
				return genFilterBuilder((SimpleNode)tree.children[0]);
		case CommandParserTreeConstants.JJT_OREXPR:			
			if(tree.children.length > 1){				
				OrFilterBuilder fb = FilterBuilders.orFilter();
				for(Node n: tree.children){
					fb.add(genFilterBuilder((SimpleNode)n));
				}
				
				return fb;
			}else
				return genFilterBuilder((SimpleNode)tree.children[0]);
			
			
		case CommandParserTreeConstants.JJT_ANDEXPR:
			if(tree.children.length > 1){
				AndFilterBuilder fb = FilterBuilders.andFilter();
				for(Node n: tree.children){
					fb.add(genFilterBuilder((SimpleNode)n));
				}
				
				return fb;
			}else
				return genFilterBuilder((SimpleNode)tree.children[0]);
			
		case CommandParserTreeConstants.JJT_UNARYEXPR:			
			if(((AST_UnaryExpr)tree).isNot){
				NotFilterBuilder fb = FilterBuilders.notFilter(genFilterBuilder((SimpleNode)tree.children[0]));
				return fb;
			}else
				return genFilterBuilder((SimpleNode)tree.children[0]);
			
		
		}
		
		return genFilterBuilder((SimpleNode)tree.children[0]);
		
	}
	
	
	
	private FilterBuilder getInternalFilter() throws CommandException {
		
		
		if (filterBuilder != null)
			return filterBuilder;
		
		ArrayList<FilterBuilder> allFilters = new ArrayList<FilterBuilder>();
		
		for (Node n : children) {
			if (n instanceof AST_OrExpr) {
				allFilters.add(genFilterBuilder((SimpleNode)n));
				break;
			}
		}
				
		ArrayList<String> childTypes = (ArrayList<String>)getOption(Option.HASCHILD);		
		String parentType = (String)getOption(Option.HASPARENT);
		
		//FilterBuilder parent_child = null;
		if(childTypes != null){
			for(int i = 0; i < childTypes.size(); i++)
				allFilters.add(FilterBuilders.hasChildFilter(childTypes.get(i), genFilterBuilder(childExpressions.get(i))));
			
		}else if(parentType != null){
			
			allFilters.add(FilterBuilders.hasParentFilter(parentType, genFilterBuilder(parentExpression)));		
		
		}
		
		String starttime = (String) getOption(Option.STARTTIME);
		String endtime = (String) getOption(Option.ENDTIME);
						
		if(starttime != null | endtime !=null){
			RangeFilterBuilder timeFilter = FilterBuilders.rangeFilter("_timestamp").from(starttime).to(endtime);
			allFilters.add(timeFilter);
		}		
		
		if( allFilters.size() == 0)
			filterBuilder = null;
		else if( allFilters.size() == 1)
			filterBuilder = allFilters.get(0);
		else
			filterBuilder = FilterBuilders.andFilter(allFilters.toArray(new FilterBuilder[allFilters.size()]));
				
		return filterBuilder;
		

	}
	*/
	private static QueryBuilder genQueryBuilder(SimpleNode tree) throws CommandException{
		
		
		//logic expression
				
		
		switch(tree.id){
		case CommandParserTreeConstants.JJT_TERMEXPRESSION:
			AST_TermExpression t = (AST_TermExpression)tree;
			return fromValueTypeQ("_all", t.term, t.type);
			
		case CommandParserTreeConstants.JJT_COMPARISONEXPRESSION:
			Expression expr = ((AST_ComparisonExpression)tree).expr;
			switch(expr.oper){
			case Expression.EQ:
				return fromValueTypeQ(expr.field,expr.value, expr.valueType);				
			case Expression.NEQ:
				return QueryBuilders.boolQuery().mustNot(fromValueTypeQ(expr.field,expr.value, expr.valueType));
			default:
				//Object number = convert(expr.value);
				//if( number == null )
				//	throw new CommandException("不支持针对非数字类型的值做Range类型的查询");
				if( expr.oper == Expression.GT)			
					return QueryBuilders.rangeQuery(expr.field).gt(expr.value);
				else if( expr.oper == Expression.GTE)
					return QueryBuilders.rangeQuery(expr.field).gte(expr.value);
				else if( expr.oper == Expression.LT)
					return QueryBuilders.rangeQuery(expr.field).lt(expr.value);
				else if( expr.oper == Expression.LTE)
					return QueryBuilders.rangeQuery(expr.field).lte(expr.value);

			}

		case CommandParserTreeConstants.JJT_PREDICATEEXPRESSION:
			if(tree.children.length > 1){
				BoolQueryBuilder fb = QueryBuilders.boolQuery();
				for(Node n: tree.children){
					fb.must(genQueryBuilder((SimpleNode)n));
				}
				
				return fb;
			}else
				return genQueryBuilder((SimpleNode)tree.children[0]);
		case CommandParserTreeConstants.JJT_OREXPR:			
			if(tree.children.length > 1){				
				BoolQueryBuilder fb = QueryBuilders.boolQuery();
				for(Node n: tree.children){
					fb.should(genQueryBuilder((SimpleNode)n));
				}
				
				return fb;
			}else
				return genQueryBuilder((SimpleNode)tree.children[0]);
			
			
		case CommandParserTreeConstants.JJT_ANDEXPR:
			if(tree.children.length > 1){
				BoolQueryBuilder fb = QueryBuilders.boolQuery();
				for(Node n: tree.children){
					fb.must(genQueryBuilder((SimpleNode)n));
				}
				
				return fb;
			}else
				return genQueryBuilder((SimpleNode)tree.children[0]);
			
		case CommandParserTreeConstants.JJT_UNARYEXPR:			
			if(((AST_UnaryExpr)tree).isNot){
				BoolQueryBuilder fb = QueryBuilders.boolQuery().mustNot(genQueryBuilder((SimpleNode)tree.children[0]));
				return fb;
			}else
				return genQueryBuilder((SimpleNode)tree.children[0]);
			
		
		}
		
		return genQueryBuilder((SimpleNode)tree.children[0]);
		
	}
	
	private QueryBuilder getInternalQuery() throws CommandException {
		
		
		if (queryBuilder != null)
			return queryBuilder;
		
		ArrayList<QueryBuilder> allQuerys = new ArrayList<QueryBuilder>();
		
		for (Node n : children) {
			if (n instanceof AST_OrExpr) {
				allQuerys.add(genQueryBuilder((SimpleNode)n));
				break;
			}
		}
				
		ArrayList<String> childTypes = (ArrayList<String>)getOption(Option.HASCHILD);		
		String parentType = (String) getOption(Option.HASPARENT);
		
		//FilterBuilder parent_child = null;
		if(childTypes != null){
			for(int i = 0; i< childTypes.size(); i++)
				allQuerys.add(QueryBuilders.hasChildQuery(childTypes.get(i), genQueryBuilder(childExpressions.get(i))));
			
		}else if(parentType != null){
			
			allQuerys.add(QueryBuilders.hasParentQuery(parentType, genQueryBuilder(parentExpression)));		
		
		}
		
		String starttime = (String) getOption(Option.STARTTIME);
		String endtime = (String) getOption(Option.ENDTIME);
						
		if(starttime != null | endtime !=null){
			RangeQueryBuilder timeFilter = QueryBuilders.rangeQuery("_timestamp").from(starttime).to(endtime);
			allQuerys.add(timeFilter);
		}		
		
		if(joinFieldsQuery != null){
			allQuerys.add(joinFieldsQuery);
		}
		
		if( allQuerys.size() == 0)
			queryBuilder = null;
		else if( allQuerys.size() == 1)
			queryBuilder = allQuerys.get(0);
		else{
			queryBuilder = QueryBuilders.boolQuery();
			for(QueryBuilder q: allQuerys){
				queryBuilder = ((BoolQueryBuilder)queryBuilder).must(q);
			}
		}
		
		return queryBuilder;		
	}
	
	public void setJoinFieldsQuery(BoolQueryBuilder joinFieldsQuery){
		this.joinFieldsQuery = joinFieldsQuery;
	}
	
	public QueryBuilder getQueryBuilder() throws CommandException{
		
		return (getInternalQuery() == null)? QueryBuilders.matchAllQuery(): QueryBuilders.constantScoreQuery( getInternalQuery() );
	
	}

}
/*
 * JavaCC - OriginalChecksum=0784cd4733c591c3e29d4817135e748c (do not edit this
 * line)
 */
