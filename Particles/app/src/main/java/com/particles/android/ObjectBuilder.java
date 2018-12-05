package com.particles.android;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.particles.android.Geometry.Circle;
import com.particles.android.Geometry.Cylinder;
import com.particles.android.Geometry.Point;

import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;


/**
 * Created by miMiau on 10/06/2018.
 */


public class ObjectBuilder {


    private static final int FLOATS_PER_VERTEX = 6;
    private final float[] vertexData;
    private final List<DrawCommand> drawList = new ArrayList<DrawCommand>();
    private int offset = 0;


    private ObjectBuilder(int sizeInVertices) {
        vertexData = new float[sizeInVertices * FLOATS_PER_VERTEX];
    }



    //Calculate the size of a Cylinder Top
    private static int sizeOfCircleInVertices(int numPoints) {
        return 1 + (numPoints + 1);
    }
    private static int sizeOfTriangleStripInVertices(int numPoints) {
        return 1 + (numPoints + 1);
    }


    private static int sizeOfOpenCylinderInVertices(int numPoints) {
        // se multiplica x 2 x porque son 2 vertices requeridos por cada punto
        return (numPoints + 1) * 2;
    }

    private static int sizeOfSimpleTriangleInVertices(){
        return (3);
    }

    private static int sizeOfLineInVertices(){
        return (2);
    }

    static GeneratedData createCilindro(Cylinder cilindro, int numPoints){
        int size = sizeOfOpenCylinderInVertices(numPoints);
        ObjectBuilder builder = new ObjectBuilder(size);
        builder.appendOpenCylinder(cilindro, numPoints);
        return builder.build();
    }

    static GeneratedData createCirculo(Circle circulo, int numPoints){
        int size = sizeOfCircleInVertices(numPoints);
        ObjectBuilder builder = new ObjectBuilder(size);
        builder.appendCirculo(circulo, numPoints);
        return builder.build();
    }

    static GeneratedData createTriangleCircleStrip(Circle circulo, int numPoints){
        int size = sizeOfTriangleStripInVertices(numPoints);
        ObjectBuilder builder = new ObjectBuilder(size);
        //aqui el append

        return builder.build();
    }


    static GeneratedData createPuck(Cylinder puck, int numPoints) {
        int size = sizeOfCircleInVertices(numPoints) +
                   + sizeOfOpenCylinderInVertices(numPoints);
        ObjectBuilder builder = new ObjectBuilder(size);
        Circle puckTop = new Circle(
                puck.center.translateY(puck.height / 2f),
                puck.radius);
        builder.appendOpenCylinder(puck, numPoints);
        builder.appendCircle(puckTop, numPoints);
        return builder.build();
    }

    static GeneratedData createTrianguloBasico(Point center, int numPoints) {
        int size = sizeOfSimpleTriangleInVertices();
        ObjectBuilder builder = new ObjectBuilder(size);
        Geometry.Triangulo triangulo  = new Geometry.Triangulo(center);

        builder.appendTrianguloBasico(triangulo, numPoints);
        return builder.build();
    }

    static GeneratedData createLinea() {
        int size = sizeOfLineInVertices();
        ObjectBuilder builder = new ObjectBuilder(size);
        //Geometry.Linea linea  = new Geometry.Linea();
        builder.appendLinea();
        return builder.build();
    }



    //Holder Class  es un holder para el array vertexData y la lista de comandos de dibujo
    static class GeneratedData {

        final float[] vertexData;
        final List<DrawCommand> drawList;

        GeneratedData(float[] vertexData , List<DrawCommand> drawList) {
            this.vertexData = vertexData;
            this.drawList = drawList;
        }

        //Construye y retorna una instancia de GenratedData
        private GeneratedData build(){
            return new GeneratedData(vertexData, drawList);
        }

    }


    // createMallet es un metodo estatico para hacer el objeto
    //  y tener la referencia al array vertexData y su drawList
    static GeneratedData createMallet(
            Point center, float radius, float height, int numPoints){

        // Calcular el tamanio requerido para almacenar los objetos a dibujar
        int size = sizeOfCircleInVertices(numPoints) * 2 +
                sizeOfOpenCylinderInVertices(numPoints) * 2;

        ObjectBuilder builder = new ObjectBuilder(size);

        //First genrate the mallet base
        float  baseHeight = height * 0.25f;

        // Creamos un nuevo circulo, pasandole el radio, y moviendolo de lugar
        //  movemos el circulo a la posicion inicial deseada
        //  usando el vertice denominado: center , que hereda de Point y
        //  usamos su funcion translacion en eje Y, con la cantidad negativa
        //  equivalente a la altura.
        Circle baseCircle = new Circle( center.translateY( -baseHeight ), radius);

        // Ahora creamos un cilindro, se requiere el radio, un vertice central
        // y la altura. el centro de este cilindro es en referencia a
        // el primer circulo , en este metodo la altura se divide entre 2
        // para que el vertice central del cilindro quede ubicado hacia abajo.
        Cylinder baseCylinder = new Cylinder(
                baseCircle.center.translateY(-baseHeight / 2f), radius, baseHeight);

        builder.appendOpenCylinder(baseCylinder, numPoints);
        builder.appendCircle(baseCircle, numPoints);


        //Generate the handle
        float handleHeight = height * 0.75f;
        float handleRadius = radius / 3f;

        Circle handleCircle = new Circle(center.translateY( height * 0.75f), handleRadius);
        Cylinder handleCylinder = new Cylinder(
                handleCircle.center.translateY( -handleHeight / 2f ), handleRadius, handleHeight);

        builder.appendOpenCylinder( handleCylinder, numPoints);
        builder.appendCircle( handleCircle, numPoints);

        return builder.build();
    }

