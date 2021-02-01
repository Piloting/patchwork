package ru.pilot.pathwork;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Trash {

    public void main2(String[] args) {
        int[] array = new Random().ints(100).toArray();
        List<Integer> original = Arrays.stream(array).boxed().collect(Collectors.toList());

        System.out.println(Arrays.toString(array));

        array = sort(array);

        System.out.println(Arrays.toString(array));
        List<Integer> sorted = Arrays.stream(array).boxed().collect(Collectors.toList());

        // отсортированы?
        for (int i = 1; i < array.length; i++) {
            if (array[i-1] > array[i]){
                throw new RuntimeException(array[i-1] + " > " + array[i]);
            }
        }

        if (original.size() != sorted.size()){
            throw new RuntimeException("Не совпадает размер!");
        }

        original.removeAll(sorted);
        if (!original.isEmpty()){
            throw new RuntimeException("В отсортированном списке не хвататет " + Arrays.toString(array));
        }
    }

    private static int[] sort(int[] array) {
        if (array.length > 2) {
            int midle = array.length / 2;
            int[] left = sort(Arrays.copyOfRange(array, 0, midle));
            int[] right = sort(Arrays.copyOfRange(array, midle, array.length));
            return merge(left, right);
        } else if (array.length < 2) {
            return array;
        }

        int first = array[0];
        int second = array[1];
        if (first > second) {
            array[0] = second;
            array[1] = first;
        }
        return array;
    }

    private static int[] merge(int[] left, int[] right) {
        int left_i = 0;
        int right_i = 0;
        int merge_i = 0;
        int[] merged = new int[left.length + right.length];
        while (merge_i < merged.length) {
            while (left.length > left_i && (right.length <= right_i || left[left_i] <= right[right_i])) {
                merged[merge_i++] = left[left_i++];
            }
            while (right.length > right_i && (left.length <= left_i || right[right_i] <= left[left_i])) {
                merged[merge_i++] = right[right_i++];
            }
        }
        return merged;
    }
}
