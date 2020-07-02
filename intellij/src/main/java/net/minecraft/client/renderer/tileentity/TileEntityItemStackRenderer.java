package net.minecraft.client.renderer.tileentity;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelShield;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.StringUtils;

public class TileEntityItemStackRenderer
{
    private static final TileEntityShulkerBox[] SHULKER_BOXES = new TileEntityShulkerBox[16];
    public static TileEntityItemStackRenderer instance;
    private final TileEntityChest chestBasic = new TileEntityChest(BlockChest.Type.BASIC);
    private final TileEntityChest chestTrap = new TileEntityChest(BlockChest.Type.TRAP);
    private final TileEntityEnderChest enderChest = new TileEntityEnderChest();
    private final TileEntityBanner banner = new TileEntityBanner();
    private final TileEntityBed bed = new TileEntityBed();
    private final TileEntitySkull field_179023_f = new TileEntitySkull();
    private final ModelShield modelShield = new ModelShield();

    public void func_179022_a(ItemStack p_179022_1_)
    {
        this.func_192838_a(p_179022_1_, 1.0F);
    }

    public void func_192838_a(ItemStack p_192838_1_, float p_192838_2_)
    {
        Item item = p_192838_1_.getItem();

        if (item == Items.field_179564_cE)
        {
            this.banner.func_175112_a(p_192838_1_, false);
            TileEntityRendererDispatcher.instance.func_192855_a(this.banner, 0.0D, 0.0D, 0.0D, 0.0F, p_192838_2_);
        }
        else if (item == Items.field_151104_aV)
        {
            this.bed.func_193051_a(p_192838_1_);
            TileEntityRendererDispatcher.instance.func_147549_a(this.bed, 0.0D, 0.0D, 0.0D, 0.0F);
        }
        else if (item == Items.SHIELD)
        {
            if (p_192838_1_.getChildTag("BlockEntityTag") != null)
            {
                this.banner.func_175112_a(p_192838_1_, true);
                Minecraft.getInstance().getTextureManager().bindTexture(BannerTextures.field_187485_b.func_187478_a(this.banner.func_175116_e(), this.banner.getPatternList(), this.banner.getColorList()));
            }
            else
            {
                Minecraft.getInstance().getTextureManager().bindTexture(BannerTextures.field_187486_c);
            }

            GlStateManager.func_179094_E();
            GlStateManager.func_179152_a(1.0F, -1.0F, -1.0F);
            this.modelShield.func_187062_a();
            GlStateManager.func_179121_F();
        }
        else if (item == Items.field_151144_bL)
        {
            GameProfile gameprofile = null;

            if (p_192838_1_.hasTag())
            {
                NBTTagCompound nbttagcompound = p_192838_1_.getTag();

                if (nbttagcompound.contains("SkullOwner", 10))
                {
                    gameprofile = NBTUtil.readGameProfile(nbttagcompound.getCompound("SkullOwner"));
                }
                else if (nbttagcompound.contains("SkullOwner", 8) && !StringUtils.isBlank(nbttagcompound.getString("SkullOwner")))
                {
                    GameProfile gameprofile1 = new GameProfile((UUID)null, nbttagcompound.getString("SkullOwner"));
                    gameprofile = TileEntitySkull.updateGameProfile(gameprofile1);
                    nbttagcompound.remove("SkullOwner");
                    nbttagcompound.func_74782_a("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
                }
            }

            if (TileEntitySkullRenderer.field_147536_b != null)
            {
                GlStateManager.func_179094_E();
                GlStateManager.func_179129_p();
                TileEntitySkullRenderer.field_147536_b.func_188190_a(0.0F, 0.0F, 0.0F, EnumFacing.UP, 180.0F, p_192838_1_.func_77960_j(), gameprofile, -1, 0.0F);
                GlStateManager.func_179089_o();
                GlStateManager.func_179121_F();
            }
        }
        else if (item == Item.getItemFromBlock(Blocks.ENDER_CHEST))
        {
            TileEntityRendererDispatcher.instance.func_192855_a(this.enderChest, 0.0D, 0.0D, 0.0D, 0.0F, p_192838_2_);
        }
        else if (item == Item.getItemFromBlock(Blocks.TRAPPED_CHEST))
        {
            TileEntityRendererDispatcher.instance.func_192855_a(this.chestTrap, 0.0D, 0.0D, 0.0D, 0.0F, p_192838_2_);
        }
        else if (Block.getBlockFromItem(item) instanceof BlockShulkerBox)
        {
            TileEntityRendererDispatcher.instance.func_192855_a(SHULKER_BOXES[BlockShulkerBox.getColorFromItem(item).func_176765_a()], 0.0D, 0.0D, 0.0D, 0.0F, p_192838_2_);
        }
        else
        {
            TileEntityRendererDispatcher.instance.func_192855_a(this.chestBasic, 0.0D, 0.0D, 0.0D, 0.0F, p_192838_2_);
        }
    }

    static
    {
        for (EnumDyeColor enumdyecolor : EnumDyeColor.values())
        {
            SHULKER_BOXES[enumdyecolor.func_176765_a()] = new TileEntityShulkerBox(enumdyecolor);
        }

        instance = new TileEntityItemStackRenderer();
    }
}
