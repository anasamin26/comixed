/*
 * ComiXed - A digital comic book library management application.
 * Copyright (C) 2023, The ComiXed Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses>
 */

package org.comixedproject.service.plugin;

/**
 * <code>LibraryPluginException</code> is thrown when an error occurs while working with plugins.
 *
 * @author Darryl L. Pierce
 */
public class LibraryPluginException extends Exception {
  /**
   * Creates an instance with a message.
   *
   * @param message the message
   */
  public LibraryPluginException(final String message) {
    super(message);
  }

  /**
   * Creates an instance with a message and cause.
   *
   * @param message the message
   * @param cause the cause
   */
  public LibraryPluginException(final String message, final Exception cause) {
    super(message, cause);
  }
}
