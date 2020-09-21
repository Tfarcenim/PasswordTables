package tfar.passwordtables;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = PasswordTables.MODID, name = PasswordTables.NAME, version = PasswordTables.VERSION)
public class PasswordTables {
    public static final String MODID = "passwordtables";
    public static final String NAME = "Password Tables";
    public static final String VERSION = "@VERSION@";

    public static PasswordTables instance;

    public PasswordTables() {
        instance = this;
    }

    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this,new GuiHandler());
        PacketHandler.registerMessages(MODID);
    }
}
