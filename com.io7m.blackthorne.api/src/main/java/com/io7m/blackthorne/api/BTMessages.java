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

package com.io7m.blackthorne.api;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Localized messages.
 */

public final class BTMessages
{
  private static final ResourceBundle RESOURCES =
    ResourceBundle.getBundle("com.io7m.blackthorne.api.Messages");

  private BTMessages()
  {

  }

  /**
   * Format a string.
   *
   * @param resource  The resource ID
   * @param arguments The arguments to the format string
   *
   * @return A formatted string
   */

  public static String format(
    final String resource,
    final Object... arguments)
  {
    Objects.requireNonNull(resource, "resource");
    Objects.requireNonNull(arguments, "arguments");
    return MessageFormat.format(RESOURCES.getString(resource), arguments);
  }
}
