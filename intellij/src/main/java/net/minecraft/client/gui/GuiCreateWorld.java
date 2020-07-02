package net.minecraft.client.gui;

import java.io.IOException;
import java.util.Random;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

public class GuiCreateWorld extends GuiScreen
{
    private final GuiScreen parentScreen;
    private GuiTextField worldNameField;
    private GuiTextField worldSeedField;
    private String saveDirName;
    private String field_146342_r = "survival";
    private String field_175300_s;
    private boolean generateStructuresEnabled = true;

    /** If cheats are allowed */
    private boolean allowCheats;

    /**
     * User explicitly clicked "Allow Cheats" at some point
     * Prevents value changes due to changing game mode
     */
    private boolean allowCheatsWasSetByUser;
    private boolean bonusChestEnabled;

    /** Set to true when "hardcore" is the currently-selected gamemode */
    private boolean hardCoreMode;
    private boolean alreadyGenerated;
    private boolean inMoreWorldOptionsDisplay;
    private GuiButton btnGameMode;
    private GuiButton btnMoreOptions;
    private GuiButton btnMapFeatures;
    private GuiButton btnBonusItems;
    private GuiButton btnMapType;
    private GuiButton btnAllowCommands;
    private GuiButton btnCustomizeType;
    private String gameModeDesc1;
    private String gameModeDesc2;
    private String worldSeed;
    private String worldName;
    private int selectedIndex;
    public String chunkProviderSettingsJson = "";
    private static final String[] field_146327_L = new String[] {"CON", "COM", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"};

    public GuiCreateWorld(GuiScreen p_i46320_1_)
    {
        this.parentScreen = p_i46320_1_;
        this.worldSeed = "";
        this.worldName = I18n.format("selectWorld.newWorld");
    }

    public void func_73876_c()
    {
        this.worldNameField.tick();
        this.worldSeedField.tick();
    }

    public void func_73866_w_()
    {
        Keyboard.enableRepeatEvents(true);
        this.field_146292_n.clear();
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 155, this.field_146295_m - 28, 150, 20, I18n.format("selectWorld.create")));
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 + 5, this.field_146295_m - 28, 150, 20, I18n.format("gui.cancel")));
        this.btnGameMode = this.func_189646_b(new GuiButton(2, this.field_146294_l / 2 - 75, 115, 150, 20, I18n.format("selectWorld.gameMode")));
        this.btnMoreOptions = this.func_189646_b(new GuiButton(3, this.field_146294_l / 2 - 75, 187, 150, 20, I18n.format("selectWorld.moreWorldOptions")));
        this.btnMapFeatures = this.func_189646_b(new GuiButton(4, this.field_146294_l / 2 - 155, 100, 150, 20, I18n.format("selectWorld.mapFeatures")));
        this.btnMapFeatures.field_146125_m = false;
        this.btnBonusItems = this.func_189646_b(new GuiButton(7, this.field_146294_l / 2 + 5, 151, 150, 20, I18n.format("selectWorld.bonusItems")));
        this.btnBonusItems.field_146125_m = false;
        this.btnMapType = this.func_189646_b(new GuiButton(5, this.field_146294_l / 2 + 5, 100, 150, 20, I18n.format("selectWorld.mapType")));
        this.btnMapType.field_146125_m = false;
        this.btnAllowCommands = this.func_189646_b(new GuiButton(6, this.field_146294_l / 2 - 155, 151, 150, 20, I18n.format("selectWorld.allowCommands")));
        this.btnAllowCommands.field_146125_m = false;
        this.btnCustomizeType = this.func_189646_b(new GuiButton(8, this.field_146294_l / 2 + 5, 120, 150, 20, I18n.format("selectWorld.customizeType")));
        this.btnCustomizeType.field_146125_m = false;
        this.worldNameField = new GuiTextField(9, this.field_146289_q, this.field_146294_l / 2 - 100, 60, 200, 20);
        this.worldNameField.setFocused2(true);
        this.worldNameField.setText(this.worldName);
        this.worldSeedField = new GuiTextField(10, this.field_146289_q, this.field_146294_l / 2 - 100, 60, 200, 20);
        this.worldSeedField.setText(this.worldSeed);
        this.showMoreWorldOptions(this.inMoreWorldOptionsDisplay);
        this.calcSaveDirName();
        this.func_146319_h();
    }

    /**
     * Determine a save-directory name from the world name
     */
    private void calcSaveDirName()
    {
        this.saveDirName = this.worldNameField.getText().trim();

        for (char c0 : ChatAllowedCharacters.ILLEGAL_FILE_CHARACTERS)
        {
            this.saveDirName = this.saveDirName.replace(c0, '_');
        }

        if (StringUtils.isEmpty(this.saveDirName))
        {
            this.saveDirName = "World";
        }

        this.saveDirName = func_146317_a(this.field_146297_k.getSaveLoader(), this.saveDirName);
    }

    private void func_146319_h()
    {
        this.btnGameMode.field_146126_j = I18n.format("selectWorld.gameMode") + ": " + I18n.format("selectWorld.gameMode." + this.field_146342_r);
        this.gameModeDesc1 = I18n.format("selectWorld.gameMode." + this.field_146342_r + ".line1");
        this.gameModeDesc2 = I18n.format("selectWorld.gameMode." + this.field_146342_r + ".line2");
        this.btnMapFeatures.field_146126_j = I18n.format("selectWorld.mapFeatures") + " ";

        if (this.generateStructuresEnabled)
        {
            this.btnMapFeatures.field_146126_j = this.btnMapFeatures.field_146126_j + I18n.format("options.on");
        }
        else
        {
            this.btnMapFeatures.field_146126_j = this.btnMapFeatures.field_146126_j + I18n.format("options.off");
        }

        this.btnBonusItems.field_146126_j = I18n.format("selectWorld.bonusItems") + " ";

        if (this.bonusChestEnabled && !this.hardCoreMode)
        {
            this.btnBonusItems.field_146126_j = this.btnBonusItems.field_146126_j + I18n.format("options.on");
        }
        else
        {
            this.btnBonusItems.field_146126_j = this.btnBonusItems.field_146126_j + I18n.format("options.off");
        }

        this.btnMapType.field_146126_j = I18n.format("selectWorld.mapType") + " " + I18n.format(WorldType.WORLD_TYPES[this.selectedIndex].getTranslationKey());
        this.btnAllowCommands.field_146126_j = I18n.format("selectWorld.allowCommands") + " ";

        if (this.allowCheats && !this.hardCoreMode)
        {
            this.btnAllowCommands.field_146126_j = this.btnAllowCommands.field_146126_j + I18n.format("options.on");
        }
        else
        {
            this.btnAllowCommands.field_146126_j = this.btnAllowCommands.field_146126_j + I18n.format("options.off");
        }
    }

    public static String func_146317_a(ISaveFormat p_146317_0_, String p_146317_1_)
    {
        p_146317_1_ = p_146317_1_.replaceAll("[\\./\"]", "_");

        for (String s : field_146327_L)
        {
            if (p_146317_1_.equalsIgnoreCase(s))
            {
                p_146317_1_ = "_" + p_146317_1_ + "_";
            }
        }

        while (p_146317_0_.getWorldInfo(p_146317_1_) != null)
        {
            p_146317_1_ = p_146317_1_ + "-";
        }

        return p_146317_1_;
    }

    public void func_146281_b()
    {
        Keyboard.enableRepeatEvents(false);
    }

    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        if (p_146284_1_.field_146124_l)
        {
            if (p_146284_1_.field_146127_k == 1)
            {
                this.field_146297_k.displayGuiScreen(this.parentScreen);
            }
            else if (p_146284_1_.field_146127_k == 0)
            {
                this.field_146297_k.displayGuiScreen((GuiScreen)null);

                if (this.alreadyGenerated)
                {
                    return;
                }

                this.alreadyGenerated = true;
                long i = (new Random()).nextLong();
                String s = this.worldSeedField.getText();

                if (!StringUtils.isEmpty(s))
                {
                    try
                    {
                        long j = Long.parseLong(s);

                        if (j != 0L)
                        {
                            i = j;
                        }
                    }
                    catch (NumberFormatException var7)
                    {
                        i = (long)s.hashCode();
                    }
                }

                WorldSettings worldsettings = new WorldSettings(i, GameType.getByName(this.field_146342_r), this.generateStructuresEnabled, this.hardCoreMode, WorldType.WORLD_TYPES[this.selectedIndex]);
                worldsettings.func_82750_a(this.chunkProviderSettingsJson);

                if (this.bonusChestEnabled && !this.hardCoreMode)
                {
                    worldsettings.enableBonusChest();
                }

                if (this.allowCheats && !this.hardCoreMode)
                {
                    worldsettings.enableCommands();
                }

                this.field_146297_k.launchIntegratedServer(this.saveDirName, this.worldNameField.getText().trim(), worldsettings);
            }
            else if (p_146284_1_.field_146127_k == 3)
            {
                this.toggleMoreWorldOptions();
            }
            else if (p_146284_1_.field_146127_k == 2)
            {
                if ("survival".equals(this.field_146342_r))
                {
                    if (!this.allowCheatsWasSetByUser)
                    {
                        this.allowCheats = false;
                    }

                    this.hardCoreMode = false;
                    this.field_146342_r = "hardcore";
                    this.hardCoreMode = true;
                    this.btnAllowCommands.field_146124_l = false;
                    this.btnBonusItems.field_146124_l = false;
                    this.func_146319_h();
                }
                else if ("hardcore".equals(this.field_146342_r))
                {
                    if (!this.allowCheatsWasSetByUser)
                    {
                        this.allowCheats = true;
                    }

                    this.hardCoreMode = false;
                    this.field_146342_r = "creative";
                    this.func_146319_h();
                    this.hardCoreMode = false;
                    this.btnAllowCommands.field_146124_l = true;
                    this.btnBonusItems.field_146124_l = true;
                }
                else
                {
                    if (!this.allowCheatsWasSetByUser)
                    {
                        this.allowCheats = false;
                    }

                    this.field_146342_r = "survival";
                    this.func_146319_h();
                    this.btnAllowCommands.field_146124_l = true;
                    this.btnBonusItems.field_146124_l = true;
                    this.hardCoreMode = false;
                }

                this.func_146319_h();
            }
            else if (p_146284_1_.field_146127_k == 4)
            {
                this.generateStructuresEnabled = !this.generateStructuresEnabled;
                this.func_146319_h();
            }
            else if (p_146284_1_.field_146127_k == 7)
            {
                this.bonusChestEnabled = !this.bonusChestEnabled;
                this.func_146319_h();
            }
            else if (p_146284_1_.field_146127_k == 5)
            {
                ++this.selectedIndex;

                if (this.selectedIndex >= WorldType.WORLD_TYPES.length)
                {
                    this.selectedIndex = 0;
                }

                while (!this.canSelectCurWorldType())
                {
                    ++this.selectedIndex;

                    if (this.selectedIndex >= WorldType.WORLD_TYPES.length)
                    {
                        this.selectedIndex = 0;
                    }
                }

                this.chunkProviderSettingsJson = "";
                this.func_146319_h();
                this.showMoreWorldOptions(this.inMoreWorldOptionsDisplay);
            }
            else if (p_146284_1_.field_146127_k == 6)
            {
                this.allowCheatsWasSetByUser = true;
                this.allowCheats = !this.allowCheats;
                this.func_146319_h();
            }
            else if (p_146284_1_.field_146127_k == 8)
            {
                if (WorldType.WORLD_TYPES[this.selectedIndex] == WorldType.FLAT)
                {
                    this.field_146297_k.displayGuiScreen(new GuiCreateFlatWorld(this, this.chunkProviderSettingsJson));
                }
                else
                {
                    this.field_146297_k.displayGuiScreen(new GuiCustomizeWorldScreen(this, this.chunkProviderSettingsJson));
                }
            }
        }
    }

    /**
     * Returns whether the currently-selected world type is actually acceptable for selection
     * Used to hide the "debug" world type unless the shift key is depressed.
     */
    private boolean canSelectCurWorldType()
    {
        WorldType worldtype = WorldType.WORLD_TYPES[this.selectedIndex];

        if (worldtype != null && worldtype.canBeCreated())
        {
            return worldtype == WorldType.DEBUG_ALL_BLOCK_STATES ? func_146272_n() : true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Toggles between initial world-creation display, and "more options" display.
     * Called when user clicks "More World Options..." or "Done" (same button, different labels depending on current
     * display).
     */
    private void toggleMoreWorldOptions()
    {
        this.showMoreWorldOptions(!this.inMoreWorldOptionsDisplay);
    }

    /**
     * Shows additional world-creation options if toggle is true, otherwise shows main world-creation elements
     */
    private void showMoreWorldOptions(boolean toggle)
    {
        this.inMoreWorldOptionsDisplay = toggle;

        if (WorldType.WORLD_TYPES[this.selectedIndex] == WorldType.DEBUG_ALL_BLOCK_STATES)
        {
            this.btnGameMode.field_146125_m = !this.inMoreWorldOptionsDisplay;
            this.btnGameMode.field_146124_l = false;

            if (this.field_175300_s == null)
            {
                this.field_175300_s = this.field_146342_r;
            }

            this.field_146342_r = "spectator";
            this.btnMapFeatures.field_146125_m = false;
            this.btnBonusItems.field_146125_m = false;
            this.btnMapType.field_146125_m = this.inMoreWorldOptionsDisplay;
            this.btnAllowCommands.field_146125_m = false;
            this.btnCustomizeType.field_146125_m = false;
        }
        else
        {
            this.btnGameMode.field_146125_m = !this.inMoreWorldOptionsDisplay;
            this.btnGameMode.field_146124_l = true;

            if (this.field_175300_s != null)
            {
                this.field_146342_r = this.field_175300_s;
                this.field_175300_s = null;
            }

            this.btnMapFeatures.field_146125_m = this.inMoreWorldOptionsDisplay && WorldType.WORLD_TYPES[this.selectedIndex] != WorldType.CUSTOMIZED;
            this.btnBonusItems.field_146125_m = this.inMoreWorldOptionsDisplay;
            this.btnMapType.field_146125_m = this.inMoreWorldOptionsDisplay;
            this.btnAllowCommands.field_146125_m = this.inMoreWorldOptionsDisplay;
            this.btnCustomizeType.field_146125_m = this.inMoreWorldOptionsDisplay && (WorldType.WORLD_TYPES[this.selectedIndex] == WorldType.FLAT || WorldType.WORLD_TYPES[this.selectedIndex] == WorldType.CUSTOMIZED);
        }

        this.func_146319_h();

        if (this.inMoreWorldOptionsDisplay)
        {
            this.btnMoreOptions.field_146126_j = I18n.format("gui.done");
        }
        else
        {
            this.btnMoreOptions.field_146126_j = I18n.format("selectWorld.moreWorldOptions");
        }
    }

    protected void func_73869_a(char p_73869_1_, int p_73869_2_) throws IOException
    {
        if (this.worldNameField.func_146206_l() && !this.inMoreWorldOptionsDisplay)
        {
            this.worldNameField.func_146201_a(p_73869_1_, p_73869_2_);
            this.worldName = this.worldNameField.getText();
        }
        else if (this.worldSeedField.func_146206_l() && this.inMoreWorldOptionsDisplay)
        {
            this.worldSeedField.func_146201_a(p_73869_1_, p_73869_2_);
            this.worldSeed = this.worldSeedField.getText();
        }

        if (p_73869_2_ == 28 || p_73869_2_ == 156)
        {
            this.func_146284_a(this.field_146292_n.get(0));
        }

        (this.field_146292_n.get(0)).field_146124_l = !this.worldNameField.getText().isEmpty();
        this.calcSaveDirName();
    }

    protected void func_73864_a(int p_73864_1_, int p_73864_2_, int p_73864_3_) throws IOException
    {
        super.func_73864_a(p_73864_1_, p_73864_2_, p_73864_3_);

        if (this.inMoreWorldOptionsDisplay)
        {
            this.worldSeedField.func_146192_a(p_73864_1_, p_73864_2_, p_73864_3_);
        }
        else
        {
            this.worldNameField.func_146192_a(p_73864_1_, p_73864_2_, p_73864_3_);
        }
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_146276_q_();
        this.func_73732_a(this.field_146289_q, I18n.format("selectWorld.create"), this.field_146294_l / 2, 20, -1);

        if (this.inMoreWorldOptionsDisplay)
        {
            this.func_73731_b(this.field_146289_q, I18n.format("selectWorld.enterSeed"), this.field_146294_l / 2 - 100, 47, -6250336);
            this.func_73731_b(this.field_146289_q, I18n.format("selectWorld.seedInfo"), this.field_146294_l / 2 - 100, 85, -6250336);

            if (this.btnMapFeatures.field_146125_m)
            {
                this.func_73731_b(this.field_146289_q, I18n.format("selectWorld.mapFeatures.info"), this.field_146294_l / 2 - 150, 122, -6250336);
            }

            if (this.btnAllowCommands.field_146125_m)
            {
                this.func_73731_b(this.field_146289_q, I18n.format("selectWorld.allowCommands.info"), this.field_146294_l / 2 - 150, 172, -6250336);
            }

            this.worldSeedField.func_146194_f();

            if (WorldType.WORLD_TYPES[this.selectedIndex].hasInfoNotice())
            {
                this.field_146289_q.drawSplitString(I18n.format(WorldType.WORLD_TYPES[this.selectedIndex].getInfoTranslationKey()), this.btnMapType.field_146128_h + 2, this.btnMapType.field_146129_i + 22, this.btnMapType.func_146117_b(), 10526880);
            }
        }
        else
        {
            this.func_73731_b(this.field_146289_q, I18n.format("selectWorld.enterName"), this.field_146294_l / 2 - 100, 47, -6250336);
            this.func_73731_b(this.field_146289_q, I18n.format("selectWorld.resultFolder") + " " + this.saveDirName, this.field_146294_l / 2 - 100, 85, -6250336);
            this.worldNameField.func_146194_f();
            this.func_73731_b(this.field_146289_q, this.gameModeDesc1, this.field_146294_l / 2 - 100, 137, -6250336);
            this.func_73731_b(this.field_146289_q, this.gameModeDesc2, this.field_146294_l / 2 - 100, 149, -6250336);
        }

        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    /**
     * Set the initial values of a new world to create, from the values from an existing world.
     *  
     * Called after construction when a user selects the "Recreate" button.
     */
    public void recreateFromExistingWorld(WorldInfo original)
    {
        this.worldName = I18n.format("selectWorld.newWorld.copyOf", original.getWorldName());
        this.worldSeed = original.getSeed() + "";
        this.selectedIndex = original.getGenerator().getId();
        this.chunkProviderSettingsJson = original.func_82571_y();
        this.generateStructuresEnabled = original.isMapFeaturesEnabled();
        this.allowCheats = original.areCommandsAllowed();

        if (original.isHardcore())
        {
            this.field_146342_r = "hardcore";
        }
        else if (original.getGameType().isSurvivalOrAdventure())
        {
            this.field_146342_r = "survival";
        }
        else if (original.getGameType().isCreative())
        {
            this.field_146342_r = "creative";
        }
    }
}
