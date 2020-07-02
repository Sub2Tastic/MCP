package net.minecraft.client.renderer;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneSlabNew;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockWall;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class RenderItem implements IResourceManagerReloadListener
{
    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    private boolean field_175058_l = true;
    public float zLevel;
    private final ItemModelMesher itemModelMesher;
    private final TextureManager textureManager;
    private final ItemColors itemColors;

    public RenderItem(TextureManager textureManagerIn, ModelManager modelManagerIn, ItemColors itemColorsIn)
    {
        this.textureManager = textureManagerIn;
        this.itemModelMesher = new ItemModelMesher(modelManagerIn);
        this.func_175041_b();
        this.itemColors = itemColorsIn;
    }

    public ItemModelMesher getItemModelMesher()
    {
        return this.itemModelMesher;
    }

    protected void func_175048_a(Item p_175048_1_, int p_175048_2_, String p_175048_3_)
    {
        this.itemModelMesher.func_178086_a(p_175048_1_, p_175048_2_, new ModelResourceLocation(p_175048_3_, "inventory"));
    }

    protected void func_175029_a(Block p_175029_1_, int p_175029_2_, String p_175029_3_)
    {
        this.func_175048_a(Item.getItemFromBlock(p_175029_1_), p_175029_2_, p_175029_3_);
    }

    private void func_175031_a(Block p_175031_1_, String p_175031_2_)
    {
        this.func_175029_a(p_175031_1_, 0, p_175031_2_);
    }

    private void func_175047_a(Item p_175047_1_, String p_175047_2_)
    {
        this.func_175048_a(p_175047_1_, 0, p_175047_2_);
    }

    private void func_191961_a(IBakedModel p_191961_1_, ItemStack p_191961_2_)
    {
        this.func_191967_a(p_191961_1_, -1, p_191961_2_);
    }

    private void func_191965_a(IBakedModel p_191965_1_, int p_191965_2_)
    {
        this.func_191967_a(p_191965_1_, p_191965_2_, ItemStack.EMPTY);
    }

    private void func_191967_a(IBakedModel p_191967_1_, int p_191967_2_, ItemStack p_191967_3_)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.field_176599_b);

        for (EnumFacing enumfacing : EnumFacing.values())
        {
            this.func_191970_a(bufferbuilder, p_191967_1_.func_188616_a((IBlockState)null, enumfacing, 0L), p_191967_2_, p_191967_3_);
        }

        this.func_191970_a(bufferbuilder, p_191967_1_.func_188616_a((IBlockState)null, (EnumFacing)null, 0L), p_191967_2_, p_191967_3_);
        tessellator.draw();
    }

    public void func_180454_a(ItemStack p_180454_1_, IBakedModel p_180454_2_)
    {
        if (!p_180454_1_.isEmpty())
        {
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b(-0.5F, -0.5F, -0.5F);

            if (p_180454_2_.isBuiltInRenderer())
            {
                GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.func_179091_B();
                TileEntityItemStackRenderer.instance.func_179022_a(p_180454_1_);
            }
            else
            {
                this.func_191961_a(p_180454_2_, p_180454_1_);

                if (p_180454_1_.hasEffect())
                {
                    this.func_191966_a(p_180454_2_);
                }
            }

            GlStateManager.func_179121_F();
        }
    }

    private void func_191966_a(IBakedModel p_191966_1_)
    {
        GlStateManager.func_179132_a(false);
        GlStateManager.func_179143_c(514);
        GlStateManager.func_179140_f();
        GlStateManager.func_187401_a(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
        this.textureManager.bindTexture(RES_ITEM_GLINT);
        GlStateManager.func_179128_n(5890);
        GlStateManager.func_179094_E();
        GlStateManager.func_179152_a(8.0F, 8.0F, 8.0F);
        float f = (float)(Minecraft.func_71386_F() % 3000L) / 3000.0F / 8.0F;
        GlStateManager.func_179109_b(f, 0.0F, 0.0F);
        GlStateManager.func_179114_b(-50.0F, 0.0F, 0.0F, 1.0F);
        this.func_191965_a(p_191966_1_, -8372020);
        GlStateManager.func_179121_F();
        GlStateManager.func_179094_E();
        GlStateManager.func_179152_a(8.0F, 8.0F, 8.0F);
        float f1 = (float)(Minecraft.func_71386_F() % 4873L) / 4873.0F / 8.0F;
        GlStateManager.func_179109_b(-f1, 0.0F, 0.0F);
        GlStateManager.func_179114_b(10.0F, 0.0F, 0.0F, 1.0F);
        this.func_191965_a(p_191966_1_, -8372020);
        GlStateManager.func_179121_F();
        GlStateManager.func_179128_n(5888);
        GlStateManager.func_187401_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.func_179145_e();
        GlStateManager.func_179143_c(515);
        GlStateManager.func_179132_a(true);
        this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    }

    private void func_175038_a(BufferBuilder p_175038_1_, BakedQuad p_175038_2_)
    {
        Vec3i vec3i = p_175038_2_.getFace().getDirectionVec();
        p_175038_1_.func_178975_e((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
    }

    private void func_191969_a(BufferBuilder p_191969_1_, BakedQuad p_191969_2_, int p_191969_3_)
    {
        p_191969_1_.func_178981_a(p_191969_2_.getVertexData());
        p_191969_1_.func_178968_d(p_191969_3_);
        this.func_175038_a(p_191969_1_, p_191969_2_);
    }

    private void func_191970_a(BufferBuilder p_191970_1_, List<BakedQuad> p_191970_2_, int p_191970_3_, ItemStack p_191970_4_)
    {
        boolean flag = p_191970_3_ == -1 && !p_191970_4_.isEmpty();
        int i = 0;

        for (int j = p_191970_2_.size(); i < j; ++i)
        {
            BakedQuad bakedquad = p_191970_2_.get(i);
            int k = p_191970_3_;

            if (flag && bakedquad.hasTintIndex())
            {
                k = this.itemColors.getColor(p_191970_4_, bakedquad.getTintIndex());

                if (EntityRenderer.field_78517_a)
                {
                    k = TextureUtil.func_177054_c(k);
                }

                k = k | -16777216;
            }

            this.func_191969_a(p_191970_1_, bakedquad, k);
        }
    }

    public boolean func_175050_a(ItemStack p_175050_1_)
    {
        IBakedModel ibakedmodel = this.itemModelMesher.getItemModel(p_175050_1_);
        return ibakedmodel == null ? false : ibakedmodel.isGui3d();
    }

    public void func_181564_a(ItemStack p_181564_1_, ItemCameraTransforms.TransformType p_181564_2_)
    {
        if (!p_181564_1_.isEmpty())
        {
            IBakedModel ibakedmodel = this.getItemModelWithOverrides(p_181564_1_, (World)null, (EntityLivingBase)null);
            this.func_184394_a(p_181564_1_, ibakedmodel, p_181564_2_, false);
        }
    }

    public IBakedModel getItemModelWithOverrides(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entitylivingbaseIn)
    {
        IBakedModel ibakedmodel = this.itemModelMesher.getItemModel(stack);
        Item item = stack.getItem();

        if (item != null && item.hasCustomProperties())
        {
            ResourceLocation resourcelocation = ibakedmodel.getOverrides().func_188021_a(stack, worldIn, entitylivingbaseIn);
            return resourcelocation == null ? ibakedmodel : this.itemModelMesher.getModelManager().getModel(new ModelResourceLocation(resourcelocation, "inventory"));
        }
        else
        {
            return ibakedmodel;
        }
    }

    public void func_184392_a(ItemStack p_184392_1_, EntityLivingBase p_184392_2_, ItemCameraTransforms.TransformType p_184392_3_, boolean p_184392_4_)
    {
        if (!p_184392_1_.isEmpty() && p_184392_2_ != null)
        {
            IBakedModel ibakedmodel = this.getItemModelWithOverrides(p_184392_1_, p_184392_2_.world, p_184392_2_);
            this.func_184394_a(p_184392_1_, ibakedmodel, p_184392_3_, p_184392_4_);
        }
    }

    protected void func_184394_a(ItemStack p_184394_1_, IBakedModel p_184394_2_, ItemCameraTransforms.TransformType p_184394_3_, boolean p_184394_4_)
    {
        if (!p_184394_1_.isEmpty())
        {
            this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            this.textureManager.func_110581_b(TextureMap.LOCATION_BLOCKS_TEXTURE).func_174936_b(false, false);
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.func_179091_B();
            GlStateManager.func_179092_a(516, 0.1F);
            GlStateManager.func_179147_l();
            GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.func_179094_E();
            ItemCameraTransforms itemcameratransforms = p_184394_2_.getItemCameraTransforms();
            ItemCameraTransforms.func_188034_a(itemcameratransforms.getTransform(p_184394_3_), p_184394_4_);

            if (this.func_183005_a(itemcameratransforms.getTransform(p_184394_3_)))
            {
                GlStateManager.func_187407_a(GlStateManager.CullFace.FRONT);
            }

            this.func_180454_a(p_184394_1_, p_184394_2_);
            GlStateManager.func_187407_a(GlStateManager.CullFace.BACK);
            GlStateManager.func_179121_F();
            GlStateManager.func_179101_C();
            GlStateManager.func_179084_k();
            this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            this.textureManager.func_110581_b(TextureMap.LOCATION_BLOCKS_TEXTURE).func_174935_a();
        }
    }

    private boolean func_183005_a(ItemTransformVec3f p_183005_1_)
    {
        return p_183005_1_.scale.x < 0.0F ^ p_183005_1_.scale.y < 0.0F ^ p_183005_1_.scale.z < 0.0F;
    }

    public void renderItemIntoGUI(ItemStack stack, int x, int y)
    {
        this.renderItemModelIntoGUI(stack, x, y, this.getItemModelWithOverrides(stack, (World)null, (EntityLivingBase)null));
    }

    protected void renderItemModelIntoGUI(ItemStack stack, int x, int y, IBakedModel bakedmodel)
    {
        GlStateManager.func_179094_E();
        this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        this.textureManager.func_110581_b(TextureMap.LOCATION_BLOCKS_TEXTURE).func_174936_b(false, false);
        GlStateManager.func_179091_B();
        GlStateManager.func_179141_d();
        GlStateManager.func_179092_a(516, 0.1F);
        GlStateManager.func_179147_l();
        GlStateManager.func_187401_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        this.func_180452_a(x, y, bakedmodel.isGui3d());
        bakedmodel.getItemCameraTransforms().func_181689_a(ItemCameraTransforms.TransformType.GUI);
        this.func_180454_a(stack, bakedmodel);
        GlStateManager.func_179118_c();
        GlStateManager.func_179101_C();
        GlStateManager.func_179140_f();
        GlStateManager.func_179121_F();
        this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        this.textureManager.func_110581_b(TextureMap.LOCATION_BLOCKS_TEXTURE).func_174935_a();
    }

    private void func_180452_a(int p_180452_1_, int p_180452_2_, boolean p_180452_3_)
    {
        GlStateManager.func_179109_b((float)p_180452_1_, (float)p_180452_2_, 100.0F + this.zLevel);
        GlStateManager.func_179109_b(8.0F, 8.0F, 0.0F);
        GlStateManager.func_179152_a(1.0F, -1.0F, 1.0F);
        GlStateManager.func_179152_a(16.0F, 16.0F, 16.0F);

        if (p_180452_3_)
        {
            GlStateManager.func_179145_e();
        }
        else
        {
            GlStateManager.func_179140_f();
        }
    }

    public void renderItemAndEffectIntoGUI(ItemStack stack, int xPosition, int yPosition)
    {
        this.renderItemAndEffectIntoGUI(Minecraft.getInstance().player, stack, xPosition, yPosition);
    }

    public void renderItemAndEffectIntoGUI(@Nullable EntityLivingBase entityIn, final ItemStack itemIn, int x, int y)
    {
        if (!itemIn.isEmpty())
        {
            this.zLevel += 50.0F;

            try
            {
                this.renderItemModelIntoGUI(itemIn, x, y, this.getItemModelWithOverrides(itemIn, (World)null, entityIn));
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering item");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being rendered");
                crashreportcategory.addDetail("Item Type", new ICrashReportDetail<String>()
                {
                    public String call() throws Exception
                    {
                        return String.valueOf((Object)itemIn.getItem());
                    }
                });
                crashreportcategory.addDetail("Item Aux", new ICrashReportDetail<String>()
                {
                    public String call() throws Exception
                    {
                        return String.valueOf(itemIn.func_77960_j());
                    }
                });
                crashreportcategory.addDetail("Item NBT", new ICrashReportDetail<String>()
                {
                    public String call() throws Exception
                    {
                        return String.valueOf((Object)itemIn.getTag());
                    }
                });
                crashreportcategory.addDetail("Item Foil", new ICrashReportDetail<String>()
                {
                    public String call() throws Exception
                    {
                        return String.valueOf(itemIn.hasEffect());
                    }
                });
                throw new ReportedException(crashreport);
            }

            this.zLevel -= 50.0F;
        }
    }

    public void renderItemOverlays(FontRenderer fr, ItemStack stack, int xPosition, int yPosition)
    {
        this.renderItemOverlayIntoGUI(fr, stack, xPosition, yPosition, (String)null);
    }

    /**
     * Renders the stack size and/or damage bar for the given ItemStack.
     */
    public void renderItemOverlayIntoGUI(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, @Nullable String text)
    {
        if (!stack.isEmpty())
        {
            if (stack.getCount() != 1 || text != null)
            {
                String s = text == null ? String.valueOf(stack.getCount()) : text;
                GlStateManager.func_179140_f();
                GlStateManager.func_179097_i();
                GlStateManager.func_179084_k();
                fr.drawStringWithShadow(s, (float)(xPosition + 19 - 2 - fr.getStringWidth(s)), (float)(yPosition + 6 + 3), 16777215);
                GlStateManager.func_179145_e();
                GlStateManager.func_179126_j();
            }

            if (stack.isDamaged())
            {
                GlStateManager.func_179140_f();
                GlStateManager.func_179097_i();
                GlStateManager.func_179090_x();
                GlStateManager.func_179118_c();
                GlStateManager.func_179084_k();
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                float f = (float)stack.getDamage();
                float f1 = (float)stack.getMaxDamage();
                float f2 = Math.max(0.0F, (f1 - f) / f1);
                int i = Math.round(13.0F - f * 13.0F / f1);
                int j = MathHelper.hsvToRGB(f2 / 3.0F, 1.0F, 1.0F);
                this.draw(bufferbuilder, xPosition + 2, yPosition + 13, 13, 2, 0, 0, 0, 255);
                this.draw(bufferbuilder, xPosition + 2, yPosition + 13, i, 1, j >> 16 & 255, j >> 8 & 255, j & 255, 255);
                GlStateManager.func_179147_l();
                GlStateManager.func_179141_d();
                GlStateManager.func_179098_w();
                GlStateManager.func_179145_e();
                GlStateManager.func_179126_j();
            }

            EntityPlayerSP entityplayersp = Minecraft.getInstance().player;
            float f3 = entityplayersp == null ? 0.0F : entityplayersp.getCooldownTracker().getCooldown(stack.getItem(), Minecraft.getInstance().getRenderPartialTicks());

            if (f3 > 0.0F)
            {
                GlStateManager.func_179140_f();
                GlStateManager.func_179097_i();
                GlStateManager.func_179090_x();
                Tessellator tessellator1 = Tessellator.getInstance();
                BufferBuilder bufferbuilder1 = tessellator1.getBuffer();
                this.draw(bufferbuilder1, xPosition, yPosition + MathHelper.floor(16.0F * (1.0F - f3)), 16, MathHelper.ceil(16.0F * f3), 255, 255, 255, 127);
                GlStateManager.func_179098_w();
                GlStateManager.func_179145_e();
                GlStateManager.func_179126_j();
            }
        }
    }

    /**
     * Draw with the WorldRenderer
     */
    private void draw(BufferBuilder renderer, int x, int y, int width, int height, int red, int green, int blue, int alpha)
    {
        renderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        renderer.func_181662_b((double)(x + 0), (double)(y + 0), 0.0D).func_181669_b(red, green, blue, alpha).endVertex();
        renderer.func_181662_b((double)(x + 0), (double)(y + height), 0.0D).func_181669_b(red, green, blue, alpha).endVertex();
        renderer.func_181662_b((double)(x + width), (double)(y + height), 0.0D).func_181669_b(red, green, blue, alpha).endVertex();
        renderer.func_181662_b((double)(x + width), (double)(y + 0), 0.0D).func_181669_b(red, green, blue, alpha).endVertex();
        Tessellator.getInstance().draw();
    }

    private void func_175041_b()
    {
        this.func_175031_a(Blocks.ANVIL, "anvil_intact");
        this.func_175029_a(Blocks.ANVIL, 1, "anvil_slightly_damaged");
        this.func_175029_a(Blocks.ANVIL, 2, "anvil_very_damaged");
        this.func_175029_a(Blocks.field_150404_cg, EnumDyeColor.BLACK.func_176765_a(), "black_carpet");
        this.func_175029_a(Blocks.field_150404_cg, EnumDyeColor.BLUE.func_176765_a(), "blue_carpet");
        this.func_175029_a(Blocks.field_150404_cg, EnumDyeColor.BROWN.func_176765_a(), "brown_carpet");
        this.func_175029_a(Blocks.field_150404_cg, EnumDyeColor.CYAN.func_176765_a(), "cyan_carpet");
        this.func_175029_a(Blocks.field_150404_cg, EnumDyeColor.GRAY.func_176765_a(), "gray_carpet");
        this.func_175029_a(Blocks.field_150404_cg, EnumDyeColor.GREEN.func_176765_a(), "green_carpet");
        this.func_175029_a(Blocks.field_150404_cg, EnumDyeColor.LIGHT_BLUE.func_176765_a(), "light_blue_carpet");
        this.func_175029_a(Blocks.field_150404_cg, EnumDyeColor.LIME.func_176765_a(), "lime_carpet");
        this.func_175029_a(Blocks.field_150404_cg, EnumDyeColor.MAGENTA.func_176765_a(), "magenta_carpet");
        this.func_175029_a(Blocks.field_150404_cg, EnumDyeColor.ORANGE.func_176765_a(), "orange_carpet");
        this.func_175029_a(Blocks.field_150404_cg, EnumDyeColor.PINK.func_176765_a(), "pink_carpet");
        this.func_175029_a(Blocks.field_150404_cg, EnumDyeColor.PURPLE.func_176765_a(), "purple_carpet");
        this.func_175029_a(Blocks.field_150404_cg, EnumDyeColor.RED.func_176765_a(), "red_carpet");
        this.func_175029_a(Blocks.field_150404_cg, EnumDyeColor.SILVER.func_176765_a(), "silver_carpet");
        this.func_175029_a(Blocks.field_150404_cg, EnumDyeColor.WHITE.func_176765_a(), "white_carpet");
        this.func_175029_a(Blocks.field_150404_cg, EnumDyeColor.YELLOW.func_176765_a(), "yellow_carpet");
        this.func_175029_a(Blocks.COBBLESTONE_WALL, BlockWall.EnumType.MOSSY.func_176657_a(), "mossy_cobblestone_wall");
        this.func_175029_a(Blocks.COBBLESTONE_WALL, BlockWall.EnumType.NORMAL.func_176657_a(), "cobblestone_wall");
        this.func_175029_a(Blocks.DIRT, BlockDirt.DirtType.COARSE_DIRT.func_176925_a(), "coarse_dirt");
        this.func_175029_a(Blocks.DIRT, BlockDirt.DirtType.DIRT.func_176925_a(), "dirt");
        this.func_175029_a(Blocks.DIRT, BlockDirt.DirtType.PODZOL.func_176925_a(), "podzol");
        this.func_175029_a(Blocks.field_150398_cm, BlockDoublePlant.EnumPlantType.FERN.func_176936_a(), "double_fern");
        this.func_175029_a(Blocks.field_150398_cm, BlockDoublePlant.EnumPlantType.GRASS.func_176936_a(), "double_grass");
        this.func_175029_a(Blocks.field_150398_cm, BlockDoublePlant.EnumPlantType.PAEONIA.func_176936_a(), "paeonia");
        this.func_175029_a(Blocks.field_150398_cm, BlockDoublePlant.EnumPlantType.ROSE.func_176936_a(), "double_rose");
        this.func_175029_a(Blocks.field_150398_cm, BlockDoublePlant.EnumPlantType.SUNFLOWER.func_176936_a(), "sunflower");
        this.func_175029_a(Blocks.field_150398_cm, BlockDoublePlant.EnumPlantType.SYRINGA.func_176936_a(), "syringa");
        this.func_175029_a(Blocks.field_150362_t, BlockPlanks.EnumType.BIRCH.func_176839_a(), "birch_leaves");
        this.func_175029_a(Blocks.field_150362_t, BlockPlanks.EnumType.JUNGLE.func_176839_a(), "jungle_leaves");
        this.func_175029_a(Blocks.field_150362_t, BlockPlanks.EnumType.OAK.func_176839_a(), "oak_leaves");
        this.func_175029_a(Blocks.field_150362_t, BlockPlanks.EnumType.SPRUCE.func_176839_a(), "spruce_leaves");
        this.func_175029_a(Blocks.field_150361_u, BlockPlanks.EnumType.ACACIA.func_176839_a() - 4, "acacia_leaves");
        this.func_175029_a(Blocks.field_150361_u, BlockPlanks.EnumType.DARK_OAK.func_176839_a() - 4, "dark_oak_leaves");
        this.func_175029_a(Blocks.field_150364_r, BlockPlanks.EnumType.BIRCH.func_176839_a(), "birch_log");
        this.func_175029_a(Blocks.field_150364_r, BlockPlanks.EnumType.JUNGLE.func_176839_a(), "jungle_log");
        this.func_175029_a(Blocks.field_150364_r, BlockPlanks.EnumType.OAK.func_176839_a(), "oak_log");
        this.func_175029_a(Blocks.field_150364_r, BlockPlanks.EnumType.SPRUCE.func_176839_a(), "spruce_log");
        this.func_175029_a(Blocks.field_150363_s, BlockPlanks.EnumType.ACACIA.func_176839_a() - 4, "acacia_log");
        this.func_175029_a(Blocks.field_150363_s, BlockPlanks.EnumType.DARK_OAK.func_176839_a() - 4, "dark_oak_log");
        this.func_175029_a(Blocks.field_150418_aU, BlockSilverfish.EnumType.CHISELED_STONEBRICK.func_176881_a(), "chiseled_brick_monster_egg");
        this.func_175029_a(Blocks.field_150418_aU, BlockSilverfish.EnumType.COBBLESTONE.func_176881_a(), "cobblestone_monster_egg");
        this.func_175029_a(Blocks.field_150418_aU, BlockSilverfish.EnumType.CRACKED_STONEBRICK.func_176881_a(), "cracked_brick_monster_egg");
        this.func_175029_a(Blocks.field_150418_aU, BlockSilverfish.EnumType.MOSSY_STONEBRICK.func_176881_a(), "mossy_brick_monster_egg");
        this.func_175029_a(Blocks.field_150418_aU, BlockSilverfish.EnumType.STONE.func_176881_a(), "stone_monster_egg");
        this.func_175029_a(Blocks.field_150418_aU, BlockSilverfish.EnumType.STONEBRICK.func_176881_a(), "stone_brick_monster_egg");
        this.func_175029_a(Blocks.field_150344_f, BlockPlanks.EnumType.ACACIA.func_176839_a(), "acacia_planks");
        this.func_175029_a(Blocks.field_150344_f, BlockPlanks.EnumType.BIRCH.func_176839_a(), "birch_planks");
        this.func_175029_a(Blocks.field_150344_f, BlockPlanks.EnumType.DARK_OAK.func_176839_a(), "dark_oak_planks");
        this.func_175029_a(Blocks.field_150344_f, BlockPlanks.EnumType.JUNGLE.func_176839_a(), "jungle_planks");
        this.func_175029_a(Blocks.field_150344_f, BlockPlanks.EnumType.OAK.func_176839_a(), "oak_planks");
        this.func_175029_a(Blocks.field_150344_f, BlockPlanks.EnumType.SPRUCE.func_176839_a(), "spruce_planks");
        this.func_175029_a(Blocks.PRISMARINE, BlockPrismarine.EnumType.BRICKS.func_176807_a(), "prismarine_bricks");
        this.func_175029_a(Blocks.PRISMARINE, BlockPrismarine.EnumType.DARK.func_176807_a(), "dark_prismarine");
        this.func_175029_a(Blocks.PRISMARINE, BlockPrismarine.EnumType.ROUGH.func_176807_a(), "prismarine");
        this.func_175029_a(Blocks.QUARTZ_BLOCK, BlockQuartz.EnumType.CHISELED.func_176796_a(), "chiseled_quartz_block");
        this.func_175029_a(Blocks.QUARTZ_BLOCK, BlockQuartz.EnumType.DEFAULT.func_176796_a(), "quartz_block");
        this.func_175029_a(Blocks.QUARTZ_BLOCK, BlockQuartz.EnumType.LINES_Y.func_176796_a(), "quartz_column");
        this.func_175029_a(Blocks.field_150328_O, BlockFlower.EnumFlowerType.ALLIUM.func_176968_b(), "allium");
        this.func_175029_a(Blocks.field_150328_O, BlockFlower.EnumFlowerType.BLUE_ORCHID.func_176968_b(), "blue_orchid");
        this.func_175029_a(Blocks.field_150328_O, BlockFlower.EnumFlowerType.HOUSTONIA.func_176968_b(), "houstonia");
        this.func_175029_a(Blocks.field_150328_O, BlockFlower.EnumFlowerType.ORANGE_TULIP.func_176968_b(), "orange_tulip");
        this.func_175029_a(Blocks.field_150328_O, BlockFlower.EnumFlowerType.OXEYE_DAISY.func_176968_b(), "oxeye_daisy");
        this.func_175029_a(Blocks.field_150328_O, BlockFlower.EnumFlowerType.PINK_TULIP.func_176968_b(), "pink_tulip");
        this.func_175029_a(Blocks.field_150328_O, BlockFlower.EnumFlowerType.POPPY.func_176968_b(), "poppy");
        this.func_175029_a(Blocks.field_150328_O, BlockFlower.EnumFlowerType.RED_TULIP.func_176968_b(), "red_tulip");
        this.func_175029_a(Blocks.field_150328_O, BlockFlower.EnumFlowerType.WHITE_TULIP.func_176968_b(), "white_tulip");
        this.func_175029_a(Blocks.SAND, BlockSand.EnumType.RED_SAND.func_176688_a(), "red_sand");
        this.func_175029_a(Blocks.SAND, BlockSand.EnumType.SAND.func_176688_a(), "sand");
        this.func_175029_a(Blocks.SANDSTONE, BlockSandStone.EnumType.CHISELED.func_176675_a(), "chiseled_sandstone");
        this.func_175029_a(Blocks.SANDSTONE, BlockSandStone.EnumType.DEFAULT.func_176675_a(), "sandstone");
        this.func_175029_a(Blocks.SANDSTONE, BlockSandStone.EnumType.SMOOTH.func_176675_a(), "smooth_sandstone");
        this.func_175029_a(Blocks.RED_SANDSTONE, BlockRedSandstone.EnumType.CHISELED.func_176827_a(), "chiseled_red_sandstone");
        this.func_175029_a(Blocks.RED_SANDSTONE, BlockRedSandstone.EnumType.DEFAULT.func_176827_a(), "red_sandstone");
        this.func_175029_a(Blocks.RED_SANDSTONE, BlockRedSandstone.EnumType.SMOOTH.func_176827_a(), "smooth_red_sandstone");
        this.func_175029_a(Blocks.field_150345_g, BlockPlanks.EnumType.ACACIA.func_176839_a(), "acacia_sapling");
        this.func_175029_a(Blocks.field_150345_g, BlockPlanks.EnumType.BIRCH.func_176839_a(), "birch_sapling");
        this.func_175029_a(Blocks.field_150345_g, BlockPlanks.EnumType.DARK_OAK.func_176839_a(), "dark_oak_sapling");
        this.func_175029_a(Blocks.field_150345_g, BlockPlanks.EnumType.JUNGLE.func_176839_a(), "jungle_sapling");
        this.func_175029_a(Blocks.field_150345_g, BlockPlanks.EnumType.OAK.func_176839_a(), "oak_sapling");
        this.func_175029_a(Blocks.field_150345_g, BlockPlanks.EnumType.SPRUCE.func_176839_a(), "spruce_sapling");
        this.func_175029_a(Blocks.SPONGE, 0, "sponge");
        this.func_175029_a(Blocks.SPONGE, 1, "sponge_wet");
        this.func_175029_a(Blocks.field_150399_cn, EnumDyeColor.BLACK.func_176765_a(), "black_stained_glass");
        this.func_175029_a(Blocks.field_150399_cn, EnumDyeColor.BLUE.func_176765_a(), "blue_stained_glass");
        this.func_175029_a(Blocks.field_150399_cn, EnumDyeColor.BROWN.func_176765_a(), "brown_stained_glass");
        this.func_175029_a(Blocks.field_150399_cn, EnumDyeColor.CYAN.func_176765_a(), "cyan_stained_glass");
        this.func_175029_a(Blocks.field_150399_cn, EnumDyeColor.GRAY.func_176765_a(), "gray_stained_glass");
        this.func_175029_a(Blocks.field_150399_cn, EnumDyeColor.GREEN.func_176765_a(), "green_stained_glass");
        this.func_175029_a(Blocks.field_150399_cn, EnumDyeColor.LIGHT_BLUE.func_176765_a(), "light_blue_stained_glass");
        this.func_175029_a(Blocks.field_150399_cn, EnumDyeColor.LIME.func_176765_a(), "lime_stained_glass");
        this.func_175029_a(Blocks.field_150399_cn, EnumDyeColor.MAGENTA.func_176765_a(), "magenta_stained_glass");
        this.func_175029_a(Blocks.field_150399_cn, EnumDyeColor.ORANGE.func_176765_a(), "orange_stained_glass");
        this.func_175029_a(Blocks.field_150399_cn, EnumDyeColor.PINK.func_176765_a(), "pink_stained_glass");
        this.func_175029_a(Blocks.field_150399_cn, EnumDyeColor.PURPLE.func_176765_a(), "purple_stained_glass");
        this.func_175029_a(Blocks.field_150399_cn, EnumDyeColor.RED.func_176765_a(), "red_stained_glass");
        this.func_175029_a(Blocks.field_150399_cn, EnumDyeColor.SILVER.func_176765_a(), "silver_stained_glass");
        this.func_175029_a(Blocks.field_150399_cn, EnumDyeColor.WHITE.func_176765_a(), "white_stained_glass");
        this.func_175029_a(Blocks.field_150399_cn, EnumDyeColor.YELLOW.func_176765_a(), "yellow_stained_glass");
        this.func_175029_a(Blocks.field_150397_co, EnumDyeColor.BLACK.func_176765_a(), "black_stained_glass_pane");
        this.func_175029_a(Blocks.field_150397_co, EnumDyeColor.BLUE.func_176765_a(), "blue_stained_glass_pane");
        this.func_175029_a(Blocks.field_150397_co, EnumDyeColor.BROWN.func_176765_a(), "brown_stained_glass_pane");
        this.func_175029_a(Blocks.field_150397_co, EnumDyeColor.CYAN.func_176765_a(), "cyan_stained_glass_pane");
        this.func_175029_a(Blocks.field_150397_co, EnumDyeColor.GRAY.func_176765_a(), "gray_stained_glass_pane");
        this.func_175029_a(Blocks.field_150397_co, EnumDyeColor.GREEN.func_176765_a(), "green_stained_glass_pane");
        this.func_175029_a(Blocks.field_150397_co, EnumDyeColor.LIGHT_BLUE.func_176765_a(), "light_blue_stained_glass_pane");
        this.func_175029_a(Blocks.field_150397_co, EnumDyeColor.LIME.func_176765_a(), "lime_stained_glass_pane");
        this.func_175029_a(Blocks.field_150397_co, EnumDyeColor.MAGENTA.func_176765_a(), "magenta_stained_glass_pane");
        this.func_175029_a(Blocks.field_150397_co, EnumDyeColor.ORANGE.func_176765_a(), "orange_stained_glass_pane");
        this.func_175029_a(Blocks.field_150397_co, EnumDyeColor.PINK.func_176765_a(), "pink_stained_glass_pane");
        this.func_175029_a(Blocks.field_150397_co, EnumDyeColor.PURPLE.func_176765_a(), "purple_stained_glass_pane");
        this.func_175029_a(Blocks.field_150397_co, EnumDyeColor.RED.func_176765_a(), "red_stained_glass_pane");
        this.func_175029_a(Blocks.field_150397_co, EnumDyeColor.SILVER.func_176765_a(), "silver_stained_glass_pane");
        this.func_175029_a(Blocks.field_150397_co, EnumDyeColor.WHITE.func_176765_a(), "white_stained_glass_pane");
        this.func_175029_a(Blocks.field_150397_co, EnumDyeColor.YELLOW.func_176765_a(), "yellow_stained_glass_pane");
        this.func_175029_a(Blocks.field_150406_ce, EnumDyeColor.BLACK.func_176765_a(), "black_stained_hardened_clay");
        this.func_175029_a(Blocks.field_150406_ce, EnumDyeColor.BLUE.func_176765_a(), "blue_stained_hardened_clay");
        this.func_175029_a(Blocks.field_150406_ce, EnumDyeColor.BROWN.func_176765_a(), "brown_stained_hardened_clay");
        this.func_175029_a(Blocks.field_150406_ce, EnumDyeColor.CYAN.func_176765_a(), "cyan_stained_hardened_clay");
        this.func_175029_a(Blocks.field_150406_ce, EnumDyeColor.GRAY.func_176765_a(), "gray_stained_hardened_clay");
        this.func_175029_a(Blocks.field_150406_ce, EnumDyeColor.GREEN.func_176765_a(), "green_stained_hardened_clay");
        this.func_175029_a(Blocks.field_150406_ce, EnumDyeColor.LIGHT_BLUE.func_176765_a(), "light_blue_stained_hardened_clay");
        this.func_175029_a(Blocks.field_150406_ce, EnumDyeColor.LIME.func_176765_a(), "lime_stained_hardened_clay");
        this.func_175029_a(Blocks.field_150406_ce, EnumDyeColor.MAGENTA.func_176765_a(), "magenta_stained_hardened_clay");
        this.func_175029_a(Blocks.field_150406_ce, EnumDyeColor.ORANGE.func_176765_a(), "orange_stained_hardened_clay");
        this.func_175029_a(Blocks.field_150406_ce, EnumDyeColor.PINK.func_176765_a(), "pink_stained_hardened_clay");
        this.func_175029_a(Blocks.field_150406_ce, EnumDyeColor.PURPLE.func_176765_a(), "purple_stained_hardened_clay");
        this.func_175029_a(Blocks.field_150406_ce, EnumDyeColor.RED.func_176765_a(), "red_stained_hardened_clay");
        this.func_175029_a(Blocks.field_150406_ce, EnumDyeColor.SILVER.func_176765_a(), "silver_stained_hardened_clay");
        this.func_175029_a(Blocks.field_150406_ce, EnumDyeColor.WHITE.func_176765_a(), "white_stained_hardened_clay");
        this.func_175029_a(Blocks.field_150406_ce, EnumDyeColor.YELLOW.func_176765_a(), "yellow_stained_hardened_clay");
        this.func_175029_a(Blocks.STONE, BlockStone.EnumType.ANDESITE.func_176642_a(), "andesite");
        this.func_175029_a(Blocks.STONE, BlockStone.EnumType.ANDESITE_SMOOTH.func_176642_a(), "andesite_smooth");
        this.func_175029_a(Blocks.STONE, BlockStone.EnumType.DIORITE.func_176642_a(), "diorite");
        this.func_175029_a(Blocks.STONE, BlockStone.EnumType.DIORITE_SMOOTH.func_176642_a(), "diorite_smooth");
        this.func_175029_a(Blocks.STONE, BlockStone.EnumType.GRANITE.func_176642_a(), "granite");
        this.func_175029_a(Blocks.STONE, BlockStone.EnumType.GRANITE_SMOOTH.func_176642_a(), "granite_smooth");
        this.func_175029_a(Blocks.STONE, BlockStone.EnumType.STONE.func_176642_a(), "stone");
        this.func_175029_a(Blocks.field_150417_aV, BlockStoneBrick.EnumType.CRACKED.func_176612_a(), "cracked_stonebrick");
        this.func_175029_a(Blocks.field_150417_aV, BlockStoneBrick.EnumType.DEFAULT.func_176612_a(), "stonebrick");
        this.func_175029_a(Blocks.field_150417_aV, BlockStoneBrick.EnumType.CHISELED.func_176612_a(), "chiseled_stonebrick");
        this.func_175029_a(Blocks.field_150417_aV, BlockStoneBrick.EnumType.MOSSY.func_176612_a(), "mossy_stonebrick");
        this.func_175029_a(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.BRICK.func_176624_a(), "brick_slab");
        this.func_175029_a(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.COBBLESTONE.func_176624_a(), "cobblestone_slab");
        this.func_175029_a(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.WOOD.func_176624_a(), "old_wood_slab");
        this.func_175029_a(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.NETHERBRICK.func_176624_a(), "nether_brick_slab");
        this.func_175029_a(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.QUARTZ.func_176624_a(), "quartz_slab");
        this.func_175029_a(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.SAND.func_176624_a(), "sandstone_slab");
        this.func_175029_a(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.SMOOTHBRICK.func_176624_a(), "stone_brick_slab");
        this.func_175029_a(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.STONE.func_176624_a(), "stone_slab");
        this.func_175029_a(Blocks.field_180389_cP, BlockStoneSlabNew.EnumType.RED_SANDSTONE.func_176915_a(), "red_sandstone_slab");
        this.func_175029_a(Blocks.field_150329_H, BlockTallGrass.EnumType.DEAD_BUSH.func_177044_a(), "dead_bush");
        this.func_175029_a(Blocks.field_150329_H, BlockTallGrass.EnumType.FERN.func_177044_a(), "fern");
        this.func_175029_a(Blocks.field_150329_H, BlockTallGrass.EnumType.GRASS.func_177044_a(), "tall_grass");
        this.func_175029_a(Blocks.field_150376_bx, BlockPlanks.EnumType.ACACIA.func_176839_a(), "acacia_slab");
        this.func_175029_a(Blocks.field_150376_bx, BlockPlanks.EnumType.BIRCH.func_176839_a(), "birch_slab");
        this.func_175029_a(Blocks.field_150376_bx, BlockPlanks.EnumType.DARK_OAK.func_176839_a(), "dark_oak_slab");
        this.func_175029_a(Blocks.field_150376_bx, BlockPlanks.EnumType.JUNGLE.func_176839_a(), "jungle_slab");
        this.func_175029_a(Blocks.field_150376_bx, BlockPlanks.EnumType.OAK.func_176839_a(), "oak_slab");
        this.func_175029_a(Blocks.field_150376_bx, BlockPlanks.EnumType.SPRUCE.func_176839_a(), "spruce_slab");
        this.func_175029_a(Blocks.field_150325_L, EnumDyeColor.BLACK.func_176765_a(), "black_wool");
        this.func_175029_a(Blocks.field_150325_L, EnumDyeColor.BLUE.func_176765_a(), "blue_wool");
        this.func_175029_a(Blocks.field_150325_L, EnumDyeColor.BROWN.func_176765_a(), "brown_wool");
        this.func_175029_a(Blocks.field_150325_L, EnumDyeColor.CYAN.func_176765_a(), "cyan_wool");
        this.func_175029_a(Blocks.field_150325_L, EnumDyeColor.GRAY.func_176765_a(), "gray_wool");
        this.func_175029_a(Blocks.field_150325_L, EnumDyeColor.GREEN.func_176765_a(), "green_wool");
        this.func_175029_a(Blocks.field_150325_L, EnumDyeColor.LIGHT_BLUE.func_176765_a(), "light_blue_wool");
        this.func_175029_a(Blocks.field_150325_L, EnumDyeColor.LIME.func_176765_a(), "lime_wool");
        this.func_175029_a(Blocks.field_150325_L, EnumDyeColor.MAGENTA.func_176765_a(), "magenta_wool");
        this.func_175029_a(Blocks.field_150325_L, EnumDyeColor.ORANGE.func_176765_a(), "orange_wool");
        this.func_175029_a(Blocks.field_150325_L, EnumDyeColor.PINK.func_176765_a(), "pink_wool");
        this.func_175029_a(Blocks.field_150325_L, EnumDyeColor.PURPLE.func_176765_a(), "purple_wool");
        this.func_175029_a(Blocks.field_150325_L, EnumDyeColor.RED.func_176765_a(), "red_wool");
        this.func_175029_a(Blocks.field_150325_L, EnumDyeColor.SILVER.func_176765_a(), "silver_wool");
        this.func_175029_a(Blocks.field_150325_L, EnumDyeColor.WHITE.func_176765_a(), "white_wool");
        this.func_175029_a(Blocks.field_150325_L, EnumDyeColor.YELLOW.func_176765_a(), "yellow_wool");
        this.func_175031_a(Blocks.FARMLAND, "farmland");
        this.func_175031_a(Blocks.ACACIA_STAIRS, "acacia_stairs");
        this.func_175031_a(Blocks.ACTIVATOR_RAIL, "activator_rail");
        this.func_175031_a(Blocks.BEACON, "beacon");
        this.func_175031_a(Blocks.BEDROCK, "bedrock");
        this.func_175031_a(Blocks.BIRCH_STAIRS, "birch_stairs");
        this.func_175031_a(Blocks.BOOKSHELF, "bookshelf");
        this.func_175031_a(Blocks.field_150336_V, "brick_block");
        this.func_175031_a(Blocks.field_150336_V, "brick_block");
        this.func_175031_a(Blocks.BRICK_STAIRS, "brick_stairs");
        this.func_175031_a(Blocks.BROWN_MUSHROOM, "brown_mushroom");
        this.func_175031_a(Blocks.CACTUS, "cactus");
        this.func_175031_a(Blocks.CLAY, "clay");
        this.func_175031_a(Blocks.COAL_BLOCK, "coal_block");
        this.func_175031_a(Blocks.COAL_ORE, "coal_ore");
        this.func_175031_a(Blocks.COBBLESTONE, "cobblestone");
        this.func_175031_a(Blocks.CRAFTING_TABLE, "crafting_table");
        this.func_175031_a(Blocks.DARK_OAK_STAIRS, "dark_oak_stairs");
        this.func_175031_a(Blocks.DAYLIGHT_DETECTOR, "daylight_detector");
        this.func_175031_a(Blocks.field_150330_I, "dead_bush");
        this.func_175031_a(Blocks.DETECTOR_RAIL, "detector_rail");
        this.func_175031_a(Blocks.DIAMOND_BLOCK, "diamond_block");
        this.func_175031_a(Blocks.DIAMOND_ORE, "diamond_ore");
        this.func_175031_a(Blocks.DISPENSER, "dispenser");
        this.func_175031_a(Blocks.DROPPER, "dropper");
        this.func_175031_a(Blocks.EMERALD_BLOCK, "emerald_block");
        this.func_175031_a(Blocks.EMERALD_ORE, "emerald_ore");
        this.func_175031_a(Blocks.ENCHANTING_TABLE, "enchanting_table");
        this.func_175031_a(Blocks.END_PORTAL_FRAME, "end_portal_frame");
        this.func_175031_a(Blocks.END_STONE, "end_stone");
        this.func_175031_a(Blocks.OAK_FENCE, "oak_fence");
        this.func_175031_a(Blocks.SPRUCE_FENCE, "spruce_fence");
        this.func_175031_a(Blocks.BIRCH_FENCE, "birch_fence");
        this.func_175031_a(Blocks.JUNGLE_FENCE, "jungle_fence");
        this.func_175031_a(Blocks.DARK_OAK_FENCE, "dark_oak_fence");
        this.func_175031_a(Blocks.ACACIA_FENCE, "acacia_fence");
        this.func_175031_a(Blocks.OAK_FENCE_GATE, "oak_fence_gate");
        this.func_175031_a(Blocks.SPRUCE_FENCE_GATE, "spruce_fence_gate");
        this.func_175031_a(Blocks.BIRCH_FENCE_GATE, "birch_fence_gate");
        this.func_175031_a(Blocks.JUNGLE_FENCE_GATE, "jungle_fence_gate");
        this.func_175031_a(Blocks.DARK_OAK_FENCE_GATE, "dark_oak_fence_gate");
        this.func_175031_a(Blocks.ACACIA_FENCE_GATE, "acacia_fence_gate");
        this.func_175031_a(Blocks.FURNACE, "furnace");
        this.func_175031_a(Blocks.GLASS, "glass");
        this.func_175031_a(Blocks.GLASS_PANE, "glass_pane");
        this.func_175031_a(Blocks.GLOWSTONE, "glowstone");
        this.func_175031_a(Blocks.field_150318_D, "golden_rail");
        this.func_175031_a(Blocks.GOLD_BLOCK, "gold_block");
        this.func_175031_a(Blocks.GOLD_ORE, "gold_ore");
        this.func_175031_a(Blocks.GRASS, "grass");
        this.func_175031_a(Blocks.GRASS_PATH, "grass_path");
        this.func_175031_a(Blocks.GRAVEL, "gravel");
        this.func_175031_a(Blocks.TERRACOTTA, "hardened_clay");
        this.func_175031_a(Blocks.HAY_BLOCK, "hay_block");
        this.func_175031_a(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, "heavy_weighted_pressure_plate");
        this.func_175031_a(Blocks.HOPPER, "hopper");
        this.func_175031_a(Blocks.ICE, "ice");
        this.func_175031_a(Blocks.IRON_BARS, "iron_bars");
        this.func_175031_a(Blocks.IRON_BLOCK, "iron_block");
        this.func_175031_a(Blocks.IRON_ORE, "iron_ore");
        this.func_175031_a(Blocks.IRON_TRAPDOOR, "iron_trapdoor");
        this.func_175031_a(Blocks.JUKEBOX, "jukebox");
        this.func_175031_a(Blocks.JUNGLE_STAIRS, "jungle_stairs");
        this.func_175031_a(Blocks.LADDER, "ladder");
        this.func_175031_a(Blocks.LAPIS_BLOCK, "lapis_block");
        this.func_175031_a(Blocks.LAPIS_ORE, "lapis_ore");
        this.func_175031_a(Blocks.LEVER, "lever");
        this.func_175031_a(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, "light_weighted_pressure_plate");
        this.func_175031_a(Blocks.field_150428_aP, "lit_pumpkin");
        this.func_175031_a(Blocks.MELON, "melon_block");
        this.func_175031_a(Blocks.MOSSY_COBBLESTONE, "mossy_cobblestone");
        this.func_175031_a(Blocks.MYCELIUM, "mycelium");
        this.func_175031_a(Blocks.NETHERRACK, "netherrack");
        this.func_175031_a(Blocks.field_150385_bj, "nether_brick");
        this.func_175031_a(Blocks.NETHER_BRICK_FENCE, "nether_brick_fence");
        this.func_175031_a(Blocks.NETHER_BRICK_STAIRS, "nether_brick_stairs");
        this.func_175031_a(Blocks.field_150323_B, "noteblock");
        this.func_175031_a(Blocks.OAK_STAIRS, "oak_stairs");
        this.func_175031_a(Blocks.OBSIDIAN, "obsidian");
        this.func_175031_a(Blocks.PACKED_ICE, "packed_ice");
        this.func_175031_a(Blocks.PISTON, "piston");
        this.func_175031_a(Blocks.PUMPKIN, "pumpkin");
        this.func_175031_a(Blocks.field_150449_bY, "quartz_ore");
        this.func_175031_a(Blocks.QUARTZ_STAIRS, "quartz_stairs");
        this.func_175031_a(Blocks.RAIL, "rail");
        this.func_175031_a(Blocks.REDSTONE_BLOCK, "redstone_block");
        this.func_175031_a(Blocks.REDSTONE_LAMP, "redstone_lamp");
        this.func_175031_a(Blocks.REDSTONE_ORE, "redstone_ore");
        this.func_175031_a(Blocks.REDSTONE_TORCH, "redstone_torch");
        this.func_175031_a(Blocks.RED_MUSHROOM, "red_mushroom");
        this.func_175031_a(Blocks.SANDSTONE_STAIRS, "sandstone_stairs");
        this.func_175031_a(Blocks.RED_SANDSTONE_STAIRS, "red_sandstone_stairs");
        this.func_175031_a(Blocks.SEA_LANTERN, "sea_lantern");
        this.func_175031_a(Blocks.SLIME_BLOCK, "slime");
        this.func_175031_a(Blocks.SNOW, "snow");
        this.func_175031_a(Blocks.field_150431_aC, "snow_layer");
        this.func_175031_a(Blocks.SOUL_SAND, "soul_sand");
        this.func_175031_a(Blocks.SPRUCE_STAIRS, "spruce_stairs");
        this.func_175031_a(Blocks.STICKY_PISTON, "sticky_piston");
        this.func_175031_a(Blocks.STONE_BRICK_STAIRS, "stone_brick_stairs");
        this.func_175031_a(Blocks.STONE_BUTTON, "stone_button");
        this.func_175031_a(Blocks.STONE_PRESSURE_PLATE, "stone_pressure_plate");
        this.func_175031_a(Blocks.field_150446_ar, "stone_stairs");
        this.func_175031_a(Blocks.TNT, "tnt");
        this.func_175031_a(Blocks.TORCH, "torch");
        this.func_175031_a(Blocks.field_150415_aT, "trapdoor");
        this.func_175031_a(Blocks.TRIPWIRE_HOOK, "tripwire_hook");
        this.func_175031_a(Blocks.VINE, "vine");
        this.func_175031_a(Blocks.field_150392_bi, "waterlily");
        this.func_175031_a(Blocks.field_150321_G, "web");
        this.func_175031_a(Blocks.field_150471_bO, "wooden_button");
        this.func_175031_a(Blocks.field_150452_aw, "wooden_pressure_plate");
        this.func_175029_a(Blocks.field_150327_N, BlockFlower.EnumFlowerType.DANDELION.func_176968_b(), "dandelion");
        this.func_175031_a(Blocks.END_ROD, "end_rod");
        this.func_175031_a(Blocks.CHORUS_PLANT, "chorus_plant");
        this.func_175031_a(Blocks.CHORUS_FLOWER, "chorus_flower");
        this.func_175031_a(Blocks.PURPUR_BLOCK, "purpur_block");
        this.func_175031_a(Blocks.PURPUR_PILLAR, "purpur_pillar");
        this.func_175031_a(Blocks.PURPUR_STAIRS, "purpur_stairs");
        this.func_175031_a(Blocks.PURPUR_SLAB, "purpur_slab");
        this.func_175031_a(Blocks.field_185770_cW, "purpur_double_slab");
        this.func_175031_a(Blocks.field_185772_cY, "end_bricks");
        this.func_175031_a(Blocks.field_189877_df, "magma");
        this.func_175031_a(Blocks.NETHER_WART_BLOCK, "nether_wart_block");
        this.func_175031_a(Blocks.field_189879_dh, "red_nether_brick");
        this.func_175031_a(Blocks.BONE_BLOCK, "bone_block");
        this.func_175031_a(Blocks.STRUCTURE_VOID, "structure_void");
        this.func_175031_a(Blocks.OBSERVER, "observer");
        this.func_175031_a(Blocks.WHITE_SHULKER_BOX, "white_shulker_box");
        this.func_175031_a(Blocks.ORANGE_SHULKER_BOX, "orange_shulker_box");
        this.func_175031_a(Blocks.MAGENTA_SHULKER_BOX, "magenta_shulker_box");
        this.func_175031_a(Blocks.LIGHT_BLUE_SHULKER_BOX, "light_blue_shulker_box");
        this.func_175031_a(Blocks.YELLOW_SHULKER_BOX, "yellow_shulker_box");
        this.func_175031_a(Blocks.LIME_SHULKER_BOX, "lime_shulker_box");
        this.func_175031_a(Blocks.PINK_SHULKER_BOX, "pink_shulker_box");
        this.func_175031_a(Blocks.GRAY_SHULKER_BOX, "gray_shulker_box");
        this.func_175031_a(Blocks.field_190985_dt, "silver_shulker_box");
        this.func_175031_a(Blocks.CYAN_SHULKER_BOX, "cyan_shulker_box");
        this.func_175031_a(Blocks.PURPLE_SHULKER_BOX, "purple_shulker_box");
        this.func_175031_a(Blocks.BLUE_SHULKER_BOX, "blue_shulker_box");
        this.func_175031_a(Blocks.BROWN_SHULKER_BOX, "brown_shulker_box");
        this.func_175031_a(Blocks.GREEN_SHULKER_BOX, "green_shulker_box");
        this.func_175031_a(Blocks.RED_SHULKER_BOX, "red_shulker_box");
        this.func_175031_a(Blocks.BLACK_SHULKER_BOX, "black_shulker_box");
        this.func_175031_a(Blocks.WHITE_GLAZED_TERRACOTTA, "white_glazed_terracotta");
        this.func_175031_a(Blocks.ORANGE_GLAZED_TERRACOTTA, "orange_glazed_terracotta");
        this.func_175031_a(Blocks.MAGENTA_GLAZED_TERRACOTTA, "magenta_glazed_terracotta");
        this.func_175031_a(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, "light_blue_glazed_terracotta");
        this.func_175031_a(Blocks.YELLOW_GLAZED_TERRACOTTA, "yellow_glazed_terracotta");
        this.func_175031_a(Blocks.LIME_GLAZED_TERRACOTTA, "lime_glazed_terracotta");
        this.func_175031_a(Blocks.PINK_GLAZED_TERRACOTTA, "pink_glazed_terracotta");
        this.func_175031_a(Blocks.GRAY_GLAZED_TERRACOTTA, "gray_glazed_terracotta");
        this.func_175031_a(Blocks.field_192435_dJ, "silver_glazed_terracotta");
        this.func_175031_a(Blocks.CYAN_GLAZED_TERRACOTTA, "cyan_glazed_terracotta");
        this.func_175031_a(Blocks.PURPLE_GLAZED_TERRACOTTA, "purple_glazed_terracotta");
        this.func_175031_a(Blocks.BLUE_GLAZED_TERRACOTTA, "blue_glazed_terracotta");
        this.func_175031_a(Blocks.BROWN_GLAZED_TERRACOTTA, "brown_glazed_terracotta");
        this.func_175031_a(Blocks.GREEN_GLAZED_TERRACOTTA, "green_glazed_terracotta");
        this.func_175031_a(Blocks.RED_GLAZED_TERRACOTTA, "red_glazed_terracotta");
        this.func_175031_a(Blocks.BLACK_GLAZED_TERRACOTTA, "black_glazed_terracotta");

        for (EnumDyeColor enumdyecolor : EnumDyeColor.values())
        {
            this.func_175029_a(Blocks.field_192443_dR, enumdyecolor.func_176765_a(), enumdyecolor.func_192396_c() + "_concrete");
            this.func_175029_a(Blocks.field_192444_dS, enumdyecolor.func_176765_a(), enumdyecolor.func_192396_c() + "_concrete_powder");
        }

        this.func_175031_a(Blocks.CHEST, "chest");
        this.func_175031_a(Blocks.TRAPPED_CHEST, "trapped_chest");
        this.func_175031_a(Blocks.ENDER_CHEST, "ender_chest");
        this.func_175047_a(Items.IRON_SHOVEL, "iron_shovel");
        this.func_175047_a(Items.IRON_PICKAXE, "iron_pickaxe");
        this.func_175047_a(Items.IRON_AXE, "iron_axe");
        this.func_175047_a(Items.FLINT_AND_STEEL, "flint_and_steel");
        this.func_175047_a(Items.APPLE, "apple");
        this.func_175047_a(Items.BOW, "bow");
        this.func_175047_a(Items.ARROW, "arrow");
        this.func_175047_a(Items.SPECTRAL_ARROW, "spectral_arrow");
        this.func_175047_a(Items.TIPPED_ARROW, "tipped_arrow");
        this.func_175048_a(Items.COAL, 0, "coal");
        this.func_175048_a(Items.COAL, 1, "charcoal");
        this.func_175047_a(Items.DIAMOND, "diamond");
        this.func_175047_a(Items.IRON_INGOT, "iron_ingot");
        this.func_175047_a(Items.GOLD_INGOT, "gold_ingot");
        this.func_175047_a(Items.IRON_SWORD, "iron_sword");
        this.func_175047_a(Items.WOODEN_SWORD, "wooden_sword");
        this.func_175047_a(Items.WOODEN_SHOVEL, "wooden_shovel");
        this.func_175047_a(Items.WOODEN_PICKAXE, "wooden_pickaxe");
        this.func_175047_a(Items.WOODEN_AXE, "wooden_axe");
        this.func_175047_a(Items.STONE_SWORD, "stone_sword");
        this.func_175047_a(Items.STONE_SHOVEL, "stone_shovel");
        this.func_175047_a(Items.STONE_PICKAXE, "stone_pickaxe");
        this.func_175047_a(Items.STONE_AXE, "stone_axe");
        this.func_175047_a(Items.DIAMOND_SWORD, "diamond_sword");
        this.func_175047_a(Items.DIAMOND_SHOVEL, "diamond_shovel");
        this.func_175047_a(Items.DIAMOND_PICKAXE, "diamond_pickaxe");
        this.func_175047_a(Items.DIAMOND_AXE, "diamond_axe");
        this.func_175047_a(Items.STICK, "stick");
        this.func_175047_a(Items.BOWL, "bowl");
        this.func_175047_a(Items.MUSHROOM_STEW, "mushroom_stew");
        this.func_175047_a(Items.GOLDEN_SWORD, "golden_sword");
        this.func_175047_a(Items.GOLDEN_SHOVEL, "golden_shovel");
        this.func_175047_a(Items.GOLDEN_PICKAXE, "golden_pickaxe");
        this.func_175047_a(Items.GOLDEN_AXE, "golden_axe");
        this.func_175047_a(Items.STRING, "string");
        this.func_175047_a(Items.FEATHER, "feather");
        this.func_175047_a(Items.GUNPOWDER, "gunpowder");
        this.func_175047_a(Items.WOODEN_HOE, "wooden_hoe");
        this.func_175047_a(Items.STONE_HOE, "stone_hoe");
        this.func_175047_a(Items.IRON_HOE, "iron_hoe");
        this.func_175047_a(Items.DIAMOND_HOE, "diamond_hoe");
        this.func_175047_a(Items.GOLDEN_HOE, "golden_hoe");
        this.func_175047_a(Items.WHEAT_SEEDS, "wheat_seeds");
        this.func_175047_a(Items.WHEAT, "wheat");
        this.func_175047_a(Items.BREAD, "bread");
        this.func_175047_a(Items.LEATHER_HELMET, "leather_helmet");
        this.func_175047_a(Items.LEATHER_CHESTPLATE, "leather_chestplate");
        this.func_175047_a(Items.LEATHER_LEGGINGS, "leather_leggings");
        this.func_175047_a(Items.LEATHER_BOOTS, "leather_boots");
        this.func_175047_a(Items.CHAINMAIL_HELMET, "chainmail_helmet");
        this.func_175047_a(Items.CHAINMAIL_CHESTPLATE, "chainmail_chestplate");
        this.func_175047_a(Items.CHAINMAIL_LEGGINGS, "chainmail_leggings");
        this.func_175047_a(Items.CHAINMAIL_BOOTS, "chainmail_boots");
        this.func_175047_a(Items.IRON_HELMET, "iron_helmet");
        this.func_175047_a(Items.IRON_CHESTPLATE, "iron_chestplate");
        this.func_175047_a(Items.IRON_LEGGINGS, "iron_leggings");
        this.func_175047_a(Items.IRON_BOOTS, "iron_boots");
        this.func_175047_a(Items.DIAMOND_HELMET, "diamond_helmet");
        this.func_175047_a(Items.DIAMOND_CHESTPLATE, "diamond_chestplate");
        this.func_175047_a(Items.DIAMOND_LEGGINGS, "diamond_leggings");
        this.func_175047_a(Items.DIAMOND_BOOTS, "diamond_boots");
        this.func_175047_a(Items.GOLDEN_HELMET, "golden_helmet");
        this.func_175047_a(Items.GOLDEN_CHESTPLATE, "golden_chestplate");
        this.func_175047_a(Items.GOLDEN_LEGGINGS, "golden_leggings");
        this.func_175047_a(Items.GOLDEN_BOOTS, "golden_boots");
        this.func_175047_a(Items.FLINT, "flint");
        this.func_175047_a(Items.PORKCHOP, "porkchop");
        this.func_175047_a(Items.COOKED_PORKCHOP, "cooked_porkchop");
        this.func_175047_a(Items.PAINTING, "painting");
        this.func_175047_a(Items.GOLDEN_APPLE, "golden_apple");
        this.func_175048_a(Items.GOLDEN_APPLE, 1, "golden_apple");
        this.func_175047_a(Items.field_151155_ap, "sign");
        this.func_175047_a(Items.field_179570_aq, "oak_door");
        this.func_175047_a(Items.field_179569_ar, "spruce_door");
        this.func_175047_a(Items.field_179568_as, "birch_door");
        this.func_175047_a(Items.field_179567_at, "jungle_door");
        this.func_175047_a(Items.field_179572_au, "acacia_door");
        this.func_175047_a(Items.field_179571_av, "dark_oak_door");
        this.func_175047_a(Items.BUCKET, "bucket");
        this.func_175047_a(Items.WATER_BUCKET, "water_bucket");
        this.func_175047_a(Items.LAVA_BUCKET, "lava_bucket");
        this.func_175047_a(Items.MINECART, "minecart");
        this.func_175047_a(Items.SADDLE, "saddle");
        this.func_175047_a(Items.field_151139_aw, "iron_door");
        this.func_175047_a(Items.REDSTONE, "redstone");
        this.func_175047_a(Items.SNOWBALL, "snowball");
        this.func_175047_a(Items.OAK_BOAT, "oak_boat");
        this.func_175047_a(Items.SPRUCE_BOAT, "spruce_boat");
        this.func_175047_a(Items.BIRCH_BOAT, "birch_boat");
        this.func_175047_a(Items.JUNGLE_BOAT, "jungle_boat");
        this.func_175047_a(Items.ACACIA_BOAT, "acacia_boat");
        this.func_175047_a(Items.DARK_OAK_BOAT, "dark_oak_boat");
        this.func_175047_a(Items.LEATHER, "leather");
        this.func_175047_a(Items.MILK_BUCKET, "milk_bucket");
        this.func_175047_a(Items.BRICK, "brick");
        this.func_175047_a(Items.CLAY_BALL, "clay_ball");
        this.func_175047_a(Items.field_151120_aE, "reeds");
        this.func_175047_a(Items.PAPER, "paper");
        this.func_175047_a(Items.BOOK, "book");
        this.func_175047_a(Items.SLIME_BALL, "slime_ball");
        this.func_175047_a(Items.CHEST_MINECART, "chest_minecart");
        this.func_175047_a(Items.FURNACE_MINECART, "furnace_minecart");
        this.func_175047_a(Items.EGG, "egg");
        this.func_175047_a(Items.COMPASS, "compass");
        this.func_175047_a(Items.FISHING_ROD, "fishing_rod");
        this.func_175047_a(Items.CLOCK, "clock");
        this.func_175047_a(Items.GLOWSTONE_DUST, "glowstone_dust");
        this.func_175048_a(Items.field_151115_aP, ItemFishFood.FishType.COD.func_150976_a(), "cod");
        this.func_175048_a(Items.field_151115_aP, ItemFishFood.FishType.SALMON.func_150976_a(), "salmon");
        this.func_175048_a(Items.field_151115_aP, ItemFishFood.FishType.CLOWNFISH.func_150976_a(), "clownfish");
        this.func_175048_a(Items.field_151115_aP, ItemFishFood.FishType.PUFFERFISH.func_150976_a(), "pufferfish");
        this.func_175048_a(Items.field_179566_aV, ItemFishFood.FishType.COD.func_150976_a(), "cooked_cod");
        this.func_175048_a(Items.field_179566_aV, ItemFishFood.FishType.SALMON.func_150976_a(), "cooked_salmon");
        this.func_175048_a(Items.field_151100_aR, EnumDyeColor.BLACK.func_176767_b(), "dye_black");
        this.func_175048_a(Items.field_151100_aR, EnumDyeColor.RED.func_176767_b(), "dye_red");
        this.func_175048_a(Items.field_151100_aR, EnumDyeColor.GREEN.func_176767_b(), "dye_green");
        this.func_175048_a(Items.field_151100_aR, EnumDyeColor.BROWN.func_176767_b(), "dye_brown");
        this.func_175048_a(Items.field_151100_aR, EnumDyeColor.BLUE.func_176767_b(), "dye_blue");
        this.func_175048_a(Items.field_151100_aR, EnumDyeColor.PURPLE.func_176767_b(), "dye_purple");
        this.func_175048_a(Items.field_151100_aR, EnumDyeColor.CYAN.func_176767_b(), "dye_cyan");
        this.func_175048_a(Items.field_151100_aR, EnumDyeColor.SILVER.func_176767_b(), "dye_silver");
        this.func_175048_a(Items.field_151100_aR, EnumDyeColor.GRAY.func_176767_b(), "dye_gray");
        this.func_175048_a(Items.field_151100_aR, EnumDyeColor.PINK.func_176767_b(), "dye_pink");
        this.func_175048_a(Items.field_151100_aR, EnumDyeColor.LIME.func_176767_b(), "dye_lime");
        this.func_175048_a(Items.field_151100_aR, EnumDyeColor.YELLOW.func_176767_b(), "dye_yellow");
        this.func_175048_a(Items.field_151100_aR, EnumDyeColor.LIGHT_BLUE.func_176767_b(), "dye_light_blue");
        this.func_175048_a(Items.field_151100_aR, EnumDyeColor.MAGENTA.func_176767_b(), "dye_magenta");
        this.func_175048_a(Items.field_151100_aR, EnumDyeColor.ORANGE.func_176767_b(), "dye_orange");
        this.func_175048_a(Items.field_151100_aR, EnumDyeColor.WHITE.func_176767_b(), "dye_white");
        this.func_175047_a(Items.BONE, "bone");
        this.func_175047_a(Items.SUGAR, "sugar");
        this.func_175047_a(Items.field_151105_aU, "cake");
        this.func_175047_a(Items.field_151107_aW, "repeater");
        this.func_175047_a(Items.COOKIE, "cookie");
        this.func_175047_a(Items.SHEARS, "shears");
        this.func_175047_a(Items.MELON_SLICE, "melon");
        this.func_175047_a(Items.PUMPKIN_SEEDS, "pumpkin_seeds");
        this.func_175047_a(Items.MELON_SEEDS, "melon_seeds");
        this.func_175047_a(Items.BEEF, "beef");
        this.func_175047_a(Items.COOKED_BEEF, "cooked_beef");
        this.func_175047_a(Items.CHICKEN, "chicken");
        this.func_175047_a(Items.COOKED_CHICKEN, "cooked_chicken");
        this.func_175047_a(Items.RABBIT, "rabbit");
        this.func_175047_a(Items.COOKED_RABBIT, "cooked_rabbit");
        this.func_175047_a(Items.MUTTON, "mutton");
        this.func_175047_a(Items.COOKED_MUTTON, "cooked_mutton");
        this.func_175047_a(Items.RABBIT_FOOT, "rabbit_foot");
        this.func_175047_a(Items.RABBIT_HIDE, "rabbit_hide");
        this.func_175047_a(Items.RABBIT_STEW, "rabbit_stew");
        this.func_175047_a(Items.ROTTEN_FLESH, "rotten_flesh");
        this.func_175047_a(Items.ENDER_PEARL, "ender_pearl");
        this.func_175047_a(Items.BLAZE_ROD, "blaze_rod");
        this.func_175047_a(Items.GHAST_TEAR, "ghast_tear");
        this.func_175047_a(Items.GOLD_NUGGET, "gold_nugget");
        this.func_175047_a(Items.NETHER_WART, "nether_wart");
        this.func_175047_a(Items.BEETROOT, "beetroot");
        this.func_175047_a(Items.BEETROOT_SEEDS, "beetroot_seeds");
        this.func_175047_a(Items.BEETROOT_SOUP, "beetroot_soup");
        this.func_175047_a(Items.TOTEM_OF_UNDYING, "totem");
        this.func_175047_a(Items.POTION, "bottle_drinkable");
        this.func_175047_a(Items.SPLASH_POTION, "bottle_splash");
        this.func_175047_a(Items.LINGERING_POTION, "bottle_lingering");
        this.func_175047_a(Items.GLASS_BOTTLE, "glass_bottle");
        this.func_175047_a(Items.DRAGON_BREATH, "dragon_breath");
        this.func_175047_a(Items.SPIDER_EYE, "spider_eye");
        this.func_175047_a(Items.FERMENTED_SPIDER_EYE, "fermented_spider_eye");
        this.func_175047_a(Items.BLAZE_POWDER, "blaze_powder");
        this.func_175047_a(Items.MAGMA_CREAM, "magma_cream");
        this.func_175047_a(Items.field_151067_bt, "brewing_stand");
        this.func_175047_a(Items.field_151066_bu, "cauldron");
        this.func_175047_a(Items.ENDER_EYE, "ender_eye");
        this.func_175047_a(Items.GLISTERING_MELON_SLICE, "speckled_melon");
        this.itemModelMesher.func_178080_a(Items.field_151063_bx, new ItemMeshDefinition()
        {
            public ModelResourceLocation func_178113_a(ItemStack p_178113_1_)
            {
                return new ModelResourceLocation("spawn_egg", "inventory");
            }
        });
        this.func_175047_a(Items.EXPERIENCE_BOTTLE, "experience_bottle");
        this.func_175047_a(Items.FIRE_CHARGE, "fire_charge");
        this.func_175047_a(Items.WRITABLE_BOOK, "writable_book");
        this.func_175047_a(Items.EMERALD, "emerald");
        this.func_175047_a(Items.ITEM_FRAME, "item_frame");
        this.func_175047_a(Items.field_151162_bE, "flower_pot");
        this.func_175047_a(Items.CARROT, "carrot");
        this.func_175047_a(Items.POTATO, "potato");
        this.func_175047_a(Items.BAKED_POTATO, "baked_potato");
        this.func_175047_a(Items.POISONOUS_POTATO, "poisonous_potato");
        this.func_175047_a(Items.MAP, "map");
        this.func_175047_a(Items.GOLDEN_CARROT, "golden_carrot");
        this.func_175048_a(Items.field_151144_bL, 0, "skull_skeleton");
        this.func_175048_a(Items.field_151144_bL, 1, "skull_wither");
        this.func_175048_a(Items.field_151144_bL, 2, "skull_zombie");
        this.func_175048_a(Items.field_151144_bL, 3, "skull_char");
        this.func_175048_a(Items.field_151144_bL, 4, "skull_creeper");
        this.func_175048_a(Items.field_151144_bL, 5, "skull_dragon");
        this.func_175047_a(Items.CARROT_ON_A_STICK, "carrot_on_a_stick");
        this.func_175047_a(Items.NETHER_STAR, "nether_star");
        this.func_175047_a(Items.END_CRYSTAL, "end_crystal");
        this.func_175047_a(Items.PUMPKIN_PIE, "pumpkin_pie");
        this.func_175047_a(Items.field_151154_bQ, "firework_charge");
        this.func_175047_a(Items.field_151132_bS, "comparator");
        this.func_175047_a(Items.field_151130_bT, "netherbrick");
        this.func_175047_a(Items.QUARTZ, "quartz");
        this.func_175047_a(Items.TNT_MINECART, "tnt_minecart");
        this.func_175047_a(Items.HOPPER_MINECART, "hopper_minecart");
        this.func_175047_a(Items.ARMOR_STAND, "armor_stand");
        this.func_175047_a(Items.IRON_HORSE_ARMOR, "iron_horse_armor");
        this.func_175047_a(Items.GOLDEN_HORSE_ARMOR, "golden_horse_armor");
        this.func_175047_a(Items.DIAMOND_HORSE_ARMOR, "diamond_horse_armor");
        this.func_175047_a(Items.LEAD, "lead");
        this.func_175047_a(Items.NAME_TAG, "name_tag");
        this.itemModelMesher.func_178080_a(Items.field_179564_cE, new ItemMeshDefinition()
        {
            public ModelResourceLocation func_178113_a(ItemStack p_178113_1_)
            {
                return new ModelResourceLocation("banner", "inventory");
            }
        });
        this.itemModelMesher.func_178080_a(Items.field_151104_aV, new ItemMeshDefinition()
        {
            public ModelResourceLocation func_178113_a(ItemStack p_178113_1_)
            {
                return new ModelResourceLocation("bed", "inventory");
            }
        });
        this.itemModelMesher.func_178080_a(Items.SHIELD, new ItemMeshDefinition()
        {
            public ModelResourceLocation func_178113_a(ItemStack p_178113_1_)
            {
                return new ModelResourceLocation("shield", "inventory");
            }
        });
        this.func_175047_a(Items.ELYTRA, "elytra");
        this.func_175047_a(Items.CHORUS_FRUIT, "chorus_fruit");
        this.func_175047_a(Items.POPPED_CHORUS_FRUIT, "chorus_fruit_popped");
        this.func_175047_a(Items.SHULKER_SHELL, "shulker_shell");
        this.func_175047_a(Items.IRON_NUGGET, "iron_nugget");
        this.func_175047_a(Items.field_151096_cd, "record_13");
        this.func_175047_a(Items.field_151093_ce, "record_cat");
        this.func_175047_a(Items.field_151094_cf, "record_blocks");
        this.func_175047_a(Items.field_151091_cg, "record_chirp");
        this.func_175047_a(Items.field_151092_ch, "record_far");
        this.func_175047_a(Items.field_151089_ci, "record_mall");
        this.func_175047_a(Items.field_151090_cj, "record_mellohi");
        this.func_175047_a(Items.field_151087_ck, "record_stal");
        this.func_175047_a(Items.field_151088_cl, "record_strad");
        this.func_175047_a(Items.field_151085_cm, "record_ward");
        this.func_175047_a(Items.field_151086_cn, "record_11");
        this.func_175047_a(Items.field_151084_co, "record_wait");
        this.func_175047_a(Items.PRISMARINE_SHARD, "prismarine_shard");
        this.func_175047_a(Items.PRISMARINE_CRYSTALS, "prismarine_crystals");
        this.func_175047_a(Items.KNOWLEDGE_BOOK, "knowledge_book");
        this.itemModelMesher.func_178080_a(Items.ENCHANTED_BOOK, new ItemMeshDefinition()
        {
            public ModelResourceLocation func_178113_a(ItemStack p_178113_1_)
            {
                return new ModelResourceLocation("enchanted_book", "inventory");
            }
        });
        this.itemModelMesher.func_178080_a(Items.FILLED_MAP, new ItemMeshDefinition()
        {
            public ModelResourceLocation func_178113_a(ItemStack p_178113_1_)
            {
                return new ModelResourceLocation("filled_map", "inventory");
            }
        });
        this.func_175031_a(Blocks.COMMAND_BLOCK, "command_block");
        this.func_175047_a(Items.field_151152_bP, "fireworks");
        this.func_175047_a(Items.COMMAND_BLOCK_MINECART, "command_block_minecart");
        this.func_175031_a(Blocks.BARRIER, "barrier");
        this.func_175031_a(Blocks.SPAWNER, "mob_spawner");
        this.func_175047_a(Items.WRITTEN_BOOK, "written_book");
        this.func_175029_a(Blocks.BROWN_MUSHROOM_BLOCK, BlockHugeMushroom.EnumType.ALL_INSIDE.func_176896_a(), "brown_mushroom_block");
        this.func_175029_a(Blocks.RED_MUSHROOM_BLOCK, BlockHugeMushroom.EnumType.ALL_INSIDE.func_176896_a(), "red_mushroom_block");
        this.func_175031_a(Blocks.DRAGON_EGG, "dragon_egg");
        this.func_175031_a(Blocks.REPEATING_COMMAND_BLOCK, "repeating_command_block");
        this.func_175031_a(Blocks.CHAIN_COMMAND_BLOCK, "chain_command_block");
        this.func_175029_a(Blocks.STRUCTURE_BLOCK, TileEntityStructure.Mode.SAVE.func_185110_a(), "structure_block");
        this.func_175029_a(Blocks.STRUCTURE_BLOCK, TileEntityStructure.Mode.LOAD.func_185110_a(), "structure_block");
        this.func_175029_a(Blocks.STRUCTURE_BLOCK, TileEntityStructure.Mode.CORNER.func_185110_a(), "structure_block");
        this.func_175029_a(Blocks.STRUCTURE_BLOCK, TileEntityStructure.Mode.DATA.func_185110_a(), "structure_block");
    }

    public void func_110549_a(IResourceManager p_110549_1_)
    {
        this.itemModelMesher.rebuildCache();
    }
}
