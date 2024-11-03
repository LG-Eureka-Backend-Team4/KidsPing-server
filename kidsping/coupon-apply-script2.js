import http from 'k6/http';
import {check, sleep} from 'k6';

export const options = {
    stages: [
        {duration: '10s', target: 100},
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'], // 95% 요청이 500ms 이내에 응답
        http_req_failed: ['rate<0.01'],   // 에러율 1% 미만
    },
};

// 무작위 userId와 phone 번호 생성
function generateRandomUserId() {
    return Math.floor(Math.random() * 1000);  // 1부터 999,999 사이의 랜덤 ID 생성
}

function generatePhoneNumber() {
    return '010-' + String(Math.floor(1000 + Math.random() * 9000)) + '-' + String(Math.floor(1000 + Math.random() * 9000));
}

export default function () {
    // 요청 본문 데이터 생성
    const userId = generateRandomUserId();
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

    // 응답 확인
    check(response, {
        'is OK': (r) => r.status === 200,
        'response contains success message': (r) => {
            const isJson = r.headers['Content-Type'] && r.headers['Content-Type'].includes('application/json');
            if (isJson) {
                const jsonResponse = r.json();
                return jsonResponse.data.applyResponseMessage === '이벤트에 참여하셨습니다.' || jsonResponse.data.applyResponseMessage === '이미 이벤트에 참여했습니다.';
            }
            return false;
        },
    });

    sleep(1);  // 1초 휴식
}