package collector;

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

    private int[] children;
    private int id;
    private int parent;

    public int collect(int parent) {
        return 1;
    }

    public int registerChild(int child) {
        System.out.println(this.id + ": registerChild("+child+")");
        this.children[0] = child;
        return 0;
    }

    //args
    //0 -> id
    //1 -> id do pai
    public static void main(String args[]) {
        int id = Integer.parseInt(args[0]);
        String registryTag = "Node" + id;
        System.err.println(registryTag);

        // boolean root = true;
        // if (args[1] != null) {
            
        // } else 
        int parent = Integer.parseInt(args[1]);
        String parentTag = "Node" + parent;

        try {
        	System.setSecurityManager(new SecurityManager());
            Registry registry = LocateRegistry.getRegistry();

            Node node = new Node(id, parent);
            Collector stub = (Collector)UnicastRemoteObject.exportObject(node, 0);
            registry.bind(registryTag, stub);

            Collector parentStub = (Collector)registry.lookup(parentTag);
            int result = parentStub.registerChild(id);
            System.out.println("registerChild result: " + result);

            System.err.println("Node waits");
        } catch (NotBoundException e) {
            System.err.println(id+"Cliente exceção: " + e.toString());
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            System.err.println(id+"Already Bound Exception: " + e.toString());
            e.printStackTrace();
        }  catch (RemoteException e) {
            System.err.println(id+"Remote Exception: " + e.toString());
            e.printStackTrace();
        }
    }
}

// javac collector/*.java
// jar cvf classes.jar collector/*.class

///Como Executar

// java -cp . -Djava.rmi.server.codebase=file:///Users/pietrodegrazia/Documents/UFRGS/PDP/ex2-coleta-rmi/classes.jar -Djava.security.policy=java.policy collector.Node
// java -cp . -Djava.rmi.server.codebase=file:///Users/pietrodegrazia/Documents/UFRGS/PDP/ex2-coleta-rmi/classes.jar -Djava.security.policy=java.policy appcalculadora.Cliente