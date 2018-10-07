package fi.ilariel.languageconverter.converter;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;



public abstract class LanguageConverter implements Closeable, AutoCloseable {

  protected final Reader reader;
  protected final Writer writer;

  public LanguageConverter(Reader reader, Writer writer)  {
    this.reader = reader;
    this.writer = writer;
  }

  @Override
  public void close()
      throws IOException {
    reader.close();
    writer.close();
  }

  public abstract void convert() throws ConversionException;
}
