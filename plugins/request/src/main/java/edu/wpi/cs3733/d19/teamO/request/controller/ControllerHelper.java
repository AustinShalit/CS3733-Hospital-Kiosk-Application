package edu.wpi.cs3733.d19.teamO.request.controller;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import com.jfoenix.controls.JFXComboBox;

@SuppressWarnings("PMD.StaticModifier")
public class ControllerHelper {

  public static void populateMonthCombobox(JFXComboBox<Integer> comboBox) {
    comboBox.getItems().setAll(months);
  }

  public static void populateYearCombobox(JFXComboBox<Integer> comboBox) {
    comboBox.getItems().setAll(years);
  }

  public static void populateStatesCombobox(JFXComboBox<String> comboBox) {
    comboBox.getItems().setAll(states);
  }

  private static final Set<Integer> months = new LinkedHashSet<>(Arrays.asList(1, 2, 2,
      3, 4, 5, 6, 8, 9, 10, 11, 12));

  private static final Set<String> states = new LinkedHashSet<>(Arrays.asList("AL", "AK",
      "AZ", "AR", "CA", "CO", "CT", "DE", "DC", "FL", "GA", "HI", "ID", "IL", "IN", "IA",
      "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH",
      "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX",
      "UT", "VT", "VA", "WA", "WV", "WI", "WY"));

  private static final Set<Integer> years = new LinkedHashSet<>(Arrays.asList(2019, 2020, 2021,
      2022, 2023, 2024, 2025, 2026, 2027, 2028, 2029, 2030, 2031, 2032, 2033, 2034, 2035));


}
