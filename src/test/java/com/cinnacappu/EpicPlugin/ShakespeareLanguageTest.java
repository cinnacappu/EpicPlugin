package com.cinnacappu.EpicPlugin;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ShakespeareLanguageTest {

    private final CommandShakespeare command = new CommandShakespeare((Plugin) Proxy.newProxyInstance(
            Plugin.class.getClassLoader(), new Class<?>[]{Plugin.class},
            (proxy, method, args) -> { throw new UnsupportedOperationException(method.getName()); }));

    @ParameterizedTest
    @MethodSource("originalTranslations")
    void retainsOriginalTranslations(String input, String expected) throws Exception {
        assertEquals(expected, transform(input));
    }

    static Stream<Arguments> originalTranslations() {
        return Stream.of(
                Arguments.of("you will talk about money at the shop", "thou shalt utter about shilling at the bazaar"),
                Arguments.of("you You YOU yOu; talks Talks TALKS tAlKs", "thou Thou THOU yOu; utters Utters UTTERS tAlKs"),
                Arguments.of("will not; Will not; WILL NOT; thank you; Thank you; THANK YOU",
                        "shalt not; Shalt not; SHALT NOT; thank thou; Thank thou; THANK THOU"),
                Arguments.of("ayou youz _you_ 1you2 éyoué 你you你 [you] you_you",
                        "ayou youz _thou_ 1thou2 éthoué 你thou你 [thou] thou_thou"),
                Arguments.of("a你b 你你 é你é _你_ 😀你😀", "a你b 汝汝 é汝é _汝_ 😀汝😀"),
                Arguments.of("it's its It's Its IT'S ITS; won't Won't WON'T; y'all Y'all Y'ALL yalls",
                        "'tis 'tis 'tis 'tis 'TIS 'TIS; shalt not Shalt not SHALT NOT; you You YOU yours"),
                Arguments.of("man men woman women city cities fish fishes",
                        "good sir good sirs lady ladies kingdom kingdoms sea-creature sea-creatures"),
                Arguments.of("you-you you'you youyou you_you; 你你", "thou-thou thou'thou youyou thou_thou; 汝汝"),
                Arguments.of("shakespeare have Shakespeare Have SHAKESPEARE HAVE",
                        "the greatest poet to have ever existed hath The greatest poet to have ever existed Hath THE GREATEST POET TO HAVE EVER EXISTED HATH"),
                Arguments.of("when how wtf what when", "at what manner time in what way what sorcery what manner at what manner time"),
                Arguments.of("\t_you_\\money$shop[]{you}(you)\r\n", "\t_thou_\\shilling$bazaar[]{thou}(thou)\r\n"),
                Arguments.of("xyzzy qqq 1234567890\t\r\n😀", "xyzzy qqq 1234567890\t\r\n😀"));
    }

    @Test
    void everyUtf16NeighborRetainsTheOriginalAsciiLetterBoundary() throws Exception {
        Pattern original = Pattern.compile("(?<![A-Za-z])" + Pattern.quote("you") + "(?![A-Za-z])");
        for (int character = Character.MIN_VALUE; character <= Character.MAX_VALUE; character++) {
            String input = (char) character + "you" + (char) character;
            assertEquals(original.matcher(input).replaceAll("thou"), transform(input), "neighbor=" + character);
        }
    }

    @ParameterizedTest
    @MethodSource("originalLore")
    void retainsSeededLoreAndRandomSequence(long seed, String expected, long nextRandom) throws Exception {
        Random random = random();
        random.setSeed(seed);
        Method method = CommandShakespeare.class.getDeclaredMethod("transformWithLore", String.class);
        method.setAccessible(true);
        assertEquals(expected, method.invoke(command, "you will talk money"));
        assertEquals(nextRandom, random.nextLong());
    }

    static Stream<Arguments> originalLore() {
        return Stream.of(
                Arguments.of(0L, "thou shalt utter shilling", 4437113781045784766L),
                Arguments.of(4096L, "thou prithee, shalt utter shilling", -4426322955154844568L),
                Arguments.of(4097L, "thou shalt utter shilling, good morrow!", 4970963539523752816L),
                Arguments.of(4098L, "verily, thou shalt utter shilling", -8142684128437798579L));
    }

    @Test
    void quittingClearsOnlyThatPlayersToggle() throws Exception {
        Player first = player();
        Player second = player();
        toggle(first);
        toggle(second);
        assertEquals(" thou", chat(first));
        command.onQuit(new PlayerQuitEvent(first, ""));
        assertEquals("you", chat(first));
        assertEquals(" thou", chat(second));
        toggle(first);
        assertEquals(" thou", chat(first));
    }

    @Test
    void shutdownClearsAllToggles() throws Exception {
        Player first = player();
        Player second = player();
        toggle(first);
        toggle(second);
        command.shutdown();
        assertEquals("you", chat(first));
        assertEquals("you", chat(second));
    }

    private String transform(String input) throws Exception {
        Method method = CommandShakespeare.class.getDeclaredMethod("applyReplacements", String.class);
        method.setAccessible(true);
        return (String) method.invoke(command, input);
    }

    private Random random() throws Exception {
        Field field = CommandShakespeare.class.getDeclaredField("rng");
        field.setAccessible(true);
        return (Random) field.get(command);
    }

    private void toggle(Player player) {
        assertTrue(command.onCommand(player, null, "shakespeare", new String[]{"toggle"}));
    }

    private String chat(Player player) throws Exception {
        random().setSeed(0L);
        AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(true, player, "you", new HashSet<>());
        command.onChat(event);
        return event.getMessage();
    }

    private static Player player() {
        UUID uuid = UUID.randomUUID();
        return (Player) Proxy.newProxyInstance(Player.class.getClassLoader(), new Class<?>[]{Player.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "getUniqueId" -> uuid;
                    case "sendMessage" -> null;
                    default -> throw new UnsupportedOperationException(method.getName());
                });
    }
}
