package com.kidsworld.kidsping.domain.like.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.kidsworld.kidsping.domain.book.entity.Book;
import com.kidsworld.kidsping.domain.book.entity.BookMbti;
import com.kidsworld.kidsping.domain.book.entity.enums.MbtiType;
import com.kidsworld.kidsping.domain.book.repository.BookMbtiRepository;
import com.kidsworld.kidsping.domain.book.repository.BookRepository;
import com.kidsworld.kidsping.domain.genre.entity.Genre;
import com.kidsworld.kidsping.domain.genre.repository.GenreRepository;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.entity.KidMbti;
import com.kidsworld.kidsping.domain.kid.entity.enums.Gender;
import com.kidsworld.kidsping.domain.kid.repository.KidMbtiRepository;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.domain.like.dto.request.DislikeCancelMbtiRequest;
import com.kidsworld.kidsping.domain.like.dto.request.DislikeMbtiRequest;
import com.kidsworld.kidsping.domain.like.dto.request.LikeCancelMbtiRequest;
import com.kidsworld.kidsping.domain.like.dto.request.LikeMbtiRequest;
import com.kidsworld.kidsping.domain.like.dto.response.LikeBookResponse;
import com.kidsworld.kidsping.domain.like.entity.LikeMbti;
import com.kidsworld.kidsping.domain.like.entity.enums.LikeStatus;
import com.kidsworld.kidsping.domain.like.repository.LikeMbtiRepository;
import com.kidsworld.kidsping.domain.like.service.LikeMbtiService;
import com.kidsworld.kidsping.global.common.enums.MbtiStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("local")
@Transactional
@SpringBootTest
class LikeMbtiServiceImplTest {

    @Autowired
    private LikeMbtiRepository likeMbtiRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private KidRepository kidRepository;

    @Autowired
    private BookMbtiRepository bookMbtiRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private LikeMbtiService likeMbtiService;
    @Autowired
    private KidMbtiRepository kidMbtiRepository;

    @DisplayName("자녀가 좋아요한 도서 목록을 조회한다.")
    @Test
    void getLikedBooksByKidId() {
        // given
        Kid kid = createKid(Gender.MALE, "이름1", LocalDate.now());
        Genre genre = new Genre("제목");
        BookMbti bookMbti1 = createBookMbti(MbtiType.ENFJ, 1, 2, 3, 4, 5, 6, 7, 8);
        BookMbti bookMbti2 = createBookMbti(MbtiType.INFJ, 11, 2, 13, 4, 15, 6, 17, 8);
        BookMbti bookMbti3 = createBookMbti(MbtiType.ESFJ, 16, 25, 34, 43, 52, 26, 27, 8);
        Book book1 = createBook(bookMbti1, genre, "제목", "요약", "작가", "출판사", 5, "http://url");
        Book book2 = createBook(bookMbti2, genre, "제목2", "요약2", "작가2", "출판사2", 3, "http://url2");
        Book book3 = createBook(bookMbti3, genre, "제목3", "요약3", "작가3", "출판사3", 8, "http://url3");
        LikeMbti likeMbti1 = createLikeMbti(book1, kid, LikeStatus.LIKE);
        LikeMbti likeMbti2 = createLikeMbti(book2, kid, LikeStatus.LIKE);
        LikeMbti likeMbti3 = createLikeMbti(book3, kid, LikeStatus.DISLIKE);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        kidRepository.save(kid);
        genreRepository.save(genre);
        bookMbtiRepository.saveAll(List.of(bookMbti1, bookMbti2, bookMbti3));
        bookRepository.saveAll(List.of(book1, book2, book3));
        likeMbtiRepository.saveAll(List.of(likeMbti1, likeMbti2, likeMbti3));
        Page<LikeBookResponse> booksLiked = likeMbtiService.getBooksLiked(kid.getId(), pageable);

        // then
        assertThat(booksLiked.getContent())
                .hasSize(2)
                .extracting("title", "summary", "author", "publisher", "age", "imageUrl", "mbtiType")
                .containsExactlyInAnyOrder(
                        tuple("제목", "요약", "작가", "출판사", 5, "http://url", MbtiType.ENFJ),
                        tuple("제목2", "요약2", "작가2", "출판사2", 3, "http://url2", MbtiType.INFJ)
                );
    }

