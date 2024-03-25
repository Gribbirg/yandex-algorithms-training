package binarysearch.newofficeplus;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Math.min;

public class NewOfficePlus {
    public static void main(String[] args) throws FileNotFoundException {
        var input = new File("input.txt");
//        var output = new File("output.txt");

        Scanner scanner = new Scanner(input);
        var firstLine = scanner.nextLine().split(" ");
        var height = Integer.parseInt(firstLine[0]);
        var width = Integer.parseInt(firstLine[1]);

        ArrayList<char[]> field = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            field.add(scanner.nextLine().toCharArray());
        }

        var sums = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i <= height; i++) {
            sums.add(new ArrayList<>());
            sums.getLast().add(0);
        }
        for (int i = 1; i <= width; i++) {
            sums.getFirst().add(0);
        }
        for (int i = 1; i <= height; i++) {
            for (int j = 1; j <= width; j++) {
                sums.get(i).add(sums.get(i).get(j - 1) + sums.get(i - 1).get(j) - sums.get(i - 1).get(j - 1) + ((field.get(i - 1)[j - 1] == '#') ? 1 : 0));
            }
        }

        var maxSize = min(width, height) / 3;

        var currentSize = 1;
        var lineIndex = 1;
        while (lineIndex <= height) {
            var charIndex = currentSize;
            while (charIndex <= width) {
                if (checkPlus(sums, lineIndex, charIndex, currentSize)) {
                    var l = currentSize;
                    var r = maxSize;
                    while (l < r) {
                        var medium = (l + r + 1) / 2;
                        if (checkPlus(sums, lineIndex, charIndex, medium)) {
                            l = medium;
                        } else {
                            r = medium - 1;
                        }
                    }
                    currentSize = l + 1;
                }
                charIndex++;
            }
            lineIndex++;
        }

        System.out.println(currentSize - 1);
    }

    private static boolean checkPlus(ArrayList<ArrayList<Integer>> sums, int startLine, int startChar, int size) {
        return startLine - size >= 0 && startLine + size * 2 < sums.size() &&
                startChar - size >= 0 && startChar + size * 2 < sums.getFirst().size() &&
                checkSquare(sums, startLine, startChar - size, size, size * 3) &&
                checkSquare(sums, startLine - size, startChar, size * 3, size);
    }

    private static boolean checkSquare(ArrayList<ArrayList<Integer>> sums, int startLine, int startChar, int sizeLine, int sizeChar) {
        return sums.get(startLine).get(startChar) +
                sums.get(startLine + sizeLine).get(startChar + sizeChar) -
                sums.get(startLine).get(startChar + sizeChar) -
                sums.get(startLine + sizeLine).get(startChar) == sizeLine * sizeChar;
    }
}
