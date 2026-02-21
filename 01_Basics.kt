/**
 * 01. 기본 문법 복습
 *
 * Kotlin의 핵심 기본 문법을 빠르게 복습합니다.
 */

fun main() {
    println("=== 01. 기본 문법 복습 ===\n")

    // 1. 변수 선언
    variablesExample()

    // 2. 함수
    functionsExample()

    // 3. 클래스
    classesExample()

    // 4. Null Safety
    nullSafetyExample()
}

// ============================================
// 1. 변수 선언
// ============================================
fun variablesExample() {
    println("--- 변수 선언 ---")

    // val: 읽기 전용 (immutable)
    val name: String = "Kotlin"
    val version = 2.3  // 타입 추론

    // var: 변경 가능 (mutable)
    var count = 0
    count += 1

    println("$name $version, count: $count")
    println()
}

// ============================================
// 2. 함수
// ============================================
fun functionsExample() {
    println("--- 함수 ---")

    // 기본 함수
    fun greet(name: String): String {
        return "Hello, $name!"
    }

    // 단일 표현식 함수
    fun add(a: Int, b: Int) = a + b

    // 기본 매개변수
    fun createUser(name: String, age: Int = 18) = "User($name, $age)"

    // 명명된 인자
    println(greet("Kotlin"))
    println("2 + 3 = ${add(2, 3)}")
    println(createUser("Alice"))
    println(createUser(name = "Bob", age = 25))
    println()
}

// ============================================
// 3. 클래스
// ============================================
fun classesExample() {
    println("--- 클래스 ---")

    // 기본 클래스
    class Person(val name: String, var age: Int)

    // Data 클래스 (equals, hashCode, toString, copy 자동 생성)
    data class User(val id: Int, val name: String, val email: String)

    val person = Person("Alice", 30)
    val user = User(1, "Bob", "bob@example.com")

    println("Person: ${person.name}, ${person.age}")
    println("User: $user")

    // copy 함수로 일부 속성만 변경
    val updatedUser = user.copy(email = "newemail@example.com")
    println("Updated: $updatedUser")
    println()
}

// ============================================
// 4. Null Safety
// ============================================
fun nullSafetyExample() {
    println("--- Null Safety ---")

    // Nullable 타입
    var nullableName: String? = "Kotlin"
    var anotherName: String? = null

    // Safe call (?.)
    println("Length: ${nullableName?.length}")
    println("Length: ${anotherName?.length}")

    // Elvis operator (?:)
    val length = anotherName?.length ?: 0
    println("Length with default: $length")

    // let 함수와 함께 사용
    nullableName?.let {
        println("Name is not null: $it")
    }

    println()
}
