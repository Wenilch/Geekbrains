package ru.geekbrains.homework6;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class IsArrayContainJustOneAndFourTest {
    private static Homework6Application application;

    @BeforeClass
    public static void init() {
        application = new Homework6Application();
    }

    @Test
    public void isArrayContainJustOneAndFourSuccessTest() {
        Assert.assertEquals(true, application.isArrayContainJustOneAndFour(new int[]{1, 1, 1, 4, 4, 1, 4, 4}));
    }

    @Test(expected = NullPointerException.class)
    public void isArrayContainJustOneAndFourNullPointerExceptionTest() {
        application.isArrayContainJustOneAndFour(null);
    }
}
