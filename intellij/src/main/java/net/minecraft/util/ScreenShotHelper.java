package net.minecraft.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;

public class ScreenShotHelper
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    private static IntBuffer field_74293_b;
    private static int[] field_74294_c;

    /**
     * Saves a screenshot in the game directory with a time-stamped filename.
     * Returns an ITextComponent indicating the success/failure of the saving.
     */
    public static ITextComponent saveScreenshot(File gameDirectory, int width, int height, Framebuffer buffer)
    {
        return saveScreenshot(gameDirectory, (String)null, width, height, buffer);
    }

    /**
     * Saves a screenshot in the game directory with the given file name (or null to generate a time-stamped name).
     * Returns an ITextComponent indicating the success/failure of the saving.
     */
    public static ITextComponent saveScreenshot(File gameDirectory, @Nullable String screenshotName, int width, int height, Framebuffer buffer)
    {
        try
        {
            File file1 = new File(gameDirectory, "screenshots");
            file1.mkdir();
            BufferedImage bufferedimage = func_186719_a(width, height, buffer);
            File file2;

            if (screenshotName == null)
            {
                file2 = getTimestampedPNGFileForDirectory(file1);
            }
            else
            {
                file2 = new File(file1, screenshotName);
            }

            ImageIO.write(bufferedimage, "png", file2);
            ITextComponent itextcomponent = new TextComponentString(file2.getName());
            itextcomponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file2.getAbsolutePath()));
            itextcomponent.getStyle().setUnderlined(Boolean.valueOf(true));
            return new TextComponentTranslation("screenshot.success", new Object[] {itextcomponent});
        }
        catch (Exception exception)
        {
            LOGGER.warn("Couldn't save screenshot", (Throwable)exception);
            return new TextComponentTranslation("screenshot.failure", new Object[] {exception.getMessage()});
        }
    }

    public static BufferedImage func_186719_a(int p_186719_0_, int p_186719_1_, Framebuffer p_186719_2_)
    {
        if (OpenGlHelper.func_148822_b())
        {
            p_186719_0_ = p_186719_2_.framebufferTextureWidth;
            p_186719_1_ = p_186719_2_.framebufferTextureHeight;
        }

        int i = p_186719_0_ * p_186719_1_;

        if (field_74293_b == null || field_74293_b.capacity() < i)
        {
            field_74293_b = BufferUtils.createIntBuffer(i);
            field_74294_c = new int[i];
        }

        GlStateManager.func_187425_g(3333, 1);
        GlStateManager.func_187425_g(3317, 1);
        field_74293_b.clear();

        if (OpenGlHelper.func_148822_b())
        {
            GlStateManager.func_179144_i(p_186719_2_.framebufferTexture);
            GlStateManager.func_187433_a(3553, 0, 32993, 33639, field_74293_b);
        }
        else
        {
            GlStateManager.func_187413_a(0, 0, p_186719_0_, p_186719_1_, 32993, 33639, field_74293_b);
        }

        field_74293_b.get(field_74294_c);
        TextureUtil.func_147953_a(field_74294_c, p_186719_0_, p_186719_1_);
        BufferedImage bufferedimage = new BufferedImage(p_186719_0_, p_186719_1_, 1);
        bufferedimage.setRGB(0, 0, p_186719_0_, p_186719_1_, field_74294_c, 0, p_186719_0_);
        return bufferedimage;
    }

    /**
     * Creates a unique PNG file in the given directory named by a timestamp.  Handles cases where the timestamp alone
     * is not enough to create a uniquely named file, though it still might suffer from an unlikely race condition where
     * the filename was unique when this method was called, but another process or thread created a file at the same
     * path immediately after this method returned.
     */
    private static File getTimestampedPNGFileForDirectory(File gameDirectory)
    {
        String s = DATE_FORMAT.format(new Date()).toString();
        int i = 1;

        while (true)
        {
            File file1 = new File(gameDirectory, s + (i == 1 ? "" : "_" + i) + ".png");

            if (!file1.exists())
            {
                return file1;
            }

            ++i;
        }
    }
}
