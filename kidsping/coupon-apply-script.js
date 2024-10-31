import http from 'k6/http';
import {check, sleep} from 'k6';

// 초당 1000명의 user가 30초간 요청을 보내는 상황을 가정
export const options = {
    stages: [
        {duration: '10s', target: 30000},   // 처음 10초 동안 초당 약 3000명
        {duration: '10s', target: 30000},   // 초당 약 3000명
        {duration: '10s', target: 20000},   // 초당 약 2000명
        {duration: '10s', target: 10000},   // 초당 약 1000명
        {duration: '10s', target: 5000},    // 초당 약 500명
        {duration: '10s', target: 5000},    // 초당 약 500명
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
    const response = http.post('http://localhost:8080/api/coupons/coupon', payload, {headers});

    check(response, {
        'is OK': (r) => r.status === 200,
        'response contains success message': (r) => {
            // JSON 파싱 전 Content-Type 확인
            const isJson = r.headers['Content-Type'] && r.headers['Content-Type'].includes('application/json');
            if (isJson) {
                const jsonResponse = r.json();
                // data 내의 responseMessage 필드가 "이벤트에 참여하셨습니다."인지 확인
                return jsonResponse.data && jsonResponse.data.responseMessage === '이벤트에 참여하셨습니다.';
            }
            return false;
        },
    });

    sleep(1);
}