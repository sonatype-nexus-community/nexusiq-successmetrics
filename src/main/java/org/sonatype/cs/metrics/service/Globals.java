package org.sonatype.cs.metrics.service;

import org.springframework.stereotype.Component;

@Component
public class Globals {
	
	public final String txtMessage;
	public final int initValue;
	  
	public final static Globals instance = new Globals();

	private Globals(){ 
		txtMessage = "Hello World!";
		initValue = 1;
	}

}
