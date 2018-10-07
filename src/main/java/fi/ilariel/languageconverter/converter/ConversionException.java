package fi.ilariel.languageconverter.converter;

import java.io.IOException;


public class ConversionException extends Exception {

  public ConversionException(String message) {
    super(message);
  }

  public ConversionException(Throwable e) {
    super(e);
  }
}
