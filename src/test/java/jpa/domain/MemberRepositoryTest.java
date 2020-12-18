package jpa.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberRepositoryTest {
	
	@Autowired
	private MemberRepository memberRepository;

	@Test
	void saveTest() {
		Member expected = new Member("email");
		Member actual = memberRepository.save(expected);
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
		);
	}

	@Test
	void updateTest() {
		String originEmail = "email";
		String changeEmail = "email2222";
		Member expected = new Member("email");
		Member actual = memberRepository.save(expected);
		actual.changeEmail(changeEmail);
		assertAll(
			() -> assertThat(memberRepository.findByEmail(originEmail)).isNull(),
			() -> assertThat(memberRepository.findByEmail(changeEmail).getEmail()).isEqualTo(changeEmail)
		);
	}

	@Test
	void deleteTest() {
		String originEmail = "email";
		Member expected = new Member(originEmail);
		Member actual = memberRepository.save(expected);
		assertThat(actual.getEmail()).isEqualTo(expected.getEmail());

		memberRepository.delete(actual);
		assertAll(
			() -> assertThat(memberRepository.findByEmail(originEmail)).isNull(),
			() -> assertThat(memberRepository.findAll().size()).isEqualTo(0)
		);
	}

	@Test
	void findByEmailTest() {
		String expected = "email";
		memberRepository.save(new Member(expected));
		String actual = memberRepository.findByEmail(expected).getEmail();
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void findByAgeBetweenTest() {
		Member expected = memberRepository.save(new Member(31, "email", "password"));

		Member actualBetweenFromTo = memberRepository.findByAgeBetween(10, 50);
		Member actualBetweenToFrom = memberRepository.findByAgeBetween(50, 10);

		assertThat(actualBetweenFromTo).isEqualTo(expected);
		assertThat(actualBetweenToFrom).isNull();
	}

}