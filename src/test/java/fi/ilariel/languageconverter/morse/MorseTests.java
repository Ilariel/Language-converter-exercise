package fi.ilariel.languageconverter.morse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fi.ilariel.languageconverter.converter.ConversionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;


public class MorseTests {

  private static final String SOS_ENGLISH_INPUT = "sos";
  private static final String SOS_ENGLISH_OUTPUT = "SOS";
  private static final String SOS_MORSE = "•••.−−−.•••";


  private StringWriter stringWriter;

  private static void doTest(String expected, String input, Writer writer, MorseConverter.Mode mode) {
    BufferedReader reader = new BufferedReader(new StringReader(input));
    doConvert(reader,writer,mode);
    assertEquals(expected, writer.toString());
  }

  private static void doConvert(BufferedReader reader, Writer writer, MorseConverter.Mode mode) {
    try (MorseConverter converter = new MorseConverter(reader, writer, mode)) {
      converter.convert();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ConversionException e) {
      e.printStackTrace();
    }
  }

  @Before
  public void setup() {
    stringWriter = new StringWriter();
  }

  @Test
  public void englishStringToMorse() {
    doTest(SOS_MORSE, SOS_ENGLISH_INPUT, stringWriter, MorseConverter.Mode.TEXT_TO_MORSE);
  }

  @Test
  public void specialCharacters() {
    String special = "me@mail.com";
    String expected = "−−.•.•−−•−•.−−.•−.••.•−••.•−•−•−.−•−•.−−−.−−";
    doTest(expected, special, stringWriter, MorseConverter.Mode.TEXT_TO_MORSE);
  }

  @Test
  public void singleLetterTest() {
    String letter ="E";
    String expected = "•";
    doTest(expected, letter, stringWriter, MorseConverter.Mode.TEXT_TO_MORSE);
    stringWriter = new StringWriter();
    doTest(letter.toUpperCase(), expected, stringWriter, MorseConverter.Mode.MORSE_TO_TEXT);
  }

  @Test
  public void uppercased() {
    String input = "sos";
    String roundTripExpected = "SOS";
    StringWriter outAgain = new StringWriter();

    BufferedReader reader = new BufferedReader(new StringReader(input));
    doConvert(reader,stringWriter,MorseConverter.Mode.TEXT_TO_MORSE);
    reader = new BufferedReader(new StringReader(stringWriter.toString()));
    doConvert(reader,outAgain,MorseConverter.Mode.MORSE_TO_TEXT);

    assertEquals(roundTripExpected,outAgain.toString());
  }

  @Test
  public void withSpaces() {
    String input = "hello world";
    String roundTripExpected = "HELLO WORLD";
    StringWriter outAgain = new StringWriter();

    BufferedReader reader = new BufferedReader(new StringReader(input));
    doConvert(reader,stringWriter,MorseConverter.Mode.TEXT_TO_MORSE);
    reader = new BufferedReader(new StringReader(stringWriter.toString()));
    doConvert(reader,outAgain,MorseConverter.Mode.MORSE_TO_TEXT);

    assertEquals(roundTripExpected,outAgain.toString());
  }

  @Test
  public void failWithMultipleDelimiters() {
    String invalidInput = "•••..−−−..•••"; //SOS with multiple delimiters
    BufferedReader reader = new BufferedReader(new StringReader(invalidInput));

    try (MorseConverter converter = new MorseConverter(reader, stringWriter, MorseConverter.Mode.MORSE_TO_TEXT)) {
      converter.convert();
      fail("Expected a ConversionException from invalid input");
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ConversionException e) {
      assertEquals(e.getMessage(),MorseConverter.MORSE_DELIMIT_EXCEPTION_MESSAGE);
    }
  }

  @Test
  public void morseStringToEnglish() {
    doTest(SOS_ENGLISH_OUTPUT, SOS_MORSE, stringWriter, MorseConverter.Mode.MORSE_TO_TEXT);
  }
}
