package com.example.primegen.core;

import java.util.ArrayList;
import java.util.List;

public class PrimeGenerator extends Thread {

	int pollTime = 100;

	private int fromNumber;

	private int toNumber;

	private List<Integer> primesFound = new ArrayList<>();

	private static int count;

	private int generatorId;

	private GeneratorState currentState;

	private boolean complete;

	private Controller controller;

	PrimeGenerator(int s, int e) {
		this.fromNumber = s;
		this.toNumber = e;
		setGeneratorId(++count);
		setName("Thread - start(" + fromNumber + "," + toNumber + ")");
		setGeneratorState(GeneratorState.CREATED);
		System.out.println("Created " + getName());
	}

	PrimeGenerator(int s, int e, Controller controller) {
		this.fromNumber = s;
		this.toNumber = e;
		setGeneratorId(++count);
		setName("Thread - (" + fromNumber + "," + toNumber + ")");
		setGeneratorState(GeneratorState.CREATED);
		this.controller = controller;
		System.out.println("Created " + getName());
	}

	public void run() {
		while (!currentState.equals(GeneratorState.COMPLETED)) {

			if (currentState.equals(GeneratorState.START)) {
				setGeneratorState(GeneratorState.RUNNING);
				for (int i = fromNumber; i <= toNumber; i++) {
					if (isPrime(i)) {
						primesFound.add(i);
					}
				}
				System.out.println(this.getName() + " ---> Primes found" + primesFound);
				setGeneratorState(GeneratorState.COMPLETED);
				((DefaultControllerImpl) controller).notifyComplete(this);
			}
		}

	}

	boolean isPrime(int num) {
		int top = num / 2;
		for (int i = top; i >= 2; i--) {
			if (num % i == 0)
				return false;
		}
		return true;
	}

	public int getFromNumber() {
		return fromNumber;
	}

	public void setFromNumber(int start) {
		this.fromNumber = start;
	}

	public int getToNumber() {
		return toNumber;
	}

	public void setToNumber(int end) {
		this.toNumber = end;
	}

	public List<Integer> getPrimesFound() {
		return primesFound;
	}

	public void setGeneratorId(int id) {
		this.generatorId = id;
	}

	public boolean isCompleteFlag() {
		return complete;
	}

	void setGeneratorState(GeneratorState state) {
		this.currentState = state;
	}

	/*
	 * @Override public String toString() { return "PrimeGenerator [pollTime=" +
	 * pollTime + ", start=" + start + ", end=" + end + ", primesFound=" +
	 * primesFound + ", id=" + id + ", currentState=" + currentState + ", complete="
	 * + complete + ", controller=" + controller + "]"; }
	 */

	@Override
	public String toString() {
		return "PrimeGenerator [id =" + generatorId + ", start=" + fromNumber + ", end=" + toNumber + "," + currentState
				+ "," + primesFound + "]";
	}

	Controller getController() {
		return controller;
	}

	void setController(Controller controller) {
		this.controller = controller;
	}

	GeneratorState getCurrentState() {
		return currentState;
	}

	int getGeneratorId() {
		return generatorId;
	}

}
