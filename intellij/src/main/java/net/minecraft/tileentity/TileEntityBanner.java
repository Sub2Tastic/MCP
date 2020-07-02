package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IWorldNameable;

public class TileEntityBanner extends TileEntity implements IWorldNameable
{
    private String name;
    private EnumDyeColor baseColor = EnumDyeColor.BLACK;

    /** A list of all the banner patterns. */
    private NBTTagList patterns;
    private boolean patternDataSet;
    private List<BannerPattern> patternList;
    private List<EnumDyeColor> colorList;
    private String patternResourceLocation;

    public void func_175112_a(ItemStack p_175112_1_, boolean p_175112_2_)
    {
        this.patterns = null;
        NBTTagCompound nbttagcompound = p_175112_1_.getChildTag("BlockEntityTag");

        if (nbttagcompound != null && nbttagcompound.contains("Patterns", 9))
        {
            this.patterns = nbttagcompound.getList("Patterns", 10).copy();
        }

        this.baseColor = p_175112_2_ ? func_190616_d(p_175112_1_) : ItemBanner.func_179225_h(p_175112_1_);
        this.patternList = null;
        this.colorList = null;
        this.patternResourceLocation = "";
        this.patternDataSet = true;
        this.name = p_175112_1_.hasDisplayName() ? p_175112_1_.func_82833_r() : null;
    }

    public String func_70005_c_()
    {
        return this.hasCustomName() ? this.name : "banner";
    }

    public boolean hasCustomName()
    {
        return this.name != null && !this.name.isEmpty();
    }

    public ITextComponent getDisplayName()
    {
        return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.func_70005_c_()) : new TextComponentTranslation(this.func_70005_c_(), new Object[0]));
    }

    public NBTTagCompound write(NBTTagCompound compound)
    {
        super.write(compound);
        compound.putInt("Base", this.baseColor.func_176767_b());

        if (this.patterns != null)
        {
            compound.func_74782_a("Patterns", this.patterns);
        }

        if (this.hasCustomName())
        {
            compound.putString("CustomName", this.name);
        }

        return compound;
    }

    public void read(NBTTagCompound compound)
    {
        super.read(compound);

        if (compound.contains("CustomName", 8))
        {
            this.name = compound.getString("CustomName");
        }

        this.baseColor = EnumDyeColor.func_176766_a(compound.getInt("Base"));
        this.patterns = compound.getList("Patterns", 10);
        this.patternList = null;
        this.colorList = null;
        this.patternResourceLocation = null;
        this.patternDataSet = true;
    }

    @Nullable

    /**
     * Retrieves packet to send to the client whenever this Tile Entity is resynced via World.notifyBlockUpdate. For
     * modded TE's, this packet comes back to you clientside in {@link #onDataPacket}
     */
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 6, this.getUpdateTag());
    }

    /**
     * Get an NBT compound to sync to the client with SPacketChunkData, used for initial loading of the chunk or when
     * many blocks change at once. This compound comes back to you clientside in {@link handleUpdateTag}
     */
    public NBTTagCompound getUpdateTag()
    {
        return this.write(new NBTTagCompound());
    }

    /**
     * Retrieves the amount of patterns stored on an ItemStack. If the tag does not exist this value will be 0.
     */
    public static int getPatterns(ItemStack stack)
    {
        NBTTagCompound nbttagcompound = stack.getChildTag("BlockEntityTag");
        return nbttagcompound != null && nbttagcompound.contains("Patterns") ? nbttagcompound.getList("Patterns", 10).func_74745_c() : 0;
    }

    public List<BannerPattern> getPatternList()
    {
        this.initializeBannerData();
        return this.patternList;
    }

    public List<EnumDyeColor> getColorList()
    {
        this.initializeBannerData();
        return this.colorList;
    }

    public String func_175116_e()
    {
        this.initializeBannerData();
        return this.patternResourceLocation;
    }

    /**
     * Establishes all of the basic properties for the banner. This will also apply the data from the tile entities nbt
     * tag compounds.
     */
    private void initializeBannerData()
    {
        if (this.patternList == null || this.colorList == null || this.patternResourceLocation == null)
        {
            if (!this.patternDataSet)
            {
                this.patternResourceLocation = "";
            }
            else
            {
                this.patternList = Lists.<BannerPattern>newArrayList();
                this.colorList = Lists.<EnumDyeColor>newArrayList();
                this.patternList.add(BannerPattern.BASE);
                this.colorList.add(this.baseColor);
                this.patternResourceLocation = "b" + this.baseColor.func_176767_b();

                if (this.patterns != null)
                {
                    for (int i = 0; i < this.patterns.func_74745_c(); ++i)
                    {
                        NBTTagCompound nbttagcompound = this.patterns.getCompound(i);
                        BannerPattern bannerpattern = BannerPattern.byHash(nbttagcompound.getString("Pattern"));

                        if (bannerpattern != null)
                        {
                            this.patternList.add(bannerpattern);
                            int j = nbttagcompound.getInt("Color");
                            this.colorList.add(EnumDyeColor.func_176766_a(j));
                            this.patternResourceLocation = this.patternResourceLocation + bannerpattern.getHashname() + j;
                        }
                    }
                }
            }
        }
    }

    /**
     * Removes all the banner related data from a provided instance of ItemStack.
     */
    public static void removeBannerData(ItemStack stack)
    {
        NBTTagCompound nbttagcompound = stack.getChildTag("BlockEntityTag");

        if (nbttagcompound != null && nbttagcompound.contains("Patterns", 9))
        {
            NBTTagList nbttaglist = nbttagcompound.getList("Patterns", 10);

            if (!nbttaglist.func_82582_d())
            {
                nbttaglist.func_74744_a(nbttaglist.func_74745_c() - 1);

                if (nbttaglist.func_82582_d())
                {
                    stack.getTag().remove("BlockEntityTag");

                    if (stack.getTag().func_82582_d())
                    {
                        stack.setTag((NBTTagCompound)null);
                    }
                }
            }
        }
    }

    public ItemStack getItem()
    {
        ItemStack itemstack = ItemBanner.func_190910_a(this.baseColor, this.patterns);

        if (this.hasCustomName())
        {
            itemstack.func_151001_c(this.func_70005_c_());
        }

        return itemstack;
    }

    public static EnumDyeColor func_190616_d(ItemStack p_190616_0_)
    {
        NBTTagCompound nbttagcompound = p_190616_0_.getChildTag("BlockEntityTag");
        return nbttagcompound != null && nbttagcompound.contains("Base") ? EnumDyeColor.func_176766_a(nbttagcompound.getInt("Base")) : EnumDyeColor.BLACK;
    }
}
