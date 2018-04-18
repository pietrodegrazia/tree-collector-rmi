// package appcalculadora;

// import java.rmi.registry.LocateRegistry;
// import java.rmi.registry.Registry;
// import java.rmi.RemoteException;
// import java.rmi.NotBoundException;

// public class Cliente {

//     private Cliente() {}

//     public static void main(String[] args) {

//         String host = (args.length < 1) ? null : args[0];
//         try {
//         	System.out.println("Inicializando Cliente...");
//         	System.setSecurityManager(new SecurityManager());
//             Registry registry = LocateRegistry.getRegistry(host);
//             Calculadora stub = (Calculadora)registry.lookup("Calculadora");

//             System.out.println("Realizando Soma");
//             int soma = stub.somar(1,1);
//             System.out.println("1 + 1: = " + soma);

//             System.out.println("Realizando Multiplicação");
//             int mult = stub.multiplicar(2,3);
//             System.out.println("2 * 3: = " + mult);

//         }catch (NotBoundException e) {
//             System.err.println("Cliente exceção: " + e.toString());
//             e.printStackTrace();
//         } catch (RemoteException e) {
//             System.err.println("Cliente exceção: " + e.toString());
//             e.printStackTrace();
//         }
//     }
// }

