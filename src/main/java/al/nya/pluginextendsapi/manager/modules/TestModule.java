package al.nya.pluginextendsapi.manager.modules;

import al.nya.pluginextendsapi.manager.ManagerConfig;
import al.nya.pluginextendsapi.manager.MiraiPluginExtendsAPIManager;
import al.nya.pluginextendsapi.modules.Module;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.FriendMessageEvent;

public class TestModule extends Module {
    public TestModule() {
        super(MiraiPluginExtendsAPIManager.INSTANCE,"TestModule");
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof FriendMessageEvent){
            if (((FriendMessageEvent) event).getSender().getId() == ManagerConfig.getOwner()){
                if (((FriendMessageEvent) event).getMessage().get(1).toString().equalsIgnoreCase("nya")){
                    ((FriendMessageEvent) event).getSender().sendMessage("nyanya");
                }
            }
        }
    }
}
