/*
  Copyright (c) 2022 Preponderous Software
  MIT License
 */
package preponderous.ponder.minecraft.spigot.tools;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

/**
 * @author Daniel McCoy Stephenson
 */
public class PermissionChecker {

    /**
     * @param sender The sender of the command.
     * @param permission The permission to check.
     * @return Whether the sender has the specified permission.
     */
    public boolean checkPermissionAndInformSenderIfTheyDoNotHavePermission(CommandSender sender, String permission) {
        if (checkPermission(sender, permission)) {
            informSenderTheyDoNotHavePermission(sender, permission);
            return false;
        }
        return true;
    }

    /**
     * @param sender The sender of the command.
     * @param permissions The permission to check.
     * @return Whether the sender has one of the specified permissions.
     */
    public boolean checkPermission(CommandSender sender, ArrayList<String> permissions) {
        for (String permission : permissions) {
            if (checkPermission(sender, permission)) {
                return true;
            }
        }
        informSenderTheyDoNotHaveOneOfTheRequiredPermissions(sender, permissions);
        return false;
    }

    /**
     * @param sender The sender of the command.
     * @param permission The permission to check.
     * @return Whether the sender has the specified permission.
     */
    public boolean checkPermission(CommandSender sender, String permission) {
        return sender.hasPermission(permission);
    }

    private void informSenderTheyDoNotHavePermission(CommandSender sender, String permission) {
        sender.sendMessage(ChatColor.RED + "In order to use this command, you need the following permission: '" + permission + "'");
    }

    private void informSenderTheyDoNotHaveOneOfTheRequiredPermissions(CommandSender sender, ArrayList<String> permissions) {
        sender.sendMessage(ChatColor.RED + "In order to use this command, you need one of the following permission: '" + permissions + "'");
    }
}