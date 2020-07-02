package net.minecraft.block.state;

import com.google.common.collect.ImmutableMap;
import java.util.Collection;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;

public interface IBlockState extends IBlockBehaviors, IBlockProperties
{
    Collection < IProperty<? >> func_177227_a();

    <T extends Comparable<T>> T get(IProperty<T> property);

    <T extends Comparable<T>, V extends T> IBlockState func_177226_a(IProperty<T> p_177226_1_, V p_177226_2_);

    <T extends Comparable<T>> IBlockState cycle(IProperty<T> property);

    ImmutableMap < IProperty<?>, Comparable<? >> func_177228_b();

    Block getBlock();
}
