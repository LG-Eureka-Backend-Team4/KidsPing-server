import http from 'k6/http';
import {check, sleep} from 'k6';
import {Rate} from 'k6/metrics';

// 실패율을 추적하는 메트릭 설정
export let errorRate = new Rate('errors');

export let options = {
    stages: [
        {duration: '10s', target: 20000},
        {duration: '10s', target: 10000},
        {duration: '10s', target: 10000},
        {duration: '10s', target: 5000},
        {duration: '10s', target: 500},
        {duration: '10s', target: 0},
    ],
    thresholds: {
        errors: ['rate<0.1'], // 실패율이 10% 미만이어야 함
        http_req_duration: ['p(95)<2000'], // 95% 요청이 2초 이내에 완료
    },
};

// API 베이스 URL
const BASE_URL = 'http://localhost:8080/api';

export default function () {
    // 1. getAllEvents 호출
    let getAllEventsRes = http.get(`${BASE_URL}/events?page=1&size=10`);
    check(getAllEventsRes, {
        'getAllEvents 성공': (res) => res.status === 200,
    });

    errorRate.add(getAllEventsRes.status !== 200);

    // 결과에서 이벤트 ID를 추출 (예: 첫 번째 이벤트 ID 사용)
    let eventId;
    if (getAllEventsRes.json().data && getAllEventsRes.json().data.content.length > 0) {
        eventId = getAllEventsRes.json().data.content[0].id;
    }

    // 2. getEvent 호출
    if (eventId) {
        let getEventRes = http.get(`${BASE_URL}/events/${eventId}`);
        check(getEventRes, {
            'getEvent 성공': (res) => res.status === 200,
        });
    }

    // 3. applyCoupon 호출
    let applyCouponPayload = JSON.stringify({
        userId: __VU,
        eventId: eventId || 1, // eventId가 없으면 기본값으로 1 사용
        name: 'Test User',
        phone: '123-456-7890'
    });
    let headers = {'Content-Type': 'application/json'};
    let applyCouponRes = http.post(`${BASE_URL}/coupons/apply`, applyCouponPayload, {headers: headers});
    check(applyCouponRes, {
        'applyCoupon 성공': (res) => res.status === 200,
    });
    errorRate.add(applyCouponRes.status !== 200);

    sleep(1); // 각 호출 간에 1초 대기
}
