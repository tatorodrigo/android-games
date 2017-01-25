package br.com.tattobr.samples.framework.gl;

import java.util.ArrayList;
import java.util.List;

import br.com.tattobr.samples.framework.game2d.GameObject;

public class SpatialHashGrid {
    private List<List<GameObject>> dynamicCells;
    private List<List<GameObject>> staticCells;
    private int cellsPerRow;
    private int cellsPerCol;
    private float cellSize;
    private int cellIds[];
    private List<GameObject> foundObjects;

    public SpatialHashGrid(float worldWidth, float worldHeight, float cellSize, int initialNumberOfObjectsInScene) {
        this.cellSize = cellSize;
        cellsPerRow = (int) Math.ceil(worldWidth / cellSize);
        cellsPerCol = (int) Math.ceil(worldHeight / cellSize);
        int numCells = cellsPerRow * cellsPerCol;

        dynamicCells = new ArrayList<>(numCells);
        staticCells = new ArrayList<>(numCells);
        for (int i = 0; i < numCells; i++) {
            dynamicCells.add(new ArrayList<GameObject>(initialNumberOfObjectsInScene));
            staticCells.add(new ArrayList<GameObject>(initialNumberOfObjectsInScene));
        }
        foundObjects = new ArrayList<>(initialNumberOfObjectsInScene);
        cellIds = new int[4];
    }

    private int[] getCellIds(GameObject obj) {
        int x1 = (int) Math.floor(obj.bounds.lowerLeft.x / cellSize);
        int y1 = (int) Math.floor(obj.bounds.lowerLeft.y / cellSize);
        int x2 = (int) Math.floor((obj.bounds.lowerLeft.x + obj.bounds.width) / cellSize);
        int y2 = (int) Math.floor((obj.bounds.lowerLeft.y + obj.bounds.height) / cellSize);

        if (x1 == x2 && y1 == y2) {
            if (x1 >= 0 && x1 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol) {
                cellIds[0] = x1 + y1 * cellsPerRow;
            } else {
                cellIds[0] = -1;
            }
            cellIds[1] = -1;
            cellIds[2] = -1;
            cellIds[3] = -1;
        } else if (x1 == x2) {
            int i = 0;
            if (x1 >= 0 && x1 < cellsPerRow) {
                if (y1 >= 0 && y1 < cellsPerCol) {
                    cellIds[i++] = x1 + y1 * cellsPerRow;
                }
                if (y2 >= 0 && y2 < cellsPerCol) {
                    cellIds[i++] = x1 + y2 * cellsPerRow;
                }
            }
            while (i <= 3) {
                cellIds[i++] = -1;
            }
        } else if (y1 == y2) {
            int i = 0;
            if (y1 >= 0 && y1 < cellsPerCol) {
                if (x1 >= 0 && x1 < cellsPerRow) {
                    cellIds[i++] = x1 + y1 * cellsPerRow;
                }
                if (x2 >= 0 && x2 < cellsPerRow) {
                    cellIds[i++] = x2 + y1 * cellsPerRow;
                }
            }
            while (i <= 3) {
                cellIds[i++] = -1;
            }
        } else {
            int i = 0;
            int y1CellsPerRow = y1 * cellsPerRow;
            int y2CellsPerRow = y2 * cellsPerRow;
            if (x1 >= 0 && x1 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol) {
                cellIds[i++] = x1 + y1CellsPerRow;
            }
            if (x2 >= 0 && x2 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol) {
                cellIds[i++] = x2 + y1CellsPerRow;
            }
            if (x1 >= 0 && x1 < cellsPerRow && y2 >= 0 && y2 < cellsPerCol) {
                cellIds[i++] = x1 + y2CellsPerRow;
            }
            if (x2 >= 0 && x2 < cellsPerRow && y2 >= 0 && y2 < cellsPerCol) {
                cellIds[i++] = x2 + y2CellsPerRow;
            }
            while (i <= 3) {
                cellIds[i++] = -1;
            }
        }
        return cellIds;
    }

    public void insertStaticObject(GameObject obj) {
        int[] cellIds = getCellIds(obj);
        int i = 0;
        int cellId;
        while (i <= 3 && (cellId = cellIds[i++]) != -1) {
            staticCells.get(cellId).add(obj);
        }
    }

    public void insertDynamicObject(GameObject obj) {
        int[] cellIds = getCellIds(obj);
        int i = 0;
        int cellId;
        while (i <= 3 && (cellId = cellIds[i++]) != -1) {
            dynamicCells.get(cellId).add(obj);
        }
    }

    public void removeObject(GameObject obj) {
        int[] cellIds = getCellIds(obj);
        int i = 0;
        int cellId;
        while (i <= 3 && (cellId = cellIds[i++]) != -1) {
            staticCells.get(cellId).remove(obj);
            dynamicCells.get(cellId).remove(obj);
        }
    }

    public void clearDynamicCells() {
        int size = dynamicCells.size();
        for (int i = 0; i < size; i++) {
            dynamicCells.get(i).clear();
        }
    }

    public void clearStaticCells() {
        int size = staticCells.size();
        for (int i = 0; i < size; i++) {
            staticCells.get(i).clear();
        }
    }

    public List<GameObject> getPotentialColliders(GameObject obj) {
        foundObjects.clear();
        int[] cellIds = getCellIds(obj);
        int i = 0;
        List<GameObject> colliderList;
        GameObject collider;
        int cellId;
        int size;

        while (i <= 3 && (cellId = cellIds[i++]) != -1) {
            colliderList = staticCells.get(cellId);
            size = colliderList.size();
            for (int j = 0; j < size; j++) {
                collider = colliderList.get(j);
                if (!foundObjects.contains(collider)) {
                    foundObjects.add(collider);
                }
            }

            colliderList = dynamicCells.get(cellId);
            size = colliderList.size();
            for (int j = 0; j < size; j++) {
                collider = colliderList.get(j);
                if (!foundObjects.contains(collider)) {
                    foundObjects.add(collider);
                }
            }
        }
        return foundObjects;
    }
}
