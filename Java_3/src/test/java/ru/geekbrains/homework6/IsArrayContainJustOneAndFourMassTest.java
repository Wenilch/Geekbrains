package ru.geekbrains.homework6;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class IsArrayContainJustOneAndFourMassTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                        {true, new int[]{1, 1, 1, 4, 4, 1, 4, 4}},
                        {false, new int[]{1, 2, 4, 4, 2, 3, 4, 1, 7, 4, 2, 3, 4, 1, 7, 4, 2, 3, 4, 1, 7, 5, 5}},
                        {false, new int[]{1, 2, 4, 4, 2, 3, 4, 1, 7, 10, 0}},
                        {false, new int[]{1, 2, 4, 4, 2, 3, 4}},
                }
        );
    }

    boolean expected;
    int[] source;

    public IsArrayContainJustOneAndFourMassTest(boolean expected, int[] source) {
        this.expected = expected;
        this.source = source;
    }

    private static Homework6Application application;

    @BeforeClass
    public static void init() {
        application = new Homework6Application();
    }

    @Test
    public void isArrayContainJustOneAndFourMassTestSuccessTest() {
        Assert.assertEquals(expected, application.isArrayContainJustOneAndFour(source));
    }
}
