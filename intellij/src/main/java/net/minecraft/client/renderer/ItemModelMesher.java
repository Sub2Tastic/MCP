package net.minecraft.client.renderer;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemModelMesher
{
    private final Map<Integer, ModelResourceLocation> field_178093_a = Maps.<Integer, ModelResourceLocation>newHashMap();
    private final Map<Integer, IBakedModel> field_178091_b = Maps.<Integer, IBakedModel>newHashMap();
    private final Map<Item, ItemMeshDefinition> field_178092_c = Maps.<Item, ItemMeshDefinition>newHashMap();
    private final ModelManager modelManager;

    public ItemModelMesher(ModelManager modelManager)
    {
        this.modelManager = modelManager;
    }

    public TextureAtlasSprite func_178082_a(Item p_178082_1_)
    {
        return this.func_178087_a(p_178082_1_, 0);
    }

    public TextureAtlasSprite func_178087_a(Item p_178087_1_, int p_178087_2_)
    {
        return this.getItemModel(new ItemStack(p_178087_1_, 1, p_178087_2_)).getParticleTexture();
    }

    public IBakedModel getItemModel(ItemStack stack)
    {
        Item item = stack.getItem();
        IBakedModel ibakedmodel = this.func_178088_b(item, this.func_178084_b(stack));

        if (ibakedmodel == null)
        {
            ItemMeshDefinition itemmeshdefinition = this.field_178092_c.get(item);

            if (itemmeshdefinition != null)
            {
                ibakedmodel = this.modelManager.getModel(itemmeshdefinition.func_178113_a(stack));
            }
        }

        if (ibakedmodel == null)
        {
            ibakedmodel = this.modelManager.getMissingModel();
        }

        return ibakedmodel;
    }

    protected int func_178084_b(ItemStack p_178084_1_)
    {
        return p_178084_1_.getMaxDamage() > 0 ? 0 : p_178084_1_.func_77960_j();
    }

    @Nullable
    protected IBakedModel func_178088_b(Item p_178088_1_, int p_178088_2_)
    {
        return this.field_178091_b.get(Integer.valueOf(this.func_178081_c(p_178088_1_, p_178088_2_)));
    }

    private int func_178081_c(Item p_178081_1_, int p_178081_2_)
    {
        return Item.getIdFromItem(p_178081_1_) << 16 | p_178081_2_;
    }

    public void func_178086_a(Item p_178086_1_, int p_178086_2_, ModelResourceLocation p_178086_3_)
    {
        this.field_178093_a.put(Integer.valueOf(this.func_178081_c(p_178086_1_, p_178086_2_)), p_178086_3_);
        this.field_178091_b.put(Integer.valueOf(this.func_178081_c(p_178086_1_, p_178086_2_)), this.modelManager.getModel(p_178086_3_));
    }

    public void func_178080_a(Item p_178080_1_, ItemMeshDefinition p_178080_2_)
    {
        this.field_178092_c.put(p_178080_1_, p_178080_2_);
    }

    public ModelManager getModelManager()
    {
        return this.modelManager;
    }

    public void rebuildCache()
    {
        this.field_178091_b.clear();

        for (Entry<Integer, ModelResourceLocation> entry : this.field_178093_a.entrySet())
        {
            this.field_178091_b.put(entry.getKey(), this.modelManager.getModel(entry.getValue()));
        }
    }
}
