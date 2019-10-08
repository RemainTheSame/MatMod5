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

    Ball[] balls;

    Model(double width, double height) {
        areaWidth = width;
        areaHeight = height;

        // Initialize the model with a few balls
        balls = new Ball[2];
        balls[0] = new Ball(width / 3, height * 0.9, 1.2, 1.6, 0.2);
        balls[1] = new Ball(2 * width / 3, height * 0.7, -0.6, 0.6, 0.3);
    }

    void step(double deltaT) {
        // TODO this method implements one step of simulation with a step deltaT

        double distance = Math.sqrt((Math.pow(balls[1].x - balls[0].x, 2)
                +
                Math.pow(balls[1].y - balls[0].y, 2)));

        if (ballCollision()) {
            System.out.println("collision");
            //   double momentum = balls[0].mass * balls[0].polarVel + balls[1].mass * balls[1].polarVel;

            rectToPolar(distance);

            //Mass represented by radius in our case
            double m1 = balls[0].mass;
            double m2 = balls[1].mass;

            //Initial speed, or u1/u2
            double ux1 = balls[0].vx;
            double uy1 = balls[0].vy;
            double ux2 = balls[1].vx;
            double uy2 = balls[1].vy;

            //Using m1u1+m2u2 = m1v1 +m2v2 to solve for v1 and v2 for both the x and y velocities:
            double vx1 = ((m1 - m2) / (m1 + m2)) * ux1 + ((2 * m2) / (m1 + m2)) * ux2;
            double vy1 = ((m1 - m2) / (m1 + m2)) * uy1 + ((2 * m2) / (m1 + m2)) * uy2;
            double vx2 = ((m2 - m1) / (m1 + m2)) * ux2 + ((2 * m2) / (m1 + m2)) * ux1;
            double vy2 = ((m2 - m1) / (m1 + m2)) * uy2 + ((2 * m2) / (m1 + m2)) * uy1;

            //Assign new velocity
            balls[0].vx = vx1;
            balls[0].vy = vy1;
            balls[1].vx = vx2;
            balls[1].vy = vy2;

            polarToRect(distance);

        }


        for (Ball b : balls) {
            // detect collision with the border
            if (b.x < b.radius || b.x > areaWidth - b.radius) {
                b.vx *= -1; // change direction of ball
            }
            if (b.y < b.radius || b.y > areaHeight - b.radius) {
                b.vy *= -1;
            } else {
                b.vy += deltaT * (-9.82);
            }
            //        if ((Math.abs(b.vy) < 0.3) && (b.y - b.radius <= 0) && !ballCollision()) {
            //          b.vy = 0;
            //      b.y = b.radius;
            //    }

            // compute new position according to the speed of the ball

            b.x += deltaT * b.vx;
            b.y += deltaT * b.vy;

        }
    }


    boolean ballCollision() {
        double distance = Math.sqrt((Math.pow(balls[1].x - balls[0].x, 2)
                +
                Math.pow(balls[1].y - balls[0].y, 2)));
        return distance < (balls[0].radius + balls[1].radius);



    }

    private void polarToRect(double distance) {
        double thetaLine = Math.acos((balls[0].x - balls[1].x) / distance);

        for (Ball b : balls) {
            b.polarVel = Math.sqrt(b.vx * b.vx + b.vy * b.vy);
            b.theta = Math.atan(b.vy / b.vx);
            b.theta -= thetaLine;
            b.vx = b.polarVel * Math.cos(b.theta);
            b.vy = b.polarVel * Math.sin(b.theta);
        }
    }

    private void rectToPolar(double distance) {

        double thetaLine = Math.acos((balls[0].x - balls[1].x) / distance);
        for (Ball b : balls) {
            b.polarVel = Math.sqrt(b.vx * b.vx + b.vy * b.vy);
            b.theta = Math.atan(b.vy / b.vx);
            b.theta += thetaLine;

            b.vx = b.polarVel * Math.cos(b.theta);
            b.vy = b.polarVel * Math.sin(b.theta);

        }
        //        b.thetaPos = Math.atan(b.y / b.x);
        //        b.polarPos = Math.sqrt(b.x * b.x + b.y * b.y);
        //     b.theta = Math.atan(b.vy / b.vx);
        //       b.polarVel = Math.sqrt(b.vx * b.vx + b.vy * b.vy);

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
            this.mass = r * r * 3.14;
        }

        /**
         * Position, speed, and radius of the ball. You may wish to add other attributes.
         */
        double x, y, vx, vy, radius, theta, polarVel, mass;
    }
}
