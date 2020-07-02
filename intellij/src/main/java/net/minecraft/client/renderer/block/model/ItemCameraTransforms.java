package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.util.vector.Quaternion;

public class ItemCameraTransforms
{
    public static final ItemCameraTransforms DEFAULT = new ItemCameraTransforms();
    public static float field_181690_b;
    public static float field_181691_c;
    public static float field_181692_d;
    public static float field_181693_e;
    public static float field_181694_f;
    public static float field_181695_g;
    public static float field_181696_h;
    public static float field_181697_i;
    public static float field_181698_j;
    public final ItemTransformVec3f thirdperson_left;
    public final ItemTransformVec3f thirdperson_right;
    public final ItemTransformVec3f firstperson_left;
    public final ItemTransformVec3f firstperson_right;
    public final ItemTransformVec3f head;
    public final ItemTransformVec3f gui;
    public final ItemTransformVec3f ground;
    public final ItemTransformVec3f fixed;

    private ItemCameraTransforms()
    {
        this(ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT);
    }

    public ItemCameraTransforms(ItemCameraTransforms transforms)
    {
        this.thirdperson_left = transforms.thirdperson_left;
        this.thirdperson_right = transforms.thirdperson_right;
        this.firstperson_left = transforms.firstperson_left;
        this.firstperson_right = transforms.firstperson_right;
        this.head = transforms.head;
        this.gui = transforms.gui;
        this.ground = transforms.ground;
        this.fixed = transforms.fixed;
    }

    public ItemCameraTransforms(ItemTransformVec3f thirdperson_leftIn, ItemTransformVec3f thirdperson_rightIn, ItemTransformVec3f firstperson_leftIn, ItemTransformVec3f firstperson_rightIn, ItemTransformVec3f headIn, ItemTransformVec3f guiIn, ItemTransformVec3f groundIn, ItemTransformVec3f fixedIn)
    {
        this.thirdperson_left = thirdperson_leftIn;
        this.thirdperson_right = thirdperson_rightIn;
        this.firstperson_left = firstperson_leftIn;
        this.firstperson_right = firstperson_rightIn;
        this.head = headIn;
        this.gui = guiIn;
        this.ground = groundIn;
        this.fixed = fixedIn;
    }

    public void func_181689_a(ItemCameraTransforms.TransformType p_181689_1_)
    {
        func_188034_a(this.getTransform(p_181689_1_), false);
    }

    public static void func_188034_a(ItemTransformVec3f p_188034_0_, boolean p_188034_1_)
    {
        if (p_188034_0_ != ItemTransformVec3f.DEFAULT)
        {
            int i = p_188034_1_ ? -1 : 1;
            GlStateManager.func_179109_b((float)i * (field_181690_b + p_188034_0_.translation.x), field_181691_c + p_188034_0_.translation.y, field_181692_d + p_188034_0_.translation.z);
            float f = field_181693_e + p_188034_0_.rotation.x;
            float f1 = field_181694_f + p_188034_0_.rotation.y;
            float f2 = field_181695_g + p_188034_0_.rotation.z;

            if (p_188034_1_)
            {
                f1 = -f1;
                f2 = -f2;
            }

            GlStateManager.func_187444_a(func_188035_a(f, f1, f2));
            GlStateManager.func_179152_a(field_181696_h + p_188034_0_.scale.x, field_181697_i + p_188034_0_.scale.y, field_181698_j + p_188034_0_.scale.z);
        }
    }

    private static Quaternion func_188035_a(float p_188035_0_, float p_188035_1_, float p_188035_2_)
    {
        float f = p_188035_0_ * 0.017453292F;
        float f1 = p_188035_1_ * 0.017453292F;
        float f2 = p_188035_2_ * 0.017453292F;
        float f3 = MathHelper.sin(0.5F * f);
        float f4 = MathHelper.cos(0.5F * f);
        float f5 = MathHelper.sin(0.5F * f1);
        float f6 = MathHelper.cos(0.5F * f1);
        float f7 = MathHelper.sin(0.5F * f2);
        float f8 = MathHelper.cos(0.5F * f2);
        return new Quaternion(f3 * f6 * f8 + f4 * f5 * f7, f4 * f5 * f8 - f3 * f6 * f7, f3 * f5 * f8 + f4 * f6 * f7, f4 * f6 * f8 - f3 * f5 * f7);
    }

