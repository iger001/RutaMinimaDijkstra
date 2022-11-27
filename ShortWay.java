import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Implementación de algoritmo de Dijkstra, usando nodos como nombres en string y pesos en cada arista
 * @author igero
 *
 */
public class ShortWay {
    String[]  nodeNames;  // Nombres de los nodos
    int[][] grafo;  // Matriz de distancias entre nodos
    String  shortWay; // ruta mas corta
    int     shortDistance = Integer.MAX_VALUE; // Distancia más corta
    List<Nodo>  revisedNodes = null; // nodos revisados por Dijkstra

    // Inicializa el grafo con los nombres de los nodos en una matriz de nxn
    ShortWay(String nodeNames[]) {
        this.nodeNames = nodeNames;
        grafo = new int[nodeNames.length][nodeNames.length];
    }

    // Agrega una arista (addEdge) al grafo con el peso de esta (distancia), en ambos sentidos A->B o B->A
    public void addEdge(String origen, String destino, int weight) {
        int n1 = getPosNode(origen);
        int n2 = getPosNode(destino);
        grafo[n1][n2]=weight;
        grafo[n2][n1]=weight;
    }

    // recupera la posición en i del listado de nodos, con base a su nombre
    private int getPosNode(String nodo) {
    	//System.out.println("Buscando a : "+ nodo);
        for(int i=0; i<nodeNames.length; i++) {
        	System.out.println(" = " + nodeNames[i] + " vs " + nodo);
            if(nodeNames[i]!=null&&nodeNames[i].equals(nodo)) return i;
        }
        return -1;
    }
    
    // encuentra el camino más corto desde el nombre del nodo incial al nombre del nodo final
    public String findShortestPathDijkstra(String start, String end) throws Exception {
    	findMinimumRoute(start);
        Nodo tmp = new Nodo(end);
        if(!revisedNodes.contains(tmp)) { // busca el nodo destino en la lista de nodos si no lo encuetra lanza una excepción
        	throw new Exception("No se encontro el nodo buscado");
        }
        tmp = revisedNodes.get(revisedNodes.indexOf(tmp));
        int distance = tmp.distance;  
        // crea una pila para almacenar la ruta desde el nodo final al origen
        Stack<Nodo> pila = new Stack<Nodo>();
        while(tmp != null) {
            pila.add(tmp);
            tmp = tmp.origin;
        }
        String ruta = "";
        // recorre la pila para armar la ruta en el orden correcto
        while(!pila.isEmpty()) ruta+=(pila.pop().id + " ");
        return distance + ": " + ruta;
    }

    // encuentra el camino más corto desde el nodo inicial hacia todos los demas
    public void findMinimumRoute(String inicio) {
        Queue<Nodo>   queue = new PriorityQueue<Nodo>(); // cola de prioridad
        Nodo initNode = new Nodo(inicio); // nodo inicial
        
        revisedNodes = new LinkedList<Nodo>();// lista de nodos ya revisados
        queue.add(initNode); // Agregar nodo inicial a la cola de prioridad
        while(!queue.isEmpty()) { // recorre toda la cola mientras tenga elementos
            Nodo tmp = queue.poll(); // pop del primer elemento de la cola
            revisedNodes.add(tmp); // lo inserta en la lista de nodos revisados
            int p = getPosNode(tmp.id);   
            for(int j=0; j<grafo[p].length; j++) { // se revisan los nodos hijos
                if(grafo[p][j]==0) continue;        // si no tiene conexiones no lo toma en cuenta
                if(isNodeRevised(j)) continue;      // si ya esta en la lista de nodos revisados no lo toma en cuenta
                Nodo nod = new Nodo(nodeNames[j],tmp.distance+grafo[p][j],tmp);
                
                if(!queue.contains(nod)) {
                	queue.add(nod); //se agrega a la cola
                    continue;
                }
                
                for(Nodo x: queue) {
                    if(x.id==nod.id && x.distance > nod.distance) { //si la distancia es menor a la que esta en la cola lo reemplaza en la cola (queue)
                    	queue.remove(x);
                        queue.add(nod);
                        break;
                    }
                }
            }
        }
    }

    public boolean isNodeRevised(int pos) {
        Nodo tmp = new Nodo(nodeNames[pos]);
        return revisedNodes.contains(tmp);
    }

    public static void main(String[] args) throws Exception {
        
    	ShortWay g = new ShortWay("a,b,c,d,e,f".split(","));
        
        g.addEdge("a","b", 1);
        g.addEdge("a","c", 10);
        g.addEdge("b","c", 18);
        g.addEdge("b","d", 10);
        g.addEdge("b","f", 30);
        g.addEdge("c","e", 5);
        g.addEdge("d","f", 10);
        g.addEdge("e","f", 5);
        
        String inicio = "b";
        String fin    = "f";
        String respuesta = g.findShortestPathDijkstra(inicio, fin);
        System.out.println(respuesta);
        
        /*respuesta = g.findShortestPathDijkstra(fin, inicio);
        System.out.println(respuesta);*/
    }

}

class Nodo implements Comparable<Nodo> {
	String id;
    int  distance   = Integer.MAX_VALUE;
    Nodo origin = null;
    Nodo(String x, int d, Nodo p) { id=x; distance=d; origin=p; }
    Nodo(String x) { this(x, 0, null); }
    public int compareTo(Nodo tmp) { return this.distance-tmp.distance; }
    public boolean equals(Object o) {
        Nodo tmp = (Nodo) o;
        if(tmp.id.equals(this.id)) return true;
        return false;
    }
}