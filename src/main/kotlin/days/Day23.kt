package org.example.days

import org.example.utils.AdventDay

object Day23 : AdventDay {
    override fun part1(input: List<String>) {
        val connections = mutableMapOf<String, Set<String>>()
        val triples = mutableSetOf<List<String>>()
        input.forEach { line ->
            val (l, r) = line.split("-")
            connections[l] = connections.getOrDefault(l, emptySet()).plus(r)
            connections[r] = connections.getOrDefault(r, emptySet()).plus(l)
        }

        connections.forEach { (t, u) ->
            u.forEach { c1 ->
                val innerConnections = connections.getOrDefault(c1, emptySet())
                val intersect = u.intersect(innerConnections)
                intersect.forEach { c2 ->
                    triples.add(listOf(t, c1, c2).sorted())
                }
            }
        }

        val output = triples.filter { it.any { it.startsWith("t") } }.size
        print(output)
    }

    override fun part2(input: List<String>) {
        val connections = mutableMapOf<String, Set<String>>()
        input.forEach { line ->
            val (l, r) = line.split("-")
            connections[l] = connections.getOrDefault(l, emptySet()).plus(r)
            connections[r] = connections.getOrDefault(r, emptySet()).plus(l)
        }

        val result = bronKerbosch(connections)
        print(result)
    }

    private fun bronKerbosch(connections: Map<String, Set<String>>): String {
        var best = emptySet<String>()

        fun find(p: Set<String>, r: Set<String> = emptySet(), x: Set<String> = emptySet()) {
            if(p.isEmpty() && x.isEmpty()) {
                if(r.size > best.size) best = r
            } else {
                val mostConnections = (p + x).maxBy { connections.getValue(it).size }
                val pWithoutConnections = p.minus(connections.getValue(mostConnections))
                pWithoutConnections.forEach { v->
                    val vConnections = connections.getValue(v)
                    find(
                        p = p.intersect(vConnections),
                        r = r + v,
                        x = x.intersect(vConnections)
                    )
                }
            }
        }

        find(connections.keys)
        return best.sorted().joinToString(",")
    }
}