package appcollector;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
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

    List<Collector> children = new ArrayList<Collector>();
    private int id;
    private int parent;

    public List<Integer> collect() {
        System.err.println("Node Collecting...");
        List<Integer> data = new ArrayList<Integer>();

        try {
            for (Collector child : this.children) {
                
                //adiciona ids dos filhos do filho
                List<Integer> childData = child.collect();
                data.addAll(childData);
            }
            data.add((Integer)this.id);

            System.err.println("Done Collecting.");
            return data;

        }  catch (RemoteException e) {
            System.err.println("Remote Exception: " + e.toString());
            e.printStackTrace();
            return data;
        }
    }

    public int registerChild(Collector child) {
        try {
            System.out.println(this.id + ": registerChild("+child.id()+")");
            this.children.add(child);
            return 0;
        }  catch (RemoteException e) {
            System.err.println("Remote Exception: " + e.toString());
            e.printStackTrace();
            return -1;
        }
    }

    public int id() {
        return this.id;
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
                // int result = parentStub.registerChild(node);
                int result = parentStub.registerChild(stub);
                System.out.println("registerChild result: " + result);

                System.err.println("Node waits collector");
            
            } else {
                System.err.println("Root Node");
                
                Scanner in = new Scanner(System.in);
                System.out.println("Press enter to begin collection");
                in.nextLine();

                System.out.println("Begin collector.");
                List<Integer> data = node.collect();
                System.out.println(Arrays.toString(data.toArray()));
                System.out.println("End collector.");
            }

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

// javac appcollector/*.java
// jar cvf classes.jar appcollector/*.class

///Como Executar
// java -cp . -Djava.rmi.server.codebase=file:///Users/pietrodegrazia/Documents/UFRGS/PDP/ex2-coleta-rmi/classes.jar -Djava.security.policy=java.policy appcollector.Node idDoNodo idDoPai