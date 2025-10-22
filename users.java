import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Users extends Thread {
    private int userId;
    Lock[] resources;
    private final String[][] accMat;
    static String[] objActions = {"R", "W"};
    static String domAction = "A";
    private int numDomains;
    private int numObjects;

    public Users(int userId, String[][] accMat, Lock[] resources, int numDomains, int numObjects) {
        this.userId = userId;
        this.accMat = accMat;
        this.resources = resources;
        this.numDomains = numDomains;
        this.numObjects = numObjects;
    }

    @Override
    public void run() {
        try {
            Random random = new Random();
            for (int i = 0; i <= 5; i++) {
                int objId = random.nextInt(resources.length);
                int actionIndex = random.nextInt(objActions.length);
                String permission = accMat[userId][objId];
                String action;
                if (objId >= numObjects) { // Domain switch
                    if (permission.equals("A")) {
                        System.out.println("User D" + userId + " SWITCHING to Domain D" + (objId - numObjects) + " (A)");
                        switchUser(objId);
                    } else if (permission.equals("E")) {
                        System.out.println("User D" + userId + " tried switch with EMPTY permissions on Domain D" + (objId - numObjects)+ " (E) - X");
                    } else if (permission.equals("N")){
                        System.out.println("User D" + userId + " CANNOT SWITCH to Domain D" + (objId - numObjects) + " (N) - X");
                    } else {
                        System.out.println("User D" + userId + " tried ILLEGAL ACTION (Switch to obj O" + objId + ") (N/A) - X");
                    }
                } else {
                    action = objActions[actionIndex];
                    switch (action) {
                    case "R":
                        if (permission.equals("E")) {
                            System.out.println("User D" + userId + " tried READ with EMPTY permissions on (O" + objId + ") (E) - X");
                        } else if (permission.equals("R") || permission.equals("B")) {
                            resources[objId].lock();
                            System.out.println("User D" + userId + " accessed (O" + objId + ") with (R)");
                            resources[objId].unlock();
                        } else {
                            System.out.println("User D" + userId + " CANNOT READ to (O" + objId + ") with (R) - X");
                        }
                        break;
                    case "W":
                        if (permission.equals("E")) {
                            System.out.println("User D" + userId + " tried WRITE with EMPTY permissions on (O" + objId + ") (E) - X");
                        } else if (permission.equals("W") || permission.equals("B")) {
                            resources[objId].lock();
                            System.out.println("User D" + userId + " accessed (O" + objId + ") with (W)");
                            resources[objId].unlock();
                        } else {
                            System.out.println("User D" + userId + " CANNOT WRITE to (O" + objId + ") with (W) - X");
                        }
                        break;
                    }
                }
                
            }
        } catch (Error e) {
            Thread.currentThread().interrupt();
        }
    }
    public void switchUser(int a){
        userId = (a - numObjects);
    }
}