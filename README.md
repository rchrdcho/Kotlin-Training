# Kotlin 재활 훈련 (Kotlin Refresher)

2021년 이후 Kotlin을 사용하지 않았던 분들을 위한 단계별 학습 자료입니다.

## 학습 목표

- Kotlin 기본 문법 복습
- 2021년 이후 추가된 새로운 기능 학습
- "기존 방식 vs 새로운 방식" 비교를 통한 효과적인 학습

## Kotlin 버전 히스토리 (2021-2026)

### 주요 릴리스

- **Kotlin 1.5.0** (2021.05): Sealed interfaces, JVM Records 지원
- **Kotlin 1.6.0** (2021.11): Exhaustive when, JVM 17 지원
- **Kotlin 1.7.0** (2022.06): K2 컴파일러 알파
- **Kotlin 1.8.0** (2023.02): JS IR 컴파일러 안정화
- **Kotlin 1.9.0** (2023.07): K2 컴파일러 베타
- **Kotlin 2.0.0** (2024.05): K2 컴파일러 안정화 🎉
- **Kotlin 2.1.0** (2024.11): When with guard conditions
- **Kotlin 2.2.0** (2025): Context parameters
- **Kotlin 2.3.0** (2025.12): 최신 버전

### 주요 변경사항

#### K2 컴파일러 (Kotlin 2.0)
- 컴파일 속도 2배 향상
- 더 정확한 타입 추론
- 멀티플랫폼 지원 강화

#### 언어 기능
- **Sealed Interfaces** (1.5): 더 유연한 타입 계층
- **Guard Conditions** (2.1): When 표현식에 복합 조건
- **Context Parameters** (2.2): Context receivers 개선
- **Data Class Copy Visibility** (2.0): Copy 함수의 접근 제어

## 학습 자료 구성

### 01. 기본 문법 복습
- `01_Basics.kts`
- 변수, 함수, 클래스, Null Safety
- Kotlin의 핵심 문법 빠른 복습

### 02. Sealed Types
- `02_SealedTypes.kts`
- 기존: Sealed Classes만 사용
- 새로운: Sealed Interfaces로 더 유연한 계층 구조
- 실전 예제: API 응답 모델링

### 03. When Expressions
- `03_WhenExpressions.kts`
- 기존: 중첩된 if-else 또는 복잡한 when
- 새로운: Guard conditions (Kotlin 2.1+)
- 패턴 매칭 스타일 개선

### 04. Data Classes
- `04_DataClasses.kts`
- Data class 기본 기능 복습
- Copy function visibility 개선 (Kotlin 2.0+)
- Immutable 도메인 모델 예제

### 05. Collections & Functional Programming
- `05_Collections.kts`
- 컬렉션 API 완벽 정리
- 변환, 필터링, 집계 함수
- Sequences를 통한 지연 평가

### 06. 확장 함수와 위임
- `06_ExtensionsAndDelegation.kts`
- 확장 함수/프로퍼티로 기능 추가
- 위임 패턴 (by 키워드)
- 위임 프로퍼티 (lazy, observable, vetoable)

### 07. 스코프 함수와 고차 함수
- `07_ScopeAndHigherOrder.kts`
- let, run, with, apply, also 완벽 정리
- 고차 함수와 함수형 프로그래밍
- 실전 예제: 전략 패턴

## 학습 방법
각 파일은 `.kts` 스크립트이며, 파일 끝에서 `main()`을 호출하도록 되어 있어 바로 실행 가능합니다.

### 1단계: 기본 복습 (30분)
```bash
# Kotlin이 설치되어 있다면:
kotlin 01_Basics.kts
```

기본 문법을 빠르게 복습합니다. 변수, 함수, 클래스 등 핵심 개념을 상기시킵니다.

### 2단계: 새로운 기능 학습 (1-2시간)
```bash
kotlin 02_SealedTypes.kts
kotlin 03_WhenExpressions.kts
kotlin 04_DataClasses.kts
```

