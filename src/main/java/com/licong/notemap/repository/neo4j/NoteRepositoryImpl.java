package com.licong.notemap.repository.neo4j;

public class NoteRepositoryImpl {


    @Autowired
    private Session session;


    public void mergeAll(List<Link> links) {
        List<Map<String, Object>> maps = new ArrayList<>();
        for (Link link : links) {
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("title", link.getTitle());
            objectMap.put("startId", link.getStart().getUniqueId());
            objectMap.put("endId", link.getEnd().getUniqueId());
            maps.add(objectMap);
        }

        String cypher = String.format("UNWIND {batch} as row " +
                "MATCH (s:%s{uniqueId:row.startId}),(e:%s{uniqueId:row.endId}) " +
                "MERGE (s)-[:%s{title:row.title}]->(e)", Node.LABLE, Node.LABLE, Link.TYPE);
        session.query(cypher, Collections.singletonMap("batch", maps));
    }

    public List<Link> findByStartInOrEndIn(List<String> nodeIds) {
        StringBuilder cypherBuilder = new StringBuilder();
        cypherBuilder.append("MATCH (n:NOTE)-[r:LINK]->(t:NOTE)" +
                " WHERE n.uniqueId IN {nodeIds} AND t.uniqueId IN {nodeIds}" +
                " RETURN r AS link, n AS start, t AS end");
        Map<String, Object> params = new HashMap<>();
        params.put("nodeIds", nodeIds);
        Result result = session.query(cypherBuilder.toString(), params);
        if (result == null) {
            return Collections.emptyList();
        }
        List<Link> links = new ArrayList<>();
        for (Map<String, Object> ret : result) {
            links.add((Link) ret.get("link"));
        }
        return links;
    }

    public List<Link> findByStartAndEndAndTitle(List<Link> links) {
        List<Map<String, Object>> maps = new ArrayList<>();
        for (Link link : links) {
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("title", link.getTitle());
            objectMap.put("startId", link.getStart().getUniqueId());
            objectMap.put("endId", link.getEnd().getUniqueId());
            maps.add(objectMap);
        }
        String cypher = String.format("UNWIND {batch} as row " +
                        "MATCH (s:%s{uniqueId:row.startId})-[l:%s{title:row.title}]->(e:%s{uniqueId:row.endId}) " +
                        "RETURN l",
                Node.LABLE,  Link.TYPE, Node.LABLE);
        Iterable<Link> iterable = session.query(Link.class, cypher, Collections.singletonMap("batch", maps));
        Iterator<Link> iterator = iterable.iterator();
        List<Link> result = new ArrayList<>();
        while (iterator.hasNext()) {
            Link link = iterator.next();
            result.add(link);
        }
        return result;
    }
}
