package com.example.primegen.core.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class PrimeGenerator implements Callable<List<Integer>> {

	private int from;
	
	private int to;
	
	public PrimeGenerator(int f,int t){
		this.from = f;
		this.to= t;
		System.out.println("Created " + f + "," + t);
	}
	
	@Override
	public List<Integer> call() throws Exception {
		List<Integer> primes = new ArrayList<>();
		for(int i = from;i <= to;i++) {
			if(isPrime(i)) {
				primes.add(i);
			}
		}
		return primes;
	}
	
	boolean isPrime(int num) {
		int top = num /2;
		for(int i = top ; i >= 2 ;i--) {
			if(num % i == 0)
				return false;
		}
		return true;
	}

}
