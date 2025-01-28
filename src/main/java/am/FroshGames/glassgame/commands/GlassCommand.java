package am.FroshGames.glassgame.commands;

import am.FroshGames.glassgame.Glassgame;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GlassCommand implements CommandExecutor {

    private final Glassgame plugin;

    public GlassCommand(Glassgame plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Por ejemplo:
        if (args.length == 0) {
            sender.sendMessage("§eUsa /glassgame [start|stop|help]");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "start":
                sender.sendMessage("§a¡Iniciando el GlassGame!");
                // Lógica para iniciar
                return true;
            case "stop":
                sender.sendMessage("§c¡Deteniendo el GlassGame!");
                // Lógica para detener
                return true;
            case "help":
                sender.sendMessage("§6Comandos de GlassGame:");
                sender.sendMessage("§6/glassgame start - Inicia el minijuego");
                sender.sendMessage("§6/glassgame stop - Detiene el minijuego");
                return true;
            default:
                sender.sendMessage("§cComando desconocido. Usa /glassgame [start|stop|help]");
                return true;
        }
    }
}