/**
 * This file Copyright (c) 2005-2010 Aptana, Inc. This program is
 * dual-licensed under both the Aptana Public License and the GNU General
 * Public license. You may elect to use one or the other of these licenses.
 * 
 * This program is distributed in the hope that it will be useful, but
 * AS-IS and WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, TITLE, or
 * NONINFRINGEMENT. Redistribution, except as permitted by whichever of
 * the GPL or APL you select, is prohibited.
 *
 * 1. For the GPL license (GPL), you can redistribute and/or modify this
 * program under the terms of the GNU General Public License,
 * Version 3, as published by the Free Software Foundation.  You should
 * have received a copy of the GNU General Public License, Version 3 along
 * with this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Aptana provides a special exception to allow redistribution of this file
 * with certain other free and open source software ("FOSS") code and certain additional terms
 * pursuant to Section 7 of the GPL. You may view the exception and these
 * terms on the web at http://www.aptana.com/legal/gpl/.
 * 
 * 2. For the Aptana Public License (APL), this program and the
 * accompanying materials are made available under the terms of the APL
 * v1.0 which accompanies this distribution, and is available at
 * http://www.aptana.com/legal/apl/.
 * 
 * You may view the GPL, Aptana's exception and additional terms, and the
 * APL in the file titled license.html at the root of the corresponding
 * plugin containing this source file.
 * 
 * Any modifications to this file must keep this entire header intact.
 */
package com.aptana.ide.filesystem.s3;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.IFileTree;
import org.eclipse.core.filesystem.provider.FileSystem;
import org.eclipse.core.runtime.IProgressMonitor;

public class S3FileSystem extends FileSystem
{

	@Override
	public IFileStore getStore(URI uri)
	{
		return new S3FileStore(uri);
	}

	@Override
	public boolean canDelete()
	{
		return true;
	}

	@Override
	public boolean canWrite()
	{
		return true;
	}

	@Override
	public IFileTree fetchFileTree(IFileStore root, IProgressMonitor monitor)
	{
		if (!(root instanceof S3FileStore))
		{
			return null;
		}
		try
		{
			S3FileStore s3Store = (S3FileStore) root;
			if (monitor != null && monitor.isCanceled())
			{
				return null;
			}
			// FIXME What about when s3Store is the absolute root (not in a bucket)?!
			return new S3FileTree(root, s3Store.listEntries());
		}
		catch (MalformedURLException e)
		{
			S3FileSystemPlugin.log(e);
		}
		catch (IOException e)
		{
			S3FileSystemPlugin.log(e);
		}
		return null;
	}
}