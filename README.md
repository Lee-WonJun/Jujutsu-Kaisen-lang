# 주술회전 프로그래밍 언어
주술회전 프로그래밍 언어 (a.k.a 주랭) 은 인기 만화 "주술회전", 특히 "고죠 사토루" 캐릭터로 부터 영감을 받은 자연어 친화 프로그래밍 언어 입니다. 

# 개요
### 예제
#### 입력 샘플
```
2초 휴재애애앳!!!
네놈은 최강 마저 5 이란 말이냐..
네놈은 사랑 마저 최강 이란 말이냐~~
네놈은 저주하는말 마저 최강 이란 말이냐!!
네놈은 고죠사토루 마저 최강 이란 말이냐!!
하! 마지막에는 저주하는말 을 내뱉어야지
넌 고죠사토루 여서
    게속해서 가르쳐 주겠어 사랑 을! 
        네놈은 사랑 마저 사랑 이란 말이냐~
        하! 마지막에는 사랑 을 내뱉어야지
    훗 에? 훗
인건가
아니면 고죠사토루 이라서
	하! 마지막에는 저주하는말 을 내뱉어야지
인건가
이건 고죠사토루 의 승리야
이건 최강 의 승리야
작별이다 최강 내가 없는 시대에 태어났을 뿐인 범부여
```

#### 출력값
```
14
4
3
2
1
0
```
반환 코드 -3

# SPEC
## 자료형
int32 만 지원합니다.


## 변수 할당
```
네놈은 {변수A} 마저 {정수값 | 변수B} 이란 말이냐
```

변수A 를 선언하고, 정수값 혹은 변수B의 값으로 초기화 합니다.


## 연산자가 포함된 변수 할당
```
네놈은 {변수A} 마저 {정수값 | 변수B} 이란 말이냐{연속된 연산자}
```

변수A 를 선언하고, 정수값 혹은 변수B의 값에 연산을 적용하여 초기화 합니다.

### 연산자
- . : Add
- ~ : Sub
- ! : Multiply
- ? : Divide
  
*연속된 연산자의 개수를 기반으로 연산을 적용합니다.*

*먼저 나오는 연산이 우선순위가 높습니다*

예를 들어
```
네놈은 최강 마저 5 이란 말이냐!!..!
```
는

`Multiply` `Multiply` `Add` `Add` `Multiply` 순이므로

`Multiply(2)` `Add(2)` `Multiply(1)` 로 해석되어

최강이라는 변수에 `(((5 * 2) + 2) * 1)` = 12 의 값이 할당됩니다.


### 단항 연산자 (Unary negation)
```
이건 {변수A} 의 승리야
```


변수A 의 값을 (변수A * -1) 로 재할당 합니다.


## 내장 함수
### Sleep
```
2{초 | 분 | 시 | 일 | 주 } 휴재애애앳!!!
```

`2초` 혹은 `2분` 혹은 `2시간` 혹은 `2일` 혹은 `2주` 간 동작을 정지합니다.


### Console 출력
```
하! 마지막에는 {정수값 | 변수A} 을 내뱉어야지
```
정수값 또는 변수A 의 값을 Console 창에 출력합니다.


## 반환 (Return)
```
작별이다 {정수값 | 변수A}
작별이다 {정수값 | 변수A} 내가 없는 시대에 태어났을 뿐인 범부여
```

정수값 또는 변수A 의 값을 반환합니다.

`내가 없는 시대에 태어났을 뿐인 범부여` 를 뒤에 붙이면 값의 절반을 리턴합니다.


## 조건문 (if-else if)
```
넌 {정수값 | 변수A} 여서 {block} 인건가
```
정수값 또는 변수A 의 값이 0 이 아니면 참, 0이면 거짓으로 판변합니다.
참인경우 block 이 실행됩니다

```
넌 {정수값 | 변수A} 여서 {block} 인건가 아니면 {정수값 | 변수A} 이라서 {block} 인건가
```
`아니면 {정수값 | 변수A} 이라 {block} 인건가` 구문을 통하여 else 조건문 구현 할 수 있습니다.



## 반복문 (while)
```
게속해서 가르쳐 주겠어 {조건}을! {block} 훗 에? 훗
```
정수값 또는 변수A 의 값이 0 이 아니면 참, 0이면 거짓으로 판변합니다.
참인경우 block 이 실행됩니다 block 실행이후에도 참이면 다시 block 이 실행됩니다.



## 이디엄
- 확장자는 `*.jjk` 를 권장합니다.
- block 작성시 `\t`(탭) 으로 구분하는것을 권장합니다.



## 기타
- 시부야사변 애니메이션 이후로 본적이 없어, 대사와 상황이 맞는지는 잘 모릅니다.

  
## 참고자료
- [FParsec](https://www.quanttec.com/fparsec/)
- [Parsing Programming Languages with FParsec](https://rosalogia.me/posts/functional-parsing/#representing-an-ast)
- [롤챙프로그래밍 언어](https://github.com/Lee-WonJun/LolChatLang)
- 기타 주술회전 자료
