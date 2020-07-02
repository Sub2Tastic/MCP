package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityMooshroom extends EntityCow
{
    public EntityMooshroom(World p_i1687_1_)
    {
        super(p_i1687_1_);
        this.func_70105_a(0.9F, 1.4F);
        this.field_175506_bl = Blocks.MYCELIUM;
    }

    public static void func_189791_c(DataFixer p_189791_0_)
    {
        EntityLiving.func_189752_a(p_189791_0_, EntityMooshroom.class);
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (itemstack.getItem() == Items.BOWL && this.getGrowingAge() >= 0 && !player.abilities.isCreativeMode)
        {
            itemstack.shrink(1);

            if (itemstack.isEmpty())
            {
                player.setHeldItem(hand, new ItemStack(Items.MUSHROOM_STEW));
            }
            else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.MUSHROOM_STEW)))
            {
                player.dropItem(new ItemStack(Items.MUSHROOM_STEW), false);
            }

            return true;
        }
        else if (itemstack.getItem() == Items.SHEARS && this.getGrowingAge() >= 0)
        {
            this.remove();
            this.world.func_175688_a(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY + (double)(this.field_70131_O / 2.0F), this.posZ, 0.0D, 0.0D, 0.0D);

            if (!this.world.isRemote)
            {
                EntityCow entitycow = new EntityCow(this.world);
                entitycow.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
                entitycow.setHealth(this.getHealth());
                entitycow.renderYawOffset = this.renderYawOffset;

                if (this.hasCustomName())
                {
                    entitycow.func_96094_a(this.func_95999_t());
                }

                this.world.addEntity0(entitycow);

                for (int i = 0; i < 5; ++i)
                {
                    this.world.addEntity0(new EntityItem(this.world, this.posX, this.posY + (double)this.field_70131_O, this.posZ, new ItemStack(Blocks.RED_MUSHROOM)));
                }

                itemstack.func_77972_a(1, player);
                this.playSound(SoundEvents.ENTITY_MOOSHROOM_SHEAR, 1.0F, 1.0F);
            }

            return true;
        }
        else
        {
            return super.processInteract(player, hand);
        }
    }

    public EntityMooshroom createChild(EntityAgeable ageable)
    {
        return new EntityMooshroom(this.world);
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableList.field_186400_H;
    }
}
