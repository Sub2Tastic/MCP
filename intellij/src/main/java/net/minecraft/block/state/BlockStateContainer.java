package net.minecraft.block.state;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MapPopulator;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Cartesian;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStateContainer
{
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-z0-9_]+$");
    private static final Function < IProperty<?>, String > field_177626_b = new Function < IProperty<?>, String > ()
    {
        @Nullable
        public String apply(@Nullable IProperty<?> p_apply_1_)
        {
            return p_apply_1_ == null ? "<NULL>" : p_apply_1_.getName();
        }
    };
    private final Block owner;
    private final ImmutableSortedMap < String, IProperty<? >> properties;
    private final ImmutableList<IBlockState> validStates;

    public BlockStateContainer(Block p_i45663_1_, IProperty<?>... p_i45663_2_)
    {
        this.owner = p_i45663_1_;
        Map < String, IProperty<? >> map = Maps. < String, IProperty<? >> newHashMap();

        for (IProperty<?> iproperty : p_i45663_2_)
        {
            func_185919_a(p_i45663_1_, iproperty);
            map.put(iproperty.getName(), iproperty);
        }

        this.properties = ImmutableSortedMap.copyOf(map);
        Map < Map < IProperty<?>, Comparable<? >> , BlockStateContainer.StateImplementation > map2 = Maps. < Map < IProperty<?>, Comparable<? >> , BlockStateContainer.StateImplementation > newLinkedHashMap();
        List<BlockStateContainer.StateImplementation> list1 = Lists.<BlockStateContainer.StateImplementation>newArrayList();

        for (List < Comparable<? >> list : Cartesian.func_179321_a(this.func_177620_e()))
        {
            Map < IProperty<?>, Comparable<? >> map1 = MapPopulator. < IProperty<?>, Comparable<? >> createMap(this.properties.values(), list);
            BlockStateContainer.StateImplementation blockstatecontainer$stateimplementation = new BlockStateContainer.StateImplementation(p_i45663_1_, ImmutableMap.copyOf(map1));
            map2.put(map1, blockstatecontainer$stateimplementation);
            list1.add(blockstatecontainer$stateimplementation);
        }

        for (BlockStateContainer.StateImplementation blockstatecontainer$stateimplementation1 : list1)
        {
            blockstatecontainer$stateimplementation1.func_177235_a(map2);
        }

        this.validStates = ImmutableList.<IBlockState>copyOf(list1);
    }

    public static <T extends Comparable<T>> String func_185919_a(Block p_185919_0_, IProperty<T> p_185919_1_)
    {
        String s = p_185919_1_.getName();

        if (!NAME_PATTERN.matcher(s).matches())
        {
            throw new IllegalArgumentException("Block: " + p_185919_0_.getClass() + " has invalidly named property: " + s);
        }
        else
        {
            for (T t : p_185919_1_.getAllowedValues())
            {
                String s1 = p_185919_1_.getName(t);

                if (!NAME_PATTERN.matcher(s1).matches())
                {
                    throw new IllegalArgumentException("Block: " + p_185919_0_.getClass() + " has property: " + s + " with invalidly named value: " + s1);
                }
            }

            return s;
        }
    }

    public ImmutableList<IBlockState> getValidStates()
    {
        return this.validStates;
    }

    private List < Iterable < Comparable<? >>> func_177620_e()
    {
        List < Iterable < Comparable<? >>> list = Lists. < Iterable < Comparable<? >>> newArrayList();
        ImmutableCollection < IProperty<? >> immutablecollection = this.properties.values();
        UnmodifiableIterator unmodifiableiterator = immutablecollection.iterator();

        while (unmodifiableiterator.hasNext())
        {
            IProperty<?> iproperty = (IProperty)unmodifiableiterator.next();
            list.add(((IProperty)iproperty).getAllowedValues());
        }

        return list;
    }

    public IBlockState getBaseState()
    {
        return (IBlockState)this.validStates.get(0);
    }

    public Block getOwner()
    {
        return this.owner;
    }

    public Collection < IProperty<? >> getProperties()
    {
        return this.properties.values();
    }

    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("block", Block.field_149771_c.getKey(this.owner)).add("properties", Iterables.transform(this.properties.values(), field_177626_b)).toString();
    }

    @Nullable
    public IProperty<?> getProperty(String propertyName)
    {
        return (IProperty)this.properties.get(propertyName);
    }

    static class StateImplementation extends BlockStateBase
    {
        private final Block field_177239_a;
        private final ImmutableMap < IProperty<?>, Comparable<? >> field_177237_b;
        private ImmutableTable < IProperty<?>, Comparable<?>, IBlockState > field_177238_c;

        private StateImplementation(Block p_i45660_1_, ImmutableMap < IProperty<?>, Comparable<? >> p_i45660_2_)
        {
            this.field_177239_a = p_i45660_1_;
            this.field_177237_b = p_i45660_2_;
        }

        public Collection < IProperty<? >> func_177227_a()
        {
            return Collections. < IProperty<? >> unmodifiableCollection(this.field_177237_b.keySet());
        }

        public <T extends Comparable<T>> T get(IProperty<T> property)
        {
            Comparable<?> comparable = (Comparable)this.field_177237_b.get(property);

            if (comparable == null)
            {
                throw new IllegalArgumentException("Cannot get property " + property + " as it does not exist in " + this.field_177239_a.getStateContainer());
            }
            else
            {
                return (T)(property.getValueClass().cast(comparable));
            }
        }

        public <T extends Comparable<T>, V extends T> IBlockState func_177226_a(IProperty<T> p_177226_1_, V p_177226_2_)
        {
            Comparable<?> comparable = (Comparable)this.field_177237_b.get(p_177226_1_);

            if (comparable == null)
            {
                throw new IllegalArgumentException("Cannot set property " + p_177226_1_ + " as it does not exist in " + this.field_177239_a.getStateContainer());
            }
            else if (comparable == p_177226_2_)
            {
                return this;
            }
            else
            {
                IBlockState iblockstate = (IBlockState)this.field_177238_c.get(p_177226_1_, p_177226_2_);

                if (iblockstate == null)
                {
                    throw new IllegalArgumentException("Cannot set property " + p_177226_1_ + " to " + p_177226_2_ + " on block " + Block.field_149771_c.getKey(this.field_177239_a) + ", it is not an allowed value");
                }
                else
                {
                    return iblockstate;
                }
            }
        }

        public ImmutableMap < IProperty<?>, Comparable<? >> func_177228_b()
        {
            return this.field_177237_b;
        }

        public Block getBlock()
        {
            return this.field_177239_a;
        }

        public boolean equals(Object p_equals_1_)
        {
            return this == p_equals_1_;
        }

        public int hashCode()
        {
            return this.field_177237_b.hashCode();
        }

        public void func_177235_a(Map < Map < IProperty<?>, Comparable<? >> , BlockStateContainer.StateImplementation > p_177235_1_)
        {
            if (this.field_177238_c != null)
            {
                throw new IllegalStateException();
            }
            else
            {
                Table < IProperty<?>, Comparable<?>, IBlockState > table = HashBasedTable. < IProperty<?>, Comparable<?>, IBlockState > create();
                UnmodifiableIterator unmodifiableiterator = this.field_177237_b.entrySet().iterator();

                while (unmodifiableiterator.hasNext())
                {
                    Entry < IProperty<?>, Comparable<? >> entry = (Entry)unmodifiableiterator.next();
                    IProperty<?> iproperty = (IProperty)entry.getKey();

                    for (Comparable<?> comparable : iproperty.getAllowedValues())
                    {
                        if (comparable != entry.getValue())
                        {
                            table.put(iproperty, comparable, p_177235_1_.get(this.func_177236_b(iproperty, comparable)));
                        }
                    }
                }

                this.field_177238_c = ImmutableTable.copyOf(table);
            }
        }

        private Map < IProperty<?>, Comparable<? >> func_177236_b(IProperty<?> p_177236_1_, Comparable<?> p_177236_2_)
        {
            Map < IProperty<?>, Comparable<? >> map = Maps. < IProperty<?>, Comparable<? >> newHashMap(this.field_177237_b);
            map.put(p_177236_1_, p_177236_2_);
            return map;
        }

        public Material getMaterial()
        {
            return this.field_177239_a.getMaterial(this);
        }

        public boolean func_185913_b()
        {
            return this.field_177239_a.func_149730_j(this);
        }

        public boolean func_189884_a(Entity p_189884_1_)
        {
            return this.field_177239_a.func_189872_a(this, p_189884_1_);
        }

        public int func_185891_c()
        {
            return this.field_177239_a.func_149717_k(this);
        }

        public int getLightValue()
        {
            return this.field_177239_a.getLightValue(this);
        }

        public boolean func_185895_e()
        {
            return this.field_177239_a.func_149751_l(this);
        }

        public boolean func_185916_f()
        {
            return this.field_177239_a.func_149710_n(this);
        }

        public MapColor getMaterialColor(IBlockAccess worldIn, BlockPos pos)
        {
            return this.field_177239_a.getMaterialColor(this, worldIn, pos);
        }

        public IBlockState rotate(Rotation rot)
        {
            return this.field_177239_a.rotate(this, rot);
        }

        public IBlockState mirror(Mirror mirrorIn)
        {
            return this.field_177239_a.mirror(this, mirrorIn);
        }

        public boolean func_185917_h()
        {
            return this.field_177239_a.func_149686_d(this);
        }

        public boolean func_191057_i()
        {
            return this.field_177239_a.func_190946_v(this);
        }

        public EnumBlockRenderType getRenderType()
        {
            return this.field_177239_a.getRenderType(this);
        }

        public int func_185889_a(IBlockAccess p_185889_1_, BlockPos p_185889_2_)
        {
            return this.field_177239_a.func_185484_c(this, p_185889_1_, p_185889_2_);
        }

        public float func_185892_j()
        {
            return this.field_177239_a.func_185485_f(this);
        }

        public boolean func_185898_k()
        {
            return this.field_177239_a.func_149637_q(this);
        }

        public boolean func_185915_l()
        {
            return this.field_177239_a.func_149721_r(this);
        }

        public boolean canProvidePower()
        {
            return this.field_177239_a.canProvidePower(this);
        }

        public int getWeakPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
        {
            return this.field_177239_a.getWeakPower(this, blockAccess, pos, side);
        }

        public boolean hasComparatorInputOverride()
        {
            return this.field_177239_a.hasComparatorInputOverride(this);
        }

        public int getComparatorInputOverride(World worldIn, BlockPos pos)
        {
            return this.field_177239_a.getComparatorInputOverride(this, worldIn, pos);
        }

        public float getBlockHardness(World worldIn, BlockPos pos)
        {
            return this.field_177239_a.getBlockHardness(this, worldIn, pos);
        }

        public float getPlayerRelativeBlockHardness(EntityPlayer player, World worldIn, BlockPos pos)
        {
            return this.field_177239_a.getPlayerRelativeBlockHardness(this, player, worldIn, pos);
        }

        public int getStrongPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
        {
            return this.field_177239_a.getStrongPower(this, blockAccess, pos, side);
        }

        public EnumPushReaction getPushReaction()
        {
            return this.field_177239_a.getPushReaction(this);
        }

        public IBlockState func_185899_b(IBlockAccess p_185899_1_, BlockPos p_185899_2_)
        {
            return this.field_177239_a.func_176221_a(this, p_185899_1_, p_185899_2_);
        }

        public AxisAlignedBB func_185918_c(World p_185918_1_, BlockPos p_185918_2_)
        {
            return this.field_177239_a.func_180640_a(this, p_185918_1_, p_185918_2_);
        }

        public boolean func_185894_c(IBlockAccess p_185894_1_, BlockPos p_185894_2_, EnumFacing p_185894_3_)
        {
            return this.field_177239_a.shouldSideBeRendered(this, p_185894_1_, p_185894_2_, p_185894_3_);
        }

        public boolean func_185914_p()
        {
            return this.field_177239_a.func_149662_c(this);
        }

        @Nullable
        public AxisAlignedBB func_185890_d(IBlockAccess p_185890_1_, BlockPos p_185890_2_)
        {
            return this.field_177239_a.func_180646_a(this, p_185890_1_, p_185890_2_);
        }

        public void func_185908_a(World p_185908_1_, BlockPos p_185908_2_, AxisAlignedBB p_185908_3_, List<AxisAlignedBB> p_185908_4_, @Nullable Entity p_185908_5_, boolean p_185908_6_)
        {
            this.field_177239_a.func_185477_a(this, p_185908_1_, p_185908_2_, p_185908_3_, p_185908_4_, p_185908_5_, p_185908_6_);
        }

        public AxisAlignedBB func_185900_c(IBlockAccess p_185900_1_, BlockPos p_185900_2_)
        {
            return this.field_177239_a.func_185496_a(this, p_185900_1_, p_185900_2_);
        }

        public RayTraceResult func_185910_a(World p_185910_1_, BlockPos p_185910_2_, Vec3d p_185910_3_, Vec3d p_185910_4_)
        {
            return this.field_177239_a.func_180636_a(this, p_185910_1_, p_185910_2_, p_185910_3_, p_185910_4_);
        }

        public boolean func_185896_q()
        {
            return this.field_177239_a.func_185481_k(this);
        }

        public Vec3d getOffset(IBlockAccess access, BlockPos pos)
        {
            return this.field_177239_a.getOffset(this, access, pos);
        }

        public boolean onBlockEventReceived(World worldIn, BlockPos pos, int id, int param)
        {
            return this.field_177239_a.eventReceived(this, worldIn, pos, id, param);
        }

        public void func_189546_a(World p_189546_1_, BlockPos p_189546_2_, Block p_189546_3_, BlockPos p_189546_4_)
        {
            this.field_177239_a.func_189540_a(this, p_189546_1_, p_189546_2_, p_189546_3_, p_189546_4_);
        }

        public boolean func_191058_s()
        {
            return this.field_177239_a.func_176214_u(this);
        }

        public BlockFaceShape func_193401_d(IBlockAccess p_193401_1_, BlockPos p_193401_2_, EnumFacing p_193401_3_)
        {
            return this.field_177239_a.func_193383_a(p_193401_1_, this, p_193401_2_, p_193401_3_);
        }
    }
}
