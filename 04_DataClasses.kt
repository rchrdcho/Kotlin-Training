/**
 * 04. Data Classes 개선사항
 *
 * 기존 (2.0 이전): copy() 함수는 항상 public
 * 새로운 방식 (2.0+): copy() 함수가 constructor와 같은 visibility
 */

fun main() {
    println("=== 04. Data Classes 개선사항 ===\n")

    basicDataClassExample()
    dataClassFeaturesExample()
    // copyVisibilityExample() // Kotlin 2.0+ 필요
}

// ============================================
// 기본 Data Class 복습
// ============================================
fun basicDataClassExample() {
    println("--- Data Class 기본 ---")

    // Data class는 자동으로 생성:
    // - equals() / hashCode()
    // - toString()
    // - copy()
    // - componentN() (destructuring)
    data class Person(
        val name: String,
        val age: Int,
        val email: String
    )

    val person1 = Person("Alice", 30, "alice@example.com")
    val person2 = Person("Alice", 30, "alice@example.com")
    val person3 = person1.copy(age = 31)

    // equals & hashCode
    println("person1 == person2: ${person1 == person2}") // true
    println("person1 === person2: ${person1 === person2}") // false (다른 인스턴스)

    // toString
    println("person1: $person1")

    // copy
    println("person3 (age changed): $person3")

    // Destructuring
    val (name, age, email) = person1
    println("Destructured: $name, $age, $email")
    println()
}

// ============================================
// Data Class 고급 기능
// ============================================
fun dataClassFeaturesExample() {
    println("--- Data Class 고급 기능 ---")

    // 1. 기본값이 있는 Data Class
    data class Configuration(
        val host: String = "localhost",
        val port: Int = 8080,
        val ssl: Boolean = false,
        val timeout: Int = 30
    )

    val defaultConfig = Configuration()
    val prodConfig = Configuration(
        host = "api.example.com",
        ssl = true
    )

    println("Default: $defaultConfig")
    println("Production: $prodConfig")
    println()

    // 2. Data Class with validation
    data class Email private constructor(val value: String) {
        companion object {
            fun create(value: String): Email? {
                return if (value.contains("@")) {
                    Email(value)
                } else {
                    null
                }
            }
        }
    }

    val validEmail = Email.create("user@example.com")
    val invalidEmail = Email.create("invalid-email")

    println("Valid email: $validEmail")
    println("Invalid email: $invalidEmail")
    println()

    // 3. Data Class 상속 제한 (sealed class와 함께)
    sealed class Result<out T> {
        data class Success<T>(val data: T, val timestamp: Long = System.currentTimeMillis()) : Result<T>()
        data class Error(val message: String, val code: Int? = null) : Result<Nothing>()
        object Loading : Result<Nothing>()
    }

    val result: Result<String> = Result.Success("User data loaded")
    when (result) {
        is Result.Success -> println("Success at ${result.timestamp}: ${result.data}")
        is Result.Error -> println("Error ${result.code}: ${result.message}")
        Result.Loading -> println("Loading...")
    }
}

// ============================================
// Copy Visibility (Kotlin 2.0+)
// ============================================
// Kotlin 2.0부터 copy() 함수의 visibility가 constructor와 동일해짐
/*
fun copyVisibilityExample() {
    println("--- Copy Visibility (Kotlin 2.0+) ---")

    // 기존: copy()는 항상 public이었음
    // 문제: private constructor를 만들어도 copy()로 생성 가능

    // 새로운 방식: copy() visibility = constructor visibility
    data class SecureToken private constructor(
        val value: String,
        val expiresAt: Long
    ) {
        companion object {
            fun create(value: String): SecureToken {
                require(value.length >= 32) { "Token must be at least 32 characters" }
                return SecureToken(value, System.currentTimeMillis() + 3600_000)
            }
        }

        // Kotlin 2.0+에서는 copy()도 private!
        // 외부에서 token.copy(value = "short")로 우회 불가
    }

    val token = SecureToken.create("a".repeat(32))
    println("Token created: ${token.value.take(10)}...")

    // Kotlin 2.0 이전: token.copy(value = "hack") 가능 (보안 문제!)
    // Kotlin 2.0+: token.copy(...) 불가능 (컴파일 에러)

    println()
}
*/

// ============================================
// 실전 예제: Immutable 도메인 모델
// ============================================
fun immutableDomainExample() {
    println("--- 실전 예제: Immutable 도메인 모델 ---")

    data class Money(val amount: Double, val currency: String = "USD") {
        init {
            require(amount >= 0) { "Amount cannot be negative" }
        }

        operator fun plus(other: Money): Money {
            require(currency == other.currency) { "Currency mismatch" }
            return copy(amount = amount + other.amount)
        }

        operator fun minus(other: Money): Money {
            require(currency == other.currency) { "Currency mismatch" }
            require(amount >= other.amount) { "Insufficient funds" }
            return copy(amount = amount - other.amount)
        }

        override fun toString() = "$currency $amount"
    }

    data class BankAccount(
        val accountNumber: String,
        val balance: Money,
        val transactions: List<Transaction> = emptyList()
    ) {
        data class Transaction(
            val amount: Money,
            val type: TransactionType,
            val timestamp: Long = System.currentTimeMillis()
        )

        enum class TransactionType { DEPOSIT, WITHDRAWAL }

        fun deposit(amount: Money): BankAccount {
            val newBalance = balance + amount
            val transaction = Transaction(amount, TransactionType.DEPOSIT)
            return copy(
                balance = newBalance,
                transactions = transactions + transaction
            )
        }

        fun withdraw(amount: Money): BankAccount {
            val newBalance = balance - amount
            val transaction = Transaction(amount, TransactionType.WITHDRAWAL)
            return copy(
                balance = newBalance,
                transactions = transactions + transaction
            )
        }
    }

    var account = BankAccount(
        accountNumber = "1234-5678",
        balance = Money(1000.0)
    )

    println("Initial balance: ${account.balance}")

    account = account.deposit(Money(500.0))
    println("After deposit: ${account.balance}")

    account = account.withdraw(Money(200.0))
    println("After withdrawal: ${account.balance}")

    println("\nTransaction history:")
    account.transactions.forEach { tx ->
        println("  ${tx.type}: ${tx.amount}")
    }
}
