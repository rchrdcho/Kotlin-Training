/**
 * 05. Collections & Functional Programming
 *
 * Kotlin의 컬렉션 API와 함수형 프로그래밍 기능 복습
 */

fun main() {
    println("=== 05. Collections & Functional Programming ===\n")

    basicCollectionsExample()
    transformationsExample()
    filteringExample()
    aggregationExample()
    sequencesExample()
}

// ============================================
// 기본 컬렉션
// ============================================
fun basicCollectionsExample() {
    println("--- 기본 컬렉션 ---")

    // List (읽기 전용)
    val numbers = listOf(1, 2, 3, 4, 5)
    val mutableNumbers = mutableListOf(1, 2, 3)
    mutableNumbers.add(4)

    // Set (중복 제거)
    val uniqueNumbers = setOf(1, 2, 2, 3, 3, 4)
    println("Unique: $uniqueNumbers") // [1, 2, 3, 4]

    // Map
    val ages = mapOf(
        "Alice" to 30,
        "Bob" to 25,
        "Charlie" to 35
    )
    println("Bob's age: ${ages["Bob"]}")

    // Collection builder functions
    val evenNumbers = buildList {
        for (i in 1..10) {
            if (i % 2 == 0) add(i)
        }
    }
    println("Even numbers: $evenNumbers")
    println()
}

// ============================================
// 변환 (Transformations)
// ============================================
fun transformationsExample() {
    println("--- 변환 (Transformations) ---")

    data class User(val name: String, val age: Int, val city: String)

    val users = listOf(
        User("Alice", 30, "서울"),
        User("Bob", 25, "부산"),
        User("Charlie", 35, "서울"),
        User("David", 28, "대구")
    )

    // map: 각 요소를 변환
    val names = users.map { it.name }
    println("Names: $names")

    // mapNotNull: null이 아닌 결과만
    val ages = users.mapNotNull { user ->
        if (user.age >= 30) user.age else null
    }
    println("Ages 30+: $ages")

    // flatMap: 중첩된 컬렉션을 평탄화
    val nameChars = users.flatMap { user ->
        user.name.toList()
    }
    println("All characters: ${nameChars.take(10)}...")

    // associate: Map 생성
    val nameToAge = users.associate { it.name to it.age }
    println("Name to age: $nameToAge")

    // groupBy: 그룹화
    val byCity = users.groupBy { it.city }
    println("By city: $byCity")
    println()
}

// ============================================
// 필터링 (Filtering)
// ============================================
fun filteringExample() {
    println("--- 필터링 (Filtering) ---")

    val numbers = (1..20).toList()

    // filter: 조건에 맞는 요소만
    val evenNumbers = numbers.filter { it % 2 == 0 }
    println("Even: $evenNumbers")

    // partition: true/false로 분리
    val (even, odd) = numbers.partition { it % 2 == 0 }
    println("Even: $even")
    println("Odd: $odd")

    // take, drop
    val first5 = numbers.take(5)
    val withoutFirst5 = numbers.drop(5)
    println("First 5: $first5")
    println("Without first 5: ${withoutFirst5.take(5)}...")

    // takeWhile, dropWhile
    val lessThan10 = numbers.takeWhile { it < 10 }
    println("Less than 10: $lessThan10")

    // distinct: 중복 제거
    val duplicates = listOf(1, 2, 2, 3, 3, 3, 4)
    println("Distinct: ${duplicates.distinct()}")
    println()
}

// ============================================
// 집계 (Aggregation)
// ============================================
fun aggregationExample() {
    println("--- 집계 (Aggregation) ---")

    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    // sum, average, max, min
    println("Sum: ${numbers.sum()}")
    println("Average: ${numbers.average()}")
    println("Max: ${numbers.maxOrNull()}")
    println("Min: ${numbers.minOrNull()}")

    // count
    println("Count of even: ${numbers.count { it % 2 == 0 }}")

    // reduce, fold
    val sum = numbers.reduce { acc, n -> acc + n }
    val product = numbers.fold(1) { acc, n -> acc * n }
    println("Sum (reduce): $sum")
    println("Product (fold): $product")

    // joinToString
    val formatted = numbers.joinToString(
        separator = ", ",
        prefix = "[",
        postfix = "]",
        limit = 5,
        truncated = "..."
    )
    println("Formatted: $formatted")

    // 실전 예제
    data class Order(val id: Int, val amount: Double, val status: String)

    val orders = listOf(
        Order(1, 100.0, "completed"),
        Order(2, 150.0, "completed"),
        Order(3, 200.0, "pending"),
        Order(4, 75.0, "completed")
    )

    val totalCompleted = orders
        .filter { it.status == "completed" }
        .sumOf { it.amount }

    println("\nTotal completed orders: $$totalCompleted")
    println()
}

// ============================================
// Sequences (지연 평가)
// ============================================
fun sequencesExample() {
    println("--- Sequences (지연 평가) ---")

    // List: 즉시 평가 (eager)
    // Sequence: 지연 평가 (lazy)

    println("List (eager):")
    val list = (1..10).toList()
        .map {
            println("  map: $it")
            it * 2
        }
        .filter {
            println("  filter: $it")
            it > 10
        }
        .take(3)
    println("Result: $list\n")

    println("Sequence (lazy):")
    val sequence = (1..10).asSequence()
        .map {
            println("  map: $it")
            it * 2
        }
        .filter {
            println("  filter: $it")
            it > 10
        }
        .take(3)
        .toList() // 여기서 실제 계산 시작!
    println("Result: $sequence\n")

    // 대용량 데이터에서 Sequence가 효율적
    val largeData = generateSequence(1) { it + 1 }
        .take(1_000_000)
        .filter { it % 2 == 0 }
        .map { it * it }
        .take(10)
        .toList()

    println("First 10 even squares: $largeData")
}

main()
