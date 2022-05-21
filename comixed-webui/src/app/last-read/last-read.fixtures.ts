/*
 * ComiXed - A digital comic book library management application.
 * Copyright (C) 2021, The ComiXed Project
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

import { LastRead } from './models/last-read';
import {
  COMIC_BOOK_1,
  COMIC_BOOK_2,
  COMIC_BOOK_3,
  COMIC_BOOK_4,
  COMIC_BOOK_5
} from '../comic-books/comic-books.fixtures';

export const LAST_READ_1: LastRead = {
  id: 1,
  comicBook: COMIC_BOOK_1,
  lastRead: new Date().getTime()
};

export const LAST_READ_2: LastRead = {
  id: 2,
  comicBook: COMIC_BOOK_2,
  lastRead: new Date().getTime()
};

export const LAST_READ_3: LastRead = {
  id: 3,
  comicBook: COMIC_BOOK_3,
  lastRead: new Date().getTime()
};

export const LAST_READ_4: LastRead = {
  id: 4,
  comicBook: COMIC_BOOK_4,
  lastRead: new Date().getTime()
};

export const LAST_READ_5: LastRead = {
  id: 5,
  comicBook: COMIC_BOOK_5,
  lastRead: new Date().getTime()
};
