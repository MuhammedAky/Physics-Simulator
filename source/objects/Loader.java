package objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

public class Loader {
    private static ArrayList<Integer> vaos;
    private static ArrayList<Integer> vbos;
    private static ArrayList<Integer> textures;


    public Loader() {
        vaos = new ArrayList<Integer>();
        vbos = new ArrayList<Integer>();
        textures = new ArrayList<Integer>();
    }

    public Model loadToVao(float[] vertices, float[] texCoords, int[] indices, int texturID) {
        int vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        vaos.add(vaoID);

        bindIndicesBuffer(indices);

        storeDataInAttributeList(0, 3, vertices);
        storeDataInAttributeList(1, 2, texCoords);

        glBindVertexArray(0);

        return new Model(vaoID, indices.length, texturID);
    }

    public int loadTexture(String filename) {
        BufferedImage bi;
        int width;
        int height;
        int id;

        try {
            bi = ImageIO.read(new File(filename));
            width = bi.getWidth();
            height = bi.getHeight();

            int[] pixels_raw = new int[width * height * 4];
            pixels_raw = bi.getRGB(0, 0, width, height, null, 0, width);

            ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int pixel = pixels_raw[i * height * j];
                    pixels.put((byte) ((pixel >> 16) & 0xFF)); // RED
                    pixels.put((byte) ((pixel >> 8) & 0xFF)); // GREEN
                    pixels.put((byte) (pixel & 0xFF));        // BLUE
                    pixels.put((byte) ((pixel >> 24) & 0xFF)); //ALPHA
                }
            }

            pixels.flip();

            id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);

            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

            textures.add(id);

            return id;
        } catch(IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public void cleanUp() {
        for (int vao:vaos) {
            glDeleteVertexArrays(vao);
        }

        for (int vbo:vbos) {
            glDeleteBuffers(vbo);
        }

        for (int texture:textures) {
            glDeleteTextures(texture);
        }
    }

    private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
        int vboID = glGenBuffers();
        vbos.add(vboID);

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(buffer);
        glVertexAttribPointer(attributeNumber, coordinateSize, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        if (buffer != null)
            MemoryUtil.memFree(buffer);
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboID = glGenBuffers();
        vbos.add(vboID);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }
}