    @DisplayName("자녀가 도서에 좋아요를 눌러 자녀의 성향이 바뀐다.")
    @Test
    void like_shouldChangeKidMbtiWhenPreferenceChanges() {
        // given
        Kid kid = saveKidWithMbti(Gender.MALE, "이름1", LocalDate.now(), 1, 2, 3, 4, 5, 6, 7, 8, MbtiStatus.INFJ);
        Book book = saveBookWithMbtiAndGenre(MbtiType.ENFJ, 2, -2, 3, -3, -1, 1, -5, 5, "제목", "요약", "작가", "출판사", 5,
                "http://url");

        // when
        LikeMbtiRequest likeMbtiRequest = new LikeMbtiRequest(kid.getId(), book.getId());
        likeMbtiService.like(likeMbtiRequest);

        Optional<KidMbti> findKidMbti = kidMbtiRepository.findById(kid.getKidMbti().getId());
        assertThat(findKidMbti)
                .isPresent()
                .get()
                .extracting("eScore", "iScore", "sScore", "nScore", "tScore", "fScore", "pScore", "jScore",
                        "mbtiStatus")
                .containsExactly(
                        3, 0, 6, 1, 4, 7, 2, 13, MbtiStatus.ESFJ
                );
    }

    @DisplayName("자녀가 도서에 좋아요를 눌러도 자녀의 성향이 바뀌지 않는다.")
    @Test
    void like_shouldNotChangeKidMbtiWhenPreferenceDoesNotChange() {
        // given
        Kid kid = saveKidWithMbti(Gender.MALE, "이름1", LocalDate.now(), 1, 2, 3, 4, 5, 6, 7, 8, MbtiStatus.INFJ);
        Book book = saveBookWithMbtiAndGenre(MbtiType.ENFJ, -2, 4, -3, 3, -1, 1, -5, 5, "제목", "요약", "작가", "출판사", 5,
                "http://url");

        // when
        LikeMbtiRequest likeMbtiRequest = new LikeMbtiRequest(kid.getId(), book.getId());
        likeMbtiService.like(likeMbtiRequest);

        Optional<KidMbti> findKidMbti = kidMbtiRepository.findById(kid.getKidMbti().getId());
        assertThat(findKidMbti)
                .isPresent()
                .get()
                .extracting("eScore", "iScore", "sScore", "nScore", "tScore", "fScore", "pScore", "jScore",
                        "mbtiStatus")
                .containsExactly(
                        -1, 6, 0, 7, 4, 7, 2, 13, MbtiStatus.INFJ
                );
    }

    @DisplayName("자녀가 도서에 좋아요 취소를 누르면 이전 성향으로 돌아온다.")
    @Test
    void likeCancel_shouldRevertKidMbtiToPreviousStatus() {
        // given
        Kid kid = saveKidWithMbti(Gender.MALE, "이름1", LocalDate.now(), 1, 2, 3, 4, 5, 6, 7, 8, MbtiStatus.INFJ);
        Book book = saveBookWithMbtiAndGenre(MbtiType.ESTP, 3, -2, 6, 1, 6, 5, 12, 3, "제목", "요약", "작가", "출판사", 5,
                "http://url");

        // when
        LikeMbtiRequest likeMbtiRequest = new LikeMbtiRequest(kid.getId(), book.getId());
        LikeCancelMbtiRequest likeCancelMbtiRequest = new LikeCancelMbtiRequest(kid.getId(), book.getId());
        likeMbtiService.like(likeMbtiRequest);
        likeMbtiService.likeCancel(likeCancelMbtiRequest);
        Optional<KidMbti> findKidMbti = kidMbtiRepository.findById(kid.getKidMbti().getId());

        // then
        assertThat(findKidMbti)
                .isPresent()
                .get()
                .extracting("eScore", "iScore", "sScore", "nScore", "tScore", "fScore", "pScore", "jScore",
                        "mbtiStatus")
                .containsExactly(
                        1, 2, 3, 4, 5, 6, 7, 8, MbtiStatus.INFJ
                );
    }

