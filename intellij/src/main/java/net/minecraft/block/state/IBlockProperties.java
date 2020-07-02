package net.minecraft.block.state;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface IBlockProperties
{
    Material getMaterial();

    boolean func_185913_b();

    boolean func_189884_a(Entity p_189884_1_);

    int func_185891_c();

    int getLightValue();

    boolean func_185895_e();

    boolean func_185916_f();

    MapColor getMaterialColor(IBlockAccess worldIn, BlockPos pos);

    /**
     * Returns the blockstate with the given rotation. If inapplicable, returns itself.
     */
    IBlockState rotate(Rotation rot);

    /**
     * Returns the blockstate mirrored in the given way. If inapplicable, returns itself.
     */
    IBlockState mirror(Mirror mirrorIn);

    boolean func_185917_h();

    boolean func_191057_i();

    EnumBlockRenderType getRenderType();

    int func_185889_a(IBlockAccess p_185889_1_, BlockPos p_185889_2_);

    float func_185892_j();

    boolean func_185898_k();

    boolean func_185915_l();

    boolean canProvidePower();

    int getWeakPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side);

    boolean hasComparatorInputOverride();

    int getComparatorInputOverride(World worldIn, BlockPos pos);

    float getBlockHardness(World worldIn, BlockPos pos);

    float getPlayerRelativeBlockHardness(EntityPlayer player, World worldIn, BlockPos pos);

    int getStrongPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side);

    EnumPushReaction getPushReaction();

    IBlockState func_185899_b(IBlockAccess p_185899_1_, BlockPos p_185899_2_);

    AxisAlignedBB func_185918_c(World p_185918_1_, BlockPos p_185918_2_);

    boolean func_185894_c(IBlockAccess p_185894_1_, BlockPos p_185894_2_, EnumFacing p_185894_3_);

    boolean func_185914_p();

    @Nullable
    AxisAlignedBB func_185890_d(IBlockAccess p_185890_1_, BlockPos p_185890_2_);

    void func_185908_a(World p_185908_1_, BlockPos p_185908_2_, AxisAlignedBB p_185908_3_, List<AxisAlignedBB> p_185908_4_, Entity p_185908_5_, boolean p_185908_6_);

    AxisAlignedBB func_185900_c(IBlockAccess p_185900_1_, BlockPos p_185900_2_);

    RayTraceResult func_185910_a(World p_185910_1_, BlockPos p_185910_2_, Vec3d p_185910_3_, Vec3d p_185910_4_);

    boolean func_185896_q();

    Vec3d getOffset(IBlockAccess access, BlockPos pos);

    boolean func_191058_s();

    BlockFaceShape func_193401_d(IBlockAccess p_193401_1_, BlockPos p_193401_2_, EnumFacing p_193401_3_);
}
