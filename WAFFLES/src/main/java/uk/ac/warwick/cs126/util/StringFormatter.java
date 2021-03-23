package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.structures.MyAVLTree;

import java.util.function.Function;

public class StringFormatter {
    private static final MyAVLTree<Integer, String[]> converterAVL;
    private static final String[][] accentAndConvertedAccent = {
            {"§", "SS"},
            {"ª", "a"},
            {"²", "2"},
            {"³", "3"},
            {"µ", "u"},
            {"¶", "P"},
            {"¹", "1"},
            {"º", "o"},
            {"À", "A"},
            {"Á", "A"},
            {"Â", "A"},
            {"Ã", "A"},
            {"Ä", "A"},
            {"Å", "A"},
            {"Æ", "AE"},
            {"Ç", "C"},
            {"È", "E"},
            {"É", "E"},
            {"Ê", "E"},
            {"Ë", "E"},
            {"Ì", "I"},
            {"Í", "I"},
            {"Î", "I"},
            {"Ï", "I"},
            {"Ð", "D"},
            {"Ñ", "N"},
            {"Ò", "O"},
            {"Ó", "O"},
            {"Ô", "O"},
            {"Õ", "O"},
            {"Ö", "O"},
            {"×", "x"},
            {"Ø", "O"},
            {"Ù", "U"},
            {"Ú", "U"},
            {"Û", "U"},
            {"Ü", "U"},
            {"Ý", "Y"},
            {"Þ", "Th"},
            {"ß", "ss"},
            {"à", "a"},
            {"á", "a"},
            {"â", "a"},
            {"ã", "a"},
            {"ä", "a"},
            {"å", "a"},
            {"æ", "ae"},
            {"ç", "c"},
            {"è", "e"},
            {"é", "e"},
            {"ê", "e"},
            {"ë", "e"},
            {"ì", "i"},
            {"í", "i"},
            {"î", "i"},
            {"ï", "i"},
            {"ð", "d"},
            {"ñ", "n"},
            {"ò", "o"},
            {"ó", "o"},
            {"ô", "o"},
            {"õ", "o"},
            {"ö", "o"},
            {"ø", "o"},
            {"ù", "u"},
            {"ú", "u"},
            {"û", "u"},
            {"ü", "u"},
            {"ý", "y"},
            {"þ", "th"},
            {"ÿ", "y"},
            {"Ā", "A"},
            {"ā", "a"},
            {"Ă", "A"},
            {"ă", "a"},
            {"Ą", "A"},
            {"ą", "a"},
            {"Ć", "C"},
            {"ć", "c"},
            {"Ĉ", "C"},
            {"ĉ", "c"},
            {"Ċ", "C"},
            {"ċ", "c"},
            {"Č", "C"},
            {"č", "c"},
            {"Ď", "D"},
            {"ď", "d"},
            {"Đ", "D"},
            {"đ", "d"},
            {"Ē", "E"},
            {"ē", "e"},
            {"Ĕ", "E"},
            {"ĕ", "e"},
            {"Ė", "E"},
            {"ė", "e"},
            {"Ę", "E"},
            {"ę", "e"},
            {"Ě", "E"},
            {"ě", "e"},
            {"Ĝ", "G"},
            {"ĝ", "g"},
            {"Ğ", "G"},
            {"ğ", "g"},
            {"Ġ", "G"},
            {"ġ", "g"},
            {"Ģ", "G"},
            {"ģ", "g"},
            {"Ĥ", "H"},
            {"ĥ", "h"},
            {"Ħ", "H"},
            {"ħ", "h"},
            {"Ĩ", "I"},
            {"ĩ", "i"},
            {"Ī", "I"},
            {"ī", "i"},
            {"Ĭ", "I"},
            {"ĭ", "i"},
            {"Į", "I"},
            {"į", "i"},
            {"İ", "I"},
            {"ı", "i"},
            {"Ĳ", "IJ"},
            {"ĳ", "ij"},
            {"Ĵ", "J"},
            {"ĵ", "j"},
            {"Ķ", "K"},
            {"ķ", "k"},
            {"ĸ", "k"},
            {"Ĺ", "L"},
            {"ĺ", "l"},
            {"Ļ", "L"},
            {"ļ", "l"},
            {"Ľ", "L"},
            {"ľ", "l"},
            {"Ŀ", "L"},
            {"ŀ", "l"},
            {"Ł", "L"},
            {"ł", "l"},
            {"Ń", "N"},
            {"ń", "n"},
            {"Ņ", "N"},
            {"ņ", "n"},
            {"Ň", "N"},
            {"ň", "n"},
            {"Ŋ", "ng"},
            {"ŋ", "NG"},
            {"Ō", "O"},
            {"ō", "o"},
            {"Ŏ", "O"},
            {"ŏ", "o"},
            {"Ő", "O"},
            {"ő", "o"},
            {"Œ", "OE"},
            {"œ", "oe"},
            {"Ŕ", "R"},
            {"ŕ", "r"},
            {"Ŗ", "R"},
            {"ŗ", "r"},
            {"Ř", "R"},
            {"ř", "r"},
            {"Ś", "S"},
            {"ś", "s"},
            {"Ŝ", "S"},
            {"ŝ", "s"},
            {"Ş", "S"},
            {"ş", "s"},
            {"Š", "S"},
            {"š", "s"},
            {"Ţ", "T"},
            {"ţ", "t"},
            {"Ť", "T"},
            {"ť", "t"},
            {"Ŧ", "T"},
            {"ŧ", "t"},
            {"Ũ", "U"},
            {"ũ", "u"},
            {"Ū", "U"},
            {"ū", "u"},
            {"Ŭ", "U"},
            {"ŭ", "u"},
            {"Ů", "U"},
            {"ů", "u"},
            {"Ű", "U"},
            {"ű", "u"},
            {"Ų", "U"},
            {"ų", "u"},
            {"Ŵ", "W"},
            {"ŵ", "w"},
            {"Ŷ", "Y"},
            {"ŷ", "y"},
            {"Ÿ", "Y"},
            {"Ź", "Z"},
            {"ź", "z"},
            {"Ż", "Z"},
            {"ż", "z"},
            {"Ž", "Z"},
            {"ž", "z"},
            {"ſ", "s"},
            {"ƀ", "b"},
            {"Ɓ", "B"},
            {"Ƃ", "B"},
            {"ƃ", "b"},
            {"Ƅ", "6"},
            {"ƅ", "6"},
            {"Ɔ", "O"},
            {"Ƈ", "C"},
            {"ƈ", "c"},
            {"Ɖ", "D"},
            {"Ɗ", "D"},
            {"Ƌ", "D"},
            {"ƌ", "d"},
            {"ƍ", "d"},
            {"Ǝ", "3"},
            {"Ɛ", "E"},
            {"Ƒ", "F"},
            {"ƒ", "f"},
            {"Ɠ", "G"},
            {"Ɣ", "G"},
            {"ƕ", "hv"},
            {"Ɩ", "I"},
            {"Ɨ", "I"},
            {"Ƙ", "K"},
            {"ƙ", "k"},
            {"ƚ", "l"},
            {"ƛ", "l"},
            {"Ɯ", "W"},
            {"Ɲ", "N"},
            {"ƞ", "n"},
            {"Ɵ", "O"},
            {"Ơ", "O"},
            {"ơ", "o"},
            {"Ƣ", "OI"},
            {"ƣ", "oi"},
            {"Ƥ", "P"},
            {"ƥ", "p"},
            {"Ʀ", "YR"},
            {"Ƨ", "2"},
            {"ƨ", "2"},
            {"Ʃ", "SH"},
            {"ƪ", "sh"},
            {"ƫ", "t"},
            {"Ƭ", "T"},
            {"ƭ", "t"},
            {"Ʈ", "T"},
            {"Ư", "U"},
            {"ư", "u"},
            {"Ʊ", "Y"},
            {"Ʋ", "V"},
            {"Ƴ", "Y"},
            {"ƴ", "y"},
            {"Ƶ", "Z"},
            {"ƶ", "z"},
            {"Ʒ", "ZH"},
            {"Ƹ", "ZH"},
            {"ƹ", "zh"},
            {"ƺ", "zh"},
            {"ƻ", "2"},
            {"Ƽ", "5"},
            {"ƽ", "5"},
            {"ƾ", "ts"},
            {"ƿ", "w"},
            {"Ǆ", "DZ"},
            {"ǅ", "Dz"},
            {"ǆ", "dz"},
            {"Ǉ", "LJ"},
            {"ǈ", "Lj"},
            {"ǉ", "lj"},
            {"Ǌ", "NJ"},
            {"ǋ", "Nj"},
            {"ǌ", "nj"},
            {"Ǎ", "A"},
            {"ǎ", "a"},
            {"Ǐ", "I"},
            {"ǐ", "i"},
            {"Ǒ", "O"},
            {"ǒ", "o"},
            {"Ǔ", "U"},
            {"ǔ", "u"},
            {"Ǖ", "U"},
            {"ǖ", "u"},
            {"Ǘ", "U"},
            {"ǘ", "u"},
            {"Ǚ", "U"},
            {"ǚ", "u"},
            {"Ǜ", "U"},
            {"ǜ", "u"},
            {"Ǟ", "A"},
            {"ǟ", "a"},
            {"Ǡ", "A"},
            {"ǡ", "a"},
            {"Ǣ", "AE"},
            {"ǣ", "ae"},
            {"Ǥ", "G"},
            {"ǥ", "g"},
            {"Ǧ", "G"},
            {"ǧ", "g"},
            {"Ǩ", "K"},
            {"ǩ", "k"},
            {"Ǫ", "O"},
            {"ǫ", "o"},
            {"Ǭ", "O"},
            {"ǭ", "o"},
            {"Ǯ", "ZH"},
            {"ǯ", "zh"},
            {"ǰ", "j"},
            {"Ǳ", "DZ"},
            {"ǲ", "Dz"},
            {"ǳ", "dz"},
            {"Ǵ", "G"},
            {"ǵ", "g"},
            {"Ƕ", "HV"},
            {"Ƿ", "W"},
            {"Ǹ", "N"},
            {"ǹ", "n"},
            {"Ǻ", "A"},
            {"ǻ", "a"},
            {"Ǽ", "AE"},
            {"ǽ", "ae"},
            {"Ǿ", "O"},
            {"ǿ", "o"},
            {"Ȁ", "A"},
            {"ȁ", "a"},
            {"Ȃ", "A"},
            {"ȃ", "a"},
            {"Ȅ", "E"},
            {"ȅ", "e"},
            {"Ȇ", "E"},
            {"ȇ", "e"},
            {"Ȉ", "I"},
            {"ȉ", "i"},
            {"Ȋ", "I"},
            {"ȋ", "i"},
            {"Ȍ", "O"},
            {"ȍ", "o"},
            {"Ȏ", "O"},
            {"ȏ", "o"},
            {"Ȑ", "R"},
            {"ȑ", "r"},
            {"Ȓ", "R"},
            {"ȓ", "r"},
            {"Ȕ", "U"},
            {"ȕ", "u"},
            {"Ȗ", "U"},
            {"ȗ", "u"},
            {"Ș", "S"},
            {"ș", "s"},
            {"Ț", "T"},
            {"ț", "t"},
            {"Ȝ", "Y"},
            {"ȝ", "y"},
            {"Ȟ", "H"},
            {"ȟ", "h"},
            {"Ƞ", "N"},
            {"ȡ", "d"},
            {"Ȣ", "OU"},
            {"ȣ", "ou"},
            {"Ȥ", "Z"},
            {"ȥ", "z"},
            {"Ȧ", "A"},
            {"ȧ", "a"},
            {"Ȩ", "E"},
            {"ȩ", "e"},
            {"Ȫ", "O"},
            {"ȫ", "o"},
            {"Ȭ", "O"},
            {"ȭ", "o"},
            {"Ȯ", "O"},
            {"ȯ", "o"},
            {"Ȱ", "O"},
            {"ȱ", "o"},
            {"Ȳ", "Y"},
            {"ȳ", "y"},
            {"ȴ", "l"},
            {"ȵ", "n"},
            {"ȶ", "t"},
            {"ȷ", "j"},
            {"ȸ", "db"},
            {"ȹ", "qp"},
            {"Ⱥ", "A"},
            {"Ȼ", "C"},
            {"ȼ", "c"},
            {"Ƚ", "L"},
            {"Ⱦ", "T"},
            {"ȿ", "s"},
            {"ɀ", "z"},
            {"Ƀ", "B"},
            {"Ʉ", "U"},
            {"Ɇ", "E"},
            {"ɇ", "e"},
            {"Ɉ", "J"},
            {"ɉ", "j"},
            {"Ɋ", "q"},
            {"ɋ", "q"},
            {"Ɍ", "R"},
            {"ɍ", "r"},
            {"Ɏ", "Y"},
            {"ɏ", "y"},
            {"ɐ", "a"},
            {"ɑ", "a"},
            {"ɒ", "a"},
            {"ɓ", "b"},
            {"ɔ", "o"},
            {"ɕ", "c"},
            {"ɖ", "d"},
            {"ɗ", "d"},
            {"ɘ", "e"},
            {"ɛ", "e"},
            {"ɜ", "e"},
            {"ɝ", "e"},
            {"ɞ", "e"},
            {"ɟ", "j"},
            {"ɠ", "g"},
            {"ɡ", "g"},
            {"ɢ", "g"},
            {"ɣ", "g"},
            {"ɤ", "u"},
            {"ɥ", "Y"},
            {"ɦ", "h"},
            {"ɧ", "h"},
            {"ɨ", "i"},
            {"ɩ", "i"},
            {"ɪ", "I"},
            {"ɫ", "l"},
            {"ɬ", "l"},
            {"ɭ", "l"},
            {"ɮ", "lZ"},
            {"ɯ", "W"},
            {"ɰ", "W"},
            {"ɱ", "m"},
            {"ɲ", "n"},
            {"ɳ", "n"},
            {"ɴ", "n"},
            {"ɵ", "o"},
            {"ɶ", "OE"},
            {"ɷ", "O"},
            {"ɸ", "F"},
            {"ɹ", "r"},
            {"ɺ", "r"},
            {"ɻ", "r"},
            {"ɼ", "r"},
            {"ɽ", "r"},
            {"ɾ", "r"},
            {"ɿ", "r"},
            {"ʀ", "R"},
            {"ʁ", "R"},
            {"ʂ", "s"},
            {"ʃ", "S"},
            {"ʄ", "j"},
            {"ʅ", "S"},
            {"ʆ", "S"},
            {"ʇ", "t"},
            {"ʈ", "t"},
            {"ʉ", "u"},
            {"ʊ", "U"},
            {"ʋ", "v"},
            {"ʍ", "w"},
            {"ʎ", "y"},
            {"ʏ", "Y"},
            {"ʐ", "z"},
            {"ʑ", "z"},
            {"ʒ", "Z"},
            {"ʓ", "Z"},
            {"ʗ", "C"},
            {"ʙ", "B"},
            {"ʚ", "E"},
            {"ʛ", "G"},
            {"ʜ", "H"},
            {"ʝ", "j"},
            {"ʞ", "k"},
            {"ʟ", "L"},
            {"ʠ", "q"},
            {"ʣ", "dz"},
            {"ʤ", "dZ"},
            {"ʥ", "dz"},
            {"ʦ", "ts"},
            {"ʧ", "tS"},
            {"ʨ", "tC"},
            {"ʩ", "fN"},
            {"ʪ", "ls"},
            {"ʫ", "lz"},
            {"ʬ", "WW"},
            {"ʮ", "h"},
            {"ʯ", "h"},
            {"ʰ", "h"},
            {"ʱ", "h"},
            {"ʲ", "j"},
            {"ʳ", "r"},
            {"ʴ", "r"},
            {"ʵ", "r"},
            {"ʶ", "r"},
            {"ʷ", "w"},
            {"ʸ", "y"},
            {"˅", "V"},
            {"ˇ", "V"},
            {"˕", "V"},
            {"˘", "V"},
            {"˞", "R"},
            {"˟", "X"},
            {"ˠ", "G"},
            {"ˡ", "l"},
            {"ˢ", "s"},
            {"ˣ", "x"},
            {"ˬ", "V"},
            {"ͣ", "a"},
            {"ͤ", "e"},
            {"ͥ", "i"},
            {"ͦ", "o"},
            {"ͧ", "u"},
            {"ͨ", "c"},
            {"ͩ", "d"},
            {"ͪ", "h"},
            {"ͫ", "m"},
            {"ͬ", "r"},
            {"ͭ", "t"},
            {"ͮ", "v"},
            {"ͯ", "x"},
            {"Ά", "A"},
            {"Έ", "E"},
            {"Ή", "E"},
            {"Ί", "I"},
            {"Ό", "O"},
            {"Ύ", "U"},
            {"Ώ", "O"},
            {"ΐ", "I"},
            {"Α", "A"},
            {"Β", "B"},
            {"Γ", "G"},
            {"Δ", "D"},
            {"Ε", "E"},
            {"Ζ", "Z"},
            {"Η", "E"},
            {"Θ", "Th"},
            {"Ι", "I"},
            {"Κ", "K"},
            {"Λ", "L"},
            {"Μ", "M"},
            {"Ν", "N"},
            {"Ξ", "Ks"},
            {"Ο", "O"},
            {"Π", "P"},
            {"Ρ", "R"},
            {"Σ", "S"},
            {"Τ", "T"},
            {"Υ", "U"},
            {"Φ", "Ph"},
            {"Χ", "Kh"},
            {"Ψ", "Ps"},
            {"Ω", "O"},
            {"Ϊ", "I"},
            {"Ϋ", "U"},
            {"ά", "a"},
            {"έ", "e"},
            {"ή", "e"},
            {"ί", "i"},
            {"ΰ", "u"},
            {"α", "a"},
            {"β", "b"},
            {"γ", "g"},
            {"δ", "d"},
            {"ε", "e"},
            {"ζ", "z"},
            {"η", "e"},
            {"θ", "th"},
            {"ι", "i"},
            {"κ", "k"},
            {"λ", "l"},
            {"μ", "m"},
            {"ν", "n"},
            {"ξ", "x"},
            {"ο", "o"},
            {"π", "p"},
            {"ρ", "r"},
            {"ς", "s"},
            {"σ", "s"},
            {"τ", "t"},
            {"υ", "u"},
            {"φ", "ph"},
            {"χ", "kh"},
            {"ψ", "ps"},
            {"ω", "o"},
            {"ϊ", "i"},
            {"ϋ", "u"},
            {"ό", "o"},
            {"ύ", "u"},
            {"ώ", "o"},
            {"ϐ", "b"},
            {"ϑ", "th"},
            {"ϒ", "U"},
            {"ϓ", "U"},
            {"ϔ", "U"},
            {"ϕ", "ph"},
            {"ϖ", "p"},
            {"Ϛ", "St"},
            {"ϛ", "st"},
            {"Ϝ", "W"},
            {"ϝ", "w"},
            {"Ϟ", "Q"},
            {"ϟ", "q"},
            {"Ϡ", "Sp"},
            {"ϡ", "sp"},
            {"Ϣ", "Sh"},
            {"ϣ", "sh"},
            {"Ϥ", "F"},
            {"ϥ", "f"},
            {"Ϧ", "Kh"},
            {"ϧ", "kh"},
            {"Ϩ", "H"},
            {"ϩ", "h"},
            {"Ϫ", "G"},
            {"ϫ", "g"},
            {"Ϭ", "CH"},
            {"ϭ", "ch"},
            {"Ϯ", "Ti"},
            {"ϯ", "ti"},
            {"ϰ", "k"},
            {"ϱ", "r"},
            {"ϲ", "c"},
            {"ϳ", "j"},
            {"Ѐ", "Ie"},
            {"Ё", "Io"},
            {"Ђ", "Dj"},
            {"Ѓ", "Gj"},
            {"Є", "Ie"},
            {"Ѕ", "Dz"},
            {"І", "I"},
            {"Ї", "Yi"},
            {"Ј", "J"},
            {"Љ", "Lj"},
            {"Њ", "Nj"},
            {"Ћ", "Tsh"},
            {"Ќ", "Kj"},
            {"Ѝ", "I"},
            {"Ў", "U"},
            {"Џ", "Dzh"},
            {"А", "A"},
            {"Б", "B"},
            {"В", "V"},
            {"Г", "G"},
            {"Д", "D"},
            {"Е", "E"},
            {"Ж", "Zh"},
            {"З", "Z"},
            {"И", "I"},
            {"Й", "I"},
            {"К", "K"},
            {"Л", "L"},
            {"М", "M"},
            {"Н", "N"},
            {"О", "O"},
            {"П", "P"},
            {"Р", "R"},
            {"С", "S"},
            {"Т", "T"},
            {"У", "U"},
            {"Ф", "F"},
            {"Х", "Kh"},
            {"Ц", "Ts"},
            {"Ч", "Ch"},
            {"Ш", "Sh"},
            {"Щ", "Shch"},
            {"Ы", "Y"},
            {"Э", "E"},
            {"Ю", "Iu"},
            {"Я", "Ia"},
            {"а", "a"},
            {"б", "b"},
            {"в", "v"},
            {"г", "g"},
            {"д", "d"},
            {"е", "e"},
            {"ж", "zh"},
            {"з", "z"},
            {"и", "i"},
            {"й", "i"},
            {"к", "k"},
            {"л", "l"},
            {"м", "m"},
            {"н", "n"},
            {"о", "o"},
            {"п", "p"},
            {"р", "r"},
            {"с", "s"},
            {"т", "t"},
            {"у", "u"},
            {"ф", "f"},
            {"х", "kh"},
            {"ц", "ts"},
            {"ч", "ch"},
            {"ш", "sh"},
            {"щ", "shch"},
            {"ы", "y"},
            {"э", "e"},
            {"ю", "iu"},
            {"я", "ia"},
            {"ѐ", "ie"},
            {"ё", "io"},
            {"ђ", "dj"},
            {"ѓ", "gj"},
            {"є", "ie"},
            {"ѕ", "dz"},
            {"і", "i"},
            {"ї", "yi"},
            {"ј", "j"},
            {"љ", "lj"},
            {"њ", "nj"},
            {"ћ", "tsh"},
            {"ќ", "kj"},
            {"ѝ", "i"},
            {"ў", "u"},
            {"џ", "dzh"},
            {"Ѡ", "O"},
            {"ѡ", "o"},
            {"Ѣ", "E"},
            {"ѣ", "e"},
            {"Ѥ", "Ie"},
            {"ѥ", "ie"},
            {"Ѧ", "E"},
            {"ѧ", "e"},
            {"Ѩ", "Ie"},
            {"ѩ", "ie"},
            {"Ѫ", "O"},
            {"ѫ", "o"},
            {"Ѭ", "Io"},
            {"ѭ", "io"},
            {"Ѯ", "Ks"},
            {"ѯ", "ks"},
            {"Ѱ", "Ps"},
            {"ѱ", "ps"},
            {"Ѳ", "F"},
            {"ѳ", "f"},
            {"Ѵ", "Y"},
            {"ѵ", "y"},
            {"Ѷ", "Y"},
            {"ѷ", "y"},
            {"Ѹ", "u"},
            {"ѹ", "u"},
            {"Ѻ", "O"},
            {"ѻ", "o"},
            {"Ѽ", "O"},
            {"ѽ", "o"},
            {"Ѿ", "Ot"},
            {"ѿ", "ot"},
            {"Ҁ", "Q"},
            {"ҁ", "q"},
            {"Ҥ", "Ng"},
            {"ҥ", "ng"},
            {"Ҩ", "Kh"},
            {"ҩ", "kh"},
            {"Ү", "U"},
            {"ү", "u"},
            {"Ҵ", "Tts"},
            {"ҵ", "tts"},
            {"Һ", "H"},
            {"һ", "h"},
            {"Ҽ", "Ch"},
            {"ҽ", "ch"},
            {"Ӂ", "Zh"},
            {"ӂ", "zh"},
            {"Ӌ", "Ch"},
            {"ӌ", "ch"},
            {"Ӑ", "a"},
            {"ӑ", "a"},
            {"Ӓ", "A"},
            {"ӓ", "a"},
            {"Ӕ", "Ae"},
            {"ӕ", "ae"},
            {"Ӗ", "Ie"},
            {"ӗ", "ie"},
            {"Ӝ", "Zh"},
            {"ӝ", "zh"},
            {"Ӟ", "Z"},
            {"ӟ", "z"},
            {"Ӡ", "Dz"},
            {"ӡ", "dz"},
            {"Ӣ", "I"},
            {"ӣ", "i"},
            {"Ӥ", "I"},
            {"ӥ", "i"},
            {"Ӧ", "O"},
            {"ӧ", "o"},
            {"Ө", "O"},
            {"ө", "o"},
            {"Ӫ", "O"},
            {"ӫ", "o"},
            {"Ӭ", "E"},
            {"ӭ", "e"},
            {"Ӯ", "U"},
            {"ӯ", "u"},
            {"Ӱ", "U"},
            {"ӱ", "u"},
            {"Ӳ", "U"},
            {"ӳ", "u"},
            {"ᚺ", "h"},
            {"ᚻ", "h"},
            {"ᛗ", "m"},
            {"ᛘ", "m"},
            {"ᛙ", "m"},
            {"ᛚ", "l"},
            {"ᛛ", "l"},
            {"ᴠ", "V"},
            {"ᴡ", "W"},
            {"ᴢ", "Z"},
            {"ᴬ", "A"},
            {"ᴭ", "AE"},
            {"ᴮ", "B"},
            {"ᴯ", "B"},
            {"ᴰ", "D"},
            {"ᴱ", "E"},
            {"ᴲ", "E"},
            {"ᴳ", "G"},
            {"ᴴ", "H"},
            {"ᴵ", "I"},
            {"ᴶ", "J"},
            {"ᴷ", "K"},
            {"ᴸ", "L"},
            {"ᴹ", "M"},
            {"ᴺ", "N"},
            {"ᴻ", "N"},
            {"ᴼ", "O"},
            {"ᴽ", "Ou"},
            {"ᴾ", "P"},
            {"ᴿ", "R"},
            {"ᵀ", "T"},
            {"ᵁ", "U"},
            {"ᵂ", "W"},
            {"ᵃ", "a"},
            {"ᵄ", "a"},
            {"ᵅ", "a"},
            {"ᵆ", "ae"},
            {"ᵇ", "b"},
            {"ᵈ", "d"},
            {"ᵉ", "e"},
            {"ᵋ", "e"},
            {"ᵌ", "e"},
            {"ᵍ", "g"},
            {"ᵎ", "i"},
            {"ᵏ", "k"},
            {"ᵐ", "m"},
            {"ᵒ", "o"},
            {"ᵖ", "p"},
            {"ᵗ", "t"},
            {"ᵘ", "u"},
            {"ᵙ", "u"},
            {"ᵚ", "m"},
            {"ᵛ", "v"},
            {"ᵝ", "b"},
            {"ᵞ", "g"},
            {"ᵟ", "d"},
            {"ᵠ", "f"},
            {"ᵢ", "i"},
            {"ᵣ", "r"},
            {"ᵤ", "u"},
            {"ᵥ", "v"},
            {"ᵦ", "b"},
            {"ᵧ", "g"},
            {"ᵨ", "r"},
            {"ᵩ", "f"},
            {"ᵬ", "b"},
            {"ᵭ", "d"},
            {"ᵮ", "f"},
            {"ᵯ", "m"},
            {"ᵰ", "n"},
            {"ᵱ", "p"},
            {"ᵲ", "r"},
            {"ᵳ", "r"},
            {"ᵴ", "s"},
            {"ᵵ", "t"},
            {"ᵶ", "z"},
            {"ᵷ", "g"},
            {"ᵽ", "p"},
            {"ᶀ", "b"},
            {"ᶁ", "d"},
            {"ᶂ", "f"},
            {"ᶃ", "g"},
            {"ᶄ", "k"},
            {"ᶅ", "l"},
            {"ᶆ", "m"},
            {"ᶇ", "n"},
            {"ᶈ", "p"},
            {"ᶉ", "r"},
            {"ᶊ", "s"},
            {"ᶌ", "v"},
            {"ᶍ", "x"},
            {"ᶎ", "z"},
            {"Ḁ", "A"},
            {"ḁ", "a"},
            {"Ḃ", "B"},
            {"ḃ", "b"},
            {"Ḅ", "B"},
            {"ḅ", "b"},
            {"Ḇ", "B"},
            {"ḇ", "b"},
            {"Ḉ", "C"},
            {"ḉ", "c"},
            {"Ḋ", "D"},
            {"ḋ", "d"},
            {"Ḍ", "D"},
            {"ḍ", "d"},
            {"Ḏ", "D"},
            {"ḏ", "d"},
            {"Ḑ", "D"},
            {"ḑ", "d"},
            {"Ḓ", "D"},
            {"ḓ", "d"},
            {"Ḕ", "E"},
            {"ḕ", "e"},
            {"Ḗ", "E"},
            {"ḗ", "e"},
            {"Ḙ", "E"},
            {"ḙ", "e"},
            {"Ḛ", "E"},
            {"ḛ", "e"},
            {"Ḝ", "E"},
            {"ḝ", "e"},
            {"Ḟ", "F"},
            {"ḟ", "f"},
            {"Ḡ", "G"},
            {"ḡ", "g"},
            {"Ḣ", "H"},
            {"ḣ", "h"},
            {"Ḥ", "H"},
            {"ḥ", "h"},
            {"Ḧ", "H"},
            {"ḧ", "h"},
            {"Ḩ", "H"},
            {"ḩ", "h"},
            {"Ḫ", "H"},
            {"ḫ", "h"},
            {"Ḭ", "I"},
            {"ḭ", "i"},
            {"Ḯ", "I"},
            {"ḯ", "i"},
            {"Ḱ", "K"},
            {"ḱ", "k"},
            {"Ḳ", "K"},
            {"ḳ", "k"},
            {"Ḵ", "K"},
            {"ḵ", "k"},
            {"Ḷ", "L"},
            {"ḷ", "l"},
            {"Ḹ", "L"},
            {"ḹ", "l"},
            {"Ḻ", "L"},
            {"ḻ", "l"},
            {"Ḽ", "L"},
            {"ḽ", "l"},
            {"Ḿ", "M"},
            {"ḿ", "m"},
            {"Ṁ", "M"},
            {"ṁ", "m"},
            {"Ṃ", "M"},
            {"ṃ", "m"},
            {"Ṅ", "N"},
            {"ṅ", "n"},
            {"Ṇ", "N"},
            {"ṇ", "n"},
            {"Ṉ", "N"},
            {"ṉ", "n"},
            {"Ṋ", "N"},
            {"ṋ", "n"},
            {"Ṍ", "O"},
            {"ṍ", "o"},
            {"Ṏ", "O"},
            {"ṏ", "o"},
            {"Ṑ", "O"},
            {"ṑ", "o"},
            {"Ṓ", "O"},
            {"ṓ", "o"},
            {"Ṕ", "P"},
            {"ṕ", "p"},
            {"Ṗ", "P"},
            {"ṗ", "p"},
            {"Ṙ", "R"},
            {"ṙ", "r"},
            {"Ṛ", "R"},
            {"ṛ", "r"},
            {"Ṝ", "R"},
            {"ṝ", "r"},
            {"Ṟ", "R"},
            {"ṟ", "r"},
            {"Ṡ", "S"},
            {"ṡ", "s"},
            {"Ṣ", "S"},
            {"ṣ", "s"},
            {"Ṥ", "S"},
            {"ṥ", "s"},
            {"Ṧ", "S"},
            {"ṧ", "s"},
            {"Ṩ", "S"},
            {"ṩ", "s"},
            {"Ṫ", "T"},
            {"ṫ", "t"},
            {"Ṭ", "T"},
            {"ṭ", "t"},
            {"Ṯ", "T"},
            {"ṯ", "t"},
            {"Ṱ", "T"},
            {"ṱ", "t"},
            {"Ṳ", "U"},
            {"ṳ", "u"},
            {"Ṵ", "U"},
            {"ṵ", "u"},
            {"Ṷ", "U"},
            {"ṷ", "u"},
            {"Ṹ", "U"},
            {"ṹ", "u"},
            {"Ṻ", "U"},
            {"ṻ", "u"},
            {"Ṽ", "V"},
            {"ṽ", "v"},
            {"Ṿ", "V"},
            {"ṿ", "v"},
            {"Ẁ", "W"},
            {"ẁ", "w"},
            {"Ẃ", "W"},
            {"ẃ", "w"},
            {"Ẅ", "W"},
            {"ẅ", "w"},
            {"Ẇ", "W"},
            {"ẇ", "w"},
            {"Ẉ", "W"},
            {"ẉ", "w"},
            {"Ẋ", "X"},
            {"ẋ", "x"},
            {"Ẍ", "X"},
            {"ẍ", "x"},
            {"Ẏ", "Y"},
            {"ẏ", "y"},
            {"Ẑ", "Z"},
            {"ẑ", "z"},
            {"Ẓ", "Z"},
            {"ẓ", "z"},
            {"Ẕ", "Z"},
            {"ẕ", "z"},
            {"ẖ", "h"},
            {"ẗ", "t"},
            {"ẘ", "w"},
            {"ẙ", "y"},
            {"ẚ", "a"},
            {"ẛ", "S"},
            {"ẞ", "Ss"},
            {"Ạ", "A"},
            {"ạ", "a"},
            {"Ả", "A"},
            {"ả", "a"},
            {"Ấ", "A"},
            {"ấ", "a"},
            {"Ầ", "A"},
            {"ầ", "a"},
            {"Ẩ", "A"},
            {"ẩ", "a"},
            {"Ẫ", "A"},
            {"ẫ", "a"},
            {"Ậ", "A"},
            {"ậ", "a"},
            {"Ắ", "A"},
            {"ắ", "a"},
            {"Ằ", "A"},
            {"ằ", "a"},
            {"Ẳ", "A"},
            {"ẳ", "a"},
            {"Ẵ", "A"},
            {"ẵ", "a"},
            {"Ặ", "A"},
            {"ặ", "a"},
            {"Ẹ", "E"},
            {"ẹ", "e"},
            {"Ẻ", "E"},
            {"ẻ", "e"},
            {"Ẽ", "E"},
            {"ẽ", "e"},
            {"Ế", "E"},
            {"ế", "e"},
            {"Ề", "E"},
            {"ề", "e"},
            {"Ể", "E"},
            {"ể", "e"},
            {"Ễ", "E"},
            {"ễ", "e"},
            {"Ệ", "E"},
            {"ệ", "e"},
            {"Ỉ", "I"},
            {"ỉ", "i"},
            {"Ị", "I"},
            {"ị", "i"},
            {"Ọ", "O"},
            {"ọ", "o"},
            {"Ỏ", "O"},
            {"ỏ", "o"},
            {"Ố", "O"},
            {"ố", "o"},
            {"Ồ", "O"},
            {"ồ", "o"},
            {"Ổ", "O"},
            {"ổ", "o"},
            {"Ỗ", "O"},
            {"ỗ", "o"},
            {"Ộ", "O"},
            {"ộ", "o"},
            {"Ớ", "O"},
            {"ớ", "o"},
            {"Ờ", "O"},
            {"ờ", "o"},
            {"Ở", "O"},
            {"ở", "o"},
            {"Ỡ", "O"},
            {"ỡ", "o"},
            {"Ợ", "O"},
            {"ợ", "o"},
            {"Ụ", "U"},
            {"ụ", "u"},
            {"Ủ", "U"},
            {"ủ", "u"},
            {"Ứ", "U"},
            {"ứ", "u"},
            {"Ừ", "U"},
            {"ừ", "u"},
            {"Ử", "U"},
            {"ử", "u"},
            {"Ữ", "U"},
            {"ữ", "u"},
            {"Ự", "U"},
            {"ự", "u"},
            {"Ỳ", "Y"},
            {"ỳ", "y"},
            {"Ỵ", "Y"},
            {"ỵ", "y"},
            {"Ỷ", "Y"},
            {"ỷ", "y"},
            {"Ỹ", "Y"},
            {"ỹ", "y"},
            {"ἀ", "a"},
            {"ἁ", "a"},
            {"ἂ", "a"},
            {"ἃ", "a"},
            {"ἄ", "a"},
            {"ἅ", "a"},
            {"ἆ", "a"},
            {"ἇ", "a"},
            {"Ἀ", "A"},
            {"Ἁ", "A"},
            {"Ἂ", "A"},
            {"Ἃ", "A"},
            {"Ἄ", "A"},
            {"Ἅ", "A"},
            {"Ἆ", "A"},
            {"Ἇ", "A"},
            {"ἐ", "e"},
            {"ἑ", "e"},
            {"ἒ", "e"},
            {"ἓ", "e"},
            {"ἔ", "e"},
            {"ἕ", "e"},
            {"Ἐ", "E"},
            {"Ἑ", "E"},
            {"Ἒ", "E"},
            {"Ἓ", "E"},
            {"Ἔ", "E"},
            {"Ἕ", "E"},
            {"ἠ", "e"},
            {"ἡ", "e"},
            {"ἢ", "e"},
            {"ἣ", "e"},
            {"ἤ", "e"},
            {"ἥ", "e"},
            {"ἦ", "e"},
            {"ἧ", "e"},
            {"Ἠ", "E"},
            {"Ἡ", "E"},
            {"Ἢ", "E"},
            {"Ἣ", "E"},
            {"Ἤ", "E"},
            {"Ἥ", "E"},
            {"Ἦ", "E"},
            {"Ἧ", "E"},
            {"ἰ", "i"},
            {"ἱ", "i"},
            {"ἲ", "i"},
            {"ἳ", "i"},
            {"ἴ", "i"},
            {"ἵ", "i"},
            {"ἶ", "i"},
            {"ἷ", "i"},
            {"Ἰ", "I"},
            {"Ἱ", "I"},
            {"Ἲ", "I"},
            {"Ἳ", "I"},
            {"Ἴ", "I"},
            {"Ἵ", "I"},
            {"Ἶ", "I"},
            {"Ἷ", "I"},
            {"ὀ", "o"},
            {"ὁ", "o"},
            {"ὂ", "o"},
            {"ὃ", "o"},
            {"ὄ", "o"},
            {"ὅ", "o"},
            {"Ὀ", "O"},
            {"Ὁ", "O"},
            {"Ὂ", "O"},
            {"Ὃ", "O"},
            {"Ὄ", "O"},
            {"Ὅ", "O"},
            {"ὐ", "u"},
            {"ὑ", "u"},
            {"ὒ", "u"},
            {"ὓ", "u"},
            {"ὔ", "u"},
            {"ὕ", "u"},
            {"ὖ", "u"},
            {"ὗ", "u"},
            {"Ὑ", "U"},
            {"Ὓ", "U"},
            {"Ὕ", "U"},
            {"Ὗ", "U"},
            {"ὠ", "o"},
            {"ὡ", "o"},
            {"ὢ", "o"},
            {"ὣ", "o"},
            {"ὤ", "o"},
            {"ὥ", "o"},
            {"ὦ", "o"},
            {"ὧ", "o"},
            {"Ὠ", "O"},
            {"Ὡ", "O"},
            {"Ὢ", "O"},
            {"Ὣ", "O"},
            {"Ὤ", "O"},
            {"Ὥ", "O"},
            {"Ὦ", "O"},
            {"Ὧ", "O"},
            {"ὰ", "a"},
            {"ά", "a"},
            {"ὲ", "e"},
            {"έ", "e"},
            {"ὴ", "e"},
            {"ή", "e"},
            {"ὶ", "i"},
            {"ί", "i"},
            {"ὸ", "o"},
            {"ό", "o"},
            {"ὺ", "u"},
            {"ύ", "u"},
            {"ὼ", "o"},
            {"ώ", "o"},
            {"ᾀ", "a"},
            {"ᾁ", "a"},
            {"ᾂ", "a"},
            {"ᾃ", "a"},
            {"ᾄ", "a"},
            {"ᾅ", "a"},
            {"ᾆ", "a"},
            {"ᾇ", "a"},
            {"ᾈ", "A"},
            {"ᾉ", "A"},
            {"ᾊ", "A"},
            {"ᾋ", "A"},
            {"ᾌ", "A"},
            {"ᾍ", "A"},
            {"ᾎ", "A"},
            {"ᾏ", "A"},
            {"ᾐ", "e"},
            {"ᾑ", "e"},
            {"ᾒ", "e"},
            {"ᾓ", "e"},
            {"ᾔ", "e"},
            {"ᾕ", "e"},
            {"ᾖ", "e"},
            {"ᾗ", "e"},
            {"ᾘ", "E"},
            {"ᾙ", "E"},
            {"ᾚ", "E"},
            {"ᾛ", "E"},
            {"ᾜ", "E"},
            {"ᾝ", "E"},
            {"ᾞ", "E"},
            {"ᾟ", "E"},
            {"ᾠ", "o"},
            {"ᾡ", "o"},
            {"ᾢ", "o"},
            {"ᾣ", "o"},
            {"ᾤ", "o"},
            {"ᾥ", "o"},
            {"ᾦ", "o"},
            {"ᾧ", "o"},
            {"ᾨ", "O"},
            {"ᾩ", "O"},
            {"ᾪ", "O"},
            {"ᾫ", "O"},
            {"ᾬ", "O"},
            {"ᾭ", "O"},
            {"ᾮ", "O"},
            {"ᾯ", "O"},
            {"ᾰ", "a"},
            {"ᾱ", "a"},
            {"ᾲ", "a"},
            {"ᾳ", "a"},
            {"ᾴ", "a"},
            {"ᾶ", "a"},
            {"ᾷ", "a"},
            {"Ᾰ", "A"},
            {"Ᾱ", "A"},
            {"Ὰ", "A"},
            {"Ά", "A"},
            {"ᾼ", "A"},
            {"ι", "i"},
            {"ῂ", "e"},
            {"ῃ", "e"},
            {"ῄ", "e"},
            {"ῆ", "e"},
            {"ῇ", "e"},
            {"Ὲ", "E"},
            {"Έ", "E"},
            {"Ὴ", "E"},
            {"Ή", "E"},
            {"ῌ", "E"},
            {"ῐ", "i"},
            {"ῑ", "i"},
            {"ῒ", "i"},
            {"ΐ", "i"},
            {"ῖ", "i"},
            {"ῗ", "i"},
            {"Ῐ", "I"},
            {"Ῑ", "I"},
            {"Ὶ", "I"},
            {"Ί", "I"},
            {"ῠ", "u"},
            {"ῡ", "u"},
            {"ῢ", "u"},
            {"ΰ", "u"},
            {"ῤ", "R"},
            {"ῥ", "R"},
            {"ῦ", "u"},
            {"ῧ", "u"},
            {"Ῠ", "U"},
            {"Ῡ", "U"},
            {"Ὺ", "U"},
            {"Ύ", "U"},
            {"Ῥ", "R"},
            {"ῲ", "o"},
            {"ῳ", "o"},
            {"ῴ", "o"},
            {"ῶ", "o"},
            {"ῷ", "o"},
            {"Ὸ", "O"},
            {"Ό", "O"},
            {"Ὼ", "O"},
            {"Ώ", "O"},
            {"ῼ", "O"},
            {"⁊", "7"},
            {"⁋", "PP"},
            {"⁰", "0"},
            {"ⁱ", "i"},
            {"⁴", "4"},
            {"⁵", "5"},
            {"⁶", "6"},
            {"⁷", "7"},
            {"⁸", "8"},
            {"⁹", "9"},
            {"ⁿ", "n"},
            {"₀", "0"},
            {"₁", "1"},
            {"₂", "2"},
            {"₃", "3"},
            {"₄", "4"},
            {"₅", "5"},
            {"₆", "6"},
            {"₇", "7"},
            {"₈", "8"},
            {"₉", "9"},
            {"ₐ", "a"},
            {"ₑ", "e"},
            {"ₒ", "o"},
            {"ₓ", "x"},
            {"ₕ", "h"},
            {"ₖ", "k"},
            {"ₗ", "l"},
            {"ₘ", "m"},
            {"ₙ", "n"},
            {"ₚ", "p"},
            {"ₛ", "s"},
            {"ₜ", "t"},
            {"₠", "ECU"},
            {"₡", "CL"},
            {"₢", "Cr"},
            {"₣", "FF"},
            {"₤", "L"},
            {"₥", "mil"},
            {"₦", "N"},
            {"₧", "Pts"},
            {"₨", "Rs"},
            {"₩", "W"},
            {"₪", "NS"},
            {"₫", "D"},
            {"€", "EUR"},
            {"₭", "K"},
            {"₮", "T"},
            {"₯", "Dr"},
            {"₰", "Pf"},
            {"₱", "P"},
            {"₲", "G"},
            {"₳", "A"},
            {"₴", "UAH"},
            {"₶", "L"},
            {"₷", "Sm"},
            {"₸", "T"},
            {"₹", "Rs"},
            {"₺", "L"},
            {"₻", "M"},
            {"₼", "m"},
            {"₽", "R"},
            {"₾", "l"},
            {"₿", "BTC"},
            {"ℂ", "C"},
            {"ℊ", "g"},
            {"ℋ", "H"},
            {"ℌ", "H"},
            {"ℍ", "H"},
            {"ℎ", "h"},
            {"ℐ", "I"},
            {"ℑ", "I"},
            {"ℒ", "L"},
            {"ℓ", "l"},
            {"ℕ", "N"},
            {"ℙ", "P"},
            {"ℚ", "Q"},
            {"ℛ", "R"},
            {"ℜ", "R"},
            {"ℝ", "R"},
            {"℡", "TEL"},
            {"ℤ", "Z"},
            {"ℨ", "Z"},
            {"K", "K"},
            {"Å", "A"},
            {"ℬ", "B"},
            {"ℭ", "C"},
            {"℮", "e"},
            {"ℯ", "e"},
            {"ℰ", "E"},
            {"ℱ", "F"},
            {"Ⅎ", "F"},
            {"ℳ", "M"},
            {"ℴ", "o"},
            {"ℹ", "i"},
            {"℻", "FAX"},
            {"ⅅ", "D"},
            {"ⅆ", "d"},
            {"ⅇ", "e"},
            {"ⅈ", "i"},
            {"ⅉ", "j"},
            {"ⅎ", "F"},
            {"Ⅰ", "I"},
            {"Ⅱ", "II"},
            {"Ⅲ", "III"},
            {"Ⅳ", "IV"},
            {"Ⅴ", "V"},
            {"Ⅵ", "VI"},
            {"Ⅶ", "VII"},
            {"Ⅷ", "VIII"},
            {"Ⅸ", "IX"},
            {"Ⅹ", "X"},
            {"Ⅺ", "XI"},
            {"Ⅻ", "XII"},
            {"Ⅼ", "L"},
            {"Ⅽ", "C"},
            {"Ⅾ", "D"},
            {"Ⅿ", "M"},
            {"ⅰ", "i"},
            {"ⅱ", "ii"},
            {"ⅲ", "iii"},
            {"ⅳ", "iv"},
            {"ⅴ", "v"},
            {"ⅵ", "vi"},
            {"ⅶ", "vii"},
            {"ⅷ", "viii"},
            {"ⅸ", "ix"},
            {"ⅹ", "x"},
            {"ⅺ", "xi"},
            {"ⅻ", "xii"},
            {"ⅼ", "l"},
            {"ⅽ", "c"},
            {"ⅾ", "d"},
            {"ⅿ", "m"},
    };

