package org.examplexiaodou.playerkillcommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class PlayerKillCommands extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        getServer().getPluginManager().registerEvents(this, this);

        // 提示信息
        getLogger().info("§a插件已加载！");
        getLogger().info("§a插件作者：小豆同学");
        getLogger().info("§a定制插件联系qq：2321388357");
        getLogger().info("§a使用 /pkc reload 或 /rkc reload 重载配置文件");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        World world = victim.getWorld();

        if (!getConfig().getStringList("enabled-worlds").contains(world.getName())) {
            return;
        }

        Player killer = victim.getKiller();
        List<String> commands = getConfig().getStringList("commands");

        for (String cmd : commands) {
            cmd = cmd.replace("%player%", victim.getName())
                    .replace("%world%", world.getName())
                    .replace("%killer%", killer != null ? killer.getName() : "无人");

            cmd = ChatColor.translateAlternateColorCodes('&', cmd);

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 支持 /pkc reload 和 /rkc reload 两种命令
        if ((label.equalsIgnoreCase("pkc"))
                && args.length > 0 && args[0].equalsIgnoreCase("reload")) {

            if (sender.hasPermission("killcommands.reload")) {
                reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "配置文件已重新加载！");
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "你没有权限执行此命令！");
                return true;
            }
        }
        return false;
    }
}