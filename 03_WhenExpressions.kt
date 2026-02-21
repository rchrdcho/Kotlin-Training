/**
 * 03. When Expressions with Guard Conditions
 *
 * 기존 (2.0 이전): When 표현식에 단일 조건만 사용 가능
 * 새로운 방식 (2.1+): Guard 조건으로 복잡한 조건 분기 가능
 */

fun main() {
    println("=== 03. When Expressions ===\n")

    oldWayExample()
    // newWayExample() // Kotlin 2.1+ 필요
}

// ============================================
// 기존 방식: 중첩된 if-else 또는 복잡한 when
// ============================================
fun oldWayExample() {
    println("--- 기존 방식: 중첩된 조건문 ---")

    data class User(val name: String, val age: Int, val isVerified: Boolean)

    fun getUserStatus(user: User): String {
        // 방법 1: 중첩된 if-else
        return if (user.age < 18) {
            "미성년자"
        } else if (user.age >= 18 && user.age < 65) {
            if (user.isVerified) {
                "성인 (인증됨)"
            } else {
                "성인 (미인증)"
            }
        } else {
            "시니어"
        }
    }

    // 방법 2: when with 복잡한 조건
    fun getUserStatusWithWhen(user: User): String = when {
        user.age < 18 -> "미성년자"
        user.age in 18..64 && user.isVerified -> "성인 (인증됨)"
        user.age in 18..64 && !user.isVerified -> "성인 (미인증)"
        user.age >= 65 -> "시니어"
        else -> "알 수 없음"
    }

    val users = listOf(
        User("Alice", 16, false),
        User("Bob", 25, true),
        User("Charlie", 30, false),
        User("Dave", 70, true)
    )

    users.forEach { user ->
        println("${user.name}: ${getUserStatus(user)}")
    }
    println()
}

// ============================================
// 새로운 방식: Guard Conditions (Kotlin 2.1+)
// ============================================
// 주의: 이 코드는 Kotlin 2.1+ 에서만 작동합니다
/*
fun newWayExample() {
    println("--- 새로운 방식: Guard Conditions (Kotlin 2.1+) ---")

    data class User(val name: String, val age: Int, val isVerified: Boolean)

    // Guard 조건으로 더 명확하고 간결한 표현
    fun getUserStatus(user: User): String = when (user.age) {
        in 0..17 -> "미성년자"
        in 18..64 if user.isVerified -> "성인 (인증됨)"
        in 18..64 if !user.isVerified -> "성인 (미인증)"
        in 65..200 -> "시니어"
        else -> "알 수 없음"
    }

    // 더 복잡한 예제: 주문 상태 처리
    sealed interface OrderStatus {
        data class Pending(val daysSincePlaced: Int) : OrderStatus
        data class Processing(val estimatedDays: Int, val isPriority: Boolean) : OrderStatus
        data class Shipped(val trackingNumber: String, val isInternational: Boolean) : OrderStatus
        object Delivered : OrderStatus
    }

    fun getOrderMessage(status: OrderStatus): String = when (status) {
        is OrderStatus.Pending if status.daysSincePlaced > 3 ->
            "경고: 주문이 ${status.daysSincePlaced}일째 대기 중입니다"
        is OrderStatus.Pending ->
            "주문 대기 중 (${status.daysSincePlaced}일)"

        is OrderStatus.Processing if status.isPriority && status.estimatedDays <= 1 ->
            "우선 처리 중 - 내일 도착 예정"
        is OrderStatus.Processing if status.isPriority ->
            "우선 처리 중 - ${status.estimatedDays}일 후 도착 예정"
        is OrderStatus.Processing ->
            "처리 중 - ${status.estimatedDays}일 후 도착 예정"

        is OrderStatus.Shipped if status.isInternational ->
            "국제 배송 중 (송장번호: ${status.trackingNumber})"
        is OrderStatus.Shipped ->
            "배송 중 (송장번호: ${status.trackingNumber})"

        OrderStatus.Delivered ->
            "배송 완료"
    }

    val orders = listOf(
        OrderStatus.Pending(1),
        OrderStatus.Pending(5),
        OrderStatus.Processing(1, true),
        OrderStatus.Processing(3, false),
        OrderStatus.Shipped("ABC123", false),
        OrderStatus.Shipped("INT456", true),
        OrderStatus.Delivered
    )

    orders.forEach { order ->
        println(getOrderMessage(order))
    }
    println()
}
*/

// ============================================
// 실전 예제: 현재 버전에서 최선의 방법
// ============================================
fun bestPracticeExample() {
    println("--- 실전 예제: 패턴 매칭 스타일 ---")

    sealed interface PaymentStatus {
        data class Pending(val amount: Double) : PaymentStatus
        data class Processing(val amount: Double, val fee: Double) : PaymentStatus
        data class Completed(val amount: Double, val transactionId: String) : PaymentStatus
        data class Failed(val reason: String) : PaymentStatus
    }

    fun getPaymentMessage(status: PaymentStatus): String = when (status) {
        is PaymentStatus.Pending -> "결제 대기 중: $${status.amount}"
        is PaymentStatus.Processing -> {
            val total = status.amount + status.fee
            "처리 중: $${status.amount} + 수수료 $${status.fee} = $${total}"
        }
        is PaymentStatus.Completed ->
            "완료: $${status.amount} (거래번호: ${status.transactionId})"
        is PaymentStatus.Failed ->
            "실패: ${status.reason}"
    }

    val payments = listOf(
        PaymentStatus.Pending(100.0),
        PaymentStatus.Processing(100.0, 2.5),
        PaymentStatus.Completed(102.5, "TXN-12345"),
        PaymentStatus.Failed("카드 승인 거부")
    )

    payments.forEach { payment ->
        println(getPaymentMessage(payment))
    }
}
