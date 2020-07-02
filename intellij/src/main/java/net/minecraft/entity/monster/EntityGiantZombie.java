package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityGiantZombie extends EntityMob
{
    public EntityGiantZombie(World p_i1736_1_)
    {
        super(p_i1736_1_);
        this.func_70105_a(this.field_70130_N * 6.0F, this.field_70131_O * 6.0F);
    }

    public static void func_189765_b(DataFixer p_189765_0_)
    {
        EntityLiving.func_189752_a(p_189765_0_, EntityGiantZombie.class);
    }

    public float getEyeHeight()
    {
        return 10.440001F;
    }

    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(50.0D);
    }

    public float getBlockPathWeight(BlockPos pos)
    {
        return this.world.func_175724_o(pos) - 0.5F;
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableList.field_186437_s;
    }
}