    @DisplayName("자녀가 도서에 싫어요를 눌러 자녀의 성향이 바뀐다.")
    @Test
    void dislike_shouldChangeKidMbtiWhenPreferenceChanges() {
        // given
        Kid kid = saveKidWithMbti(Gender.MALE, "이름1", LocalDate.now(), 1, 2, 3, 4, 5, 6, 7, 8, MbtiStatus.INFJ);
        Book book = saveBookWithMbtiAndGenre(MbtiType.INFJ, -2, 4, -3, 3, -1, 1, -5, 5, "제목", "요약", "작가", "출판사", 5,
                "http://url");

        // when
        DislikeMbtiRequest dislikeMbtiRequest = new DislikeMbtiRequest(kid.getId(), book.getId());
        likeMbtiService.dislike(dislikeMbtiRequest);
        Optional<KidMbti> findKidMbti = kidMbtiRepository.findById(kid.getKidMbti().getId());

        // then
        assertThat(findKidMbti)
                .isPresent()
                .get()
                .extracting("eScore", "iScore", "sScore", "nScore", "tScore", "fScore", "pScore", "jScore",
                        "mbtiStatus")
                .containsExactly(
                        3, -2, 6, 1, 6, 5, 12, 3, MbtiStatus.ESTP
                );
    }

    @DisplayName("자녀가 도서에 싫어요를 눌러도 자녀의 성향이 바뀌지 않는다.")
    @Test
    void dislike_shouldNotChangeKidMbtiWhenPreferenceDoesNotChange() {
        // given
        Kid kid = saveKidWithMbti(Gender.MALE, "이름1", LocalDate.now(), 1, 2, 3, 4, 5, 6, 7, 8, MbtiStatus.INFJ);
        Book book = saveBookWithMbtiAndGenre(MbtiType.ESTP, 3, -2, 6, 1, 6, 5, 12, 3, "제목", "요약", "작가", "출판사", 5,
                "http://url");

        // when
        DislikeMbtiRequest dislikeMbtiRequest = new DislikeMbtiRequest(kid.getId(), book.getId());
        likeMbtiService.dislike(dislikeMbtiRequest);
        Optional<KidMbti> findKidMbti = kidMbtiRepository.findById(kid.getKidMbti().getId());

        // then
        assertThat(findKidMbti)
                .isPresent()
                .get()
                .extracting("eScore", "iScore", "sScore", "nScore", "tScore", "fScore", "pScore", "jScore",
                        "mbtiStatus")
                .containsExactly(
                        -2, 4, -3, 3, -1, 1, -5, 5, MbtiStatus.INFJ
                );
    }

    @DisplayName("자녀가 도서에 싫어요 취소를 누르면 이전 성향으로 돌아온다.")
    @Test
    void dislikeCancel_shouldRevertKidMbtiToPreviousStatus() {
        // given
        Kid kid = saveKidWithMbti(Gender.MALE, "이름1", LocalDate.now(), 5, 2, 9, 4, 5, 2, 3, 1, MbtiStatus.ESTP);
        Book book = saveBookWithMbtiAndGenre(MbtiType.ESTP, 5, -5, 4, -4, 5, -5, 2, -2, "제목", "요약", "작가", "출판사", 5,
                "http://url");

        // when
        DislikeMbtiRequest dislikeMbtiRequest = new DislikeMbtiRequest(kid.getId(), book.getId());
        DislikeCancelMbtiRequest dislikeCancelMbtiRequest = new DislikeCancelMbtiRequest(kid.getId(), book.getId());
        likeMbtiService.dislike(dislikeMbtiRequest);
        likeMbtiService.dislikeCancel(dislikeCancelMbtiRequest);
        Optional<KidMbti> findKidMbti = kidMbtiRepository.findById(kid.getKidMbti().getId());

        // then
        assertThat(findKidMbti)
                .isPresent()
                .get()
                .extracting("eScore", "iScore", "sScore", "nScore", "tScore", "fScore", "pScore", "jScore",
                        "mbtiStatus")
                .containsExactly(
                        5, 2, 9, 4, 5, 2, 3, 1, MbtiStatus.ESTP
                );
    }

