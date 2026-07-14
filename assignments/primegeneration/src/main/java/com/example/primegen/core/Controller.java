package com.example.primegen.core;

import java.util.List;

public interface Controller {
	
	void execute(int min,int max) throws Exception;
	
	List<Integer> getPrimes();
}
