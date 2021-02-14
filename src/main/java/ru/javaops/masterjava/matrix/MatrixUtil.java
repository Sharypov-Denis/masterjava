package ru.javaops.masterjava.matrix;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        final int[][] cacheMatrix = new int[matrixSize][matrixSize];

        //for (int i = 0; i < 10; i++) {
        executor.submit(new Runnable() {
            public void run() {
                for (int i = 0; i < matrixSize; ++i) {
                    for (int j = 0; j < matrixSize; ++j) {
                        cacheMatrix[i][j] = matrixB[j][i];
                    }
                }

                for (int i = 0; i < matrixSize; ++i) {
                    for (int j = 0; j < matrixSize; ++j) {
                        //matrixC[i][j] = 0;
                        int sum = 0;
                        for (int k = 0; k < matrixSize; ++k) {
                            //matrixC[i][j] = matrixA[i][k] * cacthMatrix[j][k];
                            sum += matrixA[i][k] * cacheMatrix[j][k];//matrixB[k][j];
                        }
                        matrixC[i][j] = sum;
                    }
                }
            }
        });
        // }
        executor.shutdown();

        return matrixC;
    }

    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        final int[][] cathMatrix = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; ++i) {
            for (int j = 0; j < matrixSize; ++j) {
                cathMatrix[i][j] = matrixB[j][i];
            }
        }
        for (int i = 0; i < matrixSize; ++i) {
            for (int j = 0; j < matrixSize; ++j) {
                //matrixC[i][j] = 0;
                int sum = 0;
                for (int k = 0; k < matrixSize; ++k) {
                    //matrixC[i][j] = matrixA[i][k] * cathMatrix[j][k];
                    sum += matrixA[i][k] * cathMatrix[j][k];//matrixB[k][j];
                }
                matrixC[i][j] = sum;
            }
        }
        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