    @DisplayName("경계값 테스트 - 좋아요에서 싫어요로 전환 시 가중치가 -2로 적용된다.")
    @Test
    void likeToDislike_boundary_shouldApplyMultiplierOfMinusTwo() {
        // given
        Kid kid = saveKidWithMbti(Gender.MALE, "이름1", LocalDate.now(), 1, 1, 1, 1, 1, 1, 1, 1, MbtiStatus.INFJ);
        Book book = saveBookWithMbtiAndGenre(MbtiType.ENFJ, 2, -2, 3, -3, 1, -1, 2, -2, "제목1", "요약", "작가", "출판사", 5,
                "http://url");

        // when
        LikeMbtiRequest likeRequest = new LikeMbtiRequest(kid.getId(), book.getId());
        likeMbtiService.like(likeRequest);
        DislikeMbtiRequest dislikeRequest = new DislikeMbtiRequest(kid.getId(), book.getId());
        likeMbtiService.dislike(dislikeRequest);

        // then
        Optional<KidMbti> findKidMbti = kidMbtiRepository.findById(kid.getKidMbti().getId());
        assertThat(findKidMbti)
                .isPresent()
                .get()
                .extracting("eScore", "iScore", "sScore", "nScore", "tScore", "fScore", "pScore", "jScore",
                        "mbtiStatus")
                .containsExactly(
                        -1, 3, -2, 4, 0, 2, -1, 3, MbtiStatus.INFJ
                );
    }

    @DisplayName("경계값 테스트 - 싫어요에서 좋아요로 전환 시 가중치가 2로 적용된다.")
    @Test
    void dislikeToLike_boundary_shouldApplyMultiplierOfTwo() {
        // given
        Kid kid = saveKidWithMbti(Gender.MALE, "이름2", LocalDate.now(), 1, 1, 1, 1, 1, 1, 1, 1, MbtiStatus.ESFP);
        Book book = saveBookWithMbtiAndGenre(MbtiType.INFJ, 2, -2, 3, -3, 1, -1, 2, -2, "제목2", "요약", "작가", "출판사", 5,
                "http://url");

        // when
        DislikeMbtiRequest dislikeRequest = new DislikeMbtiRequest(kid.getId(), book.getId());
        likeMbtiService.dislike(dislikeRequest);
        LikeMbtiRequest likeRequest = new LikeMbtiRequest(kid.getId(), book.getId());
        likeMbtiService.like(likeRequest);

        // then
        Optional<KidMbti> findKidMbti = kidMbtiRepository.findById(kid.getKidMbti().getId());
        assertThat(findKidMbti)
                .isPresent()
                .get()
                .extracting("eScore", "iScore", "sScore", "nScore", "tScore", "fScore", "pScore", "jScore",
                        "mbtiStatus")
                .containsExactly(
                        3, -1, 4, -2, 2, 0, 3, -1, MbtiStatus.ESTP
                );
    }

    @DisplayName("경계값 테스트 - 좋아요 취소 시 가중치가 -1로 적용된다.")
    @Test
    void likeCancel_boundary_shouldApplyMultiplierOfMinusOne() {
        // given
        Kid kid = saveKidWithMbti(Gender.MALE, "이름3", LocalDate.now(), 3, 3, 3, 3, 3, 3, 3, 3, MbtiStatus.ESFP);
        Book book = saveBookWithMbtiAndGenre(MbtiType.ESTP, 1, -1, 1, -1, 1, -1, 1, -1, "제목3", "요약", "작가", "출판사", 5,
                "http://url");

        // when
        LikeMbtiRequest likeRequest = new LikeMbtiRequest(kid.getId(), book.getId());
        likeMbtiService.like(likeRequest);
        LikeCancelMbtiRequest likeCancelRequest = new LikeCancelMbtiRequest(kid.getId(), book.getId());
        likeMbtiService.likeCancel(likeCancelRequest);

        // then
        Optional<KidMbti> findKidMbti = kidMbtiRepository.findById(kid.getKidMbti().getId());
        assertThat(findKidMbti)
                .isPresent()
                .get()
                .extracting("eScore", "iScore", "sScore", "nScore", "tScore", "fScore", "pScore", "jScore",
                        "mbtiStatus")
                .containsExactly(
                        3, 3, 3, 3, 3, 3, 3, 3, MbtiStatus.ESFP // 원래 상태로 복원
                );
    }

