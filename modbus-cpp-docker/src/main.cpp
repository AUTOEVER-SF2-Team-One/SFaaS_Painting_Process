#include <iostream>
#include <unistd.h>
#include <cerrno>
#include <cmath> // sin 함수를 사용하기 위해 추가
#include <chrono> // 시간을 얻기 위해 추가
#include <modbus/modbus.h>

// 💡 센서 값을 시뮬레이션하는 함수
float read_temperature() {
    // 현재 시간을 초 단위로 가져옵니다.
    auto now = std::chrono::system_clock::now();
    auto seconds = std::chrono::duration_cast<std::chrono::seconds>(now.time_since_epoch()).count();

    // sin 함수를 이용해 24.0°C를 기준으로 ±2.0°C 범위에서 값이 부드럽게 변하도록 시뮬레이션합니다.
    // 60초 주기로 값이 변합니다.
    float value = 24.0f + 2.0f * sin(seconds * 2.0 * M_PI / 60.0);
    return value;
}

// 💡 센서 값을 시뮬레이션하는 함수
float read_humidity() {
    // 현재 시간을 초 단위로 가져옵니다.
    auto now = std::chrono::system_clock::now();
    auto seconds = std::chrono::duration_cast<std::chrono::seconds>(now.time_since_epoch()).count();

    // sin 함수를 이용해 55.0%를 기준으로 ±5.0% 범위에서 값이 부드럽게 변하도록 시뮬레이션합니다.
    // 90초 주기로 값이 변합니다.
    float value = 55.0f + 5.0f * sin(seconds * 2.0 * M_PI / 90.0);
    return value;
}

int main() {
    modbus_t *ctx;
    modbus_mapping_t *mb_mapping;
    int server_socket;

    // 1. Modbus TCP 컨텍스트 생성 (Port: 502)
    ctx = modbus_new_tcp(NULL, 502);
    if (ctx == NULL) {
        std::cerr << "modbus_new_tcp 실패: " << modbus_strerror(errno) << std::endl;
        return -1;
    }

    // 2. Modbus 메모리 맵(레지스터) 할당
    mb_mapping = modbus_mapping_new(0, 0, 10, 10);
    if (mb_mapping == NULL) {
        std::cerr << "modbus_mapping_new 실패: " << modbus_strerror(errno) << std::endl;
        modbus_free(ctx);
        return -1;
    }

    // 3. 서버 리슨 및 연결 대기
    server_socket = modbus_tcp_listen(ctx, 1);
    if (server_socket == -1) {
        std::cerr << "modbus_tcp_listen 실패: " << modbus_strerror(errno) << std::endl;
        modbus_free(ctx);
        return -1;
    }
    
    std::cout << "Modbus TCP 서버가 502 포트에서 실행 중입니다. (시뮬레이션 모드)" << std::endl;

    while (true) {
        if (modbus_tcp_accept(ctx, &server_socket) == -1) {
            std::cerr << "modbus_tcp_accept 실패: " << modbus_strerror(errno) << std::endl;
            break;
        }

        std::cout << "클라이언트 연결됨." << std::endl;
        
        while (true) {
            uint8_t query[MODBUS_TCP_MAX_ADU_LENGTH];
            int rc;

            rc = modbus_receive(ctx, query);
            if (rc > 0) {
                float temp = read_temperature();
                float humi = read_humidity();

                mb_mapping->tab_input_registers[0] = static_cast<uint16_t>(temp * 10);
                mb_mapping->tab_input_registers[1] = static_cast<uint16_t>(humi * 10);
                
                // 소수점 한 자리까지 출력
                printf("가상 데이터 업데이트: Temp=%.1f, Humi=%.1f\n", temp, humi);

                modbus_reply(ctx, query, rc, mb_mapping);
            } else if (rc == -1) {
                std::cerr << "클라이언트 연결 끊김: " << modbus_strerror(errno) << std::endl;
                break;
            }
        }
    }

    close(server_socket);
    modbus_mapping_free(mb_mapping);
    modbus_free(ctx);

    return 0;
}