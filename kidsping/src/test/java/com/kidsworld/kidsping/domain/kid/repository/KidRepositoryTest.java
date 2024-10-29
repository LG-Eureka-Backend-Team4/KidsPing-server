package com.kidsworld.kidsping.domain.kid.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.entity.KidMbti;
import com.kidsworld.kidsping.domain.kid.entity.enums.Gender;
import com.kidsworld.kidsping.global.common.enums.MbtiStatus;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("local")
@Transactional
@SpringBootTest
class KidRepositoryTest {

    @Autowired
    private KidMbtiRepository kidMbtiRepository;

    @Autowired
    private KidRepository kidRepository;

    @DisplayName("MBTI 성향이 있는 자녀를 조회한다.")
    @Test
    void findKidWithMbtiByKidId_whenKidHasMbti_thenReturnsKidWithMbti() {
        // given
        LocalDate nowDate = LocalDate.now();
        Kid kid = createKid(Gender.MALE, "이름1", nowDate);
        KidMbti kidMbti = createKidMbti(1, 2, 3, 4, 5, 6, 7, 8, MbtiStatus.INFJ);
        kid.updateKidMbti(kidMbti);

        // when
        kidMbtiRepository.save(kidMbti);
        kidRepository.save(kid);
        Optional<Kid> kidWithMbti = kidRepository.findKidWithMbtiByKidId(kid.getId());

        // then
        assertThat(kidWithMbti)
                .isPresent()
                .get()
                .extracting("gender", "name", "birth")
                .contains(
                        Gender.MALE, "이름1", nowDate
                );
        KidMbti findKidMbti = kidWithMbti.get().getKidMbti();
        assertThat(findKidMbti)
                .isNotNull()
                .extracting("eScore", "iScore", "sScore", "nScore", "tScore", "fScore", "pScore", "jScore",
                        "mbtiStatus")
                .contains(
                        1, 2, 3, 4, 5, 6, 7, 8, MbtiStatus.INFJ
                );
    }

    @DisplayName("MBTI 성향이 없는 자녀는 조회되지 않는다.")
    @Test
    void findKidWithMbtiByKidId_whenKidHasNoMbti_thenReturnsEmpty() {
        // given
        LocalDate nowDate = LocalDate.now();
        Kid kid = createKid(Gender.MALE, "이름1", nowDate);

        // when
        kidRepository.save(kid);
        Optional<Kid> kidWithMbti = kidRepository.findKidWithMbtiByKidId(kid.getId());

        // then
        assertThat(kidWithMbti).isEmpty();
    }

    @DisplayName("삭제된 자녀는 조회되지 않는다.")
    @Test
    void findKidWithMbtiByKidId_whenKidIsDeleted_thenReturnsEmpty() {
        // given
        LocalDate nowDate = LocalDate.now();
        Kid kid = createKid(Gender.MALE, "이름1", nowDate);
        KidMbti kidMbti = createKidMbti(1, 2, 3, 4, 5, 6, 7, 8, MbtiStatus.INFJ);
        kid.updateKidMbti(kidMbti);

        // when
        kidMbtiRepository.save(kidMbti);
        kidRepository.save(kid);
        kid.delete();
        Optional<Kid> kidWithMbti = kidRepository.findKidWithMbtiByKidId(kid.getId());

        // then
        assertThat(kidWithMbti).isEmpty();
    }

    private static Kid createKid(Gender gender, String name, LocalDate birth) {
        return Kid.builder()
                .gender(gender)
                .name(name)
                .birth(birth)
                .build();
    }

    private static KidMbti createKidMbti(int e, int i, int s, int n, int t, int f, int p, int j,
                                         MbtiStatus mbtiStatus) {
        return KidMbti
                .builder()
                .eScore(e)
                .iScore(i)
                .sScore(s)
                .nScore(n)
                .tScore(t)
                .fScore(f)
                .pScore(p)
                .jScore(j)
                .mbtiStatus(mbtiStatus)
                .build();
    }
}