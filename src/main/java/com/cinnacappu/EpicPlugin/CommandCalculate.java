package com.cinnacappu.EpicPlugin;

import java.util.Locale;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandCalculate implements CommandExecutor {
    public CommandCalculate() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2 || args.length > 3) {
            sender.sendMessage("§cUsage: /calculate (First Number) (Operator) (Second Number)");
            return true;
        }

        String operator = args[1].toLowerCase(Locale.ROOT);
        boolean unary = operator.equals("sqrt") || operator.equals("squareroot") || operator.equals("root")
                || operator.equals("cbrt") || operator.equals("cuberoot") || operator.equals("square")
                || operator.equals("squared") || operator.equals("cube") || operator.equals("cubed");
        if (!unary && args.length != 3) {
            sender.sendMessage("§cUsage: /calculate (First Number) (Operator) (Second Number)");
            return true;
        }

        try {
            double a = Double.parseDouble(args[0]);
            double b = unary ? 0 : Double.parseDouble(args[2]);
            if (!Double.isFinite(a) || !Double.isFinite(b)) {
                sender.sendMessage("§cPlease enter finite numbers.");
                return true;
            }

            double sum;
            if (operator.equals("+")) {
                sum = a + b;
            } else if (operator.equals("-")) {
                sum = a - b;
            } else if (operator.equals("x") || operator.equals("*")) {
                sum = a * b;
            } else if (operator.equals("/")) {
                if (b == 0) {
                    sender.sendMessage("§cCannot divide by zero.");
                    return true;
                }
                sum = a / b;
            } else if (operator.equals("sqrt") || operator.equals("squareroot") || operator.equals("root")) {
                sum = Math.sqrt(a);
            } else if (operator.equals("cbrt") || operator.equals("cuberoot")) {
                sum = Math.cbrt(a);
            } else if (operator.equals("square") || operator.equals("squared")) {
                sum = Math.pow(a, 2);
            } else if (operator.equals("cube") || operator.equals("cubed")) {
                sum = Math.pow(a, 3);
            } else {
                sender.sendMessage("§cUnknown operator. Use +, -, x, *, /, sqrt, cbrt, square or cube.");
                return true;
            }

            if (!Double.isFinite(sum)) {
                sender.sendMessage("§cThat calculation does not produce a finite real number.");
                return true;
            }
            sender.sendMessage("§aAnswer = §2§l" + sum);
        } catch (NumberFormatException ex) {
            sender.sendMessage("§cPlease enter valid numbers.");
        }
        return true;
    }
}
