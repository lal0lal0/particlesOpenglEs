package com.particles.android.programs;

import android.graphics.Color;
import android.opengl.GLES20;
import com.particles.android.Geometry;
import com.particles.android.VertexArray;
import static android.opengl.GLES20.GL_POINTS;
import static com.particles.android.Constants.BYTES_PER_FLOAT;

public class ParticleSystem {

    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COMPONENT_COUNT    = 3;
    private static final int VECTOR_COMPONENT_COUNT   = 3;
    private static final int PARTICLE_START_TIME_COMPONENT_COUNT = 1;

    private static final int TOTAL_COMPONENT_COUNT =
                    POSITION_COMPONENT_COUNT +
                    COLOR_COMPONENT_COUNT    +
                    VECTOR_COMPONENT_COUNT   +
                    PARTICLE_START_TIME_COMPONENT_COUNT;

    private static final int STRIDE = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT;

    private final float[] particles;
    private final VertexArray vertexArray;

    private final int maxParticleCount;
    private int currentParticleCount;
    private int nextParticle;

    public ParticleSystem(int maxParticleCount){
        particles = new float[maxParticleCount * TOTAL_COMPONENT_COUNT];
        vertexArray = new VertexArray(particles);
        this.maxParticleCount = maxParticleCount;
    }
    public void addParticle(Geometry.Point position, int color,
                            Geometry.Vector direction, float particleStartTime){
        final int particleoffset = nextParticle * TOTAL_COMPONENT_COUNT;
        int currentoffset = particleoffset;
        nextParticle++;
        if(currentParticleCount < maxParticleCount){
            currentParticleCount++;
        }
        if(nextParticle == maxParticleCount){
            /*
            Start over at the beginning, but keep currentParticleCount
            so that all the other particles still get drawn
             */
            nextParticle = 0;
        }

        particles[currentoffset++] = position.x;
        particles[currentoffset++] = position.y;
        particles[currentoffset++] = position.z;

        particles[currentoffset++] = Color.red(color) / 255f;
        particles[currentoffset++] = Color.green(color) / 255f;
        particles[currentoffset++] = Color.blue(color) / 255f;

        particles[currentoffset++] = direction.x;
        particles[currentoffset++] = direction.y;
        particles[currentoffset++] = direction.z;

        particles[currentoffset] = particleStartTime;

        vertexArray.updateBuffer(particles, particleoffset, TOTAL_COMPONENT_COUNT);
    }
    public void bindData(ParticleShaderProgram particleProgram){
        int dataoffset = 0;

        vertexArray.setVertexAttribPointer(dataoffset,
                particleProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE
        );
        dataoffset += POSITION_COMPONENT_COUNT;

        vertexArray.setVertexAttribPointer(dataoffset,
                particleProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE);
        dataoffset += COLOR_COMPONENT_COUNT;

        vertexArray.setVertexAttribPointer(dataoffset,
                particleProgram.getDirectionVectorAttributeLocation(),
                VECTOR_COMPONENT_COUNT,
                STRIDE);
        dataoffset += VECTOR_COMPONENT_COUNT;

        vertexArray.setVertexAttribPointer(dataoffset,
                particleProgram.getParticleStartTimeAttributeLocation(),
                PARTICLE_START_TIME_COMPONENT_COUNT,
                STRIDE);
    }
    public void draw(){
        GLES20.glDrawArrays(GL_POINTS, 0,  currentParticleCount);
    }
}
