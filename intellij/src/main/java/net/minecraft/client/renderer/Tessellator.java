package net.minecraft.client.renderer;

public class Tessellator
{
    private final BufferBuilder buffer;
    private final WorldVertexBufferUploader field_178182_b = new WorldVertexBufferUploader();
    private static final Tessellator INSTANCE = new Tessellator(2097152);

    public static Tessellator getInstance()
    {
        return INSTANCE;
    }

    public Tessellator(int bufferSize)
    {
        this.buffer = new BufferBuilder(bufferSize);
    }

    /**
     * Draws the data set up in this tessellator and resets the state to prepare for new drawing.
     */
    public void draw()
    {
        this.buffer.finishDrawing();
        this.field_178182_b.draw(this.buffer);
    }

    public BufferBuilder getBuffer()
    {
        return this.buffer;
    }
}
