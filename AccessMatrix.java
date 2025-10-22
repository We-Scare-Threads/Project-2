//Task 1: Implementation with an Access Matrix
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class AccessMatrix {
    static Lock[] resources;
    public static void main(String[] args) {

        System.out.println("\nAccess Matrix Key: ");
        System.out.println("R = Read, W = Write, B = Both (Read/Write), E = Empty/No Access, N = No Access (for Domains ONLY)");
        Random random = new Random();

        int numDomains = random.nextInt(5) + 3;
        int numObjects = random.nextInt(5) + 3;
        System.out.println("D: " + numDomains);
        System.out.println("O: " + numObjects + "\n");

        String[][] accMat = matrix(numDomains, numObjects);
        System.out.println("Access Matrix Rows:" + accMat.length + " Columns:" + accMat[0].length + "\n");

        resources = new Lock[numObjects + numDomains];
        for (int r = 0; r < (numObjects + numDomains); r++) {
            resources[r] = new ReentrantLock();
        }
        System.out.println("Resources holds: " + numObjects + " files(objects) and " + numDomains + " domains. " + resources.length + " in total.\n");
        System.out.println("Users holds: " + numDomains + " domains.\n");
        System.out.println("User Access Key: ");
        System.out.println("(O) = Accessed Object, (R) = Read, (W) = Write, (X) = Access Denied, (E) = No Assigned Permissions\n");

        for (int i = 0; i < numDomains; i++){
            Users user = new Users(i, accMat, resources, numDomains, numObjects);
            System.out.println("Starting User D" + i);
            user.start();
        }
    }

    public static String[][] matrix(int n, int m) {
        Random random = new Random();
        // e = empty, b = both r/w, n = n/a, a = allow, will format later
        String[] objperms = {"E", "R", "W", "B"};

        String[] domperms = {"E", "A"};

        String[][] accessmatrix = new String[n][m+n];
        System.out.println("Matrix: ");
        System.out.print("    ");
        int o, d;
        for (int i = 0; i < m + n; i++) {
            if (i < m) {
                System.out.print("O" + i + " ");
            }
            else {
                System.out.print("D" + (i - m) + " ");
            }
            if(i == m + n - 1){
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

