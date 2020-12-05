/*
 * ComiXed - A digital comic book library management application.
 * Copyright (C) 2020, The ComiXed Project
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

import { API_ROOT_URL } from '../core';

export const QUERY_PARAM_TAB = 'tab';

export const UNKNOWN_VALUE_PLACEHOLDER = 'UNKNOWN';

export const MISSING_COMIC_IMAGE_URL = '/assets/img/missing-comic-file.png';
export const GET_COMIC_COVER_URL = `${API_ROOT_URL}/comics/\${id}/cover/content`;
export const GET_PAGE_CONTENT_URL = `${API_ROOT_URL}/pages/\${id}/content`;

export const LOAD_COMIC_FILES_URL = `${API_ROOT_URL}/files/contents`;
export const SEND_COMIC_FILES_URL = `${API_ROOT_URL}/files/import`;

export const LOAD_COMIC_URL = `${API_ROOT_URL}/comics/\${id}`;
