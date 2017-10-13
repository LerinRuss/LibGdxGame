package com.mygdx.unproject;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.utils.PooledLinkedList;
import com.mygdx.game.map.GameTiledMap;
import com.mygdx.objects.units.Unit;
import com.mygdx.tiles.TileWithUnit;
import com.mygdx.utilities.TransformCoordinates;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by Viteker on 17.08.2017.
 */
public class UnitHandler {
    private GameTiledMap map;
    private Unit unit;
    private Node[][] nodes;
    private PooledLinkedList<Node> list;

    public UnitHandler(GameTiledMap map){
        this.map = map;
    }

    public PooledLinkedList<Unit> findTargets(Unit unit) {
        PooledLinkedList<Unit> list;
        list = new PooledLinkedList<Unit>(Integer.MAX_VALUE);

        int x = (int) unit.getX(),
                y = (int) unit.getY();

        int range = unit.getAttackRange();
        for (int yy = y - range; yy <= y + range; yy++) {
            for (int xx = x - range; xx <= x + range; xx++) {
                GameTiledMap.MapCoordinates coordinates = TransformCoordinates.transform(map, xx, yy);
                TiledMapTile tiledMapTile = map.getTile(coordinates);
                if (!(tiledMapTile instanceof TileWithUnit))
                    continue;
                TileWithUnit tile = (TileWithUnit) tiledMapTile;
                Unit target = tile.getUnit();
                if (target == null || target.getOwner() == target.getOwner())
                    continue;

                list.add(target);
            }
        }
        return list;
    }
    /**
     Dijkstra’s algorithm on Neumann neighborhood
     find the shortest ways from start vertex to others
     algorithm is finished, when either all vertexes are opened within radius of start vertex (no all vertexes of game field have been opened),
     or power of start vertex has ended for other vertexes.
     */
    public void findTheShortestWaysFromStartToOthers(Unit start){
        this.unit = start;
        int offer = start.getMovingPoint()/start.getMinTileExpense();
        int size = 2 * offer + 1;
        int power = start.getMovingPoint();

        Queue<Node> priorityQueue = new PriorityQueue<Node>(size*size);
        list = new PooledLinkedList<Node>(size*size);

        boolean[][] opened = new boolean[size][size];
        nodes = new Node[size][size];

        Node node = new Node(offer, offer);
        node.weight = 0;
        priorityQueue.add(node);

        while(!priorityQueue.isEmpty()){

            node = priorityQueue.poll();
            list.add(node);
            opened[node.x][node.y] = true;

            for(int y = node.y - 1; y <= node.y+1; y ++) {
                for (int x = node.x - 1; x <= node.x + 1; x++) {

                    int tileX = (int) (start.getX() + x - offer);
                    int tileY = (int) (start.getY() + y - offer);

                    TiledMapTile tile = map.getSafeIndexingTile(tileX, tileY);
                    if (!(tile instanceof TileWithUnit))
                        continue;
                    TileWithUnit tileWithUnit = (TileWithUnit) tile;
                    if (!tileWithUnit.isFree())
                        continue;

                    int expenses = start.getTileExpense(tileWithUnit.getType());
                    if (expenses == 0) {
                        continue;
                    }
                    int sum = node.weight + expenses;
                    if (sum <= power && !opened[x][y]) {
                        if (nodes[x][y] != null) {
                            if (nodes[x][y].weight > sum) {
                                nodes[x][y].weight = sum;
                                nodes[x][y].parent = node;
                            }
                        } else {
                            Node temp = new Node(x, y);
                            temp.weight = sum;
                            temp.parent = node;
                            nodes[x][y] = temp;
                            priorityQueue.add(temp);
                        }
                    }

                }
            }
        }

    }
    public Node[][] getArrayTreeRoutes(Unit unit){
        if(this.unit != unit)
            findTheShortestWaysFromStartToOthers(unit);
        return nodes;
    }
    public PooledLinkedList<Node> getListTreeRoutes(Unit unit){
        if(this.unit != unit)
            findTheShortestWaysFromStartToOthers(unit);
        return list;
    }
    public class Node implements Comparable<Node>{
        public int weight = Integer.MAX_VALUE;
        public Node parent;
        public int x,y;

        Node(int x,int y){
            this.x = x;
            this.y = y;
        }
        @Override
        public int compareTo(Node o) {
            return this.weight - o.weight;
        }
    }
}
