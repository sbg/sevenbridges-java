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
package com.sevenbridges.apiclient.upload;

import java.io.RandomAccessFile;

/**
 * Represents an attempt to create a new {@link Upload} on Seven Bridges.
 *
 * @see CreateUploadRequestBuilder
 */
public interface CreateUploadRequest {

  /**
   * Returns the Upload instance for which a new record will be created in Seven Bridges.
   *
   * @return the Upload instance for which a new record will be created in Seven Bridges.
   */
  Upload getUpload();

  /**
   * Local file to be uploaded.
   *
   * @return RandomAccessFile of local {@link java.io.File}
   */
  RandomAccessFile getFile();

  /**
   * Overwrite param of the upload. If true, upload will overwrite files with the same name as this
   * upload. Alternative is the renaming of the uploaded file.
   *
   * @return boolean overwrite flag
   */
  boolean getOverwrite();

}
