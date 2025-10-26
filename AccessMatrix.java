//Task 1: Implementation with an Access Matrix

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class AccessMatrix {
    static Lock[] resourceLocks;
    static String[] data;
    public final static String[] Colors = {"Blue", "Red", "Green", "Yellow", "Purple", "Orange", "Pink", "Brown", "Black", "White"};

    public static void main(String[] args) {

        System.out.println("\nAccess Matrix Key: ");
        System.out.println("R = Read, W = Write, B = Both (Read/Write), E = Empty/No Access, N = No Access (for Domains ONLY)");
        Random random = new Random();

        int numDomains = random.nextInt(5) + 3;
        int numObjects = random.nextInt(5) + 3;
        System.out.println("Domains: " + numDomains);
        System.out.println("Objects: " + numObjects + "\n");

        String[][] accMat = matrix(numDomains, numObjects);
        System.out.println("Access Matrix Rows:" + accMat.length + " Columns:" + accMat[0].length + "\n");

        resourceLocks = new Lock[numObjects + numDomains];
        data = new String[numObjects + numDomains];

        for (int r = 0; r < (numObjects + numDomains); r++) {
            resourceLocks[r] = new ReentrantLock();
            data[r] = Colors[random.nextInt(Colors.length)];
        }
        System.out.println("Resources Array holds: " + numObjects + " files(objects) and " + numDomains + " domains. " + resourceLocks.length + " in total.\n");
        System.out.println("Num of Users (domains) shall be: " + numDomains + ".\n");
        System.out.println("User Access Key: ");
        System.out.println("(F) = Accessed File, (R) = Read, (W) = Write, (E) = No Assigned Permissions, **An attached X = Access Denied**\n");

        for (int i = 0; i < numDomains; i++){
            Users user = new Users(i, accMat, resourceLocks, numDomains, numObjects, data, Colors);
            user.start();
        }
    }

    public static String[][] matrix(int n, int m) {
        Random random = new Random();
        // e = empty, b = both r/w, n = n/a, a = allow, will format later
        String[] objperms = {"E", "R", "W", "B"};

        String[] domperms = {"E", "A"};

        String[][] accessmatrix = new String[n][m+n];
        System.out.println("Matrix: " + n + " X " + m);
        System.out.print("    ");
        int o, d;
        for (int i = 0; i < m + n; i++) {
            if (i < m) {
                System.out.print("F" + i + " ");
            }
            else {
                System.out.print("D" + (i - m) + " ");
            }
            if (i == m + n - 1){
                System.out.println();
            }
        }
        for (int i = 0; i < n; i++) {
            System.out.print("D" + i + ": ");
            for (int j = 0; j < m + n; j++) {
                o = random.nextInt(4);
                d = random.nextInt(2);

                if (j < m) {
                    System.out.print(objperms[o] + "  ");
                    accessmatrix[i][j] = objperms[o];
                }
                else {
                    if (i + m == j) {
                        System.out.print("N  ");
                        accessmatrix[i][j] = "N";
                    }
                    else {
                        System.out.print(domperms[d] + "  ");
                        accessmatrix[i][j] = domperms[d];
                    }

                }

            }
            System.out.println();
        }
        return accessmatrix;
    }
}

