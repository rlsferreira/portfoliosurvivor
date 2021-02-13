package br.com.devs.portfoliosurvivor.mathmodel;


public class BlackScholes {
	private boolean call;
	private double STOCK;
	private double STRIKE;
	private double RATE;
	private double TIME;
	private double VOLATILITY;
	private double FORWARD;
	private double Q; //Dividend Yield
	
	private double DELTA;
	private double GAMMA;
	private double THETA;
	private double VEGA;
	private double RHO;
	
	
	//final values used to calculate the standard normal cumulative distribution function
	private static final double P = 0.2316419;
	private static final double b1 = 0.319381350;
	private static final double b2 = -0.356563782;
	private static final double b3 = 1.781477937;
	private static final double b4 = -1.821255978;
	private static final double b5 = 1.330274429;
	
	public BlackScholes(boolean call, double STOCK, double STRIKE, double RATE, double TIME, double VOLATILITY, double DIV, double YIELD) {
		this.call = call;
		this.STOCK = STOCK;
		this.STRIKE = STRIKE;
		this.RATE = RATE;
		this.TIME = TIME;
		this.VOLATILITY = VOLATILITY;
		this.Q = DIV / STOCK;
		//System.out.println("Q: " + Q);
	}
	
	public double calculateOptionPrice() {
		FORWARD = STOCK * Math.exp((RATE - Q) * TIME); //if no dividends, then Forward price is just stock price
		double d1 = d1();
		double d2 = d2();
		double SD1 = PDF(d1);
		double CD1 = CND(d1, SD1);
		
		//System.out.println("d1: " + d1 + "\nd2: " + d2);
		
		double thetaLeft = -(STOCK * SD1 * VOLATILITY) / (2 * Math.sqrt(TIME));
		
		GAMMA = SD1 / (STOCK * VOLATILITY * Math.sqrt(TIME));
		VEGA = STOCK * SD1 * Math.sqrt(TIME);
		
		if (call) {
			double CD2 = CND(d2);
			
			DELTA = CD1;
			RHO = STRIKE * TIME * Math.exp(-RATE * TIME) * CD2;
			
			double thetaRight = RATE * STRIKE * Math.exp(-RATE * TIME) * CD2;
			THETA = thetaLeft - thetaRight;
			
			double callPrice = (FORWARD * CD1 - STRIKE * CD2) * Math.exp(-RATE * TIME);
			//double callPrice = CD1 * STOCK - (STRIKE * Math.exp(-RATE * TIME) * CD2);
			return callPrice;
		} else {
			double putCD1 = CND(-d1());
			double putCD2 = CND(-d2());
			
			DELTA = CD1 - 1;
			RHO = -STRIKE * TIME * Math.exp(-RATE * TIME) * putCD2;
			
			double thetaRight = RATE * STRIKE * Math.exp(-RATE * TIME) * putCD2;
			THETA = thetaLeft + thetaRight;
			
			//double putPrice = (STRIKE * putCD2 - FORWARD * putCD1) * Math.exp(-RATE * TIME);
			double putPrice = (STRIKE * Math.exp(-RATE * TIME) * putCD2) - (STOCK * putCD1);
			return putPrice;
		}
	}
	
	/**Standard normal probability density function
	 * @param x
	 * @return probability density of x
	 * */
	public static double PDF(double x) {
		return (Math.exp(-.5 * Math.pow(x, 2))) / Math.sqrt(2 * Math.PI);
	}
	
	/**Here we are using Abramowitz & Stegun (1964) numerical approximation with 6 constant values.
	 * Accounts for negative values of x. Instead of using Math.pow to calculate the t-values, I use multiplication
	 * to achieve ~20 times more efficiency.
	 * @param x
	 * @return cumulative normal distribution of x
	 * */
	public static double CND(double x, double sdx) {
		double t = 1 / (1 + P * Math.abs(x));
		double t1 = t;
		double t2 = t1 * t;
		double t3 = t2 * t;
		double t4 = t3 * t;
		double t5 = t4 * t;
		double B1 = b1 * t1;
		double B2 = b2 + t2;
		double B3 = b3 + t3;
		double B4 = b4 + t4;
		double B5 = b5 + t5;
		double sum = B1 + B2 + B3 + B4 + B5;
		double CD = 1 - sdx * sum;
		return (x < 0) ? 1 - CD : CD;
	}
	
	public static double CND(double x) {
		return CND(x, PDF(x));
	}
	
	/**Private static method to calculate d1 of Black-Scholes
	 * @param s | stock price
	 * @param x | strike price
	 * @param r | risk free rate
	 * @param t | time to maturity in years
	 * @param v | implied volatility of returns of underlying asset/stock
	 * return d1
	 * */
	private double d1() {
		return (Math.log(FORWARD / STRIKE) + ((Math.pow(VOLATILITY, 2) * TIME) / 2)) / (Math.pow(TIME, 0.5) * VOLATILITY);
	}
	
	private double d2() {
		return d1() - (VOLATILITY * Math.pow(TIME, 0.5));
	}
	
	public void printGreeks() {
		System.out.println("Delta: " + DELTA + "\nGamma: " + GAMMA + "\nVega: " + VEGA + "\nTheta: " + THETA + "\nRho: " + RHO);
	}
}
