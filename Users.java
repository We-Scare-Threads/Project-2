import java.util.Random;
import java.util.concurrent.locks.Lock;

class Users extends Thread {
    private int userId;
    private int userDomainId;
    Lock[] resources;
    private final String[][] accMat;
    static String[] objActions = {"R", "W"};
    static String domAction = "A";
    private int numDomains;
    private int numObjects;
    private String[] data;
    private static String[] Colors;

    public Users(int userId, String[][] accMat, Lock[] resources, int numDomains, int numObjects, String[] data, String[] Colors) {
        this.userId = userId;
        this.userDomainId = userId;
        this.accMat = accMat;
        this.resources = resources;
        this.numDomains = numDomains;
        this.numObjects = numObjects;
        this.data = data;
        this.Colors = Colors;
    }

    public void yieldR() {
        Random random = new Random();
        int y = random.nextInt(5) + 3;
        System.out.println("User " + userId + "(D" + userDomainId + ")" + " yields " + y + " times");
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
                String permission = accMat[userId][objId];
                String action;

                if (objId >= numObjects) { // Domain switch
                    System.out.println("User " + userId + "(D" + userDomainId + ")" + " ATTEMPTS to switch to Domain D" + (objId - numObjects));
                    yieldR();
                    if (permission.equals("A")) {
                        resources[objId].lock();
                        System.out.println("User " + userId + "(D" + userDomainId + ")" + " SWITCHING to Domain D" + (objId - numObjects) + " (A)");
                        yieldR();
                        switchUser(objId);
                        resources[objId].unlock();
                    } else if (permission.equals("E")) {
                        resources[objId].lock();
                        System.out.println("User " + userId + "(D" + userDomainId + ")" + " tried switch with EMPTY permissions on Domain D" + (objId - numObjects)+ " (E) - X");
                        yieldR();
                        resources[objId].unlock();
                    } else if (permission.equals("N")){
                        resources[objId].lock();
                        System.out.println("User " + userId + "(D" + userDomainId + ")" + " CANNOT SWITCH to Domain D" + (objId - numObjects) + " (N) - X");
                        yieldR();
                        resources[objId].unlock();
                    } else {
                        resources[objId].lock();
                        System.out.println("User " + userId + "(D" + userDomainId + ")" + " tried ILLEGAL ACTION (Switch to obj O" + objId + ") (N/A) - X");
                        yieldR();
                        resources[objId].unlock();
                    }
                } else {
                    action = objActions[actionIndex];
                    if (action.equals("R")) {
                        System.out.println("User " + userId + "(D" + userDomainId + ")" + " ATTEMPTS to READ on (F" + objId + ")");
                        yieldR();
                    }
                    else if (action.equals("W")) {
                        System.out.println("User " + userId + "(D" + userDomainId + ")" + " ATTEMPTS to WRITE on (F" + objId + ")");
                        yieldR();
                    }

                    switch (action) {
                        case "R":
                            if (permission.equals("E")) {
                                resources[objId].lock();
                                System.out.println("User " + userId + "(D" + userDomainId + ")" + " tried READ with EMPTY permissions on (F" + objId + ") (E) - X");
                                yieldR();
                                resources[objId].unlock();
                            } else if (permission.equals("R") || permission.equals("B")) {
                                resources[objId].lock();
                                System.out.println("User " + userId + "(D" + userDomainId + ")" + " accessed (F" + objId + ") Reading " + data[objId] + " with (R)");
                                yieldR();
                                resources[objId].unlock();
                            } else {
                                resources[objId].lock();
                                System.out.println("User " + userId + "(D" + userDomainId + ")" + " CANNOT READ to (F" + objId + ") with (R) - X");
                                yieldR();
                                resources[objId].unlock();
                            }
                            break;
                        case "W":
                            if (permission.equals("E")) {
                                resources[objId].lock();
                                System.out.println("User " + userId + "(D" + userDomainId + ")" + " tried WRITE with EMPTY permissions on (F" + objId + ") (E) - X");
                                yieldR();
                                resources[objId].unlock();
                            } else if (permission.equals("W") || permission.equals("B")) {
                                resources[objId].lock();
                                int r = random.nextInt(Colors.length);
                                data[objId] = Colors[r];
                                System.out.println("User " + userId + "(D" + userDomainId + ")" + " accessed (F" + objId + ") Writing " + data[objId] + " with (W)");
                                yieldR();
                                resources[objId].unlock();
                            } else {
                                resources[objId].lock();
                                System.out.println("User " + userId + "(D" + userDomainId + ")" + " CANNOT WRITE to (F" + objId + ") with (W) - X");
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
        System.out.println("User " + userId + "(D" + userDomainId + ")" + " is Done");
    }
    public void switchUser(int a){
        userDomainId = (a - numObjects);
    }
}