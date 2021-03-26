package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.interfaces.IKeywordChecker;
import uk.ac.warwick.cs126.structures.MyAVLTree;

public class KeywordChecker implements IKeywordChecker {
    private static final String[] keywords = {
            "0",
            "agreeable",
            "air-headed",
            "apocalypse",
            "appetizing",
            "average",
            "awesome",
            "biohazard",
            "bland",
            "bleh",
            "burnt",
            "charming",
            "clueless",
            "cockroach",
            "cold",
            "crap",
            "dancing",
            "dead",
            "decadent",
            "decent",
            "dirty",
            "disgusting",
            "dreadful",
            "droppings",
            "dry",
            "dumpy",
            "excellent",
            "favourite",
            "feel-good",
            "flavourful",
            "frozen",
            "gem",
            "gross",
            "heart",
            "heavenly",
            "horrendous",
            "horrible",
            "incredible",
            "interesting",
            "lame",
            "lousy",
            "mediocre",
            "meh",
            "mess",
            "microwaved",
            "mouth-watering",
            "nightmares",
            "ok",
            "okay",
            "overcooked",
            "overhyped",
            "perfection",
            "polite",
            "prompt",
            "quality",
            "rude",
            "satisfaction",
            "savoury",
            "sewer",
            "singing",
            "slow",
            "so-so",
            "spongy",
            "sticky",
            "sublime",
            "succulent",
            "sucked",
            "surprised",
            "terrible",
            "tingling",
            "tired",
            "toxic",
            "traumatizing",
            "uncomfortable",
            "under-seasoned",
            "undercooked",
            "unique",
            "unprofessional",
            "waste",
            "worst",
            "yuck",
            "yummy"
    };

    private static final MyAVLTree<Integer, String> keywordsAVL;

    static {
        keywordsAVL = new MyAVLTree<>(String::hashCode);
        for (String str: keywords)
            keywordsAVL.insert(str);
    }

    public KeywordChecker() {
        // Initialise things here
    }

    public boolean isAKeyword(String word) {
        return keywordsAVL.search(word);
    }

    public boolean enhancedCheck(String word) {
        word = word.replaceAll("(\\. | \\? | ! | ; | : | # )","").toLowerCase();
        return isAKeyword(word);
    }
}