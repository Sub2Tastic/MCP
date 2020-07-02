package net.minecraft.item;

import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemMonsterPlacer extends Item
{
    public ItemMonsterPlacer()
    {
        this.func_77637_a(CreativeTabs.MISC);
    }

    public String func_77653_i(ItemStack p_77653_1_)
    {
        String s = ("" + I18n.func_74838_a(this.getTranslationKey() + ".name")).trim();
        String s1 = EntityList.func_191302_a(func_190908_h(p_77653_1_));

        if (s1 != null)
        {
            s = s + " " + I18n.func_74838_a("entity." + s1 + ".name");
        }

        return s;
    }

    public EnumActionResult func_180614_a(EntityPlayer p_180614_1_, World p_180614_2_, BlockPos p_180614_3_, EnumHand p_180614_4_, EnumFacing p_180614_5_, float p_180614_6_, float p_180614_7_, float p_180614_8_)
    {
        ItemStack itemstack = p_180614_1_.getHeldItem(p_180614_4_);

        if (p_180614_2_.isRemote)
        {
            return EnumActionResult.SUCCESS;
        }
        else if (!p_180614_1_.canPlayerEdit(p_180614_3_.offset(p_180614_5_), p_180614_5_, itemstack))
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            IBlockState iblockstate = p_180614_2_.getBlockState(p_180614_3_);
            Block block = iblockstate.getBlock();

            if (block == Blocks.SPAWNER)
            {
                TileEntity tileentity = p_180614_2_.getTileEntity(p_180614_3_);

                if (tileentity instanceof TileEntityMobSpawner)
                {
                    MobSpawnerBaseLogic mobspawnerbaselogic = ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic();
                    mobspawnerbaselogic.func_190894_a(func_190908_h(itemstack));
                    tileentity.markDirty();
                    p_180614_2_.notifyBlockUpdate(p_180614_3_, iblockstate, iblockstate, 3);

                    if (!p_180614_1_.abilities.isCreativeMode)
                    {
                        itemstack.shrink(1);
                    }

                    return EnumActionResult.SUCCESS;
                }
            }

            BlockPos blockpos = p_180614_3_.offset(p_180614_5_);
            double d0 = this.func_190909_a(p_180614_2_, blockpos);
            Entity entity = func_77840_a(p_180614_2_, func_190908_h(itemstack), (double)blockpos.getX() + 0.5D, (double)blockpos.getY() + d0, (double)blockpos.getZ() + 0.5D);

            if (entity != null)
            {
                if (entity instanceof EntityLivingBase && itemstack.hasDisplayName())
                {
                    entity.func_96094_a(itemstack.func_82833_r());
                }

                func_185079_a(p_180614_2_, p_180614_1_, itemstack, entity);

                if (!p_180614_1_.abilities.isCreativeMode)
                {
                    itemstack.shrink(1);
                }
            }

            return EnumActionResult.SUCCESS;
        }
    }

    protected double func_190909_a(World p_190909_1_, BlockPos p_190909_2_)
    {
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(p_190909_2_)).expand(0.0D, -1.0D, 0.0D);
        List<AxisAlignedBB> list = p_190909_1_.func_184144_a((Entity)null, axisalignedbb);

        if (list.isEmpty())
        {
            return 0.0D;
        }
        else
        {
            double d0 = axisalignedbb.minY;

            for (AxisAlignedBB axisalignedbb1 : list)
            {
                d0 = Math.max(axisalignedbb1.maxY, d0);
            }

            return d0 - (double)p_190909_2_.getY();
        }
    }

    public static void func_185079_a(World p_185079_0_, @Nullable EntityPlayer p_185079_1_, ItemStack p_185079_2_, @Nullable Entity p_185079_3_)
    {
        MinecraftServer minecraftserver = p_185079_0_.getServer();

        if (minecraftserver != null && p_185079_3_ != null)
        {
            NBTTagCompound nbttagcompound = p_185079_2_.getTag();

            if (nbttagcompound != null && nbttagcompound.contains("EntityTag", 10))
            {
                if (!p_185079_0_.isRemote && p_185079_3_.ignoreItemEntityData() && (p_185079_1_ == null || !minecraftserver.getPlayerList().canSendCommands(p_185079_1_.getGameProfile())))
                {
                    return;
                }

                NBTTagCompound nbttagcompound1 = p_185079_3_.writeWithoutTypeId(new NBTTagCompound());
                UUID uuid = p_185079_3_.getUniqueID();
                nbttagcompound1.func_179237_a(nbttagcompound.getCompound("EntityTag"));
                p_185079_3_.setUniqueId(uuid);
                p_185079_3_.read(nbttagcompound1);
            }
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (worldIn.isRemote)
        {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        else
        {
            RayTraceResult raytraceresult = this.func_77621_a(worldIn, playerIn, true);

            if (raytraceresult != null && raytraceresult.field_72313_a == RayTraceResult.Type.BLOCK)
            {
                BlockPos blockpos = raytraceresult.func_178782_a();

                if (!(worldIn.getBlockState(blockpos).getBlock() instanceof BlockLiquid))
                {
                    return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
                }
                else if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, raytraceresult.field_178784_b, itemstack))
                {
                    Entity entity = func_77840_a(worldIn, func_190908_h(itemstack), (double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.5D, (double)blockpos.getZ() + 0.5D);

                    if (entity == null)
                    {
                        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
                    }
                    else
                    {
                        if (entity instanceof EntityLivingBase && itemstack.hasDisplayName())
                        {
                            entity.func_96094_a(itemstack.func_82833_r());
                        }

                        func_185079_a(worldIn, playerIn, itemstack, entity);

                        if (!playerIn.abilities.isCreativeMode)
                        {
                            itemstack.shrink(1);
                        }

                        playerIn.addStat(StatList.func_188057_b(this));
                        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
                    }
                }
                else
                {
                    return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
                }
            }
            else
            {
                return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
            }
        }
    }

    @Nullable
    public static Entity func_77840_a(World p_77840_0_, @Nullable ResourceLocation p_77840_1_, double p_77840_2_, double p_77840_4_, double p_77840_6_)
    {
        if (p_77840_1_ != null && EntityList.field_75627_a.containsKey(p_77840_1_))
        {
            Entity entity = null;

            for (int i = 0; i < 1; ++i)
            {
                entity = EntityList.func_188429_b(p_77840_1_, p_77840_0_);

                if (entity instanceof EntityLiving)
                {
                    EntityLiving entityliving = (EntityLiving)entity;
                    entity.setLocationAndAngles(p_77840_2_, p_77840_4_, p_77840_6_, MathHelper.wrapDegrees(p_77840_0_.rand.nextFloat() * 360.0F), 0.0F);
                    entityliving.rotationYawHead = entityliving.rotationYaw;
                    entityliving.renderYawOffset = entityliving.rotationYaw;
                    entityliving.func_180482_a(p_77840_0_.getDifficultyForLocation(new BlockPos(entityliving)), (IEntityLivingData)null);
                    p_77840_0_.addEntity0(entity);
                    entityliving.playAmbientSound();
                }
            }

            return entity;
        }
        else
        {
            return null;
        }
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void fillItemGroup(CreativeTabs group, NonNullList<ItemStack> items)
    {
        if (this.isInGroup(group))
        {
            for (EntityList.EntityEggInfo entitylist$entityegginfo : EntityList.field_75627_a.values())
            {
                ItemStack itemstack = new ItemStack(this, 1);
                func_185078_a(itemstack, entitylist$entityegginfo.field_75613_a);
                items.add(itemstack);
            }
        }
    }

    public static void func_185078_a(ItemStack p_185078_0_, ResourceLocation p_185078_1_)
    {
        NBTTagCompound nbttagcompound = p_185078_0_.hasTag() ? p_185078_0_.getTag() : new NBTTagCompound();
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound1.putString("id", p_185078_1_.toString());
        nbttagcompound.func_74782_a("EntityTag", nbttagcompound1);
        p_185078_0_.setTag(nbttagcompound);
    }

    @Nullable
    public static ResourceLocation func_190908_h(ItemStack p_190908_0_)
    {
        NBTTagCompound nbttagcompound = p_190908_0_.getTag();

        if (nbttagcompound == null)
        {
            return null;
        }
        else if (!nbttagcompound.contains("EntityTag", 10))
        {
            return null;
        }
        else
        {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("EntityTag");

            if (!nbttagcompound1.contains("id", 8))
            {
                return null;
            }
            else
            {
                String s = nbttagcompound1.getString("id");
                ResourceLocation resourcelocation = new ResourceLocation(s);

                if (!s.contains(":"))
                {
                    nbttagcompound1.putString("id", resourcelocation.toString());
                }

                return resourcelocation;
            }
        }
    }
}
