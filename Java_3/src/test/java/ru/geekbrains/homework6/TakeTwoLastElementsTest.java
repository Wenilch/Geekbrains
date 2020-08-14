package ru.geekbrains.homework6;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TakeTwoLastElementsTest {
    private static Homework6Application application;

    @BeforeClass
    public static void init(){
        application = new Homework6Application();
    }

    @Test
    public void takeTwoLastElementsSuccessTest() {
        int[] ints = application.takeTwoLastElements(new int[]{1, 2, 4, 4, 2, 3, 4, 1, 7});
        Assert.assertArrayEquals(new int[]{1, 7}, ints);
    }

    @Test(expected = NullPointerException.class)
    public void takeTwoLastElementsNullPointerExceptionTest() {
        application.takeTwoLastElements(null);
    }

    @Test(expected = RuntimeException.class)
    public void takeTwoLastElementsRuntimeExceptionTest() {
        application.takeTwoLastElements(new int[]{1, 2});
    }
}
