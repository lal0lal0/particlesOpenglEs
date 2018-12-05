package com.particles.android;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Created by miMiau on 05/04/2018.
 */

public class VertexArray {

    // utiliza un buffer de tipo float porque las coordenadas estan en float
    private final FloatBuffer floatBuffer;

    // constructor recibe un arreglo lineal, reserva el espacio en memoria que requiera
    // lo convierte al tipo nativo del sistema y guarda referencia al vertexData
    public VertexArray(float[] vertexData) {
        floatBuffer = ByteBuffer.allocateDirect(vertexData.length * Constants.BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
    }


    public void setVertexAttribPointer(int dataOffset, int attributeLocation,
                                       int componentCount, int stride){
        floatBuffer.position(dataOffset);
        glVertexAttribPointer(attributeLocation, componentCount,
                GL_FLOAT, false, stride, floatBuffer);
        glEnableVertexAttribArray(attributeLocation);

    }

    public void updateBuffer(float[] vertexData, int start, int count){
        floatBuffer.position(start);
        floatBuffer.put(vertexData, start, count);
        floatBuffer.position(0);
    }

}
