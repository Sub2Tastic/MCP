package net.minecraft.item;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class ItemAxe extends ItemTool
{
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.field_150344_f, Blocks.BOOKSHELF, Blocks.field_150364_r, Blocks.field_150363_s, Blocks.CHEST, Blocks.PUMPKIN, Blocks.field_150428_aP, Blocks.MELON, Blocks.LADDER, Blocks.field_150471_bO, Blocks.field_150452_aw);
    private static final float[] field_185066_m = new float[] {6.0F, 8.0F, 8.0F, 8.0F, 6.0F};
    private static final float[] field_185067_n = new float[] { -3.2F, -3.2F, -3.1F, -3.0F, -3.0F};

    protected ItemAxe(Item.ToolMaterial p_i45327_1_)
    {
        super(p_i45327_1_, EFFECTIVE_ON);
        this.attackDamage = field_185066_m[p_i45327_1_.ordinal()];
        this.attackSpeed = field_185067_n[p_i45327_1_.ordinal()];
    }

    public float getDestroySpeed(ItemStack stack, IBlockState state)
    {
        Material material = state.getMaterial();
        return material != Material.WOOD && material != Material.PLANTS && material != Material.TALL_PLANTS ? super.getDestroySpeed(stack, state) : this.efficiency;
    }
}
