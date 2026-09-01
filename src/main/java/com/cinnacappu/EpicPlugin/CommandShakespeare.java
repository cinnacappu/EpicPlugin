package com.cinnacappu.EpicPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandShakespeare implements CommandExecutor, Listener {

    // Flair
    private static final String TAG = "§2§l[EP]§r§a";
    private static final String TAG_ERR = "§2§l[EP]§r§c";
    private static final String TAG_INFO = "§2§l[EP]§r§a";

    private final Plugin plugin;
    private final Random rng = new Random();

    // players with Shakespeare enabled
    private final Set<UUID> enabledPlayers = ConcurrentHashMap.newKeySet();

    // final compiled replacements
    private final LinkedHashMap<Pattern, String> replacements = new LinkedHashMap<>();

    // lore fragments
    private final List<String> loreAdds = new ArrayList<>();

    public CommandShakespeare(Plugin plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin");

        buildDictionary();
        loadLore();
    }

    /*=============================================
    /shakespeare
    ==============================================*/
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player p)) {
            sender.sendMessage(TAG_ERR + " Only players may speak like Shakespeare.");
            return true;
        }

        // /shakespeare toggle
        if (args.length == 1 && args[0].equalsIgnoreCase("toggle")) {
            UUID id = p.getUniqueId();
            if (enabledPlayers.contains(id)) {
                enabledPlayers.remove(id);
                p.sendMessage(TAG_ERR + " Thy archaic tongue is now §lOFF§r§c.");
            } else {
                enabledPlayers.add(id);
                p.sendMessage(TAG_INFO + " Thou now speakest in Shakespearean prose!");
            }
            return true;
        }

        // /shakespeare <message>
        if (args.length == 0) {
            p.sendMessage(TAG_ERR + " Usage: /shakespeare <message> OR /shakespeare toggle");
            return true;
        }

        String msg = String.join(" ", args);
        String out = transformWithLore(msg);

        Bukkit.broadcastMessage("<" + p.getDisplayName() + "> " + out);

        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1f, 1f);
        p.getWorld().spawnParticle(Particle.ENCHANT, p.getLocation(), 16);

        return true;
    }

    /*=============================================
    CHAT LISTENER — GLOBAL VISION
    ==============================================*/
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (!enabledPlayers.contains(p.getUniqueId())) return;

        String msg = e.getMessage();
        String out = transformWithLore(msg);

        // everyone sees Shakespeare
        e.setMessage(" " + out);
    }


    /*=============================================
    TEXT PROCESSING
    ==============================================*/
    private String transformWithLore(String input) {
        String out = applyReplacements(input);
        return maybeAddLore(out);
    }

    private String applyReplacements(String text) {
        String out = text;
        for (Map.Entry<Pattern, String> entry : replacements.entrySet()) {
            Matcher m = entry.getKey().matcher(out);
            out = m.replaceAll(entry.getValue());
        }
        return out;
    }

    private String maybeAddLore(String text) {
        if (rng.nextDouble() <= 0.1) {
            String insert = loreAdds.get(rng.nextInt(loreAdds.size()));
            int pos = rng.nextInt(3);

            return switch (pos) {
                case 0 -> insert + " " + text;
                case 1 -> {
                    String[] w = text.split("\\s+");
                    List<String> list = new ArrayList<>(Arrays.asList(w));
                    list.add(rng.nextInt(list.size() + 1), insert);
                    yield String.join(" ", list);
                }
                default -> text + ", " + insert;
            };
        }
        return text;
    }


    /*=============================================
    DICTIONARY + PLURALS
    ==============================================*/
    private static final String[][] BASE = new String[][]{
            // word, replacement, "P" => do plural
            {"you", "thou", "P"},
            {"your", "thy", "P"},
            {"yours", "thine", "P"},
            {"yourself", "thyself", "P"},
            {"yourselves", "thyselves", ""},

            {"it's", "'tis", ""},
            {"its", "'tis", ""},

            {"am", "be", ""},
            {"is", "be", ""},
            {"are", "art", ""},
            {"was", "wert", ""},
            {"were", "wert", ""},

            {"will", "shall", ""},
            {"will not", "shalt not", ""},
            {"won't", "shalt not", ""},
            {"have", "hath", ""},
            {"has", "hath", ""},
            {"do", "doth", ""},
            {"does", "doth", ""},
            {"did", "didst", ""},
            {"can", "canst", ""},
            {"cannot", "canst not", ""},
            {"shall", "shalt", ""},
            {"should", "shouldst", ""},

            // common actions
            {"go", "depart", ""},
            {"come", "approach", ""},
            {"walk", "stride", "P"},
            {"run", "hasten", "P"},
            {"jump", "vault", "P"},
            {"talk", "speak", "P"},
            {"speak", "utter", "P"},
            {"say", "proclaim", "P"},
            {"listen", "hearken", "P"},
            {"fight", "duel", "P"},
            {"attack", "assail", "P"},
            {"defend", "guard", "P"},
            {"protect", "shield", "P"},
            {"steal", "pilfer", "P"},
            {"destroy", "lay waste", "P"},
            {"build", "erect", "P"},
            {"create", "forge", "P"},
            {"work", "toil", "P"},
            {"sleep", "slumber", "P"},
            {"eat", "consume", "P"},
            {"drink", "imbibe", "P"},
            {"kill", "slay", "P"},
            {"die", "perish", "P"},
            {"win", "prevail", "P"},
            {"lose", "fall in defeat", "P"},
            {"argue", "quarrel", "P"},
            {"shout", "cry aloud", "P"},
            {"yell", "bellow", "P"},
            {"throw", "cast", "P"},
            {"push", "thrust", "P"},
            {"pull", "draw", "P"},
            {"help", "assist", "P"},
            {"stop", "cease", "P"},
            {"wait", "tarry", "P"},
            {"move", "shift", "P"},
            {"touch", "lay hand upon", "P"},
            {"thank you", "I thank thee", ""},

            // questions
            {"why", "wherefore", ""},
            {"where", "whither", ""},
            {"when", "at what time", ""},
            {"what", "what manner", ""},
            {"who", "whom", ""},
            {"how", "in what way", ""},
            {"please", "prithee", ""},

            // time
            {"now", "this instant", ""},
            {"soon", "anon", ""},
            {"today", "this day", ""},
            {"tomorrow", "the morrow", ""},
            {"yesterday", "yesternight", ""},
            {"morning", "morn", "P"},
            {"night", "eve", "P"},
            {"sunset", "eventide", "P"},
            {"sunrise", "dawn", "P"},
            {"before", "ere", ""},
            {"after", "henceforth", ""},
            {"never", "ne'er", ""},
            {"always", "for evermore", ""},

            // emotion
            {"happy", "merry", "P"},
            {"sad", "sorrowful", "P"},
            {"angry", "wrathful", "P"},
            {"upset", "vexed", "P"},
            {"excited", "enraptured", "P"},
            {"scared", "afeared", "P"},
            {"afraid", "afeared", "P"},
            {"worried", "distressed", "P"},
            {"tired", "weary", "P"},
            {"bored", "listless", "P"},
            {"confused", "perplexed", "P"},
            {"shy", "bashful", "P"},

            // insults
            {"stupid", "addlepated", "P"},
            {"idiot", "clotpole", "P"},
            {"dumb", "witless", "P"},
            {"loser", "dullard", "P"},
            {"ugly", "ill-favored", "P"},
            {"jerk", "miscreant", "P"},
            {"pathetic", "piteous", "P"},
            {"evil", "villainous", "P"},
            {"liar", "false knave", "P"},
            {"coward", "craven", "P"},
            {"villain", "knave", "P"},
            {"fool", "ninny", "P"},
            {"beautiful", "beauteous", "P"},
            {"awesome", "wondrous", "P"},
            {"amazing", "marvelous", "P"},

            // slang
            {"bro", "good sir", "P"},
            {"dude", "good fellow", "P"},
            {"bruh", "good fellow", "P"},
            {"lol", "I jest", ""},
            {"lmao", "I laugh heartily", ""},
            {"omg", "heavens above", ""},
            {"wtf", "what sorcery", ""},
            {"fr", "in earnest", ""},
            {"idk", "I know not", ""},
            {"idc", "I care not", ""},
            {"rip", "resteth in peace", ""},
            {"pls", "prithee", ""},
            {"plz", "prithee", ""},
            {"hey", "hark", ""},
            {"hello", "well met", ""},
            {"bye", "fare thee well", ""},

            // places/nature
            {"home", "dwelling", "P"},
            {"house", "manor", "P"},
            {"village", "hamlet", "P"},
            {"town", "shire", "P"},
            {"city", "kingdom", "P"},
            {"castle", "keep", "P"},
            {"mountain", "mount", "P"},
            {"river", "stream", "P"},
            {"forest", "woodland", "P"},
            {"desert", "wasteland", "P"},
            {"woods", "forest", "P"},
            {"tree", "oak", "P"},
            {"bird", "fowl", "P"},
            {"fish", "sea-creature", "P"},
            {"horse", "steed", "P"},
            {"sun", "golden orb", "P"},
            {"moon", "silver sphere", "P"},
            {"star", "celestial flame", "P"},
            {"rain", "heavens’ tears", "P"},
            {"wind", "tempest", "P"},
            {"fire", "flame", "P"},
            {"water", "aqua", "P"},
            {"air", "ether", "P"},
            {"earth", "terra", "P"},
            {"sky", "firmament", ""},

            // religion
            {"god", "the almighty", "P"},
            {"gods", "the divine", ""},
            {"angel", "seraph", "P"},
            {"devil", "demon", "P"},
            {"heaven", "the firmament", "P"},
            {"hell", "the abyss", "P"},
            {"saint", "holy one", "P"},
            {"pray", "entreat", "P"},
            {"curse", "hex", "P"},
            {"bless", "sanctify", "P"},

            // value
            {"money", "coin", ""},
            {"coin", "shilling", "P"},
            {"coins", "shillings", ""},
            {"rich", "well-endowed", "P"},
            {"poor", "impoverished", "P"},
            {"shop", "market", "P"},
            {"market", "bazaar", "P"},
            {"trade", "barter", "P"},

            // abstract
            {"world", "realm", "P"},
            {"life", "mortal coil", "P"},
            {"death", "doom", "P"},
            {"peace", "harmony", "P"},
            {"war", "conflict", "P"},
            {"battle", "warfare", "P"},
            {"story", "tale", "P"},
            {"song", "ballad", "P"},
            {"music", "melody", "P"},
            {"dance", "gambol", "P"},
            {"dream", "vision", "P"},
            {"heart", "bosom", "P"},
            {"blood", "lifeblood", "P"},

            // MC/fantasy
            {"sword", "blade", "P"},
            {"bow", "longbow", "P"},
            {"shield", "buckler", "P"},
            {"armor", "plate", "P"},
            {"weapon", "armament", "P"},
            {"enemy", "foe", "P"},
            {"monster", "beast", "P"},
            {"boss", "great foe", "P"},
            {"mine", "delve", "P"},
            {"craft", "forge", "P"},
            {"portal", "arcane gate", "P"},
            {"nether", "infernal realm", ""},
            {"end", "ethereal realm", ""},
            {"villager", "peasant", "P"},
            {"zombie", "undead knave", "P"},
            {"skeleton", "bone-soldier", "P"},
            {"creeper", "green fiend", "P"},
            {"witch", "hag", "P"},

            // Special
            {"shakespeare", "the greatest poet to have ever existed", ""},
            {"man", "good sir", "P"},
            {"men", "gentlefolk", ""},
            {"woman", "lady", "P"},
            {"women", "ladies", ""},
            {"你", "汝", ""}
    };


    /*=============================================
    BUILD DICTIONARY
    ==============================================*/
    private void buildDictionary() {

        for (String[] row : BASE) {
            String from = row[0];
            String to   = row[1];
            boolean plural = row.length > 2 && row[2].equalsIgnoreCase("P");

            addCaseVariants(from, to);

            if (plural)
                addPluralVariants(from, to);
        }

        // Add “over/never” poetic forms
        addCaseVariants("over", "o'er");
        addCaseVariants("never", "ne'er");

        // Y'all set
        addCaseVariants("yall", "you");
        addCaseVariants("y'all", "you");
        addCaseVariants("yalls", "yours");
        addCaseVariants("y'alls", "yours");
    }

    private void addCaseVariants(String from, String to) {
        // lower
        addPattern(from, to);
        // Capitalized
        addPattern(cap(from), cap(to));
        // UPPER
        addPattern(from.toUpperCase(), to.toUpperCase());
    }

    private void addPluralVariants(String from, String to) {
        String pf = pluralize(from);
        String pt = pluralize(to);

        addPattern(pf, pt);
        addPattern(cap(pf), cap(pt));
        addPattern(pf.toUpperCase(), pt.toUpperCase());
    }

    private void addPattern(String from, String to) {
        String pattern = "(?<![A-Za-z])" + Pattern.quote(from) + "(?![A-Za-z])";
        replacements.put(Pattern.compile(pattern), Matcher.quoteReplacement(to));
    }

    private static String cap(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private static boolean isVowel(char c) {
        return "aeiouAEIOU".indexOf(c) >= 0;
    }

    /** very lightweight plural logic */
    private static String pluralize(String w) {
        if (w.equalsIgnoreCase("man")) return matchCase(w, "men");
        if (w.equalsIgnoreCase("woman")) return matchCase(w, "women");
        if (w.equalsIgnoreCase("child")) return matchCase(w, "children");
        if (w.equalsIgnoreCase("person")) return matchCase(w, "people");
        if (w.equalsIgnoreCase("ox")) return matchCase(w, "oxen");
        if (w.equalsIgnoreCase("goose")) return matchCase(w, "geese");
        if (w.equalsIgnoreCase("mouse")) return matchCase(w, "mice");

        if (w.endsWith("y") && !isVowel(w.charAt(w.length() - 2))) {
            return w.substring(0, w.length() - 1) + "ies";
        }
        if (w.endsWith("s") || w.endsWith("x") || w.endsWith("z")
                || w.endsWith("ch") || w.endsWith("sh")) {
            return w + "es";
        }
        return w + "s";
    }

    private static String matchCase(String src, String tgt) {
        if (src.equals(src.toUpperCase())) return tgt.toUpperCase();
        if (src.equals(cap(src.toLowerCase()))) return cap(tgt.toLowerCase());
        return tgt.toLowerCase();
    }

    /*=============================================
    LORE
    ==============================================*/
    private void loadLore() {
        loreAdds.addAll(List.of(
                "forsooth!",
                "verily,",
                "by my troth,",
                "prithee,",
                "zounds!",
                "hark!",
                "methinks",
                "lo!",
                "for shame!",
                "good morrow!",
                "wouldst thou believe?",
                "in sooth,"
        ));
    }
}
