/*
 * Copyright © 2024 Mark Raynsford <code@io7m.com> https://www.io7m.com
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


package com.io7m.blackthorne.tests;

import com.io7m.blackthorne.core.BTElementHandlerConstructorType;
import com.io7m.blackthorne.core.BTElementHandlerType;
import com.io7m.blackthorne.core.BTElementParsingContextType;
import com.io7m.blackthorne.core.BTIgnoreUnrecognizedElements;
import com.io7m.blackthorne.core.BTPreserveLexical;
import com.io7m.blackthorne.core.BTQualifiedName;
import com.io7m.blackthorne.core.Blackthorne;
import com.io7m.jxe.core.JXEHardenedSAXParsers;
import com.io7m.jxe.core.JXEXInclude;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class Bug43Test
{
  private JXEHardenedSAXParsers saxParsers;

  private static URL resourceURL(
    final String name)
  {
    return BlackthorneTest.class.getResource("/com/io7m/blackthorne/tests/" + name);
  }

  @BeforeEach
  public void setup()
  {
    this.saxParsers = new JXEHardenedSAXParsers();
  }

  @Test
  public void testParseError()
    throws Exception
  {
    final Map<BTQualifiedName, BTElementHandlerConstructorType<?, OError>> rootElements =
      Map.of(OXError.elementName(), OXError::new);

    final var error =
      Blackthorne.parse(
        URI.create("urn:unavailable"),
        resourceURL("bug-43.xml").openStream(),
        BTPreserveLexical.PRESERVE_LEXICAL_INFORMATION,
        () -> {
          return this.saxParsers.createXMLReaderNonValidating(
            Optional.empty(),
            JXEXInclude.XINCLUDE_DISABLED
          );
        },
        rootElements
      );

    assertEquals("17EC79A1EFDF31E7", error.requestId);
    assertEquals("/", error.resource);
    assertEquals("AuthorizationQueryParametersError", error.code);
    assertEquals(
      "Error parsing the X-Amz-Credential parameter; incorrect date format. This date in the credential must be in the format \"yyyyMMdd\".",
      error.message);
  }

  public record OError(
    String code,
    String message,
    String resource,
    String requestId)
  {
    public OError
    {
      Objects.requireNonNull(code, "code");
      Objects.requireNonNull(message, "message");
      Objects.requireNonNull(resource, "resource");
      Objects.requireNonNull(requestId, "requestId");
    }
  }

  public static BTQualifiedName qName(
    final String name)
  {
    return new BTQualifiedName(
      URI.create(""),
      name
    );
  }

  public final class OXError
    implements BTElementHandlerType<OXError.ErrorFieldType, OError>
  {
    private static final BTQualifiedName ELEMENT_NAME =
      qName("Error");
    private static final BTQualifiedName CODE =
      qName("Code");
    private static final BTQualifiedName MESSAGE =
      qName("Message");
    private static final BTQualifiedName RESOURCE =
      qName("Resource");
    private static final BTQualifiedName REQUEST_ID =
      qName("RequestId");

    private String code = "";
    private String message = "";
    private String resource = "";
    private String requestId = "";

    @Override
    public BTIgnoreUnrecognizedElements onShouldIgnoreUnrecognizedElements(
      final BTElementParsingContextType context)
    {
      return BTIgnoreUnrecognizedElements.IGNORE_UNRECOGNIZED_ELEMENTS;
    }

    /**
     * An element handler.
     *
     * @param context The parse context
     */

    public OXError(
      final BTElementParsingContextType context)
    {

    }

    /**
     * @return The element name
     */

    public static BTQualifiedName elementName()
    {
      return ELEMENT_NAME;
    }

    @Override
    public Map<BTQualifiedName, BTElementHandlerConstructorType<?, ? extends ErrorFieldType>>
    onChildHandlersRequested(
      final BTElementParsingContextType context)
    {
      final var codeHandlerS =
        Blackthorne.forScalarString(CODE);
      final var messageHandlerS =
        Blackthorne.forScalarString(MESSAGE);
      final var resourceS =
        Blackthorne.forScalarString(RESOURCE);
      final var requestIdS =
        Blackthorne.forScalarString(REQUEST_ID);

      final var codeHandler =
        Blackthorne.widenConstructor(
          Blackthorne.mapConstructor(codeHandlerS, Code::new)
        );
      final var messageHandler =
        Blackthorne.widenConstructor(
          Blackthorne.mapConstructor(messageHandlerS, Message::new)
        );
      final var resourceHandler =
        Blackthorne.widenConstructor(
          Blackthorne.mapConstructor(resourceS, Resource::new)
        );
      final var requestIdHandler =
        Blackthorne.widenConstructor(
          Blackthorne.mapConstructor(requestIdS, RequestID::new)
        );

      return Map.ofEntries(
        Map.entry(CODE, codeHandler),
        Map.entry(MESSAGE, messageHandler),
        Map.entry(RESOURCE, resourceHandler),
        Map.entry(REQUEST_ID, requestIdHandler)
      );
    }

    @Override
    public void onChildValueProduced(
      final BTElementParsingContextType context,
      final ErrorFieldType result)
    {
      if (result instanceof final Code r) {
        this.code = r.value;
      } else if (result instanceof final Message r) {
        this.message = r.value;
      } else if (result instanceof final RequestID r) {
        this.requestId = r.value;
      } else if (result instanceof final Resource r) {
        this.resource = r.value;
      }
    }

    @Override
    public OError onElementFinished(
      final BTElementParsingContextType context)
    {
      return new OError(
        this.code,
        this.message,
        this.resource,
        this.requestId
      );
    }

    sealed interface ErrorFieldType
    {

    }

    record Code(
      String value)
      implements ErrorFieldType
    {

    }

    record Message(
      String value)
      implements ErrorFieldType
    {

    }

    record Resource(
      String value)
      implements ErrorFieldType
    {

    }

    record RequestID(
      String value)
      implements ErrorFieldType
    {

    }
  }
}
