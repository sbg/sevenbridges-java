/*
 * Copyright 2017 Seven Bridges Genomics, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sevenbridges.apiclient.impl.upload;

import com.sevenbridges.apiclient.upload.CreateUploadRequest;
import com.sevenbridges.apiclient.upload.Upload;

import java.io.RandomAccessFile;

public class DefaultCreateUploadRequest implements CreateUploadRequest {

  private final Upload upload;
  private final RandomAccessFile file;
  private final boolean overwrite;

  DefaultCreateUploadRequest(Upload upload, RandomAccessFile file, boolean overwrite) {
    this.upload = upload;
    this.file = file;
    this.overwrite = overwrite;
  }

  @Override
  public Upload getUpload() {
    return upload;
  }

  @Override
  public RandomAccessFile getFile() {
    return file;
  }

  @Override
  public boolean getOverwrite() {
    return overwrite;
  }
}
