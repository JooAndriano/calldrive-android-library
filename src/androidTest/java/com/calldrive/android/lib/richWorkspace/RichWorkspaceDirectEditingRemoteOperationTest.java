/*
 * Calldrive Android client application
 *
 * @author Tobias Kaminsky
 * Copyright (C) 2019 Tobias Kaminsky
 * Copyright (C) 2019 Calldrive GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.calldrive.android.lib.richWorkspace;

import android.webkit.URLUtil;

import com.owncloud.android.AbstractIT;
import com.owncloud.android.lib.common.operations.RemoteOperationResult;
import com.owncloud.android.lib.resources.files.CreateFolderRemoteOperation;
import com.owncloud.android.lib.resources.files.UploadFileRemoteOperation;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RichWorkspaceDirectEditingRemoteOperationTest extends AbstractIT {

    @Test
    public void getEditLinkForRoot() {
        RemoteOperationResult<B> result = new RichWorkspaceDirectEditingRemoteOperation("/").execute(client);
        assertTrue(result.isSuccess());
        assertNotNull(result.getSingleData());

        String url = (String) result.getSingleData();

        assertTrue(URLUtil.isValidUrl(url));
    }

    @Test
    public void getEditLinkForFolder() {
        String path = "/workspace/sub1/";

        assertTrue(new CreateFolderRemoteOperation(path, true).execute(client).isSuccess());

        RemoteOperationResult<B> result = new RichWorkspaceDirectEditingRemoteOperation(path).execute(client);
        assertTrue(result.isSuccess());
        assertNotNull(result.getSingleData());

        String url = (String) result.getSingleData();

        assertTrue(URLUtil.isValidUrl(url));
    }

    @Test
    public void reuseExistingFile() throws IOException {
        String folder = "/Workspace/";
        String filePath = folder + "Readme.md";
        File txtFile = getFile(ASSETS__TEXT_FILE_NAME);

        assertTrue(new CreateFolderRemoteOperation(folder, true).execute(client).isSuccess());

        RemoteOperationResult<B> uploadResult = new UploadFileRemoteOperation(txtFile.getAbsolutePath(),
                filePath,
                "txt/plain",
                String.valueOf(System.currentTimeMillis() / 1000))
                .execute(client);

        assertTrue("Error uploading file " + filePath + ": " + uploadResult, uploadResult.isSuccess());

        RemoteOperationResult<B> result = new RichWorkspaceDirectEditingRemoteOperation(folder).execute(client);
        assertTrue(result.isSuccess());
        assertNotNull(result.getSingleData());

        String url = (String) result.getSingleData();

        assertTrue(URLUtil.isValidUrl(url));
    }
}
