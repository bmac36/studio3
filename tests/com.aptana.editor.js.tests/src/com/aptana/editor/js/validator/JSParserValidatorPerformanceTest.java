/**
 * Aptana Studio
 * Copyright (c) 2012 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package com.aptana.editor.js.validator;

import java.io.File;
import java.net.URL;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.test.performance.PerformanceTestCase;

import com.aptana.core.build.AbstractBuildParticipant;
import com.aptana.core.util.ResourceUtil;
import com.aptana.editor.epl.tests.EditorTestHelper;
import com.aptana.editor.js.JSPlugin;
import com.aptana.editor.js.parsing.JSParser;
import com.aptana.index.core.FileStoreBuildContext;
import com.aptana.index.core.build.BuildContext;
import com.aptana.parsing.IParseState;
import com.aptana.parsing.ParseResult;
import com.aptana.parsing.WorkingParseResult;

public class JSParserValidatorPerformanceTest extends PerformanceTestCase
{
	private AbstractBuildParticipant validator;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();

		validator = createValidator();
	}

	@Override
	protected void tearDown() throws Exception
	{
		validator = null;
		super.tearDown();
	}

	protected AbstractBuildParticipant createValidator()
	{
		return new JSParserValidator()
		{

			@Override
			protected String getPreferenceNode()
			{
				return JSPlugin.PLUGIN_ID;
			}

			@Override
			public String getId()
			{
				return ID;
			}
		};
	}

	protected void perfValidate(String filename, int iterations) throws Exception
	{
		// read in the file
		URL url = FileLocator.find(Platform.getBundle("com.aptana.editor.js.tests"),
				Path.fromPortableString("performance/" + filename), null);
		File file = ResourceUtil.resourcePathToFile(url);
		IFileStore fileStore = EFS.getStore(file.toURI());

		// Ok now actually validate the thing, the real work
		for (int i = 0; i < iterations; i++)
		{
			EditorTestHelper.joinBackgroundActivities();

			BuildContext context = new FileStoreBuildContext(fileStore)
			{
				@Override
				protected ParseResult parse(String contentType, IParseState parseState, WorkingParseResult working)
						throws Exception
				{
					if (reparseEveryTime())
					{
						return new JSParser().parse(parseState);
					}
					return super.parse(contentType, parseState, working);
				}
			};
			// Don't measure reading in string...
			context.getContents();

			startMeasuring();
			validator.buildFile(context, null);
			stopMeasuring();
		}
		commitMeasurements();
		assertPerformance();
	}

	protected boolean reparseEveryTime()
	{
		return false;
	}

	public void testThreeMinJS() throws Exception
	{
		perfValidate("three.min.js", 10);
	}
}
