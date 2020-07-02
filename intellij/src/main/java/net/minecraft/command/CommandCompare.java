package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class CommandCompare extends CommandBase
{
    public String func_71517_b()
    {
        return "testforblocks";
    }

    public int func_82362_a()
    {
        return 2;
    }

    public String func_71518_a(ICommandSender p_71518_1_)
    {
        return "commands.compare.usage";
    }

    public void func_184881_a(MinecraftServer p_184881_1_, ICommandSender p_184881_2_, String[] p_184881_3_) throws CommandException
    {
        if (p_184881_3_.length < 9)
        {
            throw new WrongUsageException("commands.compare.usage", new Object[0]);
        }
        else
        {
            p_184881_2_.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
            BlockPos blockpos = func_175757_a(p_184881_2_, p_184881_3_, 0, false);
            BlockPos blockpos1 = func_175757_a(p_184881_2_, p_184881_3_, 3, false);
            BlockPos blockpos2 = func_175757_a(p_184881_2_, p_184881_3_, 6, false);
            StructureBoundingBox structureboundingbox = new StructureBoundingBox(blockpos, blockpos1);
            StructureBoundingBox structureboundingbox1 = new StructureBoundingBox(blockpos2, blockpos2.add(structureboundingbox.getLength()));
            int i = structureboundingbox.getXSize() * structureboundingbox.getYSize() * structureboundingbox.getZSize();

            if (i > 524288)
            {
                throw new CommandException("commands.compare.tooManyBlocks", new Object[] {i, 524288});
            }
            else if (structureboundingbox.minY >= 0 && structureboundingbox.maxY < 256 && structureboundingbox1.minY >= 0 && structureboundingbox1.maxY < 256)
            {
                World world = p_184881_2_.getEntityWorld();

                if (world.func_175711_a(structureboundingbox) && world.func_175711_a(structureboundingbox1))
                {
                    boolean flag = false;

                    if (p_184881_3_.length > 9 && "masked".equals(p_184881_3_[9]))
                    {
                        flag = true;
                    }

                    i = 0;
                    BlockPos blockpos3 = new BlockPos(structureboundingbox1.minX - structureboundingbox.minX, structureboundingbox1.minY - structureboundingbox.minY, structureboundingbox1.minZ - structureboundingbox.minZ);
                    BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                    BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();

                    for (int j = structureboundingbox.minZ; j <= structureboundingbox.maxZ; ++j)
                    {
                        for (int k = structureboundingbox.minY; k <= structureboundingbox.maxY; ++k)
                        {
                            for (int l = structureboundingbox.minX; l <= structureboundingbox.maxX; ++l)
                            {
                                blockpos$mutableblockpos.setPos(l, k, j);
                                blockpos$mutableblockpos1.setPos(l + blockpos3.getX(), k + blockpos3.getY(), j + blockpos3.getZ());
                                boolean flag1 = false;
                                IBlockState iblockstate = world.getBlockState(blockpos$mutableblockpos);

                                if (!flag || iblockstate.getBlock() != Blocks.AIR)
                                {
                                    if (iblockstate == world.getBlockState(blockpos$mutableblockpos1))
                                    {
                                        TileEntity tileentity = world.getTileEntity(blockpos$mutableblockpos);
                                        TileEntity tileentity1 = world.getTileEntity(blockpos$mutableblockpos1);

                                        if (tileentity != null && tileentity1 != null)
                                        {
                                            NBTTagCompound nbttagcompound = tileentity.write(new NBTTagCompound());
                                            nbttagcompound.remove("x");
                                            nbttagcompound.remove("y");
                                            nbttagcompound.remove("z");
                                            NBTTagCompound nbttagcompound1 = tileentity1.write(new NBTTagCompound());
                                            nbttagcompound1.remove("x");
                                            nbttagcompound1.remove("y");
                                            nbttagcompound1.remove("z");

                                            if (!nbttagcompound.equals(nbttagcompound1))
                                            {
                                                flag1 = true;
                                            }
                                        }
                                        else if (tileentity != null)
                                        {
                                            flag1 = true;
                                        }
                                    }
                                    else
                                    {
                                        flag1 = true;
                                    }

                                    ++i;

                                    if (flag1)
                                    {
                                        throw new CommandException("commands.compare.failed", new Object[0]);
                                    }
                                }
                            }
                        }
                    }

                    p_184881_2_.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, i);
                    func_152373_a(p_184881_2_, this, "commands.compare.success", new Object[] {i});
                }
                else
                {
                    throw new CommandException("commands.compare.outOfWorld", new Object[0]);
                }
            }
            else
            {
                throw new CommandException("commands.compare.outOfWorld", new Object[0]);
            }
        }
    }

    public List<String> func_184883_a(MinecraftServer p_184883_1_, ICommandSender p_184883_2_, String[] p_184883_3_, @Nullable BlockPos p_184883_4_)
    {
        if (p_184883_3_.length > 0 && p_184883_3_.length <= 3)
        {
            return func_175771_a(p_184883_3_, 0, p_184883_4_);
        }
        else if (p_184883_3_.length > 3 && p_184883_3_.length <= 6)
        {
            return func_175771_a(p_184883_3_, 3, p_184883_4_);
        }
        else if (p_184883_3_.length > 6 && p_184883_3_.length <= 9)
        {
            return func_175771_a(p_184883_3_, 6, p_184883_4_);
        }
        else
        {
            return p_184883_3_.length == 10 ? func_71530_a(p_184883_3_, new String[] {"masked", "all"}) : Collections.emptyList();
        }
    }
}
