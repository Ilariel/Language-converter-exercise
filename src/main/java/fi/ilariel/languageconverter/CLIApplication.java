package fi.ilariel.languageconverter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import fi.ilariel.languageconverter.converter.ConversionException;
import fi.ilariel.languageconverter.converter.LanguageConverter;
import fi.ilariel.languageconverter.morse.MorseConverter;


/**
 * Simple CLI application
 */
public class CLIApplication {

  private static final int MODE_ARG = 0;
  private static final int INPUT_FILE_ARG = 1;
  private static final int OUTPUT_FILE_ARG = 2;
  private static final char TO_MORSE = 'm';
  private static final char TO_ENGLISH = 'e';

  static boolean isValidInputFile(File inputFile, StringBuilder errorMessage) {
    boolean isValid = false;

    if (!inputFile.exists()) {
      errorMessage.append(String.format("Input file \"%s\" doesn't exist\n", inputFile.getPath()));
    } else {
      if (!inputFile.isFile()) {
        errorMessage.append(String.format("Input file \"%s\" is not a file\n", inputFile));
      } else {
        isValid = true;
      }
    }

    return isValid;
  }

  static boolean isValidOuputFile(File outputFile, StringBuilder errorMessage) {
    boolean isValid = false;

    if (outputFile.exists() && !outputFile.isFile()) {
      errorMessage.append(String.format("Output file \"%s\" exists and is not a file\n", outputFile.getPath()));
    } else {
      isValid = true;
    }

    return isValid;
  }

  public static void main(String[] args) {

    StringBuilder errorMessage = new StringBuilder();
    MorseConverter.Mode mode = null;

    //Print instructions when no arguments
    if (args.length == 0) {
      printInstructions();
      return;
    }

    //We expect 3 arguments in specified order. "Mode, input, output"
    if (args.length == 3) {
      //Check if we have defined a mode for translation
      String arg = args[MODE_ARG];
      if (arg.startsWith("-")) {
        try {
          char commandPrefix = arg.charAt(1);
          switch (commandPrefix) {
            case TO_MORSE:
              if (mode == null) {
                mode = MorseConverter.Mode.TEXT_TO_MORSE;
              }
              break;
            case TO_ENGLISH:
              if (mode == null) {
                mode = MorseConverter.Mode.MORSE_TO_TEXT;
              }
              break;
            default:
              throw new IllegalArgumentException(commandPrefix + " is not a valid mode\n");
          }
        }
        catch (IllegalArgumentException e) {
          errorMessage.append(e.getMessage());
        }
      }

      if (mode == null) {
        errorMessage.append("Conversion mode not specified. Refer to instructions for available modes");
      }

      //If we have had an error we want to exit and print the error message(s)
      if (errorMessage.length() > 0) {
        System.err.print(errorMessage.toString());
        printInstructions();
        return;
      }

      File inputFile = new File(args[INPUT_FILE_ARG]);
      File outputFile = new File(args[OUTPUT_FILE_ARG]);

      if (isValidInputFile(inputFile, errorMessage) && isValidOuputFile(outputFile, errorMessage)) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        BufferedReader inputReader = null;
        BufferedWriter outputWriter = null;
        try {
          inputStream = new FileInputStream(inputFile);
          inputReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

          outputStream = new FileOutputStream(outputFile);
          outputWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
          //This shouldn't happen to be honest
        }
        if (inputReader != null && outputWriter != null) {
          try (LanguageConverter converter = new MorseConverter(inputReader, outputWriter, mode)) {
            converter.convert();
          } catch (IOException e) {
            e.printStackTrace();
          } catch (ConversionException e) {
            errorMessage.append(e.getMessage());
          }
        }
      }

      if (errorMessage.length() > 0) {
        System.out.print(errorMessage);
      }
    } else {
      printInstructions();
    }
  }

  private static void printInstructions() {
    //Print instructions
    System.out.print("Usage: morsetool\n" + "First specify mode and then enter input file path and output file path\n"
        + "-m, English to Morse code\n" + "-e, Morse to English\n");
  }
}

