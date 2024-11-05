import http from 'k6/http';
import {check, sleep} from 'k6';

export const options = {
    stages: [
        {duration: '10s', target: 30000},
        {duration: '10s', target: 30000},
        {duration: '10s', target: 20000},
        {duration: '10s', target: 10000},
        {duration: '10s', target: 5000},
        {duration: '10s', target: 5000},
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'], // 95% 요청이 500ms 이내에 응답
        http_req_failed: ['rate<0.01'],   // 에러율 1% 미만
    },
};

// `uuidv4()` 함수로 무작위 phone 번호 생성
function generatePhoneNumber() {
    return '010-' + String(Math.floor(1000 + Math.random() * 9000)) + '-' + String(Math.floor(1000 + Math.random() * 9000));
}

export default function () {
    // 요청 본문 데이터
    const userId = __VU;  // `__VU`: 현재 가상 사용자 ID를 사용하여 고유 ID로 설정
    const payload = JSON.stringify({
        userId: userId,
        eventId: 1,  // 테스트할 이벤트 ID
        name: '테스트유저' + userId,
        phone: generatePhoneNumber(),
    });

    // POST 요청 헤더
    const headers = {
        'Content-Type': 'application/json',
    };

    // POST 요청 전송
    const response = http.post('http://localhost:8080/api/coupons/apply', payload, {headers});

    check(response, {
        'is OK': (r) => r.status === 200,
        'response contains success message': (r) => typeof r.body === 'string',
    });

    sleep(1); // 1초 휴식
}