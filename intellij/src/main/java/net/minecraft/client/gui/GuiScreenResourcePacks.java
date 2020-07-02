package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.ResourcePackListEntry;
import net.minecraft.client.resources.ResourcePackListEntryDefault;
import net.minecraft.client.resources.ResourcePackListEntryFound;
import net.minecraft.client.resources.ResourcePackListEntryServer;
import net.minecraft.client.resources.ResourcePackRepository;

public class GuiScreenResourcePacks extends GuiScreen
{
    private final GuiScreen field_146965_f;
    private List<ResourcePackListEntry> field_146966_g;
    private List<ResourcePackListEntry> field_146969_h;

    /** List component that contains the available resource packs */
    private GuiResourcePackAvailable availableResourcePacksList;

    /** List component that contains the selected resource packs */
    private GuiResourcePackSelected selectedResourcePacksList;
    private boolean changed;

    public GuiScreenResourcePacks(GuiScreen p_i45050_1_)
    {
        this.field_146965_f = p_i45050_1_;
    }

    public void func_73866_w_()
    {
        this.field_146292_n.add(new GuiOptionButton(2, this.field_146294_l / 2 - 154, this.field_146295_m - 48, I18n.format("resourcePack.openFolder")));
        this.field_146292_n.add(new GuiOptionButton(1, this.field_146294_l / 2 + 4, this.field_146295_m - 48, I18n.format("gui.done")));

        if (!this.changed)
        {
            this.field_146966_g = Lists.<ResourcePackListEntry>newArrayList();
            this.field_146969_h = Lists.<ResourcePackListEntry>newArrayList();
            ResourcePackRepository resourcepackrepository = this.field_146297_k.func_110438_M();
            resourcepackrepository.func_110611_a();
            List<ResourcePackRepository.Entry> list = Lists.newArrayList(resourcepackrepository.func_110609_b());
            list.removeAll(resourcepackrepository.func_110613_c());

            for (ResourcePackRepository.Entry resourcepackrepository$entry : list)
            {
                this.field_146966_g.add(new ResourcePackListEntryFound(this, resourcepackrepository$entry));
            }

            ResourcePackRepository.Entry resourcepackrepository$entry2 = resourcepackrepository.func_188565_b();

            if (resourcepackrepository$entry2 != null)
            {
                this.field_146969_h.add(new ResourcePackListEntryServer(this, resourcepackrepository.func_148530_e()));
            }

            for (ResourcePackRepository.Entry resourcepackrepository$entry1 : Lists.reverse(resourcepackrepository.func_110613_c()))
            {
                this.field_146969_h.add(new ResourcePackListEntryFound(this, resourcepackrepository$entry1));
            }

            this.field_146969_h.add(new ResourcePackListEntryDefault(this));
        }

        this.availableResourcePacksList = new GuiResourcePackAvailable(this.field_146297_k, 200, this.field_146295_m, this.field_146966_g);
        this.availableResourcePacksList.func_148140_g(this.field_146294_l / 2 - 4 - 200);
        this.availableResourcePacksList.func_148134_d(7, 8);
        this.selectedResourcePacksList = new GuiResourcePackSelected(this.field_146297_k, 200, this.field_146295_m, this.field_146969_h);
        this.selectedResourcePacksList.func_148140_g(this.field_146294_l / 2 + 4);
        this.selectedResourcePacksList.func_148134_d(7, 8);
    }

    public void func_146274_d() throws IOException
    {
        super.func_146274_d();
        this.selectedResourcePacksList.func_178039_p();
        this.availableResourcePacksList.func_178039_p();
    }

    public boolean func_146961_a(ResourcePackListEntry p_146961_1_)
    {
        return this.field_146969_h.contains(p_146961_1_);
    }

    public List<ResourcePackListEntry> func_146962_b(ResourcePackListEntry p_146962_1_)
    {
        return this.func_146961_a(p_146962_1_) ? this.field_146969_h : this.field_146966_g;
    }

    public List<ResourcePackListEntry> func_146964_g()
    {
        return this.field_146966_g;
    }

    public List<ResourcePackListEntry> func_146963_h()
    {
        return this.field_146969_h;
    }

    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        if (p_146284_1_.field_146124_l)
        {
            if (p_146284_1_.field_146127_k == 2)
            {
                File file1 = this.field_146297_k.func_110438_M().func_110612_e();
                OpenGlHelper.func_188786_a(file1);
            }
            else if (p_146284_1_.field_146127_k == 1)
            {
                if (this.changed)
                {
                    List<ResourcePackRepository.Entry> list = Lists.<ResourcePackRepository.Entry>newArrayList();

                    for (ResourcePackListEntry resourcepacklistentry : this.field_146969_h)
                    {
                        if (resourcepacklistentry instanceof ResourcePackListEntryFound)
                        {
                            list.add(((ResourcePackListEntryFound)resourcepacklistentry).func_148318_i());
                        }
                    }

                    Collections.reverse(list);
                    this.field_146297_k.func_110438_M().func_148527_a(list);
                    this.field_146297_k.gameSettings.resourcePacks.clear();
                    this.field_146297_k.gameSettings.incompatibleResourcePacks.clear();

                    for (ResourcePackRepository.Entry resourcepackrepository$entry : list)
                    {
                        this.field_146297_k.gameSettings.resourcePacks.add(resourcepackrepository$entry.func_110515_d());

                        if (resourcepackrepository$entry.func_183027_f() != 3)
                        {
                            this.field_146297_k.gameSettings.incompatibleResourcePacks.add(resourcepackrepository$entry.func_110515_d());
                        }
                    }

                    this.field_146297_k.gameSettings.saveOptions();
                    this.field_146297_k.func_110436_a();
                }

                this.field_146297_k.displayGuiScreen(this.field_146965_f);
            }
        }
    }

    protected void func_73864_a(int p_73864_1_, int p_73864_2_, int p_73864_3_) throws IOException
    {
        super.func_73864_a(p_73864_1_, p_73864_2_, p_73864_3_);
        this.availableResourcePacksList.func_148179_a(p_73864_1_, p_73864_2_, p_73864_3_);
        this.selectedResourcePacksList.func_148179_a(p_73864_1_, p_73864_2_, p_73864_3_);
    }

    protected void func_146286_b(int p_146286_1_, int p_146286_2_, int p_146286_3_)
    {
        super.func_146286_b(p_146286_1_, p_146286_2_, p_146286_3_);
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_146278_c(0);
        this.availableResourcePacksList.func_148128_a(p_73863_1_, p_73863_2_, p_73863_3_);
        this.selectedResourcePacksList.func_148128_a(p_73863_1_, p_73863_2_, p_73863_3_);
        this.func_73732_a(this.field_146289_q, I18n.format("resourcePack.title"), this.field_146294_l / 2, 16, 16777215);
        this.func_73732_a(this.field_146289_q, I18n.format("resourcePack.folderInfo"), this.field_146294_l / 2 - 77, this.field_146295_m - 26, 8421504);
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    /**
     * Marks the selected resource packs list as changed to trigger a resource reload when the screen is closed
     */
    public void markChanged()
    {
        this.changed = true;
    }
}
