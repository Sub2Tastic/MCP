package net.minecraft.item;

import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemHoe extends Item
{
    private final float speed;
    protected Item.ToolMaterial field_77843_a;

    public ItemHoe(Item.ToolMaterial p_i45343_1_)
    {
        this.field_77843_a = p_i45343_1_;
        this.maxStackSize = 1;
        this.func_77656_e(p_i45343_1_.func_77997_a());
        this.func_77637_a(CreativeTabs.TOOLS);
        this.speed = p_i45343_1_.func_78000_c() + 1.0F;
    }

    @SuppressWarnings("incomplete-switch")
    public EnumActionResult func_180614_a(EntityPlayer p_180614_1_, World p_180614_2_, BlockPos p_180614_3_, EnumHand p_180614_4_, EnumFacing p_180614_5_, float p_180614_6_, float p_180614_7_, float p_180614_8_)
    {
        ItemStack itemstack = p_180614_1_.getHeldItem(p_180614_4_);

        if (!p_180614_1_.canPlayerEdit(p_180614_3_.offset(p_180614_5_), p_180614_5_, itemstack))
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            IBlockState iblockstate = p_180614_2_.getBlockState(p_180614_3_);
            Block block = iblockstate.getBlock();

            if (p_180614_5_ != EnumFacing.DOWN && p_180614_2_.getBlockState(p_180614_3_.up()).getMaterial() == Material.AIR)
            {
                if (block == Blocks.GRASS || block == Blocks.GRASS_PATH)
                {
                    this.func_185071_a(itemstack, p_180614_1_, p_180614_2_, p_180614_3_, Blocks.FARMLAND.getDefaultState());
                    return EnumActionResult.SUCCESS;
                }

                if (block == Blocks.DIRT)
                {
                    switch ((BlockDirt.DirtType)iblockstate.get(BlockDirt.field_176386_a))
                    {
                        case DIRT:
                            this.func_185071_a(itemstack, p_180614_1_, p_180614_2_, p_180614_3_, Blocks.FARMLAND.getDefaultState());
                            return EnumActionResult.SUCCESS;

                        case COARSE_DIRT:
                            this.func_185071_a(itemstack, p_180614_1_, p_180614_2_, p_180614_3_, Blocks.DIRT.getDefaultState().func_177226_a(BlockDirt.field_176386_a, BlockDirt.DirtType.DIRT));
                            return EnumActionResult.SUCCESS;
                    }
                }
            }

            return EnumActionResult.PASS;
        }
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        stack.func_77972_a(1, attacker);
        return true;
    }

    protected void func_185071_a(ItemStack p_185071_1_, EntityPlayer p_185071_2_, World p_185071_3_, BlockPos p_185071_4_, IBlockState p_185071_5_)
    {
        p_185071_3_.playSound(p_185071_2_, p_185071_4_, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

        if (!p_185071_3_.isRemote)
        {
            p_185071_3_.setBlockState(p_185071_4_, p_185071_5_, 11);
            p_185071_1_.func_77972_a(1, p_185071_2_);
        }
    }

    public boolean func_77662_d()
    {
        return true;
    }

    public String func_77842_f()
    {
        return this.field_77843_a.toString();
    }

    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 0.0D, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double)(this.speed - 4.0F), 0));
        }

        return multimap;
    }
}
