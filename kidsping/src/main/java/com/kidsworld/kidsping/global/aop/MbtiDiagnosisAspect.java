package com.kidsworld.kidsping.global.aop;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.GlobalException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class MbtiDiagnosisAspect {

    private final KidRepository kidRepository;
    private final HttpServletRequest request;

    @Before("execution(public * com.kidsworld.kidsping.domain.book.controller.BookController.*(..))")
    public void validateMbtiDiagnosis(JoinPoint joinPoint) {
        String uri = request.getRequestURI();

        if (isBasicQueryPattern(uri) || isAdminApiPattern(uri)) {
            return;
        }

        Long kidId = extractKidIdFromPath();
        if (kidId == null) {
            throw new GlobalException(ExceptionCode.INVALID_REQUEST);
        }

        Optional<Kid> kidOptional = kidRepository.findById(kidId);

        if (kidOptional.isEmpty()) {
            throw new GlobalException(ExceptionCode.NOT_FOUND_KID);
        }

        Kid kid = kidOptional.get();
        if (kid.getKidMbti() == null) {
            throw new GlobalException(ExceptionCode.MBTI_DIAGNOSIS_REQUIRED,
                    "서비스를 이용하기 위해서 자녀 성향 진단이 필요합니다.");
        }
    }

    private boolean isBasicQueryPattern(String uri) {
        return (uri.matches("/api/books/[0-9]+$") &&
                request.getMethod().equals(HttpMethod.GET.name())) ||
                (uri.equals("/api/books") &&
                        request.getMethod().equals(HttpMethod.GET.name()));
    }

    private boolean isAdminApiPattern(String uri) {
        return (uri.matches("/api/books/[0-9]+$") &&
                (request.getMethod().equals(HttpMethod.PUT.name()) ||
                        request.getMethod().equals(HttpMethod.DELETE.name()))) ||
                (uri.equals("/api/books") &&
                        request.getMethod().equals(HttpMethod.POST.name()));
    }

    private Long extractKidIdFromPath() {
        String uri = request.getRequestURI();
        if (uri.contains("/kid/")) {
            String[] parts = uri.split("/");
            for (int i = 0; i < parts.length; i++) {
                if (parts[i].equals("kid") && i + 1 < parts.length) {
                    try {
                        return Long.parseLong(parts[i + 1]);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
}