    private GeneratedData build(){
        return new GeneratedData(vertexData, drawList);
    }

    public void appendTrianguloBasico(Geometry.Triangulo triangulo, int numPoints){
        final int startVertex = offset / FLOATS_PER_VERTEX;
        final int numVertices = sizeOfSimpleTriangleInVertices();

        vertexData[offset++] = triangulo.center.x;
        vertexData[offset++] = triangulo.center.y;
        vertexData[offset++] = triangulo.center.z;


        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_LINES, startVertex, numVertices);
            }
        });
    }

    public void appendLinea(){
        final int startVertex = offset / FLOATS_PER_VERTEX;
        final int numVertices = sizeOfLineInVertices();

        vertexData[offset++] = 0.0f;
        vertexData[offset++] = 0.6f;
        vertexData[offset++] = 0.0f;


        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_LINES, startVertex, numVertices);
            }
        });
    }

    public void appendCirculo(Circle circulo, int numPoints){
        Random random = new Random();

        final int startVertex = offset / FLOATS_PER_VERTEX;
        final int numVertices = sizeOfCircleInVertices(numPoints);

        vertexData[offset++] = 0.0f;
        vertexData[offset++] = 0.0f;
        vertexData[offset++] = 0.0f;
        vertexData[offset++] = 0.1f;
        vertexData[offset++] = 0.2f;
        vertexData[offset++] = 0.3f;

        for(int i = 0; i <= numPoints; i++){
            float angleInRadians =
                    ((float) i / (float) numPoints)
                            * ((float) Math.PI * 2f);
            vertexData[offset++] =
                    circulo.center.x
                            + circulo.radius * (float)Math.cos(angleInRadians);
            vertexData[offset++] = circulo.center.y;
            vertexData[offset++] =
                    circulo.center.z
                            + circulo.radius * (float)Math.sin(angleInRadians);
            vertexData[offset++] = 0.1f + random.nextFloat();
            vertexData[offset++] = 0.2f + random.nextFloat();;
            vertexData[offset++] = 0.3f + random.nextFloat();;
        }


        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_FAN, startVertex, numVertices);
            }
        });

    }


    public void appendCircle(Circle circle, int numPoints) {

        final int startVertex = offset / FLOATS_PER_VERTEX;
        final int numVertices = sizeOfCircleInVertices(numPoints);


        vertexData[offset++] = circle.center.x;
        vertexData[offset++] = circle.center.y;
        vertexData[offset++] = circle.center.z;
        vertexData[offset++] = 0.50f;
        vertexData[offset++] = 0.0f;
        vertexData[offset++] = 0.0f;


        //Por eso el punto del centro se define fuera del FOR


        // fan around center point. <= is used because
        // we want to generate the point at the
        // starting  angle twice  to complete the fan.

        for (int i = 0; i <= numPoints; i++) {

            // Calcular y convertir el angulo en Radianes
            // para ello, el indice actual i que incrementa en ++
            // se divide por la cantidad todal de puntos o vertices
            // luego se mulitplica por el doble de pi
            float angleInRadians = ((float) i / (float) numPoints) *
                    ((float) Math.PI * 2f);

            vertexData[offset++] = (float) (circle.center.x +
                    circle.radius * Math.cos(angleInRadians));

            vertexData[offset++] = circle.center.y;

            vertexData[offset++] = (float) (circle.center.z +
                    circle.radius * Math.sin(angleInRadians));

            vertexData[offset++] = 0.90f;
            vertexData[offset++] = 0.0f;
            vertexData[offset++] = 0.0f;
            //correccion : FloatMath() esta deprecated,
            // x lo que se usa Math.cos() con casting a float

        }


        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_FAN, startVertex, numVertices);
            }
        });
    }


    //Interfaz drawCommand
    static interface DrawCommand {
        void draw();
    }


    public void appendOpenCylinder(Cylinder cylinder, int numPoints) {

        Random random = new Random();
        final int startVertex = offset / FLOATS_PER_VERTEX;
        final int numVertices = sizeOfOpenCylinderInVertices(numPoints);
        final float yStart = cylinder.center.y - (cylinder.height / 2f);
        final float yEnd = cylinder.center.y + (cylinder.height / 2f);

        //Generate Triangle Strip
        for (int i = 0; i <= numPoints; i++) {

            //Calcular el angulo en  radianes
            float angleInRadians = ((float) i / (float) numPoints) *
                    (float) (Math.PI * 2f);

            float xPosition = cylinder.center.x + cylinder.radius *
                    ((float) Math.cos(angleInRadians));

            float zPosition = cylinder.center.z + cylinder.radius *
                    ((float) Math.sin(angleInRadians));


            //Top cylinder vertice este es un vertice
            vertexData[offset++] = xPosition;
            vertexData[offset++] = yStart;
            vertexData[offset++] = zPosition;
            vertexData[offset++] = 0.1f + random.nextFloat();
            vertexData[offset++] = 0.1f + random.nextFloat();
            vertexData[offset++] = 0.0f + random.nextFloat();


            //Bottom cylinder vertice, segundo vertice
            vertexData[offset++] = xPosition;
            vertexData[offset++] = yEnd;
            vertexData[offset++] = zPosition;
            vertexData[offset++] = 0.1f + random.nextFloat();
            vertexData[offset++] = 0.1f + random.nextFloat();
            vertexData[offset++] = 0.0f + random.nextFloat();
        }


        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices);
            }
        });

    }//fin de appendOpenCylinder()


}
