package com.ab.worldcup.bet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.ab.worldcup.match.Stage;
import com.ab.worldcup.web.model.UserBetData;
import com.ab.worldcup.web.validators.UserBetDataValidator;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

@ExtendWith(MockitoExtension.class)
class UserBetDataValidatorTest {

  @Mock private BetService betService;

  @InjectMocks private UserBetDataValidator validator;

  private Bet createBet(Long id, LocalDateTime lockTime) throws Exception {
    Bet bet = new Bet();
    setField(bet, "id", id);
    setField(bet, "matchId", 1L);
    setField(bet, "type", BetType.MATCH);
    setField(bet, "stageId", Stage.GROUP);
    setField(bet, "lockTime", Timestamp.valueOf(lockTime));
    return bet;
  }

  private void setField(Object obj, String fieldName, Object value) throws Exception {
    Field field = obj.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(obj, value);
  }

  @Test
  @DisplayName("supports UserBetData class")
  void supportsUserBetData() {
    assertThat(validator.supports(UserBetData.class)).isTrue();
  }

  @Test
  @DisplayName("bet before lock time with valid data → no errors")
  void beforeLockTime_noErrors() throws Exception {
    Bet bet = createBet(1L, LocalDateTime.now().plusHours(2));
    when(betService.getBetById(1L)).thenReturn(bet);

    UserBetData data = new UserBetData();
    data.setBetId(1L);
    data.setHomeTeamGoals(2);
    data.setAwayTeamGoals(1);

    Errors errors = new BeanPropertyBindingResult(data, "userBetData");
    validator.validate(data, errors);

    assertThat(errors.hasErrors()).isFalse();
  }

  @Test
  @DisplayName("bet after lock time → rejected with 'already started' error")
  void afterLockTime_rejected() throws Exception {
    Bet bet = createBet(1L, LocalDateTime.now().minusHours(1));
    when(betService.getBetById(1L)).thenReturn(bet);

    UserBetData data = new UserBetData();
    data.setBetId(1L);
    data.setHomeTeamGoals(2);
    data.setAwayTeamGoals(1);

    Errors errors = new BeanPropertyBindingResult(data, "userBetData");
    validator.validate(data, errors);

    assertThat(errors.hasErrors()).isTrue();
    assertThat(errors.getFieldError("betId").getDefaultMessage())
        .isEqualTo("Cannot bet on match that already started");
  }

  @Test
  @DisplayName("bet with null goals → validation errors on goals fields")
  void nullGoals_validationErrors() throws Exception {
    Bet bet = createBet(1L, LocalDateTime.now().plusHours(2));
    when(betService.getBetById(1L)).thenReturn(bet);

    UserBetData data = new UserBetData();
    data.setBetId(1L);
    data.setHomeTeamGoals(null);
    data.setAwayTeamGoals(null);

    Errors errors = new BeanPropertyBindingResult(data, "userBetData");
    validator.validate(data, errors);

    assertThat(errors.hasErrors()).isTrue();
    assertThat(errors.getFieldError("homeTeamGoals")).isNotNull();
    assertThat(errors.getFieldError("awayTeamGoals")).isNotNull();
  }

  @Test
  @DisplayName("bet with negative goals → validation errors")
  void negativeGoals_validationErrors() throws Exception {
    Bet bet = createBet(1L, LocalDateTime.now().plusHours(2));
    when(betService.getBetById(1L)).thenReturn(bet);

    UserBetData data = new UserBetData();
    data.setBetId(1L);
    data.setHomeTeamGoals(-1);
    data.setAwayTeamGoals(2);

    Errors errors = new BeanPropertyBindingResult(data, "userBetData");
    validator.validate(data, errors);

    assertThat(errors.hasErrors()).isTrue();
    assertThat(errors.getFieldError("homeTeamGoals")).isNotNull();
  }
}