    @DisplayName("경계값 테스트 - 싫어요 취소 시 가중치가 1로 적용된다.")
    @Test
    void dislikeCancel_boundary_shouldApplyMultiplierOfOne() {
        // given
        Kid kid = saveKidWithMbti(Gender.MALE, "이름4", LocalDate.now(), 4, 4, 4, 4, 4, 4, 4, 4, MbtiStatus.ESFP);
        Book book = saveBookWithMbtiAndGenre(MbtiType.INFJ, -1, 1, -1, 1, -1, 1, -1, 1, "제목4", "요약", "작가", "출판사", 5,
                "http://url");

        // when
        DislikeMbtiRequest dislikeRequest = new DislikeMbtiRequest(kid.getId(), book.getId());
        likeMbtiService.dislike(dislikeRequest);
        DislikeCancelMbtiRequest dislikeCancelRequest = new DislikeCancelMbtiRequest(kid.getId(), book.getId());
        likeMbtiService.dislikeCancel(dislikeCancelRequest);

        // then
        Optional<KidMbti> findKidMbti = kidMbtiRepository.findById(kid.getKidMbti().getId());
        assertThat(findKidMbti)
                .isPresent()
                .get()
                .extracting("eScore", "iScore", "sScore", "nScore", "tScore", "fScore", "pScore", "jScore",
                        "mbtiStatus")
                .containsExactly(
                        4, 4, 4, 4, 4, 4, 4, 4, MbtiStatus.ESFP // 원래 상태로 복원
                );
    }

    private Kid saveKidWithMbti(Gender gender, String name, LocalDate birth, int e, int i, int s, int n, int t, int f,
                                int p, int j, MbtiStatus mbtiStatus) {
        Kid kid = createKid(gender, name, birth);
        KidMbti kidMbti = createKidMbti(e, i, s, n, t, f, p, j, mbtiStatus);
        kid.updateKidMbti(kidMbti);

        kidRepository.save(kid);
        kidMbtiRepository.save(kidMbti);
        return kid;
    }

    private Book saveBookWithMbtiAndGenre(MbtiType mbtiType, int e, int i, int s, int n, int t, int f, int p, int j,
                                          String title, String summary, String author, String publisher, int age,
                                          String imageUrl) {
        Genre genre = new Genre("제목");
        BookMbti bookMbti = createBookMbti(mbtiType, e, i, s, n, t, f, p, j);
        Book book = createBook(bookMbti, genre, title, summary, author, publisher, age, imageUrl);

        genreRepository.save(genre);
        bookMbtiRepository.save(bookMbti);
        bookRepository.save(book);
        return book;
    }

    private static LikeMbti createLikeMbti(Book book, Kid kid, LikeStatus likeStatus) {
        return LikeMbti.builder()
                .book(book)
                .kid(kid)
                .likeStatus(likeStatus)
                .build();
    }

    private static Book createBook(BookMbti bookMbti, Genre genre, String title, String summary, String author,
                                   String publisher, Integer age, String imageUrl) {
        return Book.builder()
                .bookMbti(bookMbti)
                .genre(genre)
                .title(title)
                .summary(summary)
                .author(author)
                .publisher(publisher)
                .age(age)
                .imageUrl(imageUrl)
                .isDeleted(false)
                .build();
    }

    private static BookMbti createBookMbti(MbtiType mbtiType, int e, int i, int s, int n, int t, int f, int p, int j) {
        return BookMbti.builder()
                .bookMbtiType(mbtiType)
                .eScore(e)
                .iScore(i)
                .sScore(s)
                .nScore(n)
                .tScore(t)
                .fScore(f)
                .pScore(p)
                .jScore(j)
                .isDeleted(false)
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

    private static Kid createKid(Gender gender, String name, LocalDate birth) {
        return Kid.builder()
                .gender(gender)
                .name(name)
                .birth(birth)
                .build();
    }
}