/**
 * 02. Sealed Classes & Sealed Interfaces
 *
 * 기존 (2021 이전): Sealed Classes만 사용 가능, 같은 파일에 모든 서브클래스 필요
 * 새로운 방식 (1.5+): Sealed Interfaces 추가, 같은 패키지면 다른 파일도 가능
 */

fun main() {
    println("=== 02. Sealed Types ===\n")

    oldWayExample()
    newWayExample()
}

// ============================================
// 기존 방식: Sealed Classes만 사용
// ============================================
println("--- 기존 방식: Sealed Classes ---")

// Sealed class는 제한된 클래스 계층 구조를 표현
// 문제점: 단일 상속만 가능
sealed class Result {
    data class Success(val data: String) : Result()
    data class Error(val message: String) : Result()
    object Loading : Result()
}

fun oldWayExample() {
    fun handleResult(result: Result): String = when (result) {
        is Result.Success -> "데이터: ${result.data}"
        is Result.Error -> "에러: ${result.message}"
        Result.Loading -> "로딩 중..."
        // else 불필요 - 컴파일러가 모든 케이스를 알고 있음
    }

    val success = Result.Success("사용자 데이터")
    val error = Result.Error("네트워크 오류")
    val loading = Result.Loading

    println(handleResult(success))
    println(handleResult(error))
    println(handleResult(loading))
    println()
}

// ============================================
// 새로운 방식: Sealed Interfaces (Kotlin 1.5+)
// ============================================
println("--- 새로운 방식: Sealed Interfaces ---")

// Sealed interface는 더 유연한 계층 구조 가능
// 장점: 여러 sealed interface 구현 가능
sealed interface UiState

sealed interface LoadingState : UiState {
    object Idle : LoadingState
    object Loading : LoadingState
}

sealed interface DataState : UiState {
    data class Success(val data: String) : DataState
    data class Empty(val message: String) : DataState
}

sealed interface ErrorState : UiState {
    data class NetworkError(val code: Int) : ErrorState
    data class ValidationError(val field: String) : ErrorState
}

// 클래스가 여러 sealed interface를 구현할 수 있음!
data class SuccessWithLoading(
    val data: String,
    val isRefreshing: Boolean
) : DataState, LoadingState

fun newWayExample() {
    fun handleUiState(state: UiState): String = when (state) {
        LoadingState.Idle -> "대기 중"
        LoadingState.Loading -> "로딩 중..."
        is DataState.Success -> "성공: ${state.data}"
        is DataState.Empty -> "비어있음: ${state.message}"
        is ErrorState.NetworkError -> "네트워크 에러 (코드: ${state.code})"
        is ErrorState.ValidationError -> "검증 실패: ${state.field}"
        is SuccessWithLoading -> "데이터: ${state.data} (새로고침: ${state.isRefreshing})"
    }

    val states = listOf(
        LoadingState.Idle,
        LoadingState.Loading,
        DataState.Success("사용자 목록"),
        DataState.Empty("결과 없음"),
        ErrorState.NetworkError(404),
        ErrorState.ValidationError("email"),
        SuccessWithLoading("데이터", true)
    )

    states.forEach { state ->
        println(handleUiState(state))
    }
    println()
}

// ============================================
// 실전 예제: API 응답 모델링
// ============================================
println("--- 실전 예제 ---")

// 복잡한 API 응답 상태를 명확하게 표현
sealed interface ApiResponse<out T> {
    data class Success<T>(val data: T, val cached: Boolean = false) : ApiResponse<T>
    data class Error(val code: Int, val message: String) : ApiResponse<Nothing>
    object Loading : ApiResponse<Nothing>
}

data class UserProfile(val id: Int, val name: String, val email: String)

fun processApiResponse() {
    val response: ApiResponse<UserProfile> = ApiResponse.Success(
        data = UserProfile(1, "Alice", "alice@example.com"),
        cached = false
    )

    when (response) {
        is ApiResponse.Success -> {
            println("유저 정보: ${response.data.name}")
            if (response.cached) println("(캐시된 데이터)")
        }
        is ApiResponse.Error -> println("에러 ${response.code}: ${response.message}")
        ApiResponse.Loading -> println("로딩 중...")
    }
}

main()
