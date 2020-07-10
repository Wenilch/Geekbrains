package ru.geekbrains.homework.homework5;

public class Application {

    private static final int FULL_ARRAY_SIZE = 10_000_000;
    private static final int PART_ARRAY_SIZE = FULL_ARRAY_SIZE / 2;

    public static void main(String[] args) {
        synchronousComputing();
        asynchronousComputing();
    }

    public static void synchronousComputing() {
        float[] array = new float[FULL_ARRAY_SIZE];
        fillArray(array);

        long startTime = System.currentTimeMillis();
        calculationArrayValues(array);
        long endTime = System.currentTimeMillis();
        System.out.println("Время выполнения первого метода : " + (endTime - startTime));
    }

    public static void asynchronousComputing() {
        float[] array = new float[FULL_ARRAY_SIZE];
        fillArray(array);

        try {
            long startTime = System.currentTimeMillis();
            float[] array1 = new float[PART_ARRAY_SIZE];
            float[] array2 = new float[PART_ARRAY_SIZE];
            System.arraycopy(array, 0, array1, 0, PART_ARRAY_SIZE);
            System.arraycopy(array, PART_ARRAY_SIZE, array2, 0, PART_ARRAY_SIZE);

            Thread firstThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    calculationArrayValues(array1);
                }
            });
            firstThread.start();

            Thread secondThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    calculationArrayValues(array2);
                }
            });
            secondThread.start();

            firstThread.join();
            secondThread.join();

            System.arraycopy(array1, 0, array, 0, PART_ARRAY_SIZE);
            System.arraycopy(array2, 0, array, PART_ARRAY_SIZE, PART_ARRAY_SIZE);

            long endTime = System.currentTimeMillis();
            System.out.println("Время выполнения первого метода : " + (endTime - startTime));
        } catch (InterruptedException exception) {
            System.out.println(exception);
        }
    }

    private static void fillArray(float[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = 1;
        }
    }

    private static void calculationArrayValues(float[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = 1;
            array[i] = (float) (array[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
    }
}
