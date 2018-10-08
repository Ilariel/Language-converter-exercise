package fi.ilariel.languageconverter.morse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fi.ilariel.languageconverter.converter.ConversionException;
import fi.ilariel.languageconverter.converter.LanguageConverter;


/**
 * Language converter for Morse−to−English and English−to−Morse translations. Characters encoded in Morse code are
 * delimited with '.'
 * Only a subset of English characters is supported. See public MORSE_* members for supported characters
 */
public class MorseConverter extends LanguageConverter {

  public static final String MORSE_A = "•−";
  public static final String MORSE_B = "−•••";
  public static final String MORSE_C = "−•−•";
  public static final String MORSE_D = "−••";
  public static final String MORSE_E = "•";
  public static final String MORSE_F = "••−•";
  public static final String MORSE_G = "−−•";
  public static final String MORSE_H = "••••";
  public static final String MORSE_I = "••";
  public static final String MORSE_J = "•−−−";
  public static final String MORSE_K = "−•−";
  public static final String MORSE_L = "•−••";
  public static final String MORSE_M = "−−";
  public static final String MORSE_N = "−•";
  public static final String MORSE_O = "−−−";
  public static final String MORSE_P = "•−−•";
  public static final String MORSE_Q = "−−•−";
  public static final String MORSE_R = "•−•";
  public static final String MORSE_S = "•••";
  public static final String MORSE_T = "−";
  public static final String MORSE_U = "••−";
  public static final String MORSE_V = "•••−";
  public static final String MORSE_W = "•−−";
  public static final String MORSE_X = "−••−";
  public static final String MORSE_Y = "−•−−";
  public static final String MORSE_Z = "−−••";
  public static final String MORSE_DOT = "•−•−•−";
  public static final String MORSE_COMMA = "−−••−−";
  public static final String MORSE_QUESTION_MARK = "••−−••";
  public static final String MORSE_SLASH = "−••−•";
  public static final String MORSE_AT_SIGN = "•−−•−•";
  public static final String MORSE_1 = "•−−−−";
  public static final String MORSE_2 = "••−−−";
  public static final String MORSE_3 = "•••−−−";
  public static final String MORSE_4 = "••••−";
  public static final String MORSE_5 = "•••••";
  public static final String MORSE_6 = "−••••";
  public static final String MORSE_7 = "−−•••";
  public static final String MORSE_8 = "−−−••";
  public static final String MORSE_9 = "−−−−•";
  public static final String MORSE_0 = "−−−−−";
  static final String MORSE_DELIMIT_EXCEPTION_MESSAGE = "Morse characters should be delimited with only one dot";
  private static final String MORSE_CODE_WORD_SEPARATOR = " "; //Space
  private static final char MORSE_CODE_CHARACTER_SEPARATOR = '.';
  private final Mode mode;

  /**
   *
   * @param inputStream,
   * @param writer
   * @param mode {@link Mode}
   */
  public MorseConverter(BufferedReader inputStream, Writer writer, Mode mode) {
    super(inputStream, writer);
    this.mode = mode;
  }

  private static char morseToLetter(String morse)
      throws ConversionException {
    char morseString;
    switch (morse) {
      case MORSE_A:
        morseString = 'A';
        break;
      case MORSE_B:
        morseString = 'B';
        break;
      case MORSE_C:
        morseString = 'C';
        break;
      case MORSE_D:
        morseString = 'D';
        break;
      case MORSE_E:
        morseString = 'E';
        break;
      case MORSE_F:
        morseString = 'F';
        break;
      case MORSE_G:
        morseString = 'G';
        break;
      case MORSE_H:
        morseString = 'H';
        break;
      case MORSE_I:
        morseString = 'I';
        break;
      case MORSE_J:
        morseString = 'J';
        break;
      case MORSE_K:
        morseString = 'K';
        break;
      case MORSE_L:
        morseString = 'L';
        break;
      case MORSE_M:
        morseString = 'M';
        break;
      case MORSE_N:
        morseString = 'N';
        break;
      case MORSE_O:
        morseString = 'O';
        break;
      case MORSE_P:
        morseString = 'P';
        break;
      case MORSE_Q:
        morseString = 'Q';
        break;
      case MORSE_R:
        morseString = 'R';
        break;
      case MORSE_S:
        morseString = 'S';
        break;
      case MORSE_T:
        morseString = 'T';
        break;
      case MORSE_U:
        morseString = 'U';
        break;
      case MORSE_V:
        morseString = 'V';
        break;
      case MORSE_W:
        morseString = 'W';
        break;
      case MORSE_X:
        morseString = 'X';
        break;
      case MORSE_Y:
        morseString = 'Y';
        break;
      case MORSE_Z:
        morseString = 'Z';
        break;
      case MORSE_DOT:
        morseString = '.';
        break;
      case MORSE_COMMA:
        morseString = ',';
        break;
      case MORSE_QUESTION_MARK:
        morseString = '?';
        break;
      case MORSE_SLASH:
        morseString = '/';
        break;
      case MORSE_AT_SIGN:
        morseString = '@';
        break;
      case MORSE_1:
        morseString = '1';
        break;
      case MORSE_2:
        morseString = '2';
        break;
      case MORSE_3:
        morseString = '3';
        break;
      case MORSE_4:
        morseString = '4';
        break;
      case MORSE_5:
        morseString = '5';
        break;
      case MORSE_6:
        morseString = '6';
        break;
      case MORSE_7:
        morseString = '7';
        break;
      case MORSE_8:
        morseString = '8';
        break;
      case MORSE_9:
        morseString = '9';
        break;
      case MORSE_0:
        morseString = '0';
        break;
      case MORSE_CODE_WORD_SEPARATOR:
        morseString = ' ';
        break;
      default:
        throw new ConversionException(morse + " is not a valid Morse code sequence");
    }

    return morseString;
  }

