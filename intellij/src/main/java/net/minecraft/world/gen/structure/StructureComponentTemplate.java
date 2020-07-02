package net.minecraft.world.gen.structure;

import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public abstract class StructureComponentTemplate extends StructureComponent
{
    private static final PlacementSettings field_186179_d = new PlacementSettings();
    protected Template template;
    protected PlacementSettings placeSettings;
    protected BlockPos templatePosition;

    public StructureComponentTemplate()
    {
        this.placeSettings = field_186179_d.setIgnoreEntities(true).func_186225_a(Blocks.AIR);
    }

    public StructureComponentTemplate(int p_i46662_1_)
    {
        super(p_i46662_1_);
        this.placeSettings = field_186179_d.setIgnoreEntities(true).func_186225_a(Blocks.AIR);
    }

    protected void setup(Template templateIn, BlockPos pos, PlacementSettings settings)
    {
        this.template = templateIn;
        this.setCoordBaseMode(EnumFacing.NORTH);
        this.templatePosition = pos;
        this.placeSettings = settings;
        this.func_186174_h();
    }

    protected void func_143012_a(NBTTagCompound p_143012_1_)
    {
        p_143012_1_.putInt("TPX", this.templatePosition.getX());
        p_143012_1_.putInt("TPY", this.templatePosition.getY());
        p_143012_1_.putInt("TPZ", this.templatePosition.getZ());
    }

    /**
     * (abstract) Helper method to read subclass data from NBT
     */
    protected void readAdditional(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
    {
        this.templatePosition = new BlockPos(tagCompound.getInt("TPX"), tagCompound.getInt("TPY"), tagCompound.getInt("TPZ"));
    }

    public boolean func_74875_a(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_)
    {
        this.placeSettings.setBoundingBox(p_74875_3_);
        this.template.addBlocksToWorld(p_74875_1_, this.templatePosition, this.placeSettings, 18);
        Map<BlockPos, String> map = this.template.func_186258_a(this.templatePosition, this.placeSettings);

        for (Entry<BlockPos, String> entry : map.entrySet())
        {
            String s = entry.getValue();
            this.handleDataMarker(s, entry.getKey(), p_74875_1_, p_74875_2_, p_74875_3_);
        }

        return true;
    }

    protected abstract void handleDataMarker(String function, BlockPos pos, World worldIn, Random rand, StructureBoundingBox sbb);

    private void func_186174_h()
    {
        Rotation rotation = this.placeSettings.getRotation();
        BlockPos blockpos = this.template.transformedSize(rotation);
        Mirror mirror = this.placeSettings.getMirror();
        this.boundingBox = new StructureBoundingBox(0, 0, 0, blockpos.getX(), blockpos.getY() - 1, blockpos.getZ());

        switch (rotation)
        {
            case NONE:
            default:
                break;

            case CLOCKWISE_90:
                this.boundingBox.offset(-blockpos.getX(), 0, 0);
                break;

            case COUNTERCLOCKWISE_90:
                this.boundingBox.offset(0, 0, -blockpos.getZ());
                break;

            case CLOCKWISE_180:
                this.boundingBox.offset(-blockpos.getX(), 0, -blockpos.getZ());
        }

        switch (mirror)
        {
            case NONE:
            default:
                break;

            case FRONT_BACK:
                BlockPos blockpos2 = BlockPos.ZERO;

                if (rotation != Rotation.CLOCKWISE_90 && rotation != Rotation.COUNTERCLOCKWISE_90)
                {
                    if (rotation == Rotation.CLOCKWISE_180)
                    {
                        blockpos2 = blockpos2.offset(EnumFacing.EAST, blockpos.getX());
                    }
                    else
                    {
                        blockpos2 = blockpos2.offset(EnumFacing.WEST, blockpos.getX());
                    }
                }
                else
                {
                    blockpos2 = blockpos2.offset(rotation.rotate(EnumFacing.WEST), blockpos.getZ());
                }

                this.boundingBox.offset(blockpos2.getX(), 0, blockpos2.getZ());
                break;

            case LEFT_RIGHT:
                BlockPos blockpos1 = BlockPos.ZERO;

                if (rotation != Rotation.CLOCKWISE_90 && rotation != Rotation.COUNTERCLOCKWISE_90)
                {
                    if (rotation == Rotation.CLOCKWISE_180)
                    {
                        blockpos1 = blockpos1.offset(EnumFacing.SOUTH, blockpos.getZ());
                    }
                    else
                    {
                        blockpos1 = blockpos1.offset(EnumFacing.NORTH, blockpos.getZ());
                    }
                }
                else
                {
                    blockpos1 = blockpos1.offset(rotation.rotate(EnumFacing.NORTH), blockpos.getX());
                }

                this.boundingBox.offset(blockpos1.getX(), 0, blockpos1.getZ());
        }

        this.boundingBox.offset(this.templatePosition.getX(), this.templatePosition.getY(), this.templatePosition.getZ());
    }

    public void offset(int x, int y, int z)
    {
        super.offset(x, y, z);
        this.templatePosition = this.templatePosition.add(x, y, z);
    }
}
