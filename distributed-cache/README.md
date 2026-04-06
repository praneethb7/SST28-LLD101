# Distributed Cache

Low-Level Design assignment for a distributed cache system with pluggable distribution and eviction strategies.

## Class Diagram

```
                         ┌──────────────────────────────────────────────────┐
                         │              DistributedCache                    │
                         │──────────────────────────────────────────────────│
                         │ - nodes: List<CacheNode>                        │
                         │ - strategy: DistributionStrategy                │
                         │ - db: Database                                  │
                         │──────────────────────────────────────────────────│
                         │ + get(key: String): String                      │
                         │ + put(key: String, value: String): void         │
                         │ + addNode(node: CacheNode): void                │
                         │ + removeNode(nodeId: int): void                 │
                         └──────────┬─────────────────┬────────────────────┘
                                    │                 │
                   uses             │                 │  uses
              ┌─────────────────────┘                 └──────────────────┐
              ▼                                                          ▼
┌──────────────────────────────┐                     ┌──────────────────────────────┐
│  <<interface>>                │                     │         CacheNode             │
│  DistributionStrategy        │                     │──────────────────────────────│
│──────────────────────────────│                     │ - nodeId: int                │
│ + getNode(key, nodes): Node  │                     │ - capacity: int              │
└──────────┬───────────────────┘                     │ - evictionPolicy: Eviction   │
           │                                          │ - store: Map<String, String> │
           │ implements                               │──────────────────────────────│
           │                                          │ + get(key): String           │
     ┌─────┴──────────┐                              │ + put(key, value): void      │
     │                 │                              │ + containsKey(key): boolean  │
     ▼                 ▼                              │ + remove(key): void          │
┌──────────┐  ┌──────────────────┐                   │ + isFull(): boolean          │
│ ModStrat  │  │ ConsistentHash   │                   └──────────────┬───────────────┘
│──────────│  │──────────────────│                                  │
│ getNode()│  │ - ring: TreeMap   │                                  │ uses
└──────────┘  │──────────────────│                                  ▼
              │ + getNode()       │                   ┌──────────────────────────────┐
              │ + addNode()       │                   │  <<interface>>                │
              │ + removeNode()    │                   │  EvictionPolicy              │
              └──────────────────┘                   │──────────────────────────────│
                                                      │ + evict(store): String       │
┌──────────────────────────────┐                     │ + recordAccess(key): void    │
│  <<interface>>                │                     └──────────┬───────────────────┘
│  Database                    │                                │
│──────────────────────────────│                                │ implements
│ + fetch(key: String): String │                                │
│ + save(key, value): void     │                     ┌──────────┼──────────┐
└──────────────────────────────┘                     │          │          │
              ▲                                       ▼          ▼          ▼
              │ implements                   ┌─────────┐ ┌─────────┐ ┌─────────┐
              │                              │  LRU     │ │  MRU    │ │  LFU    │
┌──────────────────────────────┐             │─────────│ │─────────│ │─────────│
│       HashMapDB              │             │ - order │ │ - order │ │ - freq  │
│──────────────────────────────│             │  Deque  │ │  Deque  │ │  Map    │
│ - data: Map<String, String>  │             │─────────│ │─────────│ │─────────│
│──────────────────────────────│             │ evict() │ │ evict() │ │ evict() │
│ + fetch(key): String         │             │ record()│ │ record()│ │ record()│
│ + save(key, value): void     │             └─────────┘ └─────────┘ └─────────┘
└──────────────────────────────┘
```

## Design & Approach

### How Data is Distributed Across Nodes

`DistributedCache` holds a list of `CacheNode` instances and a `DistributionStrategy`. When `get` or `put` is called, the strategy decides which node handles that key.

**ModuloStrategy** (simple approach):
```
nodeIndex = hash(key) % numberOfNodes
```
Quick and easy, but adding/removing nodes remaps almost every key.

**ConsistentHashStrategy** (future/advanced):
- Nodes are placed on a virtual ring using their hash.
- A key is assigned to the next node clockwise on the ring.
- Adding/removing a node only remaps keys near that node, not the entire cache.

Because `DistributionStrategy` is an interface, swapping between these is a one-line change.

### How Cache Miss is Handled

```
get(key):
    node = strategy.getNode(key, nodes)
    if node.containsKey(key):
        return node.get(key)           // cache hit
    else:
        value = db.fetch(key)          // cache miss -> go to DB
        node.put(key, value)           // store in cache for next time
        return value
```

The database is abstracted behind a `Database` interface. In this exercise `HashMapDB` simulates it with an in-memory map.

### How Eviction Works

Each `CacheNode` has a fixed capacity and an `EvictionPolicy`.

When `put` is called on a full node:
```
put(key, value):
    if store.size() >= capacity:
        victimKey = evictionPolicy.evict(store)
        store.remove(victimKey)
    store.put(key, value)
    evictionPolicy.recordAccess(key)
```

**LRU** — maintains a `Deque` ordered by access time. `recordAccess` moves the key to the front. `evict` removes from the back (least recently used).

**MRU** — same structure but `evict` removes from the front (most recently used).

**LFU** — maintains a frequency map. `recordAccess` increments the count. `evict` removes the key with the lowest frequency.

### How the Design Supports Extensibility

| Extension Point | Interface | Swap by |
|---|---|---|
| Distribution strategy | `DistributionStrategy` | Pass a different impl to `DistributedCache` constructor |
| Eviction policy | `EvictionPolicy` | Pass a different impl to `CacheNode` constructor |
| Database backend | `Database` | Pass a different impl to `DistributedCache` constructor |

Adding a new strategy or policy means writing one class that implements the interface. No existing code changes.

### Flow Summary

```
Client
  │
  │  get("user:42")
  ▼
DistributedCache
  │
  │  strategy.getNode("user:42", nodes)  -->  picks CacheNode-2
  ▼
CacheNode-2
  │
  ├── cache hit?  -->  return value
  │
  └── cache miss? -->  db.fetch("user:42")
                        store in CacheNode-2 (evict if full)
                        return value
```

## Classes

| Class / Interface | Responsibility |
|---|---|
| `DistributedCache` | Entry point — routes get/put to the right node |
| `CacheNode` | Single cache node with bounded storage and eviction |
| `DistributionStrategy` | Interface — decides which node owns a key |
| `ModuloStrategy` | hash(key) % n distribution |
| `ConsistentHashStrategy` | Ring-based consistent hashing |
| `EvictionPolicy` | Interface — decides what to evict when full |
| `LRUEviction` | Least Recently Used eviction |
| `MRUEviction` | Most Recently Used eviction |
| `LFUEviction` | Least Frequently Used eviction |
| `Database` | Interface — backing store for cache misses |
| `HashMapDB` | Simple in-memory database simulation |
