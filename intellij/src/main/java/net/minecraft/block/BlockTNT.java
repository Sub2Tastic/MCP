package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockTNT extends Block
{
    public static final PropertyBool field_176246_a = PropertyBool.create("explode");

    public BlockTNT()
    {
        super(Material.TNT);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(field_176246_a, Boolean.valueOf(false)));
        this.func_149647_a(CreativeTabs.REDSTONE);
    }

    public void func_176213_c(World p_176213_1_, BlockPos p_176213_2_, IBlockState p_176213_3_)
    {
        super.func_176213_c(p_176213_1_, p_176213_2_, p_176213_3_);

        if (p_176213_1_.isBlockPowered(p_176213_2_))
        {
            this.onPlayerDestroy(p_176213_1_, p_176213_2_, p_176213_3_.func_177226_a(field_176246_a, Boolean.valueOf(true)));
            p_176213_1_.func_175698_g(p_176213_2_);
        }
    }

    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
        if (p_189540_2_.isBlockPowered(p_189540_3_))
        {
            this.onPlayerDestroy(p_189540_2_, p_189540_3_, p_189540_1_.func_177226_a(field_176246_a, Boolean.valueOf(true)));
            p_189540_2_.func_175698_g(p_189540_3_);
        }
    }

    /**
     * Called when this Block is destroyed by an Explosion
     */
    public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn)
    {
        if (!worldIn.isRemote)
        {
            EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(worldIn, (double)((float)pos.getX() + 0.5F), (double)pos.getY(), (double)((float)pos.getZ() + 0.5F), explosionIn.getExplosivePlacedBy());
            entitytntprimed.setFuse((short)(worldIn.rand.nextInt(entitytntprimed.getFuse() / 4) + entitytntprimed.getFuse() / 8));
            worldIn.addEntity0(entitytntprimed);
        }
    }

    /**
     * Called after a player destroys this Block - the posiiton pos may no longer hold the state indicated.
     */
    public void onPlayerDestroy(World worldIn, BlockPos pos, IBlockState state)
    {
        this.func_180692_a(worldIn, pos, state, (EntityLivingBase)null);
    }

    public void func_180692_a(World p_180692_1_, BlockPos p_180692_2_, IBlockState p_180692_3_, EntityLivingBase p_180692_4_)
    {
        if (!p_180692_1_.isRemote)
        {
            if (((Boolean)p_180692_3_.get(field_176246_a)).booleanValue())
            {
                EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(p_180692_1_, (double)((float)p_180692_2_.getX() + 0.5F), (double)p_180692_2_.getY(), (double)((float)p_180692_2_.getZ() + 0.5F), p_180692_4_);
                p_180692_1_.addEntity0(entitytntprimed);
                p_180692_1_.playSound((EntityPlayer)null, entitytntprimed.posX, entitytntprimed.posY, entitytntprimed.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    public boolean func_180639_a(World p_180639_1_, BlockPos p_180639_2_, IBlockState p_180639_3_, EntityPlayer p_180639_4_, EnumHand p_180639_5_, EnumFacing p_180639_6_, float p_180639_7_, float p_180639_8_, float p_180639_9_)
    {
        ItemStack itemstack = p_180639_4_.getHeldItem(p_180639_5_);

        if (!itemstack.isEmpty() && (itemstack.getItem() == Items.FLINT_AND_STEEL || itemstack.getItem() == Items.FIRE_CHARGE))
        {
            this.func_180692_a(p_180639_1_, p_180639_2_, p_180639_3_.func_177226_a(field_176246_a, Boolean.valueOf(true)), p_180639_4_);
            p_180639_1_.setBlockState(p_180639_2_, Blocks.AIR.getDefaultState(), 11);

            if (itemstack.getItem() == Items.FLINT_AND_STEEL)
            {
                itemstack.func_77972_a(1, p_180639_4_);
            }
            else if (!p_180639_4_.abilities.isCreativeMode)
            {
                itemstack.shrink(1);
            }

            return true;
        }
        else
        {
            return super.func_180639_a(p_180639_1_, p_180639_2_, p_180639_3_, p_180639_4_, p_180639_5_, p_180639_6_, p_180639_7_, p_180639_8_, p_180639_9_);
        }
    }

    public void func_180634_a(World p_180634_1_, BlockPos p_180634_2_, IBlockState p_180634_3_, Entity p_180634_4_)
    {
        if (!p_180634_1_.isRemote && p_180634_4_ instanceof EntityArrow)
        {
            EntityArrow entityarrow = (EntityArrow)p_180634_4_;

            if (entityarrow.isBurning())
            {
                this.func_180692_a(p_180634_1_, p_180634_2_, p_180634_1_.getBlockState(p_180634_2_).func_177226_a(field_176246_a, Boolean.valueOf(true)), entityarrow.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase)entityarrow.shootingEntity : null);
                p_180634_1_.func_175698_g(p_180634_2_);
            }
        }
    }

    /**
     * Return whether this block can drop from an explosion.
     */
    public boolean canDropFromExplosion(Explosion explosionIn)
    {
        return false;
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(field_176246_a, Boolean.valueOf((p_176203_1_ & 1) > 0));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return ((Boolean)p_176201_1_.get(field_176246_a)).booleanValue() ? 1 : 0;
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {field_176246_a});
    }
}
