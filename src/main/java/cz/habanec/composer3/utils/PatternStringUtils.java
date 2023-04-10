package cz.habanec.composer3.utils;

import lombok.Value;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@UtilityClass
public class PatternStringUtils {

    public static final String COMMA_REGEX_DELIMITER = ",[\s]*";
    public static final String WHITESPACE_REGEX_DELIMITER = "[\s]+";
    public static final String UNDERSCORE_DELIMITER = "_";
    public static final String COMMA_JOIN_DELIMITER = ",";


    public static String stringyfyPattern(List<Integer> list) {
        return list.stream().map(String::valueOf).collect(Collectors.joining(COMMA_JOIN_DELIMITER));
    }

    public static List<Integer> extractIntegerListFrom(String string, String delimiter) {
        if (!StringUtils.hasLength(string)) {
            return List.of();
        }
        return Arrays.stream(string.trim().split(delimiter))
                .map(Integer::valueOf)
                .toList();
    }

    public static int getUniqueSymbolsCount(String schema) {
        return (int) schema.chars().distinct().count();
    }

    public static Map<Integer, Integer> extractSchemaMap(String scheme) {
        if (!StringUtils.hasLength(scheme)) {
            return Map.of();
        }
        return Arrays.stream(scheme.split(WHITESPACE_REGEX_DELIMITER))
                .map(string -> {
                    String[] pair = string.split(UNDERSCORE_DELIMITER);
                    return new SchemaEntry(
                            Integer.valueOf(pair[0]),
                            Integer.valueOf(pair[1]));
                })
                .collect(Collectors.toMap(SchemaEntry::getMeasureIndex, SchemaEntry::getValue));
    }

    public static String stringifyRepetitionsPattern(List<Integer> repetitions) {
        StringBuilder sb = new StringBuilder();
        int value;
        int size = repetitions.size();
        for (int i = 0; i < size; i++) {
            value = repetitions.get(i);
            if (value == 1) {
                continue;
            }
            sb.append(i).append("_").append(value).append(" ");
        }
        return sb.toString().trim();
    }

    public static boolean isPatternNotUnique(String newPattern, List<String> rhythmPatterns) {
        return rhythmPatterns.stream().anyMatch(newPattern::equals);
    }

    //    public static String getHarmonyFragmentationForView(int harmonyFragmentationIndex) {
//        int[] harmonyFragmentationPattern = HarmonyFragmentation.patterns[harmonyFragmentationIndex];
//        StringBuilder sb3 = new StringBuilder("[");
//        for (int i = 0; i < harmonyFragmentationPattern.length; i++) {
//            sb3.append(harmonyFragmentationPattern[i]);
//            for (int r = 0; r < harmonyFragmentationPattern[i] - 1; r++) {
//                sb3.append("_");
//            }
//        }
//        return sb3.append("]").toString();
//    }


    @Value
    private static class SchemaEntry {
        Integer measureIndex, value;
    }
}
