package br.com.devs.portfoliosurvivor.mathmodel;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BlackScholesTest {

	
	@Test
	void testCalculateOptionPrice() {
		BlackScholes bs = new BlackScholes(
				true, //call, 
				28.44, //STOCK, 
				28, //STRIKE, 
				0.025, // RATE, 
				0.05, //TIME em anos, 
				0.25, //VOLATILITY, 
				0, //DIV, 
				0 //YIELD
				);
		System.out.println("Option price: " + bs.calculateOptionPrice());
		bs.printGreeks();
		
		bs = new BlackScholes(
				true, //call, 
				28.44, //STOCK, 
				28, //STRIKE, 
				0.025, // RATE, 
				0.05, //TIME em anos, 
				0.20, //VOLATILITY, 
				0, //DIV, 
				0 //YIELD
				);
		System.out.println("Option price: " + bs.calculateOptionPrice());
		bs.printGreeks();
		
	}

	@Test
	void testPDF() {
		fail("Not yet implemented");
	}

	@Test
	void testCNDDoubleDouble() {
		fail("Not yet implemented");
	}

	@Test
	void testCNDDouble() {
		fail("Not yet implemented");
	}


}
