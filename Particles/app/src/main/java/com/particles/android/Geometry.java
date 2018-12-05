package com.particles.android;



import android.util.FloatMath;
import android.util.Log;

import java.util.Vector;

import static android.util.FloatMath.*;

/**
 * Created by miMiau on 10/06/2018.
 */

public class Geometry {

    //Clsse Punto define un punto en el espacio 3d
    public static class Point {
        //Los puntos para definir un vertice.
        public final float x, y, z;

        //Constructor para un punto en una posicion determinada
        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        //Mover en vertical its a helper function
        public Point translateY(float distance) {
            return new Point(x, y + distance, z);
        }

        //Mover en vertical its a helper function
        public Point translateX(float distance) {
            return new Point(x + distance, y , z);
        }

        public Point translate(Vector vector){
            return new Point(
                    x + vector.x,
                    y + vector.y,
                    z + vector.z
            );
        }
    }

    public static class Triangulo{
        //el centro del circulo inicia con una instancia de Punto Point
        // el cual tiene las 3 coordenadas x,y z
        public final Point center;

        public Triangulo(Point center){
            this.center = center;
        }
    }

    //Un Circulo representado con un Punto
    public static class Circle {
        //el centro del circulo inicia con una instancia de Punto Point
        // el cual tiene las 3 coordenadas x,y z
        public final Point center;
        public final float radius;

        //En el constructor se requiere x parametro un centro
        // por eso se llama asi la variable del primer parametro
        //porque se requiere su centro definido por x y ,z
        public Circle(Point center, float radius) {
            this.center = center;
            this.radius = radius;
        }

        //Se requiere naturalmente un metodo Escalar  scale
        // para incrementar su tamao a partir de un  numero decimal
        //donde para escalar el circulo  se multiplica el radio
        //por la escala
        public Circle scale(float scale) {
            return new Circle(center, radius * scale);
        }


    }

    //Clase para un Cilindro Cilinder, requiere un centro,
    //representado por un Point, tambien ocupa un radio
    //representado por radius y una altura en Height
    public static class Cylinder {

        public final Point center;
        public final float radius;
        public final float height;

        public Cylinder(Point center, float radius, float height) {
            this.center = center;
            this.radius = radius;
            this.height = height;
        }


    }



    public static class Linea{

        public Linea(){

        }
    }


    public static class Ray{

        public final Point point;
        public final Vector vector;

        public Ray(Point point, Vector vector){
            this.point = point;
            this.vector = vector;
        }
    }

    public static class Vector{

        public final float x, y ,z;

        public Vector(float x, float y, float z){
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public float length(){
            return (float)Math.sqrt(x * x + y * y + z * z);
        }

        public Vector crossProduct(Vector other){
            return new Vector(
                    (y * other.z) - (z * other.y),
                    (z * other.x) - (x * other.z),
                    (x * other.y) - (y * other.x)
            );
        }

        public float dotProduct(Vector other){
            return x * other.x + y * other.y + z * other.z;
        }

        public Vector scale(float f){
            return new Vector( x * f, y * f , z * f);
        }
    }

    public static Vector vectorBetween(Point from, Point to){
        return new Vector(
                to.x - from.x,
                to.y - from.y,
                to.z - from.z
        );
    }

    public static Vector crossProductPublic(Vector from, Vector other){
        return new Vector(
                (from.y * other.z) - (from.z * other.y),
                (from.z * other.x) - (from.x * other.z),
                (from.x * other.y) - (from.y * other.x)
        );
    }

    public static class Sphere{
        public final Point center;
        public final float radius;

        public Sphere(Point center, float radius){
            this.center = center;
            this.radius = radius;
        }
    }

    public static boolean intersects(Sphere sphere, Ray ray){
        //Log.i("funcion intersect:",  " datos de la esfera,center x " +
        //sphere.center.x + " , y: " + sphere.center.y + " , z " + sphere.center.z +
        //" , el rayo ray point x : " + ray.point.x + " , ray point.y " + ray.point.y + ", " +
        //" ray point.z " + ray.point.z);
        return distanceBetween(sphere.center, ray) < sphere.radius;
    }

    public static Point intersectionPoint(Ray ray, Plane plane){
        Vector rayToPlaneVector =
                vectorBetween(ray.point, plane.point);
        float scaleFactor =
                rayToPlaneVector.dotProduct(plane.normal) / ray.vector.dotProduct(plane.normal);
        Point intersectionPoint = ray.point.translate(ray.vector.scale(scaleFactor));
        return intersectionPoint;
    }

    public static  float distanceBetween(Point point, Ray ray){
        Vector p1ToPoint = vectorBetween(ray.point, point);
        Vector p2ToPoint = vectorBetween(ray.point.translate(ray.vector), point);

        /*
        The length of the cross product gives the area of an imaginary
        parallelogram having the two vectors as sides. A parallelogram
        can be thought of as consisting of two triangles, so this is the same
        as twice the area of the  triangle defined by the two vectors.
         */
        float areaOfTriangleTimesTwo = p1ToPoint.crossProduct(p2ToPoint).length();
        float lengthOfBase = ray.vector.length();

        /*
        The area of a triangle is also equal to (base * height) / 2.In
        other words , the height is equal to ( area * 2) / base. The height
        of this triangle is the distance from the point to the ray.
         */
        float distanceFromPointToRay = areaOfTriangleTimesTwo / lengthOfBase;
        return distanceFromPointToRay;
    }

    public static class Plane{
        public final Point point;
        public final Vector normal;

        public Plane(Point point, Vector normal){
            this.point = point;
            this.normal = normal;
        }
    }

    public static double calculateAngle(double x1, double y1, double x2, double y2)
    {
        double angleinradian = Math.atan2(x2 - x1, y2 - y1);
        double angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
        // Keep angle between 0 and 360
        angle = angle + Math.ceil( -angle / 360 ) * 360;
        Log.i("calculateAngle","el angulo en radianes es:" + angleinradian +
                ", en grados es: " + angle);
        return angle;
    }
    public static double calculateAngleInRadian(double x1, double y1, double x2, double y2)
    {
        double angleinradian = Math.atan2(x2 - x1, y2 - y1);
        double angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
        // Keep angle between 0 and 360
        angle = angle + Math.ceil( -angle / 360 ) * 360;
        Log.i("calculateAngle","el angulo en radianes es:" + angleinradian +
                ", en grados es: " + angle);
        return angleinradian;
    }

}
