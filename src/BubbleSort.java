import java.util.Arrays;

public class BubbleSort {
    public static void bubblesort(int[] arr) {
        int n = arr.length;

        for (int i = 0; i < n -1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if(arr[j] > arr[j + 1]) {
                    // Swap arr[j] and arr[j + 1]
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] arrToSort ={64, 34, 25, 12, 22, 11, 90};
        System.out.println("Array before sorting");
        System.out.println(Arrays.toString(arrToSort));

        bubblesort(arrToSort);

        System.out.println("Array after sorting");
        System.out.println(Arrays.toString(arrToSort));

    }

    public static void printArray(int[] arr) {
        for (int value: arr) {
            System.out.println(value + " ");
        }
        System.out.println();
    }

}
