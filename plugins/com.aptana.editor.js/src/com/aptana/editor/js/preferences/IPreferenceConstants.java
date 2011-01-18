/**
 * Aptana Studio
 * Copyright (c) 2005-2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package com.aptana.editor.js.preferences;

public interface IPreferenceConstants
{
	/**
	 * The value is a boolean to indicate if "*" should be used for multiline comment indenting
	 */
	String COMMENT_INDENT_USE_STAR = "COMMENT_INDENT_USE_STAR"; //$NON-NLS-1$

	/**
	 * The value is a boolean to indicate if we auto-indent on carriage return
	 */
	String AUTO_INDENT_ON_CARRIAGE_RETURN = "AUTO_INDENT_ON_CARRIAGE_RETURN"; //$NON-NLS-1$

	/**
	 * The value is a double used to indicate the current format being used by the JS index categories.
	 */
	String JS_INDEX_VERSION = "JS_INDEX_VERSION"; //$NON-NLS-1$
}
