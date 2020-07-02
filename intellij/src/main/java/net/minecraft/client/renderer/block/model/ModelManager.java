package net.minecraft.client.renderer.block.model;

import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.registry.IRegistry;

public class ModelManager implements IResourceManagerReloadListener
{
    private IRegistry<ModelResourceLocation, IBakedModel> modelRegistry;
    private final TextureMap field_174956_b;
    private final BlockModelShapes modelProvider;
    private IBakedModel defaultModel;

    public ModelManager(TextureMap p_i46082_1_)
    {
        this.field_174956_b = p_i46082_1_;
        this.modelProvider = new BlockModelShapes(this);
    }

    public void func_110549_a(IResourceManager p_110549_1_)
    {
        ModelBakery modelbakery = new ModelBakery(p_110549_1_, this.field_174956_b, this.modelProvider);
        this.modelRegistry = modelbakery.func_177570_a();
        this.defaultModel = this.modelRegistry.getOrDefault(ModelBakery.MODEL_MISSING);
        this.modelProvider.reloadModels();
    }

    public IBakedModel getModel(ModelResourceLocation modelLocation)
    {
        if (modelLocation == null)
        {
            return this.defaultModel;
        }
        else
        {
            IBakedModel ibakedmodel = this.modelRegistry.getOrDefault(modelLocation);
            return ibakedmodel == null ? this.defaultModel : ibakedmodel;
        }
    }

    public IBakedModel getMissingModel()
    {
        return this.defaultModel;
    }

    public TextureMap func_174952_b()
    {
        return this.field_174956_b;
    }

    public BlockModelShapes getBlockModelShapes()
    {
        return this.modelProvider;
    }
}