    public ItemTransformVec3f getTransform(ItemCameraTransforms.TransformType type)
    {
        switch (type)
        {
            case THIRD_PERSON_LEFT_HAND:
                return this.thirdperson_left;

            case THIRD_PERSON_RIGHT_HAND:
                return this.thirdperson_right;

            case FIRST_PERSON_LEFT_HAND:
                return this.firstperson_left;

            case FIRST_PERSON_RIGHT_HAND:
                return this.firstperson_right;

            case HEAD:
                return this.head;

            case GUI:
                return this.gui;

            case GROUND:
                return this.ground;

            case FIXED:
                return this.fixed;

            default:
                return ItemTransformVec3f.DEFAULT;
        }
    }

    public boolean hasCustomTransform(ItemCameraTransforms.TransformType type)
    {
        return this.getTransform(type) != ItemTransformVec3f.DEFAULT;
    }

    static class Deserializer implements JsonDeserializer<ItemCameraTransforms>
    {
        public ItemCameraTransforms deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
        {
            JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            ItemTransformVec3f itemtransformvec3f = this.getTransform(p_deserialize_3_, jsonobject, "thirdperson_righthand");
            ItemTransformVec3f itemtransformvec3f1 = this.getTransform(p_deserialize_3_, jsonobject, "thirdperson_lefthand");

            if (itemtransformvec3f1 == ItemTransformVec3f.DEFAULT)
            {
                itemtransformvec3f1 = itemtransformvec3f;
            }

            ItemTransformVec3f itemtransformvec3f2 = this.getTransform(p_deserialize_3_, jsonobject, "firstperson_righthand");
            ItemTransformVec3f itemtransformvec3f3 = this.getTransform(p_deserialize_3_, jsonobject, "firstperson_lefthand");

            if (itemtransformvec3f3 == ItemTransformVec3f.DEFAULT)
            {
                itemtransformvec3f3 = itemtransformvec3f2;
            }

            ItemTransformVec3f itemtransformvec3f4 = this.getTransform(p_deserialize_3_, jsonobject, "head");
            ItemTransformVec3f itemtransformvec3f5 = this.getTransform(p_deserialize_3_, jsonobject, "gui");
            ItemTransformVec3f itemtransformvec3f6 = this.getTransform(p_deserialize_3_, jsonobject, "ground");
            ItemTransformVec3f itemtransformvec3f7 = this.getTransform(p_deserialize_3_, jsonobject, "fixed");
            return new ItemCameraTransforms(itemtransformvec3f1, itemtransformvec3f, itemtransformvec3f3, itemtransformvec3f2, itemtransformvec3f4, itemtransformvec3f5, itemtransformvec3f6, itemtransformvec3f7);
        }

        private ItemTransformVec3f getTransform(JsonDeserializationContext p_181683_1_, JsonObject p_181683_2_, String p_181683_3_)
        {
            return p_181683_2_.has(p_181683_3_) ? (ItemTransformVec3f)p_181683_1_.deserialize(p_181683_2_.get(p_181683_3_), ItemTransformVec3f.class) : ItemTransformVec3f.DEFAULT;
        }
    }

    public static enum TransformType
    {
        NONE,
        THIRD_PERSON_LEFT_HAND,
        THIRD_PERSON_RIGHT_HAND,
        FIRST_PERSON_LEFT_HAND,
        FIRST_PERSON_RIGHT_HAND,
        HEAD,
        GUI,
        GROUND,
        FIXED;
    }
}
