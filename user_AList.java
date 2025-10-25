import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Users_AList extends Thread {
    private int userId;
    Lock[] resources;
    private final LinkedList<LinkedList<String>> accList;
    static String[] objActions = {"R", "W"};
    static String domAction = "A";
    private int numDomains;
    private int numObjects;
    private String[] data;
    private static String[] Colors;

    public Users_AList(int userId, LinkedList<LinkedList<String>> accList, Lock[] resources, int numDomains, int numObjects, String[] data, String[] Colors) {
        this.userId = userId;
        this.accList = accList;
        this.resources = resources;
        this.numDomains = numDomains;
        this.numObjects = numObjects;
        this.data = data;
        this.Colors = Colors;
    }

    public void yieldR() {
        Random random = new Random();
        int y = random.nextInt(5) + 3;
        System.out.println("User D" + userId + " yields " + y + " times");
        for (int i = 0; i < y; i++) {
            Thread.yield();
        }
    }

    @Override
    public void run() {
        try {
            Random random = new Random();
            for (int i = 0; i <= 5; i++) {
                int objId = random.nextInt(resources.length);
                int actionIndex = random.nextInt(objActions.length);
                String permission = accList.get(objId).get(userId);
                String action;
                // yields after request but not sure where it would be
                //yieldR();
                if (objId >= numObjects) { // Domain switch
                    if (permission.equals("A")) {
                        resources[objId].lock();
                        System.out.println("User D" + userId + " SWITCHING to Domain D" + (objId - numObjects) + " (A)");
                        yieldR();
                        switchUser(objId);
                        resources[objId].unlock();
                    } else if (permission.equals("E")) {
                        resources[objId].lock();
                        System.out.println("User D" + userId + " lacks permissions for SWITCH on Domain D" + (objId - numObjects)+ " (E) - X");
                        yieldR();
                        resources[objId].unlock();
                    } else if (permission.equals("N")){
                        resources[objId].lock();
                        System.out.println("User D" + userId + " CANNOT SWITCH to Domain D itself (N) - X");
                        yieldR();
                        resources[objId].unlock();
                    } else {
                        resources[objId].lock();
                        System.out.println("User D" + userId + " tried ILLEGAL ACTION (Switch to obj O" + objId + ") (N/A) - X");
                        yieldR();
                        resources[objId].unlock();
                    }
                } else {
                    action = objActions[actionIndex];
                    switch (action) {
                        case "R":
                            if (permission.equals("E")) {
                                resources[objId].lock();
                                System.out.println("User D" + userId + " lacks permissions for READ on (O" + objId + ") (E) - X");
                                yieldR();
                                resources[objId].unlock();
                            } else if (permission.equals("R") || permission.equals("B")) {
                                resources[objId].lock();
                                System.out.println("User D" + userId + " accessed (O" + objId + ") Reading '" + data[objId] + "' with (R)");
                                yieldR();
                                resources[objId].unlock();
                            } else {
                                resources[objId].lock();
                                System.out.println("User D" + userId + " CANNOT READ to (O" + objId + ") with (R) - X");
                                yieldR();
                                resources[objId].unlock();
                            }
                            break;
                        case "W":
                            if (permission.equals("E")) {
                                resources[objId].lock();
                                System.out.println("User D" + userId + " lacks permission for WRITE on (O" + objId + ") (E) - X");
                                yieldR();
                                resources[objId].unlock();
                            } else if (permission.equals("W") || permission.equals("B")) {
                                resources[objId].lock();
                                int r = random.nextInt(Colors.length);
                                data[objId] = Colors[r];
                                System.out.println("User D" + userId + " accessed (O" + objId + ") Writing '" + data[objId] + "' with (W)");
                                yieldR();
                                resources[objId].unlock();
                            } else {
                                resources[objId].lock();
                                System.out.println("User D" + userId + " CANNOT WRITE to (O" + objId + ") with (W) - X");
                                yieldR();
                                resources[objId].unlock();
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