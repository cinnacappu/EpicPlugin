package com.cinnacappu.EpicPlugin;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommandInputTest {
    private final List<String> messages = new ArrayList<>();
    private final ConsoleCommandSender sender = (ConsoleCommandSender) Proxy.newProxyInstance(
            ConsoleCommandSender.class.getClassLoader(), new Class<?>[]{ConsoleCommandSender.class},
            (proxy, method, args) -> {
                if (method.getName().equals("sendMessage")) {
                    for (Object argument : args) {
                        if (argument instanceof String text) messages.add(text);
                        if (argument instanceof String[] lines) messages.addAll(List.of(lines));
                    }
                }
                if (method.getReturnType() == boolean.class) return true;
                if (method.getReturnType() == int.class) return 0;
                return null;
            });

    private String run(CommandExecutor command, String... args) {
        messages.clear();
        assertTrue(command.onCommand(sender, null, "test", args));
        assertFalse(messages.isEmpty());
        return messages.getLast();
    }

    @Test
    void consoleCalculatorSupportsOriginalOperatorsAndUnaryAliases() {
        CommandCalculate command = new CommandCalculate();
        for (String operator : new String[]{"x", "*"}) {
            assertTrue(run(command, "6", operator, "7").endsWith("42.0"));
        }
        assertTrue(run(command, "1.5", "+", "2.25").endsWith("3.75"));
        assertTrue(run(command, "9", "sqrt").endsWith("3.0"));
        assertTrue(run(command, "-8", "cuberoot").endsWith("-2.0"));
        assertTrue(run(command, "3", "squared", "0").endsWith("9.0"));
    }

    @Test
    void calculatorRejectsMalformedAndNonFiniteInputs() {
        CommandCalculate command = new CommandCalculate();
        assertTrue(run(command).contains("Usage"));
        assertTrue(run(command, "1", "+").contains("Usage"));
        assertTrue(run(command, "no", "+", "1").contains("valid numbers"));
        assertTrue(run(command, "NaN", "+", "1").contains("finite"));
        assertTrue(run(command, "1", "/", "0").contains("zero"));
        assertTrue(run(command, "-1", "sqrt").contains("finite"));
        assertTrue(run(command, "1", "+junk", "1").contains("Unknown operator"));
    }

    @Test
    void randomNumberIncludesBothIntegerEndpointsWithoutOverflow() {
        CommandRandomNumber command = new CommandRandomNumber();
        assertTrue(run(command, "2147483647", "2147483647").endsWith("2147483647"));
        assertTrue(run(command, "-2147483648", "-2147483648").endsWith("-2147483648"));
        assertTrue(run(command, "0", "0").endsWith("0"));
        for (int i = 0; i < 100; i++) {
            String output = ChatColor.stripColor(run(command, "-2147483648", "2147483647"));
            assertDoesNotThrow(() -> Integer.parseInt(output.substring(output.lastIndexOf(' ') + 1)));
        }
    }

    @Test
    void randomNumberReportsMissingInvalidAndReversedBounds() {
        CommandRandomNumber command = new CommandRandomNumber();
        assertTrue(run(command).contains("Usage"));
        assertTrue(run(command, "5", "1").contains("Minimum"));
        assertTrue(run(command, "1.5", "2").contains("whole numbers"));
        assertTrue(run(command, "0", "2147483648").contains("whole numbers"));
    }

    @Test
    void titleValidationRejectsOverflowAndFormattingAsColor() {
        assertEquals(40, CommandBroadcastTitle.secondsToTicks("2"));
        assertEquals(2147483640, CommandBroadcastTitle.secondsToTicks("107374182"));
        for (String input : new String[]{"-1", "107374183", "2147483647", "bad"}) {
            assertThrows(IllegalArgumentException.class, () -> CommandBroadcastTitle.secondsToTicks(input));
        }
        assertEquals(ChatColor.AQUA, CommandBroadcastTitle.parseColor("light_blue"));
        assertEquals("#12abef", CommandBroadcastTitle.parseColor("#12AbEf").getName());
        for (String input : new String[]{"italic", "bold", "reset", "not_a_color", "#fff"}) {
            assertThrows(IllegalArgumentException.class, () -> CommandBroadcastTitle.parseColor(input));
        }
        assertTrue(run(new CommandBroadcastTitle()).contains("Usage"));
    }

    @Test
    void titlesKeepLiteralQuotesSlashesStylesAndCorrectChannel() {
        List<Object[]> titles = new ArrayList<>();
        List<BaseComponent[]> actionbars = new ArrayList<>();
        Player.Spigot spigot = new Player.Spigot() {
            @Override
            public void sendMessage(ChatMessageType position, BaseComponent... components) {
                assertEquals(ChatMessageType.ACTION_BAR, position);
                actionbars.add(components);
            }
        };
        Player player = (Player) Proxy.newProxyInstance(Player.class.getClassLoader(), new Class<?>[]{Player.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("sendTitle")) titles.add(args);
                    if (method.getName().equals("spigot")) return spigot;
                    if (method.getReturnType() == boolean.class) return false;
                    if (method.getReturnType() == int.class) return 0;
                    return null;
                });
        String text = "A \"quoted\" title \\ with {braces}";
        String formatted = ChatColor.GOLD.toString() + ChatColor.ITALIC + text;
        CommandBroadcastTitle.displayTitle(player, "big", formatted, 20, 40, 60);
        CommandBroadcastTitle.displayTitle(player, "medium", formatted, 20, 40, 60);
        CommandBroadcastTitle.displayTitle(player, "small", formatted, 20, 40, 60);
        assertArrayEquals(new Object[]{formatted, "", 20, 40, 60}, titles.get(0));
        assertArrayEquals(new Object[]{"", formatted, 20, 40, 60}, titles.get(1));
        assertEquals(text, BaseComponent.toPlainText(actionbars.getFirst()));
        assertTrue(actionbars.getFirst()[0].isItalic());
    }

    @Test
    void barePasswordPromptAcceptsFirstEntryThenRequestsConfirmation() {
        CommandPasswd command = new CommandPasswd();
        assertTrue(run(command).contains("Enter new password"));
        assertTrue(run(command, "example-test-password").contains("Retype new password"));
        assertTrue(run(command, "different-test-password").contains("do not match"));
        assertTrue(run(command, "first", "second").contains("only one password"));
    }
}
