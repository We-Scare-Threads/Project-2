//Task 2: Implementation with Access Lists for Objects
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class AccessList {
    static Lock[] resourceLocks;
    static String[] data;
    public static final String[] Colors = {"blue", "red", "green", "yellow", "purple", "orange", "pink", "brown", "black", "white"};
    public static void main(String[] args) {
        System.out.println("\nAccess List Key: ");
        System.out.println("R = Read, W = Write, B = Both (Read/Write), E = Empty/No Access, N = No Access (for Domains ONLY)");
        Random random = new Random();

        int numDomains = random.nextInt(5) + 3;
        int numObjects = random.nextInt(5) + 3;

        System.out.println("D: " + numDomains);
        System.out.println("O: " + numObjects + "\n");

        LinkedList<LinkedList<String>> accList = AList(numDomains, numObjects);

        resourceLocks = new Lock[numObjects + numDomains];
        data = new String[numObjects + numDomains];
        for (int r = 0; r < (numObjects + numDomains); r++) {
            resourceLocks[r] = new ReentrantLock();
            data[r] = Colors[random.nextInt(Colors.length)];
        }
        System.out.println("Resources Array holds: " + numObjects + " files(objects) and " + numDomains + " domains. " + resourceLocks.length + " in total.\n");
        System.out.println("Num of Users (domains) shall be: " + numDomains + ".\n");
        System.out.println("User Access Key: ");
        System.out.println("(O) = Accessed Object, (R) = Read, (W) = Write, (E) = No Assigned Permissions, **An attached X = Access Denied**\n");

        for (int i = 0; i < numDomains; i++){
            Users_AList user = new Users_AList(i, accList, resourceLocks, numDomains, numObjects, data, Colors);
            user.start();
        }
    }
    public static LinkedList<LinkedList<String>> AList(int numDom, int numObj) {
        Random random = new Random();
        int r;
        // e = empty, b = both r/w, n = n/a, a = allow, will format later
        String[] objperms = {"E", "R", "W", "B"};
        String[] domperms = {"E", "A"};

        LinkedList<LinkedList<String>> lists = new LinkedList<>();

        for(int i=0; i<numDom+numObj; i++) {
            LinkedList<String> aList = new LinkedList<>();
            lists.add(aList);
        }

        System.out.println("Access List:");
        for(int j=0; j<numDom+numObj; j++) {
            for (int i = 0; i < numDom; i++) {
                if(j<(numObj)){ // Object permissions
                    r = random.nextInt(4);
                    lists.get(j).add(objperms[r]);
                }
                else{
                    if (j == (i + numObj)) { // Domain to itself always allow
                        lists.get(j).add("N");
                    } else {
                        r = random.nextInt(2);
                        lists.get(j).add(domperms[r]);
                    }
                }
            }

        }
        for (int i = 0; i < numObj; i++) {
            int lastAccess = numDom-1;
            System.out.print("O" + i + " --> [");
            while(lists.get(i).get(lastAccess).equals("E") && lastAccess > 0) lastAccess--;
            for(int j=0; j<lists.get(i).size(); j++) {
                if(j!=(lastAccess) && !(lists.get(i).get(j).equals("E"))){
                    System.out.print("D" + j + ": " + lists.get(i).get(j) + " -> ");
                }
                else{
                    if(!(lists.get(i).get(j).equals("E"))){
                        System.out.print("D" + j + ": " + lists.get(i).get(j));
                    }
                }
            }
            System.out.println("]");
        }
        for (int i = 0; i < numDom; i++) {
            int lastAccess = numDom-1;
            while((lists.get(i+numObj).get((lastAccess)).equals("E") || lists.get(i+numObj).get((lastAccess)).equals("N")) && lastAccess > 0) lastAccess--;
            System.out.print("D" + i + " --> [");
            for(int j=0; j<lists.get(i+numObj).size(); j++) {
                if(j!=(lastAccess) && !(lists.get(i+numObj).get(j).equals("E")|| lists.get(i+numObj).get(j).equals("N"))){
                    System.out.print("D" + j + ": " + lists.get(i+numObj).get(j) + " -> ");
                }
                else{
                    if(!(lists.get(i+numObj).get(j).equals("E") || lists.get(i+numObj).get(j).equals("N"))){
                        System.out.print("D" + j + ": " + lists.get(i+numObj).get(j));
                    }
                }
            }
            System.out.println("]");
        }
        return lists;
    }
}