    static {
        // Initialise things here
        Function<String[], Integer> useFirstCode = (String[] comb) -> ((int) comb[0].charAt(0));
        converterAVL = new MyAVLTree<>(useFirstCode);
        for (String[] combination : accentAndConvertedAccent) {
            converterAVL.insert(combination);
        }

    }

    public static void main(String[] args) {
        String unconverted = "Abrahaⅿ Linⅽℴln has a big bearⅾ";
        long initTime = System.nanoTime();
        String str = convertAccents(unconverted);
        long timeAfter = System.nanoTime();
        String str2 = convertAccentsFaster(unconverted);
        long timeAfter2 = System.nanoTime();

        System.out.println(timeAfter - initTime);
        System.out.println(str);
        System.out.println(timeAfter2 - timeAfter);
        System.out.println(str2);
    }

    public static String convertAccentsFaster(String str) {
        if (str == null)
            return "";

        StringBuilder output = new StringBuilder();
        for (char character : str.toCharArray()) {
            if ((int) character >= 32 && (int) character <= 126)
                output.append(character);
            else
                output.append(converterAVL.search((int) character)[1]);
        }
        return output.toString();
    }

    public static String convertAccents(String str) {
        if (str == null || str.equals("")) {
            return "";
        }

        String replacedString = str;
        for (int i = 0; i < accentAndConvertedAccent.length; i++) {
            replacedString = replacedString.replace(accentAndConvertedAccent[i][0],
                    accentAndConvertedAccent[i][1]);
        }

        return replacedString;
    }
}
