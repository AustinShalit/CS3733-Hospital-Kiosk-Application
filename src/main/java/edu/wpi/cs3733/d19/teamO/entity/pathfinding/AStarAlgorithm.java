package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import com.google.common.graph.Graph;

import java.util.*;

public class AStarAlgorithm<T> extends UninformedGraphSearchAlgorithm<T> {

    @Override
    public String getName() {
        return "A Star";
    }


    @Override
    public List<T> getPath(final Graph<T> graph, final T start, final T goal) {
        final Queue<T> frontier = new LinkedList<>();
        final Map<T, T> cameFrom = new HashMap<>();
        frontier.add(start);
        cameFrom.put(start, null);

        while (!frontier.isEmpty()) {
            T current = frontier.poll();

            if (current.equals(goal)) {
                break;
            }

            for (T next : graph.successors(current)) {
                if (!cameFrom.containsKey(next)) {
                    frontier.add(next);
                    cameFrom.put(next, current);
                }
            }
        }
        return buildPath(cameFrom, goal);
    }


}


