package edu.wpi.cs3733.d19.teamO.request.pizzapi;

import java.util.Date;
import java.util.regex.Pattern;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * A PaymentObject represents a credit card.
 *
 * <p>There's some sweet logic in here to make sure that the type of card
 * you passed is valid.
 * </p>
 */
public class Payment {

  public enum CardType {
    VISA("^4[0-9]{12}(?:[0-9]{3})?$"),
    MASTERCARD("^5[1-5][0-9]{14}$"),
    AMEX("^3[47][0-9]{13}$"),
    DISCOVER("^6(?:011|5[0-9]{2})[0-9]{12}$");

    private final Pattern numberPattern;

    CardType(final String numberPattern) {
      this.numberPattern = Pattern.compile(numberPattern);
    }

    /**
     * Get the card type entered.
     */
    public static CardType getCardType(final String number) {
      for (CardType type : values()) {
        if (type.numberPattern.matcher(number).matches()) {
          return type;
        }
      }
      return null;
    }
  }

  private static final Pattern cvvPattern = Pattern.compile("^[0-9]{3,4}$");
  private static final Pattern zipPattern = Pattern.compile("^[0-9]{5}(?:-[0-9]{4})?$");

  private final StringProperty name = new SimpleStringProperty();
  private final StringProperty number = new SimpleStringProperty();
  private final ObjectProperty<Date> expiration = new SimpleObjectProperty<>();
  private final StringProperty cvv = new SimpleStringProperty();
  private final StringProperty zip = new SimpleStringProperty();

  private final ReadOnlyObjectWrapper<CardType> type = new ReadOnlyObjectWrapper<>();
  private final ReadOnlyBooleanWrapper valid = new ReadOnlyBooleanWrapper();

  /**
   * Create a Payment object.
   */
  public Payment() {
    type.bind(Bindings.createObjectBinding(() -> CardType.getCardType(number.get()), number));

    valid.bind(Bindings.createBooleanBinding(() -> number.get() != null
            && type.get() != null
            && expiration.get() != null
            && cvvPattern.matcher(cvv.get()).matches()
            && zipPattern.matcher(zip.get()).matches(),
        number, type, expiration, cvv, zip));
  }

  public String getName() {
    return name.get();
  }

  public StringProperty nameProperty() {
    return name;
  }

  public void setName(String name) {
    this.name.set(name);
  }

  public String getNumber() {
    return number.get();
  }

  public StringProperty numberProperty() {
    return number;
  }

  public void setNumber(String number) {
    this.number.set(number);
  }

  public CardType getType() {
    return type.get();
  }

  public ReadOnlyObjectProperty<CardType> typeProperty() {
    return type.getReadOnlyProperty();
  }

  public Date getExpiration() {
    return expiration.get();
  }

  public ObjectProperty<Date> expirationProperty() {
    return expiration;
  }

  public void setExpiration(Date expiration) {
    this.expiration.set(expiration);
  }

  public String getCvv() {
    return cvv.get();
  }

  public StringProperty cvvProperty() {
    return cvv;
  }

  public void setCvv(String cvv) {
    this.cvv.set(cvv);
  }

  public String getZip() {
    return zip.get();
  }

  public StringProperty zipProperty() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip.set(zip);
  }

  public boolean isValid() {
    return valid.get();
  }

  public ReadOnlyBooleanProperty validProperty() {
    return valid.getReadOnlyProperty();
  }
}