  private static String letterToMorse(char ch)
      throws ConversionException {
    String morseString;

    switch (Character.toUpperCase(ch)) {
      case 'A':
        morseString = MORSE_A;
        break;
      case 'B':
        morseString = MORSE_B;
        break;
      case 'C':
        morseString = MORSE_C;
        break;
      case 'D':
        morseString = MORSE_D;
        break;
      case 'E':
        morseString = MORSE_E;
        break;
      case 'F':
        morseString = MORSE_F;
        break;
      case 'G':
        morseString = MORSE_G;
        break;
      case 'H':
        morseString = MORSE_H;
        break;
      case 'I':
        morseString = MORSE_I;
        break;
      case 'J':
        morseString = MORSE_J;
        break;
      case 'K':
        morseString = MORSE_K;
        break;
      case 'L':
        morseString = MORSE_L;
        break;
      case 'M':
        morseString = MORSE_M;
        break;
      case 'N':
        morseString = MORSE_N;
        break;
      case 'O':
        morseString = MORSE_O;
        break;
      case 'P':
        morseString = MORSE_P;
        break;
      case 'Q':
        morseString = MORSE_Q;
        break;
      case 'R':
        morseString = MORSE_R;
        break;
      case 'S':
        morseString = MORSE_S;
        break;
      case 'T':
        morseString = MORSE_T;
        break;
      case 'U':
        morseString = MORSE_U;
        break;
      case 'V':
        morseString = MORSE_V;
        break;
      case 'W':
        morseString = MORSE_W;
        break;
      case 'X':
        morseString = MORSE_X;
        break;
      case 'Y':
        morseString = MORSE_Y;
        break;
      case 'Z':
        morseString = MORSE_Z;
        break;
      case '.':
        morseString = MORSE_DOT;
        break;
      case ',':
        morseString = MORSE_COMMA;
        break;
      case '?':
        morseString = MORSE_QUESTION_MARK;
        break;
      case '/':
        morseString = MORSE_SLASH;
        break;
      case '@':
        morseString = MORSE_AT_SIGN;
        break;
      case '1':
        morseString = MORSE_1;
        break;
      case '2':
        morseString = MORSE_2;
        break;
      case '3':
        morseString = MORSE_3;
        break;
      case '4':
        morseString = MORSE_4;
        break;
      case '5':
        morseString = MORSE_5;
        break;
      case '6':
        morseString = MORSE_6;
        break;
      case '7':
        morseString = MORSE_7;
        break;
      case '8':
        morseString = MORSE_8;
        break;
      case '9':
        morseString = MORSE_9;
        break;
      case '0':
        morseString = MORSE_0;
        break;
      case ' ':
        morseString = MORSE_CODE_WORD_SEPARATOR;
        break;
      default:
        throw new ConversionException("Conversion for " + ch + "is not supported");
    }

    return morseString;
  }

  /**
   * Converts based on mode specified in constructor.
   * @throws ConversionException, if invalid morse code sequence or unsupported text input
   */
  @Override
  public void convert()
      throws ConversionException {

    if (mode == Mode.TEXT_TO_MORSE) {
      englishToMorse();
    } else {
      morseToEnglish();
    }
  }

  /**
   * Converts English characters to Morse code where characters are separated with '.'
   * @throws ConversionException
   */
  private void englishToMorse()
      throws ConversionException {
    BufferedReader reader = (BufferedReader) this.reader;

    try {
      String line = null;
      line = reader.readLine();
      StringBuilder builder = new StringBuilder();
      boolean first = true;
      while (line != null) {
        char[] letters = line.toCharArray();
        for (char ch : letters) {
          if (!first) {
            writer.write(MORSE_CODE_CHARACTER_SEPARATOR);
            writer.write(letterToMorse(ch));
          } else {
            writer.write(letterToMorse(ch));
            first = false;
          }
        }

        line = reader.readLine();
      }

      writer.write(builder.toString());
    } catch (IOException e) {
      throw new ConversionException(e);
    }
  }

  private void morseToEnglish()
      throws ConversionException {
    BufferedReader reader = (BufferedReader) this.reader;

    try {
      String line = null;
      line = reader.readLine();
      StringBuilder buffer = new StringBuilder();
      while (line != null) {
        buffer.append(line);
        line = reader.readLine();
      }
      String text = buffer.toString();
      buffer.setLength(0);

      //Use regular expression for finding separators
      //Capture space, sequence of dits or/and dahs to until next delimiter dot or dot
      //Group 1 is space or sequence of dits and dahs
      //Group 2 is dot '.'
      Matcher matcher = Pattern.compile("( |[•−]+)|(\\.)").matcher(text);
      boolean delimiter = false;
      while (matcher.find()) {
        String match = matcher.group(1);
        if (match != null) {
          delimiter = false;
          buffer.append(morseToLetter(match));
        } else {
          //Accept only single dot delimitation
          if (delimiter) {
            throw new ConversionException(MORSE_DELIMIT_EXCEPTION_MESSAGE);
          }
          delimiter = true;
        }
      }

      writer.write(buffer.toString());
    } catch (IOException e) {
      throw new ConversionException(e);
    }
  }

  /**
   * Mode selector for MorseConverter
   */
  public enum Mode {
    MORSE_TO_TEXT, TEXT_TO_MORSE
  }
}
