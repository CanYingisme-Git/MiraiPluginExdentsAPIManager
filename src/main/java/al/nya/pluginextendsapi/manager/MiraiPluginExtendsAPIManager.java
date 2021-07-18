package al.nya.pluginextendsapi.manager;

import al.nya.pluginextendsapi.manager.modules.Manager;
import al.nya.pluginextendsapi.manager.modules.TestModule;
import al.nya.pluginextendsapi.modules.ModuleManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import org.jetbrains.annotations.NotNull;

public class MiraiPluginExtendsAPIManager extends JavaPlugin {
    public static MiraiPluginExtendsAPIManager INSTANCE = new MiraiPluginExtendsAPIManager();
    public MiraiPluginExtendsAPIManager() {
        super(new JvmPluginDescriptionBuilder("al.nya.pluginextendsapi.manager", "0.1.0")
                .info("Manage MiraiPluginExtendsAPI")
                .name("MiraiPluginExtendsAPIManager").build());
    }
    @Override
    public void onEnable(){
        ManagerConfig.initConfig();
        ModuleManager.registerModule(new Manager());
        ModuleManager.registerModule(new TestModule());
    }
}
