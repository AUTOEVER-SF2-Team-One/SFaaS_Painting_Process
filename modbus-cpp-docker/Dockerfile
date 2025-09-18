# ----- 1단계: 빌드(Build) 환경 -----
# 라즈베리파이(ARM64)에 맞는 Debian 기반 이미지를 사용합니다.
FROM arm64v8/debian:bullseye AS builder

# 빌드에 필요한 도구와 libmodbus 라이브러리 설치
RUN apt-get update && \
    apt-get install -y build-essential libmodbus-dev

# 소스 코드를 컨테이너 안으로 복사
WORKDIR /app
COPY src/ .

# C++ 코드 컴파일 (정적 링크 옵션을 추가하여 의존성 최소화)
# -o app : 'app'이라는 이름의 실행 파일 생성
RUN g++ -o app main.cpp -lmodbus


# ----- 2단계: 최종 실행(Final) 환경 -----
# 실행에 필요한 최소한의 이미지를 사용합니다.
FROM arm64v8/debian:bullseye-slim

# libmodbus 실행에 필요한 런타임 라이브러리 설치
RUN apt-get update && \
    apt-get install -y libmodbus5 && \
    rm -rf /var/lib/apt/lists/*

# 빌드 단계(builder)에서 생성된 컴파일된 실행 파일만 복사
WORKDIR /app
COPY --from=builder /app/app .

# 컨테이너가 시작될 때 이 실행 파일을 실행
CMD ["./app"]