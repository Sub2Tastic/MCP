package net.minecraft.tileentity;

import net.minecraft.block.BlockDaylightDetector;
import net.minecraft.util.ITickable;

public class TileEntityDaylightDetector extends TileEntity implements ITickable
{
    public void tick()
    {
        if (this.world != null && !this.world.isRemote && this.world.getGameTime() % 20L == 0L)
        {
            this.field_145854_h = this.func_145838_q();

            if (this.field_145854_h instanceof BlockDaylightDetector)
            {
                ((BlockDaylightDetector)this.field_145854_h).func_180677_d(this.world, this.pos);
            }
        }
    }
}
