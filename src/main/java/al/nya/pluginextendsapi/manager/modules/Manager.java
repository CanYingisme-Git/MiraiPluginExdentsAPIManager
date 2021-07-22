package al.nya.pluginextendsapi.manager.modules;

import al.nya.pluginextendsapi.event.EventManager;
import al.nya.pluginextendsapi.manager.ManagerConfig;
import al.nya.pluginextendsapi.manager.MiraiPluginExtendsAPIManager;
import al.nya.pluginextendsapi.modules.Module;
import al.nya.pluginextendsapi.modules.ModuleManager;
import al.nya.pluginextendsapi.modules.Priority;
import com.sun.management.OperatingSystemMXBean;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.lang.management.ManagementFactory;
import java.util.List;

public class Manager extends Module {
    public Manager() {
        super(MiraiPluginExtendsAPIManager.INSTANCE,"Manager",Priority.Highest);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof GroupMessageEvent){
            String message = ((GroupMessageEvent) event).getMessage().get(1).toString();
            if (message.split("")[0].equals("/")){
                String[] commands = message.split(" ");
                if (commands[0].equalsIgnoreCase("/manager")){
                    if (commands.length == 2){
                        if (commands[1].equalsIgnoreCase("modules")){
                            if (((GroupMessageEvent) event).getSender().getId() == ManagerConfig.getOwner()) sendModules((GroupMessageEvent) event);
                        }else if (commands[1].equalsIgnoreCase("help")){
                            if (((GroupMessageEvent) event).getSender().getId() == ManagerConfig.getOwner()) sendHelp((GroupMessageEvent) event);
                        }else if (commands[1].equalsIgnoreCase("status")){
                            if (((GroupMessageEvent) event).getSender().getId() == ManagerConfig.getOwner()) sendStatus((GroupMessageEvent) event);
                        }else if (commands[1].equalsIgnoreCase("aept")){
                            if (((GroupMessageEvent) event).getSender().getId() == ManagerConfig.getOwner()) sendAEPT((GroupMessageEvent) event);
                        }
                    }else if (commands.length == 3){
                        if (commands[1].equalsIgnoreCase("enable")){
                            if (((GroupMessageEvent) event).getSender().getId() == ManagerConfig.getOwner()) enableModules((GroupMessageEvent) event,commands[2]);
                        }else if(commands[1].equalsIgnoreCase("disable")){
                            if (((GroupMessageEvent) event).getSender().getId() == ManagerConfig.getOwner()) disableModules((GroupMessageEvent) event,commands[2]);
                        }
                    }
                }
            }
        }else if(event instanceof BotInvitedJoinGroupRequestEvent){
            if (ManagerConfig.autoAccept())
            ((BotInvitedJoinGroupRequestEvent) event).accept();
        }
    }
    private void sendAEPT(GroupMessageEvent event){
        StringBuilder sb = new StringBuilder();
        sb.append("事件相应信息:+\n");
        sb.append("一共有");
        sb.append(ModuleManager.getModules().size());
        sb.append("个模块被加载\n");
        int d = 0;
        int e = 0;
        for (Module module : ModuleManager.getModules()) {
            if (module.isEnable()){
                e += 1;
            }else {
                d += 1;
            }
        }
        sb.append(e);
        sb.append("启用");
        sb.append(d);
        sb.append("禁用\n");
        sb.append("事件处理次数:");
        sb.append(EventManager.gtetEventProcessingTimes());
        sb.append("\n平均处理时间");
        sb.append(EventManager.getAverageEventProcessingTime());
        event.getGroup().sendMessage(sb.toString());
    }
    private void sendStatus(GroupMessageEvent event){
        String str = "";
        long vmFree = 0;
        long vmUse = 0;
        long vmTotal = 0;
        long vmMax = 0;
        double mb = 1024 * 1024 * 1.0;
        int byteToMb = 1024 * 1024;
        Runtime rt = Runtime.getRuntime();
        vmTotal = rt.totalMemory() / byteToMb;
        vmFree = rt.freeMemory() / byteToMb;
        vmMax = rt.maxMemory() / byteToMb;
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory
                         .getOperatingSystemMXBean();
                 String osName = System.getProperty("os.name");
                 double totalMemorySize = osmxb.getTotalPhysicalMemorySize() / mb;
                 double freeMemorySize = osmxb.getFreePhysicalMemorySize() / mb;
                 double usedMemorySize = (osmxb.getTotalPhysicalMemorySize() - osmxb
                         .getFreePhysicalMemorySize()) / mb;
        double cpuLoad = osmxb.getSystemCpuLoad();
        int percentCpuLoad = (int) (cpuLoad * 100);
        str = str + "Manager版本:"+MiraiPluginExtendsAPIManager.INSTANCE.getDescription().getVersion();
        str = str + "\n操作系统:"+JudgeSystem();
        str = str + "\n操作系统全名:"+System.getProperty("os.name");
        str = str + "\nJvm内存:"+vmTotal+"MB/"+vmMax + "MB("+(vmTotal/vmMax * 100)+"%)";
        str = str + "\n物理内存:"+(int)usedMemorySize+"MB/"+(int)totalMemorySize + "MB("+(int)(usedMemorySize/totalMemorySize * 100)+"%)";
        str = str + "\nCPU:"+percentCpuLoad+"%";
        /*if (isLinux()){
            str = str + "\n温度:"+;
        }else {
            str = str + "\n温度:不支持的操作系统";
        }*/
        event.getGroup().sendMessage(str);

    }
    private void sendModules(GroupMessageEvent event){
        List<Module> modules = ModuleManager.getModules();
        String str = "";
        str = str + "当前共有"+modules.size()+"模块被加载";
        for (Module module : modules) {
            str = str + "\n" +module.getName() + " 来源插件:"+module.getPlugin().getDescription().getName()+" 启用:"+module.isEnable();
        }
        event.getGroup().sendMessage(str);
    }
    private void sendHelp(GroupMessageEvent event){
        List<Module> modules = ModuleManager.getModules();
        String str = "";
        str = str +"\n"+"/manager modules 获取全部模块信息";
        str = str +"\n"+"/manager enable [模块名称] 获取全部模块信息";
        str = str +"\n"+ "/manager disable [模块名称] 获取全部模块信息";
        event.getGroup().sendMessage(str);
    }
    private void enableModules(GroupMessageEvent event,String name){
        if (findModule(name) == null){
            event.getGroup().sendMessage("无法找到模块:"+name);
        }else {
            findModule(name).setEnable(true);
            event.getGroup().sendMessage("已设置模块:"+name+"状态为"+findModule(name).isEnable());
        }
    }
    private void disableModules(GroupMessageEvent event,String name){
        if (findModule(name) == null){
            event.getGroup().sendMessage("无法找到模块:"+name);
        }else {
            findModule(name).setEnable(false);
            event.getGroup().sendMessage("已设置模块:"+name+"状态为"+findModule(name).isEnable());
        }
    }
    private Module findModule(String name){
        for (Module module : ModuleManager.getModules()) {
            if (module.getName().equalsIgnoreCase(name)) return  module;
        }
        return null;
    }
    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    public String JudgeSystem() {
        if (isLinux()) {
            return "Linux";
        } else if (isWindows()) {
            return "Windows";
        } else {
            return "Other system";
        }
    }
}
