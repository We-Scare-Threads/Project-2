//Task 3 - Updated with Users_CList

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class CapList {
    static Lock[] resources;
    static String[] data;
    public final static String[] Colors = {"Blue", "Red", "Green", "Yellow", "Purple", "Orange", "Pink", "Brown", "Black", "White"};


    public static void main(String[] args) {
        System.out.println("\nCapability List Key: ");
        System.out.println("R = Read, W = Write, B = Both (Read/Write), E = Empty/No Access, A = Allow Domain Switch");
        Random random = new Random();

        int numDomains = random.nextInt(5) + 3;
        int numObjects = random.nextInt(5) + 3;

        System.out.println("Domains: " + numDomains);
        System.out.println("Files: " + numObjects + "\n");

        LinkedList<LinkedList<String>> capList = CList(numDomains, numObjects);

        resources = new Lock[numObjects + numDomains];
        data = new String[numObjects + numDomains];
        for (int r = 0; r < (numObjects + numDomains); r++) {
            resources[r] = new ReentrantLock();
            data[r] = Colors[random.nextInt(Colors.length)];
        }
        System.out.println("Resources Array holds: " + numObjects + " files(objects) and " + numDomains + " domains. " + resources.length + " in total.\n");
        System.out.println("Num of Users (domains) shall be: " + numDomains + ".\n");
        System.out.println("User Access Key: ");
        System.out.println("(F) = Accessed File, (R) = Read, (W) = Write, (A) = Allow Domain Switch, **An attached X = Access Denied**\n");

        for (int i = 0; i < numDomains; i++) {
            Users_CList user = new Users_CList(i, capList, resources, numDomains, numObjects, data, Colors);
            user.start();
        }
    }

    public static LinkedList<LinkedList<String>> CList(int n, int m) {
        Random random = new Random();
        int r;
        // e = empty, b = both r/w, n = n/a, a = allow, will format later
        String[] objperms = {"E", "R", "W", "B"};
        String[] domperms = {"E", "A"};

        LinkedList<LinkedList<String>> lists = new LinkedList<>();

        for (int i = 0; i < n; i++) {
            LinkedList<String> cList = new LinkedList<>();
            lists.add(cList);
        }

        System.out.println("Capability List:");

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                r = random.nextInt(4);
                lists.get(i).add(objperms[r]);
            }
            for (int k = 0; k < n; k++) {
                if (k == i) {
                    lists.get(i).add("E");
                } else {
                    r = random.nextInt(2);
                    lists.get(i).add(domperms[r]);
                }
            }
        }

        for (int i = 0; i < n; i++) {
            System.out.print("D" + i + " --> ");
            boolean first_entry = true;

            for (int j = 0; j < m; j++) {
                String perms = lists.get(i).get(j);
                if (!perms.equals("E") && (!perms.equals("N"))) {
                    if (!first_entry) System.out.print(" -> ");
                    System.out.print("F" + j + ":" + perms);
                    first_entry = false;
                }
            }

            for (int k = 0; k < n; k++) {
                String perm = lists.get(i).get(m + k);
                if (!perm.equals("E") && (!perm.equals("N"))) {
                    if (!first_entry) System.out.print(" -> ");
                    System.out.print("D" + k + ":" + perm);
                    first_entry = false;
                }
            }

            if (first_entry) {
                System.out.print("No permissions");
            }
            System.out.println();
        }

        return lists;
    }
}