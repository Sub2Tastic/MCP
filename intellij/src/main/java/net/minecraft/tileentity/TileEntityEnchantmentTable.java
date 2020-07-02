package net.minecraft.tileentity;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;

public class TileEntityEnchantmentTable extends TileEntity implements ITickable, IInteractionObject
{
    public int field_145926_a;
    public float field_145933_i;
    public float field_145931_j;
    public float field_145932_k;
    public float field_145929_l;
    public float field_145930_m;
    public float field_145927_n;
    public float field_145928_o;
    public float field_145925_p;
    public float field_145924_q;
    private static final Random field_145923_r = new Random();
    private String field_145922_s;

    public NBTTagCompound write(NBTTagCompound compound)
    {
        super.write(compound);

        if (this.hasCustomName())
        {
            compound.putString("CustomName", this.field_145922_s);
        }

        return compound;
    }

    public void read(NBTTagCompound compound)
    {
        super.read(compound);

        if (compound.contains("CustomName", 8))
        {
            this.field_145922_s = compound.getString("CustomName");
        }
    }

    public void tick()
    {
        this.field_145927_n = this.field_145930_m;
        this.field_145925_p = this.field_145928_o;
        EntityPlayer entityplayer = this.world.func_184137_a((double)((float)this.pos.getX() + 0.5F), (double)((float)this.pos.getY() + 0.5F), (double)((float)this.pos.getZ() + 0.5F), 3.0D, false);

        if (entityplayer != null)
        {
            double d0 = entityplayer.posX - (double)((float)this.pos.getX() + 0.5F);
            double d1 = entityplayer.posZ - (double)((float)this.pos.getZ() + 0.5F);
            this.field_145924_q = (float)MathHelper.atan2(d1, d0);
            this.field_145930_m += 0.1F;

            if (this.field_145930_m < 0.5F || field_145923_r.nextInt(40) == 0)
            {
                float f1 = this.field_145932_k;

                while (true)
                {
                    this.field_145932_k += (float)(field_145923_r.nextInt(4) - field_145923_r.nextInt(4));

                    if (f1 != this.field_145932_k)
                    {
                        break;
                    }
                }
            }
        }
        else
        {
            this.field_145924_q += 0.02F;
            this.field_145930_m -= 0.1F;
        }

        while (this.field_145928_o >= (float)Math.PI)
        {
            this.field_145928_o -= ((float)Math.PI * 2F);
        }

        while (this.field_145928_o < -(float)Math.PI)
        {
            this.field_145928_o += ((float)Math.PI * 2F);
        }

        while (this.field_145924_q >= (float)Math.PI)
        {
            this.field_145924_q -= ((float)Math.PI * 2F);
        }

        while (this.field_145924_q < -(float)Math.PI)
        {
            this.field_145924_q += ((float)Math.PI * 2F);
        }

        float f2;

        for (f2 = this.field_145924_q - this.field_145928_o; f2 >= (float)Math.PI; f2 -= ((float)Math.PI * 2F))
        {
            ;
        }

        while (f2 < -(float)Math.PI)
        {
            f2 += ((float)Math.PI * 2F);
        }

        this.field_145928_o += f2 * 0.4F;
        this.field_145930_m = MathHelper.clamp(this.field_145930_m, 0.0F, 1.0F);
        ++this.field_145926_a;
        this.field_145931_j = this.field_145933_i;
        float f = (this.field_145932_k - this.field_145933_i) * 0.4F;
        float f3 = 0.2F;
        f = MathHelper.clamp(f, -0.2F, 0.2F);
        this.field_145929_l += (f - this.field_145929_l) * 0.9F;
        this.field_145933_i += this.field_145929_l;
    }

    public String func_70005_c_()
    {
        return this.hasCustomName() ? this.field_145922_s : "container.enchant";
    }

    public boolean hasCustomName()
    {
        return this.field_145922_s != null && !this.field_145922_s.isEmpty();
    }

    public void func_145920_a(String p_145920_1_)
    {
        this.field_145922_s = p_145920_1_;
    }

    public ITextComponent getDisplayName()
    {
        return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.func_70005_c_()) : new TextComponentTranslation(this.func_70005_c_(), new Object[0]));
    }

    public Container func_174876_a(InventoryPlayer p_174876_1_, EntityPlayer p_174876_2_)
    {
        return new ContainerEnchantment(p_174876_1_, this.world, this.pos);
    }

    public String func_174875_k()
    {
        return "minecraft:enchanting_table";
    }
}
