//Task 2: Implementation with Access Lists for Objects
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class AccessList {
    static Lock[] resources;
    public static void main(String[] args) {
        System.out.println("\nAccess List Key: ");
        System.out.println("R = Read, W = Write, B = Both (Read/Write), E = Empty/No Access, N = No Access (for Domains ONLY)");
        Random random = new Random();

        int numDomains = random.nextInt(5) + 3;
        int numObjects = random.nextInt(5) + 3;

        System.out.println("D: " + numDomains);
        System.out.println("O: " + numObjects + "\n");

        LinkedList<LinkedList<String>> accList = AList(numDomains, numObjects);

        resources = new Lock[numObjects + numDomains];
        for (int r = 0; r < (numObjects + numDomains); r++) {
            resources[r] = new ReentrantLock();
        }
        System.out.println("Resources Array holds: " + numObjects + " files(objects) and " + numDomains + " domains. " + resources.length + " in total.\n");
        System.out.println("Num of Users (domains) shall be: " + numDomains + ".\n");
        System.out.println("User Access Key: ");
        System.out.println("(O) = Accessed Object, (R) = Read, (W) = Write, (E) = No Assigned Permissions, **An attached X = Access Denied**\n");

        //for (int i = 0; i < numDomains; i++){
        //    Users user = new Users(i, aListD, aListF, resources, numDomains, numObjects);
        //    user.start();
        //}
    }
    public static LinkedList<LinkedList<String>> AList(int n, int m) {
        Random random = new Random();
        int r;
        // e = empty, b = both r/w, n = n/a, a = allow, will format later
        String[] objperms = {"E", "R", "W", "B"};
        String[] domperms = {"E", "A"};

        LinkedList<LinkedList<String>> lists = new LinkedList<>();

        for(int i=0; i<n+m; i++) {
            LinkedList<String> aList = new LinkedList<>();
            lists.add(aList);
        }

        System.out.println("Access List:");
        for(int j=0; j<n+m; j++) {
            for (int i = 0; i < m; i++) {
                if(j<(m)){ // Object permissions
                    r = random.nextInt(4);
                    lists.get(j).add(objperms[r]);
                }
                else{
                    r = random.nextInt(2);
                    lists.get(j).add(domperms[r]);
                }
            }

        }
        for (int i = 0; i < m; i++) {
            int lastAccess = m-1;
            System.out.print("O" + i + " --> [");
            for(int j=0; j<lists.get(i).size(); j++) {
                while(lists.get(i).get((lastAccess)).equals("E") && lastAccess > 0) lastAccess--;
                if(j!=(lastAccess)){
                    if(!(lists.get(i).get(j).equals("E"))){
                        System.out.print("D" + j + ": " + lists.get(i).get(j) + " -> ");
                    }
                }
                else{
                    if(!(lists.get(i).get(j).equals("E"))){
                        System.out.print("D" + j + ": " + lists.get(i).get(j));
                    }
                }
            }
            System.out.println("]");
        }
        for (int i = 0; i < n; i++) {
            int lastAccess = m-1;
            while(lists.get(i+m).get((lastAccess)).equals("E") && lastAccess > 0) lastAccess--;
            System.out.print("D" + i + " --> [");
            for(int j=0; j<lists.get(i+m).size(); j++) {
                if(j!=(lastAccess)){
                    if(!(lists.get(i+m).get(j).equals("E"))){
                        System.out.print("D" + j + ": " + lists.get(i+m).get(j) + " -> ");
                    }
                }
                else{
                    if(!(lists.get(i+m).get(j).equals("E"))){
                        System.out.print("D" + j + ": " + lists.get(i+m).get(j));
                    }
                }
            }
            System.out.println("]");
        }
        return lists;
    }
}