package com.epam.rd.autocode.bstprettyprint;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class PrintableTreeTest {

    @ParameterizedTest
    @MethodSource("testCases")
    public void test(String testCaseName, int[] elements, String prettyPrint) {
        final PrintableTree tree = PrintableTree.getInstance();
        for (int element : elements) {
            tree.add(element);
        }

        assertEquals(
                prettyPrint,
                tree.prettyPrint(),
                () -> testCaseName + " test case has failed.");

    }

    static Stream<Arguments> testCases() throws IOException {
        final Path testCaseRoot = Paths.get("src", "test", "resources", "test-cases");

        return Files.walk(testCaseRoot, 1)
                .filter(Files::isDirectory)
                .filter(path -> !testCaseRoot.equals(path))
                .map(testCase -> arguments(
                        testCase.getFileName().toString(),
                        readElements(testCase),
                        readPrettyPrint(testCase)
                ));

    }

    private static int[] readElements(final Path testCase) {
        try (Stream<String> lines = Files.lines(testCase.resolve("elements.txt"))) {
            return lines.mapToInt(Integer::valueOf).toArray();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String readPrettyPrint(final Path testCase) {
        try (Stream<String> lines = Files.lines(testCase.resolve("prettyprint.txt"))) {
            return lines.collect(Collectors.joining("\n")) + "\n";
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}