package net.minecraft.util.datafix.fixes;

import java.util.Random;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ZombieProfToType implements IFixableData
{
    private static final Random RANDOM = new Random();

    public int func_188216_a()
    {
        return 502;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound p_188217_1_)
    {
        if ("Zombie".equals(p_188217_1_.getString("id")) && p_188217_1_.getBoolean("IsVillager"))
        {
            if (!p_188217_1_.contains("ZombieType", 99))
            {
                int i = -1;

                if (p_188217_1_.contains("VillagerProfession", 99))
                {
                    try
                    {
                        i = this.getVillagerProfession(p_188217_1_.getInt("VillagerProfession"));
                    }
                    catch (RuntimeException var4)
                    {
                        ;
                    }
                }

                if (i == -1)
                {
                    i = this.getVillagerProfession(RANDOM.nextInt(6));
                }

                p_188217_1_.putInt("ZombieType", i);
            }

            p_188217_1_.remove("IsVillager");
        }

        return p_188217_1_;
    }

    private int getVillagerProfession(int p_191277_1_)
    {
        return p_191277_1_ >= 0 && p_191277_1_ < 6 ? p_191277_1_ : -1;
    }
}
