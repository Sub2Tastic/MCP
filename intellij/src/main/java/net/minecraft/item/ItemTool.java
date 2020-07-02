package net.minecraft.item;

import com.google.common.collect.Multimap;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTool extends Item
{
    private final Set<Block> effectiveBlocks;
    protected float efficiency;
    protected float attackDamage;
    protected float attackSpeed;
    protected Item.ToolMaterial field_77862_b;

    protected ItemTool(float p_i46745_1_, float p_i46745_2_, Item.ToolMaterial p_i46745_3_, Set<Block> p_i46745_4_)
    {
        this.efficiency = 4.0F;
        this.field_77862_b = p_i46745_3_;
        this.effectiveBlocks = p_i46745_4_;
        this.maxStackSize = 1;
        this.func_77656_e(p_i46745_3_.func_77997_a());
        this.efficiency = p_i46745_3_.func_77998_b();
        this.attackDamage = p_i46745_1_ + p_i46745_3_.func_78000_c();
        this.attackSpeed = p_i46745_2_;
        this.func_77637_a(CreativeTabs.TOOLS);
    }

    protected ItemTool(Item.ToolMaterial p_i46746_1_, Set<Block> p_i46746_2_)
    {
        this(0.0F, 0.0F, p_i46746_1_, p_i46746_2_);
    }

    public float getDestroySpeed(ItemStack stack, IBlockState state)
    {
        return this.effectiveBlocks.contains(state.getBlock()) ? this.efficiency : 1.0F;
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        stack.func_77972_a(2, attacker);
        return true;
    }

    /**
     * Called when a Block is destroyed using this Item. Return true to trigger the "Use Item" statistic.
     */
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
    {
        if (!worldIn.isRemote && (double)state.getBlockHardness(worldIn, pos) != 0.0D)
        {
            stack.func_77972_a(1, entityLiving);
        }

        return true;
    }

    public boolean func_77662_d()
    {
        return true;
    }

    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     */
    public int getItemEnchantability()
    {
        return this.field_77862_b.func_77995_e();
    }

    public String func_77861_e()
    {
        return this.field_77862_b.toString();
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return this.field_77862_b.func_150995_f() == repair.getItem() ? true : super.getIsRepairable(toRepair, repair);
    }

    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", (double)this.attackDamage, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", (double)this.attackSpeed, 0));
        }

        return multimap;
    }
}
