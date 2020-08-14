package ru.geekbrains.homework6;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TakeTwoLastElementsMassTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                        {new int[]{1, 7}, new int[]{1, 2, 4, 4, 2, 3, 4, 1, 7}},
                        {new int[]{5, 5}, new int[]{1, 2, 4, 4, 2, 3, 4, 1, 7, 4, 2, 3, 4, 1, 7, 4, 2, 3, 4, 1, 7, 5, 5}},
                        {new int[]{10, 0}, new int[]{1, 2, 4, 4, 2, 3, 4, 1, 7, 10, 0}},
                        {new int[]{3, 4}, new int[]{1, 2, 4, 4, 2, 3, 4}},
                }
        );
    }

    int[] expected;
    int[] source;

    public TakeTwoLastElementsMassTest(int[] expected, int[] source) {
        this.expected = expected;
        this.source = source;
    }

    private static Homework6Application application;

    @BeforeClass
    public static void init() {
        application = new Homework6Application();
    }

    @Test
    public void takeTwoLastElementsSuccessTest() {
        int[] ints = application.takeTwoLastElements(source);
        Assert.assertArrayEquals(expected, ints);
    }
}
