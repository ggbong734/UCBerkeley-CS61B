public class NBody{
	public static double readRadius(String fileName){
		In in = new In(fileName);
		int numPlanets = in.readInt();
		double radiusUniverse = in.readDouble();
		return radiusUniverse;
	}

	public static Body[] readBodies(String fileName){
		In in = new In(fileName);
		int numPlanets = in.readInt();
		Body[] bodyArray = new Body[numPlanets];
		double radiusUniverse = in.readDouble();
		int counter = 0;
		while(counter < numPlanets){
			double xP = in.readDouble();
			double yP = in.readDouble();
			double xV = in.readDouble();
			double yV = in.readDouble();
			double m = in.readDouble();
			String img = in.readString();
			Body b = new Body(xP, yP, xV, yV, m, img);
			bodyArray[counter] = b;
			counter += 1;
		}
		return bodyArray;
	}

	public static void main(String[] args){
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];
		double radius = readRadius(filename);
		Body[] bodyArray = readBodies(filename);
		int numPlanets = bodyArray.length;

		double time = 0;
		String imageToDraw = "images/starfield.jpg";

		StdDraw.enableDoubleBuffering();
		/* Set scale of universe */
		StdDraw.setXscale(-radius, radius);
		StdDraw.setYscale(-radius, radius);
		/* StdDraw.setScale(-100, 100);*/

		while(time < T){
			double[] xForces = new double[numPlanets];
			double[] yForces = new double[numPlanets];
			
			for(int i = 0; i < numPlanets; i++){
				xForces[i] = bodyArray[i].calcNetForceExertedByX(bodyArray);
				yForces[i] = bodyArray[i].calcNetForceExertedByY(bodyArray);
			}
			
			/* Update the velocity and position of each body */
			for(int i = 0; i < numPlanets; i++){
				bodyArray[i].update(dt, xForces[i], yForces[i]);
			}
			StdDraw.clear();
			StdDraw.picture(0, 0, imageToDraw);

			/** Draws each body in the input text file onto the universe */
			for(Body body : bodyArray){
				body.draw();
			}
			StdDraw.show();
			StdDraw.pause(10);

			time += dt;
		}

		StdOut.printf("%d\n", numPlanets);
		StdOut.printf("%.2e\n", radius);
		for (int i = 0; i < numPlanets; i++){
			StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
						  bodyArray[i].xxPos, bodyArray[i].yyPos, bodyArray[i].xxVel,
						  bodyArray[i].yyVel, bodyArray[i].mass, bodyArray[i].imgFileName);
		}		
	}

}