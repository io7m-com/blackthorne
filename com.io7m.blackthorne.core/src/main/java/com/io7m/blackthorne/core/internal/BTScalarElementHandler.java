/*
 * Copyright © 2019 Mark Raynsford <code@io7m.com> https://www.io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */


package com.io7m.blackthorne.core.internal;

import com.io7m.blackthorne.core.BTCharacterHandlerType;
import com.io7m.blackthorne.core.BTElementHandlerType;
import com.io7m.blackthorne.core.BTElementParsingContextType;
import com.io7m.blackthorne.core.BTQualifiedName;
import org.xml.sax.Attributes;

import java.util.Objects;

/**
 * A convenient handler for converting the text content of elements into scalar values.
 *
 * @param <S> The type of returned values
 */

public final class BTScalarElementHandler<S> implements BTElementHandlerType<Object, S>
{
  private final BTCharacterHandlerType<S> handler;
  private final BTQualifiedName name;
  private final StringBuilder textBuffer;

  /**
   * Construct a handler.
   *
   * @param inName    The name of elements handled by this handler
   * @param inHandler The character handler
   */

  public BTScalarElementHandler(
    final BTQualifiedName inName,
    final BTCharacterHandlerType<S> inHandler)
  {
    this.name =
      Objects.requireNonNull(inName, "name");
    this.handler =
      Objects.requireNonNull(inHandler, "handler");
    this.textBuffer =
      new StringBuilder();
  }

  @Override
  public String name()
  {
    return this.name.localName();
  }

  @Override
  public void onCharacters(
    final BTElementParsingContextType context,
    final char[] data,
    final int offset,
    final int length)
  {
    this.textBuffer.append(data, offset, length);
  }

  @Override
  public void onElementStart(
    final BTElementParsingContextType context,
    final Attributes attributes)
  {
    this.textBuffer.setLength(0);
  }

  @Override
  public S onElementFinished(
    final BTElementParsingContextType context)
    throws Exception
  {
    final var textAll =
      this.textBuffer.toString();
    final var textCharacters =
      textAll.toCharArray();
    final var parsed =
      this.handler.parse(context, textCharacters, 0, textCharacters.length);

    return Objects.requireNonNull(parsed, "parsed");
  }
}
