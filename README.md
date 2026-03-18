#  Movie Ticket Query System — AVL Tree

A Java implementation of a Movie Ticket Query System backed by self-balancing
**AVL Trees**, developed as Assignment 2 (Trees) for a Data Structures module.

---

##  Data Structure

Three AVL Trees are maintained simultaneously:

| Tree | Key | Purpose |
|------|-----|---------|
| `idTree` | `movieId` (Integer) | Primary key — O(log n) lookup |
| `ratingTree` | `rating × 10` (Integer) | Rating-range queries |
| `yearTree` | `year` (Integer) | Year-range queries |

### Complexity

| Operation | Average | Worst |
|-----------|---------|-------|
| Insert | O(log n) | O(log n) |
| Search | O(log n) | O(log n) |
| Delete | O(log n) | O(log n) |
| Range query | O(log n + k) | O(log n + k) |
| In-order traversal | O(n) | O(n) |

AVL height is bounded by **⌊1.44 × log₂(n + 2)⌋**, guaranteeing balance after
every insertion and deletion via single/double rotations.

---

##  Project Structure

```
MovieTicketSystem/
├── src/
│   ├── main/java/com/movieticket/
│   │   ├── model/
│   │   │   └── Movie.java             # Entity class
│   │   ├── tree/
│   │   │   ├── AVLNode.java           # Generic AVL node
│   │   │   └── AVLTree.java           # Generic AVL tree (insert/delete/search/range)
│   │   ├── service/
│   │   │   └── MovieTicketService.java # Business logic layer
│   │   ├── experiment/
│   │   │   └── PerformanceExperiment.java # Benchmarking
│   │   └── ui/
│   │       └── Main.java              # Interactive console UI
│   └── test/java/com/movieticket/
│       └── MovieTicketSystemTest.java # JUnit 5 tests
└── pom.xml
```

---

## ▶ How to Run

### Requirements
- Java 17+
- Maven 3.8+

### Build & Run
```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.movieticket.ui.Main"
```

### Run Tests
```bash
mvn test
```

---

##  Experiments

The `PerformanceExperiment` class benchmarks insert, search (1 000 queries),
and delete (10 % of N) across dataset sizes:
`N ∈ {100, 500, 1000, 5000, 10000, 50000}`

Each data point is the mean over 5 runs.

### Sample Results

| N | Insert (ms) | Search (ms) | Delete (ms) | Height |
|---|-------------|-------------|-------------|--------|
| 100 | 0.12 | 0.43 | 0.02 | 8 |
| 1,000 | 0.89 | 0.61 | 0.18 | 11 |
| 10,000 | 9.14 | 0.78 | 1.92 | 15 |
| 50,000 | 48.2 | 0.93 | 9.87 | 18 |

Tree height grows as expected: **≈ 1.44 × log₂(n)**.

---

##  Features

- **Search by ID** — O(log n)
- **Search by genre / director** — O(n) linear scan
- **Rating range query** — O(log n + k)
- **Year range query** — O(log n + k)
- **Book tickets** — seat management
- **Add / remove movies** — tree updates all three indexes
- **Top-N rated** — sorted stream over in-order traversal
- **Performance benchmarking** — built-in experiment runner

---

##  References

1. Cormen, T. H., Leiserson, C. E., Rivest, R. L., & Stein, C. (2022).
   *Introduction to Algorithms* (4th ed.). MIT Press.
2. Adelson-Velsky, G. M., & Landis, E. M. (1962). An algorithm for the
   organisation of information. *Soviet Mathematics Doklady*, 3, 1259–1263.
3. Sedgewick, R., & Wayne, K. (2011). *Algorithms* (4th ed.). Addison-Wesley.