/*
 * Copyright 2016 Busy, LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.busybusy.answers_provider.loggers;

import com.busybusy.analyticskit_android.AnalyticsEvent;
import com.busybusy.answers_provider.Attributes;
import com.busybusy.answers_provider.PredefinedEvents;
import com.crashlytics.android.answers.PackageScopeWrappedCalls;
import com.crashlytics.android.answers.RatingEvent;

import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Trevor
 */
public class RatedContentLoggerTest extends LoggerBaseTest
{
	RatedContentLogger logger;
	AnalyticsEvent     ratedContentEvent;

	@Override
	public void setup()
	{
		super.setup();
		logger = new RatedContentLogger(answers);
		ratedContentEvent = new AnalyticsEvent(PredefinedEvents.RATED_CONTENT).putAttribute(Attributes.RatedContent.RATING, 1)
		                                                                      .putAttribute(Attributes.RatedContent.CONTENT_NAME, "ContentName")
		                                                                      .putAttribute(Attributes.RatedContent.CONTENT_TYPE, "ContentType")
		                                                                      .putAttribute(Attributes.RatedContent.CONTENT_ID, "ContentId")
		                                                                      .putAttribute(CUSTOM_KEY, CUSTOM_DATA);
	}

	@Test
	public void test_buildAnswersRatingEvent()
	{
		RatingEvent result = logger.buildAnswersRatingEvent(ratedContentEvent);

		Map<String, Object> predefinedAttributes = PackageScopeWrappedCalls.getPredefinedAttributes(result);
		assertThat(predefinedAttributes.size()).isEqualTo(4);

		assertThat(predefinedAttributes).containsKey(Attributes.RatedContent.RATING);
		assertThat(predefinedAttributes).containsKey(Attributes.RatedContent.CONTENT_NAME);
		assertThat(predefinedAttributes).containsKey(Attributes.RatedContent.CONTENT_TYPE);
		assertThat(predefinedAttributes).containsKey(Attributes.RatedContent.CONTENT_ID);

		Map<String, Object> customAttributes = PackageScopeWrappedCalls.getCustomAttributes(result);
		assertThat(customAttributes.size()).isEqualTo(1);

		assertThat(customAttributes).containsKey(CUSTOM_KEY);
	}

	@Test
	public void test_logSpecificEvent()
	{
		logger.logSpecificEvent(ratedContentEvent);

		assertThat(answers.logRatingCalled).isTrue();
		assertThat(answers.ratingEvent).isNotNull();
	}
}
