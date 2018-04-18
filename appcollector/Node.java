package appcollector;

import java.util.ArrayList;
import java.util.List;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;


public class Node implements Collector {

    public Node(int id, int parent) {
        this.id = id;
        this.parent = parent;
    }

    List<Integer> children = new ArrayList<Integer>();
    private int id;
    private int parent;

    public int collect(int parent) {
        return 1;
    }

    public int registerChild(int child) {
        System.out.println(this.id + ": registerChild("+child+")");
        this.children.add((Integer)child);
        return 0;
    }

    //args
    //0 -> id
    //1 -> id do pai
    public static void main(String args[]) {
        int id = Integer.parseInt(args[0]);
        String registryTag = "Node" + id;
        System.err.println("Node: " + registryTag);

        int parent = Integer.parseInt(args[1]);
        String parentTag = "Node" + parent;    
        System.err.println("Node: " + parentTag);

        boolean root = (id == parent);

        try {
            System.err.println("Setting Security");
        	System.setSecurityManager(new SecurityManager());

            System.err.println("Locating Registry");
            Registry registry = LocateRegistry.getRegistry();

            System.err.println("Creating Self(Node)");
            Node node = new Node(id, parent);

            System.err.println("Exporting Self(Node)");
            Collector stub = (Collector)UnicastRemoteObject.exportObject(node, 0);

            System.err.println("Binding Self(Node)");
            registry.bind(registryTag, stub);


            if(!root) {
                System.err.println("Looking up Parent");
                Collector parentStub = (Collector)registry.lookup(parentTag);

                System.err.println("Registering as child");
                int result = parentStub.registerChild(id);
                System.out.println("registerChild result: " + result);
            } else {
                System.err.println("Root Node");
            }


            System.err.println("Node waits");
        } catch (NotBoundException e) {
            System.err.println("Error on Node " + id);
            System.err.println("Cliente exceção: " + e.toString());
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            System.err.println("Error on Node " + id);
            System.err.println("Already Bound Exception: " + e.toString());
            e.printStackTrace();
        }  catch (RemoteException e) {
            System.err.println("Error on Node " + id);
            System.err.println("Remote Exception: " + e.toString());
            e.printStackTrace();
        }
    }
}

// javac collector/*.java
// jar cvf classes.jar collector/*.class

///Como Executar

// java -cp . -Djava.rmi.server.codebase=file:///Users/pietrodegrazia/Documents/UFRGS/PDP/ex2-coleta-rmi/classes.jar -Djava.security.policy=java.policy collector.Node
// java -cp . -Djava.rmi.server.codebase=file:///Users/pietrodegrazia/Documents/UFRGS/PDP/ex2-coleta-rmi/classes.jar -Djava.security.policy=java.policy appcalculadora.Cliente