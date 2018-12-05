package com.particles.android.programs;

import android.content.Context;

import com.particles.android.R;
import com.particles.android.ShaderProgram;

import static android.opengl.GLES20.*;

public class ParticleShaderProgram extends ShaderProgram {
    //Uniform Locations
    private final int uMatrixLocation;
    private final int uTimeLocation;
    //Attribute Location
    private final int aPositionLocation;
    private final int aColorLocation;
    private final int aDirectionVectorLocation;
    private final int aParticleStartTimeLocation;

    public ParticleShaderProgram(Context context) {
        super(context, R.raw.particle_vertex_shader, R.raw.particle_fragment_shader);
        //Retrieve uniform locations for the shader program.
        this.uMatrixLocation = glGetUniformLocation(
                super.program, super.U_MATRIX
        );
        this.uTimeLocation = glGetUniformLocation(
                super.program, super.U_TIME
        );
        //Retrieve attribute locations for the shader program.
        this.aPositionLocation = glGetAttribLocation(
                super.program, super.A_POSITION
        );
        this.aColorLocation = glGetAttribLocation(
                super.program, super.A_COLOR
        );
        this.aDirectionVectorLocation = glGetAttribLocation(
                super.program, super.A_DIRECTION_VECTOR
        );
        this.aParticleStartTimeLocation = glGetAttribLocation(
                super.program, super.A_PARTICLE_START_TIME
        );
    }
    public void setUniforms(float matrix[], float elapsedTime){
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        glUniform1f(uTimeLocation, elapsedTime);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }
    public int getColorAttributeLocation() {
        return aColorLocation;
    }
    public int getDirectionVectorAttributeLocation() {
        return aDirectionVectorLocation;
    }
    public int getParticleStartTimeAttributeLocation() {
        return aParticleStartTimeLocation;
    }
}
