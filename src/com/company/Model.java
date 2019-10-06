package com.company;
/**
 * The physics model.
 *
 * This class is where you should implement your bouncing balls model.
 *
 * The code has intentionally been kept as simple as possible, but if you wish, you can improve the design.
 *
 * @author Simon Robillard
 *
 */
import java.lang.Math;

class Model {

    double areaWidth, areaHeight;

    Ball [] balls;

    Model(double width, double height) {
        areaWidth = width;
        areaHeight = height;

        // Initialize the model with a few balls
        balls = new Ball[2];
        balls[0] = new Ball(width / 5, height * 0.5, 3, -3, 0.3);
        balls[1] = new Ball(2 * width /3, height * 0.5, -3, -3, 0.2);
    }

    void step(double deltaT) {
        // TODO this method implements one step of simulation with a step deltaT

        //Check if the two radius's lengths are greater than the distance between the two balls
        // Added some additional space to try to avoid having the balls stick to each other
        if(checkDistance() <= (balls[0].radius + balls[1].radius)+0.015){
            System.out.println("Collision");
            calcVelocity();
        }


        for (Ball b : balls) {
            // detect collision with the border, added some extra space to try to reduce weird bugs
            if (b.x < b.radius+0.02 || b.x > areaWidth - b.radius +0.02) {
                b.vx *= -1; // change direction of ball
            }
            if (b.y < b.radius+0.02 || b.y > areaHeight - b.radius +0.02) {
                b.vy *= -1;
            }

            // compute new position according to the speed of the ball
            b.vy += deltaT*(-9.82);
            b.x += deltaT * b.vx;
            b.y += deltaT * b.vy;

        }
    }


    private double checkDistance(){
        //Finds the distance between two points using pythagoras
        double distance = Math.sqrt((Math.pow(balls[1].x - balls[0].x , 2)
                +
                Math.pow(balls[1].y - balls[0].y , 2)));
        return distance;
    }

    private void calcVelocity(){

        //Mass represented by radius in our case
        double m1 = balls[0].radius;
        double m2 = balls[1].radius;

        //Initial speed, or u1/u2
        double ux1 = balls[0].vx;
        double uy1 = balls[0].vy;
        double ux2 = balls[1].vx;
        double uy2 = balls[1].vy;

        //Using m1u1+m2u2 = m1v1 +m2v2 to solve for v1 and v2 for both the x and y velocities:
        double vx1 = ((m1 - m2)/(m1 + m2))*ux1 + ((2*m2)/(m1+m2))*ux2;
        double vy1 = ((m1 - m2)/(m1 + m2))*uy1 + ((2*m2)/(m1+m2))*uy2;
        double vx2 = ((m2 - m1)/(m1 + m2))*ux2 + ((2*m2)/(m1+m2))*ux1;
        double vy2 = ((m2 - m1)/(m1 + m2))*uy2 + ((2*m2)/(m1+m2))*uy1;

        //Assign new velocity
        balls[0].vx = vx1;
        balls[0].vy = vy1;
        balls[1].vx = vx2;
        balls[1].vy = vy2;

    }

    /**
     * Simple inner class describing balls.
     */
    class Ball {

        Ball(double x, double y, double vx, double vy, double r) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.radius = r;
        }

        /**
         * Position, speed, and radius of the ball. You may wish to add other attributes.
         */
        double x, y, vx, vy, radius, polarRadius, theta;
    }
}