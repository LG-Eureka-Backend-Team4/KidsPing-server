package com.kidsworld.kidsping.domain.like.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.kidsworld.kidsping.domain.book.entity.Book;
import com.kidsworld.kidsping.domain.book.entity.BookMbti;
import com.kidsworld.kidsping.domain.book.entity.enums.MbtiType;
import com.kidsworld.kidsping.domain.book.repository.BookMbtiRepository;
import com.kidsworld.kidsping.domain.book.repository.BookRepository;
import com.kidsworld.kidsping.domain.genre.entity.Genre;
import com.kidsworld.kidsping.domain.genre.repository.GenreRepository;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.entity.enums.Gender;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.domain.like.entity.LikeMbti;
import com.kidsworld.kidsping.domain.like.entity.enums.LikeStatus;
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
class LikeMbtiRepositoryTest {

    @Autowired
    private LikeMbtiRepository likeMbtiRepository;

    @Autowired
    private KidRepository kidRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMbtiRepository bookMbtiRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    @DisplayName("자녀 ID와 도서 ID로 LikeMbti를 조회한다.")
    void findLikeMbtiByKidIdAndBookId() {
        // given
        Kid kid = createKid(Gender.MALE, "이름1", LocalDate.now());
        Genre genre = new Genre("제목");
        BookMbti bookMbti = createBookMbti(MbtiType.ENFJ, 1, 2, 3, 4, 5, 6, 7, 8);
        Book book = createBook(bookMbti, genre, "제목", "요약", "작가", "출판사", 5, "http://url");
        LikeMbti likeMbti = createLikeMbti(book, kid, LikeStatus.CANCEL);

        // when
        kidRepository.save(kid);
        genreRepository.save(genre);
        bookMbtiRepository.save(bookMbti);
        bookRepository.save(book);
        likeMbtiRepository.save(likeMbti);
        Optional<LikeMbti> findLikeMbti = likeMbtiRepository.findByKidIdAndBookId(kid.getId(), book.getId());

        // then
        assertThat(findLikeMbti)
                .isPresent()
                .get()
                .extracting("likeStatus", "previousLikeStatus")
                .contains(
                        LikeStatus.CANCEL
                );
    }

    @Test
    @DisplayName("자녀 엔티티와 도서 엔티티로 LikeMbti를 조회한다.")
    void findLikeMbtiByKidAndBook() {
        // given
        Kid kid = createKid(Gender.MALE, "이름1", LocalDate.now());
        Genre genre = new Genre("제목");
        BookMbti bookMbti = createBookMbti(MbtiType.ENFJ, 1, 2, 3, 4, 5, 6, 7, 8);
        Book book = createBook(bookMbti, genre, "제목", "요약", "작가", "출판사", 5, "http://url");
        LikeMbti likeMbti = createLikeMbti(book, kid, LikeStatus.LIKE);

        // when
        kidRepository.save(kid);
        genreRepository.save(genre);
        bookMbtiRepository.save(bookMbti);
        bookRepository.save(book);
        likeMbtiRepository.save(likeMbti);
        Optional<LikeMbti> findLikeMbti = likeMbtiRepository.findLikeMbtiByKidAndBook(kid, book);

        // then
        assertThat(findLikeMbti)
                .isPresent()
                .get()
                .extracting("likeStatus", "previousLikeStatus")
                .contains(
                        LikeStatus.LIKE
                );
    }

    @Test
    @DisplayName("자녀가 좋아요한 도서 목록을 조회한다.")
    void findLikedBooksByKidId() {
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
        Page<LikeMbti> booksWithLike = likeMbtiRepository.findBooksByLikeMbtiId(kid.getId(), pageable,
                LikeStatus.LIKE);
        Book findBook1 = booksWithLike.getContent().get(0).getBook();
        Book findBook2 = booksWithLike.getContent().get(1).getBook();

        // then
        assertThat(booksWithLike.getContent())
                .hasSize(2)
                .extracting("likeStatus")
                .containsExactlyInAnyOrder(
                        LikeStatus.LIKE,
                        LikeStatus.LIKE
                );
        assertThat(findBook1)
                .extracting("title", "summary", "author", "publisher", "age", "imageUrl")
                .contains(
                        "제목", "요약", "작가", "출판사", 5, "http://url"
                );
        assertThat(findBook2)
                .extracting("title", "summary", "author", "publisher", "age", "imageUrl")
                .contains(
                        "제목2", "요약2", "작가2", "출판사2", 3, "http://url2"
                );
    }

    @Test
    @DisplayName("자녀 ID로 모든 LikeMbti 엔티티를 삭제한다.")
    void deleteLikeMbtisByKidId() {
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

        // when
        kidRepository.save(kid);
        genreRepository.save(genre);
        bookMbtiRepository.saveAll(List.of(bookMbti1, bookMbti2, bookMbti3));
        bookRepository.saveAll(List.of(book1, book2, book3));
        likeMbtiRepository.saveAll(List.of(likeMbti1, likeMbti2, likeMbti3));

        // 삭제 전 확인
        List<LikeMbti> likeMbtisBeforeDelete = likeMbtiRepository.findAll();
        assertThat(likeMbtisBeforeDelete).hasSize(3);

        // when
        likeMbtiRepository.deleteLikeMbtisByKidId(kid.getId());

        // then
        List<LikeMbti> likeMbtisAfterDelete = likeMbtiRepository.findAll();
        assertThat(likeMbtisAfterDelete).isEmpty();
    }

    private static LikeMbti createLikeMbti(Book book, Kid kid, LikeStatus likeStatus) {
        return LikeMbti.builder()
                .previousLikeStatus(LikeStatus.LIKE)
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

    private static Kid createKid(Gender gender, String name, LocalDate birth) {
        return Kid.builder()
                .gender(gender)
                .name(name)
                .birth(birth)
                .build();
    }
}