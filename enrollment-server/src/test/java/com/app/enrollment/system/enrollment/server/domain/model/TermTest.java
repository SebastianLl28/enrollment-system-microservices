package com.app.enrollment.system.enrollment.server.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.app.enrollment.system.enrollment.server.testsupport.Mothers;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class TermTest {

  private static final LocalDate JAN = LocalDate.of(2026, 1, 1);
  private static final LocalDate MAR = LocalDate.of(2026, 3, 1);
  private static final LocalDate JUL = LocalDate.of(2026, 7, 31);
  private static final LocalDate DEC = LocalDate.of(2026, 12, 31);

  @Test
  void createBuildsActiveTerm() {
    Term term = Term.create("2026-I", JAN, JUL, Mothers.NOW);

    assertThat(term.getCode()).isEqualTo("2026-I");
    assertThat(term.getStartDate()).isEqualTo(JAN);
    assertThat(term.getEndDate()).isEqualTo(JUL);
    assertThat(term.isActive()).isTrue();
    assertThat(term.getId()).isNull();
    assertThat(term.getCreateAt()).isEqualTo(Mothers.NOW);
  }

  @Test
  void overlapsWhenRangesIntersect() {
    Term term = Term.create("2026-I", JAN, JUL, Mothers.NOW);

    assertThat(term.overlaps(MAR, DEC)).isTrue();
  }

  @Test
  void overlapsWhenCompletelyContained() {
    Term term = Term.create("2026-I", JAN, DEC, Mothers.NOW);

    assertThat(term.overlaps(MAR, JUL)).isTrue();
  }

  @Test
  void overlapsWhenSameRange() {
    Term term = Term.create("2026-I", JAN, JUL, Mothers.NOW);

    assertThat(term.overlaps(JAN, JUL)).isTrue();
  }

  @Test
  void doesNotOverlapWhenAdjacentAfter() {
    // term ends JUL 31; new starts AUG 1 — no overlap
    Term term = Term.create("2026-I", JAN, JUL, Mothers.NOW);
    LocalDate aug1 = LocalDate.of(2026, 8, 1);

    assertThat(term.overlaps(aug1, DEC)).isFalse();
  }

  @Test
  void doesNotOverlapWhenAdjacentBefore() {
    // term starts MAR 1; new ends FEB 28 — no overlap
    Term term = Term.create("2026-I", MAR, DEC, Mothers.NOW);
    LocalDate feb28 = LocalDate.of(2026, 2, 28);

    assertThat(term.overlaps(JAN, feb28)).isFalse();
  }

  @Test
  void doesNotOverlapWhenCompletelyBefore() {
    Term term = Term.create("2026-II", JUL, DEC, Mothers.NOW);

    assertThat(term.overlaps(JAN, MAR)).isFalse();
  }

  @Test
  void doesNotOverlapWhenNullDates() {
    Term term = Term.create("2026-I", null, null, Mothers.NOW);

    assertThat(term.overlaps(JAN, JUL)).isFalse();
  }
}
