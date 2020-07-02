package net.minecraft.client.renderer.block.model;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModelBlock
{
    private static final Logger LOGGER = LogManager.getLogger();
    @VisibleForTesting
    static final Gson SERIALIZER = (new GsonBuilder()).registerTypeAdapter(ModelBlock.class, new ModelBlock.Deserializer()).registerTypeAdapter(BlockPart.class, new BlockPart.Deserializer()).registerTypeAdapter(BlockPartFace.class, new BlockPartFace.Deserializer()).registerTypeAdapter(BlockFaceUV.class, new BlockFaceUV.Deserializer()).registerTypeAdapter(ItemTransformVec3f.class, new ItemTransformVec3f.Deserializer()).registerTypeAdapter(ItemCameraTransforms.class, new ItemCameraTransforms.Deserializer()).registerTypeAdapter(ItemOverride.class, new ItemOverride.Deserializer()).create();
    private final List<BlockPart> elements;
    private final boolean gui3d;
    private final boolean ambientOcclusion;
    private final ItemCameraTransforms cameraTransforms;
    private final List<ItemOverride> overrides;
    public String name = "";
    @VisibleForTesting
    protected final Map<String, String> textures;
    @VisibleForTesting
    protected ModelBlock parent;
    @VisibleForTesting
    protected ResourceLocation parentLocation;

    public static ModelBlock deserialize(Reader readerIn)
    {
        return (ModelBlock)JsonUtils.fromJson(SERIALIZER, readerIn, ModelBlock.class, false);
    }

    public static ModelBlock deserialize(String jsonString)
    {
        return deserialize(new StringReader(jsonString));
    }

    public ModelBlock(@Nullable ResourceLocation parentLocationIn, List<BlockPart> elementsIn, Map<String, String> texturesIn, boolean ambientOcclusionIn, boolean gui3dIn, ItemCameraTransforms cameraTransformsIn, List<ItemOverride> overridesIn)
    {
        this.elements = elementsIn;
        this.ambientOcclusion = ambientOcclusionIn;
        this.gui3d = gui3dIn;
        this.textures = texturesIn;
        this.parentLocation = parentLocationIn;
        this.cameraTransforms = cameraTransformsIn;
        this.overrides = overridesIn;
    }

    public List<BlockPart> getElements()
    {
        return this.elements.isEmpty() && this.func_178295_k() ? this.parent.getElements() : this.elements;
    }

    private boolean func_178295_k()
    {
        return this.parent != null;
    }

    public boolean isAmbientOcclusion()
    {
        return this.func_178295_k() ? this.parent.isAmbientOcclusion() : this.ambientOcclusion;
    }

    public boolean isGui3d()
    {
        return this.gui3d;
    }

    public boolean func_178303_d()
    {
        return this.parentLocation == null || this.parent != null && this.parent.func_178303_d();
    }

    public void func_178299_a(Map<ResourceLocation, ModelBlock> p_178299_1_)
    {
        if (this.parentLocation != null)
        {
            this.parent = p_178299_1_.get(this.parentLocation);
        }
    }

    public Collection<ResourceLocation> getDependencies()
    {
        Set<ResourceLocation> set = Sets.<ResourceLocation>newHashSet();

        for (ItemOverride itemoverride : this.overrides)
        {
            set.add(itemoverride.getLocation());
        }

        return set;
    }

    protected List<ItemOverride> getOverrides()
    {
        return this.overrides;
    }

    public ItemOverrideList func_187967_g()
    {
        return this.overrides.isEmpty() ? ItemOverrideList.EMPTY : new ItemOverrideList(this.overrides);
    }

    public boolean isTexturePresent(String textureName)
    {
        return !"missingno".equals(this.func_178308_c(textureName));
    }

    public String func_178308_c(String p_178308_1_)
    {
        if (!this.startsWithHash(p_178308_1_))
        {
            p_178308_1_ = '#' + p_178308_1_;
        }

        return this.func_178302_a(p_178308_1_, new ModelBlock.Bookkeep(this));
    }

    private String func_178302_a(String p_178302_1_, ModelBlock.Bookkeep p_178302_2_)
    {
        if (this.startsWithHash(p_178302_1_))
        {
            if (this == p_178302_2_.field_178323_b)
            {
                LOGGER.warn("Unable to resolve texture due to upward reference: {} in {}", p_178302_1_, this.name);
                return "missingno";
            }
            else
            {
                String s = this.textures.get(p_178302_1_.substring(1));

                if (s == null && this.func_178295_k())
                {
                    s = this.parent.func_178302_a(p_178302_1_, p_178302_2_);
                }

                p_178302_2_.field_178323_b = this;

                if (s != null && this.startsWithHash(s))
                {
                    s = p_178302_2_.field_178324_a.func_178302_a(s, p_178302_2_);
                }

                return s != null && !this.startsWithHash(s) ? s : "missingno";
            }
        }
        else
        {
            return p_178302_1_;
        }
    }

    private boolean startsWithHash(String p_178304_1_)
    {
        return p_178304_1_.charAt(0) == '#';
    }

    @Nullable
    public ResourceLocation func_178305_e()
    {
        return this.parentLocation;
    }

    public ModelBlock getRootModel()
    {
        return this.func_178295_k() ? this.parent.getRootModel() : this;
    }

    public ItemCameraTransforms getAllTransforms()
    {
        ItemTransformVec3f itemtransformvec3f = this.getTransform(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND);
        ItemTransformVec3f itemtransformvec3f1 = this.getTransform(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
        ItemTransformVec3f itemtransformvec3f2 = this.getTransform(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND);
        ItemTransformVec3f itemtransformvec3f3 = this.getTransform(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND);
        ItemTransformVec3f itemtransformvec3f4 = this.getTransform(ItemCameraTransforms.TransformType.HEAD);
        ItemTransformVec3f itemtransformvec3f5 = this.getTransform(ItemCameraTransforms.TransformType.GUI);
        ItemTransformVec3f itemtransformvec3f6 = this.getTransform(ItemCameraTransforms.TransformType.GROUND);
        ItemTransformVec3f itemtransformvec3f7 = this.getTransform(ItemCameraTransforms.TransformType.FIXED);
        return new ItemCameraTransforms(itemtransformvec3f, itemtransformvec3f1, itemtransformvec3f2, itemtransformvec3f3, itemtransformvec3f4, itemtransformvec3f5, itemtransformvec3f6, itemtransformvec3f7);
    }

    private ItemTransformVec3f getTransform(ItemCameraTransforms.TransformType type)
    {
        return this.parent != null && !this.cameraTransforms.hasCustomTransform(type) ? this.parent.getTransform(type) : this.cameraTransforms.getTransform(type);
    }

    public static void func_178312_b(Map<ResourceLocation, ModelBlock> p_178312_0_)
    {
        for (ModelBlock modelblock : p_178312_0_.values())
        {
            try
            {
                ModelBlock modelblock1 = modelblock.parent;

                for (ModelBlock modelblock2 = modelblock1.parent; modelblock1 != modelblock2; modelblock2 = modelblock2.parent.parent)
                {
                    modelblock1 = modelblock1.parent;
                }

                throw new ModelBlock.LoopException();
            }
            catch (NullPointerException var5)
            {
                ;
            }
        }
    }

    static final class Bookkeep
    {
        public final ModelBlock field_178324_a;
        public ModelBlock field_178323_b;

        private Bookkeep(ModelBlock p_i46223_1_)
        {
            this.field_178324_a = p_i46223_1_;
        }
    }

    public static class Deserializer implements JsonDeserializer<ModelBlock>
    {
        public ModelBlock deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
        {
            JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            List<BlockPart> list = this.getModelElements(p_deserialize_3_, jsonobject);
            String s = this.getParent(jsonobject);
            Map<String, String> map = this.getTextures(jsonobject);
            boolean flag = this.getAmbientOcclusionEnabled(jsonobject);
            ItemCameraTransforms itemcameratransforms = ItemCameraTransforms.DEFAULT;

            if (jsonobject.has("display"))
            {
                JsonObject jsonobject1 = JsonUtils.getJsonObject(jsonobject, "display");
                itemcameratransforms = (ItemCameraTransforms)p_deserialize_3_.deserialize(jsonobject1, ItemCameraTransforms.class);
            }

            List<ItemOverride> list1 = this.getItemOverrides(p_deserialize_3_, jsonobject);
            ResourceLocation resourcelocation = s.isEmpty() ? null : new ResourceLocation(s);
            return new ModelBlock(resourcelocation, list, map, flag, true, itemcameratransforms, list1);
        }

        protected List<ItemOverride> getItemOverrides(JsonDeserializationContext deserializationContext, JsonObject object)
        {
            List<ItemOverride> list = Lists.<ItemOverride>newArrayList();

            if (object.has("overrides"))
            {
                for (JsonElement jsonelement : JsonUtils.getJsonArray(object, "overrides"))
                {
                    list.add((ItemOverride)deserializationContext.deserialize(jsonelement, ItemOverride.class));
                }
            }

            return list;
        }

        private Map<String, String> getTextures(JsonObject object)
        {
            Map<String, String> map = Maps.<String, String>newHashMap();

            if (object.has("textures"))
            {
                JsonObject jsonobject = object.getAsJsonObject("textures");

                for (Entry<String, JsonElement> entry : jsonobject.entrySet())
                {
                    map.put(entry.getKey(), ((JsonElement)entry.getValue()).getAsString());
                }
            }

            return map;
        }

        private String getParent(JsonObject object)
        {
            return JsonUtils.getString(object, "parent", "");
        }

        protected boolean getAmbientOcclusionEnabled(JsonObject object)
        {
            return JsonUtils.getBoolean(object, "ambientocclusion", true);
        }

        protected List<BlockPart> getModelElements(JsonDeserializationContext deserializationContext, JsonObject object)
        {
            List<BlockPart> list = Lists.<BlockPart>newArrayList();

            if (object.has("elements"))
            {
                for (JsonElement jsonelement : JsonUtils.getJsonArray(object, "elements"))
                {
                    list.add((BlockPart)deserializationContext.deserialize(jsonelement, BlockPart.class));
                }
            }

            return list;
        }
    }

    public static class LoopException extends RuntimeException
    {
    }
}
