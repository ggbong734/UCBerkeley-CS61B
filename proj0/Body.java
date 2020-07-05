import java.lang.Math;

public class Body{
	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;

	/*Gravitational constant in N*m^2/kg^2 */
	private static final double gConst = 6.67e-11;

	public Body(double xP, double yP, double xV, 
				  double yV, double m, String img){
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img; 
	}

	/* Constructor that creates a copy of an object*/
	public Body(Body b){
		this.xxPos = b.xxPos;
		this.yyPos = b.yyPos;
		this.xxVel = b.xxVel;
		this.yyVel = b.yyVel;
		this.mass = b.mass;
		this.imgFileName = b.imgFileName;
	}

	/*Calculate distance between two bodies*/
	public double calcDistance(Body b){
		double xdistance = (this.xxPos - b.xxPos);
		double ydistance = (this.yyPos - b.yyPos);
		double distance = Math.sqrt((xdistance * xdistance) +
						   (ydistance * ydistance));	
		return distance;
	}

	/* Calculate pair-wise force between two bodies*/
	public double calcForceExertedBy(Body b){
		double force = gConst * this.mass * b.mass /
						Math.pow(calcDistance(b), 2);
		return force;
	}

	public double calcForceExertedByX(Body b){
		double forceX = calcForceExertedBy(b) / calcDistance(b) *
						(b.xxPos - this.xxPos); 
		return forceX;
	}

	public double calcForceExertedByY(Body b){
		double forceY = calcForceExertedBy(b) / calcDistance(b) *
						(b.yyPos - this.yyPos);
		return forceY;
	}

	public double calcNetForceExertedByX(Body[] allBody){
		double total = 0;
		for (Body body : allBody) {
			if(!(body.equals(this))){
				total += calcForceExertedByX(body);
			}
		}
		return total;
	}

	public double calcNetForceExertedByY(Body[] allBody){
		double total = 0;
		for(Body body : allBody) {
			if(!(body.equals(this))){
				total += calcForceExertedByY(body);
			}
		}
		return total;
	}

	/* This method updates the velocity and position of the body 
		after a force in the x and y direction is applied for dt time */
	public void update(double dt, double forceX, double forceY){
		double accelX = forceX/this.mass;
		double accelY = forceY/this.mass;
		this.xxVel = this.xxVel + dt * accelX;
		this.yyVel = this.yyVel + dt * accelY;
		this.xxPos = this.xxPos + dt * xxVel;
		this.yyPos = this.yyPos + dt * yyVel;
	}

	/* Draws the Body at its position in the universe */
	public void draw(){
		StdDraw.picture(this.xxPos, this.yyPos, "images/" + this.imgFileName);
	}
}