2021년 이후 추가된 새로운 기능들을 "기존 방식 vs 새로운 방식" 비교를 통해 학습합니다.

### 3단계: 고급 기능 (1-2시간)
```bash
kotlin 05_Collections.kts
kotlin 06_ExtensionsAndDelegation.kts
kotlin 07_ScopeAndHigherOrder.kts
```

Kotlin의 강력한 기능들을 실전 예제와 함께 학습합니다.

## 주요 개념 요약

### Sealed Interfaces (Kotlin 1.5+)
```kotlin
// 기존: Sealed class만 사용 가능
sealed class Result {
    data class Success(val data: String) : Result()
    data class Error(val message: String) : Result()
}

// 새로운: Sealed interface로 더 유연하게
sealed interface UiState
sealed interface LoadingState : UiState
sealed interface DataState : UiState

// 여러 인터페이스 구현 가능!
data class SuccessWithLoading(val data: String) : DataState, LoadingState
```

### When with Guard Conditions (Kotlin 2.1+)
```kotlin
// 기존: 중첩된 if-else
if (user.age >= 18 && user.age < 65) {
    if (user.isVerified) {
        "성인 (인증됨)"
    } else {
        "성인 (미인증)"
    }
}

// 새로운: Guard conditions
when (user.age) {
    in 18..64 if user.isVerified -> "성인 (인증됨)"
    in 18..64 if !user.isVerified -> "성인 (미인증)"
}
```

### 스코프 함수 비교
| 함수 | this/it | 반환값 | 주요 용도 |
|------|---------|--------|-----------|
| let | it | 람다 결과 | null 체크, 변환 |
| run | this | 람다 결과 | 객체 초기화 및 계산 |
| with | this | 람다 결과 | 객체 없이 호출 |
| apply | this | 객체 자체 | 객체 설정 |
| also | it | 객체 자체 | 부가 작업 |

## 실전 팁

### 1. Sealed Types 활용
API 응답, UI 상태, 결과 타입 등 제한된 상태를 표현할 때 사용하세요.

### 2. Extension Functions
기존 클래스에 기능을 추가할 때 상속 대신 확장 함수를 사용하세요.

### 3. Sequences 사용
대용량 컬렉션 처리 시 Sequence로 지연 평가하여 성능을 개선하세요.

### 4. 스코프 함수 선택
- Null 체크: `?.let { }`
- 객체 초기화: `.apply { }`
- 로깅/디버깅: `.also { }`
- 값 변환: `.let { }`

## 추가 학습 자료

- [Kotlin 공식 문서](https://kotlinlang.org/docs/home.html)
- [What's new in Kotlin](https://kotlinlang.org/docs/releases.html)
- [Kotlin Blog](https://blog.jetbrains.com/kotlin/)

## 참고 자료

이 학습 자료는 다음 공식 문서를 참고하여 작성되었습니다:

- [Kotlin 2.3.0 Released](https://blog.jetbrains.com/kotlin/2025/12/kotlin-2-3-0-released/)
- [What's new in Kotlin 2.2.0](https://kotlinlang.org/docs/whatsnew22.html)
- [What's new in Kotlin 2.1.0](https://kotlinlang.org/docs/whatsnew21.html)
- [Celebrating Kotlin 2.0](https://blog.jetbrains.com/kotlin/2024/05/celebrating-kotlin-2-0-fast-smart-and-multiplatform/)
- [What's new in Kotlin 1.5.0](https://kotlinlang.org/docs/whatsnew15.html)
- [Kotlin releases documentation](https://kotlinlang.org/docs/releases.html)

## 다음 단계

이 재활 훈련을 마치셨다면:

1. **Coroutines**: 비동기 프로그래밍
2. **Kotlin Multiplatform**: 멀티플랫폼 개발
3. **Compose Multiplatform**: UI 프레임워크
4. **Ktor**: 서버/클라이언트 프레임워크

---

즐거운 Kotlin 재활 훈련 되세요! 🚀
