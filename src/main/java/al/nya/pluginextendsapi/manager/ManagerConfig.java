package al.nya.pluginextendsapi.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ManagerConfig {
    private static Properties Pro = new Properties();
    public static void initConfig(){
        boolean created = false;
        File file = new File("./MiraiPluginExtendsAPIManager");
        if (!file.isDirectory()) file.mkdir();
        file = new File("./MiraiPluginExtendsAPIManager/manager.properties");
        if (!file.exists()) {
            try {
                file.createNewFile();
                created = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Pro.load(file.toURI().toURL().openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        defaultValue();
    }
    public static void defaultValue(){
        if (Pro.getProperty("botOwner","null").equals("null")){
            Pro.setProperty("botOwner","0");
            MiraiPluginExtendsAPIManager.INSTANCE.getLogger().info("警告: MiraiPluginExtendsAPIManager没有进行初始化 缺少 botOnwer 请在配置文件中修改");
        }
        if (Pro.getProperty("autoAcceptGroupInvite","null").equals("null")){
            Pro.setProperty("autoAcceptGroupInvite","true");
        }
        save();
    }
    public static boolean autoAccept(){
        if (Pro.getProperty("autoAcceptGroupInvite","true").equals("true")){
            return true;
        }
        return false;
    }
    public static long getOwner(){
        if (Pro.getProperty("botOwner","null").equals("null")){
            return 0;
        }
        return Long.parseLong(Pro.getProperty("botOwner","null"));
    }
    public static void save(){
        File file = new File("./MiraiPluginExtendsAPIManager/manager.properties");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            Pro.store(fos,"ManagerConfig");
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
