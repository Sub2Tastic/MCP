package net.minecraft.item;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemRecord extends Item
{
    private static final Map<SoundEvent, ItemRecord> RECORDS = Maps.<SoundEvent, ItemRecord>newHashMap();
    private final SoundEvent sound;
    private final String field_185077_c;

    protected ItemRecord(String p_i46742_1_, SoundEvent p_i46742_2_)
    {
        this.field_185077_c = "item.record." + p_i46742_1_ + ".desc";
        this.sound = p_i46742_2_;
        this.maxStackSize = 1;
        this.func_77637_a(CreativeTabs.MISC);
        RECORDS.put(this.sound, this);
    }

    public EnumActionResult func_180614_a(EntityPlayer p_180614_1_, World p_180614_2_, BlockPos p_180614_3_, EnumHand p_180614_4_, EnumFacing p_180614_5_, float p_180614_6_, float p_180614_7_, float p_180614_8_)
    {
        IBlockState iblockstate = p_180614_2_.getBlockState(p_180614_3_);

        if (iblockstate.getBlock() == Blocks.JUKEBOX && !((Boolean)iblockstate.get(BlockJukebox.HAS_RECORD)).booleanValue())
        {
            if (!p_180614_2_.isRemote)
            {
                ItemStack itemstack = p_180614_1_.getHeldItem(p_180614_4_);
                ((BlockJukebox)Blocks.JUKEBOX).insertRecord(p_180614_2_, p_180614_3_, iblockstate, itemstack);
                p_180614_2_.func_180498_a((EntityPlayer)null, 1010, p_180614_3_, Item.getIdFromItem(this));
                itemstack.shrink(1);
                p_180614_1_.addStat(StatList.PLAY_RECORD);
            }

            return EnumActionResult.SUCCESS;
        }
        else
        {
            return EnumActionResult.PASS;
        }
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(this.func_150927_i());
    }

    public String func_150927_i()
    {
        return I18n.func_74838_a(this.field_185077_c);
    }

    /**
     * Return an item rarity from EnumRarity
     */
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.RARE;
    }

    @Nullable
    public static ItemRecord getBySound(SoundEvent soundIn)
    {
        return RECORDS.get(soundIn);
    }

    public SoundEvent getSound()
    {
        return this.sound;
    }
}
