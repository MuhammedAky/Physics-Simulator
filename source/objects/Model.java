package source.objects;

public class Model {
    private int vaoID;
    private int textureID;
    private int vertexCount;

    public Model(int vaoID, int vertexCount, int textureID) {
        this.vaoID = vaoID;
        this.textureID = textureID;
        this.vertexCount = vertexCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getTextureID() {
        return textureID;
    }

    public void setTextureID(String texture, Loader loader) {
        this.textureID = loader.loadTexture(texture);
    }
}