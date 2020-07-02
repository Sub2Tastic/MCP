package net.minecraft.entity.item;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityPainting extends EntityHanging
{
    public EntityPainting.EnumArt art;

    public EntityPainting(World p_i1599_1_)
    {
        super(p_i1599_1_);
    }

    public EntityPainting(World worldIn, BlockPos pos, EnumFacing facing)
    {
        super(worldIn, pos);
        List<EntityPainting.EnumArt> list = Lists.<EntityPainting.EnumArt>newArrayList();
        int i = 0;

        for (EntityPainting.EnumArt entitypainting$enumart : EntityPainting.EnumArt.values())
        {
            this.art = entitypainting$enumart;
            this.updateFacingWithBoundingBox(facing);

            if (this.onValidSurface())
            {
                list.add(entitypainting$enumart);
                int j = entitypainting$enumart.field_75703_B * entitypainting$enumart.field_75704_C;

                if (j > i)
                {
                    i = j;
                }
            }
        }

        if (!list.isEmpty())
        {
            Iterator<EntityPainting.EnumArt> iterator = list.iterator();

            while (iterator.hasNext())
            {
                EntityPainting.EnumArt entitypainting$enumart1 = iterator.next();

                if (entitypainting$enumart1.field_75703_B * entitypainting$enumart1.field_75704_C < i)
                {
                    iterator.remove();
                }
            }

            this.art = list.get(this.rand.nextInt(list.size()));
        }

        this.updateFacingWithBoundingBox(facing);
    }

    public EntityPainting(World p_i45850_1_, BlockPos p_i45850_2_, EnumFacing p_i45850_3_, String p_i45850_4_)
    {
        this(p_i45850_1_, p_i45850_2_, p_i45850_3_);

        for (EntityPainting.EnumArt entitypainting$enumart : EntityPainting.EnumArt.values())
        {
            if (entitypainting$enumart.field_75702_A.equals(p_i45850_4_))
            {
                this.art = entitypainting$enumart;
                break;
            }
        }

        this.updateFacingWithBoundingBox(p_i45850_3_);
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
        p_70014_1_.putString("Motive", this.art.field_75702_A);
        super.func_70014_b(p_70014_1_);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
        String s = compound.getString("Motive");

        for (EntityPainting.EnumArt entitypainting$enumart : EntityPainting.EnumArt.values())
        {
            if (entitypainting$enumart.field_75702_A.equals(s))
            {
                this.art = entitypainting$enumart;
            }
        }

        if (this.art == null)
        {
            this.art = EntityPainting.EnumArt.KEBAB;
        }

        super.readAdditional(compound);
    }

    public int getWidthPixels()
    {
        return this.art.field_75703_B;
    }

    public int getHeightPixels()
    {
        return this.art.field_75704_C;
    }

    /**
     * Called when this entity is broken. Entity parameter may be null.
     */
    public void onBroken(@Nullable Entity brokenEntity)
    {
        if (this.world.getGameRules().func_82766_b("doEntityDrops"))
        {
            this.playSound(SoundEvents.ENTITY_PAINTING_BREAK, 1.0F, 1.0F);

            if (brokenEntity instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)brokenEntity;

                if (entityplayer.abilities.isCreativeMode)
                {
                    return;
                }
            }

            this.entityDropItem(new ItemStack(Items.PAINTING), 0.0F);
        }
    }

    public void playPlaceSound()
    {
        this.playSound(SoundEvents.ENTITY_PAINTING_PLACE, 1.0F, 1.0F);
    }

    /**
     * Sets the location and Yaw/Pitch of an entity in the world
     */
    public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch)
    {
        this.setPosition(x, y, z);
    }

    /**
     * Sets a target for the client to interpolate towards over the next few ticks
     */
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport)
    {
        BlockPos blockpos = this.hangingPosition.add(x - this.posX, y - this.posY, z - this.posZ);
        this.setPosition((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ());
    }

    public static enum EnumArt
    {
        KEBAB("Kebab", 16, 16, 0, 0),
        AZTEC("Aztec", 16, 16, 16, 0),
        ALBAN("Alban", 16, 16, 32, 0),
        AZTEC_2("Aztec2", 16, 16, 48, 0),
        BOMB("Bomb", 16, 16, 64, 0),
        PLANT("Plant", 16, 16, 80, 0),
        WASTELAND("Wasteland", 16, 16, 96, 0),
        POOL("Pool", 32, 16, 0, 32),
        COURBET("Courbet", 32, 16, 32, 32),
        SEA("Sea", 32, 16, 64, 32),
        SUNSET("Sunset", 32, 16, 96, 32),
        CREEBET("Creebet", 32, 16, 128, 32),
        WANDERER("Wanderer", 16, 32, 0, 64),
        GRAHAM("Graham", 16, 32, 16, 64),
        MATCH("Match", 32, 32, 0, 128),
        BUST("Bust", 32, 32, 32, 128),
        STAGE("Stage", 32, 32, 64, 128),
        VOID("Void", 32, 32, 96, 128),
        SKULL_AND_ROSES("SkullAndRoses", 32, 32, 128, 128),
        WITHER("Wither", 32, 32, 160, 128),
        FIGHTERS("Fighters", 64, 32, 0, 96),
        POINTER("Pointer", 64, 64, 0, 192),
        PIGSCENE("Pigscene", 64, 64, 64, 192),
        BURNING_SKULL("BurningSkull", 64, 64, 128, 192),
        SKELETON("Skeleton", 64, 48, 192, 64),
        DONKEY_KONG("DonkeyKong", 64, 48, 192, 112);

        public static final int field_180001_A = "SkullAndRoses".length();
        public final String field_75702_A;
        public final int field_75703_B;
        public final int field_75704_C;
        public final int field_75699_D;
        public final int field_75700_E;

        private EnumArt(String p_i1598_3_, int p_i1598_4_, int p_i1598_5_, int p_i1598_6_, int p_i1598_7_)
        {
            this.field_75702_A = p_i1598_3_;
            this.field_75703_B = p_i1598_4_;
            this.field_75704_C = p_i1598_5_;
            this.field_75699_D = p_i1598_6_;
            this.field_75700_E = p_i1598_7_;
        }
    }
}
