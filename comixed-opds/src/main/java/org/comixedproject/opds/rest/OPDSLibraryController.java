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

package org.comixedproject.opds.rest;

import static org.comixedproject.opds.model.OPDSAcquisitionFeed.ACQUISITION_FEED_LINK_TYPE;
import static org.comixedproject.opds.model.OPDSNavigationFeed.NAVIGATION_FEED_LINK_TYPE;

import lombok.extern.log4j.Log4j2;
import org.comixedproject.auditlog.rest.AuditableRestEndpoint;
import org.comixedproject.opds.model.OPDSLink;
import org.comixedproject.opds.model.OPDSNavigationFeed;
import org.comixedproject.opds.model.OPDSNavigationFeedContent;
import org.comixedproject.opds.model.OPDSNavigationFeedEntry;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <code>OPDSLibraryController</code> provides the web interface for accessing the OPDS feeds.
 *
 * @author Darryl L. Pierce
 */
@RestController
@Log4j2
public class OPDSLibraryController {
  public static final String SUBSECTION = "subsection";
  public static final String SELF = "self";
  private static final String ROOT_ID = "1";
  private static final String LIBRARY_ID = "10";
  static final long PUBLISHERS_ID = 11L;
  static final long SERIES_ID = 12L;
  static final long CHARACTERS_ID = 13L;
  static final long TEAMS_ID = 14L;
  static final long LOCATIONS_ID = 15L;
  static final long STORIES_ID = 16L;

  /**
   * Returns the root feed.
   *
   * @return the feed
   */
  @GetMapping(value = "/opds", produces = MediaType.APPLICATION_XML_VALUE)
  @AuditableRestEndpoint(logResponse = true)
  @PreAuthorize("hasRole('READER')")
  @ResponseBody
  public OPDSNavigationFeed getRootFeed() {
    log.info("Fetching root navigation feed");
    final OPDSNavigationFeed response = new OPDSNavigationFeed("Comixed Comics Catalog", ROOT_ID);
    response.getLinks().add(new OPDSLink(NAVIGATION_FEED_LINK_TYPE, SELF, "/opds/"));
    log.trace("Adding library root feed");
    OPDSNavigationFeedEntry entry;
    // add the library link
    entry = new OPDSNavigationFeedEntry("Library", "1");
    entry.setContent(new OPDSNavigationFeedContent("The library root"));
    entry.getLinks().add(new OPDSLink(ACQUISITION_FEED_LINK_TYPE, SUBSECTION, "/opds/library/"));
    response.getEntries().add(entry);
    // add the reading lists link
    entry = new OPDSNavigationFeedEntry("Reading Lists", "2");
    entry.setContent(new OPDSNavigationFeedContent("Your personal reading lists"));
    entry.getLinks().add(new OPDSLink(ACQUISITION_FEED_LINK_TYPE, SUBSECTION, "/opds/lists/"));
    response.getEntries().add(entry);
    // add the cover date
    entry = new OPDSNavigationFeedEntry("Cover Date", "3");
    entry.setContent(new OPDSNavigationFeedContent("Comics Week Year & Week"));
    entry.getLinks().add(new OPDSLink(ACQUISITION_FEED_LINK_TYPE, SUBSECTION, "/opds/dates/years"));
    response.getEntries().add(entry);
    return response;
  }

  /**
   * Returns the root library feed.
   *
   * @return the feed
   */
  @GetMapping(value = "/opds/library", produces = MediaType.APPLICATION_XML_VALUE)
  @AuditableRestEndpoint(logResponse = true)
  @PreAuthorize("hasRole('READER')")
  @ResponseBody
  public OPDSNavigationFeed getLibraryFeed() {
    log.info("Fetching the library root feed");
    final OPDSNavigationFeed response = new OPDSNavigationFeed("Library", LIBRARY_ID);
    response.getLinks().add(new OPDSLink(NAVIGATION_FEED_LINK_TYPE, SELF, "/opds/library/"));
    OPDSNavigationFeedEntry entry;
    log.trace("Adding publishers link");
    entry = new OPDSNavigationFeedEntry("Publishers", String.valueOf(PUBLISHERS_ID));
    entry.setContent(new OPDSNavigationFeedContent("All Publishers"));
    entry
        .getLinks()
        .add(new OPDSLink(ACQUISITION_FEED_LINK_TYPE, SUBSECTION, "/opds/collections/publishers/"));
    response.getEntries().add(entry);

    log.trace("Adding series link");
    entry = new OPDSNavigationFeedEntry("Series", String.valueOf(SERIES_ID));
    entry.setContent(new OPDSNavigationFeedContent("All Series"));
    entry
        .getLinks()
        .add(new OPDSLink(ACQUISITION_FEED_LINK_TYPE, SUBSECTION, "/opds/collections/series/"));
    response.getEntries().add(entry);

    log.trace("Adding characters link");
    entry = new OPDSNavigationFeedEntry("Characters", String.valueOf(CHARACTERS_ID));
    entry.setContent(new OPDSNavigationFeedContent("All Characters"));
    entry
        .getLinks()
        .add(new OPDSLink(ACQUISITION_FEED_LINK_TYPE, SUBSECTION, "/opds/collections/characters/"));
    response.getEntries().add(entry);

    log.trace("Adding teams link");
    entry = new OPDSNavigationFeedEntry("Teams", String.valueOf(TEAMS_ID));
    entry.setContent(new OPDSNavigationFeedContent("All Teams"));
    entry
        .getLinks()
        .add(new OPDSLink(ACQUISITION_FEED_LINK_TYPE, SUBSECTION, "/opds/collections/teams/"));
    response.getEntries().add(entry);

    log.trace("Adding locations link");
    entry = new OPDSNavigationFeedEntry("Locations", String.valueOf(LOCATIONS_ID));
    entry.setContent(new OPDSNavigationFeedContent("All Locations"));
    entry
        .getLinks()
        .add(new OPDSLink(ACQUISITION_FEED_LINK_TYPE, SUBSECTION, "/opds/collections/locations/"));
    response.getEntries().add(entry);

    log.trace("Adding stories link");
    entry = new OPDSNavigationFeedEntry("Stories", String.valueOf(STORIES_ID));
    entry.setContent(new OPDSNavigationFeedContent("All Stories"));
    entry
        .getLinks()
        .add(new OPDSLink(ACQUISITION_FEED_LINK_TYPE, SUBSECTION, "/opds/collections/stories/"));
    response.getEntries().add(entry);
    return response;
  }
}
