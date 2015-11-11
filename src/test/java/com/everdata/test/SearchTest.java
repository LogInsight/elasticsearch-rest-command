package com.everdata.test;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import com.everdata.command.Option;
import com.everdata.command.Search;
import com.everdata.parser.AST_Search;
import com.everdata.parser.CommandParser;
import com.everdata.parser.Node;

import static org.junit.Assert.assertNotNull;

public class SearchTest {

	static final String[] SEARCH_COMMANDS = new String[] {
		"SEARCH INDEX=IDC_LOG_201403261835,IDC_LOG SOURCETYPE=IDCLOGMAPPING HASCHILD=(SOURCETYPE=DDD DDD=DDD DDD) HASCHILD=(SOURCETYPE=DDD DDD=DDD DDD) HASPARENT=(SOURCETYPE=TTT TTT=TT TT) DDD _DDD=\"中文\"",
		"search index=idc_log_201403261835,idc_log sourcetype=idcLogMapping haschild=(sourcetype=ddd ddd=ddd ddd) haschild=(sourcetype=ddd ddd=ddd ddd) hasparent=(sourcetype=ttt ttt=tt tt) ddd _ddd=\"中文\"",
		// THIS COMMAND DOES NOT PARSE:
		// "search sourcetype=user http://*? | stats count sum(\"asdfasdfasdfasf+[]''\"), sum(down_bytes) by hostt | top ddd,ddd by ddd,ddd",		
		"SEARCH INDEX=LOG_20140501,LOG_20140502,LOG_20140503,LOG_20140504,LOG_20140505,LOG_20140506,LOG_20140507,LOG_20140508,LOG_20140509,LOG_20140510,LOG_20140511,LOG_20140512,LOG_20140513,LOG_20140514,LOG_20140515,LOG_20140516 SOURCETYPE=HTTPLOG MSISDN=\"\" STARTDATE>=\"2014-07-01 00:00:00\" ENDDATE<=\"2014-07-02 21:49:50\" | STATS SUM(NTOTALFLOW) BY PID",
		"search index=log sourcetype=*  endtime=\"2014-04-01 00:00:00\" msisdn=15527222196 1starttime>=\"2014-03-01 00:00:00\"| stats sum(ntotalflow) by pid"
	};
	
	@Test
	public void testParseSearchCommands() throws Exception {
		for (String searchCommand : SEARCH_COMMANDS) {
			searchCommand = searchCommand.toUpperCase();
		  CommandParser parser = new CommandParser(searchCommand);		  
		  ArrayList<Node> searchCommands = parser.getSearchCommandList();
			AST_Search searchTree = (AST_Search) searchCommands.get(0);
			assertNotNull(searchTree);	
			String[] sourceTypes = Search.parseTypes(searchTree);
			System.out.println("souces: " + Arrays.toString(sourceTypes));
			String index = (String) searchTree.getOption(Option.INDEX);
			System.out.println("index: " + index);
			String startTime = Search.parseStartTime(searchTree);
			String endTime = Search.parseEndTime(searchTree);
			System.out.println("time: [" + startTime+" ... "+endTime+"]");
		}
	}
}
