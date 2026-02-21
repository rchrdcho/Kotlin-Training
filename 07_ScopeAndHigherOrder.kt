/**
 * 07. 스코프 함수와 고차 함수
 *
 * Kotlin의 스코프 함수 (let, run, with, apply, also)와 고차 함수
 */

fun main() {
    println("=== 07. 스코프 함수와 고차 함수 ===\n")

    scopeFunctionsOverview()
    letExample()
    runExample()
    withExample()
    applyExample()
    alsoExample()
    higherOrderFunctionsExample()
}

// ============================================
// 스코프 함수 개요
// ============================================
fun scopeFunctionsOverview() {
    println("--- 스코프 함수 개요 ---")
    println("""
    스코프 함수 비교:

    함수    | this/it | 반환값        | 주요 용도
    --------|---------|---------------|------------------
    let     | it      | 람다 결과     | null 체크, 변환
    run     | this    | 람다 결과     | 객체 초기화 및 계산
    with    | this    | 람다 결과     | 객체 없이 호출
    apply   | this    | 객체 자체     | 객체 설정
    also    | it      | 객체 자체     | 부가 작업
    """.trimIndent())
    println()
}

// ============================================
// let: null 체크와 변환에 유용
// ============================================
fun letExample() {
    println("--- let ---")

    val nullableName: String? = "Alice"
    val nullValue: String? = null

    // null이 아닐 때만 실행
    nullableName?.let { name ->
        println("Name is not null: $name")
        println("Name length: ${name.length}")
    }

    nullValue?.let {
        println("This won't be executed")
    } ?: println("Value is null")

    // 변환
    data class User(val name: String, val email: String)

    val user: User? = User("Bob", "bob@example.com")
    val emailDomain = user?.let {
        it.email.substringAfter("@")
    }
    println("Email domain: $emailDomain")
    println()
}

// ============================================
// run: 객체의 메서드를 여러 번 호출할 때
// ============================================
fun runExample() {
    println("--- run ---")

    data class Config(
        var host: String = "",
        var port: Int = 0,
        var ssl: Boolean = false
    )

    // 객체에서 run 호출
    val config = Config().run {
        host = "localhost"
        port = 8080
        ssl = true
        this // 명시적으로 반환 (생략 가능)
    }
    println("Config: $config")

    // 독립적인 run 블록
    val result = run {
        val a = 10
        val b = 20
        a + b // 마지막 표현식이 반환값
    }
    println("Result: $result")
    println()
}

// ============================================
// with: 객체를 전달받아 사용
// ============================================
fun withExample() {
    println("--- with ---")

    data class Person(val name: String, val age: Int, val city: String)

    val person = Person("Alice", 30, "서울")

    // with는 확장 함수가 아님
    val description = with(person) {
        "이름: $name, 나이: $age, 도시: $city"
    }
    println(description)

    // StringBuilder와 함께 자주 사용
    val message = with(StringBuilder()) {
        append("Hello, ")
        append("Kotlin!")
        append(" Welcome.")
        toString()
    }
    println("Message: $message")
    println()
}

// ============================================
// apply: 객체 초기화와 설정
// ============================================
fun applyExample() {
    println("--- apply ---")

    data class User(
        var name: String = "",
        var age: Int = 0,
        var email: String = ""
    )

    // apply는 객체 자체를 반환
    val user = User().apply {
        name = "Alice"
        age = 30
        email = "alice@example.com"
    }
    println("User: $user")

    // 체이닝 가능
    val users = mutableListOf<User>().apply {
        add(User().apply {
            name = "Bob"
            age = 25
        })
        add(User().apply {
            name = "Charlie"
            age = 35
        })
    }
    println("Users: $users")
    println()
}

// ============================================
// also: 부가 작업 (로깅, 검증 등)
// ============================================
fun alsoExample() {
    println("--- also ---")

    data class Order(val id: Int, val amount: Double)

    fun processOrder(order: Order): Order {
        return order
            .also { println("Processing order ${it.id}") }
            .also { println("Amount: $${it.amount}") }
            .also { println("Order validated") }
    }

    val order = processOrder(Order(1, 100.0))
    println("Processed: $order")
    println()

    // 실전 예제: 파이프라인
    val numbers = listOf(1, 2, 3, 4, 5)
        .also { println("Original: $it") }
        .map { it * 2 }
        .also { println("After map: $it") }
        .filter { it > 5 }
        .also { println("After filter: $it") }

    println("Final: $numbers")
    println()
}

// ============================================
// 고차 함수 (Higher-Order Functions)
// ============================================
fun higherOrderFunctionsExample() {
    println("--- 고차 함수 ---")

    // 함수를 매개변수로 받는 함수
    fun <T> List<T>.customFilter(predicate: (T) -> Boolean): List<T> {
        val result = mutableListOf<T>()
        for (item in this) {
            if (predicate(item)) {
                result.add(item)
            }
        }
        return result
    }

    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val evenNumbers = numbers.customFilter { it % 2 == 0 }
    println("Even numbers: $evenNumbers")

    // 함수를 반환하는 함수
    fun makeMultiplier(factor: Int): (Int) -> Int {
        return { number -> number * factor }
    }

    val double = makeMultiplier(2)
    val triple = makeMultiplier(3)

    println("5 doubled: ${double(5)}")
    println("5 tripled: ${triple(5)}")

    // 실전 예제: 전략 패턴
    data class Product(val name: String, val price: Double, val category: String)

    val products = listOf(
        Product("Laptop", 1200.0, "Electronics"),
        Product("Book", 20.0, "Books"),
        Product("Phone", 800.0, "Electronics"),
        Product("Desk", 300.0, "Furniture")
    )

    fun applyDiscount(
        products: List<Product>,
        strategy: (Product) -> Double
    ): List<Pair<Product, Double>> {
        return products.map { product ->
            product to strategy(product)
        }
    }

    // 전략 1: 모든 제품 10% 할인
    val flatDiscount: (Product) -> Double = { it.price * 0.9 }

    // 전략 2: 카테고리별 차등 할인
    val categoryDiscount: (Product) -> Double = { product ->
        when (product.category) {
            "Electronics" -> product.price * 0.85  // 15% 할인
            "Books" -> product.price * 0.9         // 10% 할인
            else -> product.price * 0.95           // 5% 할인
        }
    }

    println("\nFlat discount:")
    applyDiscount(products, flatDiscount).forEach { (product, price) ->
        println("  ${product.name}: $${product.price} -> $${price}")
    }

    println("\nCategory discount:")
    applyDiscount(products, categoryDiscount).forEach { (product, price) ->
        println("  ${product.name}: $${product.price} -> $${price}")
    }

    // inline 함수
    inline fun <T> measureTime(block: () -> T): Pair<T, Long> {
        val start = System.currentTimeMillis()
        val result = block()
        val time = System.currentTimeMillis() - start
        return result to time
    }

    println("\nMeasuring time:")
    val (sum, elapsed) = measureTime {
        (1..1_000_000).sum()
    }
    println("Sum: $sum, Time: ${elapsed}ms")
}
