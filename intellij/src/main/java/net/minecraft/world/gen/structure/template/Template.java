package net.minecraft.world.gen.structure.template;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.Mirror;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.Rotation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class Template
{
    private final List<Template.BlockInfo> field_186270_a = Lists.<Template.BlockInfo>newArrayList();
    private final List<Template.EntityInfo> entities = Lists.<Template.EntityInfo>newArrayList();
    private BlockPos size = BlockPos.ZERO;
    private String author = "?";

    public BlockPos getSize()
    {
        return this.size;
    }

    public void setAuthor(String authorIn)
    {
        this.author = authorIn;
    }

    public String getAuthor()
    {
        return this.author;
    }

    /**
     * takes blocks from the world and puts the data them into this template
     */
    public void takeBlocksFromWorld(World worldIn, BlockPos startPos, BlockPos size, boolean takeEntities, @Nullable Block toIgnore)
    {
        if (size.getX() >= 1 && size.getY() >= 1 && size.getZ() >= 1)
        {
            BlockPos blockpos = startPos.add(size).add(-1, -1, -1);
            List<Template.BlockInfo> list = Lists.<Template.BlockInfo>newArrayList();
            List<Template.BlockInfo> list1 = Lists.<Template.BlockInfo>newArrayList();
            List<Template.BlockInfo> list2 = Lists.<Template.BlockInfo>newArrayList();
            BlockPos blockpos1 = new BlockPos(Math.min(startPos.getX(), blockpos.getX()), Math.min(startPos.getY(), blockpos.getY()), Math.min(startPos.getZ(), blockpos.getZ()));
            BlockPos blockpos2 = new BlockPos(Math.max(startPos.getX(), blockpos.getX()), Math.max(startPos.getY(), blockpos.getY()), Math.max(startPos.getZ(), blockpos.getZ()));
            this.size = size;

            for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.func_177975_b(blockpos1, blockpos2))
            {
                BlockPos blockpos3 = blockpos$mutableblockpos.subtract(blockpos1);
                IBlockState iblockstate = worldIn.getBlockState(blockpos$mutableblockpos);

                if (toIgnore == null || toIgnore != iblockstate.getBlock())
                {
                    TileEntity tileentity = worldIn.getTileEntity(blockpos$mutableblockpos);

                    if (tileentity != null)
                    {
                        NBTTagCompound nbttagcompound = tileentity.write(new NBTTagCompound());
                        nbttagcompound.remove("x");
                        nbttagcompound.remove("y");
                        nbttagcompound.remove("z");
                        list1.add(new Template.BlockInfo(blockpos3, iblockstate, nbttagcompound));
                    }
                    else if (!iblockstate.func_185913_b() && !iblockstate.func_185917_h())
                    {
                        list2.add(new Template.BlockInfo(blockpos3, iblockstate, (NBTTagCompound)null));
                    }
                    else
                    {
                        list.add(new Template.BlockInfo(blockpos3, iblockstate, (NBTTagCompound)null));
                    }
                }
            }

            this.field_186270_a.clear();
            this.field_186270_a.addAll(list);
            this.field_186270_a.addAll(list1);
            this.field_186270_a.addAll(list2);

            if (takeEntities)
            {
                this.takeEntitiesFromWorld(worldIn, blockpos1, blockpos2.add(1, 1, 1));
            }
            else
            {
                this.entities.clear();
            }
        }
    }

    /**
     * takes blocks from the world and puts the data them into this template
     */
    private void takeEntitiesFromWorld(World worldIn, BlockPos startPos, BlockPos endPos)
    {
        List<Entity> list = worldIn.<Entity>getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(startPos, endPos), new Predicate<Entity>()
        {
            public boolean apply(@Nullable Entity p_apply_1_)
            {
                return !(p_apply_1_ instanceof EntityPlayer);
            }
        });
        this.entities.clear();

        for (Entity entity : list)
        {
            Vec3d vec3d = new Vec3d(entity.posX - (double)startPos.getX(), entity.posY - (double)startPos.getY(), entity.posZ - (double)startPos.getZ());
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            entity.writeUnlessPassenger(nbttagcompound);
            BlockPos blockpos;

            if (entity instanceof EntityPainting)
            {
                blockpos = ((EntityPainting)entity).getHangingPosition().subtract(startPos);
            }
            else
            {
                blockpos = new BlockPos(vec3d);
            }

            this.entities.add(new Template.EntityInfo(vec3d, blockpos, nbttagcompound));
        }
    }

    public Map<BlockPos, String> func_186258_a(BlockPos p_186258_1_, PlacementSettings p_186258_2_)
    {
        Map<BlockPos, String> map = Maps.<BlockPos, String>newHashMap();
        StructureBoundingBox structureboundingbox = p_186258_2_.getBoundingBox();

        for (Template.BlockInfo template$blockinfo : this.field_186270_a)
        {
            BlockPos blockpos = transformedBlockPos(p_186258_2_, template$blockinfo.pos).add(p_186258_1_);

            if (structureboundingbox == null || structureboundingbox.isVecInside(blockpos))
            {
                IBlockState iblockstate = template$blockinfo.state;

                if (iblockstate.getBlock() == Blocks.STRUCTURE_BLOCK && template$blockinfo.nbt != null)
                {
                    TileEntityStructure.Mode tileentitystructure$mode = TileEntityStructure.Mode.valueOf(template$blockinfo.nbt.getString("mode"));

                    if (tileentitystructure$mode == TileEntityStructure.Mode.DATA)
                    {
                        map.put(blockpos, template$blockinfo.nbt.getString("metadata"));
                    }
                }
            }
        }

        return map;
    }

    public BlockPos calculateConnectedPos(PlacementSettings placementIn, BlockPos p_186262_2_, PlacementSettings p_186262_3_, BlockPos p_186262_4_)
    {
        BlockPos blockpos = transformedBlockPos(placementIn, p_186262_2_);
        BlockPos blockpos1 = transformedBlockPos(p_186262_3_, p_186262_4_);
        return blockpos.subtract(blockpos1);
    }

    public static BlockPos transformedBlockPos(PlacementSettings placementIn, BlockPos pos)
    {
        return func_186268_a(pos, placementIn.getMirror(), placementIn.getRotation());
    }

    /**
     * Add blocks and entities from this structure to the given world, restricting placement to within the chunk
     * bounding box.
     *  
     * @see PlacementSettings#setBoundingBoxFromChunk
     */
    public void addBlocksToWorldChunk(World worldIn, BlockPos pos, PlacementSettings placementIn)
    {
        placementIn.setBoundingBoxFromChunk();
        this.addBlocksToWorld(worldIn, pos, placementIn);
    }

    /**
     * This takes the data stored in this instance and puts them into the world.
     */
    public void addBlocksToWorld(World worldIn, BlockPos pos, PlacementSettings placementIn)
    {
        this.func_189960_a(worldIn, pos, new BlockRotationProcessor(pos, placementIn), placementIn, 2);
    }

    /**
     * Adds blocks and entities from this structure to the given world.
     */
    public void addBlocksToWorld(World worldIn, BlockPos pos, PlacementSettings placementIn, int flags)
    {
        this.func_189960_a(worldIn, pos, new BlockRotationProcessor(pos, placementIn), placementIn, flags);
    }

    public void func_189960_a(World p_189960_1_, BlockPos p_189960_2_, @Nullable ITemplateProcessor p_189960_3_, PlacementSettings p_189960_4_, int p_189960_5_)
    {
        if ((!this.field_186270_a.isEmpty() || !p_189960_4_.getIgnoreEntities() && !this.entities.isEmpty()) && this.size.getX() >= 1 && this.size.getY() >= 1 && this.size.getZ() >= 1)
        {
            Block block = p_189960_4_.func_186219_f();
            StructureBoundingBox structureboundingbox = p_189960_4_.getBoundingBox();

            for (Template.BlockInfo template$blockinfo : this.field_186270_a)
            {
                BlockPos blockpos = transformedBlockPos(p_189960_4_, template$blockinfo.pos).add(p_189960_2_);
                Template.BlockInfo template$blockinfo1 = p_189960_3_ != null ? p_189960_3_.func_189943_a(p_189960_1_, blockpos, template$blockinfo) : template$blockinfo;

                if (template$blockinfo1 != null)
                {
                    Block block1 = template$blockinfo1.state.getBlock();

                    if ((block == null || block != block1) && (!p_189960_4_.func_186227_h() || block1 != Blocks.STRUCTURE_BLOCK) && (structureboundingbox == null || structureboundingbox.isVecInside(blockpos)))
                    {
                        IBlockState iblockstate = template$blockinfo1.state.mirror(p_189960_4_.getMirror());
                        IBlockState iblockstate1 = iblockstate.rotate(p_189960_4_.getRotation());

                        if (template$blockinfo1.nbt != null)
                        {
                            TileEntity tileentity = p_189960_1_.getTileEntity(blockpos);

                            if (tileentity != null)
                            {
                                if (tileentity instanceof IInventory)
                                {
                                    ((IInventory)tileentity).clear();
                                }

                                p_189960_1_.setBlockState(blockpos, Blocks.BARRIER.getDefaultState(), 4);
                            }
                        }

                        if (p_189960_1_.setBlockState(blockpos, iblockstate1, p_189960_5_) && template$blockinfo1.nbt != null)
                        {
                            TileEntity tileentity2 = p_189960_1_.getTileEntity(blockpos);

                            if (tileentity2 != null)
                            {
                                template$blockinfo1.nbt.putInt("x", blockpos.getX());
                                template$blockinfo1.nbt.putInt("y", blockpos.getY());
                                template$blockinfo1.nbt.putInt("z", blockpos.getZ());
                                tileentity2.read(template$blockinfo1.nbt);
                                tileentity2.mirror(p_189960_4_.getMirror());
                                tileentity2.rotate(p_189960_4_.getRotation());
                            }
                        }
                    }
                }
            }

            for (Template.BlockInfo template$blockinfo2 : this.field_186270_a)
            {
                if (block == null || block != template$blockinfo2.state.getBlock())
                {
                    BlockPos blockpos1 = transformedBlockPos(p_189960_4_, template$blockinfo2.pos).add(p_189960_2_);

                    if (structureboundingbox == null || structureboundingbox.isVecInside(blockpos1))
                    {
                        p_189960_1_.func_175722_b(blockpos1, template$blockinfo2.state.getBlock(), false);

                        if (template$blockinfo2.nbt != null)
                        {
                            TileEntity tileentity1 = p_189960_1_.getTileEntity(blockpos1);

                            if (tileentity1 != null)
                            {
                                tileentity1.markDirty();
                            }
                        }
                    }
                }
            }

            if (!p_189960_4_.getIgnoreEntities())
            {
                this.func_186263_a(p_189960_1_, p_189960_2_, p_189960_4_.getMirror(), p_189960_4_.getRotation(), structureboundingbox);
            }
        }
    }

    private void func_186263_a(World p_186263_1_, BlockPos p_186263_2_, Mirror p_186263_3_, Rotation p_186263_4_, @Nullable StructureBoundingBox p_186263_5_)
    {
        for (Template.EntityInfo template$entityinfo : this.entities)
        {
            BlockPos blockpos = func_186268_a(template$entityinfo.blockPos, p_186263_3_, p_186263_4_).add(p_186263_2_);

            if (p_186263_5_ == null || p_186263_5_.isVecInside(blockpos))
            {
                NBTTagCompound nbttagcompound = template$entityinfo.nbt;
                Vec3d vec3d = func_186269_a(template$entityinfo.pos, p_186263_3_, p_186263_4_);
                Vec3d vec3d1 = vec3d.add((double)p_186263_2_.getX(), (double)p_186263_2_.getY(), (double)p_186263_2_.getZ());
                NBTTagList nbttaglist = new NBTTagList();
                nbttaglist.func_74742_a(new NBTTagDouble(vec3d1.x));
                nbttaglist.func_74742_a(new NBTTagDouble(vec3d1.y));
                nbttaglist.func_74742_a(new NBTTagDouble(vec3d1.z));
                nbttagcompound.func_74782_a("Pos", nbttaglist);
                nbttagcompound.putUniqueId("UUID", UUID.randomUUID());
                Entity entity;

                try
                {
                    entity = EntityList.func_75615_a(nbttagcompound, p_186263_1_);
                }
                catch (Exception var15)
                {
                    entity = null;
                }

                if (entity != null)
                {
                    float f = entity.getMirroredYaw(p_186263_3_);
                    f = f + (entity.rotationYaw - entity.getRotatedYaw(p_186263_4_));
                    entity.setLocationAndAngles(vec3d1.x, vec3d1.y, vec3d1.z, f, entity.rotationPitch);
                    p_186263_1_.addEntity0(entity);
                }
            }
        }
    }

    public BlockPos transformedSize(Rotation rotationIn)
    {
        switch (rotationIn)
        {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                return new BlockPos(this.size.getZ(), this.size.getY(), this.size.getX());

            default:
                return this.size;
        }
    }

    private static BlockPos func_186268_a(BlockPos p_186268_0_, Mirror p_186268_1_, Rotation p_186268_2_)
    {
        int i = p_186268_0_.getX();
        int j = p_186268_0_.getY();
        int k = p_186268_0_.getZ();
        boolean flag = true;

        switch (p_186268_1_)
        {
            case LEFT_RIGHT:
                k = -k;
                break;

            case FRONT_BACK:
                i = -i;
                break;

            default:
                flag = false;
        }

        switch (p_186268_2_)
        {
            case COUNTERCLOCKWISE_90:
                return new BlockPos(k, j, -i);

            case CLOCKWISE_90:
                return new BlockPos(-k, j, i);

            case CLOCKWISE_180:
                return new BlockPos(-i, j, -k);

            default:
                return flag ? new BlockPos(i, j, k) : p_186268_0_;
        }
    }

    private static Vec3d func_186269_a(Vec3d p_186269_0_, Mirror p_186269_1_, Rotation p_186269_2_)
    {
        double d0 = p_186269_0_.x;
        double d1 = p_186269_0_.y;
        double d2 = p_186269_0_.z;
        boolean flag = true;

        switch (p_186269_1_)
        {
            case LEFT_RIGHT:
                d2 = 1.0D - d2;
                break;

            case FRONT_BACK:
                d0 = 1.0D - d0;
                break;

            default:
                flag = false;
        }

        switch (p_186269_2_)
        {
            case COUNTERCLOCKWISE_90:
                return new Vec3d(d2, d1, 1.0D - d0);

            case CLOCKWISE_90:
                return new Vec3d(1.0D - d2, d1, d0);

            case CLOCKWISE_180:
                return new Vec3d(1.0D - d0, d1, 1.0D - d2);

            default:
                return flag ? new Vec3d(d0, d1, d2) : p_186269_0_;
        }
    }

    public BlockPos getZeroPositionWithTransform(BlockPos p_189961_1_, Mirror p_189961_2_, Rotation p_189961_3_)
    {
        return getZeroPositionWithTransform(p_189961_1_, p_189961_2_, p_189961_3_, this.getSize().getX(), this.getSize().getZ());
    }

    public static BlockPos getZeroPositionWithTransform(BlockPos p_191157_0_, Mirror p_191157_1_, Rotation p_191157_2_, int p_191157_3_, int p_191157_4_)
    {
        --p_191157_3_;
        --p_191157_4_;
        int i = p_191157_1_ == Mirror.FRONT_BACK ? p_191157_3_ : 0;
        int j = p_191157_1_ == Mirror.LEFT_RIGHT ? p_191157_4_ : 0;
        BlockPos blockpos = p_191157_0_;

        switch (p_191157_2_)
        {
            case COUNTERCLOCKWISE_90:
                blockpos = p_191157_0_.add(j, 0, p_191157_3_ - i);
                break;

            case CLOCKWISE_90:
                blockpos = p_191157_0_.add(p_191157_4_ - j, 0, i);
                break;

            case CLOCKWISE_180:
                blockpos = p_191157_0_.add(p_191157_3_ - i, 0, p_191157_4_ - j);
                break;

            case NONE:
                blockpos = p_191157_0_.add(i, 0, j);
        }

        return blockpos;
    }

    public static void func_191158_a(DataFixer p_191158_0_)
    {
        p_191158_0_.func_188258_a(FixTypes.STRUCTURE, new IDataWalker()
        {
            public NBTTagCompound func_188266_a(IDataFixer p_188266_1_, NBTTagCompound p_188266_2_, int p_188266_3_)
            {
                if (p_188266_2_.contains("entities", 9))
                {
                    NBTTagList nbttaglist = p_188266_2_.getList("entities", 10);

                    for (int i = 0; i < nbttaglist.func_74745_c(); ++i)
                    {
                        NBTTagCompound nbttagcompound = (NBTTagCompound)nbttaglist.func_179238_g(i);

                        if (nbttagcompound.contains("nbt", 10))
                        {
                            nbttagcompound.func_74782_a("nbt", p_188266_1_.func_188251_a(FixTypes.ENTITY, nbttagcompound.getCompound("nbt"), p_188266_3_));
                        }
                    }
                }

                if (p_188266_2_.contains("blocks", 9))
                {
                    NBTTagList nbttaglist1 = p_188266_2_.getList("blocks", 10);

                    for (int j = 0; j < nbttaglist1.func_74745_c(); ++j)
                    {
                        NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist1.func_179238_g(j);

                        if (nbttagcompound1.contains("nbt", 10))
                        {
                            nbttagcompound1.func_74782_a("nbt", p_188266_1_.func_188251_a(FixTypes.BLOCK_ENTITY, nbttagcompound1.getCompound("nbt"), p_188266_3_));
                        }
                    }
                }

                return p_188266_2_;
            }
        });
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        Template.BasicPalette template$basicpalette = new Template.BasicPalette();
        NBTTagList nbttaglist = new NBTTagList();

        for (Template.BlockInfo template$blockinfo : this.field_186270_a)
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.func_74782_a("pos", this.writeInts(template$blockinfo.pos.getX(), template$blockinfo.pos.getY(), template$blockinfo.pos.getZ()));
            nbttagcompound.putInt("state", template$basicpalette.idFor(template$blockinfo.state));

            if (template$blockinfo.nbt != null)
            {
                nbttagcompound.func_74782_a("nbt", template$blockinfo.nbt);
            }

            nbttaglist.func_74742_a(nbttagcompound);
        }

        NBTTagList nbttaglist1 = new NBTTagList();

        for (Template.EntityInfo template$entityinfo : this.entities)
        {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.func_74782_a("pos", this.writeDoubles(template$entityinfo.pos.x, template$entityinfo.pos.y, template$entityinfo.pos.z));
            nbttagcompound1.func_74782_a("blockPos", this.writeInts(template$entityinfo.blockPos.getX(), template$entityinfo.blockPos.getY(), template$entityinfo.blockPos.getZ()));

            if (template$entityinfo.nbt != null)
            {
                nbttagcompound1.func_74782_a("nbt", template$entityinfo.nbt);
            }

            nbttaglist1.func_74742_a(nbttagcompound1);
        }

        NBTTagList nbttaglist2 = new NBTTagList();

        for (IBlockState iblockstate : template$basicpalette)
        {
            nbttaglist2.func_74742_a(NBTUtil.writeBlockState(new NBTTagCompound(), iblockstate));
        }

        nbt.func_74782_a("palette", nbttaglist2);
        nbt.func_74782_a("blocks", nbttaglist);
        nbt.func_74782_a("entities", nbttaglist1);
        nbt.func_74782_a("size", this.writeInts(this.size.getX(), this.size.getY(), this.size.getZ()));
        nbt.putString("author", this.author);
        nbt.putInt("DataVersion", 1343);
        return nbt;
    }

    public void read(NBTTagCompound compound)
    {
        this.field_186270_a.clear();
        this.entities.clear();
        NBTTagList nbttaglist = compound.getList("size", 3);
        this.size = new BlockPos(nbttaglist.getInt(0), nbttaglist.getInt(1), nbttaglist.getInt(2));
        this.author = compound.getString("author");
        Template.BasicPalette template$basicpalette = new Template.BasicPalette();
        NBTTagList nbttaglist1 = compound.getList("palette", 10);

        for (int i = 0; i < nbttaglist1.func_74745_c(); ++i)
        {
            template$basicpalette.addMapping(NBTUtil.readBlockState(nbttaglist1.getCompound(i)), i);
        }

        NBTTagList nbttaglist3 = compound.getList("blocks", 10);

        for (int j = 0; j < nbttaglist3.func_74745_c(); ++j)
        {
            NBTTagCompound nbttagcompound = nbttaglist3.getCompound(j);
            NBTTagList nbttaglist2 = nbttagcompound.getList("pos", 3);
            BlockPos blockpos = new BlockPos(nbttaglist2.getInt(0), nbttaglist2.getInt(1), nbttaglist2.getInt(2));
            IBlockState iblockstate = template$basicpalette.stateFor(nbttagcompound.getInt("state"));
            NBTTagCompound nbttagcompound1;

            if (nbttagcompound.contains("nbt"))
            {
                nbttagcompound1 = nbttagcompound.getCompound("nbt");
            }
            else
            {
                nbttagcompound1 = null;
            }

            this.field_186270_a.add(new Template.BlockInfo(blockpos, iblockstate, nbttagcompound1));
        }

        NBTTagList nbttaglist4 = compound.getList("entities", 10);

        for (int k = 0; k < nbttaglist4.func_74745_c(); ++k)
        {
            NBTTagCompound nbttagcompound3 = nbttaglist4.getCompound(k);
            NBTTagList nbttaglist5 = nbttagcompound3.getList("pos", 6);
            Vec3d vec3d = new Vec3d(nbttaglist5.getDouble(0), nbttaglist5.getDouble(1), nbttaglist5.getDouble(2));
            NBTTagList nbttaglist6 = nbttagcompound3.getList("blockPos", 3);
            BlockPos blockpos1 = new BlockPos(nbttaglist6.getInt(0), nbttaglist6.getInt(1), nbttaglist6.getInt(2));

            if (nbttagcompound3.contains("nbt"))
            {
                NBTTagCompound nbttagcompound2 = nbttagcompound3.getCompound("nbt");
                this.entities.add(new Template.EntityInfo(vec3d, blockpos1, nbttagcompound2));
            }
        }
    }

    private NBTTagList writeInts(int... values)
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i : values)
        {
            nbttaglist.func_74742_a(new NBTTagInt(i));
        }

        return nbttaglist;
    }

    private NBTTagList writeDoubles(double... values)
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (double d0 : values)
        {
            nbttaglist.func_74742_a(new NBTTagDouble(d0));
        }

        return nbttaglist;
    }

    static class BasicPalette implements Iterable<IBlockState>
    {
        public static final IBlockState DEFAULT_BLOCK_STATE = Blocks.AIR.getDefaultState();
        final ObjectIntIdentityMap<IBlockState> ids;
        private int lastId;

        private BasicPalette()
        {
            this.ids = new ObjectIntIdentityMap<IBlockState>(16);
        }

        public int idFor(IBlockState state)
        {
            int i = this.ids.get(state);

            if (i == -1)
            {
                i = this.lastId++;
                this.ids.put(state, i);
            }

            return i;
        }

        @Nullable
        public IBlockState stateFor(int id)
        {
            IBlockState iblockstate = this.ids.getByValue(id);
            return iblockstate == null ? DEFAULT_BLOCK_STATE : iblockstate;
        }

        public Iterator<IBlockState> iterator()
        {
            return this.ids.iterator();
        }

        public void addMapping(IBlockState p_189956_1_, int p_189956_2_)
        {
            this.ids.put(p_189956_1_, p_189956_2_);
        }
    }

    public static class BlockInfo
    {
        public final BlockPos pos;
        public final IBlockState state;
        public final NBTTagCompound nbt;

        public BlockInfo(BlockPos pos, IBlockState state, @Nullable NBTTagCompound nbt)
        {
            this.pos = pos;
            this.state = state;
            this.nbt = nbt;
        }
    }

    public static class EntityInfo
    {
        public final Vec3d pos;
        public final BlockPos blockPos;
        public final NBTTagCompound nbt;

        public EntityInfo(Vec3d vecIn, BlockPos posIn, NBTTagCompound nbt)
        {
            this.pos = vecIn;
            this.blockPos = posIn;
            this.nbt = nbt;
        }
    }
}
