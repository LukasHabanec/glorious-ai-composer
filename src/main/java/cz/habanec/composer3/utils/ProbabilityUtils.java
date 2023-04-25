package cz.habanec.composer3.utils;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class ProbabilityUtils {

    public static final Random RANDOM = new Random();

    public static int rollDice(int amount) {
        int result = 0;
        for (int i = 0; i < amount; i++) {
            result += RANDOM.nextInt(1, 7);
        }
        return result;
    }

    public static List<Integer> getRandomAndShuffledCustomOptionsListEvenlyBounded(int amount, int mainBottom, int mainTop) {
        int boundDifference = mainTop - mainBottom + 1;
        if (amount > boundDifference) {
            int newAmount1 = boundDifference - mainBottom;
            int newAmount2 = amount - newAmount1;
            var ret = getRandomAndShuffledCustomOptionsListEvenlyBounded(newAmount1, mainBottom, mainTop);
            ret.addAll(getRandomAndShuffledCustomOptionsListEvenlyBounded(newAmount2, mainBottom, mainTop));
            return ret;
        } else if (amount == boundDifference) {
            List<Integer> ret = Stream.iterate(mainBottom, num -> num <= mainTop, num -> ++num).collect(Collectors.toList());
            Collections.shuffle(ret);
            return ret;
        }
        List<Integer> list = new ArrayList<>(amount);
        float interval = (mainTop - mainBottom + 1) / (float) amount;
        float bottom = mainBottom;
        float top = Math.round(mainBottom + interval);

        for (int i = 0; i < amount; i++) {
            if (i == amount - 1) {
                top = mainTop + 1;
            }
            System.out.printf("Bottom: %f, top: %f%n", bottom, top);
            list.add(RANDOM.nextInt(Math.round(bottom), Math.round(top)));
            bottom = top;
            top += interval;
        }
        Collections.shuffle(list);
        System.out.println("Returning: " + list);
        return list;
    }}
