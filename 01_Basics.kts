/**
 * 01. 기본 문법 복습 (Java 관점 포함)
 *
 * Kotlin의 핵심 기본 문법을 빠르게 복습합니다.
 * Java에서 넘어온 학습자도 이해하기 쉽도록 짧은 비교 주석을 포함합니다.
 */

fun main() {
    println("=== 01. 기본 문법 복습 ===\n")

    entryPoints()
    variablesExample()
    stringTemplates()
    equalityExample()
    functionsExample()
    classesExample()
    nullSafetyExample()
    whenAndRanges()
    smartCastExample()
    objectsExample()
    extensionExample()
    collectionsExample()
    tryExpressionExample()
}

// ============================================
// 1. 엔트리 포인트와 top-level
// ============================================
fun entryPoints() {
    println("--- Entry Point & Top-Level ---")
    // Java 참고: 클래스 없이 파일 최상위에 함수/프로퍼티를 둘 수 있음
    println("Kotlin은 파일 최상위에 함수/프로퍼티를 둘 수 있습니다.")
    println("main()이 엔트리 포인트입니다.\n")
}

// ============================================
// 2. 변수 선언
// ============================================
fun variablesExample() {
    println("--- 변수 선언 ---")

    // val: 읽기 전용 (Java의 final과 유사)
    val name: String = "Kotlin"
    val version = 2.3  // 타입 추론

    // var: 변경 가능
    var count = 0
    count += 1

    println("$name $version, count: $count")
    println()
}

// ============================================
// 3. String Template
// ============================================
fun stringTemplates() {
    println("--- String Template ---")
    val lang = "Kotlin"
    val version = 2.3
    // Java 참고: 문자열 결합 대신 템플릿 사용
    println("Hello, $lang $version\n")
}

// ============================================
// 4. == vs ===
// ============================================
fun equalityExample() {
    println("--- == vs === ---")
    data class Person(val name: String)
    val a = Person("Alice")
    val b = Person("Alice")
    // Java 참고: == 는 equals(), === 는 참조 비교
    println("a == b  : ${a == b}  (값 비교)")
    println("a === b : ${a === b} (참조 비교)\n")
}

// ============================================
// 5. 함수
// ============================================
fun functionsExample() {
    println("--- 함수 ---")

    // 기본 함수
    fun greet(name: String): String {
        return "Hello, $name!"
    }

    // 단일 표현식 함수
    fun add(a: Int, b: Int) = a + b

    // 기본 매개변수 (오버로딩 대체)
    fun createUser(name: String, age: Int = 18) = "User($name, $age)"

    // 명명된 인자
    println(greet("Kotlin"))
    println("2 + 3 = ${add(2, 3)}")
    println(createUser("Alice"))
    println(createUser(name = "Bob", age = 25))
    println()
}

// ============================================
// 6. 클래스 / data class
// ============================================
fun classesExample() {
    println("--- 클래스 / data class ---")

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
// 7. Null Safety
// ============================================
fun nullSafetyExample() {
    println("--- Null Safety ---")

    // Nullable 타입
    val nullableName: String? = "Kotlin"
    val anotherName: String? = null

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

// ============================================
// 8. when + range
// ============================================
fun whenAndRanges() {
    println("--- when & range ---")
    // Java 참고: switch보다 유연한 when
    val age = 20
    val result = when (age) {
        in 0..17 -> "minor"
        in 18..64 -> "adult"
        else -> "senior"
    }
    println("age=$age -> $result\n")
}

// ============================================
// 9. 스마트 캐스트 (is 이후 자동 캐스팅)
// ============================================
fun smartCastExample() {
    println("--- Smart Cast ---")
    fun lengthOf(value: Any): Int = if (value is String) value.length else -1
    println("length: ${lengthOf("hello")}\n")
}

// ============================================
// 10. object / companion object (static 대체)
// ============================================
object Logger {
    fun log(message: String) = println("LOG: $message")
}

class Account private constructor(val name: String) {
    companion object {
        fun create(name: String) = Account(name)
    }
}

fun objectsExample() {
    println("--- object / companion object ---")
    // Java 참고: object는 싱글톤, companion object는 static 대체
    Logger.log("hello")
    println(Account.create("Alice").name)
    println()
}

// ============================================
// 11. 확장 함수
// ============================================
fun extensionExample() {
    println("--- Extension Function ---")
    fun String.truncate(max: Int) = if (length > max) take(max) + "..." else this
    println("Kotlin extensions".truncate(10))
    println()
}

// ============================================
// 12. 컬렉션: 읽기 전용 vs 변경 가능
// ============================================
fun collectionsExample() {
    println("--- Collections ---")
    val readOnly = listOf(1, 2, 3)
    val mutable = mutableListOf(1, 2, 3)
    mutable.add(4)
    println("readOnly: $readOnly")
    println("mutable: $mutable\n")
}

// ============================================
// 13. try는 표현식 (값 반환)
// ============================================
fun tryExpressionExample() {
    println("--- try expression ---")
    val value = try {
        "10".toInt()
    } catch (e: NumberFormatException) {
        0
    }
    println("value: $value\n")
}

main()
