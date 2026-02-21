/**
 * 06. 확장 함수와 위임
 *
 * Kotlin의 강력한 기능: 확장 함수와 위임 패턴
 */

fun main() {
    println("=== 06. 확장 함수와 위임 ===\n")

    extensionFunctionsExample()
    extensionPropertiesExample()
    delegationExample()
    delegatedPropertiesExample()
}

// ============================================
// 확장 함수 (Extension Functions)
// ============================================
fun extensionFunctionsExample() {
    println("--- 확장 함수 ---")

    // String에 확장 함수 추가
    fun String.isValidEmail(): Boolean {
        return this.contains("@") && this.contains(".")
    }

    fun String.truncate(maxLength: Int, suffix: String = "..."): String {
        return if (this.length > maxLength) {
            this.take(maxLength) + suffix
        } else {
            this
        }
    }

    val email = "user@example.com"
    val invalidEmail = "invalid-email"
    println("'$email' is valid: ${email.isValidEmail()}")
    println("'$invalidEmail' is valid: ${invalidEmail.isValidEmail()}")

    val longText = "This is a very long text that needs to be truncated"
    println("Truncated: ${longText.truncate(20)}")
    println()

    // List에 확장 함수 추가
    fun <T> List<T>.secondOrNull(): T? {
        return if (this.size >= 2) this[1] else null
    }

    val numbers = listOf(1, 2, 3, 4, 5)
    val shortList = listOf(1)
    println("Second of $numbers: ${numbers.secondOrNull()}")
    println("Second of $shortList: ${shortList.secondOrNull()}")
    println()
}

// ============================================
// 확장 프로퍼티 (Extension Properties)
// ============================================
fun extensionPropertiesExample() {
    println("--- 확장 프로퍼티 ---")

    // String에 확장 프로퍼티 추가
    val String.wordCount: Int
        get() = this.split(" ").filter { it.isNotEmpty() }.size

    val String.lastChar: Char?
        get() = this.lastOrNull()

    val text = "Hello Kotlin World"
    println("'$text' has ${text.wordCount} words")
    println("Last character: ${text.lastChar}")
    println()

    // 실전 예제: Date 확장
    data class Date(val year: Int, val month: Int, val day: Int)

    val Date.isLeapYear: Boolean
        get() = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)

    fun Date.formatted(): String = "$year-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"

    val date = Date(2024, 2, 29)
    println("Date: ${date.formatted()}")
    println("Is leap year: ${date.isLeapYear}")
    println()
}

// ============================================
// 위임 (Delegation)
// ============================================
fun delegationExample() {
    println("--- 위임 (Delegation) ---")

    // 인터페이스 위임 (by 키워드)
    interface Printer {
        fun print(message: String)
    }

    class ConsolePrinter : Printer {
        override fun print(message: String) {
            println("[Console] $message")
        }
    }

    class FilePrinter : Printer {
        override fun print(message: String) {
            println("[File] $message (saved to file)")
        }
    }

    // Logger가 Printer의 구현을 위임
    class Logger(printer: Printer) : Printer by printer {
        fun log(level: String, message: String) {
            print("[$level] $message")
        }
    }

    val consoleLogger = Logger(ConsolePrinter())
    val fileLogger = Logger(FilePrinter())

    consoleLogger.log("INFO", "Application started")
    fileLogger.log("ERROR", "Database connection failed")
    println()

    // 실전 예제: Repository 패턴
    interface Repository<T> {
        fun save(item: T)
        fun findAll(): List<T>
    }

    class InMemoryRepository<T> : Repository<T> {
        private val items = mutableListOf<T>()

        override fun save(item: T) {
            items.add(item)
        }

        override fun findAll(): List<T> = items.toList()
    }

    class CachedRepository<T>(
        private val delegate: Repository<T>
    ) : Repository<T> by delegate {
        private var cache: List<T>? = null

        override fun findAll(): List<T> {
            if (cache == null) {
                println("  Cache miss - fetching from delegate")
                cache = delegate.findAll()
            } else {
                println("  Cache hit!")
            }
            return cache!!
        }

        override fun save(item: T) {
            delegate.save(item)
            cache = null // Invalidate cache
        }
    }

    val repo = CachedRepository(InMemoryRepository<String>())
    repo.save("Item 1")
    repo.save("Item 2")

    println("First findAll:")
    println(repo.findAll())

    println("\nSecond findAll:")
    println(repo.findAll())
    println()
}

// ============================================
// 위임 프로퍼티 (Delegated Properties)
// ============================================
fun delegatedPropertiesExample() {
    println("--- 위임 프로퍼티 ---")

    // lazy: 처음 접근할 때만 초기화
    class HeavyObject {
        val expensiveData: String by lazy {
            println("  Computing expensive data...")
            Thread.sleep(100)
            "Expensive Result"
        }
    }

    val obj = HeavyObject()
    println("Object created")
    println("First access: ${obj.expensiveData}")
    println("Second access: ${obj.expensiveData}")
    println()

    // observable: 값이 변경될 때 콜백 실행
    class User {
        var name: String by observable("Unknown") { property, oldValue, newValue ->
            println("  ${property.name} changed from '$oldValue' to '$newValue'")
        }
    }

    val user = User()
    println("Initial name: ${user.name}")
    user.name = "Alice"
    user.name = "Bob"
    println()

    // vetoable: 값 변경을 검증
    class Product {
        var price: Double by vetoable(0.0) { _, oldValue, newValue ->
            if (newValue < 0) {
                println("  Rejected: Price cannot be negative")
                false
            } else {
                println("  Accepted: Price changed from $oldValue to $newValue")
                true
            }
        }
    }

    val product = Product()
    product.price = 100.0
    product.price = -50.0  // 거부됨
    println("Final price: ${product.price}")
    println()

    // Custom delegate
    class LoggingDelegate<T>(private var value: T) {
        operator fun getValue(thisRef: Any?, property: kotlin.reflect.KProperty<*>): T {
            println("  Getting ${property.name} = $value")
            return value
        }

        operator fun setValue(thisRef: Any?, property: kotlin.reflect.KProperty<*>, newValue: T) {
            println("  Setting ${property.name} from $value to $newValue")
            value = newValue
        }
    }

    class Config {
        var timeout: Int by LoggingDelegate(30)
    }

    val config = Config()
    println("Reading timeout:")
    println(config.timeout)
    println("\nSetting timeout:")
    config.timeout = 60
}

// Helper function for observable
private fun <T> observable(
    initialValue: T,
    onChange: (property: kotlin.reflect.KProperty<*>, oldValue: T, newValue: T) -> Unit
) = object {
    private var value = initialValue

    operator fun getValue(thisRef: Any?, property: kotlin.reflect.KProperty<*>): T {
        return value
    }

    operator fun setValue(thisRef: Any?, property: kotlin.reflect.KProperty<*>, newValue: T) {
        val old = value
        value = newValue
        onChange(property, old, newValue)
    }
}

// Helper function for vetoable
private fun <T> vetoable(
    initialValue: T,
    onChange: (property: kotlin.reflect.KProperty<*>, oldValue: T, newValue: T) -> Boolean
) = object {
    private var value = initialValue

    operator fun getValue(thisRef: Any?, property: kotlin.reflect.KProperty<*>): T {
        return value
    }

    operator fun setValue(thisRef: Any?, property: kotlin.reflect.KProperty<*>, newValue: T) {
        val old = value
        if (onChange(property, old, newValue)) {
            value = newValue
        }
    }
}

main()
